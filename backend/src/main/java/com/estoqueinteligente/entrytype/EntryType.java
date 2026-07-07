package com.estoqueinteligente.entrytype;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="entry_types")
public class EntryType {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,length=120) private String name;
    @Column(length=500) private String description;
    @Column(nullable=false) private boolean active=true;
    @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt;
    @Column(name="updated_at",nullable=false) private Instant updatedAt;
    @PrePersist void prePersist(){createdAt=updatedAt=Instant.now();}
    @PreUpdate void preUpdate(){updatedAt=Instant.now();}
    public Long getId(){return id;} public String getName(){return name;} public void setName(String v){name=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
    public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
}
