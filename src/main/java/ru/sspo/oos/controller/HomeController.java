package ru.sspo.oos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.sspo.oos.model.Pizza;
import ru.sspo.oos.model.PizzaCategory;
import ru.sspo.oos.repository.PizzaCategoryRepository;
import ru.sspo.oos.repository.PizzaRepository;

import java.util.List;

/**
 * Контроллер для главной страницы и навигации.
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PizzaRepository pizzaRepository;
    private final PizzaCategoryRepository categoryRepository;

    @GetMapping("/")
    public String home(Model model) {
        List<Pizza> pizzas = pizzaRepository.findAll();
        List<PizzaCategory> categories = categoryRepository.findAll();
        
        model.addAttribute("pizzas", pizzas);
        model.addAttribute("categories", categories);
        model.addAttribute("title", "Меню пицц");
        
        return "index";
    }
}

