package ru.sspo.oos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sspo.oos.dto.PaymentRequest;
import ru.sspo.oos.model.Order;
import ru.sspo.oos.model.Payment;
import ru.sspo.oos.repository.OrderRepository;
import ru.sspo.oos.repository.PaymentRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment payOrder(Long orderId, PaymentRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (order.isPaid()) {
            throw new IllegalStateException("Order is already paid");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(request.getMethod());
        payment.setPaidAt(LocalDateTime.now());
        payment = paymentRepository.save(payment);

        order.setPaid(true);
        orderRepository.save(order);

        return payment;
    }
}


