package ru.sspo.oos.service;

import ru.sspo.oos.dto.PizzaCategoryRequest;
import ru.sspo.oos.model.PizzaCategory;

import java.util.List;

public interface PizzaCategoryService {
    PizzaCategory createCategory(PizzaCategoryRequest request);
    List<PizzaCategory> getAll();
    void deleteCategory(Long id);
}


