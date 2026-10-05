package cr.ac.cuc.nuevoavatar.persona4;

import java.util.Base64;
import java.nio.charset.StandardCharsets;
import cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClient;
import cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;



import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

class SeguridadRutasTest {
    @Autowired private MockMvc mvc;
    @MockitoBean private SeguridadClient seguridad;
    @MockitoBean private ExpedienteService expedientes;

    @ParameterizedTest
    @ValueSource(strings = {"/expediente", "/expediente/prueba", "/prematricula",
            "/prematricula/prueba", "/historialacademico", "/listadoestudiantes"})
    void aplicaAutorizacionEnRutasAsignadas(String ruta) throws Exception {
        mvc.perform(get(ruta)).andExpect(status().isUnauthorized());
        verifyNoInteractions(seguridad);
    }

    @Test
    void protegeLasCincoOperacionesDeExpediente() throws Exception {
        mvc.perform(post("/expediente")).andExpect(status().isUnauthorized());
        mvc.perform(put("/expediente/prueba")).andExpect(status().isUnauthorized());
        mvc.perform(delete("/expediente/prueba")).andExpect(status().isUnauthorized());
        mvc.perform(get("/expediente")).andExpect(status().isUnauthorized());
        mvc.perform(get("/expediente/prueba")).andExpect(status().isUnauthorized());
    }

    @Test
    void permiteRutaTrasValidacionUsr5() throws Exception {
        String token = "Bearer e30." + Base64.getUrlEncoder().withoutPadding()
                .encodeToString("{\"sub\":\"ana@cuc.cr\"}".getBytes(StandardCharsets.UTF_8)) + ".firma";
        when(seguridad.validar(token)).thenReturn(true);
        mvc.perform(get("/expediente").header("Authorization", token))
                .andExpect(status().isOk()).andExpect(content().json("[]"));
        verify(seguridad).validar(token);
    }
}
