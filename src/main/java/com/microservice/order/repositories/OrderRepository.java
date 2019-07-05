package com.microservice.order.repositories;

import com.microservice.order.models.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface OrderRepository extends CrudRepository<Order, Long> {
    /**
     * <p>Выполняет запрос к таблице заказов</p>
     * @param user_id - идентификатор пользователя
     * @return Возвращает список заказов для пользователя с идентификатором user_id
     */
    @Query(value = "select * from orders where user_id = ?1", nativeQuery = true)
    ArrayList<Order> findByUserId(Long user_id);
}
