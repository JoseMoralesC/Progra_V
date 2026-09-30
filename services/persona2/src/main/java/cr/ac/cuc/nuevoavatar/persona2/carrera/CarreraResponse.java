package cr.ac.cuc.nuevoavatar.persona2.carrera;

public record CarreraResponse(
    Integer id,
    String nombre,
    Integer institucionId,
    Integer directorProfesorId
) {
    public static CarreraResponse desde(Carrera carrera) {
        return new CarreraResponse(
            carrera.getId(),
            carrera.getNombre(),
            carrera.getInstitucionId(),
            carrera.getDirectorProfesorId()
        );
    }
}