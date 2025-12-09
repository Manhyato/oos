package ru.sspo.oos.dto;

import lombok.Data;

/**
 * Для MVP — только способ оплаты.
 */
@Data
public class PaymentRequest {
    private String method; // "CASH", "CARD"
}
