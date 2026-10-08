using GrupoEntity = NuevoAvatar.Grupo.Nuevo.Entities.Grupo;
using NuevoAvatar.Grupo.Nuevo.Entities;
using NuevoAvatar.Grupo.Nuevo.Repository;

namespace NuevoAvatar.Grupo.Nuevo.Services;

public class GrupoService : IGrupoService
{
    private readonly IGrupoRepository _repository;

    public GrupoService(IGrupoRepository repository)
    {
        _repository = repository;
    }

    public async Task<int> CrearAsync(GrupoRequest grupo)
    {
        var errors = GrupoValidator.Validate(grupo);

        if (errors.Count > 0)
            throw new ArgumentException(string.Join(" ", errors));

        return await _repository.CrearAsync(grupo);
    }

    public async Task<bool> ModificarAsync(
        int grupoId,
        GrupoRequest grupo)
    {
        var errors = GrupoValidator.Validate(grupo);

        if (errors.Count > 0)
            throw new ArgumentException(string.Join(" ", errors));

        return await _repository.ModificarAsync(grupoId, grupo);
    }

    public Task<bool> EliminarAsync(int grupoId)
    {
        return _repository.EliminarAsync(grupoId);
    }

    public Task<IEnumerable<GrupoEntity>> ObtenerTodosAsync()
    {
        return _repository.ObtenerTodosAsync();
    }

    public Task<GrupoEntity?> ObtenerPorIdAsync(int grupoId)
    {
        return _repository.ObtenerPorIdAsync(grupoId);
    }
}