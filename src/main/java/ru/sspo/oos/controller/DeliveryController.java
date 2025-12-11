package ru.sspo.oos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sspo.oos.dto.UpdateDeliveryStatusRequest;
import ru.sspo.oos.model.Courier;
import ru.sspo.oos.model.Order;
import ru.sspo.oos.model.OrderStatus;
import ru.sspo.oos.service.DeliveryService;

import java.util.List;

/**
 * Контроллер для управления доставкой заказов.
 * Реализует REST API для процессов 3.1, 3.2, 3.3 из DFD.
 */
@Tag(name = "Доставка", description = "API для управления доставкой заказов и курьерами")
@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Operation(summary = "Получить заказы, ожидающие курьера", description = "Возвращает список оплаченных заказов без назначенного курьера")
    @GetMapping("/orders/waiting")
    public List<Order> getOrdersWaitingForCourier() {
        return deliveryService.getOrdersWaitingForCourier();
    }

    @Operation(summary = "Назначить курьера на заказ", description = "Назначает курьера на оплаченный заказ. Если courierId не указан, назначается первый свободный курьер")
    @PostMapping("/orders/{orderId}/assign")
    public Order assignCourier(
            @Parameter(description = "ID заказа", required = true) @PathVariable Long orderId,
            @Parameter(description = "ID курьера (опционально)") @RequestParam(required = false) Long courierId
    ) {
        return deliveryService.assignCourier(orderId, courierId);
    }

    @Operation(summary = "Получить информацию о доставке", description = "Возвращает полную информацию о заказе для курьера: адрес, клиент, позиции")
    @GetMapping("/orders/{orderId}/info")
    public Order getDeliveryInfo(
            @Parameter(description = "ID заказа", required = true) @PathVariable Long orderId
    ) {
        return deliveryService.getDeliveryInfo(orderId);
    }

    @Operation(summary = "Получить заказы курьера", description = "Возвращает список заказов, назначенных конкретному курьеру")
    @GetMapping("/couriers/{courierId}/orders")
    public List<Order> getCourierOrders(
            @Parameter(description = "ID курьера", required = true) @PathVariable Long courierId
    ) {
        return deliveryService.getCourierOrders(courierId);
    }

    @Operation(summary = "Обновить статус доставки", description = "Обновляет статус доставки заказа (DELIVERING или DELIVERED)")
    @PutMapping("/orders/{orderId}/status")
    public Order updateDeliveryStatus(
            @Parameter(description = "ID заказа", required = true) @PathVariable Long orderId,
            @RequestBody @Valid UpdateDeliveryStatusRequest request
    ) {
        return deliveryService.updateDeliveryStatus(orderId, request.getStatus());
    }

    @Operation(summary = "Получить статус доставки для клиента", description = "Возвращает текущий статус доставки заказа и имя курьера")
    @GetMapping("/orders/{orderId}/status")
    public ResponseEntity<OrderStatusResponse> getOrderStatus(
            @Parameter(description = "ID заказа", required = true) @PathVariable Long orderId
    ) {
        Order order = deliveryService.getDeliveryInfo(orderId);
        OrderStatusResponse response = new OrderStatusResponse();
        response.setOrderId(order.getId());
        response.setStatus(order.getStatus());
        response.setCourierName(order.getCourier() != null ? order.getCourier().getName() : null);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Получить всех курьеров", description = "Возвращает список всех курьеров в системе")
    @GetMapping("/couriers")
    public List<Courier> getAllCouriers() {
        return deliveryService.getAllCouriers();
    }

    /**
     * DTO для ответа со статусом заказа.
     */
    public static class OrderStatusResponse {
        private Long orderId;
        private OrderStatus status;
        private String courierName;

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public OrderStatus getStatus() {
            return status;
        }

        public void setStatus(OrderStatus status) {
            this.status = status;
        }

        public String getCourierName() {
            return courierName;
        }

        public void setCourierName(String courierName) {
            this.courierName = courierName;
        }
    }
}

