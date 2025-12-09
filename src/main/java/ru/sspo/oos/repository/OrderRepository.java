package ru.sspo.oos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sspo.oos.model.Order;
import ru.sspo.oos.model.OrderStatus;

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

    /**
     * Получить оплаченные заказы без назначенного курьера.
     * Для процесса 3.1 "Назначение курьера".
     */
    List<Order> findByPaidAndCourierIsNull(boolean paid);

    /**
     * Получить заказы курьера с указанными статусами.
     * Для процесса 3.2 "Передача заказа курьеру".
     */
    List<Order> findByCourierIdAndStatusIn(Long courierId, List<OrderStatus> statuses);
}

