package cr.ac.cuc.nuevoavatar.persona4.prematricula;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PrematriculaRequest(
        @NotBlank @Size(max = 30) String identificacionEstudiante,
        @NotNull Integer carreraId,
        @NotEmpty List<@NotNull Integer> cursos,
        String observaciones,
        @NotNull Integer periodoId) {
}
