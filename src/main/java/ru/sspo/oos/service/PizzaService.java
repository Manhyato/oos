package ru.sspo.oos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sspo.oos.dto.PizzaRequest;
import ru.sspo.oos.exception.ResourceNotFoundException;
import ru.sspo.oos.model.Pizza;
import ru.sspo.oos.model.PizzaCategory;
import ru.sspo.oos.repository.PizzaCategoryRepository;
import ru.sspo.oos.repository.PizzaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PizzaService {

    private final PizzaRepository repository;
    private final PizzaCategoryRepository categoryRepository;

    public Pizza save(Pizza pizza) {
        return repository.save(pizza);
    }

    public List<Pizza> getAll() {
        return repository.findAll();
    }

    public List<Pizza> getByCategory(Long categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    public Pizza createPizza(PizzaRequest request) {
        PizzaCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Категория не найдена"));

        Pizza pizza = new Pizza();
        pizza.setName(request.getName());
        pizza.setPrice(request.getPrice());
        pizza.setCategory(category);
        return repository.save(pizza);
    }

    public void deletePizza(Long id) {
        repository.deleteById(id);
    }
}


