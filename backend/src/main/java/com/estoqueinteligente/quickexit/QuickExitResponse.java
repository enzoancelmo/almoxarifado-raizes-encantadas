package com.estoqueinteligente.quickexit;

import com.estoqueinteligente.stockmovement.StockMovementResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class QuickExitResponse {
    private final Long id,eventTemplateId; private final String eventName,eventTemplateName, exitTypeName, responsibleName, notes; private final LocalDate exitDate;
    private final int totalDifferentItems; private final int totalQuantity; private final BigDecimal totalValue; private final List<StockMovementResponse> movements;
    public QuickExitResponse(Long id,String eventName,Long eventTemplateId,String eventTemplateName,String exitTypeName,String responsibleName,LocalDate exitDate,String notes,int totalDifferentItems,int totalQuantity,BigDecimal totalValue,List<StockMovementResponse> movements){this.id=id;this.eventName=eventName;this.eventTemplateId=eventTemplateId;this.eventTemplateName=eventTemplateName;this.exitTypeName=exitTypeName;this.responsibleName=responsibleName;this.exitDate=exitDate;this.notes=notes;this.totalDifferentItems=totalDifferentItems;this.totalQuantity=totalQuantity;this.totalValue=totalValue;this.movements=movements;}
    public Long getId(){return id;} public String getEventName(){return eventName;} public Long getEventTemplateId(){return eventTemplateId;} public String getEventTemplateName(){return eventTemplateName;} public String getExitTypeName(){return exitTypeName;} public String getResponsibleName(){return responsibleName;} public LocalDate getExitDate(){return exitDate;} public String getNotes(){return notes;} public int getTotalDifferentItems(){return totalDifferentItems;} public int getTotalItems(){return totalDifferentItems;} public int getTotalQuantity(){return totalQuantity;} public BigDecimal getTotalValue(){return totalValue;} public List<StockMovementResponse> getMovements(){return movements;}
}