package cr.ac.cuc.nuevoavatar.persona5.seguridad;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Component
public class BitacoraFilter extends OncePerRequestFilter {

    public static final String DETALLE_ATTRIBUTE = "bitacora.detalle";
    private static final ObjectMapper JSON = new ObjectMapper().findAndRegisterModules();

    private static final Set<String> RUTAS_EXCLUIDAS = Set.of(
        "/error", "/swagger-ui.html"
    );

    private final SeguridadClient seguridad;

    public BitacoraFilter(SeguridadClient seguridad) {
        this.seguridad = seguridad;
    }

    public static void auditar(String descripcion) {
        var attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes sra) {
            sra.getRequest().setAttribute(DETALLE_ATTRIBUTE, descripcion);
        }
    }

    public static String comoJson(Object valor) {
        try {
            return JSON.writeValueAsString(valor);
        } catch (JsonProcessingException ex) {
            return String.valueOf(valor);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String ruta = request.getRequestURI();
        return RUTAS_EXCLUIDAS.contains(ruta)
            || ruta.startsWith("/swagger-ui/")
            || ruta.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper envuelta =
            new ContentCachingRequestWrapper(request, 1_048_576);
        try {
            filterChain.doFilter(envuelta, response);
        } finally {
            registrar(envuelta, response);
        }
    }

    private void registrar(
            ContentCachingRequestWrapper request,
            HttpServletResponse response) {
        Object token = request.getAttribute(AutorizacionInterceptor.TOKEN_ATTRIBUTE);
        Object usuario = request.getAttribute(AutorizacionInterceptor.USER_ATTRIBUTE);
        if (token == null || usuario == null) {
            return;
        }

        String cuerpo = new String(
            request.getContentAsByteArray(),
            StandardCharsets.UTF_8
        );
        seguridad.registrarBitacora(
            token.toString(),
            usuario.toString(),
            descripcion(request, response, cuerpo)
        );
    }

    private String descripcion(
            HttpServletRequest request,
            HttpServletResponse response,
            String cuerpo) {
        String metodo = request.getMethod();
        String ruta = request.getRequestURI();
        if (response.getStatus() >= 500) {
            return "Error tecnico en " + metodo + " " + ruta
                + ". Estado: " + response.getStatus();
        }
        Object auditado = request.getAttribute(DETALLE_ATTRIBUTE);
        if (auditado != null && response.getStatus() < 400) {
            return auditado.toString();
        }
        if ("GET".equals(metodo)) {
            return "El usuario consulta " + ruta;
        }
        if ("DELETE".equals(metodo)) {
            return "Eliminacion solicitada en " + ruta;
        }
        String detalle = cuerpo.isBlank() ? "{}" : cuerpo;
        return metodo + " " + ruta + ". JSON: " + detalle;
    }
}

