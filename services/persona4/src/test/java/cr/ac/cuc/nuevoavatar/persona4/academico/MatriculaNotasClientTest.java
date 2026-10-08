package cr.ac.cuc.nuevoavatar.persona4.academico;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class MatriculaNotasClientTest {
    private MockRestServiceServer servidor;
    private MatriculaNotasClient cliente;

    @BeforeEach
    void preparar() {
        var builder = RestClient.builder().baseUrl("http://matricula.test");
        servidor = MockRestServiceServer.bindTo(builder).build();
        cliente = new MatriculaNotasClient(builder.build());
    }

    @Test
    void consumeMatriculaConParametrosYJsonDeJose() {
        servidor.expect(requestTo("http://matricula.test/matricula?cursoId=10&grupoId=100"))
                .andExpect(method(HttpMethod.GET)).andExpect(header("Authorization", "Bearer prueba"))
                .andRespond(withSuccess("""
                        [{"id":1,"identificacionEstudiante":"00123","cursoId":10,"grupoId":100,
                          "periodoId":20,"estado":"ACTIVA","fechaMatricula":"2026-10-01T08:30:00"}]
                        """, MediaType.APPLICATION_JSON));
        var filas = cliente.obtenerMatriculas("Bearer prueba", 10, 100);
        assertEquals(1, filas.size());
        assertEquals("00123", filas.getFirst().identificacionEstudiante());
        assertEquals(20, filas.getFirst().periodoId());
        assertEquals(2026, filas.getFirst().fechaMatricula().getYear());
        servidor.verify();
    }

    @Test
    void consumeNotasSinCalcularTodaviaUnPromedio() {
        servidor.expect(requestTo("http://matricula.test/obtenernotas?identificacionEstudiante=00123&cursoId=10&grupoId=100"))
                .andExpect(method(HttpMethod.GET)).andExpect(header("Authorization", "Bearer prueba"))
                .andRespond(withSuccess("""
                        [{"id":1,"matriculaId":7,"rubroId":9,"rubro":"Examen","porcentaje":40.00,"nota":85.50}]
                        """, MediaType.APPLICATION_JSON));
        var filas = cliente.obtenerNotas("Bearer prueba", "00123", 10, 100);
        assertEquals(new BigDecimal("40.00"), filas.getFirst().porcentaje());
        assertEquals(new BigDecimal("85.50"), filas.getFirst().nota());
        assertEquals(7, filas.getFirst().matriculaId());
        servidor.verify();
    }

    @Test
    void consultaDesgloseParaConocerLosRubrosReales() {
        servidor.expect(requestTo("http://matricula.test/obtenerdesglose?grupoId=100"))
                .andExpect(method(HttpMethod.GET)).andExpect(header("Authorization", "Bearer prueba"))
                .andRespond(withSuccess("""
                        [{"id":9,"grupoId":100,"nombre":"Examen","porcentaje":100.00}]
                        """, MediaType.APPLICATION_JSON));
        var filas = cliente.obtenerDesglose("Bearer prueba", 100);
        assertEquals(9, filas.getFirst().id());
        assertEquals(new BigDecimal("100.00"), filas.getFirst().porcentaje());
        servidor.verify();
    }

    @Test
    void identificacionNoInyectaParametrosAdicionalesEnLaUrl() {
        servidor.expect(requestTo("http://matricula.test/obtenernotas?identificacionEstudiante=A%26cursoId%3D99&cursoId=10&grupoId=100"))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));
        assertTrue(cliente.obtenerNotas("Bearer prueba", "A&cursoId=99", 10, 100).isEmpty());
        servidor.verify();
    }

    @Test
    void aceptaListasVaciasComoRespuestasValidas() {
        servidor.expect(requestTo("http://matricula.test/matricula?cursoId=10&grupoId=100"))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));
        assertTrue(cliente.obtenerMatriculas("Bearer prueba", 10, 100).isEmpty());
        servidor.verify();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "null", "no es json"})
    void noConvierteRespuestaInvalidaEnUnaListaVacia(String cuerpo) {
        servidor.expect(requestTo("http://matricula.test/matricula?cursoId=10&grupoId=100"))
                .andRespond(withSuccess(cuerpo, MediaType.APPLICATION_JSON));
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, assertThrows(ResponseStatusException.class,
                () -> cliente.obtenerMatriculas("Bearer prueba", 10, 100)).getStatusCode());
        servidor.verify();
    }

    @ParameterizedTest
    @ValueSource(ints = {401, 404, 500})
    void informaFalloExternoSinAparentarQueNoHayDatos(int estado) {
        servidor.expect(requestTo("http://matricula.test/obtenernotas?identificacionEstudiante=123&cursoId=10&grupoId=100"))
                .andRespond(withStatus(HttpStatus.valueOf(estado)));
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, assertThrows(ResponseStatusException.class,
                () -> cliente.obtenerNotas("Bearer prueba", "123", 10, 100)).getStatusCode());
        servidor.verify();
    }
}
