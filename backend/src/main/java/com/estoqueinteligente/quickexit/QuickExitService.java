package com.estoqueinteligente.quickexit;

import com.estoqueinteligente.common.BusinessException;
import com.estoqueinteligente.common.ResourceNotFoundException;
import com.estoqueinteligente.exittype.ExitType;
import com.estoqueinteligente.eventtemplate.EventTemplate;
import com.estoqueinteligente.exittype.ExitTypeService;
import com.estoqueinteligente.product.Product;
import com.estoqueinteligente.product.ProductRepository;
import com.estoqueinteligente.product.ProductService;
import com.estoqueinteligente.stockmovement.StockMovement;
import com.estoqueinteligente.stockmovement.StockMovementRepository;
import com.estoqueinteligente.stockmovement.StockMovementResponse;
import com.estoqueinteligente.stockmovement.StockMovementType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuickExitService {
    private final StockExitBatchRepository batchRepository;
    private final StockMovementRepository movementRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final ExitTypeService exitTypeService;
    private final com.estoqueinteligente.eventtemplate.EventTemplateService eventTemplateService;

    public QuickExitService(StockExitBatchRepository batchRepository,StockMovementRepository movementRepository,ProductRepository productRepository,ProductService productService,ExitTypeService exitTypeService,com.estoqueinteligente.eventtemplate.EventTemplateService eventTemplateService){this.batchRepository=batchRepository;this.movementRepository=movementRepository;this.productRepository=productRepository;this.productService=productService;this.exitTypeService=exitTypeService;this.eventTemplateService=eventTemplateService;}

    @Transactional
    public QuickExitResponse create(QuickExitRequest request){
        if(request.getItems()==null||request.getItems().isEmpty())throw new BusinessException("Informe pelo menos um item para a saída.");
        ExitType exitType=exitTypeService.getEntity(request.getExitTypeId());
        EventTemplate eventTemplate=request.getEventTemplateId()==null?null:eventTemplateService.getEntity(request.getEventTemplateId());
        List<AggregatedItem> items=aggregate(request.getItems());
        Map<Long,Product> products=loadAndValidateProducts(items);

        StockExitBatch batch=new StockExitBatch();
        batch.setEventName(request.getEventName().trim());
        batch.setExitType(exitType);
        batch.setEventTemplate(eventTemplate);
        batch.setResponsibleName(normalize(request.getResponsibleName()));
        batch.setExitDate(request.getExitDate());
        batch.setNotes(normalize(request.getNotes()));
        batch=batchRepository.save(batch);

        BigDecimal totalValue=BigDecimal.ZERO;
        int totalQuantity=0;
        List<StockMovementResponse> movements=new ArrayList<>();
        for(AggregatedItem item:items){
            Product product=products.get(item.itemId());
            BigDecimal unitValue=resolveUnitValue(item.unitValue(),product);
            BigDecimal itemTotal=unitValue.multiply(BigDecimal.valueOf(item.quantity())).setScale(2,RoundingMode.HALF_UP);
            totalValue=totalValue.add(itemTotal);
            totalQuantity+=item.quantity();

            int previous=product.getQuantity();
            int next=previous-item.quantity();
            BigDecimal currentValue=money(product.getCurrentStockValue()).subtract(itemTotal);
            if(next<=0){product.setCurrentStockValue(BigDecimal.ZERO);product.setAverageCost(BigDecimal.ZERO);} else {if(currentValue.compareTo(BigDecimal.ZERO)<0)currentValue=BigDecimal.ZERO;product.setCurrentStockValue(currentValue);product.setAverageCost(currentValue.divide(BigDecimal.valueOf(next),2,RoundingMode.HALF_UP));}
            product.setQuantity(next);
            product.setCountPending(false);
            product.setStatus(productService.calculateStatus(product));
            productRepository.save(product);

            StockMovement movement=new StockMovement();
            movement.setProduct(product);
            movement.setMovementType(StockMovementType.SAIDA);
            movement.setQuantity(item.quantity());
            movement.setPreviousQuantity(previous);
            movement.setNewQuantity(next);
            movement.setUnitValue(unitValue);
            movement.setTotalValue(itemTotal);
            movement.setExitType(exitType);
            movement.setExitBatch(batch);
            movement.setReason("Saída rápida");
            movement.setResponsibleName(normalize(request.getResponsibleName()));
            movement.setEventName(request.getEventName().trim());
            movement.setNotes(normalize(item.notes())==null?normalize(request.getNotes()):normalize(item.notes()));
            movements.add(StockMovementResponse.from(movementRepository.save(movement)));
        }

        batch.setTotalDifferentItems(items.size());
        batch.setTotalQuantity(totalQuantity);
        batch.setTotalValue(totalValue);
        batch=batchRepository.save(batch);
        return new QuickExitResponse(batch.getId(),batch.getEventName(),eventTemplate==null?null:eventTemplate.getId(),eventTemplate==null?null:eventTemplate.getName(),exitType.getName(),batch.getResponsibleName(),batch.getExitDate(),batch.getNotes(),items.size(),totalQuantity,totalValue,movements);
    }

    private List<AggregatedItem> aggregate(List<QuickExitRequest.Item> rawItems){
        Map<Long,AggregatedItem> map=new LinkedHashMap<>();
        for(QuickExitRequest.Item raw:rawItems){
            if(raw.getItemId()==null)throw new BusinessException("Existe uma linha sem item selecionado.");
            if(raw.getQuantity()==null||raw.getQuantity()<=0)throw new BusinessException("Quantidade deve ser maior que zero.");
            AggregatedItem current=map.get(raw.getItemId());
            if(current==null){map.put(raw.getItemId(),new AggregatedItem(raw.getItemId(),raw.getQuantity(),raw.getUnitValue(),normalize(raw.getNotes())));}
            else{
                int quantity=current.quantity()+raw.getQuantity();
                BigDecimal unitValue=mergeUnitValue(current,raw);
                String notes=mergeNotes(current.notes(),normalize(raw.getNotes()));
                map.put(raw.getItemId(),new AggregatedItem(raw.getItemId(),quantity,unitValue,notes));
            }
        }
        return map.values().stream().sorted(Comparator.comparing(AggregatedItem::itemId)).toList();
    }

    private Map<Long,Product> loadAndValidateProducts(List<AggregatedItem> items){
        Map<Long,Product> products=new LinkedHashMap<>();
        for(AggregatedItem item:items){
            Product product=productRepository.findByIdForUpdate(item.itemId()).orElseThrow(()->new ResourceNotFoundException("Item não encontrado: "+item.itemId()));
            if(item.quantity()>product.getQuantity())throw new BusinessException("Estoque insuficiente para "+product.getName()+". Disponível: "+product.getQuantity()+". Solicitado: "+item.quantity()+".");
            products.put(item.itemId(),product);
        }
        return products;
    }

    private BigDecimal resolveUnitValue(BigDecimal requested,Product product){
        if(requested!=null)return requested;
        if(money(product.getAverageCost()).compareTo(BigDecimal.ZERO)>0)return money(product.getAverageCost());
        if(money(product.getExitValue()).compareTo(BigDecimal.ZERO)>0)return money(product.getExitValue());
        if(money(product.getPurchaseValue()).compareTo(BigDecimal.ZERO)>0)return money(product.getPurchaseValue());
        return BigDecimal.ZERO;
    }

    private BigDecimal mergeUnitValue(AggregatedItem current,QuickExitRequest.Item raw){
        if(current.unitValue()==null)return raw.getUnitValue();
        if(raw.getUnitValue()==null)return current.unitValue();
        BigDecimal currentTotal=current.unitValue().multiply(BigDecimal.valueOf(current.quantity()));
        BigDecimal newTotal=raw.getUnitValue().multiply(BigDecimal.valueOf(raw.getQuantity()));
        return currentTotal.add(newTotal).divide(BigDecimal.valueOf(current.quantity()+raw.getQuantity()),2,RoundingMode.HALF_UP);
    }

    private String mergeNotes(String a,String b){if(a==null||a.isBlank())return b;if(b==null||b.isBlank())return a;return a+" | "+b;}
    private BigDecimal money(BigDecimal value){return value==null?BigDecimal.ZERO:value;}
    private String normalize(String value){return value==null||value.isBlank()?null:value.trim();}

    private record AggregatedItem(Long itemId,int quantity,BigDecimal unitValue,String notes){}
}