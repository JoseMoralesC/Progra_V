package cr.ac.cuc.nuevoavatar.persona2.curso;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record CursoRequest(
    @NotNull @Positive Integer carreraId,
    @NotNull @Min(1) @Max(12) Integer nivel,
    @NotBlank
    @Pattern(
        regexp = "[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+",
        message = "El nombre solo puede contener letras y espacios"
    )
    String nombre
) {
}
