package com.microservice.order.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.order.models.User;
import com.microservice.order.services.IOrderService;
import com.microservice.order.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.microservice.order.models.Order;

import java.math.BigInteger;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderService orderService;

    @RequestMapping(method = RequestMethod.GET, value = "/api/users/list", produces = "application/json")
    @ResponseBody
    public String getAllUsers() {
        List<User> users = userService.getAll();
        if(users!=null){
            try {
                StringBuilder builder = new StringBuilder();
                for (User user : users){
                    builder.append(new ObjectMapper().writeValueAsString(user) + "\n");
                }
                return builder.toString();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/users/count", produces = "application/json")
    @ResponseBody
    public String getUsersCount() {
        return userService.count().toString();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/api/users/create")
    @ResponseBody
    public String create(@RequestParam("balance") BigInteger balance){
        User new_user = userService.create(new User(0L, balance));
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(new ObjectMapper().writeValueAsString(new_user));
            return builder.toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/api/users/remove")
    @ResponseBody
    public String remove(@RequestParam("id") Long id){
        List<Order> user_orders = orderService.getByUserId(id);
        if(user_orders!=null){
            if(!user_orders.isEmpty()){
                return "Error: user with id=" + id.toString() + " has orders";
            }
        }
        return (!userService.remove(id))?"Error: user with id=" + id.toString() + " is not exist":"done";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/users/update")
    @ResponseBody
    public String update(@RequestBody User user){
        return (userService.update(new User(user.getId(), user.getBalance()))==null)?
                "Error: user with id=" + user.getId().toString() + " is not exist":
                "done";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/users/get")
    @ResponseBody
    public String get(@RequestParam("id") Long id){
        User user = userService.get(id);
        if(user==null){
            return "Error: user with id=" + id.toString() + " is not exist";
        }
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(new ObjectMapper().writeValueAsString(user));
            return builder.toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}
