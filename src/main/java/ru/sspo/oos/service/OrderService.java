package ru.sspo.oos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sspo.oos.dto.CreateOrderRequest;
import ru.sspo.oos.model.*;
import ru.sspo.oos.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ClientRepository clientRepository;
    private final PizzaRepository pizzaRepository;

    @Transactional
    public Order createOrder(CreateOrderRequest request) {

        Client client = clientRepository.findByPhone(request.getPhone())
                .orElseGet(() -> {
                    Client c = new Client();
                    c.setFullName(request.getFullName());
                    c.setPhone(request.getPhone());
                    return clientRepository.save(c);
                });

        Order order = new Order();
        order.setClient(client);
        order.setCreatedAt(LocalDateTime.now());
        order.setPaid(false);
        order = orderRepository.save(order);

        List<OrderItem> items = new ArrayList<>();
        for (CreateOrderRequest.Item it : request.getItems()) {
            Pizza pizza = pizzaRepository.findById(it.getPizzaId())
                    .orElseThrow(() -> new IllegalArgumentException("Pizza not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setPizza(pizza);
            orderItem.setQuantity(it.getQuantity());
            orderItem.setPrice(pizza.getPrice());
            items.add(orderItem);
        }

        orderItemRepository.saveAll(items);
        order.setItems(items);

        return order;
    }

    public List<Order> getAll() {
        return orderRepository.findAll();
    }
}

