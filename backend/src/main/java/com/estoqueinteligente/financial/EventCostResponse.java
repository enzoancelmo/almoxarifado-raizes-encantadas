package com.estoqueinteligente.financial;

import java.math.BigDecimal;
import java.util.List;

public class EventCostResponse {
    private final String eventName; private final BigDecimal totalOutputValue; private final long totalItemsUsed; private final List<FinancialMovementResponse> movements;
    public EventCostResponse(String eventName,BigDecimal totalOutputValue,long totalItemsUsed,List<FinancialMovementResponse> movements){this.eventName=eventName;this.totalOutputValue=totalOutputValue;this.totalItemsUsed=totalItemsUsed;this.movements=movements;}
    public String getEventName(){return eventName;} public BigDecimal getTotalOutputValue(){return totalOutputValue;} public long getTotalItemsUsed(){return totalItemsUsed;} public List<FinancialMovementResponse> getMovements(){return movements;}
}
