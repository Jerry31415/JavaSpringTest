package com.microservice.order.services;

import com.microservice.order.models.User;
import com.microservice.order.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAll() {
        List<User> users = null;
        if(userRepository.count()>0){
            users = (List<User>) userRepository.findAll();
        }
        return users;
    }

    @Override
    public Long count() {
        return userRepository.count();
    }

    @Override
    public synchronized User create(BigInteger balance){
        return userRepository.save(new User(0L, balance));
    }

    @Override
    public synchronized boolean remove(Long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public synchronized User update(Long id, BigInteger balance){
        if(userRepository.existsById(id)){
            User user = userRepository.findById(id).get();
            user.setBalance(balance);
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public synchronized User update(User user){
        if(userRepository.existsById(user.getId())){
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public User get(Long id){
        return (userRepository.existsById(id))?userRepository.findById(id).get():null;
    }


}
