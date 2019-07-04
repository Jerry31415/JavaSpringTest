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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<Long, Long> bookCounter = new HashMap<>();
        ArrayList<Book> booksList = new ArrayList<>();

        for (Product elem : ord_books) {
            Book book = bookService.get(elem.getBookId());
            if (book == null)
                return "Error: the book with id=" + elem.getBookId() + " is not exist";
            booksList.add(book);
            if(bookCounter.containsKey(elem.getBookId())){
                bookCounter.put(elem.getBookId(), bookCounter.get(elem.getBookId())+elem.getNumber());
            }
            else bookCounter.put(elem.getBookId(), elem.getNumber());
        }
        order.clearBooksList();
        for (Map.Entry<Long, Long> product : bookCounter.entrySet()) {
            order.addBook(product.getKey(), product.getValue());
        }

        for (Book book : booksList) {
            Long ord_book_number = bookCounter.get(book.getId());
            Long available_number = book.getAvaliable_number();
            if(available_number==0)
                return "Error: book (id="+book.getId()+") not available for order";
            if(ord_book_number>available_number || ord_book_number<1){
                return "Error: incorrect number value of book (id="+book.getId()
                        +") (must be equal from 1 to "+available_number+")";
            }
            total_payment = total_payment.add(book.getPrice().multiply(BigInteger.valueOf(ord_book_number)));
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


    @RequestMapping(method = RequestMethod.GET, value = "/api/orders/get")
    @ResponseBody
    public String get(@RequestParam("id") Long id){
        Order order = orderService.get(id);
        if(order==null){
            return "Error: order with id=" + id + " is not exist";
        }
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(new ObjectMapper().writeValueAsString(order));
            return builder.toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/orders/get/byUserId")
    @ResponseBody
    public String getByUserId(@RequestParam("id") Long id){
        List<Order> orders = orderService.getByUserId(id);
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(new ObjectMapper().writeValueAsString(orders));
            return builder.toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/orders/pay")
    @ResponseBody
    public String payOrder(@RequestParam("id") Long id){
        Order order = orderService.get(id);
        if(order == null)
            return "Error: order with id="+id+" is not exist";
        if(order.getStatus()=="paid")
            return "Error: order with id="+id+" already paid";
        User user = userService.get(order.getUserId());
        if(user == null)
            return "Error: user with id="+order.getUserId()+" is not exist";
        // Check avaliable
        ArrayList<Product> books = order.getBooks();
        BigInteger total_payment = BigInteger.valueOf(0);
        for(Product prod : books){
            Book book = bookService.get(prod.getBookId());
            if(book==null){
                return "Error: book with id=" + prod.getBookId() + " is not exist";
            }
            if(book.getAvaliable_number()<prod.getNumber()){
                return "Error: the number of book (id="+prod.getBookId()
                            +") must be equal from 1 to "+book.getAvaliable_number();
            }
            total_payment = total_payment.add(book.getPrice().multiply(BigInteger.valueOf(prod.getNumber())));
        }
        if(total_payment.compareTo(order.getTotalPayment())!=0){
            String msg = "Info: price has changed. The order (id="+order.getId()+") was updated. "+
                    "Old price="+order.getTotalPayment()+", new price="+total_payment+". Try again\n";
            order.setTotalPayment(total_payment);
            Order updated_order = orderService.update(order);
            try {
                StringBuilder builder = new StringBuilder();
                builder.append(msg);
                builder.append(new ObjectMapper().writeValueAsString(updated_order));
                return builder.toString();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return e.toString();
            }
        }
        if(user.getBalance().compareTo(total_payment)<0){
            return "Error: the balance of user (id="+user.getId()+") is less payment sum="+total_payment;
        }
        // pay products
        for(Product prod : books){
            Book book = bookService.get(prod.getBookId());
            book.setTotalSoldNumber(book.getTotalSoldNumber()+prod.getNumber());
            book.setAvaliable_number(book.getAvaliable_number()-prod.getNumber());
            bookService.update(book);
        }
        user.setBalance(user.getBalance().subtract(total_payment));
        userService.update(user);
        order.setStatus("paid");
        Order updated_order = orderService.update(order);
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(new ObjectMapper().writeValueAsString(updated_order));
            return builder.toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/orders/cancel")
    @ResponseBody
    public String cancelOrder(@RequestParam("id") Long id){
        Order order = orderService.get(id);
        if(order == null)
            return "Error: order with id="+id+" is not exist";
        if(order.getStatus().equals("paid")) {
            return "Error: permission denied. Order with id=" + id + " was paid";
        }
        if(!orderService.remove(order.getId())){
            return "Error: order with id="+id+" is not exist";
        }
        return "done";
    }

}
