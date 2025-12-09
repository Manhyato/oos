package ru.sspo.oos.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sspo.oos.dto.PizzaCategoryRequest;
import ru.sspo.oos.model.PizzaCategory;
import ru.sspo.oos.repository.PizzaCategoryRepository;
import ru.sspo.oos.service.PizzaCategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PizzaCategoryServiceImpl implements PizzaCategoryService {

    private final PizzaCategoryRepository categoryRepository;

    @Override
    public PizzaCategory createCategory(PizzaCategoryRequest request) {
        PizzaCategory category = new PizzaCategory();
        category.setName(request.getName());
        return categoryRepository.save(category);
    }

    @Override
    public List<PizzaCategory> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}

