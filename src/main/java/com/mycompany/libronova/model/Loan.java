package com.mycompany.libronova.model;

import java.time.LocalDate;

public class Loan {
    private Long id;
    private Long memberId;
    private Long bookId;
    private LocalDate fechaPrestamo;
    private LocalDate fechaVencimiento;
    private LocalDate fechaDevolucion;
    private int multa;
    private String estado; // PRESTADO | DEVUELTO | VENCIDO

    // Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    public int getMulta() { return multa; }
    public void setMulta(int multa) { this.multa = multa; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return String.format("#%-4s | M:%-4s | B:%-4s | %s → %s | dev:%s | multa:%d | %s",
                id, memberId, bookId,
                fechaPrestamo, fechaVencimiento,
                fechaDevolucion == null ? "-" : fechaDevolucion.toString(),
                multa, estado);
    }
}
