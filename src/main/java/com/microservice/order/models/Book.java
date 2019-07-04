package com.microservice.order.models;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;
    @Column(name="authors")
    private String authors ;
    @Column(name="title")
    private String title;
    @Column(name="year")
    private int year;
    @Column(name="pages")
    private int pages;
    @Column(name="annotation")
    private String annotation;
    @Column(name="price")
    private BigInteger price;
    @Column(name="avaliable_number")
    private Long avaliable_number;
    @Column(name="total_sold_number ")
    private Long total_sold_number ;


    public Book(){
        id = 0L;
        authors = "";
        title = "";
        year = 0;
        pages = 0;
        annotation = "";
        price = BigInteger.valueOf(0);
        avaliable_number = 0L;
        total_sold_number = 0L;
    }

    public Book(Long id, String authors, String title, int year, int pages, String annotation,
                BigInteger price, Long number, Long total_sold_number){
        this.id = id;
        this.authors = authors;
        this.title = title;
        this.year = year;
        this.pages = pages;
        this.annotation = annotation;
        this.price = price;
        this.avaliable_number = number;
        this.total_sold_number = total_sold_number;
    }

    public Long getId() {
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getAuthors(){
        return authors;
    }

    public int getYear(){
        return year;
    }

    public int getPages(){
        return pages;
    }

    public String getAnnotation(){
        return annotation;
    }

    public BigInteger getPrice() {
        return price;
    }

    public Long getAvaliable_number() {
        return avaliable_number;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }

    public void setAvaliable_number(Long avaliable_number) {
        this.avaliable_number = avaliable_number;
    }

    public Long getTotalSoldNumber() {
        return total_sold_number;
    }

    public void setTotalSoldNumber(Long total_sold_number) {
        this.total_sold_number = total_sold_number;
    }

    //check of fields values
    public String checkFields(){
        if(title.isEmpty()){
            return "Error: title field is empty";
        }
        if(authors.isEmpty()){
            return "Error: authors field is empty";
        }
        if(annotation.isEmpty()){
            return "Error: annotation field is empty";
        }
        if(pages<=0){
            return "Error: pages number <= 0";
        }
        if(year<=0){
            return "Error: year <= 0";
        }
        if(avaliable_number<0){
            return "Error: avaliable_number < 0";
        }
        if(price.compareTo(BigInteger.valueOf(0))<0){
            return "Error: price < 0";
        }
        return "";
    }

}

