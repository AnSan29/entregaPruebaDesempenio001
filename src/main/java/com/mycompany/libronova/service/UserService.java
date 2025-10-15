package com.mycompany.libronova.service;

import java.util.Optional;

import com.mycompany.libronova.config.AppConfig;
import com.mycompany.libronova.dao.UserDao;
import com.mycompany.libronova.dao.jdbc.UserDaoJdbc;
import com.mycompany.libronova.exception.CredencialesInvalidasException;
import com.mycompany.libronova.model.User;
import com.mycompany.libronova.util.AppLogger;

public class UserService {

    private final UserDao dao = new UserDaoJdbc();

    // Decorador sobre create()
    public Long create(User u) {
        if (u.getRole() == null) u.setRole("ASISTENTE");
        if (u.getEstado() == null) u.setEstado("ACTIVO");
        Long id = dao.create(u);
        AppLogger.http("POST", "/users", "Usuario creado: " + u.getUsername());
        return id;
    }

    public Optional<User> login(String username, String password) {
        var userOpt = dao.findByUsername(username);
        if (userOpt.isEmpty()) throw new CredencialesInvalidasException("Usuario no encontrado");
        var u = userOpt.get();
        if (!u.getPasswordHash().equals(password))
            throw new CredencialesInvalidasException("Contraseña incorrecta");
        if (!"ACTIVO".equalsIgnoreCase(u.getEstado()))
            throw new CredencialesInvalidasException("Usuario inactivo");
        AppLogger.http("POST", "/login", "Sesión iniciada: " + u.getUsername());
        return userOpt;
    }

    public void listarUsuarios() {
        dao.findAll().forEach(u -> System.out.println("GET /users -> " + u));
    }

    /** Crea admin por defecto si no existe (valores en config.properties) */
    public void bootstrapAdminIfNeeded() {
        String username = AppConfig.get("admin.username", "admin");
        String password = AppConfig.get("admin.password", "1234");
        String role     = AppConfig.get("admin.role", "ADMIN");

        if (dao.findByUsername(username).isEmpty()) {
            User admin = new User(username, password, role, "ACTIVO");
            dao.create(admin);
            AppLogger.http("POST", "/users", "Bootstrap admin created: " + username);
        }
    }
}
