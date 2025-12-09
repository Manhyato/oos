package ru.sspo.oos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Для MVP — только способ оплаты.
 */
@Data
public class PaymentRequest {
    @NotBlank(message = "Способ оплаты обязателен")
    private String method; // "CASH", "CARD"
}
