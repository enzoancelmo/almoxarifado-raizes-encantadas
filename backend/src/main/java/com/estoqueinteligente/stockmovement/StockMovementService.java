package com.estoqueinteligente.stockmovement;

import com.estoqueinteligente.common.BusinessException;
import com.estoqueinteligente.common.ResourceNotFoundException;
import com.estoqueinteligente.exittype.ExitTypeService;
import com.estoqueinteligente.product.Product;
import com.estoqueinteligente.product.ProductRepository;
import com.estoqueinteligente.product.ProductService;
import java.math.BigDecimal;
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
        int next=calculateNewQuantity(request,previous);

        product.setQuantity(next);
        product.setCountPending(false);
        product.setStatus(productService.calculateStatus(product));
        productRepository.save(product);

        BigDecimal unitValue=resolveUnitValue(request,product);
        BigDecimal totalValue=unitValue.multiply(BigDecimal.valueOf(request.getQuantity()));

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
        if(request.getUnitValue()!=null)return request.getUnitValue();
        if(request.getMovementType()==StockMovementType.ENTRADA)return money(product.getPurchaseValue());
        if(request.getMovementType()==StockMovementType.SAIDA){
            BigDecimal exit=money(product.getExitValue());
            return exit.compareTo(BigDecimal.ZERO)>0?exit:money(product.getPurchaseValue());
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal money(BigDecimal value){return value==null?BigDecimal.ZERO:value;}
    private String normalize(String value){return value==null||value.isBlank()?null:value.trim();}
}
