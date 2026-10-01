using NuevoAvatar.Periodo.Nuevo.Entities;

namespace NuevoAvatar.Periodo.Nuevo.Repository;

public interface IPeriodoRepository
{
    Task<IEnumerable<PeriodoRequest>> ObtenerTodosAsync();

    Task<PeriodoRequest?> ObtenerPorIdAsync(int periodoId);

    Task<int> CrearAsync(PeriodoRequest request);

    Task<bool> ModificarAsync(
        int periodoId,
        PeriodoRequest request);

    Task<bool> EliminarAsync(int periodoId);
}