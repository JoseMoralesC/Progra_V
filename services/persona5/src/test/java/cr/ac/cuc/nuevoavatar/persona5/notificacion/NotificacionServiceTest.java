package cr.ac.cuc.nuevoavatar.persona5.notificacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;

class NotificacionServiceTest {

    @Test
    void enviaCorreoHtml() {
        JavaMailSender sender = mock(JavaMailSender.class);
        when(sender.createMimeMessage()).thenReturn(
            new MimeMessage(Session.getInstance(new Properties()))
        );

        NotificacionService servicio = new NotificacionService(
            sender,
            "no-reply@nuevoavatar.local"
        );

        NotificacionResponse response = servicio.notificar(
            new NotificacionRequest(
                " estudiante@cuc.cr ",
                "Aviso",
                "<strong>Hola</strong>"
            )
        );

        assertEquals("estudiante@cuc.cr", response.email());
        assertEquals("ENVIADA", response.estado());
        verify(sender).send(any(MimeMessage.class));
    }
}
