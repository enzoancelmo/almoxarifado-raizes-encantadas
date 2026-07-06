package com.estoqueinteligente.product;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class ProductResponse {
    private final Long id,categoryId;
    private final String name,brand,entityPurpose,unitOfMeasure,categoryName,notes;
    private final Integer currentQuantity,monthlyRequiredQuantity,balance;
    private final BigDecimal purchaseValue,exitValue,averageCost,currentStockValue,totalInventoryValue;
    private final boolean countPending;
    private final ProductStatus status;
    private final Instant createdAt,updatedAt;
    ProductResponse(Product p,ProductStatus status){
        id=p.getId();name=p.getName();brand=p.getBrand();entityPurpose=p.getEntityPurpose();unitOfMeasure=p.getUnitOfMeasure();
        categoryId=p.getCategory().getId();categoryName=p.getCategory().getName();currentQuantity=p.getQuantity();
        monthlyRequiredQuantity=p.getMonthlyRequiredQuantity();balance=currentQuantity-monthlyRequiredQuantity;
        purchaseValue=p.getPurchaseValue()==null?BigDecimal.ZERO:p.getPurchaseValue();exitValue=p.getExitValue()==null?BigDecimal.ZERO:p.getExitValue();
        averageCost=p.getAverageCost()==null?BigDecimal.ZERO:p.getAverageCost();currentStockValue=p.getCurrentStockValue()==null?BigDecimal.ZERO:p.getCurrentStockValue();
        totalInventoryValue=currentStockValue;
        countPending=p.isCountPending();notes=p.getNotes();this.status=status;createdAt=p.getCreatedAt();updatedAt=p.getUpdatedAt();
    }
    static ProductResponse from(Product p,ProductStatus s){return new ProductResponse(p,s);}
    public Long id(){return id;} public Long getId(){return id;} public String getName(){return name;}
    public String getBrand(){return brand;} public String getEntityPurpose(){return entityPurpose;}
    public String getUnitOfMeasure(){return unitOfMeasure;} public Long getCategoryId(){return categoryId;}
    public String getCategoryName(){return categoryName;} public Integer getCurrentQuantity(){return currentQuantity;}
    public Integer getMonthlyRequiredQuantity(){return monthlyRequiredQuantity;} public Integer getBalance(){return balance;}
    public BigDecimal getPurchaseValue(){return purchaseValue;} public BigDecimal getExitValue(){return exitValue;} public BigDecimal getAverageCost(){return averageCost;} public BigDecimal getCurrentStockValue(){return currentStockValue;} public BigDecimal getTotalInventoryValue(){return totalInventoryValue;}
    public boolean isCountPending(){return countPending;} public String getNotes(){return notes;}
    public ProductStatus getStatus(){return status;} public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
    // Compatibilidade temporária com relatórios e componentes reaproveitados.
    public Integer getQuantity(){return currentQuantity;} public Integer getMinimumStock(){return monthlyRequiredQuantity;}
    public String getBarcode(){return null;} public Long getSupplierId(){return null;} public String getSupplierName(){return null;}
    public BigDecimal getCostPrice(){return purchaseValue;} public BigDecimal getSalePrice(){return exitValue;}
    public LocalDate getExpirationDate(){return null;}
}
