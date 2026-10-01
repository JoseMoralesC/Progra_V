package cr.ac.cuc.nuevoavatar.persona5.matricula;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MatriculaRequest(
    @NotBlank @Size(max = 30) String identificacionEstudiante,
    @NotNull @Positive Integer cursoId,
    @NotNull @Positive Integer grupoId,
    @NotNull @Positive Integer periodoId
) {
}
