using System.Net.Http.Headers;
using NuevoAvatar.Seguridad.Models;

namespace NuevoAvatar.Gateway;

public static class AuditoriaDashboard
{
    public static async Task<IResult?> Registrar(HttpContext context, IHttpClientFactory clientes)
    {
        if (context.Items["sesion"] is not SesionActual sesion)
            return Results.Unauthorized();
        if (sesion.Rol is not ("ADMINISTRADOR" or "PROFESOR"))
            return Results.StatusCode(StatusCodes.Status403Forbidden);

        // La identidad y el texto de auditoría se determinan en el servidor.
        using var solicitud = new HttpRequestMessage(HttpMethod.Post, "/bitacora");
        solicitud.Headers.Authorization = AuthenticationHeaderValue.Parse(context.Request.Headers.Authorization.ToString());
        solicitud.Content = JsonContent.Create(new {
            usuario = sesion.UsuarioID,
            descripcion = "El usuario consulta el dashboard"
        });
        try
        {
            using var cliente = clientes.CreateClient("dashboard-bitacora");
            using var respuesta = await cliente.SendAsync(solicitud, context.RequestAborted);
            if (respuesta.IsSuccessStatusCode) return null;
        }
        catch (Exception error) when (error is HttpRequestException or TaskCanceledException)
        {
            context.RequestAborted.ThrowIfCancellationRequested();
        }
        return Results.Json(new { error = "No se pudo registrar la visualización del dashboard." }, statusCode: 503);
    }
}
