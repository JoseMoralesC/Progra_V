package cr.ac.cuc.nuevoavatar.persona2.usuario;

import java.time.OffsetDateTime;

public record UsuarioResponse(
    String email,
    String tipoIdentificacion,
    String identificacion,
    String nombre,
    String idRol,
    OffsetDateTime fechaModificacion
) {
    public static UsuarioResponse desde(Usuario usuario) {
        return new UsuarioResponse(
            usuario.getEmail(),
            usuario.getTipoIdentificacion(),
            usuario.getIdentificacion(),
            usuario.getNombre(),
            usuario.getIdRol(),
            usuario.getFechaModificacion()
        );
    }
}
