package cr.ac.cuc.nuevoavatar.persona4.academico;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/sql/datos-h2-v3.sql", "/sql/datos-mat1-h2.sql", "/sql/datos-aca2-h2.sql"})
class ListadoEstudiantesApiTest {
    private static final String TOKEN = "Bearer e30." + Base64.getUrlEncoder().withoutPadding()
            .encodeToString("{\"sub\":\"operador@cuc.cr\"}".getBytes(StandardCharsets.UTF_8)) + ".firma";
    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @Autowired private JdbcTemplate jdbc;
    @MockitoBean private SeguridadClient seguridad;
    @MockitoBean private MatriculaNotasClient matriculas;
    @MockitoSpyBean private ListadoEstudiantesRepository repositorio;

    @BeforeEach
    void preparar() {
        when(seguridad.validar(TOKEN)).thenReturn(true);
        when(matriculas.obtenerMatriculas(TOKEN, 10, 100)).thenReturn(List.of(matricula(1, "123", 10, 100, 20, "ACTIVA")));
        when(matriculas.obtenerMatriculas(TOKEN, 11, 101)).thenReturn(List.of(matricula(2, "123", 11, 101, 20, "ACTIVA"),
                matricula(3, "456", 11, 101, 20, "ACTIVA")));
        when(matriculas.obtenerMatriculas(TOKEN, 13, 102)).thenReturn(List.of(matricula(4, "456", 13, 102, 21, "ACTIVA")));
    }

    private MatriculaNotasClient.Matricula matricula(int id, String estudiante, int curso, int grupo, int periodo, String estado) {
        return new MatriculaNotasClient.Matricula(id, estudiante, curso, grupo, periodo, estado, null);
    }

    @Test
    void entregaSoloCamposDelPdfYConservaCadaCursoDelEstudiante() throws Exception {
        String contenido = mvc.perform(get("/listadoestudiantes").param("periodoId", "20").header("Authorization", TOKEN))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].tipoIdentificacion").value("Cedula"))
                .andExpect(jsonPath("$[0].identificacion").value("123"))
                .andExpect(jsonPath("$[0].nombreCompleto").value("Ana Maria"))
                .andExpect(jsonPath("$[0].carrera").value("Carrera de prueba"))
                .andExpect(jsonPath("$[0].curso").value("Curso primero"))
                .andExpect(jsonPath("$[0].grupo").value(7)).andReturn().getResponse().getContentAsString();
        for (var fila : mapper.readTree(contenido)) assertEquals(6, fila.size());
        verify(matriculas).obtenerMatriculas(TOKEN, 10, 100);
        verify(matriculas).obtenerMatriculas(TOKEN, 11, 101);
        verify(matriculas, never()).obtenerMatriculas(TOKEN, 13, 102);
        verify(seguridad).registrarBitacora(TOKEN, "operador@cuc.cr", "El usuario consulta listado de estudiantes");
        assertEquals(2, jdbc.queryForObject("SELECT COUNT(*) FROM matricula.Estudiante", Integer.class));
    }

    @Test
    void otroPeriodoConsultaSoloSusGrupos() throws Exception {
        mvc.perform(get("/listadoestudiantes").param("periodoId", "21").header("Authorization", TOKEN))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].carrera").value("Segunda carrera"))
                .andExpect(jsonPath("$[0].grupo").value(9));
        verify(matriculas, never()).obtenerMatriculas(TOKEN, 10, 100);
    }

    @Test
    void noIncluyeAnuladasNiDatosDeOtroCursoGrupoOPeriodo() throws Exception {
        when(matriculas.obtenerMatriculas(TOKEN, 10, 100)).thenReturn(List.of(
                matricula(1, "123", 10, 100, 20, "ACTIVA"), matricula(2, "456", 10, 100, 20, "ANULADA"),
                matricula(3, "456", 10, 100, 21, "ACTIVA"), matricula(4, "456", 11, 100, 20, "ACTIVA"),
                matricula(5, "456", 10, 101, 20, "ACTIVA")));
        when(matriculas.obtenerMatriculas(TOKEN, 11, 101)).thenReturn(List.of());
        mvc.perform(get("/listadoestudiantes").param("periodoId", "20").header("Authorization", TOKEN))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void periodoSinGruposDevuelveListaVaciaYRegistraConsulta() throws Exception {
        mvc.perform(get("/listadoestudiantes").param("periodoId", "999").header("Authorization", TOKEN))
                .andExpect(status().isOk()).andExpect(content().json("[]"));
        verifyNoInteractions(matriculas);
        verify(seguridad).registrarBitacora(TOKEN, "operador@cuc.cr", "El usuario consulta listado de estudiantes");
    }

    @Test
    void gruposSinMatriculadosDevuelvenListaVacia() throws Exception {
        when(matriculas.obtenerMatriculas(TOKEN, 10, 100)).thenReturn(List.of());
        when(matriculas.obtenerMatriculas(TOKEN, 11, 101)).thenReturn(List.of());
        mvc.perform(get("/listadoestudiantes").param("periodoId", "20").header("Authorization", TOKEN))
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void exigePeriodoConFormatoEntero() throws Exception {
        mvc.perform(get("/listadoestudiantes").header("Authorization", TOKEN)).andExpect(status().isBadRequest());
        mvc.perform(get("/listadoestudiantes").param("periodoId", "texto").header("Authorization", TOKEN))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(matriculas);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ausente", "invalido", "vencido"})
    void requiereAutorizacion(String tipo) throws Exception {
        var request = get("/listadoestudiantes").param("periodoId", "20");
        if (!tipo.equals("ausente")) request.header("Authorization", "Bearer " + tipo);
        mvc.perform(request).andExpect(status().isUnauthorized());
        verifyNoInteractions(matriculas);
    }

    @Test
    void falloDeJoseNoSeDevuelveComoUnListadoParcial() throws Exception {
        when(matriculas.obtenerMatriculas(TOKEN, 11, 101))
                .thenThrow(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Matricula no disponible"));
        mvc.perform(get("/listadoestudiantes").param("periodoId", "20").header("Authorization", TOKEN))
                .andExpect(status().isServiceUnavailable());
        verify(seguridad).registrarBitacora(eq(TOKEN), eq("operador@cuc.cr"), contains("Error tecnico en ACA2 GET"));
    }

    @Test
    void expedienteAusenteEsUnErrorDeIntegracion() throws Exception {
        when(matriculas.obtenerMatriculas(TOKEN, 10, 100)).thenReturn(List.of(matricula(1, "inexistente", 10, 100, 20, "ACTIVA")));
        mvc.perform(get("/listadoestudiantes").param("periodoId", "20").header("Authorization", TOKEN))
                .andExpect(status().isInternalServerError());
        verify(seguridad).registrarBitacora(eq(TOKEN), eq("operador@cuc.cr"), contains("Error tecnico en ACA2 GET"));
    }

    @Test
    void respuestaIncompletaDeMatriculaNoSeConfundeConCeroEstudiantes() throws Exception {
        when(matriculas.obtenerMatriculas(TOKEN, 10, 100))
                .thenReturn(List.of(new MatriculaNotasClient.Matricula(1, "123", 10, 100, null, "ACTIVA", null)));
        mvc.perform(get("/listadoestudiantes").param("periodoId", "20").header("Authorization", TOKEN))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    void errorSqlSeRegistraSinExponerDetalles() throws Exception {
        doThrow(new DataAccessResourceFailureException("password=ficticio")).when(repositorio).obtenerGrupos(20);
        mvc.perform(get("/listadoestudiantes").param("periodoId", "20").header("Authorization", TOKEN))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("No fue posible completar la operacion"));
        verify(seguridad).registrarBitacora(eq(TOKEN), eq("operador@cuc.cr"),
                argThat(texto -> texto.contains("ACA2") && !texto.contains("password")));
    }

    @Test
    void falloDeBitacoraNoSeOculta() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "GEN1 no disponible"))
                .when(seguridad).registrarBitacora(anyString(), anyString(), anyString());
        mvc.perform(get("/listadoestudiantes").param("periodoId", "20").header("Authorization", TOKEN))
                .andExpect(status().isServiceUnavailable());
    }
}
