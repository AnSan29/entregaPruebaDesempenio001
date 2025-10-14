/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.libronova;

import com.mycompany.libronova.config.AppConfig;
import com.mycompany.libronova.view.BookMenu;
import com.mycompany.libronova.view.MemberMenu;
import com.mycompany.libronova.view.LoanMenu;
import com.mycompany.libronova.view.ExportMenu;

import javax.swing.*;
/**
 *
 * @author andres
 */
public class LibroNova {
    public static void main(String[] args) {
        // inicializa properties + logging
       AppConfig.get("db.url", "");

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
