package ru.sspo.oos.dto;

import lombok.Data;
import java.util.List;

/**
 * Данные, которые присылает клиент для оформления заказа.
 * Клиент передает: телефон (+ имя если новый) и список позиций (id пиццы + кол-во).
 */
@Data
public class CreateOrderRequest {
    private String phone;
    private String fullName;
    private List<Item> items;

    @Data
    public static class Item {
        private Long pizzaId;
        private int quantity;
    }
}
