package com.microservice.order.services;

import com.microservice.order.models.Order;
import com.microservice.order.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Override
    public Order get(Long id){
        return (orderRepository.existsById(id))?orderRepository.findById(id).get():null;
    }

    @Override
    public List<Order> getByUserId(Long id){
        return orderRepository.findByUserId(id);
    }

    @Override
    public synchronized Order update(Order order){
        if(orderRepository.existsById(order.getId())){
            return orderRepository.save(order);
        }
        return null;
    }
}
