using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Http;
using NuevoAvatar.Direccion.Nuevo.Services;

namespace NuevoAvatar.Direccion.Nuevo;

public static class DireccionEndpoint
{
    public static void MapDireccionEndpoints(this WebApplication app)
    {
        app.MapGet("/provincias", ObtenerProvinciasAsync);

        app.MapGet(
            "/cantones/{provinciaId:int}",
            ObtenerCantonesAsync);

        app.MapGet(
            "/distritos/{provinciaId:int}/{cantonId:int}",
            ObtenerDistritosAsync);
    }

    private static async Task<IResult> ObtenerProvinciasAsync(
        IDireccionService service,
        ISeguridadService seguridadService,
        HttpContext context)
    {
        var provincias = await service.ObtenerProvinciasAsync();

        await RegistrarBitacoraAsync(
            seguridadService,
            context,
            "Consulta de todas las provincias.");

        return Results.Ok(provincias);
    }

    private static async Task<IResult> ObtenerCantonesAsync(
        int provinciaId,
        IDireccionService service,
        ISeguridadService seguridadService,
        HttpContext context)
    {
        try
        {
            var cantones = await service.ObtenerCantonesAsync(
                provinciaId);

            await RegistrarBitacoraAsync(
                seguridadService,
                context,
                $"Consulta de cantones de la provincia {provinciaId}.");

            return Results.Ok(cantones);
        }
        catch (ArgumentException ex)
        {
            return Results.BadRequest(ex.Message);
        }
    }

    private static async Task<IResult> ObtenerDistritosAsync(
        int provinciaId,
        int cantonId,
        IDireccionService service,
        ISeguridadService seguridadService,
        HttpContext context)
    {
        try
        {
            var distritos = await service.ObtenerDistritosAsync(
                provinciaId,
                cantonId);

            await RegistrarBitacoraAsync(
                seguridadService,
                context,
                $"Consulta de distritos del cantón {cantonId} de la provincia {provinciaId}.");

            return Results.Ok(distritos);
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