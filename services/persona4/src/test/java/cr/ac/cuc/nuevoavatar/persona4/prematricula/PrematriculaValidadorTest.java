package cr.ac.cuc.nuevoavatar.persona4.prematricula;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import cr.ac.cuc.nuevoavatar.persona4.prematricula.DatosPrematricula.Curso;
import cr.ac.cuc.nuevoavatar.persona4.prematricula.DatosPrematricula.Periodo;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class PrematriculaValidadorTest {
    private static final LocalDate HOY = LocalDate.of(2026, 10, 3);
    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private final PrematriculaValidador validador = new PrematriculaValidador(FACTORY.getValidator());

    @AfterAll
    static void cerrar() {
        FACTORY.close();
    }

    private static DatosPrematricula datos(String identificacion, Integer carrera,
            List<Curso> cursos, String observaciones, Periodo periodo) {
        return new DatosPrematricula(identificacion, carrera, cursos, observaciones, periodo);
    }

    private static List<Curso> cursosValidos() {
        return List.of(new Curso(10, 1), new Curso(11, 1));
    }

    private static Periodo periodoFuturo() {
        return new Periodo(20, HOY.plusDays(1));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    void identificacionEsRequerida(String identificacion) {
        var datos = datos(identificacion, 3, cursosValidos(), null, periodoFuturo());
        assertThrows(ConstraintViolationException.class, () -> validador.validar(datos, HOY));
    }

    static Stream<Arguments> datosAusentes() {
        return Stream.of(
                Arguments.of("carreraId", datos("123", null, cursosValidos(), null, periodoFuturo())),
                Arguments.of("cursos", datos("123", 3, null, null, periodoFuturo())),
                Arguments.of("cursos", datos("123", 3, List.of(), null, periodoFuturo())),
                Arguments.of("cursos", datos("123", 3, Arrays.asList(new Curso(10, 1), null), null, periodoFuturo())),
                Arguments.of("cursos", datos("123", 3, List.of(new Curso(null, 1)), null, periodoFuturo())),
                Arguments.of("cursos", datos("123", 3, List.of(new Curso(10, null)), null, periodoFuturo())),
                Arguments.of("periodo", datos("123", 3, cursosValidos(), null, null)),
                Arguments.of("periodo", datos("123", 3, cursosValidos(), null, new Periodo(null, HOY.plusDays(1)))),
                Arguments.of("periodo", datos("123", 3, cursosValidos(), null, new Periodo(20, null))));
    }

    @ParameterizedTest(name = "Requerido: {0} [{index}]")
    @MethodSource("datosAusentes")
    void rechazaDatosAusentesDeCarreraCursosOPeriodo(String campo, DatosPrematricula datos) {
        var ex = assertThrows(ConstraintViolationException.class, () -> validador.validar(datos, HOY));
        assertTrue(ex.getConstraintViolations().stream()
                .anyMatch(v -> v.getPropertyPath().toString().startsWith(campo)));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "Desea ingresar a la carrera indicada."})
    void observacionesSonOpcionales(String observaciones) {
        var datos = datos("123", 3, cursosValidos(), observaciones, periodoFuturo());
        assertDoesNotThrow(() -> validador.validar(datos, HOY));
        assertEquals(observaciones, datos.observaciones());
    }

    @Test
    void aceptaUnSoloCursoDePrimerNivel() {
        var datos = datos("123", 3, List.of(new Curso(10, 1)), null, periodoFuturo());
        assertDoesNotThrow(() -> validador.validar(datos, HOY));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 2, 12})
    void todosLosCursosDebenSerDePrimerNivel(int nivel) {
        var datos = datos("123", 3, List.of(new Curso(10, 1), new Curso(11, nivel)), null, periodoFuturo());
        var ex = assertThrows(ResponseStatusException.class, () -> validador.validar(datos, HOY));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Solo se pueden prematricular cursos de primer nivel", ex.getReason());
    }

    @ParameterizedTest
    @ValueSource(ints = {-365, -1, 0})
    void rechazaPeriodoPasadoOQueIniciaHoy(int diferenciaDias) {
        var datos = datos("123", 3, cursosValidos(), null, new Periodo(20, HOY.plusDays(diferenciaDias)));
        var ex = assertThrows(ResponseStatusException.class, () -> validador.validar(datos, HOY));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("La fecha de inicio del periodo debe ser posterior a la fecha actual", ex.getReason());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 30, 365})
    void aceptaPeriodoFuturoSinAgregarPlazoMaximo(int diferenciaDias) {
        var datos = datos("123", 3, cursosValidos(), null, new Periodo(20, HOY.plusDays(diferenciaDias)));
        assertDoesNotThrow(() -> validador.validar(datos, HOY));
    }

    @Test
    void evaluaFechaDeCadaOperacionSinConservarUnHoyFijo() {
        var datos = datos("123", 3, cursosValidos(), null, periodoFuturo());
        assertDoesNotThrow(() -> validador.validar(datos, HOY));
        assertThrows(ResponseStatusException.class, () -> validador.validar(datos, HOY.plusDays(1)));
    }

    @Test
    void comparaFechasAlCambiarDeAnio() {
        var datos = datos("123", 3, cursosValidos(), null, new Periodo(20, LocalDate.of(2027, 1, 1)));
        assertDoesNotThrow(() -> validador.validar(datos, LocalDate.of(2026, 12, 31)));
    }

    @Test
    void rechazaDatosNulos() {
        assertThrows(IllegalArgumentException.class, () -> validador.validar(null, HOY));
    }

    @Test
    void necesitaFechaDelServidorParaEvaluarPeriodo() {
        var datos = datos("123", 3, cursosValidos(), null, periodoFuturo());
        assertThrows(IllegalArgumentException.class, () -> validador.validar(datos, null));
    }
}
