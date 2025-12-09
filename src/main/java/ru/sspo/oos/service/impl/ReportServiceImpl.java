package ru.sspo.oos.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sspo.oos.dto.FinancialReport;
import ru.sspo.oos.model.Order;
import ru.sspo.oos.model.OrderStatus;
import ru.sspo.oos.model.Payment;
import ru.sspo.oos.repository.OrderRepository;
import ru.sspo.oos.repository.PaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса отчётов для бухгалтерии.
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ru.sspo.oos.service.ReportService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public FinancialReport getFinancialReport(LocalDateTime startDate, LocalDateTime endDate) {
        // Получаем все заказы за период
        List<Order> orders = orderRepository.findAll().stream()
                .filter(order -> order.getCreatedAt() != null)
                .filter(order -> !order.getCreatedAt().isBefore(startDate))
                .filter(order -> !order.getCreatedAt().isAfter(endDate))
                .collect(Collectors.toList());

        // Подсчитываем статистику
        int totalOrders = orders.size();
        int paidOrders = (int) orders.stream().filter(Order::isPaid).count();
        int deliveredOrders = (int) orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .count();

        BigDecimal totalRevenue = orders.stream()
                .filter(Order::isPaid)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Получаем все оплаты за период
        List<Payment> payments = paymentRepository.findAll().stream()
                .filter(payment -> payment.getPaidAt() != null)
                .filter(payment -> !payment.getPaidAt().isBefore(startDate))
                .filter(payment -> !payment.getPaidAt().isAfter(endDate))
                .collect(Collectors.toList());

        BigDecimal totalPaidAmount = payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Формируем детализацию по заказам
        List<FinancialReport.OrderSummary> orderSummaries = orders.stream()
                .filter(Order::isPaid)
                .map(order -> {
                    Payment payment = paymentRepository.findByOrderId(order.getId())
                            .orElse(null);

                    FinancialReport.OrderSummary summary = new FinancialReport.OrderSummary();
                    summary.setOrderId(order.getId());
                    summary.setOrderDate(order.getCreatedAt());
                    summary.setClientName(order.getClient() != null ? order.getClient().getFullName() : "Неизвестно");
                    summary.setAmount(order.getTotalAmount());
                    summary.setPaymentMethod(payment != null ? payment.getMethod() : "Не указан");
                    summary.setStatus(order.getStatus() != null ? order.getStatus().name() : "Неизвестно");
                    
                    // Дата доставки (если доставлен)
                    if (order.getStatus() == OrderStatus.DELIVERED) {
                        // Для MVP используем дату создания заказа как приблизительную дату доставки
                        summary.setDeliveryDate(order.getCreatedAt());
                    }

                    return summary;
                })
                .collect(Collectors.toList());

        FinancialReport report = new FinancialReport();
        report.setReportDate(LocalDateTime.now());
        report.setPeriodStart(startDate);
        report.setPeriodEnd(endDate);
        report.setTotalOrders(totalOrders);
        report.setPaidOrders(paidOrders);
        report.setDeliveredOrders(deliveredOrders);
        report.setTotalRevenue(totalRevenue);
        report.setTotalPaidAmount(totalPaidAmount);
        report.setOrders(orderSummaries);

        return report;
    }

    @Override
    public FinancialReport getTodayFinancialReport() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);
        return getFinancialReport(start, end);
    }

    @Override
    public FinancialReport getMonthlyFinancialReport() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.withDayOfMonth(1).atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);
        return getFinancialReport(start, end);
    }
}

