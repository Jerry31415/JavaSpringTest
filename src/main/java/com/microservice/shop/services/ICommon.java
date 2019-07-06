package com.microservice.shop.services;

import java.util.List;

public interface ICommon<T> {
    T create(T new_obj);
    List<T> getAll();
    Long count();
    boolean remove(Long id);
    T update(T obj);
    T get(Long id);
}
