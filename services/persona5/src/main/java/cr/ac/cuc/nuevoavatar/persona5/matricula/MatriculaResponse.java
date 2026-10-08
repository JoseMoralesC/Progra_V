package cr.ac.cuc.nuevoavatar.persona5.matricula;

import java.time.LocalDateTime;

public record MatriculaResponse(
    Integer id,
    String identificacionEstudiante,
    Integer cursoId,
    Integer grupoId,
    Integer periodoId,
    String estado,
    LocalDateTime fechaMatricula
) {
    public static MatriculaResponse desde(Matricula matricula) {
        return new MatriculaResponse(
            matricula.getId(),
            matricula.getIdentificacionEstudiante(),
            matricula.getCursoId(),
            matricula.getGrupoId(),
            matricula.getPeriodoId(),
            matricula.getEstado(),
            matricula.getFechaMatricula()
        );
    }
}
