package com.estoqueinteligente.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryRequest {
    @NotBlank(message = "O nome é obrigatório") @Size(max = 100) private String name;
    @Size(max = 255) private String description;
    public CategoryRequest() {}
    public String name() { return name; } public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String description() { return description; } public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
}
