namespace NuevoAvatar.Curso.Services;

public interface ISeguridadService
{
    Task<bool> ValidarTokenAsync(string token);

    Task RegistrarBitacoraAsync(
        string token,
        string usuario,
        string descripcion);
}