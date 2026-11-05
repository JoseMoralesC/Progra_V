using NuevoAvatar.Periodo.Nuevo.Entities;

namespace NuevoAvatar.Periodo.Nuevo.Services;

public interface IPeriodoService
{
    Task<IEnumerable<PeriodoRequest>> ObtenerTodosAsync(short? anio = null, string? estado = null);

    Task<PeriodoRequest?> ObtenerPorIdAsync(int periodoId);

    Task<int> CrearAsync(PeriodoRequest request);

    Task<bool> ModificarAsync(
        int periodoId,
        PeriodoRequest request);

    Task<bool> EliminarAsync(int periodoId);
}
