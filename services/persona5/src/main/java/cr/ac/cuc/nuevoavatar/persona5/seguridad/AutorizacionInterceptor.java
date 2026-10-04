package cr.ac.cuc.nuevoavatar.persona5.seguridad;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AutorizacionInterceptor implements HandlerInterceptor {

    public static final String TOKEN_ATTRIBUTE = "seguridad.token";
    public static final String USER_ATTRIBUTE = "seguridad.usuario";

    private final SeguridadClient seguridad;

    public AutorizacionInterceptor(SeguridadClient seguridad) {
        this.seguridad = seguridad;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws IOException {
        String autorizacion = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (autorizacion == null
                || !autorizacion.regionMatches(true, 0, "Bearer ", 0, 7)
                || !seguridad.validar(autorizacion)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
                "{\"status\":401,\"error\":\"No autorizado\"}"
            );
            return false;
        }

        request.setAttribute(TOKEN_ATTRIBUTE, autorizacion);
        request.setAttribute(USER_ATTRIBUTE, JwtUsuario.obtener(autorizacion));
        return true;
    }
}
