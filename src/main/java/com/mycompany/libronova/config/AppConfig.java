package com.mycompany.libronova.config;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.LogManager;

public final class AppConfig {
    private static final Properties P = new Properties();

    static {
        try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) throw new IllegalStateException("config.properties no encontrado");
            P.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Error cargando config.properties", e);
        }
        // Logging (log.properties)
        try (InputStream logIn = AppConfig.class.getClassLoader().getResourceAsStream("log.properties")) {
            if (logIn != null) LogManager.getLogManager().readConfiguration(logIn);
        } catch (Exception e) {
            System.err.println("Logging por defecto (no se pudo leer log.properties): " + e.getMessage());
        }

        
    }

    private AppConfig() {}

    public static String get(String key, String def) { return P.getProperty(key, def); }
    public static String get(String key) { return P.getProperty(key); }
}
