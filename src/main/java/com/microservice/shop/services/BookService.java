package com.microservice.shop.services;

import com.microservice.shop.models.Book;
import com.microservice.shop.repositories.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService extends CommonService<Book, BookRepository> implements IBookService {
}
