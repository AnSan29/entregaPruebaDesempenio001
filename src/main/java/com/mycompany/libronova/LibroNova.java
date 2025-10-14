/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.libronova;

import com.mycompany.libronova.model.User;
import com.mycompany.libronova.service.UserService;
import com.mycompany.libronova.view.LoginDialog;

/**
 *
 * @author andres
 */
public class LibroNova {

    public static void main(String[] args) {
        var userService = new UserService();

        //crear un usuario de prueba
        try {
            userService.create(new User("admin","123","ADMIN","ACTIVO"));
        } catch (Exception ignored) {}

        var login = new LoginDialog();
        var logged = login.prompt();
        if(logged != null){
            System.out.println("Bienvenido "+ logged.getUsername() + " ("+ logged.getRole() + ")" );
            userService.listarUsuarios();
        }else{
            System.out.println("Login cancelado.");
        }
    }
}
