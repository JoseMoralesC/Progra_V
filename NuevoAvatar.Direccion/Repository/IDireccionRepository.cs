using NuevoAvatar.Direccion.Nuevo.Entities;

namespace NuevoAvatar.Direccion.Nuevo.Repository;

public interface IDireccionRepository
{
    Task<IEnumerable<Provincia>> ObtenerProvinciasAsync();

    Task<IEnumerable<Canton>> ObtenerCantonesAsync(
        int provinciaId);

    Task<IEnumerable<Distrito>> ObtenerDistritosAsync(
        int provinciaId,
        int cantonId);
}