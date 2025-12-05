package ru.sspo.oos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sspo.oos.model.Pizza;
import ru.sspo.oos.service.PizzaService;

import java.util.List;

@RestController
@RequestMapping("/api/pizzas")
@RequiredArgsConstructor
public class PizzaController {

    private final PizzaService service;

    @PostMapping
    public Pizza create(@RequestBody Pizza pizza) {
        return service.save(pizza);
    }

    @GetMapping
    public List<Pizza> getAll() {
        return service.getAll();
    }

    @GetMapping("/category/{id}")
    public List<Pizza> getByCategory(@PathVariable Long id) {
        return service.getByCategory(id);
    }
}

