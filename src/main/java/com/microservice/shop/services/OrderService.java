package com.microservice.shop.services;

import com.microservice.shop.models.Order;
import com.microservice.shop.repositories.OrderRepository;
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
