package cr.ac.cuc.nuevoavatar.persona2.carrera;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record CarreraRequest(
    @NotBlank
    @Pattern(
        regexp = "[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+",
        message = "El nombre solo puede contener letras y espacios"
    )
    String nombre,

    @NotNull @Positive Integer institucionId,
    @NotNull @Positive Integer directorProfesorId
) {
}