package com.mycompany.libronova.service;

import com.mycompany.libronova.config.DbManager;
import com.mycompany.libronova.dao.jdbc.BookDaoJdbc;
import com.mycompany.libronova.dao.jdbc.LoanDaoJdbc;
import com.mycompany.libronova.dao.jdbc.MemberDaoJdbc;
import com.mycompany.libronova.exception.StockInsuficienteException;
import com.mycompany.libronova.model.Book;
import com.mycompany.libronova.model.Loan;
import com.mycompany.libronova.model.Member;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class LoanService {

    private final LoanDaoJdbc loanDao = new LoanDaoJdbc();
    private final MemberDaoJdbc memberDao = new MemberDaoJdbc();
    private final BookDaoJdbc bookDao = new BookDaoJdbc();

    public int multaPorDia() { return Integer.parseInt(System.getProperty("multaPorDia","0")); }

    public int calcularMulta(LocalDate vencimiento, LocalDate devolucion, int multaPorDia) {
        long dias = ChronoUnit.DAYS.between(vencimiento, devolucion);
        return (int) Math.max(0, dias) * Math.max(0, multaPorDia);
    }

    public void prestar(Long memberId, String isbn) {
        Member m = memberDao.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Socio no existe"));
        if (!"ACTIVO".equalsIgnoreCase(m.getEstado()))
            throw new IllegalArgumentException("Socio inactivo");

        Book b = bookDao.findByIsbn(isbn).orElseThrow(() -> new IllegalArgumentException("Libro no existe"));
        if (b.getEjemplaresDisponibles() <= 0)
            throw new StockInsuficienteException("No hay ejemplares disponibles");

        LocalDate hoy = LocalDate.now();
        int diasPrestamo = Integer.parseInt(System.getProperty("diasPrestamo","7"));
        LocalDate venc = hoy.plusDays(diasPrestamo);

        String sqlInsertLoan = "INSERT INTO loan(member_id,book_id,fecha_prestamo,fecha_vencimiento,estado,multa) VALUES(?,?,?,?, 'PRESTADO', 0)";
        String sqlUpdBookDec = "UPDATE book SET ejemplares_disponibles = ejemplares_disponibles - 1 WHERE id=?";

        try (Connection cx = DbManager.getConnection()) {
            cx.setAutoCommit(false);
            try (PreparedStatement ps1 = cx.prepareStatement(sqlInsertLoan);
                 PreparedStatement ps2 = cx.prepareStatement(sqlUpdBookDec)) {

                ps1.setLong(1, m.getId());
                ps1.setLong(2, b.getId());
                ps1.setDate(3, Date.valueOf(hoy));
                ps1.setDate(4, Date.valueOf(venc));
                ps1.executeUpdate();

                ps2.setLong(1, b.getId());
                ps2.executeUpdate();

                cx.commit();
            } catch (SQLException e) {
                cx.rollback();
                throw new RuntimeException("Error en transacción de préstamo", e);
            } finally {
                cx.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de conexión", e);
        }
    }

    public int devolver(Long loanId) {
        var loan = loanDao.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Préstamo no existe"));
        if (!"PRESTADO".equalsIgnoreCase(loan.getEstado()))
            throw new IllegalArgumentException("Préstamo no está activo");

        LocalDate hoy = LocalDate.now();
        int multaDia = Integer.getInteger("multaPorDia", 1500); // fallback 1500 si no viene por -Dprop
        int multa = calcularMulta(loan.getFechaVencimiento(), hoy, multaDia);

        String sqlUpdLoan = "UPDATE loan SET fecha_devolucion=?, multa=?, estado='DEVUELTO' WHERE id=?";
        String sqlUpdBookInc = "UPDATE book SET ejemplares_disponibles = ejemplares_disponibles + 1 WHERE id=?";

        try (Connection cx = DbManager.getConnection()) {
            cx.setAutoCommit(false);
            try (PreparedStatement ps1 = cx.prepareStatement(sqlUpdLoan);
                 PreparedStatement ps2 = cx.prepareStatement(sqlUpdBookInc)) {
                ps1.setDate(1, Date.valueOf(hoy));
                ps1.setInt(2, multa);
                ps1.setLong(3, loan.getId());
                ps1.executeUpdate();

                ps2.setLong(1, loan.getBookId());
                ps2.executeUpdate();

                cx.commit();
                return multa;
            } catch (SQLException e) {
                cx.rollback();
                throw new RuntimeException("Error en transacción de devolución", e);
            } finally {
                cx.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de conexión", e);
        }
    }

    public List<Loan> listarTodos()     { return loanDao.findAll(); }
    public List<Loan> listarActivos()   { return loanDao.findActivos(); }
    public List<Loan> listarVencidos()  { return loanDao.findVencidos(); }

}
