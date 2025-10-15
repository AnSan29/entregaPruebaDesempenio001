package com.mycompany.libronova.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbManager {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = DbManager.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) throw new IllegalStateException("Archivo config.properties no encontrado");
            PROPS.load(in);
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            throw new RuntimeException("Error cargando configuración DB", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                PROPS.getProperty("db.url"),
                PROPS.getProperty("db.user"),
                PROPS.getProperty("db.password")
        );
    }

}
