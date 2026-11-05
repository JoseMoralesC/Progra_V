package cr.ac.cuc.nuevoavatar.persona5.notas;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import cr.ac.cuc.nuevoavatar.persona5.academico.GrupoRepository;
import cr.ac.cuc.nuevoavatar.persona5.matricula.Matricula;
import cr.ac.cuc.nuevoavatar.persona5.matricula.MatriculaRepository;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.*;
import org.springframework.web.server.ResponseStatusException;

// Dos instancias de servicio y dos transacciones. Repositorios/bloqueo simulados;
// no certifica la ejecución de sp_getapplock contra SQL Server.
class ConcurrenciaNotasTest {
    @Test void primeraNotaConfirmadaImpideReemplazarElDesgloseConcurrente() throws Exception {
        var f = new Escenario();
        f.enParalelo(() -> f.uno.asignarNota(f.nota(), false), () -> f.dos.cargarDesglose(f.desglose()), 409);
        assertNotNull(f.guardada);
        assertEquals(2, f.rubro.getId());
    }

    @Test void reemplazoConfirmadoRechazaLaNotaDelRubroQueYaNoExiste() throws Exception {
        var f = new Escenario();
        f.enParalelo(() -> f.uno.cargarDesglose(f.desglose()), () -> f.dos.asignarNota(f.nota(), false), 422);
        assertNull(f.guardada);
        assertEquals(20, f.rubro.getId());
    }

    @Test void dosPostConcurrentesNoSobrescribenNiDuplicanLaPrimeraNota() throws Exception {
        var f = new Escenario();
        f.enParalelo(() -> f.uno.asignarNota(f.nota(), false), () -> f.dos.asignarNota(f.nota(), false), 409);
        verify(f.notas, times(1)).saveAndFlush(any());
    }

    private static class Escenario {
        final DesgloseRubroRepository rubros = mock(DesgloseRubroRepository.class);
        final NotaRubroRepository notas = mock(NotaRubroRepository.class);
        final AuditoriaNotas auditoria = mock(AuditoriaNotas.class);
        final ReentrantLock lock = new ReentrantLock();
        final CountDownLatch primeroEscribio = new CountDownLatch(1), segundoEspera = new CountDownLatch(1), confirmar = new CountDownLatch(1);
        final AtomicInteger adquisiciones = new AtomicInteger();
        volatile DesgloseRubro rubro = rubro(2);
        volatile NotaRubro guardada;
        final NotasService uno, dos;

        Escenario() {
            var grupos = mock(GrupoRepository.class);
            var matriculas = mock(MatriculaRepository.class);
            when(grupos.existsById(7)).thenReturn(true);
            Matricula matricula = new Matricula(); matricula.setGrupoId(7);
            when(matriculas.findById(1)).thenReturn(Optional.of(matricula));
            when(rubros.findById(anyInt())).thenAnswer(i -> rubro != null && rubro.getId().equals(i.getArgument(0)) ? Optional.of(rubro) : Optional.empty());
            when(rubros.findByGrupoId(7)).thenAnswer(i -> List.of(rubro));
            when(notas.existsByRubroIdIn(anyList())).thenAnswer(i -> guardada != null);
            when(notas.findByMatriculaIdAndRubroId(1, 2)).thenAnswer(i -> Optional.ofNullable(guardada));
            when(notas.saveAndFlush(any())).thenAnswer(i -> { guardada = i.getArgument(0); return guardada; });
            doAnswer(i -> { rubro = null; return null; }).when(rubros).deleteByGrupoId(7);
            when(rubros.saveAll(anyList())).thenAnswer(i -> { rubro = rubro(20); return List.of(rubro); });
            doAnswer(i -> esperarConfirmacion()).when(auditoria).registrar(any(), any());
            doAnswer(i -> esperarConfirmacion()).when(auditoria).registrarDesglose(anyInt(), anyList(), anyList());
            uno = new NotasService(rubros, notas, matriculas, grupos, auditoria, bloqueo());
            dos = new NotasService(rubros, notas, matriculas, grupos, auditoria, bloqueo());
        }

        Object esperarConfirmacion() throws Exception {
            primeroEscribio.countDown();
            if (!confirmar.await(5, TimeUnit.SECONDS)) throw new AssertionError("No se permitió confirmar");
            return null;
        }

        BloqueoGrupoNotas bloqueo() {
            var bloqueo = mock(BloqueoGrupoNotas.class);
            doAnswer(i -> {
                assertTrue(TransactionSynchronizationManager.isActualTransactionActive());
                if (adquisiciones.incrementAndGet() == 2) segundoEspera.countDown();
                assertTrue(lock.tryLock(5, TimeUnit.SECONDS));
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override public void afterCompletion(int status) { lock.unlock(); }
                });
                return null;
            }).when(bloqueo).adquirir(7);
            return bloqueo;
        }

        void enParalelo(Runnable primera, Runnable segunda, int conflicto) throws Exception {
            var tx = new TransactionTemplate(new TransaccionLocal());
            try (var pool = Executors.newVirtualThreadPerTaskExecutor()) {
                var a = pool.submit(() -> tx.executeWithoutResult(s -> primera.run()));
                assertTrue(primeroEscribio.await(5, TimeUnit.SECONDS));
                var b = pool.submit(() -> tx.executeWithoutResult(s -> segunda.run()));
                try {
                    assertTrue(segundoEspera.await(5, TimeUnit.SECONDS));
                    assertFalse(b.isDone(), "La segunda escritura no debe terminar antes del commit de la primera");
                } finally { confirmar.countDown(); }
                a.get(5, TimeUnit.SECONDS);
                var error = assertThrows(ExecutionException.class, () -> b.get(5, TimeUnit.SECONDS));
                assertEquals(conflicto, ((ResponseStatusException)error.getCause()).getStatusCode().value());
            }
        }

        AsignarNotaRequest nota() { return new AsignarNotaRequest(1, 2, new BigDecimal("85")); }
        CargarDesgloseRequest desglose() { return new CargarDesgloseRequest(7,
            List.of(new CargarDesgloseRequest.RubroRequest("Proyecto", new BigDecimal("100")))); }
        static DesgloseRubro rubro(int id) {
            var r = mock(DesgloseRubro.class); when(r.getId()).thenReturn(id); when(r.getGrupoId()).thenReturn(7);
            when(r.getNombre()).thenReturn("Proyecto"); when(r.getPorcentaje()).thenReturn(new BigDecimal("100")); return r;
        }
    }

    private static class TransaccionLocal extends AbstractPlatformTransactionManager {
        @Override protected Object doGetTransaction() { return new Object(); }
        @Override protected void doBegin(Object transaction, TransactionDefinition definition) { }
        @Override protected void doCommit(DefaultTransactionStatus status) { }
        @Override protected void doRollback(DefaultTransactionStatus status) { }
    }
}
