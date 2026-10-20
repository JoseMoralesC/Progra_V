package cr.ac.cuc.nuevoavatar.integracion;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

class ConfiguracionLocalTest {
    @TempDir Path directorio;

    @Test
    void encuentraArchivoEnPadresYResuelveValoresEntreComillas() throws Exception {
        Files.writeString(directorio.resolve(".env"),
            "# configuracion de prueba\nP5_TEST_PASSWORD=\"prueba#123\"\nexport P5_TEST_HOST='smtp.example.test'\n");
        Path servicio=Files.createDirectories(directorio.resolve("NuevoAvatar.Notificacion"));
        var valores=IntegracionBootstrap.cargarConfiguracionLocal(servicio);
        var environment=new StandardEnvironment();
        environment.getPropertySources().addLast(new MapPropertySource("dotenv",new HashMap<>(valores)));
        assertEquals("prueba#123",environment.resolveRequiredPlaceholders("${P5_TEST_PASSWORD}"));
        assertEquals("smtp.example.test",environment.resolveRequiredPlaceholders("${P5_TEST_HOST}"));
        environment.getPropertySources().addFirst(new MapPropertySource("override",java.util.Objects.requireNonNull(Map.of("P5_TEST_HOST","smtp.override.test"))));
        assertEquals("smtp.override.test",environment.resolveRequiredPlaceholders("${P5_TEST_HOST}"));
    }

    @Test
    void permiteArchivoLocalVacio() throws Exception {
        Files.writeString(directorio.resolve(".env"), "# sin valores\n");
        assertTrue(IntegracionBootstrap.cargarConfiguracionLocal(directorio).isEmpty());
    }

    @Test
    void incluyeContratosEnClasspath() throws Exception {
        try(var input=ConfiguracionLocalTest.class.getResourceAsStream("/contratos.json")) {
            assertNotNull(input);
            assertFalse(new com.fasterxml.jackson.databind.ObjectMapper().readTree(input).isEmpty());
        }
    }
}
