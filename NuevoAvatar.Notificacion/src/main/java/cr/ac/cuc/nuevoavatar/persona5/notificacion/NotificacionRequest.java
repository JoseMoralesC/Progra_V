package cr.ac.cuc.nuevoavatar.persona5.notificacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record NotificacionRequest(
    @Size(max = 150) String email,
    List<@Size(max = 150) String> destinatarios,
    @NotBlank @Size(max = 150) String asunto,
    @NotBlank String cuerpoHtml
) {
}

