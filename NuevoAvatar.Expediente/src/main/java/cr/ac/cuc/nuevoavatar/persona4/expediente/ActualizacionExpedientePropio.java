package cr.ac.cuc.nuevoavatar.persona4.expediente;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/** Campos que un estudiante puede modificar en su propio expediente mediante WEB2. */
public record ActualizacionExpedientePropio(
        @NotEmpty List<@NotBlank String> telefonos,
        @NotNull Integer provinciaId,
        @NotNull Integer cantonId,
        @NotNull Integer distritoId,
        @NotBlank String otrasSenas) {
}
