package com.mycompany.libronova.view;

import com.mycompany.libronova.model.Loan;
import com.mycompany.libronova.service.LoanService;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

public class LoanMenu {

    private final LoanService service = new LoanService();

    public void mostrar() {
        String[] ops = {"Prestar", "Devolver", "Activos", "Vencidos", "Todos", "Salir"};
        int op;
        do {
            op = JOptionPane.showOptionDialog(null, "Gestión de Préstamos", "LibroNova",
                    0, JOptionPane.PLAIN_MESSAGE, null, ops, ops[0]);
            switch (op) {
                case 0 -> prestar();
                case 1 -> devolver();
                case 2 -> mostrar(service.listarActivos(), "Préstamos activos");
                case 3 -> mostrar(service.listarVencidos(), "Préstamos vencidos");
                case 4 -> mostrar(service.listarTodos(), "Todos los préstamos");
            }
        } while (op != 5 && op != JOptionPane.CLOSED_OPTION);
    }

    private void prestar() {
        try {
            Long memberId = Long.valueOf(JOptionPane.showInputDialog("ID del socio:"));
            String isbn = JOptionPane.showInputDialog("ISBN del libro:");
            service.prestar(memberId, isbn);
            JOptionPane.showMessageDialog(null, "Préstamo registrado.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void devolver() {
        try {
            Long loanId = Long.valueOf(JOptionPane.showInputDialog("ID del préstamo:"));
            int multa = service.devolver(loanId);
            JOptionPane.showMessageDialog(null, "Devolución registrada. Multa: " + multa);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar(List<Loan> list, String titulo) {
        if (list.isEmpty()) { JOptionPane.showMessageDialog(null, "Sin registros."); return; }
        String tabla = list.stream().map(Loan::toString).collect(Collectors.joining("\n"));
        JOptionPane.showMessageDialog(null, tabla, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

}
