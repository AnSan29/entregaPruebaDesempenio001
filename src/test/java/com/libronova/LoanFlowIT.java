package com.libronova;

import com.mycompany.libronova.config.DbManager;
import com.mycompany.libronova.model.Member;
import com.mycompany.libronova.model.Book;
import com.mycompany.libronova.service.MemberService;
import com.mycompany.libronova.service.BookService;
import com.mycompany.libronova.service.LoanService;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class LoanFlowIT {
    private static final MemberService memberSvc = new MemberService();
    private static final BookService bookSvc = new BookService();
    private static final LoanService loanSvc = new LoanService();

    private static Long memberId;
    private static String isbn = "TEST-ISBN-LOAN-001";
    private static Long loanIdCreado; // lo obtenemos consultando

    @BeforeAll
    static void prepararDatos() throws Exception {
        // Limpia por si existen previos
        try (Connection cx = DbManager.getConnection();
             PreparedStatement ps1 = cx.prepareStatement("DELETE FROM loan WHERE book_id IN (SELECT id FROM book WHERE isbn=?)");
             PreparedStatement ps2 = cx.prepareStatement("DELETE FROM book WHERE isbn=?");
             PreparedStatement ps3 = cx.prepareStatement("DELETE FROM member WHERE email=?")) {
            ps1.setString(1, isbn); ps1.executeUpdate();
            ps2.setString(1, isbn); ps2.executeUpdate();
            ps3.setString(1, "it_test@example.com"); ps3.executeUpdate();
        }

        // Crea member
        var m = new Member("IT Test", "it_test@example.com", "ACTIVO");
        memberId = memberSvc.crear(m);
        assertNotNull(memberId);

        // Crea book con stock
        var b = new Book(isbn, "Libro IT", "Autor IT", "TEST", 2, 2, 10000.0, true);
        var bookId = bookSvc.crear(b);
        assertNotNull(bookId);
    }

    @Test @Order(1)
    void prestar_ok_disminuyeStock() throws Exception {
        // Stock antes
        int stockAntes = stock();
        loanSvc.prestar(memberId, isbn);
        int stockDespues = stock();
        assertEquals(stockAntes - 1, stockDespues);

        // Guarda el último loanId creado
        loanIdCreado = ultimoLoanId();
        assertNotNull(loanIdCreado);
    }

    @Test @Order(2)
    void devolver_ok_aumentaStock_yCalculaMulta() throws Exception {
        // Simula atraso: movemos fecha_vencimiento a 3 días antes
        try (Connection cx = DbManager.getConnection();
             PreparedStatement ps = cx.prepareStatement("UPDATE loan SET fecha_vencimiento=? WHERE id=?")) {
            ps.setDate(1, Date.valueOf(LocalDate.now().minusDays(3)));
            ps.setLong(2, loanIdCreado);
            ps.executeUpdate();
        }

        int stockAntes = stock();
        int multa = loanSvc.devolver(loanIdCreado);
        int stockDespues = stock();

        assertTrue(multa >= 3 * 1500);  // según tu multaPorDia default
        assertEquals(stockAntes + 1, stockDespues);
    }

    @AfterAll
    static void limpiar() throws Exception {
        try (Connection cx = DbManager.getConnection();
             PreparedStatement ps1 = cx.prepareStatement("DELETE FROM loan WHERE book_id IN (SELECT id FROM book WHERE isbn=?)");
             PreparedStatement ps2 = cx.prepareStatement("DELETE FROM book WHERE isbn=?");
             PreparedStatement ps3 = cx.prepareStatement("DELETE FROM member WHERE email=?")) {
            ps1.setString(1, isbn); ps1.executeUpdate();
            ps2.setString(1, isbn); ps2.executeUpdate();
            ps3.setString(1, "it_test@example.com"); ps3.executeUpdate();
        }
    }

    // Helpers
    private static int stock() throws Exception {
        try (Connection cx = DbManager.getConnection();
             PreparedStatement ps = cx.prepareStatement("SELECT ejemplares_disponibles FROM book WHERE isbn=?")) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new IllegalStateException("Libro de prueba no encontrado");
    }

    private static Long ultimoLoanId() throws Exception {
        try (Connection cx = DbManager.getConnection();
             PreparedStatement ps = cx.prepareStatement("SELECT id FROM loan ORDER BY id DESC LIMIT 1");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return null;
    }
}
