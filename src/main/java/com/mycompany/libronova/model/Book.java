package com.mycompany.libronova.model;

public class Book {

    private Long id;
    private String isbn;
    private String titulo;
    private String autor;
    private String categoria;
    private int ejemplaresTotales;
    private int ejemplaresDisponibles;
    private double precioReferencia;
    private boolean activo;


    //constructor
    public Book(){}

    public Book(String isbn, String titulo, String autor, String categoria,
                int totales, int disponibles, double precio, boolean activo) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.ejemplaresTotales = totales;
        this.ejemplaresDisponibles = disponibles;
        this.precioReferencia = precio;
        this.activo = activo;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getEjemplaresTotales() { return ejemplaresTotales; }
    public void setEjemplaresTotales(int ejemplaresTotales) { this.ejemplaresTotales = ejemplaresTotales; }

    public int getEjemplaresDisponibles() { return ejemplaresDisponibles; }
    public void setEjemplaresDisponibles(int ejemplaresDisponibles) { this.ejemplaresDisponibles = ejemplaresDisponibles; }

    public double getPrecioReferencia() { return precioReferencia; }
    public void setPrecioReferencia(double precioReferencia) { this.precioReferencia = precioReferencia; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %s | %d/%d | $%.2f | %s",
                isbn, titulo, autor, categoria, ejemplaresDisponibles, ejemplaresTotales,
                precioReferencia, activo ? "ACTIVO" : "INACTIVO");
    }

}
