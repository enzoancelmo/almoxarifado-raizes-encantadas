package com.estoqueinteligente.quickexit;

import com.estoqueinteligente.exittype.ExitType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "stock_exit_batches")
public class StockExitBatch {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "event_name", nullable = false, length = 180) private String eventName;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "exit_type_id") private ExitType exitType;
    @Column(name = "responsible_name", length = 150) private String responsibleName;
    @Column(name = "exit_date") private LocalDate exitDate;
    @Column(columnDefinition = "TEXT") private String notes;
    @Column(name = "total_value", nullable = false, precision = 15, scale = 2) private BigDecimal totalValue = BigDecimal.ZERO;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;

    @PrePersist void prePersist(){createdAt=updatedAt=Instant.now();}
    @PreUpdate void preUpdate(){updatedAt=Instant.now();}

    public Long getId(){return id;}
    public String getEventName(){return eventName;} public void setEventName(String v){eventName=v;}
    public ExitType getExitType(){return exitType;} public void setExitType(ExitType v){exitType=v;}
    public String getResponsibleName(){return responsibleName;} public void setResponsibleName(String v){responsibleName=v;}
    public LocalDate getExitDate(){return exitDate;} public void setExitDate(LocalDate v){exitDate=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
    public BigDecimal getTotalValue(){return totalValue;} public void setTotalValue(BigDecimal v){totalValue=v==null?BigDecimal.ZERO:v;}
    public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
}