using NuevoAvatar.Periodo.Nuevo.Entities;
using NuevoAvatar.Periodo.Nuevo.Repository;

namespace NuevoAvatar.Periodo.Nuevo.Services;

public class PeriodoService : IPeriodoService
{
    private readonly IPeriodoRepository _repository;
    private readonly IPeriodoValidator _validator;

    public PeriodoService(
        IPeriodoRepository repository,
        IPeriodoValidator validator)
    {
        _repository = repository;
        _validator = validator;
    }

    public async Task<IEnumerable<PeriodoRequest>> ObtenerTodosAsync()
    {
        return await _repository.ObtenerTodosAsync();
    }

    public async Task<PeriodoRequest?> ObtenerPorIdAsync(int periodoId)
    {
        if (periodoId <= 0)
            throw new ArgumentException(
                "El identificador del periodo debe ser mayor que cero.");

        return await _repository.ObtenerPorIdAsync(periodoId);
    }

    public async Task<int> CrearAsync(PeriodoRequest request)
    {
        _validator.Validar(request);

        return await _repository.CrearAsync(request);
    }

    public async Task<bool> ModificarAsync(
        int periodoId,
        PeriodoRequest request)
    {
        if (periodoId <= 0)
            throw new ArgumentException(
                "El identificador del periodo debe ser mayor que cero.");

        _validator.Validar(request);

        return await _repository.ModificarAsync(
            periodoId,
            request);
    }

    public async Task<bool> EliminarAsync(int periodoId)
    {
        if (periodoId <= 0)
            throw new ArgumentException(
                "El identificador del periodo debe ser mayor que cero.");

        return await _repository.EliminarAsync(periodoId);
    }
}