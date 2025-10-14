package com.mycompany.libronova.util;

import java.time.LocalDateTime;

public class AppLogger {
    public static void log(String endpoint, String message) {
        System.out.printf("[%s] %s | %s%n", LocalDateTime.now(), endpoint, message);
    }
}
