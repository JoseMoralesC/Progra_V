using NuevoAvatar.Seguridad.Models;

namespace NuevoAvatar.Seguridad.Abstract;

public interface IIdentidadSesionService
{
    Task<SesionActual?> Obtener(string email);
}
