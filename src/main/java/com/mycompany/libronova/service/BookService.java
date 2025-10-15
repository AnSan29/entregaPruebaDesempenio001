package com.mycompany.libronova.service;

import com.mycompany.libronova.dao.BookDao;
import com.mycompany.libronova.dao.jdbc.BookDaoJdbc;
import com.mycompany.libronova.exception.IsbnDuplicadoException;
import com.mycompany.libronova.model.Book;
import java.util.List;

public class BookService {

    private final BookDao dao = new BookDaoJdbc();

    public Long crear(Book b) {
        dao.findByIsbn(b.getIsbn()).ifPresent(x -> {
            throw new IsbnDuplicadoException("El ISBN " + b.getIsbn() + " ya existe");
        });
        return dao.create(b);
    }

    public boolean actualizar(Book b) { return dao.update(b); }
    public boolean eliminar(Long id) { return dao.deleteById(id); }
    public List<Book> listar() { return dao.findAll(); }

}
