package com.microservice.order.services;

import com.microservice.order.models.Order;
import com.microservice.order.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> getAll() {
        List<Order> orders = null;
        if(orderRepository.count()>0) {
            orders = (List<Order>) orderRepository.findAll();
        }
        return orders;
    }

    @Override
    public Long count() {
        return orderRepository.count();
    }

    @Override
    public synchronized Order create(Order new_order){
        return orderRepository.save(new_order);
    }
}
