using GrupoEntity = NuevoAvatar.Grupo.Nuevo.Entities.Grupo;
using NuevoAvatar.Grupo.Nuevo.Entities;

namespace NuevoAvatar.Grupo.Nuevo.Repository;

public interface IGrupoRepository
{
    Task<int> CrearAsync(GrupoRequest grupo);

    Task<bool> ModificarAsync(int grupoId, GrupoRequest grupo);

    Task<bool> EliminarAsync(int grupoId);

    Task<IEnumerable<GrupoEntity>> ObtenerTodosAsync();

    Task<GrupoEntity?> ObtenerPorIdAsync(int grupoId);
}