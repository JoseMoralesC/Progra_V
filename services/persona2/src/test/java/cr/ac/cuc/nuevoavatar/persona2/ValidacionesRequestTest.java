package cr.ac.cuc.nuevoavatar.persona2;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import cr.ac.cuc.nuevoavatar.persona2.curso.CursoRequest;
import cr.ac.cuc.nuevoavatar.persona2.grupo.GrupoRequest;
import cr.ac.cuc.nuevoavatar.persona2.periodo.PeriodoRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ValidacionesRequestTest {

    private static Validator validator;

    @BeforeAll
    static void prepararValidador() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void cursoAceptaNivelEntreUnoYDoce() {
        assertTrue(validator.validate(
            new CursoRequest(1, 1, "Programación")
        ).isEmpty());
        assertTrue(validator.validate(
            new CursoRequest(1, 12, "Programación")
        ).isEmpty());
    }

    @Test
    void cursoRechazaNivelFueraDelRangoYNombreConNumeros() {
        assertFalse(validator.validate(
            new CursoRequest(1, 0, "Programación 5")
        ).isEmpty());
        assertFalse(validator.validate(
            new CursoRequest(1, 13, "Programación")
        ).isEmpty());
    }

    @Test
    void periodoExigeTodosLosDatos() {
        PeriodoRequest invalido = new PeriodoRequest(
            null, null, null, LocalDate.now()
        );
        assertFalse(validator.validate(invalido).isEmpty());
    }

    @Test
    void grupoExigeIdentificadoresPositivosHorarioYCupo() {
        GrupoRequest invalido = new GrupoRequest(
            0, -1, 0, " ", 0, -2
        );
        assertFalse(validator.validate(invalido).isEmpty());
    }
}
