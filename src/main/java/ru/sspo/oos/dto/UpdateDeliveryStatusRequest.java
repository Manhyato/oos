package ru.sspo.oos.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.sspo.oos.model.OrderStatus;

/**
 * DTO для обновления статуса доставки заказа.
 */
@Data
public class UpdateDeliveryStatusRequest {

    @NotNull(message = "Статус доставки обязателен")
    private OrderStatus status;
}

