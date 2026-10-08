package cr.ac.cuc.nuevoavatar.persona5.matricula;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import cr.ac.cuc.nuevoavatar.persona5.academico.CursoRepository;
import cr.ac.cuc.nuevoavatar.persona5.academico.Grupo;
import cr.ac.cuc.nuevoavatar.persona5.academico.GrupoRepository;
import cr.ac.cuc.nuevoavatar.persona5.academico.Periodo;
import cr.ac.cuc.nuevoavatar.persona5.academico.PeriodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class MatriculaServiceTest {

    @Test
    void rechazaCursoInexistente() {
        CursoRepository cursos = mock(CursoRepository.class);
        when(cursos.existsById(10)).thenReturn(false);

        MatriculaService servicio = new MatriculaService(
            mock(MatriculaRepository.class),
            cursos,
            mock(GrupoRepository.class),
            mock(PeriodoRepository.class)
        );

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.crear(new MatriculaRequest("1-111", 10, 20, 30))
        );

        assertEquals(HttpStatus.UNPROCESSABLE_CONTENT, error.getStatusCode());
    }

    @Test
    void rechazaPeriodoInactivo() {
        CursoRepository cursos = mock(CursoRepository.class);
        GrupoRepository grupos = mock(GrupoRepository.class);
        PeriodoRepository periodos = mock(PeriodoRepository.class);

        Grupo grupo = grupo(10, 30);
        Periodo periodo = periodo(
            LocalDate.now().minusDays(10),
            LocalDate.now().minusDays(1)
        );

        when(cursos.existsById(10)).thenReturn(true);
        when(grupos.findById(20)).thenReturn(Optional.of(grupo));
        when(periodos.findById(30)).thenReturn(Optional.of(periodo));

        MatriculaService servicio = new MatriculaService(
            mock(MatriculaRepository.class),
            cursos,
            grupos,
            periodos
        );

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.crear(new MatriculaRequest("1-111", 10, 20, 30))
        );

        assertEquals(HttpStatus.UNPROCESSABLE_CONTENT, error.getStatusCode());
    }

    @Test
    void creaMatriculaActiva() {
        MatriculaRepository matriculas = mock(MatriculaRepository.class);
        CursoRepository cursos = mock(CursoRepository.class);
        GrupoRepository grupos = mock(GrupoRepository.class);
        PeriodoRepository periodos = mock(PeriodoRepository.class);

        Grupo grupo = grupo(10, 30);
        Periodo periodo = periodo(
            LocalDate.now().minusDays(1),
            LocalDate.now().plusDays(10)
        );

        when(cursos.existsById(10)).thenReturn(true);
        when(grupos.findById(20)).thenReturn(Optional.of(grupo));
        when(periodos.findById(30)).thenReturn(Optional.of(periodo));
        when(matriculas.saveAndFlush(any(Matricula.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        MatriculaService servicio = new MatriculaService(
            matriculas,
            cursos,
            grupos,
            periodos
        );

        MatriculaResponse response = servicio.crear(
            new MatriculaRequest(" 1-111 ", 10, 20, 30)
        );

        assertEquals("1-111", response.identificacionEstudiante());
        assertEquals(Matricula.ACTIVA, response.estado());
    }

    @Test
    void rechazaModificarHaciaMatriculaDuplicada() throws Exception {
        MatriculaRepository matriculas = mock(MatriculaRepository.class);
        CursoRepository cursos = mock(CursoRepository.class);
        GrupoRepository grupos = mock(GrupoRepository.class);
        PeriodoRepository periodos = mock(PeriodoRepository.class);

        Grupo grupo = grupo(10, 30);
        Periodo periodo = periodo(
            LocalDate.now().minusDays(1),
            LocalDate.now().plusDays(10)
        );
        Matricula actual = matricula(1);
        Matricula duplicada = matricula(2);

        when(matriculas.findById(1)).thenReturn(Optional.of(actual));
        when(cursos.existsById(10)).thenReturn(true);
        when(grupos.findById(20)).thenReturn(Optional.of(grupo));
        when(periodos.findById(30)).thenReturn(Optional.of(periodo));
        when(matriculas.findFirstByIdentificacionEstudianteAndCursoIdAndGrupoIdAndPeriodoIdAndEstado(
            "1-111",
            10,
            20,
            30,
            Matricula.ACTIVA
        )).thenReturn(Optional.of(duplicada));

        MatriculaService servicio = new MatriculaService(
            matriculas,
            cursos,
            grupos,
            periodos
        );

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.modificar(1, new MatriculaRequest("1-111", 10, 20, 30))
        );

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
    }

    private Grupo grupo(Integer cursoId, Integer periodoId) {
        Grupo grupo = mock(Grupo.class);
        when(grupo.getCursoId()).thenReturn(cursoId);
        when(grupo.getPeriodoId()).thenReturn(periodoId);
        return grupo;
    }

    private Periodo periodo(LocalDate inicio, LocalDate fin) {
        Periodo periodo = new Periodo();
        periodo.setFechaInicio(inicio);
        periodo.setFechaFin(fin);
        return periodo;
    }

    private Matricula matricula(Integer id) throws Exception {
        Matricula matricula = new Matricula();
        Field idField = Matricula.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(matricula, id);
        matricula.setIdentificacionEstudiante("1-111");
        matricula.setCursoId(10);
        matricula.setGrupoId(20);
        matricula.setPeriodoId(30);
        matricula.setEstado(Matricula.ACTIVA);
        matricula.setFechaMatricula(LocalDateTime.now());
        return matricula;
    }
}
