package cr.ac.cuc.nuevoavatar.persona2;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Map;
import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
import cr.ac.cuc.nuevoavatar.integracion.IntegracionConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

class IntegracionCompartidaTest {
    @Test
    void cargaBibliotecaYContratoConSpringBootDelServicioSinBaseReal() {
        var jdbc=mock(JdbcTemplate.class);
        when(jdbc.queryForObject("SELECT DB_NAME()",String.class)).thenReturn("PrograV_Usuario");
        try(var context=new AnnotationConfigApplicationContext()) {
            context.getEnvironment().getPropertySources().addFirst(new MapPropertySource("prueba",Map.of(
                "integration.service","Usuario",
                "SERVICE_DB_NAME","PrograV_Usuario",
                "SERVICES_INTERNAL_KEY","clave-ficticia-solo-para-pruebas-locales")));
            context.registerBean(JdbcTemplate.class,()->jdbc);
            context.register(IntegracionConfiguration.class);
            context.refresh();
            assertNotNull(context.getBean(DatosRemotos.class).contrato("Usuario"));
            verify(jdbc).queryForObject("SELECT DB_NAME()",String.class);
        }
    }
}
