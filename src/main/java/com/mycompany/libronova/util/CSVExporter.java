package com.mycompany.libronova.util;

import com.mycompany.libronova.config.AppConfig;
import com.mycompany.libronova.model.Book;
import com.mycompany.libronova.model.Loan;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public class CSVExporter {

    private CSVExporter() {}

    private static Path exportDir() throws IOException {
        String dir = AppConfig.get("export.dir", "export");
        Path p = Paths.get(dir);
        if (!Files.exists(p)) Files.createDirectories(p);
        return p;
    }

    public static Path exportBooks(List<Book> books) {
        try {
            Path out = exportDir().resolve("libros_export.csv");
            try (BufferedWriter w = Files.newBufferedWriter(out, StandardCharsets.UTF_8)) {
                w.write("isbn,titulo,autor,categoria,totales,disponibles,precio,activo\n");
                for (Book b : books) {
                    w.write(String.format("\"%s\",\"%s\",\"%s\",\"%s\",%d,%d,%.2f,%s%n",
                            nz(b.getIsbn()), nz(b.getTitulo()), nz(b.getAutor()), nz(b.getCategoria()),
                            b.getEjemplaresTotales(), b.getEjemplaresDisponibles(),
                            b.getPrecioReferencia(), b.isActivo()));
                }
            }
            AppLogger.http("GET", "/export/books", "Archivo: " + out.toAbsolutePath());
            return out;
        } catch (IOException e) {
            AppLogger.error("Error exportando libros", e);
            throw new RuntimeException("No se pudo exportar libros", e);
        }
    }

    public static Path exportVencidos(List<Loan> loans) {
        try {
            Path out = exportDir().resolve("prestamos_vencidos.csv");
            try (BufferedWriter w = Files.newBufferedWriter(out, StandardCharsets.UTF_8)) {
                w.write("id,member_id,book_id,fecha_prestamo,fecha_vencimiento,fecha_devolucion,multa,estado\n");
                for (Loan l : loans) {
                    w.write(String.format("%d,%d,%d,%s,%s,%s,%d,%s%n",
                            nz(l.getId()), nz(l.getMemberId()), nz(l.getBookId()),
                            nz(l.getFechaPrestamo()), nz(l.getFechaVencimiento()),
                            nz(l.getFechaDevolucion()), l.getMulta(), nz(l.getEstado())));
                }
            }
            AppLogger.http("GET", "/export/loans/vencidos", "Archivo: " + out.toAbsolutePath());
            return out;
        } catch (IOException e) {
            AppLogger.error("Error exportando vencidos", e);
            throw new RuntimeException("No se pudo exportar préstamos vencidos", e);
        }
    }

    private static String nz(Object o) { return o == null ? "" : o.toString(); }

}
