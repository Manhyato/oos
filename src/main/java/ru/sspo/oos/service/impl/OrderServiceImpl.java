package ru.sspo.oos.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sspo.oos.dto.CreateOrderRequest;
import ru.sspo.oos.exception.ResourceNotFoundException;
import ru.sspo.oos.model.Client;
import ru.sspo.oos.model.Order;
import ru.sspo.oos.model.OrderItem;
import ru.sspo.oos.model.Pizza;
import ru.sspo.oos.model.OrderStatus;
import ru.sspo.oos.repository.ClientRepository;
import ru.sspo.oos.repository.OrderItemRepository;
import ru.sspo.oos.repository.CourierRepository;
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
    private final CourierRepository courierRepository;

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
        newOrder.setStatus(OrderStatus.NEW);
        newOrder.setAddress(request.getAddress());
        newOrder.setCreatedAt(LocalDateTime.now());
        orderRepository.save(newOrder); // нужно сохранить сначала сам заказ

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        // Создаём позиции заказа
        for (CreateOrderRequest.ItemRequest itemReq : request.getItems()) {

            Pizza pizza = pizzaRepository.findById(itemReq.getPizzaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Пицца с ID " + itemReq.getPizzaId() + " не найдена"));

            OrderItem item = new OrderItem();
            item.setOrder(newOrder);
            item.setPizza(pizza);
            item.setQuantity(itemReq.getQuantity());

            BigDecimal linePrice = pizza.getPrice().multiply(
                    BigDecimal.valueOf(itemReq.getQuantity()));

            item.setPrice(linePrice);
            total = total.add(linePrice);

            orderItemRepository.save(item);
            items.add(item);
        }

        newOrder.setTotalAmount(total);

        // Привязываем позиции к заказу и сохраняем
        newOrder.setItems(items);
        return orderRepository.save(newOrder);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        // загружаем заказ с деталями, чтобы избежать LazyInitializationException на страницах деталей
        return orderRepository.findByIdWithDetails(id)
                .or(() -> orderRepository.findById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Заказ с ID " + id + " не найден"));
    }

    @Override
    public Order updateStatus(Long id, OrderStatus status) {
        Order order = getOrderById(id);

        // допустимые переходы статусов для админ-панели
        switch (status) {
            case PAID -> {
                if (order.isPaid()) {
                    return order;
                }
                order.setPaid(true);
            }
            case DELIVERY_ASSIGNED, DELIVERING -> {
                if (!order.isPaid()) {
                    throw new IllegalStateException("Нельзя назначить доставку для неоплаченного заказа");
                }
            }
            case DELIVERED -> {
                if (!order.isPaid()) {
                    throw new IllegalStateException("Нельзя завершить доставку для неоплаченного заказа");
                }
            }
            default -> { /* NEW → NEW, без дополнительных действий */ }
        }

        order.setStatus(status);
        Order saved = orderRepository.save(order);

        // Если заказ завершён доставкой — освобождаем курьера для новых назначений
        if (status == OrderStatus.DELIVERED && order.getCourier() != null) {
            order.getCourier().setAvailable(true);
            courierRepository.save(order.getCourier());
        }

        return saved;
    }
}

