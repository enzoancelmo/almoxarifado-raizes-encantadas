package com.estoqueinteligente.stockmovement;

import java.time.Instant;

public class StockMovementResponse {
    private final Long id; private final Long productId; private final String productName; private final StockMovementType movementType;
    private final Integer quantity; private final Integer previousQuantity; private final Integer newQuantity; private final String reason; private final Instant createdAt;
    public StockMovementResponse(Long id,Long productId,String productName,StockMovementType movementType,Integer quantity,Integer previousQuantity,Integer newQuantity,String reason,Instant createdAt){this.id=id;this.productId=productId;this.productName=productName;this.movementType=movementType;this.quantity=quantity;this.previousQuantity=previousQuantity;this.newQuantity=newQuantity;this.reason=reason;this.createdAt=createdAt;}
    public static StockMovementResponse from(StockMovement m){return new StockMovementResponse(m.getId(),m.getProduct().getId(),m.getProduct().getName(),m.getMovementType(),m.getQuantity(),m.getPreviousQuantity(),m.getNewQuantity(),m.getReason(),m.getCreatedAt());}
    public Long getId(){return id;} public Long getProductId(){return productId;} public String getProductName(){return productName;} public StockMovementType getMovementType(){return movementType;} public Integer getQuantity(){return quantity;} public Integer getPreviousQuantity(){return previousQuantity;} public Integer getNewQuantity(){return newQuantity;} public String getReason(){return reason;} public Instant getCreatedAt(){return createdAt;}
}
