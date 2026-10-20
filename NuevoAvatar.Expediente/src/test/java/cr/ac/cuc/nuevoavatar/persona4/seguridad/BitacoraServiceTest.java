package cr.ac.cuc.nuevoavatar.persona4.seguridad;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BitacoraServiceTest {
    private SeguridadClient seguridad;
    private BitacoraService bitacora;

    @BeforeEach
    void preparar() {
        seguridad = mock(SeguridadClient.class);
        bitacora = new BitacoraService(seguridad, new ObjectMapper());
    }

    @Test
    void conservaEstadoAnteriorAunqueElObjetoCambie() {
        var registro = new HashMap<>(Map.of("nombreCompleto", "Ana"));
        String anterior = bitacora.comoJson(registro);
        registro.put("nombreCompleto", "Maria");
        String actual = bitacora.comoJson(registro);
        bitacora.modificacion("Bearer prueba", "ana@cuc.cr", "expediente", anterior, actual);
        verify(seguridad).registrarBitacora("Bearer prueba", "ana@cuc.cr",
                "Modificacion de expediente. Anterior: {\"nombreCompleto\":\"Ana\"} Actual: {\"nombreCompleto\":\"Maria\"}");
    }

    @Test
    void creacionYEliminacionIncluyenRegistro() {
        String json = bitacora.comoJson(Map.of("identificacion", "123"));
        bitacora.creacion("Bearer prueba", "ana@cuc.cr", "expediente", json);
        bitacora.eliminacion("Bearer prueba", "ana@cuc.cr", "expediente", json);
        verify(seguridad).registrarBitacora("Bearer prueba", "ana@cuc.cr", "Creacion de expediente: " + json);
        verify(seguridad).registrarBitacora("Bearer prueba", "ana@cuc.cr", "Eliminacion de expediente: " + json);
    }

    @Test
    void consultaRespetaTextoDelDocumento() {
        bitacora.consulta("Bearer prueba", "ana@cuc.cr", "expedientes");
        verify(seguridad).registrarBitacora("Bearer prueba", "ana@cuc.cr", "El usuario consulta expedientes");
    }

    @Test
    void registraTipoDeErrorSinCopiarMensajeConCredenciales() {
        bitacora.errorTecnico("Bearer prueba", "ana@cuc.cr", "crear expediente",
                new IllegalStateException("password=valor-ficticio"));
        var descripcion = ArgumentCaptor.forClass(String.class);
        verify(seguridad).registrarBitacora(eq("Bearer prueba"), eq("ana@cuc.cr"), descripcion.capture());
        assertTrue(descripcion.getValue().contains("IllegalStateException"));
        assertFalse(descripcion.getValue().contains("password"));
        assertFalse(descripcion.getValue().contains("valor-ficticio"));
    }
}
