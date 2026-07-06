package com.estoqueinteligente.product;

import jakarta.validation.constraints.*;

public class ProductRequest {
    @NotBlank(message="O nome do item é obrigatório") @Size(max=150) private String name;
    @Size(max=150) private String brand;
    @Size(max=500) private String entityPurpose;
    @Size(max=60) private String unitOfMeasure;
    @NotNull(message="O tipo é obrigatório") private Long categoryId;
    @NotNull @DecimalMin(value="0.0") private Double currentQuantity;
    @NotNull @DecimalMin(value="0.0") private Double monthlyRequiredQuantity;
    private Boolean countPending;
    @Size(max=2000) private String notes;
    public String name(){return name;} public String getName(){return name;} public void setName(String v){name=v;}
    public String getBrand(){return brand;} public void setBrand(String v){brand=v;}
    public String getEntityPurpose(){return entityPurpose;} public void setEntityPurpose(String v){entityPurpose=v;}
    public String getUnitOfMeasure(){return unitOfMeasure;} public void setUnitOfMeasure(String v){unitOfMeasure=v;}
    public Long categoryId(){return categoryId;} public Long getCategoryId(){return categoryId;} public void setCategoryId(Long v){categoryId=v;}
    public Double getCurrentQuantity(){return currentQuantity;} public void setCurrentQuantity(Double v){currentQuantity=v;}
    public Double getMonthlyRequiredQuantity(){return monthlyRequiredQuantity;} public void setMonthlyRequiredQuantity(Double v){monthlyRequiredQuantity=v;}
    public Boolean getCountPending(){return countPending;} public void setCountPending(Boolean v){countPending=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
}
