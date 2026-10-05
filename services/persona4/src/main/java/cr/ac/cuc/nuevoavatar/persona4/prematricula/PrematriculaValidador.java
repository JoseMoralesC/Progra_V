package cr.ac.cuc.nuevoavatar.persona4.prematricula;

import java.time.LocalDate;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PrematriculaValidador {
    private final Validator validator;

    public PrematriculaValidador(Validator validator) {
        this.validator = validator;
    }

    /** La fecha actual procede del servidor que ejecuta la operacion. */
    public void validar(DatosPrematricula datos, LocalDate fechaActual) {
        if (datos == null) {
            throw new IllegalArgumentException("Los datos de la prematricula son requeridos");
        }
        if (fechaActual == null) {
            throw new IllegalArgumentException("La fecha actual es requerida para validar el periodo");
        }
        var errores = validator.validate(datos);
        if (!errores.isEmpty()) {
            throw new ConstraintViolationException(errores);
        }

        if (datos.cursos().stream().anyMatch(curso -> curso.nivel() != 1)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Solo se pueden prematricular cursos de primer nivel");
        }
        if (!datos.periodo().fechaInicio().isAfter(fechaActual)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La fecha de inicio del periodo debe ser posterior a la fecha actual");
        }
    }
}
