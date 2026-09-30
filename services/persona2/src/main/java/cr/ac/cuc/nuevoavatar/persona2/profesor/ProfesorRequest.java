package cr.ac.cuc.nuevoavatar.persona2.profesor;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ProfesorRequest(
    @NotBlank String tipoIdentificacion,
    @NotBlank String identificacion,
    @NotBlank @Email String email,

    @NotBlank
    @Pattern(
        regexp = "[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+",
        message = "El nombre solo puede contener letras y espacios"
    )
    String nombreCompleto,

    @NotNull LocalDate fechaNacimiento,

    @NotEmpty(message = "Debe indicar al menos un teléfono")
    List<@NotBlank String> telefonos
) {
}