package cr.ac.cuc.nuevoavatar.persona5.notificacion;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class NotificacionService {

    private final JavaMailSender mailSender;
    private final String remitente;

    public NotificacionService(
            JavaMailSender mailSender,
            @Value("${notificacion.mail.from}") String remitente) {
        this.mailSender = mailSender;
        this.remitente = remitente;
    }

    public NotificacionResponse notificar(NotificacionRequest request) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, "UTF-8");
            helper.setFrom(remitente);
            helper.setTo(request.email().trim());
            helper.setSubject(request.asunto().trim());
            helper.setText(request.cuerpoHtml(), true);
            mailSender.send(mensaje);
            return new NotificacionResponse(request.email().trim(), "ENVIADA");
        } catch (MessagingException | MailException ex) {
            throw new ResponseStatusException(
                HttpStatus.BAD_GATEWAY,
                "No fue posible enviar la notificacion",
                ex
            );
        }
    }
}
