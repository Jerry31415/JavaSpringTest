package com.microservice.shop.models;

import java.io.Serializable;

public class Product implements Serializable {
    private Long book_id;
    private Long number;

    public Product(){
        book_id=0L;
        number=0L;
    }

    public Product(Long book_id, Long number){
        this.book_id = book_id;
        this.number = number;
    }

    public Long getBookId() {
        return book_id;
    }

    public void setBookId(Long book_id) {
        this.book_id = book_id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }
}