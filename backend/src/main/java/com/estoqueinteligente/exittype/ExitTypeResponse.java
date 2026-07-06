package com.estoqueinteligente.exittype;

import java.time.Instant;

public class ExitTypeResponse {
    private final Long id; private final String name,description; private final boolean active; private final Instant createdAt,updatedAt;
    private ExitTypeResponse(ExitType e){id=e.getId();name=e.getName();description=e.getDescription();active=e.isActive();createdAt=e.getCreatedAt();updatedAt=e.getUpdatedAt();}
    public static ExitTypeResponse from(ExitType e){return new ExitTypeResponse(e);}
    public Long getId(){return id;} public String getName(){return name;} public String getDescription(){return description;} public boolean isActive(){return active;} public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
}
