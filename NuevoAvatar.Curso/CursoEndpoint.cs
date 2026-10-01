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
            async (ICursoService service) =>
                Results.Ok(await service.GetAllAsync()))
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
                ICursoService service) =>
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
                ICursoService service) =>
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

                var updated =
                    await service.UpdateAsync(id, curso);

                if (!updated)
                {
                    return Results.NotFound();
                }

                var updatedCurso =
                    await service.GetByIdAsync(id);

                return updatedCurso is null
                    ? Results.NotFound()
                    : Results.Ok(updatedCurso);
            })
            .WithName("UpdateCurso")
            .WithSummary("Modifica un curso.");

        group.MapDelete(
            "/{id:int}",
            async (
                int id,
                ICursoService service) =>
                await service.DeleteAsync(id)
                    ? Results.NoContent()
                    : Results.NotFound())
            .WithName("DeleteCurso")
            .WithSummary("Elimina un curso.");

        return endpoints;
    }
}