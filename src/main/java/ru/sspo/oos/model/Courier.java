package ru.sspo.oos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Модель курьера для доставки заказов.
 * Для MVP — простая структура: имя, телефон, доступность.
 */
@Entity
@Data
@Table(name = "couriers")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Для JPA прокси
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

