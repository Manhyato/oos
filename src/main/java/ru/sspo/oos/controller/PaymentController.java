package ru.sspo.oos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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

/**
 * Контроллер для управления оплатами.
 * Реализует процесс 2.0 "Оплата заказа" из DFD.
 */
@Tag(name = "Оплаты", description = "API для обработки оплат заказов")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Оплатить заказ", description = "Обрабатывает оплату заказа указанным способом")
    @PostMapping("/order/{orderId}")
    public Payment pay(
            @Parameter(description = "ID заказа для оплаты", required = true) @PathVariable Long orderId,
            @RequestBody @Valid PaymentRequest request
    ) {
        return paymentService.payOrder(orderId, request);
    }
}



