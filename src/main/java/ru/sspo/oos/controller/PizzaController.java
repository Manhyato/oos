package ru.sspo.oos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sspo.oos.dto.PizzaRequest;
import ru.sspo.oos.model.Pizza;
import ru.sspo.oos.service.PizzaService;

import java.util.List;

@RestController
@RequestMapping("/api/pizzas")
@RequiredArgsConstructor
public class PizzaController {

    private final PizzaService service;

    @PostMapping
    public Pizza create(@RequestBody @jakarta.validation.Valid PizzaRequest request) {
        return service.createPizza(request);
    }

    @GetMapping
    public List<Pizza> getAll() {
        return service.getAll();
    }

    @GetMapping("/category/{id}")
    public List<Pizza> getByCategory(@PathVariable Long id) {
        return service.getByCategory(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deletePizza(id);
    }
}

