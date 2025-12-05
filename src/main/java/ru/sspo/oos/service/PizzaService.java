package ru.sspo.oos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sspo.oos.model.Pizza;
import ru.sspo.oos.repository.PizzaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PizzaService {

    private final PizzaRepository repository;

    public Pizza save(Pizza pizza) {
        return repository.save(pizza);
    }

    public List<Pizza> getAll() {
        return repository.findAll();
    }

    public List<Pizza> getByCategory(Long categoryId) {
        return repository.findByCategoryId(categoryId);
    }
}


