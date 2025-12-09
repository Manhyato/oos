package ru.sspo.oos.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sspo.oos.dto.PaymentRequest;
import ru.sspo.oos.model.Payment;
import ru.sspo.oos.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/order/{orderId}")
    public Payment pay(@PathVariable Long orderId, @RequestBody @Valid PaymentRequest request) {
        return paymentService.payOrder(orderId, request);
    }
}



