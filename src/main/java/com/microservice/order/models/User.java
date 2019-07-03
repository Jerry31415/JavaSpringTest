package com.microservice.order.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@Entity
@Table(name = "users")
@ApiModel(description = "User.")
public class User implements Serializable {
    @ApiModelProperty(notes = "The database auto-generated user ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @ApiModelProperty(notes = "The balance of user")
    @Column(name="balance")
    private BigInteger balance;

    public User(){
        id = 0L;
        balance = BigInteger.valueOf(0);
    }

    public User(Long id, BigInteger balance){
        this.id = id;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }
    public BigInteger getBalance(){
        return balance;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setBalance(BigInteger balance) {
        this.balance = balance;
    }
}
