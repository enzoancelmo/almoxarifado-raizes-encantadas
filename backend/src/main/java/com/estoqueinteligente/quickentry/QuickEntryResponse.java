package com.estoqueinteligente.quickentry;

import com.estoqueinteligente.product.ProductResponse;
import com.estoqueinteligente.stockmovement.StockMovementResponse;

public class QuickEntryResponse {
    private final ProductResponse item;
    private final StockMovementResponse movement;
    private final String message;
    public QuickEntryResponse(ProductResponse item,StockMovementResponse movement,String message){this.item=item;this.movement=movement;this.message=message;}
    public ProductResponse getItem(){return item;} public StockMovementResponse getMovement(){return movement;} public String getMessage(){return message;}
}
