package com.microservice.order.services;

import com.microservice.order.models.User;
import com.microservice.order.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends CommonService<User, UserRepository> implements IUserService  {
}
