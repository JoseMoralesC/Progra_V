package cr.ac.cuc.nuevoavatar.persona2.usuario;

import jakarta.validation.constraints.NotBlank;

public record CambiarContrasenaPropiaRequest(
    @NotBlank(message = "La contraseña actual es requerida") String contrasenaActual,
    @NotBlank(message = "La nueva contraseña es requerida") String contrasenaNueva
) {
    @Override
    public String toString() {
        return "CambiarContrasenaPropiaRequest[credenciales ocultas]";
    }
}
