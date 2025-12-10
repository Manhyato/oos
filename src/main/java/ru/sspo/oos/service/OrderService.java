package ru.sspo.oos.service;

import ru.sspo.oos.dto.CreateOrderRequest;
import ru.sspo.oos.model.Order;
import ru.sspo.oos.model.OrderStatus;

import java.util.List;

public interface OrderService {
    Order createOrder(CreateOrderRequest request);
    List<Order> getAllOrders();
    Order getOrderById(Long id);
    Order updateStatus(Long id, OrderStatus status);
}



