package ru.sspo.oos.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sspo.oos.model.Courier;
import ru.sspo.oos.model.Order;
import ru.sspo.oos.model.OrderStatus;
import ru.sspo.oos.repository.CourierRepository;
import ru.sspo.oos.repository.OrderRepository;
import ru.sspo.oos.service.DeliveryService;

import java.util.List;

/**
 * Реализация сервиса доставки.
 * Соответствует процессу 3.0 "Организация доставки" из DFD.
 */
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;

    @Override
    @Transactional
    public Order assignCourier(Long orderId, Long courierId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));

        // Проверяем, что заказ оплачен
        if (!order.isPaid() || order.getStatus() != OrderStatus.PAID) {
            throw new IllegalStateException("Можно назначить курьера только на оплаченный заказ");
        }

        // Проверяем, что курьер ещё не назначен
        if (order.getCourier() != null) {
            throw new IllegalStateException("Курьер уже назначен на этот заказ");
        }

        Courier courier;
        if (courierId != null) {
            // Назначаем конкретного курьера
            courier = courierRepository.findById(courierId)
                    .orElseThrow(() -> new IllegalArgumentException("Курьер не найден"));
        } else {
            // Автоматически выбираем первого свободного курьера
            List<Courier> availableCouriers = courierRepository.findByAvailableTrue();
            if (availableCouriers.isEmpty()) {
                throw new IllegalStateException("Нет доступных курьеров");
            }
            courier = availableCouriers.get(0);
        }

        // Проверяем доступность курьера
        if (!courier.isAvailable()) {
            throw new IllegalStateException("Курьер занят");
        }

        // Назначаем курьера и обновляем статус
        order.setCourier(courier);
        order.setStatus(OrderStatus.DELIVERY_ASSIGNED);
        courier.setAvailable(false); // Помечаем курьера как занятого

        courierRepository.save(courier);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersWaitingForCourier() {
        // Получаем все оплаченные заказы без назначенного курьера
        return orderRepository.findByPaidAndCourierIsNull(true);
    }

    @Override
    public List<Order> getCourierOrders(Long courierId) {
        // Проверяем существование курьера
        if (!courierRepository.existsById(courierId)) {
            throw new IllegalArgumentException("Курьер не найден");
        }

        // Получаем заказы курьера со статусами доставки
        return orderRepository.findByCourierIdAndStatusIn(
                courierId,
                List.of(OrderStatus.DELIVERY_ASSIGNED, OrderStatus.DELIVERING)
        );
    }

    @Override
    @Transactional
    public Order updateDeliveryStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));

        // Проверяем, что курьер назначен
        if (order.getCourier() == null) {
            throw new IllegalStateException("Курьер не назначен на этот заказ");
        }

        // Проверяем валидность перехода статуса
        OrderStatus currentStatus = order.getStatus();
        if (currentStatus == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Заказ уже доставлен");
        }

        // Обновляем статус
        order.setStatus(status);

        // Если заказ доставлен, освобождаем курьера
        if (status == OrderStatus.DELIVERED) {
            Courier courier = order.getCourier();
            courier.setAvailable(true);
            courierRepository.save(courier);
        }

        return orderRepository.save(order);
    }

    @Override
    public Order getDeliveryInfo(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));

        // Проверяем, что курьер назначен
        if (order.getCourier() == null) {
            throw new IllegalStateException("Курьер не назначен на этот заказ");
        }

        return order;
    }
}

