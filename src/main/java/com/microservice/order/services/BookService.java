package com.microservice.order.services;

import com.microservice.order.models.Book;
import com.microservice.order.repositories.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService extends CommonService<Book, BookRepository> implements IBookService {
}
