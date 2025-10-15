package com.mycompany.libronova.dao.jdbc;

import com.mycompany.libronova.config.DbManager;
import com.mycompany.libronova.dao.BookDao;
import com.mycompany.libronova.model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoJdbc implements BookDao{


    private Book map(ResultSet rs) throws SQLException {
        Book b = new Book();
        b.setId(rs.getLong("id"));
        b.setIsbn(rs.getString("isbn"));
        b.setTitulo(rs.getString("titulo"));
        b.setAutor(rs.getString("autor"));
        b.setCategoria(rs.getString("categoria"));
        b.setEjemplaresTotales(rs.getInt("ejemplares_totales"));
        b.setEjemplaresDisponibles(rs.getInt("ejemplares_disponibles"));
        b.setPrecioReferencia(rs.getDouble("precio_referencia"));
        b.setActivo(rs.getBoolean("activo"));
        return b;
    }

    @Override
    public Long create(Book b) {
        String sql = "INSERT INTO book (isbn,titulo,autor,categoria,ejemplares_totales,ejemplares_disponibles,precio_referencia,activo) "
                   + "VALUES (?,?,?,?,?,?,?,?)";
        try (Connection c = DbManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, b.getIsbn());
            ps.setString(2, b.getTitulo());
            ps.setString(3, b.getAutor());
            ps.setString(4, b.getCategoria());
            ps.setInt(5, b.getEjemplaresTotales());
            ps.setInt(6, b.getEjemplaresDisponibles());
            ps.setDouble(7, b.getPrecioReferencia());
            ps.setBoolean(8, b.isActivo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear libro", e);
        }
        return null;
    }

    @Override
    public boolean update(Book b) {
        String sql = "UPDATE book SET titulo=?,autor=?,categoria=?,ejemplares_totales=?,ejemplares_disponibles=?,precio_referencia=?,activo=? WHERE isbn=?";
        try (Connection c = DbManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, b.getTitulo());
            ps.setString(2, b.getAutor());
            ps.setString(3, b.getCategoria());
            ps.setInt(4, b.getEjemplaresTotales());
            ps.setInt(5, b.getEjemplaresDisponibles());
            ps.setDouble(6, b.getPrecioReferencia());
            ps.setBoolean(7, b.isActivo());
            ps.setString(8, b.getIsbn());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar libro", e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (Connection c = DbManager.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM book WHERE id=?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar libro", e);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        try (Connection c = DbManager.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM book WHERE id=?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        try (Connection c = DbManager.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM book WHERE isbn=?")) {
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        List<Book> list = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM book ORDER BY titulo")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error listando libros", e);
        }
        return list;
    }

    @Override
    public List<Book> findByAutor(String autor) {
        List<Book> list = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM book WHERE autor LIKE ?")) {
            ps.setString(1, "%" + autor + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<Book> findByCategoria(String categoria) {
        List<Book> list = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM book WHERE categoria LIKE ?")) {
            ps.setString(1, "%" + categoria + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
