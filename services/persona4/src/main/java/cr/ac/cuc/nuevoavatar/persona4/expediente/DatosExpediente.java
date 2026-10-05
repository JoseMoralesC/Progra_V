package cr.ac.cuc.nuevoavatar.persona4.expediente;

import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Datos del expediente; la identificacion coincide con la referencia utilizada por matricula. */
public record DatosExpediente(
        @NotBlank String tipoIdentificacion,
        @NotBlank @Size(max = 30) String identificacion,
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "(?:\\p{L}\\p{M}*| )+",
                message = "El nombre solo admite letras y espacios") String nombreCompleto,
        @NotNull LocalDate fechaNacimiento,
        @NotNull Integer provinciaId,
        @NotNull Integer cantonId,
        @NotNull Integer distritoId,
        @NotBlank String otrasSenas,
        @NotEmpty List<@NotBlank String> telefonos) {
}
