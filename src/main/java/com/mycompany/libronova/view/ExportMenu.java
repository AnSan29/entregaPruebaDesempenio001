package com.mycompany.libronova.view;

import com.mycompany.libronova.service.BookService;
import com.mycompany.libronova.service.LoanService;
import com.mycompany.libronova.util.CSVExporter;

import javax.swing.*;
import java.nio.file.Path;

public class ExportMenu {
    private final BookService bookService = new BookService();
    private final LoanService loanService = new LoanService();

    public void mostrar() {
        String[] ops = {"Exportar libros CSV", "Exportar vencidos CSV", "Salir"};
        int op;
        do {
            op = JOptionPane.showOptionDialog(null, "Exportaciones", "LibroNova",
                    0, JOptionPane.PLAIN_MESSAGE, null, ops, ops[0]);
            try {
                if (op == 0) {
                    Path p = CSVExporter.exportBooks(bookService.listar());
                    JOptionPane.showMessageDialog(null, "Exportado: " + p.toAbsolutePath());
                } else if (op == 1) {
                    Path p = CSVExporter.exportVencidos(loanService.listarVencidos());
                    JOptionPane.showMessageDialog(null, "Exportado: " + p.toAbsolutePath());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } while (op != 2 && op != JOptionPane.CLOSED_OPTION);
    }
}
