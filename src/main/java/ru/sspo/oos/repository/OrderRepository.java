package ru.sspo.oos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sspo.oos.model.Order;
import ru.sspo.oos.model.OrderStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByClientId(Long clientId);

    List<Order> findByPaidFalse();

    /**
     * Оплаченные заказы без назначенного курьера и в заданных статусах.
     * Используем фильтр по статусам, чтобы не подхватывать доставленные заказы,
     * которым курьер не был назначен (например, из админки).
     */
    List<Order> findByPaidAndCourierIsNullAndStatusIn(boolean paid, List<OrderStatus> statuses);

    List<Order> findByCourierIdAndStatusIn(Long courierId, List<OrderStatus> statuses);

    /**
     * Есть ли у курьера активные заказы в указанных статусах (для проверки занятости).
     */
    boolean existsByCourierIdAndStatusIn(Long courierId, List<OrderStatus> statuses);

    /**
     * Получить заказ с полной информацией: курьер, клиент, позиции заказа с пиццей.
     * Нужно для отображения страницы /orders/{id} без LazyInitializationException
     */
    @Query("SELECT o FROM Order o " +
           "LEFT JOIN FETCH o.courier " +
           "LEFT JOIN FETCH o.client " +
           "LEFT JOIN FETCH o.items i " +
           "LEFT JOIN FETCH i.pizza " +
           "WHERE o.id = :id")
    Optional<Order> findByIdWithDetails(@Param("id") Long id);
}


