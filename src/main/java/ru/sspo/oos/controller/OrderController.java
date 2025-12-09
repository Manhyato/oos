package ru.sspo.oos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sspo.oos.dto.CreateOrderRequest;
import ru.sspo.oos.model.Order;
import ru.sspo.oos.service.OrderService;

import java.util.List;

/**
 * Контроллер для управления заказами.
 * Реализует процесс 1.0 "Приём и обработка заказа" из DFD.
 */
@Tag(name = "Заказы", description = "API для создания и управления заказами")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Создать новый заказ", description = "Создаёт новый заказ с указанными блюдами и данными клиента")
    @PostMapping
    public Order createOrder(@RequestBody @Valid CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @Operation(summary = "Получить все заказы", description = "Возвращает список всех заказов в системе")
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @Operation(summary = "Получить заказ по ID", description = "Возвращает информацию о конкретном заказе")
    @GetMapping("/{id}")
    public Order getOrderById(
            @Parameter(description = "ID заказа", required = true) @PathVariable Long id
    ) {
        return orderService.getOrderById(id);
    }
}


