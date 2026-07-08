package com.estoqueinteligente.quickentry;

import com.estoqueinteligente.common.BusinessException;
import com.estoqueinteligente.common.ResourceNotFoundException;
import com.estoqueinteligente.product.*;
import com.estoqueinteligente.stockmovement.*;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuickEntryService {
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final StockMovementService movementService;

    public QuickEntryService(ProductService productService,ProductRepository productRepository,StockMovementService movementService){this.productService=productService;this.productRepository=productRepository;this.movementService=movementService;}

    @Transactional
    public QuickEntryResponse create(QuickEntryRequest request){
        if(request.getExistingItemId()!=null)return entryExisting(request);
        return entryNewItem(request);
    }

    private QuickEntryResponse entryExisting(QuickEntryRequest request){
        Product product=productRepository.findByIdWithCategory(request.getExistingItemId()).orElseThrow(()->new ResourceNotFoundException("Item não encontrado"));
        StockMovementResponse movement=movementService.create(movementRequest(product.getId(),request,request.getUnitValue()));
        return new QuickEntryResponse(productService.findById(product.getId()),movement,"Entrada registrada com sucesso.");
    }

    private QuickEntryResponse entryNewItem(QuickEntryRequest request){
        if(request.getNewItem()==null)throw new BusinessException("Dados do novo item são obrigatórios");
        if(request.getUnitValue()==null)throw new BusinessException("Valor unitário é obrigatório para novo item");
        QuickEntryRequest.NewItemRequest n=request.getNewItem();
        ProductRequest product=new ProductRequest();
        product.setName(n.getName());product.setBrand(n.getBrand());product.setEntityPurpose(n.getEntityPurpose());product.setUnitOfMeasure(n.getUnitOfMeasure());product.setCategoryId(n.getCategoryId());
        product.setCurrentQuantity(0.0);product.setMonthlyRequiredQuantity(Double.valueOf(n.getMonthlyRequiredQuantity()));product.setPurchaseValue(request.getUnitValue());product.setExitValue(request.getUnitValue());product.setExpirationDate(n.getExpirationDate());product.setCountPending(false);product.setNotes(request.getNotes());
        ProductResponse created=productService.create(product);
        StockMovementResponse movement=movementService.create(movementRequest(created.getId(),request,request.getUnitValue()));
        return new QuickEntryResponse(productService.findById(created.getId()),movement,"Item criado e entrada registrada com sucesso.");
    }

    private StockMovementRequest movementRequest(Long productId,QuickEntryRequest request,BigDecimal unitValue){
        StockMovementRequest movement=new StockMovementRequest();
        movement.setProductId(productId);movement.setMovementType(StockMovementType.ENTRADA);movement.setQuantity(request.getQuantity());movement.setUnitValue(unitValue);movement.setEntryTypeId(request.getEntryTypeId());
        movement.setReason("Entrada rápida");movement.setResponsibleName(request.getResponsibleName());movement.setPurpose(request.getEntryOrigin());movement.setNotes(request.getNotes());
        return movement;
    }
}
