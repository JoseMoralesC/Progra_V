package cr.ac.cuc.nuevoavatar.persona4.expediente;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClient;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpedienteValidadorTest {
    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private SeguridadClient seguridad;
    private ExpedienteValidador validador;

    @BeforeEach
    void preparar() {
        seguridad = mock(SeguridadClient.class);
        validador = new ExpedienteValidador(FACTORY.getValidator(), seguridad);
    }

    @AfterAll
    static void cerrar() {
        FACTORY.close();
    }

    private static Object[] valoresValidos() {
        return new Object[]{"Cedula", "123", "ana@cuc.cr", "Ana Maria",
                LocalDate.of(2012, 5, 10), 1, 2, 3, "Casa azul", List.of("123")};
    }

    @SuppressWarnings("unchecked")
    private static DatosExpediente datos(Object[] valores) {
        return new DatosExpediente((String) valores[0], (String) valores[1],
                (String) valores[2], (String) valores[3], (LocalDate) valores[4],
                (Integer) valores[5], (Integer) valores[6], (Integer) valores[7],
                (String) valores[8], (List<String>) valores[9]);
    }

    static Stream<Arguments> requeridos() {
        var casos = Stream.<Arguments>builder();
        String[] campos = {"tipoIdentificacion", "identificacion", "email", "nombreCompleto",
                "fechaNacimiento", "provinciaId", "cantonId", "distritoId", "otrasSenas", "telefonos"};
        for (int i = 0; i < campos.length; i++) {
            Object[] valores = valoresValidos();
            valores[i] = null;
            casos.add(Arguments.of(campos[i], datos(valores)));
        }
        for (int i : new int[]{0, 1, 2, 3, 8}) {
            for (String vacio : List.of("", "   ", "\t")) {
                Object[] valores = valoresValidos();
                valores[i] = vacio;
                casos.add(Arguments.of(campos[i], datos(valores)));
            }
        }
        for (List<String> telefonos : List.<List<String>>of(List.of(), List.of(""),
                List.of("   "), Arrays.asList("123", null))) {
            Object[] valores = valoresValidos();
            valores[9] = telefonos;
            casos.add(Arguments.of("telefonos", datos(valores)));
        }
        return casos.build();
    }

    @ParameterizedTest(name = "Requerido: {0} [{index}]")
    @MethodSource("requeridos")
    void rechazaDatosAusentesAntesDeConsultarParametro(String campo, DatosExpediente datos) {
        var ex = assertThrows(ConstraintViolationException.class, () -> validador.validar(datos, "Bearer prueba"));
        assertTrue(ex.getConstraintViolations().stream()
                .anyMatch(v -> v.getPropertyPath().toString().startsWith(campo)));
        verifyNoInteractions(seguridad);
    }

    @ParameterizedTest
    @ValueSource(strings = {"José Muñoz", "Ana María", "Jose\u0301 Mun\u0303oz"})
    void aceptaLetrasAcentuadas(String nombre) {
        var valores = valoresValidos();
        valores[3] = nombre;
        when(seguridad.obtenerParametro("Bearer prueba", "DOMESTUD")).thenReturn("cuc.cr");
        assertDoesNotThrow(() -> validador.validar(datos(valores), "Bearer prueba"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Ana2", "Ana-Maria", "Ana\nMaria", "Ana_Maria", "\u0301"})
    void rechazaNombreConOtrosCaracteres(String nombre) {
        var valores = valoresValidos();
        valores[3] = nombre;
        assertThrows(ConstraintViolationException.class, () -> validador.validar(datos(valores), "Bearer prueba"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ana", "ana@@cuc.cr", "ana maria@cuc.cr", "@cuc.cr"})
    void rechazaFormatoCorreo(String email) {
        var valores = valoresValidos();
        valores[2] = email;
        assertThrows(ConstraintViolationException.class, () -> validador.validar(datos(valores), "Bearer prueba"));
        verifyNoInteractions(seguridad);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ana@otro.cr", "ana@sub.cuc.cr", "ana@cuc.cr.otro.com"})
    void rechazaDominioDistinto(String email) {
        var valores = valoresValidos();
        valores[2] = email;
        when(seguridad.obtenerParametro("Bearer prueba", "DOMESTUD")).thenReturn("cuc.cr");
        var ex = assertThrows(ResponseStatusException.class, () -> validador.validar(datos(valores), "Bearer prueba"));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void utilizaDominioConfiguradoYSinDiferenciarMayusculas() {
        var valores = valoresValidos();
        valores[2] = "ana@ESTUDIANTES.CR";
        when(seguridad.obtenerParametro("Bearer prueba", "DOMESTUD")).thenReturn("estudiantes.cr");
        assertDoesNotThrow(() -> validador.validar(datos(valores), "Bearer prueba"));
    }

    @Test
    void noAgregaEdadMinimaNiFormatoDeTelefono() {
        var valores = valoresValidos();
        valores[4] = LocalDate.now().minusYears(10);
        valores[9] = List.of("+506 2222-3333 ext 4", "123");
        when(seguridad.obtenerParametro("Bearer prueba", "DOMESTUD")).thenReturn("cuc.cr");
        assertDoesNotThrow(() -> validador.validar(datos(valores), "Bearer prueba"));
    }

    @Test
    void rechazaExpedienteNulo() {
        assertThrows(IllegalArgumentException.class, () -> validador.validar(null, "Bearer prueba"));
        verifyNoInteractions(seguridad);
    }

    @Test
    void propagaFalloDelParametro() {
        when(seguridad.obtenerParametro("Bearer prueba", "DOMESTUD"))
                .thenThrow(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE));
        assertThrows(ResponseStatusException.class, () -> validador.validar(datos(valoresValidos()), "Bearer prueba"));
    }
}
