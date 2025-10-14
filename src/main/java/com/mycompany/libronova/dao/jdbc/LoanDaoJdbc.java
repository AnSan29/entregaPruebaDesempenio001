package com.mycompany.libronova.dao.jdbc;

import com.mycompany.libronova.config.DbManager;
import com.mycompany.libronova.dao.LoanDao;
import com.mycompany.libronova.model.Loan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanDaoJdbc implements LoanDao {
    private Loan map(ResultSet rs) throws SQLException {
        Loan l = new Loan();
        l.setId(rs.getLong("id"));
        l.setMemberId(rs.getLong("member_id"));
        l.setBookId(rs.getLong("book_id"));
        l.setFechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate());
        l.setFechaVencimiento(rs.getDate("fecha_vencimiento").toLocalDate());
        Date d = rs.getDate("fecha_devolucion");
        l.setFechaDevolucion(d == null ? null : d.toLocalDate());
        l.setMulta(rs.getInt("multa"));
        l.setEstado(rs.getString("estado"));
        return l;
    }

    @Override
    public Optional<Loan> findById(Long id) {
        String sql = "SELECT * FROM loan WHERE id=?";
        try (Connection c = DbManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error consultando préstamo", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Loan> findAll() {
        List<Loan> out = new ArrayList<>();
        String sql = "SELECT * FROM loan ORDER BY id DESC";
        try (Connection c = DbManager.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) out.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error listando préstamos", e);
        }
        return out;
    }

    @Override
    public List<Loan> findActivos() {
        List<Loan> out = new ArrayList<>();
        String sql = "SELECT * FROM loan WHERE estado='PRESTADO' ORDER BY fecha_vencimiento ASC";
        try (Connection c = DbManager.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) out.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error listando activos", e);
        }
        return out;
    }

    @Override
    public List<Loan> findVencidos() {
        List<Loan> out = new ArrayList<>();
        String sql = "SELECT * FROM loan WHERE estado='PRESTADO' AND fecha_vencimiento < CURRENT_DATE() ORDER BY fecha_vencimiento ASC";
        try (Connection c = DbManager.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) out.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error listando vencidos", e);
        }
        return out;
    }

    @Override
    public List<Loan> findByMember(Long memberId) {
        List<Loan> out = new ArrayList<>();
        String sql = "SELECT * FROM loan WHERE member_id=? ORDER BY id DESC";
        try (Connection c = DbManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, memberId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) out.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error por socio", e);
        }
        return out;
    }
}
