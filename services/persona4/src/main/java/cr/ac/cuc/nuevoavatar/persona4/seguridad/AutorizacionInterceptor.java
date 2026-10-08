package cr.ac.cuc.nuevoavatar.persona4.seguridad;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AutorizacionInterceptor implements HandlerInterceptor {
    public static final String TOKEN_ATTRIBUTE = "seguridad.token";
    public static final String USER_ATTRIBUTE = "seguridad.usuario";
    private final SeguridadClient seguridad;
    private final ObjectMapper mapper;

    public AutorizacionInterceptor(SeguridadClient seguridad, ObjectMapper mapper) {
        this.seguridad = seguridad;
        this.mapper = mapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws IOException {
        String autorizacion = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (autorizacion == null || !autorizacion.regionMatches(true, 0, "Bearer ", 0, 7)
                || autorizacion.substring(7).isBlank()) {
            return rechazar(response, 401, "No autorizado");
        }
        try {
            if (!seguridad.validar(autorizacion)) {
                return rechazar(response, 401, "No autorizado");
            }
        } catch (ResponseStatusException ex) {
            return rechazar(response, ex.getStatusCode().value(), ex.getReason());
        }

        // USR5 valida el JWT antes de leer el usuario que se emplea en GEN1.
        try {
            String[] partes = autorizacion.substring(7).trim().split("\\.", -1);
            if (partes.length != 3) {
                return rechazar(response, 401, "No autorizado");
            }
            var payload = mapper.readTree(Base64.getUrlDecoder().decode(partes[1]));
            var usuario = payload == null ? null : payload.get("sub");
            if (usuario == null || !usuario.isTextual() || usuario.asText().isBlank()) {
                return rechazar(response, 401, "No autorizado");
            }
            request.setAttribute(TOKEN_ATTRIBUTE, autorizacion);
            request.setAttribute(USER_ATTRIBUTE, usuario.asText());
        } catch (IllegalArgumentException | IOException ex) {
            return rechazar(response, 401, "No autorizado");
        }
        return true;
    }

    private boolean rechazar(HttpServletResponse response, int status, String mensaje)
            throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getOutputStream(), Map.of("status", status, "error", mensaje));
        return false;
    }
}
