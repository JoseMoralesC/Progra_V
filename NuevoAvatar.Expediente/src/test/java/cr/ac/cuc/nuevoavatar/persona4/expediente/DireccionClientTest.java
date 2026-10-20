package cr.ac.cuc.nuevoavatar.persona4.expediente;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class DireccionClientTest {
    private MockRestServiceServer servidor;
    private DireccionClient cliente;

    @BeforeEach
    void preparar() {
        var builder = RestClient.builder().baseUrl("http://direccion.test");
        servidor = MockRestServiceServer.bindTo(builder).build();
        cliente = new DireccionClient(builder.build());
    }

    private void provincias() {
        servidor.expect(requestTo("http://direccion.test/provincias"))
                .andExpect(header("Authorization", "Bearer prueba"))
                .andRespond(withSuccess("[{\"provinciaId\":1,\"nombre\":\"Prueba\"}]", MediaType.APPLICATION_JSON));
    }

    @Test
    void consumeRutasYCamelCaseDelServicioDeAlejandro() {
        provincias();
        servidor.expect(requestTo("http://direccion.test/cantones/1"))
                .andExpect(header("Authorization", "Bearer prueba"))
                .andRespond(withSuccess("[{\"cantonId\":2,\"provinciaId\":1,\"nombre\":\"Prueba\"}]", MediaType.APPLICATION_JSON));
        servidor.expect(requestTo("http://direccion.test/distritos/1/2"))
                .andExpect(header("Authorization", "Bearer prueba"))
                .andRespond(withSuccess("[{\"distritoId\":3,\"cantonId\":2,\"nombre\":\"Prueba\"}]", MediaType.APPLICATION_JSON));
        assertDoesNotThrow(() -> cliente.validar("Bearer prueba", 1, 2, 3));
        servidor.verify();
    }

    @Test
    void rechazaProvinciaInexistente() {
        provincias();
        assertEquals(HttpStatus.BAD_REQUEST, assertThrows(ResponseStatusException.class,
                () -> cliente.validar("Bearer prueba", 9, 2, 3)).getStatusCode());
        servidor.verify();
    }

    @Test
    void rechazaCantonDeOtraProvincia() {
        provincias();
        servidor.expect(requestTo("http://direccion.test/cantones/1"))
                .andRespond(withSuccess("[{\"cantonId\":2,\"provinciaId\":9}]", MediaType.APPLICATION_JSON));
        assertEquals(HttpStatus.BAD_REQUEST, assertThrows(ResponseStatusException.class,
                () -> cliente.validar("Bearer prueba", 1, 2, 3)).getStatusCode());
        servidor.verify();
    }

    @Test
    void rechazaDistritoDeOtroCanton() {
        provincias();
        servidor.expect(requestTo("http://direccion.test/cantones/1"))
                .andRespond(withSuccess("[{\"cantonId\":2,\"provinciaId\":1}]", MediaType.APPLICATION_JSON));
        servidor.expect(requestTo("http://direccion.test/distritos/1/2"))
                .andRespond(withSuccess("[{\"distritoId\":3,\"cantonId\":9}]", MediaType.APPLICATION_JSON));
        assertEquals(HttpStatus.BAD_REQUEST, assertThrows(ResponseStatusException.class,
                () -> cliente.validar("Bearer prueba", 1, 2, 3)).getStatusCode());
        servidor.verify();
    }

    @Test
    void comunicaFallaDelServicioSinTratarlaComoDireccionInvalida() {
        servidor.expect(requestTo("http://direccion.test/provincias")).andRespond(withServerError());
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, assertThrows(ResponseStatusException.class,
                () -> cliente.validar("Bearer prueba", 1, 2, 3)).getStatusCode());
        servidor.verify();
    }
}
