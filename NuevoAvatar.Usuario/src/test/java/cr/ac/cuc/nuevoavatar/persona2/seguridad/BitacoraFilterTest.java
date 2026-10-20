package cr.ac.cuc.nuevoavatar.persona2.seguridad;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BitacoraFilterTest {
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
