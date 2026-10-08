package cr.ac.cuc.nuevoavatar.persona2.seguridad;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.regex.Pattern;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Component
public class BitacoraFilter extends OncePerRequestFilter {

    private static final Pattern SECRETOS_JSON = Pattern.compile(
        "(\"(?:contrasena|password|password_hash|passwordHash)\"\\s*:\\s*)\"(?:\\\\.|[^\"\\\\])*\"",
        Pattern.CASE_INSENSITIVE
    );

    private static final Set<String> RUTAS_EXCLUIDAS = Set.of(
        "/error", "/swagger-ui.html"
    );

    private final SeguridadClient seguridad;

    public BitacoraFilter(SeguridadClient seguridad) {
        this.seguridad = seguridad;
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
        String descripcion = descripcion(request, response, cuerpo);
        seguridad.registrarBitacora(
            token.toString(), usuario.toString(), descripcion
        );
    }

    private String descripcion(
            HttpServletRequest request,
            HttpServletResponse response,
            String cuerpo) {
        String metodo = request.getMethod();
        String ruta = request.getRequestURI();
        if (response.getStatus() >= 500) {
            return "Error técnico en " + metodo + " " + ruta
                + ". Estado: " + response.getStatus();
        }
        if ("GET".equals(metodo)) {
            return "El usuario consulta " + ruta;
        }
        if ("DELETE".equals(metodo)) {
            return "Eliminación solicitada en " + ruta;
        }
        String detalle = cuerpo.isBlank() ? "{}" : ocultarSecretos(cuerpo);
        return metodo + " " + ruta + ". JSON: " + detalle;
    }

    static String ocultarSecretos(String json) {
        return SECRETOS_JSON.matcher(json).replaceAll("$1\"[OCULTA]\"");
    }
}
