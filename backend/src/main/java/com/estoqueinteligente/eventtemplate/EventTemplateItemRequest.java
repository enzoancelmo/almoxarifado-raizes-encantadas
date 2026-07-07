package com.estoqueinteligente.eventtemplate;

import jakarta.validation.constraints.*;

public class EventTemplateItemRequest {
    @NotNull(message="Item é obrigatório") private Long itemId;
    @NotNull(message="Quantidade sugerida é obrigatória") @Min(value=1,message="Quantidade sugerida deve ser maior que zero") private Integer suggestedQuantity;
    @Size(max=2000) private String notes;
    public Long getItemId(){return itemId;} public void setItemId(Long v){itemId=v;}
    public Integer getSuggestedQuantity(){return suggestedQuantity;} public void setSuggestedQuantity(Integer v){suggestedQuantity=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
}