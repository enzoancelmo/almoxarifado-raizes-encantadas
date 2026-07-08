package com.estoqueinteligente.quickentry;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class QuickEntryRequest {
    private Long existingItemId;
    @Valid private NewItemRequest newItem;
    @NotNull(message="Quantidade é obrigatória") @Min(value=1,message="Quantidade deve ser maior que zero") private Integer quantity;
    @DecimalMin(value="0.0",inclusive=false,message="Valor unitário deve ser maior que zero") private BigDecimal unitValue;
    private Long entryTypeId;
    @Size(max=120) private String entryOrigin;
    @Size(max=150) private String responsibleName;
    @Size(max=2000) private String notes;
    private LocalDate entryDate;
    public Long getExistingItemId(){return existingItemId;} public void setExistingItemId(Long v){existingItemId=v;}
    public NewItemRequest getNewItem(){return newItem;} public void setNewItem(NewItemRequest v){newItem=v;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
    public BigDecimal getUnitValue(){return unitValue;} public void setUnitValue(BigDecimal v){unitValue=v;}
    public Long getEntryTypeId(){return entryTypeId;} public void setEntryTypeId(Long v){entryTypeId=v;}
    public String getEntryOrigin(){return entryOrigin;} public void setEntryOrigin(String v){entryOrigin=v;}
    public String getResponsibleName(){return responsibleName;} public void setResponsibleName(String v){responsibleName=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
    public LocalDate getEntryDate(){return entryDate;} public void setEntryDate(LocalDate v){entryDate=v;}

    public static class NewItemRequest {
        @NotBlank(message="Nome do item é obrigatório") @Size(max=150) private String name;
        @Size(max=150) private String brand;
        @Size(max=500) private String entityPurpose;
        @NotNull(message="Tipo/Categoria é obrigatório") private Long categoryId;
        @Size(max=60) private String unitOfMeasure;
        @NotNull(message="Necessidade mensal é obrigatória") @Min(0) private Integer monthlyRequiredQuantity;
        private LocalDate expirationDate;
        public String getName(){return name;} public void setName(String v){name=v;}
        public String getBrand(){return brand;} public void setBrand(String v){brand=v;}
        public String getEntityPurpose(){return entityPurpose;} public void setEntityPurpose(String v){entityPurpose=v;}
        public Long getCategoryId(){return categoryId;} public void setCategoryId(Long v){categoryId=v;}
        public String getUnitOfMeasure(){return unitOfMeasure;} public void setUnitOfMeasure(String v){unitOfMeasure=v;}
        public Integer getMonthlyRequiredQuantity(){return monthlyRequiredQuantity;} public void setMonthlyRequiredQuantity(Integer v){monthlyRequiredQuantity=v;}
        public LocalDate getExpirationDate(){return expirationDate;} public void setExpirationDate(LocalDate v){expirationDate=v;}
    }
}
