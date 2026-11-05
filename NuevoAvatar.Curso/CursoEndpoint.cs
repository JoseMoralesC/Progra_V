using System.Text.Json;
using NuevoAvatar.Curso.Entities;
using NuevoAvatar.Curso.Services;

namespace NuevoAvatar.Curso;

public static class CursoEndpoint
{
    public static IEndpointRouteBuilder MapCursoEndpoints(
        this IEndpointRouteBuilder endpoints)
    {
        var group = endpoints.MapGroup("/curso")
            .WithTags("Curso")
            .AddEndpointFilter<CursoAuthorizationFilter>();

        group.MapGet(
            "",
            async (int? carreraId, byte? nivel, ICursoService service) =>
                Results.Ok(carreraId is null && nivel is null
                    ? await service.GetAllAsync()
                    : await service.GetFilteredAsync(carreraId, nivel)))
            .WithName("GetCursos")
            .WithSummary("Obtiene todos los cursos.");

        group.MapGet(
            "/carrera/{carreraId:int}",
            async (
                int carreraId,
                ICursoService service) =>
                Results.Ok(
                    await service.GetByCarreraIdAsync(carreraId)))
            .WithName("GetCursosPorCarrera")
            .WithSummary("Obtiene los cursos de una carrera.");

        group.MapGet(
            "/{id:int}",
            async (
                int id,
                ICursoService service) =>
            {
                var curso = await service.GetByIdAsync(id);

                return curso is null
                    ? Results.NotFound()
                    : Results.Ok(curso);
            })
            .WithName("GetCursoPorId")
            .WithSummary("Obtiene un curso por su llave primaria.");

        group.MapPost(
            "",
            async (
                CursoRequest? curso,
                ICursoService service,
                HttpContext http) =>
            {
                if (curso is null)
                {
                    return Results.BadRequest(new
                    {
                        errors = new[]
                        {
                            "El curso es requerido."
                        }
                    });
                }

                var id = await service.CreateAsync(curso);
                var creado = await service.GetByIdAsync(id) ?? new Entities.Curso
                {
                    CursoId = id,
                    CarreraId = curso.CarreraId,
                    Nivel = curso.Nivel,
                    Nombre = curso.Nombre.Trim()
                };
                http.Items["bitacora"] = $"Creación de curso: {JsonSerializer.Serialize(creado)}";

                return Results.CreatedAtRoute(
                    "GetCursoPorId",
                    new { id },
                    new { CursoId = id });
            })
            .WithName("CreateCurso")
            .WithSummary("Crea un curso.");

        group.MapPut(
            "/{id:int}",
            async (
                int id,
                CursoRequest? curso,
                ICursoService service,
                HttpContext http) =>
            {
                if (curso is null)
                {
                    return Results.BadRequest(new
                    {
                        errors = new[]
                        {
                            "El curso es requerido."
                        }
                    });
                }

                var anterior = await service.GetByIdAsync(id);
                if (anterior is null)
                {
                    return Results.NotFound();
                }

                var updated =
                    await service.UpdateAsync(id, curso);

                if (!updated)
                {
                    return Results.NotFound();
                }

                var updatedCurso =
                    await service.GetByIdAsync(id);

                if (updatedCurso is null)
                {
                    return Results.NotFound();
                }

                http.Items["bitacora"] = $"Modificación de curso. Anterior: {JsonSerializer.Serialize(anterior)} Actual: {JsonSerializer.Serialize(updatedCurso)}";
                return Results.Ok(updatedCurso);
            })
            .WithName("UpdateCurso")
            .WithSummary("Modifica un curso.");

        group.MapDelete(
            "/{id:int}",
            async (
                int id,
                ICursoService service,
                HttpContext http) =>
            {
                var existente = await service.GetByIdAsync(id);
                if (existente is null)
                {
                    return Results.NotFound();
                }

                if (!await service.DeleteAsync(id))
                {
                    return Results.NotFound();
                }

                http.Items["bitacora"] = $"Eliminación de curso: {JsonSerializer.Serialize(existente)}";
                return Results.NoContent();
            })
            .WithName("DeleteCurso")
            .WithSummary("Elimina un curso.");

        return endpoints;
    }
}
