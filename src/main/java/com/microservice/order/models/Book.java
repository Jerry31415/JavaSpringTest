package com.microservice.order.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@Entity
@Table(name = "books")
@ApiModel(description = "Book. The model includes description of book, price, available number and total sold number")
public class Book extends Model implements Serializable {
    @ApiModelProperty(notes = "The auto-generated ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @ApiModelProperty(notes = "The authors of book")
    @Column(name="authors")
    private String authors ;

    @ApiModelProperty(notes = "The title of book")
    @Column(name="title")
    private String title;

    @ApiModelProperty(notes = "The publish year of book ")
    @Column(name="year")
    private int year;

    @ApiModelProperty(notes = "The pages number of book")
    @Column(name="pages")
    private int pages;

    @ApiModelProperty(notes = "The annotation (short description) of book")
    @Column(name="annotation")
    private String annotation;

    @ApiModelProperty(notes = "The price of book (in cents)")
    @Column(name="price")
    private BigInteger price;

    @ApiModelProperty(notes = "The available number")
    @Column(name="avaliable_number")
    private Long avaliable_number;

    @ApiModelProperty(notes = "The total sold number")
    @Column(name="total_sold_number")
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

    public Long getAvaliableNumber() {
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

    public void setAvaliableNumber(Long avaliable_number) {
        this.avaliable_number = avaliable_number;
    }

    public Long getTotalSoldNumber() {
        return total_sold_number;
    }

    public void setTotalSoldNumber(Long total_sold_number) {
        this.total_sold_number = total_sold_number;
    }

    /**
     * <p>Производит проверку корректности заполнения полей</p>
     * @return Если поля заполнены некорректно, то возвращает текстовое сообщение с ошибкой, иначе пустую строку
     */
    public String checkFields(){
        if(title.isEmpty()) return "Error: title field is empty";
        if(authors.isEmpty()) return "Error: authors field is empty";
        if(annotation.isEmpty()) return "Error: annotation field is empty";
        if(pages<=0) return "Error: pages number <= 0";
        if(year<=0) return "Error: year <= 0";
        if(avaliable_number<0) return "Error: avaliable_number < 0";
        if(price.compareTo(BigInteger.valueOf(0))<0) return "Error: price < 0";
        return "";
    }
}

