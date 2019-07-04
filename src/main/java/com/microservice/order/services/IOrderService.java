package com.microservice.order.services;

import com.microservice.order.models.Order;

import java.util.List;

public interface IOrderService extends ICommon<Order> {
    List<Order> getByUserId(Long id);
}
