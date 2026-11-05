package cr.ac.cuc.nuevoavatar.persona5.notificacion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.junit.jupiter.api.Test;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.server.ResponseStatusException;

class NotificacionServiceTest {
    @Test
    void rechazaDestinatariosInvalidos() {
        var s = new NotificacionService(mock(DatosRemotos.class), mock(NotificacionRepository.class));
        assertThrows(
            ResponseStatusException.class,
            () -> s.notificar(new NotificacionRequest(null, List.of("incorrecto"), "Aviso", "<b>Hola</b>"), "admin")
        );
    }

    @Test
    void sanitizacionEliminaScriptsYAtributosPeligrosos() {
        String limpio = Jsoup.clean("<p onclick='x()'>Hola<script>alert(1)</script></p>", Safelist.relaxed());
        assertFalse(limpio.contains("script"));
        assertFalse(limpio.contains("onclick"));
        assertTrue(limpio.contains("Hola"));
    }

    @Test
    void registraErrorEnHistorialCuandoFaltanParametrosSmtp() {
        DatosRemotos datos = mock(DatosRemotos.class);
        NotificacionRepository repo = mock(NotificacionRepository.class);
        when(datos.buscar(eq("Parametro"), eq("parametro"), eq("id_parametro"), anyString()))
            .thenReturn(Optional.empty());
        var s = new NotificacionService(datos, repo);

        ResponseStatusException ex = assertThrows(
            ResponseStatusException.class,
            () -> s.notificar(new NotificacionRequest(null, List.of("usuario@cuc.cr"), "Aviso", "<b>Hola</b>"), "admin")
        );
        assertEquals(503, ex.getStatusCode().value());
        verify(repo).save(argThat(n -> "ERROR".equals(n.getEstado()) && n.getError() != null));
    }

    @Test
    void informaEnvioRealizadoAunqueFalleGuardadoDeHistorial() {
        DatosRemotos datos = mock(DatosRemotos.class);
        NotificacionRepository repo = mock(NotificacionRepository.class);
        for (var entry : Map.of(
                "MAILHOST", "smtp.cuc.ac.cr",
                "MAILPORT", "587",
                "MAILUSER", "notificaciones@cuc.ac.cr",
                "MAILPASS", "secreto",
                "MAILFROM", "notificaciones@cuc.ac.cr",
                "MAILTLS", "true").entrySet()) {
            when(datos.buscar("Parametro", "parametro", "id_parametro", entry.getKey()))
                .thenReturn(Optional.of(Map.of("valor", entry.getValue())));
        }
        when(repo.save(any(Notificacion.class))).thenThrow(new RuntimeException("Fallo SQL"));

        JavaMailSenderImpl sender = mock(JavaMailSenderImpl.class);
        when(sender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        NotificacionService s = new NotificacionService(datos, repo) {
            @Override
            JavaMailSenderImpl crearSender(Map<String, String> config) {
                return sender;
            }
        };

        NotificacionResponse res = s.notificar(
            new NotificacionRequest(null, List.of("usuario@cuc.cr"), "Aviso", "<b>Hola</b>"),
            "admin"
        );
        assertEquals("ENVIADA_SIN_HISTORIAL", res.estado());
        verify(sender).send(any(MimeMessage.class));
    }

    @Test
    void guardaHistorialEnErrorCuandoFallaTransporteSmtp() {
        DatosRemotos datos = mock(DatosRemotos.class);
        NotificacionRepository repo = mock(NotificacionRepository.class);
        for (var entry : Map.of(
                "MAILHOST", "smtp.cuc.ac.cr",
                "MAILPORT", "587",
                "MAILUSER", "notificaciones@cuc.ac.cr",
                "MAILPASS", "secreto",
                "MAILFROM", "notificaciones@cuc.ac.cr",
                "MAILTLS", "true").entrySet()) {
            when(datos.buscar("Parametro", "parametro", "id_parametro", entry.getKey()))
                .thenReturn(Optional.of(Map.of("valor", entry.getValue())));
        }

        JavaMailSenderImpl sender = mock(JavaMailSenderImpl.class);
        when(sender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        doThrow(new MailSendException("SMTP caído")).when(sender).send(any(MimeMessage.class));
        NotificacionService s = new NotificacionService(datos, repo) {
            @Override
            JavaMailSenderImpl crearSender(Map<String, String> config) {
                return sender;
            }
        };

        ResponseStatusException ex = assertThrows(
            ResponseStatusException.class,
            () -> s.notificar(new NotificacionRequest(null, List.of("usuario@cuc.cr"), "Aviso", "<b>Hola</b>"), "admin")
        );
        assertEquals(502, ex.getStatusCode().value());
        verify(repo).save(argThat(n -> "ERROR".equals(n.getEstado())));
    }
}
