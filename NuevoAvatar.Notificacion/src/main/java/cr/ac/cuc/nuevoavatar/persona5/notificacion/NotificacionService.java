package cr.ac.cuc.nuevoavatar.persona5.notificacion;

import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
import jakarta.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class NotificacionService {
    private static final Pattern EMAIL =
        Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final DatosRemotos datos;
    private final NotificacionRepository repositorio;

    public NotificacionService(
            DatosRemotos datos,
            NotificacionRepository repositorio) {
        this.datos = datos;
        this.repositorio = repositorio;
    }

    public NotificacionResponse notificar(
            NotificacionRequest request,
            String usuario) {
        LinkedHashSet<String> destinatarios = obtenerDestinatarios(request);
        String html = Jsoup.clean(request.cuerpoHtml(), Safelist.relaxed());
        Notificacion historial = new Notificacion();

        try {
            Map<String, String> config = configuracion();
            JavaMailSenderImpl sender = crearSender(config);
            MimeMessage mensaje = sender.createMimeMessage();
            MimeMessageHelper helper =
                new MimeMessageHelper(mensaje, "UTF-8");

            helper.setFrom(config.get("MAILFROM"));
            helper.setTo(destinatarios.toArray(String[]::new));
            helper.setSubject(request.asunto().trim());
            helper.setText(html, true);
            sender.send(mensaje);

            String correos = String.join(",", destinatarios);
            historial.datos(
                correos, request.asunto().trim(), html,
                usuario, "ENVIADA", null
            );
            repositorio.save(historial);
            return new NotificacionResponse(correos, "ENVIADA");
        } catch (ResponseStatusException exception) {
            throw exception;
        } catch (Exception exception) {
            historial.datos(
                String.join(",", destinatarios),
                request.asunto().trim(),
                html,
                usuario,
                "ERROR",
                "No fue posible enviar la notificación"
            );
            repositorio.save(historial);
            throw new ResponseStatusException(
                HttpStatus.BAD_GATEWAY,
                "No fue posible enviar la notificación"
            );
        }
    }

    private LinkedHashSet<String> obtenerDestinatarios(
            NotificacionRequest request) {
        LinkedHashSet<String> resultado = new LinkedHashSet<>();

        agregarDestinatario(resultado, request.email());
        if (request.destinatarios() != null) {
            for (String destinatario : request.destinatarios()) {
                agregarDestinatario(resultado, destinatario);
            }
        }

        if (resultado.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_CONTENT,
                "Destinatarios inválidos"
            );
        }
        return resultado;
    }

    private static void agregarDestinatario(
            LinkedHashSet<String> resultado,
            String destinatario) {
        if (destinatario == null || destinatario.isBlank()) {
            return;
        }
        String normalizado =
            destinatario.trim().toLowerCase(Locale.ROOT);
        if (!EMAIL.matcher(normalizado).matches()) {
            throw new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_CONTENT,
                "Destinatarios inválidos"
            );
        }
        resultado.add(normalizado);
    }

    private Map<String, String> configuracion() {
        Map<String, String> resultado = new HashMap<>();
        for (String clave : List.of(
                "MAILHOST", "MAILPORT", "MAILUSER",
                "MAILPASS", "MAILFROM", "MAILTLS")) {
            Map<String, Object> fila = datos.buscar(
                "Parametro", "parametro", "id_parametro", clave
            ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Falta configuración SMTP"
            ));
            String valor = DatosRemotos.texto(fila, "valor");
            if (valor == null || valor.isBlank()) {
                throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Falta configuración SMTP"
                );
            }
            resultado.put(clave, valor);
        }
        return resultado;
    }

    private static JavaMailSenderImpl crearSender(
            Map<String, String> config) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(config.get("MAILHOST"));
        sender.setPort(Integer.parseInt(config.get("MAILPORT")));
        sender.setUsername(config.get("MAILUSER"));
        sender.setPassword(config.get("MAILPASS"));
        sender.getJavaMailProperties().put("mail.smtp.auth", "true");
        sender.getJavaMailProperties().put(
            "mail.smtp.starttls.enable", config.get("MAILTLS")
        );
        return sender;
    }

    public Map<String, Object> historial(
            int page,
            int size,
            String destinatario,
            String estado) {
        if (page < 0 || size < 1 || size > 100) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Paginación inválida"
            );
        }

        Specification<Notificacion> filtros =
            (root, query, builder) -> builder.conjunction();
        if (!destinatario.isBlank()) {
            filtros = filtros.and((root, query, builder) ->
                builder.like(
                    builder.lower(root.get("destinatarios")),
                    "%" + destinatario.toLowerCase(Locale.ROOT) + "%"
                )
            );
        }
        if (!estado.isBlank()) {
            filtros = filtros.and((root, query, builder) ->
                builder.equal(
                    root.get("estado"),
                    estado.toUpperCase(Locale.ROOT)
                )
            );
        }

        var pagina = repositorio.findAll(
            filtros,
            PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "fecha")
            )
        ).map(HistorialResponse::desde);

        return Map.of(
            "items", pagina.getContent(),
            "page", pagina.getNumber(),
            "size", pagina.getSize(),
            "total", pagina.getTotalElements()
        );
    }
}
