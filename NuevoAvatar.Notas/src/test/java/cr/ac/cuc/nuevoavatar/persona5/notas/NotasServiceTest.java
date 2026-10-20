package cr.ac.cuc.nuevoavatar.persona5.notas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import cr.ac.cuc.nuevoavatar.persona5.academico.GrupoRepository;
import cr.ac.cuc.nuevoavatar.persona5.matricula.MatriculaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class NotasServiceTest {

    @Test
    void rechazaDesgloseQueNoSumaCien() {
        GrupoRepository grupos = mock(GrupoRepository.class);
        when(grupos.existsById(1)).thenReturn(true);

        NotasService servicio = new NotasService(
            mock(DesgloseRubroRepository.class),
            mock(NotaRubroRepository.class),
            mock(MatriculaRepository.class),
            grupos
        );

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.cargarDesglose(new CargarDesgloseRequest(
                1,
                List.of(
                    new CargarDesgloseRequest.RubroRequest(
                        "Proyecto",
                        new BigDecimal("60.00")
                    )
                )
            ))
        );

        assertEquals(HttpStatus.UNPROCESSABLE_CONTENT, error.getStatusCode());
    }

    @Test
    void bloqueaCambioDeRubrosCuandoYaExistenNotas() {
        GrupoRepository grupos = mock(GrupoRepository.class);
        DesgloseRubroRepository rubros = mock(DesgloseRubroRepository.class);
        NotaRubroRepository notas = mock(NotaRubroRepository.class);

        DesgloseRubro existente = mock(DesgloseRubro.class);
        when(existente.getId()).thenReturn(10);

        when(grupos.existsById(1)).thenReturn(true);
        when(rubros.findByGrupoId(1)).thenReturn(List.of(existente));
        when(notas.existsByRubroIdIn(anyList())).thenReturn(true);

        NotasService servicio = new NotasService(
            rubros,
            notas,
            mock(MatriculaRepository.class),
            grupos
        );

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.cargarDesglose(new CargarDesgloseRequest(
                1,
                List.of(
                    new CargarDesgloseRequest.RubroRequest(
                        "Proyecto",
                        new BigDecimal("100.00")
                    )
                )
            ))
        );

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
    }

}
