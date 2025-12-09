package ru.sspo.oos.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sspo.oos.dto.CreateOrderRequest;
import ru.sspo.oos.model.Client;
import ru.sspo.oos.model.Order;
import ru.sspo.oos.model.OrderItem;
import ru.sspo.oos.model.Pizza;
import ru.sspo.oos.repository.ClientRepository;
import ru.sspo.oos.repository.OrderItemRepository;
import ru.sspo.oos.repository.OrderRepository;
import ru.sspo.oos.repository.PizzaRepository;
import ru.sspo.oos.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final PizzaRepository pizzaRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Order createOrder(CreateOrderRequest request) {

        // Находим клиента по телефону или создаём нового
        Client client = clientRepository.findByPhone(request.getClientPhone())
                .orElseGet(() -> {
                    Client newClient = new Client();
                    newClient.setFullName(request.getClientName());
                    newClient.setPhone(request.getClientPhone());
                    return clientRepository.save(newClient);
                });

        // Создаём новый заказ
        Order newOrder = new Order();
        newOrder.setClient(client);
        newOrder.setPaid(false);
        newOrder.setCreatedAt(LocalDateTime.now());
        orderRepository.save(newOrder); // нужно сохранить сначала сам заказ

        List<OrderItem> items = new ArrayList<>();

        // Создаём позиции заказа
        for (CreateOrderRequest.ItemRequest itemReq : request.getItems()) {

            Pizza pizza = pizzaRepository.findById(itemReq.getPizzaId())
                    .orElseThrow(() -> new RuntimeException("Пицца не найдена!"));

            OrderItem item = new OrderItem();
            item.setOrder(newOrder);
            item.setPizza(pizza);
            item.setQuantity(itemReq.getQuantity());
            item.setPrice(pizza.getPrice().multiply(
                    BigDecimal.valueOf(itemReq.getQuantity()))
            );

            orderItemRepository.save(item);
            items.add(item);
        }

        // Привязываем позиции к заказу и сохраняем
        newOrder.setItems(items);
        return orderRepository.save(newOrder);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}

