package com.microservice.order.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.order.models.Book;
import com.microservice.order.models.Order;
import com.microservice.order.models.User;
import com.microservice.order.services.IBookService;
import com.microservice.order.services.IOrderService;
import com.microservice.order.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.microservice.order.models.Product;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IBookService bookService;

    @RequestMapping(method = RequestMethod.GET, value = "/api/orders/list", produces = "application/json")
    @ResponseBody
    public String getAllOrders() {
        List<Order> orders = orderService.getAll();
        if (orders!=null) {
            try {
                StringBuilder builder = new StringBuilder();
                for (Order order : orders) {
                    builder.append(new ObjectMapper().writeValueAsString(order) + "\n");
                }
                return builder.toString();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/orders/count", produces = "application/json")
    @ResponseBody
    public String getOrdersCount() {
        return orderService.count().toString();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/api/orders/create")
    @ResponseBody
    public String create(@RequestBody Order order){
        User user = userService.get(order.getUserId());
        if(user == null)
            return "Error: user with id="+order.getUserId()+" is not exist";
        ArrayList<Product> ord_books = order.getBooks();
        BigInteger total_payment = BigInteger.valueOf(0);
        for (Product elem : ord_books) {
            Book book = bookService.get(elem.getBookId());
            if(book == null)
                return "Error: the book with id="+elem.getBookId()+" is not exist";
            if(book.getAvaliable_number()==0)
                return "Error: book (id="+elem.getBookId()+") not available for order";
            if(elem.getNumber()>book.getAvaliable_number() || elem.getNumber()<1){
                return "Error: incorrect number value of book (id="+elem.getBookId()
                        +") (must be equal from 1 to "+book.getAvaliable_number()+")";
            }
            total_payment = total_payment.add(book.getPrice().multiply(BigInteger.valueOf(elem.getNumber())));
        }
        order.setTotalPayment(total_payment);
        order.setStatus("pending");
        Order new_order = orderService.create(order);
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(new ObjectMapper().writeValueAsString(new_order));
            return builder.toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

}
