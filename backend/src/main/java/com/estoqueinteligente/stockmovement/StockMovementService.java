package com.estoqueinteligente.stockmovement;

import com.estoqueinteligente.common.BusinessException;
import com.estoqueinteligente.common.ResourceNotFoundException;
import com.estoqueinteligente.exittype.ExitTypeService;
import com.estoqueinteligente.product.Product;
import com.estoqueinteligente.product.ProductRepository;
import com.estoqueinteligente.product.ProductService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockMovementService {
    private final StockMovementRepository repository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final ExitTypeService exitTypeService;

    public StockMovementService(StockMovementRepository repository,ProductRepository productRepository,ProductService productService,ExitTypeService exitTypeService){
        this.repository=repository;
        this.productRepository=productRepository;
        this.productService=productService;
        this.exitTypeService=exitTypeService;
    }

    @Transactional(readOnly=true)
    public List<StockMovementResponse> findAll(){
        return repository.findAllWithProduct().stream().map(StockMovementResponse::from).toList();
    }

    @Transactional(readOnly=true)
    public StockMovementResponse findById(Long id){
        return StockMovementResponse.from(repository.findByIdWithProduct(id).orElseThrow(()->new ResourceNotFoundException("Movimentacao nao encontrada")));
    }

    @Transactional(readOnly=true)
    public List<StockMovementResponse> findByProduct(Long productId){
        if(!productRepository.existsById(productId))throw new ResourceNotFoundException("Item nao encontrado");
        return repository.findByProductId(productId).stream().map(StockMovementResponse::from).toList();
    }

    @Transactional
    public StockMovementResponse create(StockMovementRequest request){
        Product product=productRepository.findByIdForUpdate(request.getProductId()).orElseThrow(()->new ResourceNotFoundException("Item nao encontrado"));
        int previous=product.getQuantity();
        BigDecimal unitValue=resolveUnitValue(request,product);
        int next=calculateNewQuantity(request,previous);
        BigDecimal totalValue=applyFinancialMovement(product,request,previous,next,unitValue);

        product.setQuantity(next);
        product.setCountPending(false);
        product.setStatus(productService.calculateStatus(product));
        productRepository.save(product);

        StockMovement movement=new StockMovement();
        movement.setProduct(product);
        movement.setMovementType(request.getMovementType());
        movement.setQuantity(request.getQuantity());
        movement.setPreviousQuantity(previous);
        movement.setNewQuantity(next);
        movement.setUnitValue(unitValue);
        movement.setTotalValue(totalValue);
        movement.setExitType(request.getExitTypeId()==null?null:exitTypeService.getEntity(request.getExitTypeId()));
        movement.setReason(normalize(request.getReason()));
        movement.setResponsibleName(normalize(request.getResponsibleName()));
        movement.setPurpose(normalize(request.getPurpose()));
        movement.setEventName(normalize(request.getEventName()));
        movement.setNotes(normalize(request.getNotes()));
        return StockMovementResponse.from(repository.save(movement));
    }

    private int calculateNewQuantity(StockMovementRequest request,int previous){
        if(request.getMovementType()==StockMovementType.ENTRADA)return previous+request.getQuantity();
        if(request.getMovementType()==StockMovementType.AJUSTE)return request.getQuantity();
        if(request.getQuantity()>previous)throw new BusinessException("Quantidade insuficiente. Disponivel: "+previous+" unidade(s)");
        return previous-request.getQuantity();
    }

    private BigDecimal resolveUnitValue(StockMovementRequest request,Product product){
        if(request.getMovementType()==StockMovementType.AJUSTE&&request.getQuantity()<product.getQuantity())return money(product.getAverageCost());
        if(request.getUnitValue()!=null)return request.getUnitValue();
        if(request.getMovementType()==StockMovementType.ENTRADA)return money(product.getPurchaseValue());
        if(request.getMovementType()==StockMovementType.SAIDA){
            return money(product.getAverageCost());
        }
        if(request.getMovementType()==StockMovementType.AJUSTE){
            int current=product.getQuantity();
            int requested=request.getQuantity();
            if(requested>current)return money(product.getPurchaseValue());
            if(requested<current)return money(product.getAverageCost());
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal applyFinancialMovement(Product product,StockMovementRequest request,int previous,int next,BigDecimal unitValue){
        BigDecimal currentValue=money(product.getCurrentStockValue());
        BigDecimal totalValue;
        if(request.getMovementType()==StockMovementType.ENTRADA){
            totalValue=unitValue.multiply(BigDecimal.valueOf(request.getQuantity()));
            currentValue=currentValue.add(totalValue);
        }else if(request.getMovementType()==StockMovementType.SAIDA){
            totalValue=unitValue.multiply(BigDecimal.valueOf(request.getQuantity()));
            currentValue=currentValue.subtract(totalValue);
        }else{
            int difference=next-previous;
            if(next==0){
                totalValue=currentValue;
                currentValue=BigDecimal.ZERO;
            }else if(difference>0){
                totalValue=unitValue.multiply(BigDecimal.valueOf(difference));
                currentValue=currentValue.add(totalValue);
            }else if(difference<0){
                totalValue=money(product.getAverageCost()).multiply(BigDecimal.valueOf(Math.abs(difference)));
                currentValue=currentValue.subtract(totalValue);
            }else{
                totalValue=BigDecimal.ZERO;
            }
        }
        if(next<=0){
            product.setCurrentStockValue(BigDecimal.ZERO);
            product.setAverageCost(BigDecimal.ZERO);
            return totalValue;
        }
        if(currentValue.compareTo(BigDecimal.ZERO)<0)currentValue=BigDecimal.ZERO;
        product.setCurrentStockValue(currentValue);
        product.setAverageCost(currentValue.divide(BigDecimal.valueOf(next),2,RoundingMode.HALF_UP));
        return totalValue;
    }

    private BigDecimal money(BigDecimal value){return value==null?BigDecimal.ZERO:value;}
    private String normalize(String value){return value==null||value.isBlank()?null:value.trim();}
}
