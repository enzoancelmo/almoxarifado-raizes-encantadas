package com.estoqueinteligente.eventtemplate;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="event_templates")
public class EventTemplate {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,length=150) private String name;
    @Column(length=500) private String description;
    @Column(name="event_type",length=120) private String eventType;
    @Column(nullable=false) private boolean active=true;
    @Column(columnDefinition="TEXT") private String notes;
    @OneToMany(mappedBy="eventTemplate",cascade=CascadeType.ALL,orphanRemoval=true) private List<EventTemplateItem> items=new ArrayList<>();
    @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt;
    @Column(name="updated_at",nullable=false) private Instant updatedAt;
    @PrePersist void prePersist(){createdAt=updatedAt=Instant.now();}
    @PreUpdate void preUpdate(){updatedAt=Instant.now();}
    public Long getId(){return id;} public String getName(){return name;} public void setName(String v){name=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getEventType(){return eventType;} public void setEventType(String v){eventType=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
    public List<EventTemplateItem> getItems(){return items;} public void setItems(List<EventTemplateItem> v){items=v;}
    public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
}