package com.mycompany.libronova.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AppLogger {
    private static final Logger LOG = Logger.getLogger("LibroNova");

    private AppLogger() {}

    public static void info(String msg)  { LOG.log(Level.INFO,  msg); }
    public static void error(String msg, Throwable t) { LOG.log(Level.SEVERE, msg, t); }

    // “Simulación HTTP” para la rúbrica
    public static void http(String method, String path, String detail) {
        info(method + " " + path + " | " + detail);
        System.out.println(method + " " + path + " | " + detail);
    }
}
