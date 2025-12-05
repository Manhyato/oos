package ru.sspo.oos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sspo.oos.model.PizzaCategory;
import ru.sspo.oos.repository.PizzaCategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PizzaCategoryService {

    private final PizzaCategoryRepository repository;

    public PizzaCategory save(PizzaCategory category) {
        return repository.save(category);
    }

    public List<PizzaCategory> getAll() {
        return repository.findAll();
    }
}

