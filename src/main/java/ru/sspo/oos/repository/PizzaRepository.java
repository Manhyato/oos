package ru.sspo.oos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sspo.oos.model.Pizza;

import java.util.List;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {

    /**
     * Получить все пиццы по id категории.
     * Используется на экране выбора блюд.
     */
    List<Pizza> findByCategoryId(Long categoryId);

    /**
     * Поиск по названию (частичное совпадение).
     * Например: поиск "Маргар" вернёт "Маргарита".
     */
    List<Pizza> findByNameContainingIgnoreCase(String namePart);
}

