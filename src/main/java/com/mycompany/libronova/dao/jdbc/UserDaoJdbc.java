package com.mycompany.libronova.dao.jdbc;

import com.mycompany.libronova.config.DbManager;
import com.mycompany.libronova.dao.UserDao;
import com.mycompany.libronova.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoJdbc implements UserDao {

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setUsername(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setRole(rs.getString("role"));
        u.setEstado(rs.getString("estado"));
        u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return u;
    }


    @Override
    public Long create(User u) {
        String sql = "INSERT INTO user (username,password_hash,role,estado,created_at) VALUES (?,?,?,?,NOW())";
        try (Connection c = DbManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getRole());
            ps.setString(4, u.getEstado());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear usuario", e);
        }
        return null;
    }

    @Override
    public boolean update(User u) {
        String sql = "UPDATE user SET password_hash=?,role=?,estado=? WHERE username=?";
        try (Connection c = DbManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getPasswordHash());
            ps.setString(2, u.getRole());
            ps.setString(3, u.getEstado());
            ps.setString(4, u.getUsername());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario", e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (Connection c = DbManager.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM user WHERE id=?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario", e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (Connection c = DbManager.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM user WHERE username=?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario", e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM user ORDER BY username")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error listando usuarios", e);
        }
        return list;
    }

}
