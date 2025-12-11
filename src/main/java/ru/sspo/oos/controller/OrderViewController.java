package ru.sspo.oos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.sspo.oos.model.Order;
import ru.sspo.oos.service.OrderService;
import ru.sspo.oos.service.PaymentService;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderViewController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    @GetMapping("/create")
    public String createOrderPage(Model model) {
        model.addAttribute("title", "Оформление заказа");
        return "order/create";
    }

    @GetMapping("/track")
    public String trackOrderPage(Model model) {
        model.addAttribute("title", "Отследить заказ");
        return "order/track";
    }

    @GetMapping("/{id}")
    public String orderDetails(@PathVariable Long id, Model model) {
        try {
            Order order = orderService.getOrderById(id);
            if (order == null) {
                model.addAttribute("error", "Заказ не найден");
                model.addAttribute("status", 404);
                return "error";
            }
            model.addAttribute("order", order);
            model.addAttribute("payment", paymentService.getPaymentByOrderId(id).orElse(null));
            model.addAttribute("title", "Заказ #" + id);

            // Вычисляем CSS-класс для статуса здесь, в контроллере
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

            return "order/details";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при загрузке заказа: " + e.getMessage());
            model.addAttribute("status", 500);
            return "error";
        }
    }

    @GetMapping("/{id}/payment")
    public String paymentPage(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        model.addAttribute("title", "Оплата заказа #" + id);
        return "order/payment";
    }

    @GetMapping("/{id}/receipt")
    public String receiptPage(@PathVariable Long id, Model model) {
        try {
            Order order = orderService.getOrderById(id);
            if (order == null) {
                model.addAttribute("error", "Заказ не найден");
                model.addAttribute("status", 404);
                return "error";
            }
            model.addAttribute("order", order);
            model.addAttribute("payment", paymentService.getPaymentByOrderId(id).orElse(null));
            model.addAttribute("title", "Чек по заказу #" + id);
            return "order/receipt";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при загрузке чека: " + e.getMessage());
            model.addAttribute("status", 500);
            return "error";
        }
    }
}


