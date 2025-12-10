package ru.sspo.oos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.sspo.oos.model.Order;
import ru.sspo.oos.service.DeliveryService;
import ru.sspo.oos.service.OrderService;

import java.util.List;

/**
 * Контроллер для админ-панели.
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminViewController {

    private final OrderService orderService;
    private final DeliveryService deliveryService;

    @GetMapping("/orders")
    public String ordersPage(Model model) {
        try {
            List<Order> orders = orderService.getAllOrders();
            List<Order> waitingOrders = deliveryService.getOrdersWaitingForCourier();
            
            model.addAttribute("orders", orders != null ? orders : List.of());
            model.addAttribute("waitingOrders", waitingOrders != null ? waitingOrders : List.of());
            model.addAttribute("title", "Управление заказами");
            return "admin/orders";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при загрузке заказов: " + e.getMessage());
            model.addAttribute("orders", List.of());
            model.addAttribute("waitingOrders", List.of());
            return "admin/orders";
        }
    }

    @GetMapping("/orders/{id}")
    public String orderDetails(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        model.addAttribute("title", "Заказ #" + id);
        return "admin/order-details";
    }

    @GetMapping("/delivery")
    public String deliveryPage(Model model) {
        try {
            List<Order> waitingOrders = deliveryService.getOrdersWaitingForCourier();
            model.addAttribute("waitingOrders", waitingOrders != null ? waitingOrders : List.of());
            model.addAttribute("title", "Управление доставкой");
            return "admin/delivery";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при загрузке заказов: " + e.getMessage());
            model.addAttribute("waitingOrders", List.of());
            return "admin/delivery";
        }
    }
    
}

