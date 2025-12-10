package ru.sspo.oos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PizzaCategoryRequest {
    @NotBlank(message = "Название категории обязательно")
    private String name;
}

