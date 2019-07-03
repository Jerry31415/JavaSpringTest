package com.microservice.order.repositories;

import com.microservice.order.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
