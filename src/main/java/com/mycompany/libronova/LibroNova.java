package com.mycompany.libronova;

import com.mycompany.libronova.config.AppConfig;
import com.mycompany.libronova.model.User;
import com.mycompany.libronova.service.UserService;
import com.mycompany.libronova.view.BookMenu;
import com.mycompany.libronova.view.MemberMenu;
import com.mycompany.libronova.view.LoanMenu;
import com.mycompany.libronova.view.ExportMenu;
import com.mycompany.libronova.view.LoginDialog;

import javax.swing.*;

public class LibroNova {
    public static void main(String[] args) {
        // inicializa properties + logging
        AppConfig.get("db.url", "");

        // crea admin por defecto si no existe
        UserService userService = new UserService();
        userService.bootstrapAdminIfNeeded();

        // login obligatorio
        LoginDialog login = new LoginDialog();
        User logged = login.prompt();
        if (logged == null) {
            JOptionPane.showMessageDialog(null, "Sesión cancelada. Saliendo…");
            return;
        }
        JOptionPane.showMessageDialog(null, "Bienvenido " + logged.getUsername() + " (" + logged.getRole() + ")");

        String[] ops = {"Libros", "Socios", "Préstamos", "Exportar CSV", "Salir"};
        int op;
        do {
            op = JOptionPane.showOptionDialog(null, "Menú principal", "LibroNova",
                    0, JOptionPane.PLAIN_MESSAGE, null, ops, ops[0]);
            if (op == 0) new BookMenu().mostrar();
            else if (op == 1) new MemberMenu().mostrar();
            else if (op == 2) new LoanMenu().mostrar();
            else if (op == 3) new ExportMenu().mostrar();
        } while (op != 4 && op != JOptionPane.CLOSED_OPTION);
    }
}
