package com.microservice.shop.services;

import com.microservice.shop.models.User;
import com.microservice.shop.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends CommonService<User, UserRepository> implements IUserService  {
}
