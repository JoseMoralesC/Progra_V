package cr.ac.cuc.nuevoavatar.persona2;

import cr.ac.cuc.nuevoavatar.integracion.ConsultaAutomatica;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Proxy;
import java.util.*;
import org.springframework.core.env.MapPropertySource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

/** Sin SQL ni servicios: comprueba que no se omitan errores, escrituras o marcas no confiables. */
public final class PruebaConsultaAutomatica {
    public static void main(String[] args) {
        var cabeceras = new HashMap<String, String>();
        var contexto = new GenericWebApplicationContext();
        contexto.getEnvironment().getPropertySources().addFirst(new MapPropertySource("prueba", Map.of(
            "SERVICES_INTERNAL_KEY", "clave-ficticia-de-prueba")));
        var servlet = (ServletContext) Proxy.newProxyInstance(ServletContext.class.getClassLoader(), new Class<?>[]{ServletContext.class},
            (p, m, a) -> m.getName().equals("getAttribute") && WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE.equals(a[0]) ? contexto : null);
        String[] metodo = {"GET"};
        var solicitud = (HttpServletRequest) Proxy.newProxyInstance(HttpServletRequest.class.getClassLoader(), new Class<?>[]{HttpServletRequest.class},
            (p, m, a) -> switch (m.getName()) {
                case "getMethod" -> metodo[0];
                case "getHeader" -> cabeceras.get(a[0]);
                case "getServletContext" -> servlet;
                default -> null;
            });
        comprobar(!ConsultaAutomatica.esCargaDashboard(solicitud, 200));
        cabeceras.put("X-Auditoria-Contexto", "dashboard");
        comprobar(!ConsultaAutomatica.esCargaDashboard(solicitud, 200));
        cabeceras.put("X-Servicio-Key", "incorrecta");
        comprobar(!ConsultaAutomatica.esCargaDashboard(solicitud, 200));
        cabeceras.put("X-Servicio-Key", "clave-ficticia-de-prueba");
        comprobar(ConsultaAutomatica.esCargaDashboard(solicitud, 200));
        for (int estado : new int[]{401, 403, 404, 500}) comprobar(!ConsultaAutomatica.esCargaDashboard(solicitud, estado));
        for (String verbo : new String[]{"POST", "PUT", "PATCH", "DELETE"}) {
            metodo[0] = verbo;
            comprobar(!ConsultaAutomatica.esCargaDashboard(solicitud, 200));
        }
        contexto.close();
        System.out.println("12 verificaciones Java de consultas automáticas completadas, sin acceso a DB.");
    }
    private static void comprobar(boolean condicion) {
        if (!condicion) throw new AssertionError("Clasificación incorrecta de carga automática");
    }
}
