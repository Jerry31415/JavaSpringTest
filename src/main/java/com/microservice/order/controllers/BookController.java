package com.microservice.order.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.microservice.order.models.Book;
import com.microservice.order.services.*;
import com.microservice.order.repositories.*;
import java.math.BigInteger;

@Controller
public class BookController extends CommonController<Book, BookRepository, BookService>{
    @Autowired
    private IOrderService orderService;

    @ApiOperation(value = "Return json list of books")
    @RequestMapping(method = RequestMethod.GET, value = "/api/books/list", produces = "application/json")
    @ResponseBody
    public String getAllBooks() {
        return getAllObjects();
    }

    @ApiOperation(value = "Return number of books")
    @RequestMapping(method = RequestMethod.GET, value = "/api/books/count", produces = "application/json")
    @ResponseBody
    public String getBooksCount() {
        return count();
    }

    @ApiOperation(value = "Return a book (json) by ID")
    @RequestMapping(method = RequestMethod.GET, value = "/api/books/get")
    @ResponseBody
    public String get(@RequestParam("id") Long id){
        return getObjectJSON(id);
    }

    @ApiOperation(value = "Create a new book via json")
    @RequestMapping(method = RequestMethod.PUT, value = "/api/books/create")
    @ResponseBody
    public String create(@RequestBody Book book){
        String check = book.checkFields();
        if(!check.isEmpty()) return check;
        Book new_book = service.create(book);
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(new ObjectMapper().writeValueAsString(new_book));
            return builder.toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @ApiOperation(value = "Remove a book by ID")
    @RequestMapping(method = RequestMethod.DELETE, value = "/api/books/remove")
    @ResponseBody
    public String remove(@RequestParam("id") Long id){
        Book book = service.get(id);
        if(book==null){
            return "Error: book with id=" + id + " is not exist";
        }
        if(book.getTotalSoldNumber()>0){
            return "Error: operation denied. The book (id="+id+") was bought";
        }
        return (!service.remove(id))?"Error: book with id=" + id.toString() + " is not exist":"done";
    }

    @ApiOperation(value = "Update a book")
    @RequestMapping(method = RequestMethod.POST, value = "/api/books/update")
    @ResponseBody
    public String update(@RequestBody Book book){
        if(book.getId()>1){
            Book old_book = service.get(book.getId());
            if(old_book!=null){
                String check = book.checkFields();
                if(!check.isEmpty()){
                    return check;
                }
                service.update(book);
                return "done";
            }
        }
        return "Error: book with id=" + book.getId().toString() + " is not exist";
    }

    @ApiOperation(value = "Update price value")
    @RequestMapping(method = RequestMethod.POST, value = "/api/books/update/price")
    @ResponseBody
    public String update_price(@RequestParam("id") Long id, @RequestParam("value") BigInteger price){
        if(price.compareTo(BigInteger.valueOf(0))>=0) {
            if (id > 1) {
                Book book = service.get(id);
                if (book != null) {
                    book.setPrice(price);
                    service.update(book);
                    return "done";
                }
            }
            return "Error: book with id=" + id.toString() + " is not exist";
        }
        return "Error: new price < 0";
    }

    @ApiOperation(value = "Update available number of books")
    @RequestMapping(method = RequestMethod.POST, value = "/api/books/update/available_number")
    @ResponseBody
    public String update_number(@RequestParam("id") Long id, @RequestParam("value") Long number){
        if(number>=0) {
            if (id > 1) {
                Book book = service.get(id);
                if (book != null) {
                    book.setAvaliableNumber(number);
                    service.update(book);
                    return "done";
                }
            }
            return "Error: book with id=" + id.toString() + " is not exist";
        }
        return "Error: new available_number < 0";
    }

}
