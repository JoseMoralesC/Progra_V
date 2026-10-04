package cr.ac.cuc.nuevoavatar.persona2.grupo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record GrupoRequest(
    @NotNull @Positive Integer numeroGrupo,
    @NotNull @Positive Integer cursoId,
    @NotNull @Positive Integer profesorId,
    @NotBlank @Size(max = 100) String horario,
    @NotNull @Positive Integer cupo,
    @NotNull @Positive Integer periodoId
) {
}
