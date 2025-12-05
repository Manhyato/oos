package ru.sspo.oos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sspo.oos.model.Payment;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Найти оплату по связному заказу (единственная для MVP — 1:1).
     */
    Optional<Payment> findByOrderId(Long orderId);
}


