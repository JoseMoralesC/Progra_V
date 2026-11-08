package cr.ac.cuc.nuevoavatar.persona4.prematricula;

import java.util.List;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/** Campos editables de una solicitud propia. La identidad se deriva de /sesion. */
public record PrematriculaPropiaRequest(
        @NotNull Integer carreraId,
        @NotEmpty List<@NotNull Integer> cursos,
        String observaciones,
        @NotNull Integer periodoId) {
}
