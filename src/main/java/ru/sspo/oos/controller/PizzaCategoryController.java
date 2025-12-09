package ru.sspo.oos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.sspo.oos.dto.PizzaCategoryRequest;
import ru.sspo.oos.service.PizzaCategoryService;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class PizzaCategoryController {

    private final PizzaCategoryService pizzaCategoryService;

    @GetMapping
    public String listCategoriesPage() {
        return "admin/categories"; // ✔ просто возвращаем HTML страницу
    }

    // 🔥 JSON API для получения списка категорий
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(pizzaCategoryService.getAll());
    }

    // 🔥 JSON API для добавления категории
    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<?> createCategory(@RequestBody PizzaCategoryRequest request) {
        pizzaCategoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 🔥 JSON API для удаления категории
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        pizzaCategoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}




