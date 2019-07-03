package com.microservice.order.services;

import com.microservice.order.models.User;

import java.math.BigInteger;

public interface IUserService extends ICommon<User> {
    User create(BigInteger balance);
    boolean remove(Long id);
    User update(Long id, BigInteger balance);
    User get(Long id);
}
