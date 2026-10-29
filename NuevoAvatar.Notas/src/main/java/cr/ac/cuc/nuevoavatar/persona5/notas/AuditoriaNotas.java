package cr.ac.cuc.nuevoavatar.persona5.notas;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cr.ac.cuc.nuevoavatar.persona5.seguridad.AutorizacionInterceptor;
import cr.ac.cuc.nuevoavatar.persona5.seguridad.SeguridadClient;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class AuditoriaNotas {
    private final HttpServletRequest request;
    private final ApplicationEventPublisher eventos;
    private final SeguridadClient seguridad;
    private final ObjectMapper json = new ObjectMapper();

    public AuditoriaNotas(HttpServletRequest request, ApplicationEventPublisher eventos, SeguridadClient seguridad) {
        this.request = request;
        this.eventos = eventos;
        this.seguridad = seguridad;
    }

    public void registrar(NotaResponse anterior, NotaResponse actual) {
        Object token = request.getAttribute(AutorizacionInterceptor.TOKEN_ATTRIBUTE);
        Object usuario = request.getAttribute(AutorizacionInterceptor.USER_ATTRIBUTE);
        if (token == null || usuario == null) throw new IllegalStateException("Falta identidad para auditar la nota");
        try {
            String descripcion = anterior == null
                ? "Alta de nota. JSON nuevo: " + json.writeValueAsString(actual)
                : "Modificacion de nota. JSON anterior: " + json.writeValueAsString(anterior)
                    + ". JSON actual: " + json.writeValueAsString(actual);
            eventos.publishEvent(new NotaConfirmada(token.toString(), usuario.toString(), descripcion));
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("No fue posible preparar la auditoria de la nota", ex);
        }
    }

    // Un rollback no debe dejar una bitacora que afirme una escritura exitosa.
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void confirmar(NotaConfirmada evento) {
        seguridad.registrarBitacora(evento.token(), evento.usuario(), evento.descripcion());
    }

    public record NotaConfirmada(String token, String usuario, String descripcion) { }
}
