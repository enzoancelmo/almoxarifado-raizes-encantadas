package com.estoqueinteligente.eventtemplate;

import jakarta.validation.constraints.*;

public class EventTemplateRequest {
    @NotBlank(message="Nome é obrigatório") @Size(max=150) private String name;
    @Size(max=500) private String description;
    @Size(max=120) private String eventType;
    private Boolean active;
    @Size(max=2000) private String notes;
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getEventType(){return eventType;} public void setEventType(String v){eventType=v;}
    public Boolean getActive(){return active;} public void setActive(Boolean v){active=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
}