package ru.sspo.oos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.sspo.oos.model.Order;
import ru.sspo.oos.service.OrderService;

/**
 * Контроллер для веб-страниц заказов.
 */
@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderViewController {

    private final OrderService orderService;

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
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        model.addAttribute("title", "Заказ #" + id);
        return "order/details";
    }

    @GetMapping("/{id}/payment")
    public String paymentPage(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        model.addAttribute("title", "Оплата заказа #" + id);
        return "order/payment";
    }
}
