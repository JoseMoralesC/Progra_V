package cr.ac.cuc.nuevoavatar.persona5.notas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import cr.ac.cuc.nuevoavatar.persona5.academico.GrupoRepository;
import cr.ac.cuc.nuevoavatar.persona5.matricula.MatriculaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class NotasServiceTest {

    @ParameterizedTest
    @CsvSource(value = {
        "Proyecto|Proyecto",
        " Proyecto |proyecto",
        "Evaluación|EVALUACIÓN",
        "Evaluación|Evaluacio\u0301n"
    }, delimiter = '|', ignoreLeadingAndTrailingWhitespace = false)
    void rechazaNombresRepetidosAntesDeBloquearOBorrar(String primero, String segundo) {
        var grupos = mock(GrupoRepository.class);
        var rubros = mock(DesgloseRubroRepository.class);
        var notas = mock(NotaRubroRepository.class);
        var auditoria = mock(AuditoriaNotas.class);
        var bloqueo = mock(BloqueoGrupoNotas.class);
        when(grupos.existsById(1)).thenReturn(true);
        var servicio = new NotasService(rubros, notas, mock(MatriculaRepository.class), grupos, auditoria, bloqueo);
        var request = new CargarDesgloseRequest(1, List.of(
            new CargarDesgloseRequest.RubroRequest(primero, new BigDecimal("50")),
            new CargarDesgloseRequest.RubroRequest(segundo, new BigDecimal("50"))));

        var error = assertThrows(ResponseStatusException.class, () -> servicio.cargarDesglose(request));

        assertEquals(HttpStatus.UNPROCESSABLE_CONTENT, error.getStatusCode());
        assertEquals("Los nombres de los rubros no pueden repetirse en el mismo grupo", error.getReason());
        verifyNoInteractions(rubros, notas, auditoria, bloqueo);
    }

    @ParameterizedTest
    @ValueSource(ints = {2601, 2627})
    void traduceConflictoDeNombreSqlYNoAuditaExito(int codigo) {
        var grupos = mock(GrupoRepository.class);
        var rubros = mock(DesgloseRubroRepository.class);
        var auditoria = mock(AuditoriaNotas.class);
        when(grupos.existsById(1)).thenReturn(true);
        when(rubros.saveAll(anyList())).thenThrow(new DataIntegrityViolationException("restricción",
            new SQLException("UQ_DesgloseRubro_Grupo_Nombre", "23000", codigo)));
        var servicio = new NotasService(rubros, mock(NotaRubroRepository.class),
            mock(MatriculaRepository.class), grupos, auditoria, mock(BloqueoGrupoNotas.class));

        var error = assertThrows(ResponseStatusException.class, () -> servicio.cargarDesglose(
            new CargarDesgloseRequest(1, List.of(
                new CargarDesgloseRequest.RubroRequest("Proyecto", new BigDecimal("100"))))));

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verifyNoInteractions(auditoria);
    }

    @Test
    void noConvierteOtraRestriccionSqlEnNombreRepetido() {
        var grupos = mock(GrupoRepository.class);
        var rubros = mock(DesgloseRubroRepository.class);
        when(grupos.existsById(1)).thenReturn(true);
        var fallo = new DataIntegrityViolationException("otra restricción",
            new SQLException("PK_DesgloseRubro", "23000", 2627));
        when(rubros.saveAll(anyList())).thenThrow(fallo);
        var servicio = new NotasService(rubros, mock(NotaRubroRepository.class),
            mock(MatriculaRepository.class), grupos, mock(AuditoriaNotas.class), mock(BloqueoGrupoNotas.class));

        assertEquals(fallo, assertThrows(DataIntegrityViolationException.class, () -> servicio.cargarDesglose(
            new CargarDesgloseRequest(1, List.of(
                new CargarDesgloseRequest.RubroRequest("Proyecto", new BigDecimal("100")))))));
    }

    @Test
    void nombresDistintosConYSinTildeSePuedenGuardar() {
        var grupos = mock(GrupoRepository.class);
        var rubros = mock(DesgloseRubroRepository.class);
        when(grupos.existsById(1)).thenReturn(true);
        when(rubros.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));
        var servicio = new NotasService(rubros, mock(NotaRubroRepository.class),
            mock(MatriculaRepository.class), grupos, mock(AuditoriaNotas.class), mock(BloqueoGrupoNotas.class));

        var resultado = servicio.cargarDesglose(new CargarDesgloseRequest(1, List.of(
            new CargarDesgloseRequest.RubroRequest("Examen", new BigDecimal("50")),
            new CargarDesgloseRequest.RubroRequest("Exámen", new BigDecimal("50")))));

        assertEquals(List.of("Examen", "Exámen"), resultado.stream().map(RubroResponse::nombre).toList());
    }

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
        var servicio = new NotasService(rubros, notas, mock(MatriculaRepository.class), grupos, auditoria, mock(BloqueoGrupoNotas.class));
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
            mock(AuditoriaNotas.class),
            mock(BloqueoGrupoNotas.class)
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
            mock(AuditoriaNotas.class),
            mock(BloqueoGrupoNotas.class)
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
