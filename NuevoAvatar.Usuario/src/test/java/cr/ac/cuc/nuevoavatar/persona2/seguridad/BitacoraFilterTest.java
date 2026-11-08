package cr.ac.cuc.nuevoavatar.persona2.seguridad;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BitacoraFilterTest {
    @Test
    void registraAmbosEstadosYNoConfirmaCambiosFallidos() throws Exception {
        var descripciones = new java.util.ArrayList<String>();
        var cliente = new SeguridadClient("http://localhost", "http://localhost") {
            @Override
            public void registrarBitacora(String token, String usuario, String descripcion) {
                descripciones.add(descripcion);
            }
        };
        for (int estado : new int[] { 200, 500 }) {
            var request = new org.springframework.mock.web.MockHttpServletRequest("PUT", "/usuario/ana@cuc.cr");
            request.setAttribute(AutorizacionInterceptor.TOKEN_ATTRIBUTE, "Bearer prueba");
            request.setAttribute(AutorizacionInterceptor.USER_ATTRIBUTE, "admin@cuc.ac.cr");
            var response = new org.springframework.mock.web.MockHttpServletResponse();
            org.springframework.web.context.request.RequestContextHolder.setRequestAttributes(
                new org.springframework.web.context.request.ServletRequestAttributes(request));
            try {
                new BitacoraFilter(cliente).doFilter(request, response, (peticion, respuesta) -> {
                    BitacoraFilter.cambio("Modificación de usuario",
                        java.util.Map.of("nombre", "Ana"), java.util.Map.of("nombre", "Ana Mora"));
                    ((jakarta.servlet.http.HttpServletResponse) respuesta).setStatus(estado);
                });
            } finally {
                org.springframework.web.context.request.RequestContextHolder.resetRequestAttributes();
            }
        }
        assertEquals("Modificación de usuario. JSON: {\"anterior\":{\"nombre\":\"Ana\"},\"actual\":{\"nombre\":\"Ana Mora\"}}",
            descripciones.get(0));
        assertEquals("Error técnico en PUT /usuario/ana@cuc.cr. Estado: 500", descripciones.get(1));
    }

    @Test
    void ocultaContrasenaYConservaDatosDelRegistro() {
        assertEquals(
            "{\"nombre\":\"Prueba\",\"contrasena\":\"[OCULTA]\"}",
            BitacoraFilter.ocultarSecretos("{\"nombre\":\"Prueba\",\"contrasena\":\"clave\"}")
        );
    }

    @Test
    void ocultaSecretosConComillasEscapadasYCamposAnidados() {
        assertEquals(
            "{\"PASSWORD\": \"[OCULTA]\",\"anterior\":{\"passwordHash\":\"[OCULTA]\"}}",
            BitacoraFilter.ocultarSecretos("{\"PASSWORD\": \"a\\\"b\",\"anterior\":{\"passwordHash\":\"hash\"}}")
        );
    }

    @Test
    void conservaJsonSinSecretos() {
        String json = "{\"nombre\":\"Institucion\",\"id\":1}";
        assertEquals(json, BitacoraFilter.ocultarSecretos(json));
    }
}
