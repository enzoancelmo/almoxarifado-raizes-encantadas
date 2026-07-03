package com.estoqueinteligente.stockmovement;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.estoqueinteligente.common.*;
import com.estoqueinteligente.product.*;

@Service
public class StockMovementService {
    private final StockMovementRepository repository; private final ProductRepository productRepository; private final ProductService productService;
    public StockMovementService(StockMovementRepository repository,ProductRepository productRepository,ProductService productService){this.repository=repository;this.productRepository=productRepository;this.productService=productService;}
    @Transactional(readOnly=true) public List<StockMovementResponse> findAll(){return repository.findAllWithProduct().stream().map(StockMovementResponse::from).toList();}
    @Transactional(readOnly=true) public StockMovementResponse findById(Long id){return StockMovementResponse.from(repository.findByIdWithProduct(id).orElseThrow(()->new ResourceNotFoundException("Movimentação não encontrada")));}
    @Transactional(readOnly=true) public List<StockMovementResponse> findByProduct(Long productId){
        if(!productRepository.existsById(productId))throw new ResourceNotFoundException("Produto não encontrado");
        return repository.findByProductId(productId).stream().map(StockMovementResponse::from).toList();
    }
    @Transactional public StockMovementResponse create(StockMovementRequest request){
        Product product=productRepository.findByIdForUpdate(request.getProductId()).orElseThrow(()->new ResourceNotFoundException("Produto não encontrado"));
        int previous=product.getQuantity(); int next=calculateNewQuantity(request,previous);
        product.setQuantity(next); product.setStatus(productService.calculateStatus(product)); productRepository.save(product);
        StockMovement movement=new StockMovement(); movement.setProduct(product); movement.setMovementType(request.getMovementType()); movement.setQuantity(request.getQuantity()); movement.setPreviousQuantity(previous); movement.setNewQuantity(next); movement.setReason(normalizeReason(request.getReason()));
        return StockMovementResponse.from(repository.save(movement));
    }
    private int calculateNewQuantity(StockMovementRequest request,int previous){
        return switch(request.getMovementType()){
            case ENTRADA -> previous+request.getQuantity();
            case AJUSTE -> request.getQuantity();
            case SAIDA -> {if(request.getQuantity()>previous)throw new BusinessException("Estoque insuficiente. Disponível: "+previous+" unidade(s)");yield previous-request.getQuantity();}
        };
    }
    private String normalizeReason(String reason){return reason==null||reason.isBlank()?null:reason.trim();}
}
