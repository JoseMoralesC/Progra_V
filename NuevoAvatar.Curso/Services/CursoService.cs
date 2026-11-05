using NuevoAvatar.Curso.Entities;
using NuevoAvatar.Curso.Repository;
using CursoEntity = NuevoAvatar.Curso.Entities.Curso;

namespace NuevoAvatar.Curso.Services;

public sealed class CursoService(
    ICursoRepository repository,
    CursoValidator validator, NuevoAvatar.Integracion.DatosRemotos datos) : ICursoService
{
    public Task<IReadOnlyList<CursoEntity>> GetAllAsync() =>
        repository.GetAllAsync();

    public Task<IReadOnlyList<CursoEntity>> GetFilteredAsync(int? carreraId, byte? nivel)
    {
        if (carreraId is <= 0) throw new CursoValidationException(["CarreraId debe ser mayor que cero."]);
        if (nivel is < 1 or > 12) throw new CursoValidationException(["El nivel debe estar entre 1 y 12."]);
        return repository.GetFilteredAsync(carreraId, nivel);
    }

    public Task<CursoEntity?> GetByIdAsync(int id)
    {
        ValidateId(id, "Id");

        return repository.GetByIdAsync(id);
    }

    public Task<IReadOnlyList<CursoEntity>> GetByCarreraIdAsync(
        int carreraId)
    {
        ValidateId(carreraId, "CarreraId");

        return repository.GetByCarreraIdAsync(carreraId);
    }

    public async Task<int> CreateAsync(CursoRequest? curso)
    {
        Validate(curso);
        await ExigirCarrera(curso!.CarreraId);

        try
        {
            return await repository.CreateAsync(curso!);
        }
        catch (CursoRepositoryException exception)
        {
            throw MapRepositoryException(exception);
        }
    }

    public async Task<bool> UpdateAsync(
        int id,
        CursoRequest? curso)
    {
        ValidateId(id, "Id");
        Validate(curso);
        await ExigirCarrera(curso!.CarreraId);

        try
        {
            return await repository.UpdateAsync(id, curso!);
        }
        catch (CursoRepositoryException exception)
        {
            throw MapRepositoryException(exception);
        }
    }

    public async Task<bool> DeleteAsync(int id)
    {
        ValidateId(id, "Id");

        await datos.SinReferencias("Curso", "Curso", id);
        try
        {
            return await repository.DeleteAsync(id);
        }
        catch (CursoRepositoryException exception)
        {
            throw MapRepositoryException(exception);
        }
    }

    private async Task ExigirCarrera(int carreraId)
    {
        try { await datos.Exigir("Carrera", "Carrera", "CarreraId", carreraId); }
        catch (ArgumentException) { throw new CarreraInexistenteException(carreraId); }
    }

    private void Validate(CursoRequest? curso)
    {
        if (curso is null)
        {
            throw new CursoValidationException(
                ["El curso es requerido."]);
        }

        var errors = validator.Validate(curso);

        if (errors.Count > 0)
        {
            throw new CursoValidationException(errors);
        }
    }

    private static void ValidateId(int id, string fieldName)
    {
        if (id <= 0)
        {
            throw new CursoValidationException(
                [$"{fieldName} debe ser mayor que cero."]);
        }
    }

    private static Exception MapRepositoryException(
        CursoRepositoryException exception) =>
        exception.Error switch
        {
            CursoRepositoryError.CarreraNotFound =>
                new CarreraInexistenteException(exception.Id),

            CursoRepositoryError.CursoReferenced =>
                new CursoReferenciadoException(exception.Id),

            _ => new InvalidOperationException(
                "Se produjo un error de persistencia no contemplado.",
                exception)
        };
}

public sealed class CursoValidationException(
    IReadOnlyList<string> errors)
    : Exception("La solicitud contiene datos inválidos.")
{
    public IReadOnlyList<string> Errors { get; } = errors;
}

public sealed class CarreraInexistenteException(int carreraId)
    : Exception($"No existe la carrera {carreraId}.");

public sealed class CursoReferenciadoException(int cursoId)
    : Exception($"El curso {cursoId} está relacionado con otros registros.");
