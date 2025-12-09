package ru.sspo.oos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "orders") // "order" — зарезервировано SQL
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Для JPA прокси
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Предотвращаем циклическую ссылку
    private Client client;

    private LocalDateTime createdAt;

    private boolean paid;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private BigDecimal totalAmount;

    private String address;

    /**
     * Курьер, назначенный на доставку заказа.
     * Может быть null, если курьер ещё не назначен.
     */
    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Предотвращаем циклическую ссылку
    private Courier courier;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("order") // Предотвращаем циклическую ссылку
    private List<OrderItem> items;
}

