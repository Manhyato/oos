package ru.sspo.oos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sspo.oos.model.PizzaCategory;

import java.util.Optional;

@Repository
public interface PizzaCategoryRepository extends JpaRepository<PizzaCategory, Long> {
    Optional<PizzaCategory> findByName(String name);
}


