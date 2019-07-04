package com.microservice.order.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.order.models.Book;
import com.microservice.order.models.Order;
import com.microservice.order.services.IBookService;
import com.microservice.order.services.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@Controller
public class BookController {
    @Autowired
    private IBookService bookService;

    @Autowired
    private IOrderService orderService;

    @RequestMapping(method = RequestMethod.GET, value = "/api/books/list", produces = "application/json")
    @ResponseBody
    public String getAllBooks() {
        List<Book> books = bookService.getAll();
        if(books!=null){
            try {
                StringBuilder builder = new StringBuilder();
                for (Book book : books){
                    builder.append(new ObjectMapper().writeValueAsString(book) + "\n");
                }
                return builder.toString();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/books/count", produces = "application/json")
    @ResponseBody
    public String getBooksCount() {
        return bookService.count().toString();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/api/books/create")
    @ResponseBody
    public String create(@RequestBody Book book){
        String check = book.checkFields();
        if(!check.isEmpty()){
            return check;
        }
        Book new_book = bookService.create(book);
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(new ObjectMapper().writeValueAsString(new_book));
            return builder.toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/api/books/remove")
    @ResponseBody
    public String remove(@RequestParam("id") Long id){
        Book book = bookService.get(id);
        if(book==null){
            return "Error: book with id=" + id + " is not exist";
        }
        if(book.getTotalSoldNumber()>0){
            return "Error: operation denied. The book (id="+id+") was bought";
        }
        return (!bookService.remove(id))?"Error: book with id=" + id.toString() + " is not exist":"done";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/books/update")
    @ResponseBody
    public String update(@RequestBody Book book){
        if(book.getId()>1){
            Book old_book = bookService.get(book.getId());
            if(old_book!=null){
                String check = book.checkFields();
                if(!check.isEmpty()){
                    return check;
                }
                bookService.update(book);
                return "done";
            }
        }
        return "Error: book with id=" + book.getId().toString() + " is not exist";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/books/get")
    @ResponseBody
    public String get(@RequestParam("id") Long id){
        Book book = bookService.get(id);
        if(book==null){
            return "Error: book with id=" + id.toString() + " is not exist";
        }
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(new ObjectMapper().writeValueAsString(book));
            return builder.toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/books/update/price")
    @ResponseBody
    public String update_price(@RequestParam("id") Long id, @RequestParam("value") BigInteger price){
        if(price.compareTo(BigInteger.valueOf(0))>=0) {
            if (id > 1) {
                Book book = bookService.get(id);
                if (book != null) {
                    book.setPrice(price);
                    bookService.update(book);
                    return "done";
                }
            }
            return "Error: book with id=" + id.toString() + " is not exist";
        }
        return "Error: new price < 0";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/books/update/avaliable_number")
    @ResponseBody
    public String update_number(@RequestParam("id") Long id, @RequestParam("value") Long number){
        if(number>=0) {
            if (id > 1) {
                Book book = bookService.get(id);
                if (book != null) {
                    book.setAvaliable_number(number);
                    bookService.update(book);
                    return "done";
                }
            }
            return "Error: book with id=" + id.toString() + " is not exist";
        }
        return "Error: new avaliable_number < 0";
    }

}
