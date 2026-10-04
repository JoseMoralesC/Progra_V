using NuevoAvatar.Direccion.Nuevo.Entities;

namespace NuevoAvatar.Direccion.Nuevo.Services;

public interface IDireccionService
{
    Task<IEnumerable<Provincia>> ObtenerProvinciasAsync();

    Task<IEnumerable<Canton>> ObtenerCantonesAsync(
        int provinciaId);

    Task<IEnumerable<Distrito>> ObtenerDistritosAsync(
        int provinciaId,
        int cantonId);
}