package com.estoqueinteligente.quickexit;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class QuickExitRequest {
    @NotBlank(message = "Evento/Cerimônia é obrigatório") @Size(max = 180) private String eventName;
    private Long exitTypeId;
    @Size(max = 150) private String responsibleName;
    private LocalDate exitDate;
    @Size(max = 2000) private String notes;
    @Valid @NotEmpty(message = "Informe pelo menos um item") private List<Item> items = new ArrayList<>();

    public String getEventName(){return eventName;} public void setEventName(String v){eventName=v;}
    public Long getExitTypeId(){return exitTypeId;} public void setExitTypeId(Long v){exitTypeId=v;}
    public String getResponsibleName(){return responsibleName;} public void setResponsibleName(String v){responsibleName=v;}
    public LocalDate getExitDate(){return exitDate;} public void setExitDate(LocalDate v){exitDate=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
    public List<Item> getItems(){return items;} public void setItems(List<Item> v){items=v;}

    public static class Item {
        @NotNull(message = "Item é obrigatório") private Long itemId;
        @NotNull(message = "Quantidade é obrigatória") @Min(value = 1, message = "Quantidade deve ser maior que zero") private Integer quantity;
        @DecimalMin(value = "0.0", message = "Valor unitário não pode ser negativo") private BigDecimal unitValue;
        @Size(max = 2000) private String notes;
        public Long getItemId(){return itemId;} public void setItemId(Long v){itemId=v;}
        public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
        public BigDecimal getUnitValue(){return unitValue;} public void setUnitValue(BigDecimal v){unitValue=v;}
        public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
    }
}