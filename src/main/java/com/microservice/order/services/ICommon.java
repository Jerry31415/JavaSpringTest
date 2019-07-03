package com.microservice.order.services;

import java.util.List;

public interface ICommon<T> {
    List<T> getAll();
    Long count();

}
