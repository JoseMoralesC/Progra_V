package cr.ac.cuc.nuevoavatar.persona5.notas;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import cr.ac.cuc.nuevoavatar.persona5.seguridad.AutorizacionInterceptor;
import cr.ac.cuc.nuevoavatar.persona5.seguridad.SeguridadClient;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.event.TransactionalEventListenerFactory;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

class AuditoriaNotasTest {
    @Test void enviaAntesYDespuesSoloTrasConfirmarTransaccion() {
        verificar(false);
    }

    @Test void rollbackNoEnviaBitacoraDeExito() {
        verificar(true);
    }

    @Test void reemplazoDeRubrosAuditaAntesYDespuesTrasCommit() { verificarDesglose(false, false); }
    @Test void altaDeRubrosAuditaJsonNuevoTrasCommit() { verificarDesglose(false, true); }
    @Test void rollbackDeRubrosNoEnviaBitacoraDeExito() { verificarDesglose(true, false); }

    private void verificarDesglose(boolean rollback, boolean alta) {
        var request = new MockHttpServletRequest();
        request.setAttribute(AutorizacionInterceptor.TOKEN_ATTRIBUTE, "Bearer prueba");
        request.setAttribute(AutorizacionInterceptor.USER_ATTRIBUTE, "profesor@cuc.ac.cr");
        var seguridad = mock(SeguridadClient.class);
        try (var context = new AnnotationConfigApplicationContext()) {
            context.registerBean(TransactionalEventListenerFactory.class);
            context.registerBean(AuditoriaNotas.class, () -> new AuditoriaNotas(request, context, seguridad));
            context.refresh();
            new TransactionTemplate(new TransaccionLocalFalsa()).executeWithoutResult(status -> {
                context.getBean(AuditoriaNotas.class).registrarDesglose(7,
                    alta ? List.of() : List.of(new RubroResponse(1, 7, "Anterior", new BigDecimal("100"))),
                    List.of(new RubroResponse(2, 7, "Proyecto", new BigDecimal("100"))));
                verifyNoInteractions(seguridad);
                if (rollback) status.setRollbackOnly();
            });
            if (rollback) verifyNoInteractions(seguridad);
            else verify(seguridad).registrarBitacora(eq("Bearer prueba"), eq("profesor@cuc.ac.cr"),
                argThat(texto -> texto.contains("desglose") && texto.contains("grupoId") && texto.contains("Proyecto")
                    && (alta ? texto.contains("JSON nuevo:") : texto.contains("JSON anterior:") && texto.contains("Anterior") && texto.contains("JSON actual:"))));
        }
    }

    private void verificar(boolean rollback) {
        var request = new MockHttpServletRequest();
        request.setAttribute(AutorizacionInterceptor.TOKEN_ATTRIBUTE, "Bearer prueba");
        request.setAttribute(AutorizacionInterceptor.USER_ATTRIBUTE, "profesor@cuc.ac.cr");
        var seguridad = mock(SeguridadClient.class);
        try (var context = new AnnotationConfigApplicationContext()) {
            context.registerBean(TransactionalEventListenerFactory.class);
            context.registerBean(AuditoriaNotas.class, () -> new AuditoriaNotas(request, context, seguridad));
            context.refresh();
            new TransactionTemplate(new TransaccionLocalFalsa()).executeWithoutResult(status -> {
                context.getBean(AuditoriaNotas.class).registrar(nota("40"), nota("85"));
                verifyNoInteractions(seguridad);
                if (rollback) status.setRollbackOnly();
            });
            if (rollback) verifyNoInteractions(seguridad);
            else verify(seguridad).registrarBitacora(eq("Bearer prueba"), eq("profesor@cuc.ac.cr"),
                argThat(texto -> texto.contains("JSON anterior:") && texto.contains("JSON actual:")
                    && texto.contains("40") && texto.contains("85") && !texto.contains("Bearer")));
        }
    }

    private NotaResponse nota(String valor) {
        return new NotaResponse(1, 2, 3, "Proyecto", new BigDecimal("100"), new BigDecimal(valor));
    }

    // Comprueba el ciclo de eventos transaccionales sin abrir una base de datos.
    private static class TransaccionLocalFalsa extends AbstractPlatformTransactionManager {
        @Override protected Object doGetTransaction() { return new Object(); }
        @Override protected void doBegin(Object transaction, TransactionDefinition definition) { }
        @Override protected void doCommit(DefaultTransactionStatus status) { }
        @Override protected void doRollback(DefaultTransactionStatus status) { }
    }
}
