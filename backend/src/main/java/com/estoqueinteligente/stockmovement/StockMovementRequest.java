package com.estoqueinteligente.stockmovement;

import jakarta.validation.constraints.*;

public class StockMovementRequest {
    @NotNull(message="O produto é obrigatório") private Long productId;
    @NotNull(message="O tipo de movimentação é obrigatório") private StockMovementType movementType;
    @NotNull(message="A quantidade é obrigatória") @Min(value=0,message="A quantidade não pode ser negativa") private Integer quantity;
    @Size(max=255,message="O motivo deve ter no máximo 255 caracteres") private String reason;
    public StockMovementRequest(){}
    public Long getProductId(){return productId;} public void setProductId(Long v){productId=v;}
    public StockMovementType getMovementType(){return movementType;} public void setMovementType(StockMovementType v){movementType=v;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
    public String getReason(){return reason;} public void setReason(String v){reason=v;}
}
