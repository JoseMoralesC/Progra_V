package cr.ac.cuc.nuevoavatar.persona2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties={
    "spring.datasource.url=jdbc:h2:mem:usuario-contexto;MODE=MSSQLServer;INIT=CREATE SCHEMA IF NOT EXISTS seguridad",
    "spring.datasource.username=sa","spring.datasource.password=",
    "spring.datasource.driver-class-name=org.h2.Driver","spring.jpa.hibernate.ddl-auto=create-drop",
    "SERVICES_INTERNAL_KEY=clave-ficticia-solo-para-pruebas-locales"
})
class NuevoAvatarPersona2ApplicationTests {
    @MockitoBean(name="verificarBase") InitializingBean verificarBase;
    @Autowired ApplicationContext contexto;
    @Test void contextLoads() {
        assertNotNull(contexto.getBean(cr.ac.cuc.nuevoavatar.persona2.usuario.UsuarioService.class));
        assertNotNull(contexto.getBean(cr.ac.cuc.nuevoavatar.integracion.DatosRemotos.class));
    }
}
