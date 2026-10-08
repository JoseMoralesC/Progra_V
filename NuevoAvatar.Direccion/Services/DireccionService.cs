using NuevoAvatar.Direccion.Nuevo.Entities;
using NuevoAvatar.Direccion.Nuevo.Repository;

namespace NuevoAvatar.Direccion.Nuevo.Services;

public class DireccionService : IDireccionService
{
    private readonly IDireccionRepository _repository;

    public DireccionService(
        IDireccionRepository repository)
    {
        _repository = repository;
    }

    public async Task<IEnumerable<Provincia>> ObtenerProvinciasAsync()
    {
        return await _repository.ObtenerProvinciasAsync();
    }

    public async Task<IEnumerable<Canton>> ObtenerCantonesAsync(
        int provinciaId)
    {
        ValidarId(provinciaId, "provincia");

        return await _repository.ObtenerCantonesAsync(provinciaId);
    }

    public async Task<IEnumerable<Distrito>> ObtenerDistritosAsync(
        int provinciaId,
        int cantonId)
    {
        ValidarId(provinciaId, "provincia");
        ValidarId(cantonId, "cantón");

        return await _repository.ObtenerDistritosAsync(
            provinciaId,
            cantonId);
    }

    private static void ValidarId(
        int id,
        string nombre)
    {
        if (id <= 0)
        {
            throw new ArgumentException(
                $"El identificador de {nombre} debe ser mayor que cero.");
        }
    }
}