package cr.ac.cuc.nuevoavatar.persona2.curso;

public record CursoResponse(
    Integer id,
    Integer carreraId,
    Integer nivel,
    String nombre
) {
    public static CursoResponse desde(Curso curso) {
        return new CursoResponse(
            curso.getId(),
            curso.getCarreraId(),
            curso.getNivel().intValue(),
            curso.getNombre()
        );
    }
}
