package ru.sspo.oos.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sspo.oos.dto.UpdateDeliveryStatusRequest;
import ru.sspo.oos.model.Courier;
import ru.sspo.oos.model.Order;
import ru.sspo.oos.model.OrderStatus;
import ru.sspo.oos.repository.CourierRepository;
import ru.sspo.oos.service.DeliveryService;

import java.util.List;

/**
 * Контроллер для управления доставкой заказов.
 * Реализует REST API для процессов 3.1, 3.2, 3.3 из DFD.
 */
@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final CourierRepository courierRepository;

    /**
     * Получить список оплаченных заказов, ожидающих назначения курьера.
     * Процесс 3.1 - входные данные для назначения курьера.
     */
    @GetMapping("/orders/waiting")
    public List<Order> getOrdersWaitingForCourier() {
        return deliveryService.getOrdersWaitingForCourier();
    }

    /**
     * Назначить курьера на заказ (процесс 3.1).
     * Если courierId не указан, назначается первый свободный курьер.
     */
    @PostMapping("/orders/{orderId}/assign")
    public Order assignCourier(
            @PathVariable Long orderId,
            @RequestParam(required = false) Long courierId
    ) {
        return deliveryService.assignCourier(orderId, courierId);
    }

    /**
     * Получить информацию о доставке для курьера (процесс 3.2).
     * Возвращает полную информацию о заказе: адрес, клиент, позиции.
     */
    @GetMapping("/orders/{orderId}/info")
    public Order getDeliveryInfo(@PathVariable Long orderId) {
        return deliveryService.getDeliveryInfo(orderId);
    }

    /**
     * Получить список заказов курьера.
     */
    @GetMapping("/couriers/{courierId}/orders")
    public List<Order> getCourierOrders(@PathVariable Long courierId) {
        return deliveryService.getCourierOrders(courierId);
    }

    /**
     * Обновить статус доставки (процесс 3.3).
     * Курьер сообщает о текущем статусе: DELIVERING (в пути) или DELIVERED (доставлен).
     */
    @PutMapping("/orders/{orderId}/status")
    public Order updateDeliveryStatus(
            @PathVariable Long orderId,
            @RequestBody @Valid UpdateDeliveryStatusRequest request
    ) {
        return deliveryService.updateDeliveryStatus(orderId, request.getStatus());
    }

    /**
     * Получить статус доставки заказа для клиента.
     */
    @GetMapping("/orders/{orderId}/status")
    public ResponseEntity<OrderStatusResponse> getOrderStatus(@PathVariable Long orderId) {
        Order order = deliveryService.getDeliveryInfo(orderId);
        OrderStatusResponse response = new OrderStatusResponse();
        response.setOrderId(order.getId());
        response.setStatus(order.getStatus());
        response.setCourierName(order.getCourier() != null ? order.getCourier().getName() : null);
        return ResponseEntity.ok(response);
    }

    /**
     * Получить список всех курьеров.
     */
    @GetMapping("/couriers")
    public List<Courier> getAllCouriers() {
        return courierRepository.findAll();
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

