package com.microservice.order.services;

import com.microservice.order.models.Order;

import java.util.List;

public interface IOrderService extends ICommon<Order> {
    Order create(Order new_order);
    Order get(Long id);
    List<Order> getByUserId(Long id);
    Order update(Order order);
}
