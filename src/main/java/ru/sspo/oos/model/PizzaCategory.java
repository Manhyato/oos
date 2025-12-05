package ru.sspo.oos.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PizzaCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}

