package cr.ac.cuc.nuevoavatar.persona4.prematricula;

import java.time.LocalDate;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/** Datos internos con el nivel y la fecha recuperados de la oferta academica. */
public record DatosPrematricula(
        @NotBlank String identificacionEstudiante,
        @NotNull Integer carreraId,
        @NotEmpty List<@NotNull @Valid Curso> cursos,
        String observaciones,
        @NotNull @Valid Periodo periodo) {

    public record Curso(@NotNull Integer cursoId, @NotNull Integer nivel) {
    }

    public record Periodo(@NotNull Integer periodoId, @NotNull LocalDate fechaInicio) {
    }
}
