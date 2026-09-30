package cr.ac.cuc.nuevoavatar.persona2.grupo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cr.ac.cuc.nuevoavatar.persona2.curso.CursoRepository;
import cr.ac.cuc.nuevoavatar.persona2.periodo.PeriodoRepository;
import cr.ac.cuc.nuevoavatar.persona2.profesor.ProfesorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class GrupoServiceTest {

    @Test
    void rechazaCursoInexistente() {
        CursoRepository cursos = mock(CursoRepository.class);
        ProfesorRepository profesores = mock(ProfesorRepository.class);
        PeriodoRepository periodos = mock(PeriodoRepository.class);
        when(cursos.existsById(10)).thenReturn(false);

        GrupoService servicio = new GrupoService(
            mock(GrupoRepository.class), cursos, profesores, periodos
        );

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.crear(new GrupoRequest(1, 10, 20, "Lunes 18:00", 30, 1))
        );
        assertEquals(HttpStatus.UNPROCESSABLE_CONTENT, error.getStatusCode());
    }
}
