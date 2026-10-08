package cr.ac.cuc.nuevoavatar.persona4.expediente;

import cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClient;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ExpedienteValidador {
    private final Validator validator;
    private final SeguridadClient seguridad;

    public ExpedienteValidador(Validator validator, SeguridadClient seguridad) {
        this.validator = validator;
        this.seguridad = seguridad;
    }

    public void validar(DatosExpediente datos, String autorizacion) {
        if (datos == null) {
            throw new IllegalArgumentException("Los datos del expediente son requeridos");
        }
        var errores = validator.validate(datos);
        if (!errores.isEmpty()) {
            throw new ConstraintViolationException(errores);
        }

        // El dominio procede de USR3; su ausencia no habilita correos de cualquier dominio.
        String dominioPermitido = seguridad.obtenerParametro(autorizacion, "DOMESTUD").strip();
        String dominioCorreo = datos.email().substring(datos.email().lastIndexOf('@') + 1);
        if (!dominioCorreo.equalsIgnoreCase(dominioPermitido)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El email no pertenece al dominio permitido para estudiantes");
        }
    }
}
