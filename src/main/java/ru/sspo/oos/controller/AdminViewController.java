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
import ru.sspo.oos.service.PaymentService;

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
    private final PaymentService paymentService;

    @GetMapping({"", "/"})
    public String adminRoot() {
        return "admin/login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("title", "Вход в админ-панель");
        return "admin/login";
    }

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
        model.addAttribute("payment", paymentService.getPaymentByOrderId(id).orElse(null));
        String statusClass = "";
        if (order.getStatus() != null) {
            switch (order.getStatus()) {
                case NEW -> statusClass = "status-new";
                case PAID -> statusClass = "status-paid";
                case DELIVERING, DELIVERY_ASSIGNED -> statusClass = "status-delivering";
                case DELIVERED -> statusClass = "status-delivered";
            }
        }
        model.addAttribute("statusClass", statusClass);
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
    
    @GetMapping("/reports")
    public String reportsPage(Model model) {
        model.addAttribute("title", "Финансовые отчёты");
        return "admin/reports";
    }

    @GetMapping("/menu")
    public String menuPage(Model model) {
        model.addAttribute("title", "Управление меню");
        return "admin/categories";
    }
    
}

