package com.estoqueinteligente.product;

import com.estoqueinteligente.category.Category;
import com.estoqueinteligente.supplier.Supplier;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 150) private String name;
    @Column(length = 150) private String brand;
    @Column(name = "entity_purpose", length = 500) private String entityPurpose;
    @Column(name = "unit_of_measure", length = 60) private String unitOfMeasure;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "category_id") private Category category;
    @Column(nullable = false) private Integer quantity;
    @Column(name = "monthly_required_quantity", nullable = false) private Integer monthlyRequiredQuantity;
    @Column(name = "count_pending", nullable = false) private boolean countPending;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30) private ProductStatus status;
    @Column(columnDefinition = "TEXT") private String notes;

    // Colunas legadas mantidas para compatibilidade da base técnica.
    @Column(unique = true, length = 50) private String barcode;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "supplier_id") private Supplier supplier;
    @Column(name = "cost_price", nullable = false, precision = 12, scale = 2) private BigDecimal costPrice = BigDecimal.ZERO;
    @Column(name = "sale_price", nullable = false, precision = 12, scale = 2) private BigDecimal salePrice = BigDecimal.ZERO;
    @Column(name = "minimum_stock", nullable = false) private Integer minimumStock = 0;
    @Column(name = "expiration_date") private LocalDate expirationDate;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;

    @PrePersist void createTimestamps(){createdAt=updatedAt=Instant.now();}
    @PreUpdate void updateTimestamp(){updatedAt=Instant.now();}
    public Long getId(){return id;} public String getName(){return name;} public void setName(String v){name=v;}
    public String getBrand(){return brand;} public void setBrand(String v){brand=v;}
    public String getEntityPurpose(){return entityPurpose;} public void setEntityPurpose(String v){entityPurpose=v;}
    public String getUnitOfMeasure(){return unitOfMeasure;} public void setUnitOfMeasure(String v){unitOfMeasure=v;}
    public Category getCategory(){return category;} public void setCategory(Category v){category=v;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
    public Integer getMonthlyRequiredQuantity(){return monthlyRequiredQuantity;} public void setMonthlyRequiredQuantity(Integer v){monthlyRequiredQuantity=v;}
    public boolean isCountPending(){return countPending;} public void setCountPending(boolean v){countPending=v;}
    public ProductStatus getStatus(){return status;} public void setStatus(ProductStatus v){status=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
    public String getBarcode(){return barcode;} public void setBarcode(String v){barcode=v;}
    public Supplier getSupplier(){return supplier;} public void setSupplier(Supplier v){supplier=v;}
    public BigDecimal getCostPrice(){return costPrice;} public void setCostPrice(BigDecimal v){costPrice=v;}
    public BigDecimal getSalePrice(){return salePrice;} public void setSalePrice(BigDecimal v){salePrice=v;}
    public Integer getMinimumStock(){return minimumStock;} public void setMinimumStock(Integer v){minimumStock=v;}
    public LocalDate getExpirationDate(){return expirationDate;} public void setExpirationDate(LocalDate v){expirationDate=v;}
    public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
}
