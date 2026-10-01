package cr.ac.cuc.nuevoavatar.persona5.notificacion;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NotificacionRequest(
    @NotBlank @Email @Size(max = 150) String email,
    @NotBlank @Size(max = 150) String asunto,
    @NotBlank String cuerpoHtml
) {
}
