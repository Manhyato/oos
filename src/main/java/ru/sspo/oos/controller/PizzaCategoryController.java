package ru.sspo.oos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sspo.oos.model.PizzaCategory;
import ru.sspo.oos.service.PizzaCategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class PizzaCategoryController {

    private final PizzaCategoryService service;

    @PostMapping
    public PizzaCategory create(@RequestBody PizzaCategory category) {
        return service.save(category);
    }

    @GetMapping
    public List<PizzaCategory> getAll() {
        return service.getAll();
    }
}

