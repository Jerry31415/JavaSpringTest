package com.microservice.order.services;

import com.microservice.order.models.Order;

public interface IOrderService extends ICommon<Order> {
    Order create(Order new_order);
}
