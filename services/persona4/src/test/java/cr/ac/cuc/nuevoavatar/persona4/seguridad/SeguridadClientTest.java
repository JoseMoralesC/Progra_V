package cr.ac.cuc.nuevoavatar.persona4.seguridad;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class SeguridadClientTest {
    private MockRestServiceServer servidor;
    private SeguridadClient cliente;

    @BeforeEach
    void preparar() {
        var builder = RestClient.builder().baseUrl("http://seguridad.test");
        servidor = MockRestServiceServer.bindTo(builder).build();
        cliente = new SeguridadClient(builder.build());
    }

    @Test
    void validaContratoUsr5() {
        servidor.expect(requestTo("http://seguridad.test/validate"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer prueba"))
                .andRespond(withSuccess("true", MediaType.APPLICATION_JSON));
        assertTrue(cliente.validar("Bearer prueba"));
        servidor.verify();
    }

    @Test
    void rechazaTokenCon401() {
        servidor.expect(requestTo("http://seguridad.test/validate"))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED));
        assertFalse(cliente.validar("Bearer prueba"));
        servidor.verify();
    }

    @ParameterizedTest
    @ValueSource(strings = {"false", "null", ""})
    void noAutorizaSinTrue(String respuesta) {
        servidor.expect(requestTo("http://seguridad.test/validate"))
                .andRespond(withSuccess(respuesta, MediaType.APPLICATION_JSON));
        assertFalse(cliente.validar("Bearer prueba"));
        servidor.verify();
    }

    @Test
    void distingueFallaDeServicioDeTokenInvalido() {
        servidor.expect(requestTo("http://seguridad.test/validate"))
                .andRespond(withServerError());
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE,
                assertThrows(ResponseStatusException.class, () -> cliente.validar("Bearer prueba")).getStatusCode());
        servidor.verify();
    }

    @Test
    void comunicaFalloDeConexion() {
        servidor.expect(requestTo("http://seguridad.test/validate"))
                .andRespond(withException(new IOException("Conexion interrumpida")));
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE,
                assertThrows(ResponseStatusException.class, () -> cliente.validar("Bearer prueba")).getStatusCode());
        servidor.verify();
    }

    @Test
    void leeParametroConContratoUsr3() {
        servidor.expect(requestTo("http://seguridad.test/parametro/DOMESTUD"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer prueba"))
                .andRespond(withSuccess("{\"idParametro\":\"DOMESTUD\",\"valor\":\"cuc.cr\"}", MediaType.APPLICATION_JSON));
        assertEquals("cuc.cr", cliente.obtenerParametro("Bearer prueba", "DOMESTUD"));
        servidor.verify();
    }

    @ParameterizedTest
    @ValueSource(strings = {"null", "{}", "{\"valor\":\"\"}", "{\"valor\":\"   \"}"})
    void rechazaParametroSinValor(String respuesta) {
        servidor.expect(requestTo("http://seguridad.test/parametro/DOMESTUD"))
                .andRespond(withSuccess(respuesta, MediaType.APPLICATION_JSON));
        assertThrows(ResponseStatusException.class, () -> cliente.obtenerParametro("Bearer prueba", "DOMESTUD"));
        servidor.verify();
    }

    @Test
    void informaParametroInexistente() {
        servidor.expect(requestTo("http://seguridad.test/parametro/DOMESTUD"))
                .andRespond(withResourceNotFound());
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, assertThrows(ResponseStatusException.class,
                () -> cliente.obtenerParametro("Bearer prueba", "DOMESTUD")).getStatusCode());
        servidor.verify();
    }

    @Test
    void enviaBitacoraSinInventarFechaNiIdentificador() {
        servidor.expect(requestTo("http://seguridad.test/bitacora"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer prueba"))
                .andExpect(content().json("{\"usuario\":\"ana@cuc.cr\",\"descripcion\":\"El usuario consulta expedientes\"}", true))
                .andRespond(withStatus(HttpStatus.CREATED));
        cliente.registrarBitacora("Bearer prueba", "ana@cuc.cr", "El usuario consulta expedientes");
        servidor.verify();
    }

    @Test
    void noOcultaFalloAlGuardarBitacora() {
        servidor.expect(requestTo("http://seguridad.test/bitacora"))
                .andRespond(withServerError());
        assertThrows(ResponseStatusException.class,
                () -> cliente.registrarBitacora("Bearer prueba", "ana@cuc.cr", "Consulta"));
        servidor.verify();
    }
}
