package cr.ac.cuc.nuevoavatar.persona2.usuario;

public record PoliticaContrasenaResponse(
    int minLength,
    int maxLength,
    boolean requireUppercase,
    boolean requireNumber,
    int maxBcryptUtf8Bytes
) {
}
