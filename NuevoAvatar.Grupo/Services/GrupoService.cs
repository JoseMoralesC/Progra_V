using GrupoEntity = NuevoAvatar.Grupo.Nuevo.Entities.Grupo;
using NuevoAvatar.Grupo.Nuevo.Entities;
using NuevoAvatar.Grupo.Nuevo.Repository;

namespace NuevoAvatar.Grupo.Nuevo.Services;

public class GrupoService : IGrupoService
{
    private readonly IGrupoRepository _repository;
    private readonly NuevoAvatar.Integracion.DatosRemotos datos;

    public GrupoService(IGrupoRepository repository, NuevoAvatar.Integracion.DatosRemotos datos)
    {
        _repository = repository;
        this.datos = datos;
    }

    public async Task<int> CrearAsync(GrupoRequest grupo)
    {
        var errors = GrupoValidator.Validate(grupo);

        if (errors.Count > 0)
            throw new ArgumentException(string.Join(" ", errors));

        await ValidarReferencias(grupo);
        return await _repository.CrearAsync(grupo);
    }

    public async Task<bool> ModificarAsync(
        int grupoId,
        GrupoRequest grupo)
    {
        var errors = GrupoValidator.Validate(grupo);

        if (errors.Count > 0)
            throw new ArgumentException(string.Join(" ", errors));

        await ValidarReferencias(grupo);
        return await _repository.ModificarAsync(grupoId, grupo);
    }

    private async Task ValidarReferencias(GrupoRequest grupo) {
        await datos.Exigir("Curso","Curso","CursoId",grupo.CursoId);
        await datos.Exigir("Profesor","Profesor","ProfesorId",grupo.ProfesorId);
        await datos.Exigir("Periodo","Periodo","PeriodoId",grupo.PeriodoId);
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