package cr.ac.cuc.nuevoavatar.persona2.grupo;

public record GrupoResponse(
    Integer id,
    Integer numeroGrupo,
    Integer cursoId,
    Integer profesorId,
    String horario,
    Integer cupo,
    Integer periodoId
) {
    public static GrupoResponse desde(Grupo grupo) {
        return new GrupoResponse(
            grupo.getId(),
            grupo.getNumeroGrupo(),
            grupo.getCursoId(),
            grupo.getProfesorId(),
            grupo.getHorario(),
            grupo.getCupo(),
            grupo.getPeriodoId()
        );
    }
}
