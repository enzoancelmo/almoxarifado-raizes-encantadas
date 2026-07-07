package com.estoqueinteligente.eventtemplate;

import com.estoqueinteligente.product.Product;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="event_template_items")
public class EventTemplateItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="event_template_id") private EventTemplate eventTemplate;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="product_id") private Product product;
    @Column(name="item_name",nullable=false,length=150) private String itemName;
    @Column(name="unit_of_measure",length=60) private String unitOfMeasure;
    @Column(name="suggested_quantity",nullable=false) private Integer suggestedQuantity;
    @Column(columnDefinition="TEXT") private String notes;
    @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt;
    @Column(name="updated_at",nullable=false) private Instant updatedAt;
    @PrePersist void prePersist(){createdAt=updatedAt=Instant.now();}
    @PreUpdate void preUpdate(){updatedAt=Instant.now();}
    public Long getId(){return id;} public EventTemplate getEventTemplate(){return eventTemplate;} public void setEventTemplate(EventTemplate v){eventTemplate=v;}
    public Product getProduct(){return product;} public void setProduct(Product v){product=v;} public String getItemName(){return itemName;} public void setItemName(String v){itemName=v;} public String getUnitOfMeasure(){return unitOfMeasure;} public void setUnitOfMeasure(String v){unitOfMeasure=v;}
    public Integer getSuggestedQuantity(){return suggestedQuantity;} public void setSuggestedQuantity(Integer v){suggestedQuantity=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
    public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
}