package com.estoqueinteligente.eventtemplate;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

public class EventTemplateResponse {
    private final Long id; private final String name,description,eventType,notes; private final boolean active; private final Instant createdAt,updatedAt; private final List<EventTemplateItemResponse> items;
    public EventTemplateResponse(EventTemplate t){id=t.getId();name=t.getName();description=t.getDescription();eventType=t.getEventType();active=t.isActive();notes=t.getNotes();createdAt=t.getCreatedAt();updatedAt=t.getUpdatedAt();items=t.getItems().stream().sorted(Comparator.comparing(i->i.getProduct()==null?i.getItemName():i.getProduct().getName(),String.CASE_INSENSITIVE_ORDER)).map(EventTemplateItemResponse::new).toList();}
    public Long getId(){return id;} public String getName(){return name;} public String getDescription(){return description;} public String getEventType(){return eventType;} public boolean isActive(){return active;} public String getNotes(){return notes;} public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;} public List<EventTemplateItemResponse> getItems(){return items;}
}
