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

    /**
     * @return Возвращает список всех книг в виде JSON-строки
     */
    @ApiOperation(value = "Return json list of books")
    @RequestMapping(method = RequestMethod.GET, value = "/api/books/list", produces = "application/json")
    @ResponseBody
    public String getAllBooks() {
        return getAllObjects();
    }

    /**
     * @return Возвращает общее количество книг в базе
     */
    @ApiOperation(value = "Return number of books")
    @RequestMapping(method = RequestMethod.GET, value = "/api/books/count", produces = "application/json")
    @ResponseBody
    public String getBooksCount() {
        return count();
    }

    /**
     * @param id - идентификатор книги
     * @return Возвращает книгу (Book) с идентификатором id в виде JSON-строки. Если запись отсутствует в таблице,
     * возвращает сообщение об ошибке
     */
    @ApiOperation(value = "Return a book (json) by ID")
    @RequestMapping(method = RequestMethod.GET, value = "/api/books/get")
    @ResponseBody
    public String get(@RequestParam("id") Long id){
        return getObjectJSON(id);
    }

    /**
     *  <p>Создает новую запись в таблице</p>
     * @param book - объект типа Book в формате JSON
     * @return Возвращает книгу (JSON) с назначенным идентификатором и заполнеными полями
     */
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

    /**
     *  <p>Удаляет книгу с идентификатором id</p>
     * @param id - идентификатор книги
     * @return Если книга была куплена или не существует, то возвращается сообщение с ошибкой,
     * иначе объект удаляется из таблицы и возвращается сообщение "done"
     */
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
        return (!service.remove(id))?"Error: book with id=" + id + " is not exist":"done";
    }

    /**
     *  <p>Обновляет запись в таблице</p>
     * @param book - объект типа Book в формате JSON
     * @return Если книга не существует или поля заполненны некорректно, то возвращается сообщение с ошибкой,
     * иначе сообщение "done"
     */
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
        return "Error: book with id=" + book.getId() + " is not exist";
    }

    /**
     *  <p>Обновляет поле "стоимость книги" с заданным идентификатором</p>
     * @param id - идентификатор книги
     * @param price - стоимость книги
     * @return Если книга не существует или новое значение стоимости некорректно, то возвращается сообщение с ошибкой,
     * иначе сообщение "done"
     */
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
            return "Error: book with id=" + id + " is not exist";
        }
        return "Error: new price < 0";
    }

    /**
     *  <p>Обновляет количество доступных для заказа книг с заданным идентификатором</p>
     * @param id - идентификатор книги
     * @param number - количество доступных для заказа книг
     * @return Если книга не существует или новое колиичество книг определено некорректно,
     * то возвращается сообщение с ошибкой, иначе сообщение "done"
     */
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
            return "Error: book with id=" + id + " is not exist";
        }
        return "Error: new available_number < 0";
    }

}
