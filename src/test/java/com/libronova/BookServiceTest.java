package com.libronova;

import com.mycompany.libronova.config.DbManager;
import com.mycompany.libronova.exception.IsbnDuplicadoException;
import com.mycompany.libronova.model.Book;
import com.mycompany.libronova.service.BookService;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BookServiceTest {

    private final BookService svc = new BookService();

    // ISBN corto: máx 20 chars (incluye "T-")
    private String shortIsbn() {
        return "T-" + UUID.randomUUID().toString().replace("-", "").substring(0, 18);
    }

    private void deleteByIsbn(String isbn) {
        try (Connection cx = DbManager.getConnection();
             PreparedStatement ps = cx.prepareStatement("DELETE FROM book WHERE isbn=?")) {
            ps.setString(1, isbn);
            ps.executeUpdate();
        } catch (Exception ignored) {}
    }

    @Test
    void crearLibro_ok() {
        String isbn = shortIsbn();          // <-- AHORA CORTO
        deleteByIsbn(isbn);

        var id = svc.crear(new Book(isbn, "Libro Prueba", "Autor X", "TEST",
                3, 3, 50000.0, true));
        assertNotNull(id);

        deleteByIsbn(isbn);
    }

    @Test
    void crearLibro_isbnDuplicado_lanzaExcepcion() {
        String isbn = shortIsbn();          // <-- AHORA CORTO
        deleteByIsbn(isbn);

        // 1ra vez: OK
        svc.crear(new Book(isbn, "Uno", "Autor", "TEST", 1, 1, 10000.0, true));

        // 2da vez: debe lanzar IsbnDuplicadoException
        assertThrows(IsbnDuplicadoException.class, () -> {
            svc.crear(new Book(isbn, "Dos", "Autor", "TEST", 1, 1, 10000.0, true));
        });

        deleteByIsbn(isbn);
    }
}
