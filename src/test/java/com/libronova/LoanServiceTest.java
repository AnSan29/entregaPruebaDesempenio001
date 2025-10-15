package com.libronova;

import com.mycompany.libronova.service.LoanService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoanServiceTest {

    @Test
    void calcularMulta_sinRetraso_daCero() {
        var svc = new LoanService();
        var vto = LocalDate.now();
        var dev = vto;
        int multa = svc.calcularMulta(vto, dev, 1500);
        assertEquals(0, multa);
    }

    @Test
    void calcularMulta_con2DiasRetraso() {
        var svc = new LoanService();
        var vto = LocalDate.now().minusDays(2);
        var dev = LocalDate.now();
        int multa = svc.calcularMulta(vto, dev, 1500);
        assertEquals(2 * 1500, multa);
    }

}
