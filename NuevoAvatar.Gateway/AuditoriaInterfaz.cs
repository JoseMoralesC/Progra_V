using System.Net.Http.Headers;
using System.Net.Http.Json;
using NuevoAvatar.Seguridad.Models;

namespace NuevoAvatar.Gateway;

public sealed record EventoInterfazRequest(string Tipo, string Ruta);

public static class AuditoriaInterfaz
{
    private static readonly HashSet<string> RutasCriticas = new(StringComparer.Ordinal)
    {
        "/inicio", "/instituciones", "/parametros", "/roles", "/usuarios", "/modulos",
        "/carreras", "/cursos", "/profesores", "/periodos", "/notificaciones", "/notas",
        "/estudiante", "/estudiante/expediente", "/estudiante/prematricula", "/estudiante/perfil",
        "/perfil"
    };

    public static async Task<IResult> Registrar(EventoInterfazRequest evento, HttpContext context,
        IHttpClientFactory clientes)
    {
        if (evento is null || !RutasCriticas.Contains(evento.Ruta) ||
            evento.Tipo is not ("navegacion" or "error-ui"))
            return Results.BadRequest(new { error = "Evento de interfaz no permitido." });

        if (context.Items["sesion"] is not SesionActual sesion ||
            !AuthenticationHeaderValue.TryParse(context.Request.Headers.Authorization.ToString(), out var bearer))
            return Results.Unauthorized();

        var descripcion = evento.Tipo == "navegacion"
            ? $"ADM1: navegación a ruta crítica {evento.Ruta}"
            : $"ADM1: error de interfaz en ruta crítica {evento.Ruta}";
        try
        {
            await context.RequestServices.GetRequiredService<NuevoAvatar.Integracion.IAuditoriaLocal>().Registrar(sesion.UsuarioID, descripcion);
            return Results.NoContent();
        }
        catch (Exception error) when (error is HttpRequestException or TaskCanceledException)
        {
            context.RequestAborted.ThrowIfCancellationRequested();
        }

        return Results.Json(new { error = "No se pudo registrar el evento de interfaz." },
            statusCode: StatusCodes.Status503ServiceUnavailable);
    }
}
