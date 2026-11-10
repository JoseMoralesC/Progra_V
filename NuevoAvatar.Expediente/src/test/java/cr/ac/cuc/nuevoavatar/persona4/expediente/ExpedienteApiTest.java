package cr.ac.cuc.nuevoavatar.persona4.expediente;

import java.nio.charset.StandardCharsets;
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
@Sql("/sql/datos-h2-v3.sql")
class ExpedienteApiTest extends cr.ac.cuc.nuevoavatar.persona4.PruebaRemotaBase {
    private static final String TOKEN = "Bearer e30." + Base64.getUrlEncoder().withoutPadding()
            .encodeToString("{\"sub\":\"operador@cuc.cr\"}".getBytes(StandardCharsets.UTF_8)) + ".firma";
    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @Autowired private JdbcTemplate jdbc;
    @MockitoBean private SeguridadClient seguridad;
    @MockitoBean private DireccionClient direcciones;
    @MockitoSpyBean private ExpedienteRepository repositorio;

    @BeforeEach
    void prepararServiciosExternos() {
        when(seguridad.validar(TOKEN)).thenReturn(true);
        when(seguridad.obtenerParametro(TOKEN, "DOMESTUD")).thenReturn("cuc.cr");
    }

    private ObjectNode datos() throws Exception {
        return (ObjectNode) mapper.readTree("""
                {"identificacion":"123", "tipoIdentificacion":"Cedula", "email":"ana@cuc.cr",
                 "nombreCompleto":"Ana Maria", "fechaNacimiento":"2012-05-10", "provinciaId":1,
                 "cantonId":2, "distritoId":3, "otrasSenas":"Casa azul", "telefonos":["111","222"]}
                """);
    }

    private @org.springframework.lang.NonNull MockHttpServletRequestBuilder conDatos(MockHttpServletRequestBuilder solicitud, ObjectNode datos) {
        return solicitud.header("Authorization", TOKEN).contentType(java.util.Objects.requireNonNull(MediaType.APPLICATION_JSON)).content(java.util.Objects.requireNonNull(datos.toString()));
    }

    private void crear() throws Exception {
        mvc.perform(conDatos(post("/expediente"), datos())).andExpect(status().isCreated());
    }

    private SeguridadClient.SesionActual sesion(String email, String identificacion, String tipo, String rol) {
        return new SeguridadClient.SesionActual(email, "Ana Maria", rol, tipo, identificacion, identificacion);
    }

    private String tokenPara(String email) {
        String payload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(("{\"sub\":\"" + email + "\"}").getBytes(StandardCharsets.UTF_8));
        return "Bearer e30." + payload + ".firma";
    }

    private String tokenAna() { return tokenPara("ana@cuc.cr"); }

    private MockHttpServletRequestBuilder conDatosToken(MockHttpServletRequestBuilder solicitud, ObjectNode datos, String token) {
        return solicitud.header("Authorization", token).contentType(java.util.Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(java.util.Objects.requireNonNull(datos.toString()));
    }

    private ObjectNode cambiosPropios() throws Exception {
        return (ObjectNode) mapper.readTree("""
                {"telefonos":["777", "888"],"provinciaId":1,"cantonId":2,
                 "distritoId":3,"otrasSenas":"Casa verde"}
                """);
    }

    private int cantidad(String tabla) {
        return java.util.Objects.requireNonNull(jdbc.queryForObject("SELECT COUNT(*) FROM " + tabla, Integer.class));
    }

    @Test
    void cicloCompletoDeCincoOperacionesYBitacoras() throws Exception {
        mvc.perform(conDatos(post("/expediente"), datos())).andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/expediente/123"))
                .andExpect(jsonPath("$.identificacion").value("123"))
                .andExpect(jsonPath("$.telefonos.length()").value(2));
        mvc.perform(get("/expediente/123").header("Authorization", TOKEN)).andExpect(status().isOk())
                .andExpect(jsonPath("$.provinciaId").value(1)).andExpect(jsonPath("$.cantonId").value(2));
        mvc.perform(get("/expediente").header("Authorization", TOKEN)).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        ObjectNode cambios = datos().put("nombreCompleto", "Ana Mora");
        cambios.putArray("telefonos").add("333");
        mvc.perform(conDatos(put("/expediente/123"), cambios)).andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCompleto").value("Ana Mora"))
                .andExpect(jsonPath("$.telefonos.length()").value(1));
        mvc.perform(delete("/expediente/123").header("Authorization", TOKEN)).andExpect(status().isNoContent());
        assertEquals(0, cantidad("matricula.Estudiante"));
        assertEquals(0, cantidad("matricula.EstudianteTelefono"));
        mvc.perform(get("/expediente/123").header("Authorization", TOKEN)).andExpect(status().isNotFound());

        var mensajes = ArgumentCaptor.forClass(String.class);
        verify(seguridad, times(5)).registrarBitacora(eq(TOKEN), eq("operador@cuc.cr"), mensajes.capture());
        List<String> logs = mensajes.getAllValues();
        assertTrue(logs.get(0).contains("Creacion de expediente: {"));
        assertEquals("El usuario consulta expediente", logs.get(1));
        assertEquals("El usuario consulta expedientes", logs.get(2));
        assertTrue(logs.get(3).contains("Anterior: {"));
        assertTrue(logs.get(3).contains("Ana Maria"));
        assertTrue(logs.get(3).contains("Actual: {"));
        assertTrue(logs.get(3).contains("Ana Mora"));
        assertTrue(logs.get(4).contains("Eliminacion de expediente: {"));
        assertTrue(logs.get(4).contains("333"));
        verify(seguridad, times(6)).validar(TOKEN);
        verify(direcciones, times(2)).validar(TOKEN, 1, 2, 3);
    }

    @Test
    void noPermiteMismoNumeroAunqueCambieElTipo() throws Exception {
        crear();
        mvc.perform(conDatos(post("/expediente"), datos().put("tipoIdentificacion", "Pasaporte")))
                .andExpect(status().isConflict());
        assertEquals(1, cantidad("matricula.Estudiante"));
    }

    @Test
    void conservaCerosInicialesYNoExigeCorreoOTelefonosUnicos() throws Exception {
        crear();
        mvc.perform(conDatos(post("/expediente"), datos().put("identificacion", "00123")))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.identificacion").value("00123"));
        assertEquals(2, cantidad("matricula.Estudiante"));
    }

    @Test
    void identificacionReferenciadaNoSeCambiaEntreServicios() throws Exception {
        crear();
        jdbc.update("INSERT INTO matricula.Matricula VALUES (1, '123')");
        jdbc.update("INSERT INTO finanzas.Factura VALUES (1, 1, '123')");
        mvc.perform(conDatos(put("/expediente/123"), datos().put("identificacion", "456")))
                .andExpect(status().isConflict());
        assertEquals("123", jdbc.queryForObject("SELECT IdentificacionEstudiante FROM matricula.Matricula", String.class));
        assertEquals("123", jdbc.queryForObject("SELECT IdentificacionEstudiante FROM finanzas.Factura", String.class));
        assertEquals(2, jdbc.queryForObject("SELECT COUNT(*) FROM matricula.EstudianteTelefono WHERE IdentificacionEstudiante='123'", Integer.class));
    }

    @Test
    void noEliminaEstudianteReferenciadoNiSusTelefonos() throws Exception {
        crear();
        jdbc.update("INSERT INTO matricula.Matricula VALUES (1, '123')");
        clearInvocations(seguridad);
        mvc.perform(delete("/expediente/123").header("Authorization", TOKEN)).andExpect(status().isConflict());
        assertEquals(1, cantidad("matricula.Estudiante"));
        assertEquals(2, cantidad("matricula.EstudianteTelefono"));
        verify(seguridad, never()).registrarBitacora(anyString(), anyString(), anyString());
    }

    @Test
    void identificacionDuplicadaAlModificarNoPierdeDatos() throws Exception {
        crear();
        mvc.perform(conDatos(post("/expediente"), datos().put("identificacion", "456"))).andExpect(status().isCreated());
        mvc.perform(conDatos(put("/expediente/123"), datos().put("identificacion", "456"))).andExpect(status().isConflict());
        assertEquals(2, cantidad("matricula.Estudiante"));
        assertEquals(4, cantidad("matricula.EstudianteTelefono"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"identificacion", "tipoIdentificacion", "email", "nombreCompleto", "fechaNacimiento",
            "provinciaId", "cantonId", "distritoId", "otrasSenas", "telefonos"})
    void camposRequeridosEnCrearYModificar(String campo) throws Exception {
        ObjectNode incompleto = datos();
        incompleto.remove(campo);
        mvc.perform(conDatos(post("/expediente"), incompleto)).andExpect(status().isBadRequest());
        crear();
        mvc.perform(conDatos(put("/expediente/123"), incompleto)).andExpect(status().isBadRequest());
        assertEquals(1, cantidad("matricula.Estudiante"));
        assertEquals(2, cantidad("matricula.EstudianteTelefono"));
    }

    @Test
    void nombreCorreoDominioYFechaInvalidosSeRechazanEnCrearYModificar() throws Exception {
        crear();
        for (ObjectNode invalido : List.of(datos().put("nombreCompleto", "Ana2"), datos().put("email", "ana"),
                datos().put("email", "ana@otro.cr"), datos().put("fechaNacimiento", "no-fecha"))) {
            mvc.perform(conDatos(post("/expediente"), invalido)).andExpect(status().isBadRequest());
            mvc.perform(conDatos(put("/expediente/123"), invalido)).andExpect(status().isBadRequest());
        }
        assertEquals("Ana Maria", jdbc.queryForObject("SELECT NombreCompleto FROM matricula.Estudiante", String.class));
    }

    @Test
    void dominioCambiaMedianteParametro() throws Exception {
        when(seguridad.obtenerParametro(TOKEN, "DOMESTUD")).thenReturn("estudiantes.cr");
        mvc.perform(conDatos(post("/expediente"), datos())).andExpect(status().isBadRequest());
        mvc.perform(conDatos(post("/expediente"), datos().put("email", "ana@estudiantes.cr"))).andExpect(status().isCreated());
    }

    @Test
    void direccionInvalidaNoEscribeDatos() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Direccion invalida"))
                .when(direcciones).validar(TOKEN, 1, 2, 3);
        mvc.perform(conDatos(post("/expediente"), datos())).andExpect(status().isBadRequest());
        assertEquals(0, cantidad("matricula.Estudiante"));
    }

    static Stream<Arguments> sinAutorizacion() {
        return Stream.of(new String[]{"POST", "/expediente"}, new String[]{"PUT", "/expediente/123"},
                new String[]{"DELETE", "/expediente/123"}, new String[]{"GET", "/expediente"},
                new String[]{"GET", "/expediente/123"}).flatMap(op -> Stream.of("ausente", "invalido", "vencido")
                        .map(tipo -> Arguments.of(op[0], op[1], tipo)));
    }

    @ParameterizedTest
    @MethodSource("sinAutorizacion")
    void protegeTodasLasOperaciones(String metodo, String ruta, String tipo) throws Exception {
        crear();
        clearInvocations(seguridad);
        var solicitud = request(HttpMethod.valueOf(java.util.Objects.requireNonNull(metodo)), java.util.Objects.requireNonNull(ruta)).contentType(java.util.Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(java.util.Objects.requireNonNull(datos().toString()));
        if (!tipo.equals("ausente")) solicitud.header("Authorization", "Bearer " + tipo);
        mvc.perform(solicitud).andExpect(status().isUnauthorized());
        assertEquals(1, cantidad("matricula.Estudiante"));
        verify(seguridad, never()).registrarBitacora(anyString(), anyString(), anyString());
    }

    @Test
    void falloGen1RevierteCreacionYTelefonos() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "GEN1 no disponible"))
                .when(seguridad).registrarBitacora(anyString(), anyString(), anyString());
        mvc.perform(conDatos(post("/expediente"), datos())).andExpect(status().isServiceUnavailable());
        assertEquals(0, cantidad("matricula.Estudiante"));
        assertEquals(0, cantidad("matricula.EstudianteTelefono"));
    }

    @Test
    void falloGen1RevierteModificacionYEliminacion() throws Exception {
        crear();
        doThrow(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "GEN1 no disponible"))
                .when(seguridad).registrarBitacora(anyString(), anyString(), anyString());
        mvc.perform(conDatos(put("/expediente/123"), datos().put("nombreCompleto", "Ana Mora")))
                .andExpect(status().isServiceUnavailable());
        mvc.perform(delete("/expediente/123").header("Authorization", TOKEN)).andExpect(status().isServiceUnavailable());
        assertEquals("Ana Maria", jdbc.queryForObject("SELECT NombreCompleto FROM matricula.Estudiante", String.class));
        assertEquals(2, cantidad("matricula.EstudianteTelefono"));
    }

    @Test
    void errorTecnicoRegistraBitacoraYRevierteEscritura() throws Exception {
        doAnswer(invocacion -> {
            invocacion.callRealMethod();
            throw new DataAccessResourceFailureException("password=dato-ficticio");
        }).when(repositorio).crear(any());
        mvc.perform(conDatos(post("/expediente"), datos())).andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("No fue posible completar la operacion"));
        assertEquals(0, cantidad("matricula.Estudiante"));
        assertEquals(0, cantidad("matricula.EstudianteTelefono"));
        verify(seguridad).registrarBitacora(eq(TOKEN), eq("operador@cuc.cr"),
                argThat(descripcion -> descripcion.contains("Error tecnico") && !descripcion.contains("password")));
    }

    @Test
    void recursosInexistentesDevuelven404() throws Exception {
        mvc.perform(get("/expediente/ausente").header("Authorization", TOKEN)).andExpect(status().isNotFound());
        mvc.perform(conDatos(put("/expediente/ausente"), datos())).andExpect(status().isNotFound());
        mvc.perform(delete("/expediente/ausente").header("Authorization", TOKEN)).andExpect(status().isNotFound());
    }

    @Test
    void metodoNoAdmitidoNoSeReportaComoErrorInterno() throws Exception {
        mvc.perform(conDatos(patch("/expediente/123"), datos())).andExpect(status().isMethodNotAllowed());
        verify(seguridad, never()).registrarBitacora(anyString(), anyString(), anyString());
    }

    @Test
    void sqlNoInterpretaLaIdentificacionComoUnaInstruccion() throws Exception {
        crear();
        mvc.perform(get("/expediente/{identificacion}", "' OR '1'='1").header("Authorization", TOKEN))
                .andExpect(status().isNotFound());
        assertEquals(1, cantidad("matricula.Estudiante"));
    }

    @Test
    void consultaYActualizaSoloSuExpedienteConPersistenciaYBitacoraMinima() throws Exception {
        crear();
        String tokenA = tokenAna();
        when(seguridad.validar(tokenA)).thenReturn(true);
        when(seguridad.obtenerSesion(tokenA)).thenReturn(sesion("ana@cuc.cr", "123", "Cedula", "ESTUDIANTE"));
        clearInvocations(seguridad);
        mvc.perform(get("/expediente/propio").header("Authorization", tokenA))
                .andExpect(status().isOk()).andExpect(jsonPath("$.identificacion").value("123"))
                .andExpect(jsonPath("$.email").value("ana@cuc.cr"));
        mvc.perform(conDatosToken(put("/expediente/propio"), cambiosPropios(), tokenA))
                .andExpect(status().isOk()).andExpect(jsonPath("$.telefonos.length()").value(2))
                .andExpect(jsonPath("$.otrasSenas").value("Casa verde"));
        assertEquals("Ana Maria", jdbc.queryForObject("SELECT NombreCompleto FROM matricula.Estudiante WHERE Identificacion='123'", String.class));
        assertEquals("ana@cuc.cr", jdbc.queryForObject("SELECT Email FROM matricula.Estudiante WHERE Identificacion='123'", String.class));
        assertEquals("Casa verde", jdbc.queryForObject("SELECT OtrasSenas FROM matricula.Estudiante WHERE Identificacion='123'", String.class));
        assertEquals(List.of("777", "888"), jdbc.queryForList(
                "SELECT Telefono FROM matricula.EstudianteTelefono WHERE IdentificacionEstudiante='123' ORDER BY Telefono", String.class));
        verify(seguridad, times(2)).obtenerSesion(tokenA);
        var logs = org.mockito.ArgumentCaptor.forClass(String.class);
        verify(seguridad, times(2)).registrarBitacora(eq(tokenA), eq("ana@cuc.cr"), logs.capture());
        assertEquals("El usuario consulta expediente propio", logs.getAllValues().get(0));
        assertTrue(logs.getAllValues().get(1).contains("Casa verde"));
        assertFalse(logs.getAllValues().get(1).contains("ana@cuc.cr"));
        assertFalse(logs.getAllValues().get(1).contains("Ana Maria"));
    }

    @Test
    void estudiantesDiferentesSoloVenYActualizanSuPropioRegistro() throws Exception {
        crear();
        ObjectNode segundo = datos().put("identificacion", "456").put("email", "luis@cuc.cr").put("nombreCompleto", "Luis Mora");
        mvc.perform(conDatos(post("/expediente"), segundo)).andExpect(status().isCreated());
        String tokenA = tokenAna();
        when(seguridad.validar(tokenA)).thenReturn(true);
        when(seguridad.obtenerSesion(tokenA)).thenReturn(sesion("ana@cuc.cr", "123", "Cedula", "ESTUDIANTE"));
        String tokenB = tokenPara("luis@cuc.cr");
        when(seguridad.validar(tokenB)).thenReturn(true);
        when(seguridad.obtenerSesion(tokenB)).thenReturn(sesion("luis@cuc.cr", "456", "Cedula", "ESTUDIANTE"));
        mvc.perform(get("/expediente/propio").header("Authorization", tokenA))
                .andExpect(status().isOk()).andExpect(jsonPath("$.identificacion").value("123"));
        mvc.perform(get("/expediente/propio").header("Authorization", tokenB))
                .andExpect(status().isOk()).andExpect(jsonPath("$.identificacion").value("456"));
        mvc.perform(conDatosToken(put("/expediente/propio"), cambiosPropios(), tokenB))
                .andExpect(status().isOk()).andExpect(jsonPath("$.identificacion").value("456"));
        assertEquals("Casa azul", jdbc.queryForObject("SELECT OtrasSenas FROM matricula.Estudiante WHERE Identificacion='123'", String.class));
        assertEquals("Casa verde", jdbc.queryForObject("SELECT OtrasSenas FROM matricula.Estudiante WHERE Identificacion='456'", String.class));
    }

    @Test
    void rechazaCualquierCampoProtegidoAunqueElRestoSeaValido() throws Exception {
        crear();
        String tokenA = tokenAna();
        when(seguridad.validar(tokenA)).thenReturn(true);
        when(seguridad.obtenerSesion(tokenA)).thenReturn(sesion("ana@cuc.cr", "123", "Cedula", "ESTUDIANTE"));
        ObjectNode solicitud = cambiosPropios().put("identificacion", "456").put("nombreCompleto", "Cambio indebido");
        mvc.perform(conDatosToken(put("/expediente/propio"), solicitud, tokenA)).andExpect(status().isBadRequest());
        assertEquals("Ana Maria", jdbc.queryForObject("SELECT NombreCompleto FROM matricula.Estudiante", String.class));
        assertEquals("Casa azul", jdbc.queryForObject("SELECT OtrasSenas FROM matricula.Estudiante", String.class));
    }

    @Test
    void rechazaRolIncorrectoVinculoAusenteYAmbiguo() throws Exception {
        crear();
        String tokenA = tokenAna();
        when(seguridad.validar(tokenA)).thenReturn(true);
        when(seguridad.obtenerSesion(TOKEN)).thenReturn(sesion("operador@cuc.cr", "123", "Cedula", "ADMINISTRADOR"));
        mvc.perform(get("/expediente/propio").header("Authorization", TOKEN)).andExpect(status().isForbidden());
        when(seguridad.obtenerSesion(tokenA)).thenReturn(sesion("ana@cuc.cr", "999", "Cedula", "ESTUDIANTE"));
        mvc.perform(get("/expediente/propio").header("Authorization", tokenA)).andExpect(status().isNotFound());
        when(seguridad.obtenerSesion(tokenA)).thenReturn(sesion("ana@cuc.cr", "123", "Cedula", "ESTUDIANTE"));
        doReturn(List.of(new DatosExpediente("Cedula", "123", "ana@cuc.cr", "Ana Maria",
                java.time.LocalDate.parse("2012-05-10"), 1, 2, 3, "Casa azul", List.of("111")),
                new DatosExpediente("Cedula", "123", "ana@cuc.cr", "Ana Maria",
                java.time.LocalDate.parse("2012-05-10"), 1, 2, 3, "Casa azul", List.of("222"))))
                .when(repositorio).obtenerVinculados("ana@cuc.cr", "Cedula", "123");
        mvc.perform(get("/expediente/propio").header("Authorization", tokenA)).andExpect(status().isConflict());
    }

    @Test
    void rechazaDireccionIncoherenteYTokenInvalido() throws Exception {
        crear();
        String tokenA = tokenAna();
        when(seguridad.validar(tokenA)).thenReturn(true);
        when(seguridad.obtenerSesion(tokenA)).thenReturn(sesion("ana@cuc.cr", "123", "Cedula", "ESTUDIANTE"));
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Direccion invalida"))
                .when(direcciones).validar(tokenA, 1, 2, 3);
        mvc.perform(conDatosToken(put("/expediente/propio"), cambiosPropios(), tokenA)).andExpect(status().isBadRequest());
        clearInvocations(seguridad);
        when(seguridad.validar("Bearer vencido")).thenReturn(false);
        mvc.perform(get("/expediente/propio").header("Authorization", "Bearer vencido"))
                .andExpect(status().isUnauthorized());
        verify(seguridad, never()).obtenerSesion(anyString());
        assertEquals("Casa azul", jdbc.queryForObject("SELECT OtrasSenas FROM matricula.Estudiante", String.class));
    }

    @Test
    void dependenciasDeAutenticacionNoDisponiblesDevuelven503() throws Exception {
        String tokenA = tokenAna();
        when(seguridad.validar(tokenA)).thenReturn(true);
        when(seguridad.obtenerSesion(tokenA)).thenThrow(new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE, "Autenticación no disponible"));
        mvc.perform(get("/expediente/propio").header("Authorization", tokenA))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    void camposEditablesObligatoriosSeValidanAntesDePersistir() throws Exception {
        crear();
        String tokenA = tokenAna();
        when(seguridad.validar(tokenA)).thenReturn(true);
        when(seguridad.obtenerSesion(tokenA)).thenReturn(sesion("ana@cuc.cr", "123", "Cedula", "ESTUDIANTE"));
        mvc.perform(conDatosToken(put("/expediente/propio"), cambiosPropios().put("otrasSenas", "  "), tokenA))
                .andExpect(status().isBadRequest());
        ObjectNode sinTelefonos = cambiosPropios();
        sinTelefonos.putArray("telefonos");
        mvc.perform(conDatosToken(put("/expediente/propio"), sinTelefonos, tokenA))
                .andExpect(status().isBadRequest());
        assertEquals("Casa azul", jdbc.queryForObject("SELECT OtrasSenas FROM matricula.Estudiante", String.class));
    }

    @Test
    void identidadEnQueryNoCambiaElExpedientePropio() throws Exception {
        crear();
        mvc.perform(get("/expediente/propio?identificacion=456").header("Authorization", TOKEN))
                .andExpect(status().isBadRequest());
        assertEquals(1, cantidad("matricula.Estudiante"));
    }
}
