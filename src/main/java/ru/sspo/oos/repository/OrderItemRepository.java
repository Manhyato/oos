package ru.sspo.oos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sspo.oos.model.OrderItem;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Получить позиции заказа по идентификатору заказа.
     */
    List<OrderItem> findByOrderId(Long orderId);
}

