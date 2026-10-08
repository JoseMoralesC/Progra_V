package cr.ac.cuc.nuevoavatar.persona4.seguridad;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AutorizacionInterceptorTest {
    private SeguridadClient seguridad;
    private AutorizacionInterceptor interceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void preparar() {
        seguridad = mock(SeguridadClient.class);
        interceptor = new AutorizacionInterceptor(seguridad, new ObjectMapper());
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    private String token(String payload) {
        return "Bearer e30." + Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payload.getBytes(StandardCharsets.UTF_8)) + ".firma";
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"Basic prueba", "Bearer ", "Bearer    "})
    void rechazaHeaderAusenteOInvalido(String header) throws Exception {
        if (header != null) request.addHeader("Authorization", header);
        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(401, response.getStatus());
        verifyNoInteractions(seguridad);
    }

    @Test
    void noConfiaEnSubSinValidacionExterna() throws Exception {
        String header = token("{\"sub\":\"ana@cuc.cr\"}");
        request.addHeader("Authorization", header);
        when(seguridad.validar(header)).thenReturn(false);
        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertNull(request.getAttribute(AutorizacionInterceptor.USER_ATTRIBUTE));
        assertEquals(401, response.getStatus());
    }

    @Test
    void conservaUsuarioYTokenValidados() throws Exception {
        String header = token("{\"sub\":\"ana@cuc.cr\"}");
        request.addHeader("Authorization", header);
        when(seguridad.validar(header)).thenReturn(true);
        assertTrue(interceptor.preHandle(request, response, new Object()));
        assertEquals("ana@cuc.cr", request.getAttribute(AutorizacionInterceptor.USER_ATTRIBUTE));
        assertEquals(header, request.getAttribute(AutorizacionInterceptor.TOKEN_ATTRIBUTE));
        verify(seguridad).validar(header);
    }

    @ParameterizedTest
    @ValueSource(strings = {"{}", "null", "{\"sub\":\" \"}", "{\"sub\":123}", "no es json"})
    void noRegistraUsuarioInventadoSiPayloadInvalido(String payload) throws Exception {
        String header = token(payload);
        request.addHeader("Authorization", header);
        when(seguridad.validar(header)).thenReturn(true);
        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(401, response.getStatus());
        assertNull(request.getAttribute(AutorizacionInterceptor.USER_ATTRIBUTE));
    }

    @Test
    void responde503CuandoSeguridadNoResponde() throws Exception {
        request.addHeader("Authorization", "Bearer prueba");
        when(seguridad.validar("Bearer prueba"))
                .thenThrow(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Seguridad no disponible"));
        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(503, response.getStatus());
        assertFalse(response.getContentAsString().contains("prueba"));
    }
}
