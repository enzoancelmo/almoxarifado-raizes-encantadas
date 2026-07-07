package com.estoqueinteligente.entrytype;

import jakarta.validation.constraints.*;

public class EntryTypeRequest {
    @NotBlank(message="Nome do tipo de entrada é obrigatório") @Size(max=120) private String name;
    @Size(max=500) private String description;
    private Boolean active;
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public Boolean getActive(){return active;} public void setActive(Boolean v){active=v;}
}
