package com.mycompany.libronova.dao.jdbc;

import com.mycompany.libronova.config.DbManager;
import com.mycompany.libronova.dao.MemberDao;
import com.mycompany.libronova.model.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberDaoJdbc implements MemberDao {

    private Member map(ResultSet rs) throws SQLException {
        Member m = new Member();
        m.setId(rs.getLong("id"));
        m.setFullName(rs.getString("full_name"));
        m.setEmail(rs.getString("email"));
        m.setEstado(rs.getString("estado"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) m.setCreatedAt(ts.toLocalDateTime());
        return m;
    }


    @Override
    public Long create(Member m) {
        String sql = "INSERT INTO member (full_name,email,estado,created_at) VALUES (?,?,?,NOW())";
        try (Connection c = DbManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getFullName());
            if (m.getEmail() == null || m.getEmail().isBlank()) ps.setNull(2, Types.VARCHAR);
            else ps.setString(2, m.getEmail());
            ps.setString(3, m.getEstado());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear socio", e);
        }
        return null;
    }

    @Override
    public boolean update(Member m) {
        String sql = "UPDATE member SET full_name=?, email=?, estado=? WHERE id=?";
        try (Connection c = DbManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getFullName());
            if (m.getEmail() == null || m.getEmail().isBlank()) ps.setNull(2, Types.VARCHAR);
            else ps.setString(2, m.getEmail());
            ps.setString(3, m.getEstado());
            ps.setLong(4, m.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar socio", e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (Connection c = DbManager.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM member WHERE id=?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar socio", e);
        }
    }

    @Override
    public Optional<Member> findById(Long id) {
        try (Connection c = DbManager.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM member WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar socio", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        try (Connection c = DbManager.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM member WHERE email=?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar por email", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Member> findAll() {
        List<Member> list = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM member ORDER BY full_name")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error listando socios", e);
        }
        return list;
    }

    @Override
    public List<Member> findByNameLike(String name) {
        List<Member> list = new ArrayList<>();
        try (Connection c = DbManager.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM member WHERE full_name LIKE ? ORDER BY full_name")) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error filtrando socios", e);
        }
        return list;
    }


}
