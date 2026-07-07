package com.estoqueinteligente.eventtemplate;

import java.math.BigDecimal;

public class EventTemplateItemResponse {
    private final Long id,itemId; private final String itemName,unitOfMeasure,notes; private final Integer suggestedQuantity,currentQuantity; private final BigDecimal averageCost,currentStockValue;
    public EventTemplateItemResponse(EventTemplateItem item){this.id=item.getId();this.itemId=item.getProduct().getId();this.itemName=item.getProduct().getName();this.unitOfMeasure=item.getProduct().getUnitOfMeasure();this.suggestedQuantity=item.getSuggestedQuantity();this.notes=item.getNotes();this.currentQuantity=item.getProduct().getQuantity();this.averageCost=item.getProduct().getAverageCost();this.currentStockValue=item.getProduct().getCurrentStockValue();}
    public Long getId(){return id;} public Long getItemId(){return itemId;} public String getItemName(){return itemName;} public String getUnitOfMeasure(){return unitOfMeasure;} public Integer getSuggestedQuantity(){return suggestedQuantity;} public String getNotes(){return notes;} public Integer getCurrentQuantity(){return currentQuantity;} public BigDecimal getAverageCost(){return averageCost;} public BigDecimal getCurrentStockValue(){return currentStockValue;}
}