package com.mycompany.libronova.view;

import com.mycompany.libronova.exception.CredencialesInvalidasException;
import com.mycompany.libronova.model.User;
import com.mycompany.libronova.service.UserService;
import javax.swing.JOptionPane;

public class LoginDialog {

    private final UserService service = new UserService();

    public User prompt() {
        while (true) {
            String user = JOptionPane.showInputDialog("Usuario:");
            if (user == null) return null;
            String pass = JOptionPane.showInputDialog("Contraseña:");
            if (pass == null) return null;
            try {
                return service.login(user, pass).orElse(null);
            } catch (CredencialesInvalidasException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
