package cr.ac.cuc.nuevoavatar.persona4.academico;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:historial;MODE=MSSQLServer;DB_CLOSE_DELAY=-1",
    "spring.sql.init.schema-locations=classpath:sql/esquema-h2-v4.sql"
})
@AutoConfigureMockMvc
@Sql({"/sql/datos-h2-v3.sql", "/sql/datos-mat1-h2.sql", "/sql/datos-aca1-h2.sql"})
class HistorialAcademicoApiTest {
    private static final String TOKEN = "Bearer e30." + Base64.getUrlEncoder().withoutPadding()
            .encodeToString("{\"sub\":\"operador@cuc.cr\"}".getBytes(StandardCharsets.UTF_8)) + ".firma";
    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @Autowired private JdbcTemplate jdbc;
    @MockitoBean private SeguridadClient seguridad;
    @MockitoBean private MatriculaNotasClient notas;
    @MockitoSpyBean private HistorialAcademicoRepository repositorio;

    @BeforeEach
    void preparar() {
        when(seguridad.validar(TOKEN)).thenReturn(true);
        when(notas.obtenerNotas(TOKEN, "123", 10, 200)).thenReturn(List.of(nota(1, 1, "20", "100")));
        when(notas.obtenerNotas(TOKEN, "123", 10, 201)).thenReturn(List.of(nota(2, 2, "100", "50")));
        when(notas.obtenerNotas(TOKEN, "123", 11, 202)).thenReturn(List.of());
    }

    private MatriculaNotasClient.Nota nota(int matricula, int rubro, String porcentaje, String valor) {
        return new MatriculaNotasClient.Nota(rubro, matricula, rubro, "Rubro",
                porcentaje == null ? null : new BigDecimal(porcentaje),
                valor == null ? null : new BigDecimal(valor));
    }

    private MockHttpServletRequestBuilder consulta() {
        return get("/historialacademico").param("tipoIdentificacion", "Cedula")
                .param("identificacion", "123").header("Authorization", TOKEN);
    }

    @Test
    void conservaIntentosYDevuelveSoloTresCamposDelPdf() throws Exception {
        String body = mvc.perform(consulta()).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].codigoCurso").value(10))
                .andExpect(jsonPath("$[0].nombreCurso").value("Curso primero"))
                .andExpect(jsonPath("$[0].promedio").value(20))
                .andExpect(jsonPath("$[1].codigoCurso").value(10))
                .andExpect(jsonPath("$[1].promedio").value(50))
                .andExpect(jsonPath("$[2].promedio").value(0))
                .andReturn().getResponse().getContentAsString();
        for (var row : mapper.readTree(body)) assertEquals(3, row.size());
        verify(seguridad).registrarBitacora(TOKEN, "operador@cuc.cr", "El usuario consulta historial academico");
        verify(notas, times(3)).obtenerNotas(eq(TOKEN), eq("123"), anyInt(), anyInt());
        verify(notas, never()).obtenerNotas(eq(TOKEN), eq("123"), eq(12), anyInt());
    }

    @Test
    void ochentaEnRubroDeVeinteAportaDieciseis() throws Exception {
        when(notas.obtenerNotas(TOKEN, "123", 10, 200)).thenReturn(List.of(nota(1, 1, "20", "80")));
        mvc.perform(consulta()).andExpect(status().isOk()).andExpect(jsonPath("$[0].promedio").value(16));
    }

    @Test
    void sumaPonderadaConDecimalesSinNormalizarNiRedondearRubros() throws Exception {
        when(notas.obtenerNotas(TOKEN, "123", 10, 200)).thenReturn(List.of(
                nota(1, 1, "12.5", "83.33"), nota(1, 2, "37.5", "90.5")));
        mvc.perform(consulta()).andExpect(status().isOk()).andExpect(jsonPath("$[0].promedio").value(44.35375));
    }

    @Test
    void cincoRubrosCompletosConCienDanCien() throws Exception {
        when(notas.obtenerNotas(TOKEN, "123", 10, 200)).thenReturn(List.of(
                nota(1, 1, "20", "100"), nota(1, 2, "20", "100"), nota(1, 3, "20", "100"),
                nota(1, 4, "20", "100"), nota(1, 5, "20", "100")));
        mvc.perform(consulta()).andExpect(status().isOk()).andExpect(jsonPath("$[0].promedio").value(100));
    }

    @Test
    void estudianteSinMatriculasDevuelveListaVacia() throws Exception {
        jdbc.update("DELETE FROM matricula.Matricula WHERE IdentificacionEstudiante = '123'");
        mvc.perform(consulta()).andExpect(status().isOk()).andExpect(content().json("[]"));
        verifyNoInteractions(notas);
    }

    @Test
    void noMezclaLasMatriculasDeOtroEstudiante() throws Exception {
        when(notas.obtenerNotas(TOKEN, "456", 10, 200)).thenReturn(List.of(nota(4, 1, "100", "75")));
        mvc.perform(get("/historialacademico").param("tipoIdentificacion", "Cedula")
                .param("identificacion", "456").header("Authorization", TOKEN))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].promedio").value(75));
    }

    @Test
    void requiereTipoYNumeroNoVacios() throws Exception {
        for (String parametro : List.of("tipoIdentificacion", "identificacion")) {
            String otro = parametro.equals("identificacion") ? "tipoIdentificacion" : "identificacion";
            String valor = otro.equals("identificacion") ? "123" : "Cedula";
            mvc.perform(get("/historialacademico").param(otro, valor).header("Authorization", TOKEN))
                    .andExpect(status().isBadRequest());
            mvc.perform(get("/historialacademico").param(otro, valor).param(parametro, "   ")
                    .header("Authorization", TOKEN)).andExpect(status().isBadRequest());
        }
        verifyNoInteractions(notas);
    }

    @Test
    void tipoIncorrectoOIdentificacionInexistenteNoDevuelvenNotas() throws Exception {
        mvc.perform(get("/historialacademico").param("tipoIdentificacion", "Pasaporte")
                .param("identificacion", "123").header("Authorization", TOKEN)).andExpect(status().isNotFound());
        mvc.perform(get("/historialacademico").param("tipoIdentificacion", "Cedula")
                .param("identificacion", "123' OR '1'='1").header("Authorization", TOKEN)).andExpect(status().isNotFound());
        verifyNoInteractions(notas);
    }

    @Test
    void conservaCerosInicialesYRecortaEspacios() throws Exception {
        jdbc.update("UPDATE matricula.Estudiante SET Identificacion = '00123' WHERE Identificacion = '123'");
        when(notas.obtenerNotas(TOKEN, "00123", 10, 200)).thenReturn(List.of(nota(1, 1, "20", "100")));
        mvc.perform(get("/historialacademico").param("tipoIdentificacion", " Cedula ")
                .param("identificacion", " 00123 ").header("Authorization", TOKEN))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].promedio").value(20));
        verify(notas).obtenerNotas(TOKEN, "00123", 10, 200);
    }

    @Test
    void requiereTokenValido() throws Exception {
        mvc.perform(get("/historialacademico")).andExpect(status().isUnauthorized());
        when(seguridad.validar(TOKEN)).thenReturn(false);
        mvc.perform(consulta()).andExpect(status().isUnauthorized());
        verifyNoInteractions(notas);
    }

    @ParameterizedTest
    @ValueSource(strings = {"otraMatricula", "notaNula", "porcentajeNulo", "duplicado", "fueraRango", "sumaExcesiva", "filaNula"})
    void noPublicaPromediosConNotasInconsistentes(String caso) throws Exception {
        List<MatriculaNotasClient.Nota> filas = switch (caso) {
            case "otraMatricula" -> List.of(nota(4, 1, "20", "100"));
            case "notaNula" -> List.of(nota(1, 1, "20", null));
            case "porcentajeNulo" -> List.of(nota(1, 1, null, "100"));
            case "duplicado" -> List.of(nota(1, 1, "20", "100"), nota(1, 1, "20", "100"));
            case "fueraRango" -> List.of(nota(1, 1, "20", "101"));
            case "sumaExcesiva" -> List.of(nota(1, 1, "60", "100"), nota(1, 2, "60", "100"));
            default -> Arrays.asList((MatriculaNotasClient.Nota) null);
        };
        when(notas.obtenerNotas(TOKEN, "123", 10, 200)).thenReturn(filas);
        mvc.perform(consulta()).andExpect(status().isServiceUnavailable());
    }

    @Test
    void matriculasAmbiguasDelMismoGrupoNoCompartenNotas() throws Exception {
        jdbc.update("UPDATE matricula.Matricula SET GrupoId = 200 WHERE MatriculaId = 2");
        mvc.perform(consulta()).andExpect(status().isServiceUnavailable());
        verifyNoInteractions(notas);
    }

    @Test
    void falloDeNotasNoDevuelveHistorialParcial() throws Exception {
        when(notas.obtenerNotas(TOKEN, "123", 10, 201)).thenThrow(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE));
        mvc.perform(consulta()).andExpect(status().isServiceUnavailable());
        verify(seguridad, never()).registrarBitacora(anyString(), anyString(), eq("El usuario consulta historial academico"));
    }

    @Test
    void falloDeBitacoraSeInforma() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE)).when(seguridad)
                .registrarBitacora(TOKEN, "operador@cuc.cr", "El usuario consulta historial academico");
        mvc.perform(consulta()).andExpect(status().isServiceUnavailable());
    }

    @Test
    void errorSqlSeRegistraComoAca1SinExponerDetalles() throws Exception {
        doThrow(new DataAccessResourceFailureException("detalle interno SQL")).when(repositorio).obtenerIntentos("123");
        mvc.perform(consulta()).andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("No fue posible completar la operacion"));
        verify(seguridad).registrarBitacora(eq(TOKEN), eq("operador@cuc.cr"), contains("ACA1"));
    }
}
