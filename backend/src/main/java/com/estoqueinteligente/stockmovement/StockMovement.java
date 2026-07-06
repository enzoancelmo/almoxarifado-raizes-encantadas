package com.estoqueinteligente.stockmovement;

import java.time.Instant;
import com.estoqueinteligente.product.Product;
import jakarta.persistence.*;

@Entity
@Table(name="stock_movements")
public class StockMovement {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="product_id") private Product product;
    @Enumerated(EnumType.STRING) @Column(name="movement_type",nullable=false,length=10) private StockMovementType movementType;
    @Column(nullable=false) private Integer quantity;
    @Column(name="previous_quantity",nullable=false) private Integer previousQuantity;
    @Column(name="new_quantity",nullable=false) private Integer newQuantity;
    @Column(length=255) private String reason;
    @Column(name="responsible_name",length=150) private String responsibleName;
    @Column(length=255) private String purpose;
    @Column(name="event_name",length=180) private String eventName;
    @Column(columnDefinition="TEXT") private String notes;
    @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt;
    @PrePersist void prePersist(){if(createdAt==null)createdAt=Instant.now();}
    public Long getId(){return id;} public Product getProduct(){return product;} public void setProduct(Product v){product=v;}
    public StockMovementType getMovementType(){return movementType;} public void setMovementType(StockMovementType v){movementType=v;}
    public Integer getQuantity(){return quantity;} public void setQuantity(Integer v){quantity=v;}
    public Integer getPreviousQuantity(){return previousQuantity;} public void setPreviousQuantity(Integer v){previousQuantity=v;}
    public Integer getNewQuantity(){return newQuantity;} public void setNewQuantity(Integer v){newQuantity=v;}
    public String getReason(){return reason;} public void setReason(String v){reason=v;} public Instant getCreatedAt(){return createdAt;}
    public String getResponsibleName(){return responsibleName;} public void setResponsibleName(String v){responsibleName=v;} public String getPurpose(){return purpose;} public void setPurpose(String v){purpose=v;} public String getEventName(){return eventName;} public void setEventName(String v){eventName=v;} public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
}
