package ru.sspo.oos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    @NotBlank(message = "Имя клиента обязательно")
    private String clientName;

    @NotBlank(message = "Телефон обязателен")
    private String clientPhone;

    @NotBlank(message = "Адрес обязателен")
    private String address;

    @NotEmpty(message = "Нужен хотя бы один товар")
    private List<ItemRequest> items;

    @Data
    public static class ItemRequest {
        @NotNull
        private Long pizzaId;

        @Min(value = 1, message = "Количество должно быть больше 0")
        private int quantity;
    }
}

