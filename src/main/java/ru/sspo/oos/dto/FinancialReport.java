package ru.sspo.oos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Финансовый отчёт для бухгалтерии.
 * Соответствует выходным данным процесса 3.0 "Организация доставки".
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialReport {
    private LocalDateTime reportDate;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private int totalOrders;
    private int paidOrders;
    private int deliveredOrders;
    private BigDecimal totalRevenue;
    private BigDecimal totalPaidAmount;
    private List<OrderSummary> orders;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderSummary {
        private Long orderId;
        private LocalDateTime orderDate;
        private String clientName;
        private BigDecimal amount;
        private String paymentMethod;
        private String status;
        private LocalDateTime deliveryDate;
    }
}

