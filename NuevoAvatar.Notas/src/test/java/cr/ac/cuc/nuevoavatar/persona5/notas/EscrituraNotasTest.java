package cr.ac.cuc.nuevoavatar.persona5.notas;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import cr.ac.cuc.nuevoavatar.persona5.academico.GrupoRepository;
import cr.ac.cuc.nuevoavatar.persona5.matricula.Matricula;
import cr.ac.cuc.nuevoavatar.persona5.matricula.MatriculaRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class EscrituraNotasTest {
    private final DesgloseRubroRepository rubros = mock(DesgloseRubroRepository.class);
    private final NotaRubroRepository notas = mock(NotaRubroRepository.class);
    private final MatriculaRepository matriculas = mock(MatriculaRepository.class);
    private final AuditoriaNotas auditoria = mock(AuditoriaNotas.class);
    private final NotasService servicio = new NotasService(rubros, notas, matriculas, mock(GrupoRepository.class), auditoria);

    @ParameterizedTest @ValueSource(strings = { "0", "100.01", "-1" })
    void notaFueraDeRangoNoEscribeNiBloqueaRubros(String valor) {
        assertEquals(HttpStatus.BAD_REQUEST, assertThrows(ResponseStatusException.class,
            () -> servicio.asignarNota(request(valor), false)).getStatusCode());
        verifyNoInteractions(notas, rubros, matriculas, auditoria);
    }

    @Test void postNoSobrescribeNotaExistente() {
        preparar();
        when(notas.findByMatriculaIdAndRubroId(1, 2)).thenReturn(Optional.of(new NotaRubro()));
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
            () -> servicio.asignarNota(request("50"), false)).getStatusCode());
        verify(notas, never()).saveAndFlush(any());
        verifyNoInteractions(auditoria);
    }

    @Test void putNoCreaUnaNotaInexistente() {
        preparar();
        when(notas.findByMatriculaIdAndRubroId(1, 2)).thenReturn(Optional.empty());
        assertEquals(HttpStatus.NOT_FOUND, assertThrows(ResponseStatusException.class,
            () -> servicio.asignarNota(request("50"), true)).getStatusCode());
        verify(notas, never()).saveAndFlush(any());
    }

    @ParameterizedTest @ValueSource(strings = { "1", "100" })
    void creaEnLimitesYAuditaResultadoPersistido(String valor) {
        preparar();
        when(notas.findByMatriculaIdAndRubroId(1, 2)).thenReturn(Optional.empty());
        when(notas.saveAndFlush(any())).thenAnswer(i -> i.getArgument(0));
        var resultado = servicio.asignarNota(request(valor), false);
        assertEquals(new BigDecimal(valor), resultado.nota());
        verify(auditoria).registrar(null, resultado);
    }

    @Test void modificacionConservaSnapshotAnterior() {
        preparar();
        NotaRubro existente = new NotaRubro();
        existente.setMatriculaId(1); existente.setRubroId(2); existente.setNota(new BigDecimal("40"));
        when(notas.findByMatriculaIdAndRubroId(1, 2)).thenReturn(Optional.of(existente));
        when(notas.saveAndFlush(any())).thenAnswer(i -> i.getArgument(0));
        var resultado = servicio.asignarNota(request("85"), true);
        verify(auditoria).registrar(argThat(previa -> previa.nota().compareTo(new BigDecimal("40")) == 0), eq(resultado));
        assertEquals(new BigDecimal("85"), resultado.nota());
    }

    @Test void falloAlGuardarNoPublicaExitoEnBitacora() {
        preparar();
        when(notas.findByMatriculaIdAndRubroId(1, 2)).thenReturn(Optional.empty());
        when(notas.saveAndFlush(any())).thenThrow(new IllegalStateException("fallo SQL"));
        assertThrows(IllegalStateException.class, () -> servicio.asignarNota(request("50"), false));
        verifyNoInteractions(auditoria);
    }

    private AsignarNotaRequest request(String valor) { return new AsignarNotaRequest(1, 2, new BigDecimal(valor)); }
    private void preparar() {
        Matricula matricula = new Matricula();
        matricula.setGrupoId(3);
        when(matriculas.findById(1)).thenReturn(Optional.of(matricula));
        DesgloseRubro rubro = mock(DesgloseRubro.class);
        when(rubro.getGrupoId()).thenReturn(3);
        when(rubros.findById(2)).thenReturn(Optional.of(rubro));
    }
}
