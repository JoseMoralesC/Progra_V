package cr.ac.cuc.nuevoavatar.persona2.seguridad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class AutorizacionInterceptorTest {

    @Test
    void rechazaSolicitudSinBearer() throws Exception {
        AutorizacionInterceptor interceptor =
            new AutorizacionInterceptor(mock(SeguridadClient.class));
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean permitido = interceptor.preHandle(
            new MockHttpServletRequest(), response, new Object()
        );

        assertFalse(permitido);
        assertEquals(401, response.getStatus());
    }

    @Test
    void aceptaBearerValidadoPorSeguridad() throws Exception {
        SeguridadClient seguridad = mock(SeguridadClient.class);
        when(seguridad.validar("Bearer token-valido")).thenReturn(true);
        AutorizacionInterceptor interceptor =
            new AutorizacionInterceptor(seguridad);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token-valido");

        assertTrue(interceptor.preHandle(
            request, new MockHttpServletResponse(), new Object()
        ));
    }
}
