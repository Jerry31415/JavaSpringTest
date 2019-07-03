package com.microservice.order.models;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;


// create table orders (id bigserial, user_id serial, books integer[][], total_payment bigint, order_date timestamp, status text);
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="user_id")
    private Long user_id;
    @Column(name="books")
    private ArrayList<Product> books;
   // @Column(name="book_id")
   // private Long book_id;
  //  @Column(name="number")
   // private int number;
    @Column(name="total_payment")
    private BigInteger total_payment;
    @Column(name="order_date")
    private LocalDateTime order_date;
    @Column(name="status")
    private String status;

    public Order(){
        id = 0L;
        user_id = 0L;
        books = new ArrayList<Product>();
        //book_id = 0L;
        //number = 0;
        total_payment = BigInteger.valueOf(0);
        order_date = LocalDateTime.now();
        status = "";
    }

    public Order(Long id, Long user_id, Long book_id, int number, ArrayList<Product> books,
                 BigInteger total_payment, LocalDateTime order_date, String status){
        this.id = id;
        this.user_id = user_id;
        this.books = books;
     //   this.book_id = book_id;
     //   this.number = number;
        this.total_payment = total_payment;
        this.order_date = order_date;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId(){
        return user_id;
    }

    public BigInteger getTotalPayment(){
        return total_payment;
    }

    public LocalDateTime getOrderDate() {
        return order_date;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long user_id) {
        this.user_id = user_id;
    }

    public void addBook(Long book_id, Long number) {
        books.add(new Product(book_id, number));
    }

    public void setNumber(Long book_id, Long number) {
        for(int i=0;i<books.size();++i){
            if(books.get(i).getBookId()==book_id){
                books.get(i).setNumber(number);
            }
        }
    }

    public void setTotalPayment(BigInteger total_payment) {
        this.total_payment = total_payment;
    }

    public void setOrderDate(LocalDateTime order_date) {
        this.order_date = order_date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Product> getBooks(){
        return books;
    }
}
// pending/paid
// id bigserial, user_id serial, book_id serial, numer integer, total_payment money, order_date timestamp, status varchar(7)
