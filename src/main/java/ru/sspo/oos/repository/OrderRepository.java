package ru.sspo.oos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sspo.oos.model.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Получить заказы клиента.
     */
    List<Order> findByClientId(Long clientId);

    /**
     * Получить все неоплаченные заказы (для модуля оплаты/курьеров).
     */
    List<Order> findByPaidFalse();
}

