package ru.sspo.oos.model;

/**
 * Простые статусы для MVP: новый → оплачен → в доставке → доставлен.
 */
public enum OrderStatus {
    NEW,
    PAID,
    DELIVERY_ASSIGNED,
    DELIVERING,
    DELIVERED
}



