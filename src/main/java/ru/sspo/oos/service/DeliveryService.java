package ru.sspo.oos.service;

import ru.sspo.oos.model.Order;

import java.util.List;

/**
 * Сервис для управления доставкой заказов.
 * Реализует процесс 3.0 "Организация доставки" из DFD.
 */
public interface DeliveryService {

    /**
     * Назначить курьера на оплаченный заказ (процесс 3.1).
     * @param orderId ID заказа
     * @param courierId ID курьера (если null, назначается первый свободный)
     * @return Обновлённый заказ с назначенным курьером
     */
    Order assignCourier(Long orderId, Long courierId);

    /**
     * Получить список оплаченных заказов, ожидающих назначения курьера.
     */
    List<Order> getOrdersWaitingForCourier();

    /**
     * Получить список заказов, назначенных конкретному курьеру.
     * @param courierId ID курьера
     */
    List<Order> getCourierOrders(Long courierId);

    /**
     * Обновить статус доставки заказа (процесс 3.3).
     * Курьер сообщает о текущем статусе доставки.
     * @param orderId ID заказа
     * @param status Новый статус заказа
     * @return Обновлённый заказ
     */
    Order updateDeliveryStatus(Long orderId, ru.sspo.oos.model.OrderStatus status);

    /**
     * Получить информацию о доставке для курьера (процесс 3.2).
     * @param orderId ID заказа
     * @return Заказ с полной информацией для доставки
     */
    Order getDeliveryInfo(Long orderId);
}

