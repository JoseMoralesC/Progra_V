using GrupoEntity = NuevoAvatar.Grupo.Nuevo.Entities.Grupo;
using NuevoAvatar.Grupo.Nuevo.Entities;

namespace NuevoAvatar.Grupo.Nuevo.Services;

public interface IGrupoService
{
    Task<int> CrearAsync(GrupoRequest grupo);

    Task<bool> ModificarAsync(int grupoId, GrupoRequest grupo);

    Task<bool> EliminarAsync(int grupoId);

    Task<IEnumerable<GrupoEntity>> ObtenerTodosAsync();

    Task<GrupoEntity?> ObtenerPorIdAsync(int grupoId);
}