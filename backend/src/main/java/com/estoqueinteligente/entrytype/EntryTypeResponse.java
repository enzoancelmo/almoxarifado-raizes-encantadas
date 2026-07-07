package com.estoqueinteligente.entrytype;

import java.time.Instant;

public class EntryTypeResponse {
    private final Long id; private final String name,description; private final boolean active; private final Instant createdAt,updatedAt;
    private EntryTypeResponse(EntryType e){id=e.getId();name=e.getName();description=e.getDescription();active=e.isActive();createdAt=e.getCreatedAt();updatedAt=e.getUpdatedAt();}
    public static EntryTypeResponse from(EntryType e){return new EntryTypeResponse(e);}
    public Long getId(){return id;} public String getName(){return name;} public String getDescription(){return description;} public boolean isActive(){return active;} public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
}
