package com.estoqueinteligente.financial;

import com.estoqueinteligente.stockmovement.StockMovement;
import com.estoqueinteligente.stockmovement.StockMovementType;
import java.math.BigDecimal;
import java.time.Instant;

public class FinancialMovementResponse {
    private final Long id,exitBatchId,eventTemplateId; private final String itemName,exitTypeName,responsibleName,eventName,eventTemplateName,notes; private final StockMovementType movementType; private final Integer quantity; private final BigDecimal unitValue,totalValue; private final Instant createdAt;
    private FinancialMovementResponse(StockMovement m){id=m.getId();itemName=m.getProduct().getName();movementType=m.getMovementType();quantity=m.getQuantity();unitValue=m.getUnitValue();totalValue=m.getTotalValue();exitTypeName=m.getExitType()==null?null:m.getExitType().getName();responsibleName=m.getResponsibleName();eventName=m.getEventName();notes=m.getNotes()==null?m.getReason():m.getNotes();createdAt=m.getCreatedAt();exitBatchId=m.getExitBatch()==null?null:m.getExitBatch().getId();eventTemplateId=m.getExitBatch()==null||m.getExitBatch().getEventTemplate()==null?null:m.getExitBatch().getEventTemplate().getId();eventTemplateName=m.getExitBatch()==null||m.getExitBatch().getEventTemplate()==null?null:m.getExitBatch().getEventTemplate().getName();}
    public static FinancialMovementResponse from(StockMovement m){return new FinancialMovementResponse(m);}
    public Long getId(){return id;} public Long getExitBatchId(){return exitBatchId;} public Long getEventTemplateId(){return eventTemplateId;} public String getEventTemplateName(){return eventTemplateName;} public String getItemName(){return itemName;} public StockMovementType getMovementType(){return movementType;} public Integer getQuantity(){return quantity;} public BigDecimal getUnitValue(){return unitValue;} public BigDecimal getTotalValue(){return totalValue;} public String getExitTypeName(){return exitTypeName;} public String getResponsibleName(){return responsibleName;} public String getEventName(){return eventName;} public String getNotes(){return notes;} public Instant getCreatedAt(){return createdAt;}
}
