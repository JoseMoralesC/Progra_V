package cr.ac.cuc.nuevoavatar.persona5.notas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import cr.ac.cuc.nuevoavatar.persona5.academico.GrupoRepository;
import cr.ac.cuc.nuevoavatar.persona5.matricula.MatriculaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class NotasServiceTest {

    @Test
    void reemplazoAuditaSnapshotPrevioYResultadoPersistido() {
        var grupos = mock(GrupoRepository.class);
        var rubros = mock(DesgloseRubroRepository.class);
        var notas = mock(NotaRubroRepository.class);
        var auditoria = mock(AuditoriaNotas.class);
        when(grupos.existsById(1)).thenReturn(true);
        var anterior = mock(DesgloseRubro.class);
        when(anterior.getId()).thenReturn(10); when(anterior.getGrupoId()).thenReturn(1);
        when(anterior.getNombre()).thenReturn("Anterior"); when(anterior.getPorcentaje()).thenReturn(new BigDecimal("100"));
        var actual = mock(DesgloseRubro.class);
        when(actual.getId()).thenReturn(20); when(actual.getGrupoId()).thenReturn(1);
        when(actual.getNombre()).thenReturn("Proyecto"); when(actual.getPorcentaje()).thenReturn(new BigDecimal("100"));
        when(rubros.findByGrupoId(1)).thenReturn(List.of(anterior));
        when(rubros.saveAll(anyList())).thenReturn(List.of(actual));
        var servicio = new NotasService(rubros, notas, mock(MatriculaRepository.class), grupos, auditoria);
        var resultado = servicio.cargarDesglose(new CargarDesgloseRequest(1,
            List.of(new CargarDesgloseRequest.RubroRequest("Proyecto", new BigDecimal("100")))));
        assertEquals(20, resultado.get(0).id());
        var orden = inOrder(rubros, auditoria);
        orden.verify(rubros).findByGrupoId(1);
        orden.verify(rubros).deleteByGrupoId(1); orden.verify(rubros).flush();
        orden.verify(rubros).saveAll(anyList()); orden.verify(rubros).flush();
        orden.verify(auditoria).registrarDesglose(1, List.of(RubroResponse.desde(anterior)), resultado);
    }

    @Test
    void rechazaDesgloseQueNoSumaCien() {
        GrupoRepository grupos = mock(GrupoRepository.class);
        when(grupos.existsById(1)).thenReturn(true);

        NotasService servicio = new NotasService(
            mock(DesgloseRubroRepository.class),
            mock(NotaRubroRepository.class),
            mock(MatriculaRepository.class),
            grupos,
            mock(AuditoriaNotas.class)
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
            grupos,
            mock(AuditoriaNotas.class)
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
