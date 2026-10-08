package cr.ac.cuc.nuevoavatar.persona5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

class ConfiguracionLocalTest {

    @TempDir
    Path directorio;

    @Test
    void encuentraArchivoEnPadresYResuelveValoresEntreComillas() throws Exception {
        Files.writeString(directorio.resolve(".env"),
            "P5_TEST_PASSWORD=\"prueba#123\"\nP5_TEST_HOST=smtp.gmail.com\n");
        Path servicio = Files.createDirectories(directorio.resolve("services/persona5"));
        Map<String, Object> valores = NuevoAvatarPersona5Application.cargarConfiguracionLocal(servicio);
        StandardEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().addLast(new MapPropertySource("dotenv", valores));

        assertEquals("prueba#123", environment.resolveRequiredPlaceholders("${P5_TEST_PASSWORD}"));
        assertEquals("smtp.gmail.com", environment.resolveRequiredPlaceholders("${P5_TEST_HOST}"));
        environment.getPropertySources().addFirst(new MapPropertySource("override",
            Map.of("P5_TEST_HOST", "smtp.override.test")));
        assertEquals("smtp.override.test", environment.resolveRequiredPlaceholders("${P5_TEST_HOST}"));
    }

    @Test
    void permiteArrancarSinArchivoLocal() {
        assertTrue(NuevoAvatarPersona5Application.cargarConfiguracionLocal(
            directorio.getRoot()).isEmpty());
    }
}
