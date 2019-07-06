package com.microservice.shop.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.shop.models.*;
import com.microservice.shop.services.*;
import com.microservice.shop.repositories.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController extends CommonController<Order, OrderRepository, OrderService>{
    @Autowired
    private IUserService userService;
    @Autowired
    private IBookService bookService;

    /**
     * @return Возвращает список всех заказов в виде JSON-строки
     */
    @ApiOperation(value = "Return json list of orders")
    @RequestMapping(method = RequestMethod.GET, value = "/api/orders/list", produces = "application/json")
    @ResponseBody
    public String getAllOrders() {
        return getAllObjects();
    }

    /**
     * @return Возвращает общее количество заказов
     */
    @ApiOperation(value = "Return number of orders")
    @RequestMapping(method = RequestMethod.GET, value = "/api/orders/count", produces = "application/json")
    @ResponseBody
    public String getOrdersCount() {
        return count();
    }

    /**
     * @param id - идентификатор заказа (объекта типа Order) в таблице
     * @return Возвращает заказ с идентификатором id в виде JSON-строки, если он существует в таблице,
     * иначе сообщение об ошибке
     */
    @ApiOperation(value = "Return a order (json) by ID")
    @RequestMapping(method = RequestMethod.GET, value = "/api/orders/get")
    @ResponseBody
    public String get(@RequestParam("id") Long id){
        return getObjectJSON(id);
    }

    /**
     * <p>Создает новый заказ. Устанавливает статус заказа "pending" (в обработке)</p>
     * @param order - объект типа Order в формате JSON
     * @return Возвращает заказ с идентификатором id в JSON формате. Если поля заполнены некорректно -
     * возвращет сообщение об ошибке
     */
    @ApiOperation(value = "Create a new order via json")
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
            Long available_number = book.getAvaliableNumber();
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
        Order new_order = service.create(order);
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(new ObjectMapper().writeValueAsString(new_order));
            return builder.toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    /**
     * @param id - идентификатор пользователя
     * @return Возвращает список заказов в формате JSON для пользователя с заданным идентификатором
     */
    @ApiOperation(value = "Return orders (json-list) for user by ID")
    @RequestMapping(method = RequestMethod.GET, value = "/api/orders/get/byUserId")
    @ResponseBody
    public String getByUserId(@RequestParam("id") Long id){
        List<Order> orders = service.getByUserId(id);
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(new ObjectMapper().writeValueAsString(orders));
            return builder.toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    /**
     * <p>Оплата заказа. Устанавливает статус заказа "paid".
     * Списывает стоимость заказа с счета пользователя.
     * Модифицирует колличество доотупных для заказа книг</p>
     * @param id -  идентификатор заказа
     * @return Возвращает заказ с идентификатором id и модифицированным статусом в JSON формате.
     * Если цена товара была измена - возвращает информционное сообщение с обновленной стоимостью.
     * Для подтверждения оплаты потребуется дополнительный вызов данной функции.
     * Если баланс пользователя меньше суммы заказа или возникают другие проблемы, то возвращет сообщение об ошибке
     */
    @ApiOperation(value = "Apply payment of order by ID")
    @RequestMapping(method = RequestMethod.POST, value = "/api/orders/pay")
    @ResponseBody
    public String payOrder(@RequestParam("id") Long id){
        Order order = service.get(id);
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
            if(book.getAvaliableNumber()<prod.getNumber()){
                return "Error: the number of book (id="+prod.getBookId()
                            +") must be equal from 1 to "+book.getAvaliableNumber();
            }
            total_payment = total_payment.add(book.getPrice().multiply(BigInteger.valueOf(prod.getNumber())));
        }
        if(total_payment.compareTo(order.getTotalPayment())!=0){
            String msg = "Info: price has changed. The order (id="+order.getId()+") was updated. "+
                    "Old price="+order.getTotalPayment()+", new price="+total_payment+". Try again\n";
            order.setTotalPayment(total_payment);
            Order updated_order = service.update(order);
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
            book.setAvaliableNumber(book.getAvaliableNumber()-prod.getNumber());
            bookService.update(book);
        }
        user.setBalance(user.getBalance().subtract(total_payment));
        userService.update(user);
        order.setStatus("paid");
        Order updated_order = service.update(order);
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(new ObjectMapper().writeValueAsString(updated_order));
            return builder.toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    /**
     * <p>Отмена заказа. Оплаченый заказ не может быть отменен</p>
     * @param id -  идентификатор заказа
     * @return Если заказа с указаным id не существует или заказ был оплачен ранее (имеет статус "paid"),
     * то возвращает сообщение об ошибке. Иначе, производит удаление записи из таблица и возвращает сообщение "done"
     */
    @ApiOperation(value = "The abort of not-payment order by ID")
    @RequestMapping(method = RequestMethod.POST, value = "/api/orders/cancel")
    @ResponseBody
    public String cancelOrder(@RequestParam("id") Long id){
        Order order = service.get(id);
        if(order == null)
            return "Error: order with id="+id+" is not exist";
        if(order.getStatus().equals("paid")) {
            return "Error: permission denied. Order with id=" + id + " was paid";
        }
        if(!service.remove(order.getId())){
            return "Error: order with id="+id+" is not exist";
        }
        return "done";
    }
}
