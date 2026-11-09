package cr.ac.cuc.nuevoavatar.persona2.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActualizarPerfilPropioRequest(
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
    String nombre
) {
}
