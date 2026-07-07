package com.estoqueinteligente.eventtemplate;

import java.math.BigDecimal;

public class EventTemplateItemResponse {
    private final Long id,itemId;
    private final String itemName,unitOfMeasure,notes,status;
    private final Integer suggestedQuantity,currentQuantity,missingQuantity;
    private final BigDecimal averageCost,currentStockValue;

    public EventTemplateItemResponse(EventTemplateItem item){
        var product=item.getProduct();
        this.id=item.getId();
        this.itemId=product==null?null:product.getId();
        this.itemName=product==null?item.getItemName():product.getName();
        this.unitOfMeasure=product==null?item.getUnitOfMeasure():product.getUnitOfMeasure();
        this.suggestedQuantity=item.getSuggestedQuantity();
        this.notes=item.getNotes();
        this.currentQuantity=product==null?0:product.getQuantity();
        this.averageCost=product==null?BigDecimal.ZERO:product.getAverageCost();
        this.currentStockValue=product==null?BigDecimal.ZERO:product.getCurrentStockValue();
        this.missingQuantity=Math.max(0,this.suggestedQuantity-this.currentQuantity);
        this.status=calculateStatus(product,this.suggestedQuantity,this.currentQuantity);
    }

    private String calculateStatus(Object product,int suggested,int current){
        if(product==null)return "NAO_CADASTRADO";
        if(current>=suggested)return "DISPONIVEL";
        if(current>0)return "ESTOQUE_INSUFICIENTE";
        return "PRECISA_COMPRAR";
    }

    public Long getId(){return id;} public Long getItemId(){return itemId;} public String getItemName(){return itemName;} public String getUnitOfMeasure(){return unitOfMeasure;} public Integer getSuggestedQuantity(){return suggestedQuantity;} public String getNotes(){return notes;} public Integer getCurrentQuantity(){return currentQuantity;} public Integer getMissingQuantity(){return missingQuantity;} public String getStatus(){return status;} public BigDecimal getAverageCost(){return averageCost;} public BigDecimal getCurrentStockValue(){return currentStockValue;}
}