package com.mycompany.libronova.model;

import java.time.LocalDateTime;

public class Member {
     private Long id;
    private String fullName;
    private String email;      // puede ser null, pero si existe debe ser único
    private String estado;     // ACTIVO | INACTIVO
    private LocalDateTime createdAt;

    public Member() { this.estado = "ACTIVO"; }

    public Member(String fullName, String email, String estado) {
        this.fullName = fullName;
        this.email = email;
        this.estado = (estado == null ? "ACTIVO" : estado);
    }

    // Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }
    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return String.format("%-4s | %-24s | %-25s | %-9s",
                (id == null ? "-" : id), cut(fullName, 24), cut(email, 25), estado);
    }
    private static String cut(String s, int n) { return s == null ? "" : (s.length()<=n ? s : s.substring(0,n-1)+"…"); }
}
