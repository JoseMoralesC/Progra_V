package cr.ac.cuc.nuevoavatar.persona2.institucion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record InstitucionRequest(
    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(
        regexp = "[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+",
        message = "El nombre solo puede contener letras y espacios"
    )
    String nombre
) {
}