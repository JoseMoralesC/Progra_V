package cr.ac.cuc.nuevoavatar.persona2.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CrearUsuarioRequest(
    @NotBlank @Email String email,
    @NotBlank String tipoIdentificacion,
    @NotBlank String identificacion,
    @NotBlank String nombre,
    @NotBlank String idRol,
    @NotBlank String contrasena
) {
}