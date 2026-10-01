using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Http;
using NuevoAvatar.Grupo.Nuevo.Entities;
using NuevoAvatar.Grupo.Nuevo.Services;

namespace NuevoAvatar.Grupo.Nuevo;

public static class GrupoEndpoint
{
    public static void MapGrupoEndpoints(this WebApplication app)
    {
        app.MapGet("/grupo", ObtenerTodosAsync);

        app.MapGet("/grupo/{grupoId:int}", ObtenerPorIdAsync);

        app.MapPost("/grupo", CrearAsync);

        app.MapPut("/grupo/{grupoId:int}", ModificarAsync);

        app.MapDelete("/grupo/{grupoId:int}", EliminarAsync);
    }

    private static async Task<IResult> ObtenerTodosAsync(
        IGrupoService service,
        ISeguridadService seguridadService,
        HttpContext context)
    {
        var grupos = await service.ObtenerTodosAsync();

        await RegistrarBitacoraAsync(
            seguridadService,
            context,
            "Consulta de todos los grupos.");

        return Results.Ok(grupos);
    }

    private static async Task<IResult> ObtenerPorIdAsync(
        int grupoId,
        IGrupoService service,
        ISeguridadService seguridadService,
        HttpContext context)
    {
        if (grupoId <= 0)
            return Results.BadRequest(
                "El identificador del grupo debe ser mayor que cero.");

        var grupo = await service.ObtenerPorIdAsync(grupoId);

        if (grupo is null)
            return Results.NotFound();

        await RegistrarBitacoraAsync(
            seguridadService,
            context,
            $"Consulta del grupo {grupoId}.");

        return Results.Ok(grupo);
    }

    private static async Task<IResult> CrearAsync(
        GrupoRequest request,
        IGrupoService service,
        ISeguridadService seguridadService,
        HttpContext context)
    {
        try
        {
            var grupoId = await service.CrearAsync(request);

            await RegistrarBitacoraAsync(
                seguridadService,
                context,
                $"Creación del grupo {grupoId}.");

            return Results.Created(
                $"/grupo/{grupoId}",
                new { GrupoId = grupoId });
        }
        catch (ArgumentException ex)
        {
            return Results.BadRequest(ex.Message);
        }
    }

    private static async Task<IResult> ModificarAsync(
        int grupoId,
        GrupoRequest request,
        IGrupoService service,
        ISeguridadService seguridadService,
        HttpContext context)
    {
        if (grupoId <= 0)
            return Results.BadRequest(
                "El identificador del grupo debe ser mayor que cero.");

        try
        {
            var actualizado = await service.ModificarAsync(
                grupoId,
                request);

            if (!actualizado)
                return Results.NotFound();

            await RegistrarBitacoraAsync(
                seguridadService,
                context,
                $"Modificación del grupo {grupoId}.");

            return Results.NoContent();
        }
        catch (ArgumentException ex)
        {
            return Results.BadRequest(ex.Message);
        }
    }

    private static async Task<IResult> EliminarAsync(
        int grupoId,
        IGrupoService service,
        ISeguridadService seguridadService,
        HttpContext context)
    {
        if (grupoId <= 0)
            return Results.BadRequest(
                "El identificador del grupo debe ser mayor que cero.");

        var eliminado = await service.EliminarAsync(grupoId);

        if (!eliminado)
            return Results.NotFound();

        await RegistrarBitacoraAsync(
            seguridadService,
            context,
            $"Eliminación del grupo {grupoId}.");

        return Results.NoContent();
    }

    private static async Task RegistrarBitacoraAsync(
        ISeguridadService seguridadService,
        HttpContext context,
        string descripcion)
    {
        var token = context.Items["token"]?.ToString();
        var usuario = context.Items["usuario"]?.ToString()
                      ?? "desconocido";

        if (string.IsNullOrWhiteSpace(token))
            return;

        await seguridadService.RegistrarBitacoraAsync(
            token,
            usuario,
            descripcion);
    }
}