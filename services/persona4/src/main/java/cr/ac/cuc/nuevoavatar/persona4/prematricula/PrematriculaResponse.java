package cr.ac.cuc.nuevoavatar.persona4.prematricula;

import java.util.List;

public record PrematriculaResponse(
        Integer id,
        String identificacionEstudiante,
        Integer carreraId,
        List<Integer> cursos,
        String observaciones,
        Integer periodoId) {
}
