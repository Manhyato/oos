package ru.sspo.oos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sspo.oos.dto.PaymentRequest;
import ru.sspo.oos.exception.BusinessException;
import ru.sspo.oos.exception.ResourceNotFoundException;
import ru.sspo.oos.model.Order;
import ru.sspo.oos.model.OrderStatus;
import ru.sspo.oos.model.Payment;
import ru.sspo.oos.model.PaymentStatus;
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
                .orElseThrow(() -> new ResourceNotFoundException("Заказ с ID " + orderId + " не найден"));

        if (order.isPaid()) {
            throw new BusinessException("Заказ уже оплачен");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(request.getMethod());
        payment.setPaidAt(LocalDateTime.now());
        payment.setAmount(order.getTotalAmount());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment = paymentRepository.save(payment);

        order.setPaid(true);
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        return payment;
    }
}


