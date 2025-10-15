package com.mycompany.libronova.view;

import com.mycompany.libronova.model.Member;
import com.mycompany.libronova.service.MemberService;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

public class MemberMenu {

    private final MemberService service = new MemberService();

    public void mostrar() {
        String[] ops = {"Listar", "Buscar por nombre", "Crear", "Editar", "Activar/Inactivar", "Eliminar", "Salir"};
        int op;
        do {
            op = JOptionPane.showOptionDialog(null, "Gestión de Socios", "LibroNova",
                    0, JOptionPane.PLAIN_MESSAGE, null, ops, ops[0]);
            switch (op) {
                case 0 -> listar();
                case 1 -> buscar();
                case 2 -> crear();
                case 3 -> editar();
                case 4 -> toggleEstado();
                case 5 -> eliminar();
            }
        } while (op != 6 && op != JOptionPane.CLOSED_OPTION);
    }

    private void listar() {
        List<Member> list = service.listar();
        if (list.isEmpty()) { JOptionPane.showMessageDialog(null, "Sin socios registrados."); return; }
        String tabla = header() + toRows(list);
        JOptionPane.showMessageDialog(null, tabla, "Socios", JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscar() {
        String q = JOptionPane.showInputDialog("Nombre contiene:");
        if (q == null) return;
        List<Member> list = service.buscarPorNombre(q);
        String tabla = header() + toRows(list);
        JOptionPane.showMessageDialog(null, tabla, "Resultado", JOptionPane.INFORMATION_MESSAGE);
    }

    private void crear() {
        try {
            String nombre = JOptionPane.showInputDialog("Nombre completo:");
            if (nombre == null) return;
            String email = JOptionPane.showInputDialog("Email (opcional):");
            Member m = new Member(nombre, (email == null || email.isBlank() ? null : email), "ACTIVO");
            service.crear(m);
            JOptionPane.showMessageDialog(null, "Socio creado.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editar() {
        try {
            Long id = Long.valueOf(JOptionPane.showInputDialog("ID del socio:"));
            List<Member> all = service.listar();
            Member m = all.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
            if (m == null) { JOptionPane.showMessageDialog(null, "No encontrado."); return; }
            String nombre = JOptionPane.showInputDialog("Nuevo nombre:", m.getFullName());
            String email = JOptionPane.showInputDialog("Nuevo email (vacío = eliminar):", m.getEmail());
            m.setFullName(nombre);
            m.setEmail(email == null || email.isBlank() ? null : email);
            service.actualizar(m);
            JOptionPane.showMessageDialog(null, "Actualizado.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void toggleEstado() {
        try {
            Long id = Long.valueOf(JOptionPane.showInputDialog("ID del socio:"));
            Member m = service.listar().stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
            if (m == null) { JOptionPane.showMessageDialog(null, "No encontrado."); return; }
            String nuevo = "ACTIVO".equalsIgnoreCase(m.getEstado()) ? "INACTIVO" : "ACTIVO";
            m.setEstado(nuevo);
            service.actualizar(m);
            JOptionPane.showMessageDialog(null, "Estado actualizado a " + nuevo + ".");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        try {
            Long id = Long.valueOf(JOptionPane.showInputDialog("ID del socio a eliminar:"));
            int c = JOptionPane.showConfirmDialog(null, "¿Eliminar definitivamente?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (c != JOptionPane.YES_OPTION) return;
            service.eliminar(id);
            JOptionPane.showMessageDialog(null, "Eliminado.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String header() {
        return String.format("%-4s | %-24s | %-25s | %-9s%n", "ID", "NOMBRE", "EMAIL", "ESTADO")
             + "---------------------------------------------------------------\n";
    }
    
    private String toRows(List<Member> list) {
        return list.stream().map(Member::toString).collect(Collectors.joining("\n"));
    }
}
