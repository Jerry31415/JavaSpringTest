package com.microservice.shop.services;

import com.microservice.shop.models.Order;

import java.util.List;

public interface IOrderService extends ICommon<Order> {
    /**
     * @param id - идентификатор пользователя
     * @return Возвращает список заказов пользователя с заданным идентификатором
     */
    List<Order> getByUserId(Long id);
}
