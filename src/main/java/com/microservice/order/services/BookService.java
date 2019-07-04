package com.microservice.order.services;

import com.microservice.order.models.Book;
import com.microservice.order.repositories.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class BookService implements IBookService {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> getAll() {
        List<Book> books = null;
        if(bookRepository.count()>0){
            books = (List<Book>) bookRepository.findAll();
        }
        return books;
    }

    @Override
    public Long count() {
        return bookRepository.count();
    }

    @Override
    public synchronized Book create(Book new_book){
        return bookRepository.save(new_book);
    }

    @Override
    public synchronized boolean remove(Long id){
        if(bookRepository.existsById(id)){
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public synchronized Book update(Book new_book){
        if(bookRepository.existsById(new_book.getId())){
            return bookRepository.save(new_book);
        }
        return null;
    }

    @Override
    public Book get(Long id){
        return (bookRepository.existsById(id))?bookRepository.findById(id).get():null;
    }

}
