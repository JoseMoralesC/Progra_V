package cr.ac.cuc.nuevoavatar.persona2.usuario;

import jakarta.validation.constraints.NotBlank;

public record ActualizarUsuarioRequest(
    @NotBlank String tipoIdentificacion,
    @NotBlank String identificacion,
    @NotBlank String nombre,
    @NotBlank String idRol,
    String contrasena
) {
}