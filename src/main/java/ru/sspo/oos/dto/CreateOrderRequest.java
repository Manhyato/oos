package ru.sspo.oos.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequest {
    private String clientName;
    private String clientPhone;
    private List<ItemRequest> items;

    @Data
    public static class ItemRequest {
        private Long pizzaId;
        private int quantity;
    }
}

