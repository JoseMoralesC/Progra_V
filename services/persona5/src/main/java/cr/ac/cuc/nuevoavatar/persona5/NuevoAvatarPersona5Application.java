package cr.ac.cuc.nuevoavatar.persona5;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NuevoAvatarPersona5Application {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(NuevoAvatarPersona5Application.class);
        application.setDefaultProperties(cargarConfiguracionLocal(Path.of("")));
        application.run(args);
    }

    static Map<String, Object> cargarConfiguracionLocal(Path directorio) {
        Path actual = directorio.toAbsolutePath().normalize();
        while (actual != null) {
            if (Files.isRegularFile(actual.resolve(".env"))) {
                Map<String, Object> configuracion = new LinkedHashMap<>();
                Dotenv.configure().directory(actual.toString()).load().entries()
                    .forEach(entry -> configuracion.put(entry.getKey(), entry.getValue()));
                return configuracion;
            }
            actual = actual.getParent();
        }
        return Map.of();
    }
}
