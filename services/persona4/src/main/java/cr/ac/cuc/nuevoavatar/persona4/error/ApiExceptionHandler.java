package cr.ac.cuc.nuevoavatar.persona4.error;

import java.util.Map;
import cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptor;
import cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.ErrorResponse;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);
    private final BitacoraService bitacora;

    public ApiExceptionHandler(BitacoraService bitacora) {
        this.bitacora = bitacora;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class,
            HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Map<String, Object>> datosInvalidos(Exception ex) {
        return respuesta(400, "Los datos de la solicitud estan incompletos o tienen un formato invalido");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Map<String, Object>> duplicado() {
        return respuesta(409, "La operacion duplica una llave existente");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> integridad() {
        return respuesta(409, "La operacion entra en conflicto con datos relacionados o restricciones de la base");
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> estado(ResponseStatusException ex, HttpServletRequest request) {
        if (ex.getStatusCode().is5xxServerError()) registrarError(ex, request);
        return respuesta(ex.getStatusCode().value(), ex.getReason() == null ? "No fue posible completar la operacion" : ex.getReason());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> tecnico(Exception ex, HttpServletRequest request) {
        if (ex instanceof ErrorResponse error && error.getStatusCode().is4xxClientError()) {
            return respuesta(error.getStatusCode().value(), "La solicitud no corresponde a una operacion valida");
        }
        registrarError(ex, request);
        return respuesta(500, "No fue posible completar la operacion");
    }

    private void registrarError(Exception ex, HttpServletRequest request) {
        String token = (String) request.getAttribute(AutorizacionInterceptor.TOKEN_ATTRIBUTE);
        String usuario = (String) request.getAttribute(AutorizacionInterceptor.USER_ATTRIBUTE);
        String modulo;
        if (request.getRequestURI().startsWith("/listadoestudiantes")) modulo = "ACA2";
        else if (request.getRequestURI().startsWith("/historialacademico")) modulo = "ACA1";
        else if (request.getRequestURI().startsWith("/prematricula")) modulo = "MAT1";
        else modulo = "MAT3";
        LOG.error("Fallo tecnico en {}: {}", modulo, ex.getClass().getSimpleName());
        if (token == null || usuario == null) return;
        try {
            bitacora.errorTecnico(token, usuario, modulo + " " + request.getMethod(), ex);
        } catch (RuntimeException falloBitacora) {
            // Una falla de GEN1 no sustituye el error original ni provoca reintentos recursivos.
            LOG.error("No fue posible registrar el error en GEN1: {}", falloBitacora.getClass().getSimpleName());
        }
    }

    private ResponseEntity<Map<String, Object>> respuesta(int status, String error) {
        return ResponseEntity.status(status).body(Map.of("status", status, "error", error));
    }
}
