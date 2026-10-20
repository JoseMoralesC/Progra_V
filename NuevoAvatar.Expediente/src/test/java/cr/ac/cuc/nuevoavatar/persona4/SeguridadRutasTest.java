package cr.ac.cuc.nuevoavatar.persona4;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
class SeguridadRutasTest extends PruebaRemotaBase {
    @Autowired MockMvc mvc;
    @ParameterizedTest
    @ValueSource(strings={"/expediente","/expediente/prueba"})
    void rechazaRutasSinToken(String ruta) throws Exception {
        mvc.perform(get(java.util.Objects.requireNonNull(ruta))).andExpect(status().isUnauthorized());
        mvc.perform(post("/expediente")).andExpect(status().isUnauthorized());
        mvc.perform(put("/expediente/prueba")).andExpect(status().isUnauthorized());
        mvc.perform(delete("/expediente/prueba")).andExpect(status().isUnauthorized());
    }
}
