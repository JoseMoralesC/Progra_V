package cr.ac.cuc.nuevoavatar.persona5.error;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validacion(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errores.putIfAbsent(error.getField(), error.getDefaultMessage())
        );
        return respuesta(HttpStatus.BAD_REQUEST, "Solicitud invalida", request, errores);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> estado(
            ResponseStatusException ex,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return respuesta(status, ex.getReason(), request, Map.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> inesperado(
            Exception ex,
            HttpServletRequest request) {
        LOG.error("Error en {} {}", request.getMethod(), request.getRequestURI(), ex);
        return respuesta(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Error interno del servidor",
            request,
            Map.of()
        );
    }

    private ResponseEntity<ApiError> respuesta(
            HttpStatus status,
            String mensaje,
            HttpServletRequest request,
            Map<String, String> errores) {
        return ResponseEntity.status(status).body(new ApiError(
            OffsetDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            mensaje,
            request.getRequestURI(),
            errores
        ));
    }
}
