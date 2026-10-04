using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Http;
using NuevoAvatar.Periodo.Nuevo.Entities;
using NuevoAvatar.Periodo.Nuevo.Services;

namespace NuevoAvatar.Periodo.Nuevo;

public static class PeriodoEndpoint
{
    public static void MapPeriodoEndpoints(this WebApplication app)
    {
        app.MapGet("/periodo", ObtenerTodosAsync);

        app.MapGet("/periodo/{periodoId:int}", ObtenerPorIdAsync);

        app.MapPost("/periodo", CrearAsync);

        app.MapPut("/periodo/{periodoId:int}", ModificarAsync);

        app.MapDelete("/periodo/{periodoId:int}", EliminarAsync);
    }

    private static async Task<IResult> ObtenerTodosAsync(
        IPeriodoService service,
        ISeguridadService seguridadService,
        HttpContext context)
    {
        var periodos = await service.ObtenerTodosAsync();

        await RegistrarBitacoraAsync(
            seguridadService,
            context,
            "Consulta de todos los periodos.");

        return Results.Ok(periodos);
    }

    private static async Task<IResult> ObtenerPorIdAsync(
        int periodoId,
        IPeriodoService service,
        ISeguridadService seguridadService,
        HttpContext context)
    {
        if (periodoId <= 0)
        {
            return Results.BadRequest(
                "El identificador del periodo debe ser mayor que cero.");
        }

        var periodo = await service.ObtenerPorIdAsync(periodoId);

        if (periodo is null)
        {
            return Results.NotFound();
        }

        await RegistrarBitacoraAsync(
            seguridadService,
            context,
            $"Consulta del periodo {periodoId}.");

        return Results.Ok(periodo);
    }

    private static async Task<IResult> CrearAsync(
        PeriodoRequest request,
        IPeriodoService service,
        ISeguridadService seguridadService,
        HttpContext context)
    {
        try
        {
            var periodoId = await service.CrearAsync(request);

            await RegistrarBitacoraAsync(
                seguridadService,
                context,
                $"Creación del periodo {periodoId}.");

            return Results.Created(
                $"/periodo/{periodoId}",
                new { PeriodoId = periodoId });
        }
        catch (ArgumentException ex)
        {
            return Results.BadRequest(ex.Message);
        }
    }

    private static async Task<IResult> ModificarAsync(
        int periodoId,
        PeriodoRequest request,
        IPeriodoService service,
        ISeguridadService seguridadService,
        HttpContext context)
    {
        if (periodoId <= 0)
        {
            return Results.BadRequest(
                "El identificador del periodo debe ser mayor que cero.");
        }

        try
        {
            var actualizado = await service.ModificarAsync(
                periodoId,
                request);

            if (!actualizado)
            {
                return Results.NotFound();
            }

            await RegistrarBitacoraAsync(
                seguridadService,
                context,
                $"Modificación del periodo {periodoId}.");

            return Results.NoContent();
        }
        catch (ArgumentException ex)
        {
            return Results.BadRequest(ex.Message);
        }
    }

    private static async Task<IResult> EliminarAsync(
        int periodoId,
        IPeriodoService service,
        ISeguridadService seguridadService,
        HttpContext context)
    {
        if (periodoId <= 0)
        {
            return Results.BadRequest(
                "El identificador del periodo debe ser mayor que cero.");
        }
       


        try
        {
            var eliminado = await service.EliminarAsync(periodoId);

            if (!eliminado)
            {
                return Results.NotFound();
            }

            await RegistrarBitacoraAsync(
                seguridadService,
                context,
                $"Eliminación del periodo {periodoId}.");

            return Results.NoContent();
        }
        catch (ArgumentException ex)
        {
            return Results.BadRequest(ex.Message);
        }
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
        {
            return;
        }

        await seguridadService.RegistrarBitacoraAsync(
            token,
            usuario,
            descripcion);
    }
}