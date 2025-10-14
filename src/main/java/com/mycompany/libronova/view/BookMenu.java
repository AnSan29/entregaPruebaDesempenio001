package com.mycompany.libronova.view;

import com.mycompany.libronova.model.Book;
import com.mycompany.libronova.service.BookService;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.stream.Collectors;

public class BookMenu {

    private final BookService service = new BookService();
    
    public void mostrar() {
        String[] opciones = {"Listar", "Crear", "Editar", "Eliminar", "Salir"};
        int op;
        do {
            op = JOptionPane.showOptionDialog(null, "Gestión de Libros", "LibroNova",
                    0, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
            switch (op) {
                case 0 -> listar();
                case 1 -> crear();
                case 2 -> editar();
                case 3 -> eliminar();
            }
        } while (op != 4 && op != JOptionPane.CLOSED_OPTION);
    }

    private void listar() {
        List<Book> list = service.listar();
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay libros registrados.");
            return;
        }
        String tabla = list.stream()
                .map(Book::toString)
                .collect(Collectors.joining("\n"));
        JOptionPane.showMessageDialog(null, tabla, "Catálogo de Libros", JOptionPane.INFORMATION_MESSAGE);
    }


    private void crear() {
        try {
            String isbn = JOptionPane.showInputDialog("ISBN:");
            String titulo = JOptionPane.showInputDialog("Título:");
            String autor = JOptionPane.showInputDialog("Autor:");
            String cat = JOptionPane.showInputDialog("Categoría:");
            int tot = Integer.parseInt(JOptionPane.showInputDialog("Ejemplares totales:"));
            int disp = tot;
            double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio referencia:"));
            service.crear(new Book(isbn, titulo, autor, cat, tot, disp, precio, true));
            JOptionPane.showMessageDialog(null, "Libro creado correctamente");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private void editar() {
        String isbn = JOptionPane.showInputDialog("ISBN del libro a editar:");
        Book b = service.listar().stream()
                .filter(x -> x.getIsbn().equals(isbn))
                .findFirst().orElse(null);
        if (b == null) {
            JOptionPane.showMessageDialog(null, "No encontrado.");
            return;
        }
        String nuevoTitulo = JOptionPane.showInputDialog("Nuevo título:", b.getTitulo());
        b.setTitulo(nuevoTitulo);
        service.actualizar(b);
        JOptionPane.showMessageDialog(null, "Actualizado correctamente");
    }


    private void eliminar() {
        try {
            Long id = Long.parseLong(JOptionPane.showInputDialog("ID del libro a eliminar:"));
            service.eliminar(id);
            JOptionPane.showMessageDialog(null, "Eliminado correctamente");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

}
