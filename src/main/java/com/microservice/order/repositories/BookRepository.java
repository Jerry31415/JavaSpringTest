package com.microservice.order.repositories;

import com.microservice.order.models.Book;
import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
/*
    @Query("select * from books where title = :name")
    List<Book> getAll(@Param("name") String name);
*/
}

