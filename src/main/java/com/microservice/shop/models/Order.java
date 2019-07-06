package com.microservice.shop.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Table(name = "orders")
@ApiModel(description = "Order. The model includes order information such as: order id, " +
        "user id, list of products (books), status and etc")
public class Order extends Model implements Serializable {
    @ApiModelProperty(notes = "The auto-generated ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @ApiModelProperty(notes = "The user ID")
    @Column(name="user_id")
    private Long user_id;

    @ApiModelProperty(notes = "The list of pairs (book_id, number)")
    @Column(name="books")
    private ArrayList<Product> books;

    @ApiModelProperty(notes = "The total payment value (in cents)")
    @Column(name="total_payment")
    private BigInteger total_payment;

    @ApiModelProperty(notes = "The timestamp of order (LocalDateTime java-type)")
    @Column(name="order_date")
    private LocalDateTime order_date;

    @ApiModelProperty(notes = "The status: pending / paid")
    @Column(name="status")
    private String status;

    public Order(){
        id = 0L;
        user_id = 0L;
        books = new ArrayList<Product>();
        total_payment = BigInteger.valueOf(0);
        order_date = LocalDateTime.now();
        status = "";
    }

    public Order(Long id, Long user_id, Long book_id, int number, ArrayList<Product> books,
                 BigInteger total_payment, LocalDateTime order_date, String status){
        this.id = id;
        this.user_id = user_id;
        this.books = books;
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

    public void clearBooksList(){
        books.clear();
    }

}