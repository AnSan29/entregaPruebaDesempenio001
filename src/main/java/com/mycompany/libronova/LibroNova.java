/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.libronova;

import com.mycompany.libronova.view.BookMenu;
import com.mycompany.libronova.view.MemberMenu;
import com.mycompany.libronova.view.LoanMenu;

/**
 *
 * @author andres
 */
public class LibroNova {

    public static void main(String[] args) {
         String[] ops = {"Libros", "Socios", "Préstamos", "Salir"};
        int op;
        do {
            op = javax.swing.JOptionPane.showOptionDialog(null, "Menú principal", "LibroNova",
                    0, javax.swing.JOptionPane.PLAIN_MESSAGE, null, ops, ops[0]);
            if (op == 0) new BookMenu().mostrar();
            else if (op == 1) new MemberMenu().mostrar();
            else if (op == 2) new LoanMenu().mostrar();
        } while (op != 3 && op != javax.swing.JOptionPane.CLOSED_OPTION);
    }
}
