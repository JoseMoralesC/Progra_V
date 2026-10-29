package cr.ac.cuc.nuevoavatar.persona5.seguridad;

public record SesionActual(String usuarioID, String nombre, String rol,
        String tipoIdentificacion, String identificacion, String estudianteId, Integer profesorId) {
}
