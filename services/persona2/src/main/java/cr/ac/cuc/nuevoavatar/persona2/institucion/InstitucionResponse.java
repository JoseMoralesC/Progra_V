package cr.ac.cuc.nuevoavatar.persona2.institucion;

public record InstitucionResponse(Integer id, String nombre) {

    public static InstitucionResponse desde(Institucion institucion) {
        return new InstitucionResponse(
            institucion.getId(),
            institucion.getNombre()
        );
    }
}
