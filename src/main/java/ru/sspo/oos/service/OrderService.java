package ru.sspo.oos.service;

import ru.sspo.oos.dto.CreateOrderRequest;
import ru.sspo.oos.model.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(CreateOrderRequest request);
    List<Order> getAllOrders();
}



