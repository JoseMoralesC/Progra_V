package cr.ac.cuc.nuevoavatar.integracion;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.springframework.web.context.support.WebApplicationContextUtils;

public final class ConsultaAutomatica {
    private ConsultaAutomatica() {}

    public static boolean esCargaDashboard(HttpServletRequest solicitud, int estado) {
        if (!"GET".equals(solicitud.getMethod()) || estado < 200 || estado >= 300 ||
            !("dashboard".equals(solicitud.getHeader("X-Auditoria-Contexto")) ||
              "servicio".equals(solicitud.getHeader("X-Auditoria-Contexto")))) return false;
        var servletContext = java.util.Objects.requireNonNull(solicitud.getServletContext(), "Falta el contexto de servlet");
        var contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        String clave = contexto.getEnvironment().getProperty("SERVICES_INTERNAL_KEY");
        String recibida = solicitud.getHeader("X-Servicio-Key");
        return clave != null && !clave.isBlank() && recibida != null && MessageDigest.isEqual(
            clave.getBytes(StandardCharsets.UTF_8), recibida.getBytes(StandardCharsets.UTF_8));
    }

    public static void marcarInterna(org.springframework.http.HttpHeaders cabeceras, String clave) {
        if (clave == null || clave.isBlank()) return;
        cabeceras.set("X-Auditoria-Contexto", "servicio");
        cabeceras.set("X-Servicio-Key", clave);
    }
}
