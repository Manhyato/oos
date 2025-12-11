package ru.sspo.oos.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sspo.oos.exception.BusinessException;
import ru.sspo.oos.exception.ResourceNotFoundException;
import ru.sspo.oos.model.Courier;
import ru.sspo.oos.model.Order;
import ru.sspo.oos.model.OrderStatus;
import ru.sspo.oos.repository.CourierRepository;
import ru.sspo.oos.repository.OrderRepository;
import ru.sspo.oos.service.DeliveryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;

    @Override
    @Transactional
    public Order assignCourier(Long orderId, Long courierId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Заказ с ID " + orderId + " не найден"));

        if (!order.isPaid() || order.getStatus() != OrderStatus.PAID) {
            throw new BusinessException("Можно назначить курьера только на оплаченный заказ");
        }

        if (order.getCourier() != null) {
            throw new BusinessException("Курьер уже назначен на этот заказ");
        }

        Courier courier;
        if (courierId != null) {
            courier = courierRepository.findById(courierId)
                    .orElseThrow(() -> new ResourceNotFoundException("Курьер с ID " + courierId + " не найден"));
        } else {
            List<Courier> availableCouriers = courierRepository.findByAvailableTrue();
            if (availableCouriers.isEmpty()) {
                throw new BusinessException("Нет доступных курьеров");
            }
            courier = availableCouriers.get(0);
        }

        if (!courier.isAvailable()) {
            throw new BusinessException("Курьер занят");
        }

        order.setCourier(courier);
        order.setStatus(OrderStatus.DELIVERY_ASSIGNED);
        courier.setAvailable(false);

        courierRepository.save(courier);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersWaitingForCourier() {
        // Только оплаченные заказы, которые ещё ждут назначения курьера.
        return orderRepository.findByPaidAndCourierIsNullAndStatusIn(
                true,
                List.of(OrderStatus.PAID)
        );
    }

    @Override
    public List<Order> getCourierOrders(Long courierId) {
        if (!courierRepository.existsById(courierId)) {
            throw new ResourceNotFoundException("Курьер с ID " + courierId + " не найден");
        }

        return orderRepository.findByCourierIdAndStatusIn(
                courierId,
                List.of(OrderStatus.DELIVERY_ASSIGNED, OrderStatus.DELIVERING)
        );
    }

    @Override
    @Transactional
    public Order updateDeliveryStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Заказ с ID " + orderId + " не найден"));

        if (order.getCourier() == null) {
            throw new BusinessException("Курьер не назначен на этот заказ");
        }

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new BusinessException("Заказ уже доставлен");
        }

        order.setStatus(status);

        if (status == OrderStatus.DELIVERED) {
            Courier courier = order.getCourier();
            courier.setAvailable(true);
            courierRepository.save(courier);
        }

        return orderRepository.save(order);
    }

    @Override
    public Order getDeliveryInfo(Long orderId) {
        // 🔹 Ключевое изменение: возвращаем заказ с деталями, без проверки курьера
        return orderRepository.findByIdWithDetails(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Заказ с ID " + orderId + " не найден"));
    }
}


