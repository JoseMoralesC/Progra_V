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

    private static final String CAMBIO = BitacoraFilter.class.getName() + ".cambio";
    private static final String REGISTRO_CONFIRMADO = BitacoraFilter.class.getName() + ".registroConfirmado";

    public static void marcarRegistroConfirmado() {
        var atributos = org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
        if (atributos instanceof org.springframework.web.context.request.ServletRequestAttributes solicitud) {
            solicitud.getRequest().setAttribute(REGISTRO_CONFIRMADO, Boolean.TRUE);
        }
    }

    // La descripción conserva el estado confirmado sin incluir credenciales.
    public static void cambio(String accion, Object anterior, Object actual) {
        var atributos = org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
        if (!(atributos instanceof org.springframework.web.context.request.ServletRequestAttributes solicitud)) return;
        solicitud.getRequest().setAttribute(CAMBIO, descripcionCambio(accion, anterior, actual));
        cr.ac.cuc.nuevoavatar.integracion.AuditoriaFiable.registrarCambioHttp(solicitud.getRequest(), descripcionCambio(accion, anterior, actual));
    }

    public static String descripcionCambio(String accion, Object anterior, Object actual) {
        try {
            var estados = new java.util.LinkedHashMap<String, Object>();
            estados.put("anterior", anterior);
            estados.put("actual", actual);
            String json = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(estados);
            return accion + ". JSON: " + json;
        } catch (com.fasterxml.jackson.core.JsonProcessingException ex) {
            throw new IllegalStateException("No se pudo preparar la bitácora", ex);
        }
    }

    private static final Pattern SECRETOS_JSON = Pattern.compile(
        "(\"(?:contrasena(?:actual|nueva|confirmacion)?|confirmacionContrasena|password(?:actual|nueva|confirmation)?|currentPassword|newPassword|confirmPassword|password_hash|passwordHash)\"\\s*:\\s*)\"(?:\\\\.|[^\"\\\\])*\"",
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
        boolean fallo = false;
        try {
            filterChain.doFilter(envuelta, response);
        } catch (IOException | ServletException | RuntimeException ex) {
            fallo = true;
            throw ex;
        } finally {
            registrar(envuelta, response, fallo);
        }
    }

    private void registrar(
            ContentCachingRequestWrapper request,
            HttpServletResponse response, boolean fallo) {
        if (!fallo && cr.ac.cuc.nuevoavatar.integracion.ConsultaAutomatica.esCargaDashboard(request, response.getStatus())) return;
        if (!fallo && response.getStatus() < 400 && Boolean.TRUE.equals(request.getAttribute(cr.ac.cuc.nuevoavatar.integracion.AuditoriaFiable.ENCOLADA))) return;
        if (Boolean.TRUE.equals(request.getAttribute(REGISTRO_CONFIRMADO))) return;
        Object token = request.getAttribute(AutorizacionInterceptor.TOKEN_ATTRIBUTE);
        Object usuario = request.getAttribute(AutorizacionInterceptor.USER_ATTRIBUTE);
        if (token == null || usuario == null) {
            return;
        }

        String cuerpo = new String(
            request.getContentAsByteArray(),
            StandardCharsets.UTF_8
        );
        String descripcion = descripcion(request, response, cuerpo) + ". Estado HTTP: " + (fallo ? 500 : response.getStatus());
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
        Object cambio = request.getAttribute(CAMBIO);
        if (response.getStatus() < 400 && cambio != null) return cambio.toString();
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

