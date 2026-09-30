package cr.ac.cuc.nuevoavatar.persona2.periodo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class PeriodoServiceTest {

    @Test
    void rechazaFechaFinalIgualOAnteriorAlInicio() {
        PeriodoService servicio = new PeriodoService(null);
        LocalDate fecha = LocalDate.of(2026, 1, 1);

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.crear(new PeriodoRequest(2026, 1, fecha, fecha))
        );

        assertEquals(HttpStatus.UNPROCESSABLE_CONTENT, error.getStatusCode());
    }
}
