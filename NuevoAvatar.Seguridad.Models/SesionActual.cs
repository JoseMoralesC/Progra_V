namespace NuevoAvatar.Seguridad.Models;

public sealed record SesionActual(
    string UsuarioID,
    string Nombre,
    string Rol,
    string TipoIdentificacion,
    string Identificacion,
    string? EstudianteId,
    int? ProfesorId);
