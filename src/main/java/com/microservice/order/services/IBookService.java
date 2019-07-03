package com.microservice.order.services;

import com.microservice.order.models.Book;

public interface IBookService extends ICommon<Book> {
    Book create(Book new_book);
    boolean remove(Long id);
    Book update(Book new_book);
    Book get(Long id);
}

