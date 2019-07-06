package com.microservice.shop.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.shop.models.User;
import com.microservice.shop.repositories.UserRepository;
import com.microservice.shop.services.IOrderService;
import com.microservice.shop.models.Order;
import com.microservice.shop.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;
import java.util.List;

@Controller
public class UserController extends CommonController<User, UserRepository, UserService>{
    @Autowired
    private IOrderService orderService;

    /**
     * @return Возвращает список всех пользователей в виде JSON-строки
     */
    @ApiOperation(value = "Return json list of users")
    @RequestMapping(method = RequestMethod.GET, value = "/api/users/list", produces = "application/json")
    @ResponseBody
    public String getAllUsers() {
        return getAllObjects();
    }

    /**
     * @return Возвращает общее количество пользователей
     */
    @ApiOperation(value = "Return number of users")
    @RequestMapping(method = RequestMethod.GET, value = "/api/users/count", produces = "application/json")
    @ResponseBody
    public String getUsersCount() {
        return count();
    }

    /**
     * @param id - идентификатор пользователя
     * @return Возвращает пользователя с идентификатором id в виде JSON-строки, если он существует в таблице,
     * иначе сообщение об ошибке
     */
    @ApiOperation(value = "Return a user (json) by ID")
    @RequestMapping(method = RequestMethod.GET, value = "/api/users/get")
    @ResponseBody
    public String get(@RequestParam("id") Long id){
        return getObjectJSON(id);
    }

    /**
     *  <p>Создает нового пользователя</p>
     * @param balance - баланс пользователя
     * @return Возвращает пользователя (JSON) с назначенным идентификатором и установленным балансом
     */
    @ApiOperation(value = "Create a new user via json")
    @RequestMapping(method = RequestMethod.PUT, value = "/api/users/create")
    @ResponseBody
    public String create(@RequestParam("balance") BigInteger balance){
        User new_user = service.create(new User(0L, balance));
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(new ObjectMapper().writeValueAsString(new_user));
            return builder.toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    /**
     *  <p>Удаляет пользователя с идентификатором id</p>
     * @param id - идентификатор пользователя
     * @return Если пользователь имеет заказы или не существует, то возвращается сообщение с ошибкой,
     * иначе сообщение "done"
     */
    @ApiOperation(value = "Remove a user by ID")
    @RequestMapping(method = RequestMethod.DELETE, value = "/api/users/remove")
    @ResponseBody
    public String remove(@RequestParam("id") Long id){
        List<Order> user_orders = orderService.getByUserId(id);
        if(user_orders!=null){
            if(!user_orders.isEmpty()){
                return "Error: user with id=" + id.toString() + " has orders";
            }
        }
        return (!service.remove(id))?"Error: user with id=" + id.toString() + " is not exist":"done";
    }

    /**
     *  <p>Обновляет запись в таблице</p>
     * @param user - пользователь
     * @return Если пользователь не существует, то возвращается сообщение с ошибкой, иначе сообщение "done"
     */
    @ApiOperation(value = "Update a user")
    @RequestMapping(method = RequestMethod.POST, value = "/api/users/update")
    @ResponseBody
    public String update(@RequestBody User user){
        return (service.update(new User(user.getId(), user.getBalance()))==null)?
                "Error: user with id=" + user.getId().toString() + " is not exist":
                "done";
    }


}
