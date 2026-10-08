package cr.ac.cuc.nuevoavatar.persona4.academico;

public record ListadoEstudianteResponse(
        String tipoIdentificacion,
        String identificacion,
        String nombreCompleto,
        String carrera,
        String curso,
        Integer grupo) {
}
