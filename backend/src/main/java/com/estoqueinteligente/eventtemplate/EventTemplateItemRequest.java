package com.estoqueinteligente.eventtemplate;

import jakarta.validation.constraints.*;

public class EventTemplateItemRequest {
    private Long itemId;
    @Size(max=150) private String itemName;
    @Size(max=60) private String unitOfMeasure;
    @NotNull(message="Quantidade sugerida é obrigatória") @Min(value=1,message="Quantidade sugerida deve ser maior que zero") private Integer suggestedQuantity;
    @Size(max=2000) private String notes;
    public Long getItemId(){return itemId;} public void setItemId(Long v){itemId=v;}
    public String getItemName(){return itemName;} public void setItemName(String v){itemName=v;}
    public String getUnitOfMeasure(){return unitOfMeasure;} public void setUnitOfMeasure(String v){unitOfMeasure=v;}
    public Integer getSuggestedQuantity(){return suggestedQuantity;} public void setSuggestedQuantity(Integer v){suggestedQuantity=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
}