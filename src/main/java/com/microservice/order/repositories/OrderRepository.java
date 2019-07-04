package com.microservice.order.repositories;

import com.microservice.order.models.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface OrderRepository extends CrudRepository<Order, Long> {
    @Query(value = "select * from orders where user_id = ?1", nativeQuery = true)
    ArrayList<Order> findByUserId(Long user_id);
}
