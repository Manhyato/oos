package ru.sspo.oos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sspo.oos.dto.CreateOrderRequest;
import ru.sspo.oos.model.Order;
import ru.sspo.oos.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    public Order create(@RequestBody CreateOrderRequest request) {
        return service.createOrder(request);
    }

    @GetMapping
    public List<Order> getAll() {
        return service.getAll();
    }
}

