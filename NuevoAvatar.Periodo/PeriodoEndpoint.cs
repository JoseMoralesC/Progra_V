using System.Text.Json;
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
        short? anio,
        string? estado,
        IPeriodoService service,
        ISeguridadService seguridadService,
        HttpContext context)
    {
        IEnumerable<PeriodoRequest> periodos; try{periodos=await service.ObtenerTodosAsync(anio,estado);}catch(ArgumentException ex){return Results.BadRequest(new{error=ex.Message});}

        await RegistrarBitacoraAsync(
            seguridadService,
            context,
            "El usuario consulta periodos");

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
            $"El usuario consulta periodo {periodoId}");

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
            var creado = await service.ObtenerPorIdAsync(periodoId) ?? new PeriodoRequest
            {
                PeriodoId = periodoId,
                Anio = request.Anio,
                NumeroPeriodo = request.NumeroPeriodo,
                FechaInicio = request.FechaInicio,
                FechaFin = request.FechaFin
            };

            await RegistrarBitacoraAsync(
                seguridadService,
                context,
                $"Creación de periodo: {JsonSerializer.Serialize(creado)}");

            return Results.Created(
                $"/periodo/{periodoId}",
                new { PeriodoId = periodoId });
        }
        catch (ArgumentException ex)
        {
            return Results.BadRequest(ex.Message);
        }
        catch (PeriodoConflictoException ex){return Results.Conflict(new{error=ex.Message});}
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
            var anterior = await service.ObtenerPorIdAsync(periodoId);
            if (anterior is null)
            {
                return Results.NotFound();
            }

            var actualizado = await service.ModificarAsync(
                periodoId,
                request);

            if (!actualizado)
            {
                return Results.NotFound();
            }

            var actual = await service.ObtenerPorIdAsync(periodoId) ?? new PeriodoRequest
            {
                PeriodoId = periodoId,
                Anio = request.Anio,
                NumeroPeriodo = request.NumeroPeriodo,
                FechaInicio = request.FechaInicio,
                FechaFin = request.FechaFin
            };

            await RegistrarBitacoraAsync(
                seguridadService,
                context,
                $"Modificación de periodo. Anterior: {JsonSerializer.Serialize(anterior)} Actual: {JsonSerializer.Serialize(actual)}");

            return Results.NoContent();
        }
        catch (ArgumentException ex)
        {
            return Results.BadRequest(ex.Message);
        }
        catch (PeriodoConflictoException ex){return Results.Conflict(new{error=ex.Message});}
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
            var existente = await service.ObtenerPorIdAsync(periodoId);
            if (existente is null)
            {
                return Results.NotFound();
            }

            var eliminado = await service.EliminarAsync(periodoId);

            if (!eliminado)
            {
                return Results.NotFound();
            }

            await RegistrarBitacoraAsync(
                seguridadService,
                context,
                $"Eliminación de periodo: {JsonSerializer.Serialize(existente)}");

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
        if (context.Items[NuevoAvatar.Integracion.AuditoriaTransaccion.EscrituraRegistrada] is true) return;
        if (NuevoAvatar.Integracion.ConsultaAutomatica.EsCargaDashboard(context, 200)) return;
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
            descripcion + ". Resultado: Exitoso");
    }
}
