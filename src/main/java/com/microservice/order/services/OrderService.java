package com.microservice.order.services;

import com.microservice.order.models.Order;
import com.microservice.order.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService extends CommonService<Order, OrderRepository> implements IOrderService {

    /**
     * @param id - идентификатор пользователя
     * @return Возвращает список заказов пользователя с заданным идентификатором
     */
    @Override
    public List<Order> getByUserId(Long id){
        return repo.findByUserId(id);
    }
}
