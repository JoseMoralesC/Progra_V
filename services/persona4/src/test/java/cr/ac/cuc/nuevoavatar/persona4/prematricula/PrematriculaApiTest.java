package cr.ac.cuc.nuevoavatar.persona4.prematricula;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/sql/datos-h2-v3.sql", "/sql/datos-mat1-h2.sql"})
class PrematriculaApiTest {
    private static final String TOKEN = "Bearer e30." + Base64.getUrlEncoder().withoutPadding()
            .encodeToString("{\"sub\":\"operador@cuc.cr\"}".getBytes(StandardCharsets.UTF_8)) + ".firma";
    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @Autowired private JdbcTemplate jdbc;
    @MockitoBean private SeguridadClient seguridad;
    @MockitoBean private Clock reloj;
    @MockitoSpyBean private PrematriculaRepository repositorio;

    @BeforeEach
    void preparar() {
        when(seguridad.validar(TOKEN)).thenReturn(true);
        when(reloj.instant()).thenReturn(Instant.parse("2026-10-03T18:00:00Z"));
        when(reloj.getZone()).thenReturn(ZoneId.of("America/Costa_Rica"));
    }

    private ObjectNode datos() throws Exception {
        return (ObjectNode) mapper.readTree("""
                {"identificacionEstudiante":"123", "carreraId":1, "cursos":[10,11],
                 "observaciones":"Desea ingresar", "periodoId":20}
                """);
    }

    private MockHttpServletRequestBuilder conDatos(MockHttpServletRequestBuilder solicitud, ObjectNode datos) {
        return solicitud.header("Authorization", TOKEN).contentType(MediaType.APPLICATION_JSON).content(datos.toString());
    }

    private int crear() throws Exception {
        var respuesta = mvc.perform(conDatos(post("/prematricula"), datos())).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return mapper.readTree(respuesta).get("id").asInt();
    }

    private int cantidad(String tabla) {
        return jdbc.queryForObject("SELECT COUNT(*) FROM " + tabla, Integer.class);
    }

    @Test
    void cincoOperacionesConDatosPersistidosYBitacoras() throws Exception {
        var respuesta = mvc.perform(conDatos(post("/prematricula"), datos())).andExpect(status().isCreated())
                .andExpect(jsonPath("$.identificacionEstudiante").value("123"))
                .andExpect(jsonPath("$.cursos.length()").value(2)).andReturn().getResponse();
        int id = mapper.readTree(respuesta.getContentAsString()).get("id").asInt();
        assertEquals("http://localhost/prematricula/" + id, respuesta.getHeader("Location"));
        mvc.perform(get("/prematricula/{id}", id).header("Authorization", TOKEN)).andExpect(status().isOk())
                .andExpect(jsonPath("$.periodoId").value(20));
        mvc.perform(get("/prematricula").header("Authorization", TOKEN)).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        var cambio = datos().put("observaciones", "Observacion corregida");
        cambio.putArray("cursos").add(11);
        mvc.perform(conDatos(put("/prematricula/{id}", id), cambio)).andExpect(status().isOk())
                .andExpect(jsonPath("$.cursos.length()").value(1)).andExpect(jsonPath("$.cursos[0]").value(11));
        mvc.perform(delete("/prematricula/{id}", id).header("Authorization", TOKEN)).andExpect(status().isNoContent());
        assertEquals(0, cantidad("matricula.Prematricula"));
        assertEquals(0, cantidad("matricula.PrematriculaCurso"));
        assertEquals(4, cantidad("academico.Curso"));
        assertEquals(2, cantidad("matricula.Estudiante"));
        mvc.perform(get("/prematricula/{id}", id).header("Authorization", TOKEN)).andExpect(status().isNotFound());

        var captor = ArgumentCaptor.forClass(String.class);
        verify(seguridad, times(5)).registrarBitacora(eq(TOKEN), eq("operador@cuc.cr"), captor.capture());
        List<String> logs = captor.getAllValues();
        assertTrue(logs.get(0).contains("Creacion de prematricula: {"));
        assertEquals("El usuario consulta prematricula", logs.get(1));
        assertEquals("El usuario consulta prematriculas", logs.get(2));
        assertTrue(logs.get(3).contains("Anterior: {"));
        assertTrue(logs.get(3).contains("Desea ingresar"));
        assertTrue(logs.get(3).contains("Actual: {"));
        assertTrue(logs.get(3).contains("Observacion corregida"));
        assertTrue(logs.get(4).contains("Eliminacion de prematricula: {"));
        assertTrue(logs.get(4).contains("\"cursos\":[11]"));
        verify(seguridad, times(6)).validar(TOKEN);
    }

    @ParameterizedTest
    @ValueSource(strings = {"identificacionEstudiante", "carreraId", "cursos", "periodoId"})
    void datosRequeridosEnCrearYModificar(String campo) throws Exception {
        int id = crear();
        var incompleto = datos();
        incompleto.remove(campo);
        mvc.perform(conDatos(post("/prematricula"), incompleto)).andExpect(status().isBadRequest());
        mvc.perform(conDatos(put("/prematricula/{id}", id), incompleto)).andExpect(status().isBadRequest());
        assertEquals(1, cantidad("matricula.Prematricula"));
        assertEquals(2, cantidad("matricula.PrematriculaCurso"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t"})
    void identificacionSinContenidoNoSeAcepta(String identificacion) throws Exception {
        int id = crear();
        var invalido = datos().put("identificacionEstudiante", identificacion);
        mvc.perform(conDatos(post("/prematricula"), invalido)).andExpect(status().isBadRequest());
        mvc.perform(conDatos(put("/prematricula/{id}", id), invalido)).andExpect(status().isBadRequest());
    }

    @Test
    void listaVaciaOCursoNuloNoSeAceptan() throws Exception {
        int id = crear();
        var vacio = datos();
        vacio.putArray("cursos");
        var nulo = datos();
        nulo.putArray("cursos").add(10).addNull();
        for (var invalido : List.of(vacio, nulo)) {
            mvc.perform(conDatos(post("/prematricula"), invalido)).andExpect(status().isBadRequest());
            mvc.perform(conDatos(put("/prematricula/{id}", id), invalido)).andExpect(status().isBadRequest());
        }
        assertEquals(2, cantidad("matricula.PrematriculaCurso"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "Texto libre con numeros 123 y simbolos."})
    void observacionesOpcionalesEnCrearYModificar(String observaciones) throws Exception {
        var cuerpo = datos().put("observaciones", observaciones);
        var respuesta = mvc.perform(conDatos(post("/prematricula"), cuerpo)).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int id = mapper.readTree(respuesta).get("id").asInt();
        cuerpo.remove("observaciones");
        mvc.perform(conDatos(put("/prematricula/{id}", id), cuerpo)).andExpect(status().isOk());
        assertNull(jdbc.queryForObject("SELECT Observaciones FROM matricula.Prematricula WHERE PrematriculaId=?", String.class, id));
    }

    @Test
    void observacionesPuedenOmitirseAlCrear() throws Exception {
        var cuerpo = datos();
        cuerpo.remove("observaciones");
        mvc.perform(conDatos(post("/prematricula"), cuerpo)).andExpect(status().isCreated());
    }

    @Test
    void referenciasInexistentesNoGuardanDatos() throws Exception {
        int id = crear();
        var cursoAusente = datos();
        cursoAusente.putArray("cursos").add(999);
        for (var invalido : List.of(datos().put("identificacionEstudiante", "ausente"),
                datos().put("carreraId", 999), datos().put("periodoId", 999), cursoAusente)) {
            mvc.perform(conDatos(post("/prematricula"), invalido)).andExpect(status().isBadRequest());
            mvc.perform(conDatos(put("/prematricula/{id}", id), invalido)).andExpect(status().isBadRequest());
        }
        assertEquals(1, cantidad("matricula.Prematricula"));
        assertEquals(2, cantidad("matricula.PrematriculaCurso"));
    }

    @Test
    void compruebaTodosLosNivelesDesdeLaBase() throws Exception {
        int id = crear();
        var invalido = datos().put("nivel", 1);
        invalido.putArray("cursos").add(10).add(12);
        mvc.perform(conDatos(post("/prematricula"), invalido)).andExpect(status().isBadRequest());
        mvc.perform(conDatos(put("/prematricula/{id}", id), invalido)).andExpect(status().isBadRequest());
        assertEquals(2, cantidad("matricula.PrematriculaCurso"));
    }

    @ParameterizedTest
    @ValueSource(ints = {21, 22})
    void rechazaPeriodoQueIniciaHoyOPasadoAunqueSolicitanteEnvieOtraFecha(int periodo) throws Exception {
        int id = crear();
        var invalido = datos().put("periodoId", periodo).put("fechaInicio", "2099-01-01");
        mvc.perform(conDatos(post("/prematricula"), invalido)).andExpect(status().isBadRequest());
        mvc.perform(conDatos(put("/prematricula/{id}", id), invalido)).andExpect(status().isBadRequest());
    }

    @Test
    void reevaluaLaFechaParaCadaEscrituraYPermiteConsultarOEliminar() throws Exception {
        int id = crear();
        when(reloj.instant()).thenReturn(Instant.parse("2026-10-04T18:00:00Z"));
        mvc.perform(conDatos(post("/prematricula"), datos())).andExpect(status().isBadRequest());
        mvc.perform(conDatos(put("/prematricula/{id}", id), datos())).andExpect(status().isBadRequest());
        mvc.perform(get("/prematricula/{id}", id).header("Authorization", TOKEN)).andExpect(status().isOk());
        mvc.perform(delete("/prematricula/{id}", id).header("Authorization", TOKEN)).andExpect(status().isNoContent());
    }

    @Test
    void noImponeUnicidadNoSolicitadaEntreEstudianteYPeriodo() throws Exception {
        int primero = crear();
        int segundo = crear();
        assertNotEquals(primero, segundo);
        assertEquals(2, cantidad("matricula.Prematricula"));
    }

    @Test
    void modificarPermiteCambiarEstudianteYCarrera() throws Exception {
        int id = crear();
        var cambio = datos().put("identificacionEstudiante", "456").put("carreraId", 2);
        cambio.putArray("cursos").add(13);
        mvc.perform(conDatos(put("/prematricula/{id}", id), cambio)).andExpect(status().isOk())
                .andExpect(jsonPath("$.identificacionEstudiante").value("456"))
                .andExpect(jsonPath("$.carreraId").value(2));
    }

    @Test
    void correccionDeNumeroDeExpedienteConservaLaPrematricula() throws Exception {
        int id = crear();
        jdbc.update("UPDATE matricula.Estudiante SET Identificacion='789' WHERE Identificacion='123'");
        mvc.perform(get("/prematricula/{id}", id).header("Authorization", TOKEN)).andExpect(status().isOk())
                .andExpect(jsonPath("$.identificacionEstudiante").value("789"));
        mvc.perform(delete("/expediente/789").header("Authorization", TOKEN)).andExpect(status().isConflict());
        assertEquals(1, cantidad("matricula.Prematricula"));
    }

    static Stream<Arguments> sinAutorizacion() {
        return Stream.of(new String[]{"POST", "/prematricula"}, new String[]{"PUT", "/prematricula/1"},
                new String[]{"DELETE", "/prematricula/1"}, new String[]{"GET", "/prematricula"},
                new String[]{"GET", "/prematricula/1"}).flatMap(op -> Stream.of("ausente", "invalido", "vencido")
                        .map(tipo -> Arguments.of(op[0], op[1], tipo)));
    }

    @ParameterizedTest
    @MethodSource("sinAutorizacion")
    void protegeLasCincoOperaciones(String metodo, String ruta, String tipo) throws Exception {
        crear();
        clearInvocations(seguridad);
        var solicitud = request(HttpMethod.valueOf(metodo), ruta).contentType(MediaType.APPLICATION_JSON).content(datos().toString());
        if (!tipo.equals("ausente")) solicitud.header("Authorization", "Bearer " + tipo);
        mvc.perform(solicitud).andExpect(status().isUnauthorized());
        assertEquals(1, cantidad("matricula.Prematricula"));
        verify(seguridad, never()).registrarBitacora(anyString(), anyString(), anyString());
    }

    @Test
    void falloGen1RevierteCreacionYCursos() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "GEN1 no disponible"))
                .when(seguridad).registrarBitacora(anyString(), anyString(), anyString());
        mvc.perform(conDatos(post("/prematricula"), datos())).andExpect(status().isServiceUnavailable());
        assertEquals(0, cantidad("matricula.Prematricula"));
        assertEquals(0, cantidad("matricula.PrematriculaCurso"));
    }

    @Test
    void falloGen1RevierteModificacionYEliminacion() throws Exception {
        int id = crear();
        doThrow(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "GEN1 no disponible"))
                .when(seguridad).registrarBitacora(anyString(), anyString(), anyString());
        var cambio = datos().put("observaciones", "No debe quedar guardado");
        cambio.putArray("cursos").add(11);
        mvc.perform(conDatos(put("/prematricula/{id}", id), cambio)).andExpect(status().isServiceUnavailable());
        mvc.perform(delete("/prematricula/{id}", id).header("Authorization", TOKEN)).andExpect(status().isServiceUnavailable());
        assertEquals("Desea ingresar", jdbc.queryForObject("SELECT Observaciones FROM matricula.Prematricula WHERE PrematriculaId=?", String.class, id));
        assertEquals(2, cantidad("matricula.PrematriculaCurso"));
    }

    @Test
    void errorDespuesDeEscribirRevierteYSeRegistraComoMat1() throws Exception {
        doAnswer(invocacion -> {
            invocacion.callRealMethod();
            throw new DataAccessResourceFailureException("password=ficticio");
        }).when(repositorio).crear(any());
        mvc.perform(conDatos(post("/prematricula"), datos())).andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("No fue posible completar la operacion"));
        assertEquals(0, cantidad("matricula.Prematricula"));
        assertEquals(0, cantidad("matricula.PrematriculaCurso"));
        verify(seguridad).registrarBitacora(eq(TOKEN), eq("operador@cuc.cr"),
                argThat(texto -> texto.contains("Error tecnico en MAT1 POST") && !texto.contains("password")));
    }

    @Test
    void recursoAusenteIdInvalidoYMetodoNoAdmitidoTienenRespuestasRest() throws Exception {
        mvc.perform(get("/prematricula/999").header("Authorization", TOKEN)).andExpect(status().isNotFound());
        mvc.perform(conDatos(put("/prematricula/999"), datos())).andExpect(status().isNotFound());
        mvc.perform(delete("/prematricula/999").header("Authorization", TOKEN)).andExpect(status().isNotFound());
        mvc.perform(get("/prematricula/no-numero").header("Authorization", TOKEN)).andExpect(status().isBadRequest());
        mvc.perform(conDatos(patch("/prematricula/1"), datos())).andExpect(status().isMethodNotAllowed());
    }
}
