package cr.ac.cuc.nuevoavatar.persona4;

import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NuevoAvatarPersona4Application {
    @Bean
    public Clock reloj() {
        return Clock.systemDefaultZone();
    }

    public static void main(String[] args) {
        SpringApplication.run(NuevoAvatarPersona4Application.class, args);
    }
}
