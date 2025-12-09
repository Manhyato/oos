package ru.sspo.oos.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Модель курьера для доставки заказов.
 * Для MVP — простая структура: имя, телефон, доступность.
 */
@Entity
@Data
@Table(name = "couriers")
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    /**
     * Флаг доступности курьера (true = свободен, false = занят).
     */
    private boolean available = true;
}

