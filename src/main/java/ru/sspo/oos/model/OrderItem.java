package ru.sspo.oos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Для JPA прокси
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties({"items", "client", "courier"}) // Предотвращаем циклическую ссылку
    private Order order;

    @ManyToOne
    @JsonIgnoreProperties("category") // Предотвращаем циклическую ссылку
    private Pizza pizza;

    private int quantity;
    private BigDecimal price; // фиксируем цену на момент заказа
}

