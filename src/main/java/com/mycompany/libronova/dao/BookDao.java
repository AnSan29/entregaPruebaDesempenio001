package com.mycompany.libronova.dao;


import com.mycompany.libronova.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookDao {

    Long create(Book b);
    boolean update(Book b);
    boolean deleteById(Long id);
    Optional<Book> findById(Long id);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findAll();
    List<Book> findByAutor(String autor);
    List<Book> findByCategoria(String categoria);

}
