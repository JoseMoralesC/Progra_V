package cr.ac.cuc.nuevoavatar.persona2.usuario;

public record UsuarioResponse(
    String email,
    String tipoIdentificacion,
    String identificacion,
    String nombre,
    String idRol
) {
    public static UsuarioResponse desde(Usuario usuario) {
        return new UsuarioResponse(
            usuario.getEmail(),
            usuario.getTipoIdentificacion(),
            usuario.getIdentificacion(),
            usuario.getNombre(),
            usuario.getIdRol()
        );
    }
}