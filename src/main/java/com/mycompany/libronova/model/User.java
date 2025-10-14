package com.mycompany.libronova.model;

import java.time.LocalDateTime;

public class User {

    private Long id;
    private String username;
    private String passwordHash;
    private String role;    // "ADMIN" o "ASISTENTE"
    private String estado;  // "ACTIVO" o "INACTIVO"
    private LocalDateTime createdAt;

    public User() {}

    public User(String username, String passwordHash, String role, String estado) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.estado = estado;
        this.createdAt = LocalDateTime.now();
    }

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return String.format("%-15s | %-10s | %-10s", username, role, estado);
    }

}
