package com.mycompany.libronova.service;

import com.mycompany.libronova.dao.MemberDao;
import com.mycompany.libronova.dao.jdbc.MemberDaoJdbc;
import com.mycompany.libronova.model.Member;

import java.util.List;

public class MemberService {

    private final MemberDao dao = new MemberDaoJdbc();

    public Long crear(Member m) {
        if (m.getFullName() == null || m.getFullName().isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio");
        if (m.getEmail() != null && !m.getEmail().isBlank()) {
            dao.findByEmail(m.getEmail()).ifPresent(x -> {
                throw new IllegalArgumentException("El email ya está registrado");
            });
        }
        if (m.getEstado() == null || m.getEstado().isBlank()) m.setEstado("ACTIVO");
        return dao.create(m);
    }

    public boolean actualizar(Member m) {
        if (m.getId() == null) throw new IllegalArgumentException("ID requerido");
        return dao.update(m);
    }

    public boolean eliminar(Long id) { return dao.deleteById(id); }
    public List<Member> listar() { return dao.findAll(); }
    public List<Member> buscarPorNombre(String name) { return dao.findByNameLike(name); }

}
