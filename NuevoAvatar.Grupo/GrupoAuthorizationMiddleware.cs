using System.Text;
using System.Text.Json;
using NuevoAvatar.Grupo.Nuevo.Services;

namespace NuevoAvatar.Grupo.Nuevo;

public class GrupoAuthorizationMiddleware
{
    private readonly RequestDelegate _next;

    public GrupoAuthorizationMiddleware(RequestDelegate next)
    {
        _next = next;
    }

    public async Task InvokeAsync(
        HttpContext context,
        ISeguridadService seguridadService)
    {
        var authorization = context.Request.Headers.Authorization.ToString();

        if (string.IsNullOrWhiteSpace(authorization) ||
            !authorization.StartsWith("Bearer ", StringComparison.OrdinalIgnoreCase))
        {
            context.Response.StatusCode = StatusCodes.Status401Unauthorized;
            await context.Response.WriteAsync("No autorizado");
            return;
        }

        var token = authorization.Substring("Bearer ".Length).Trim();

        if (string.IsNullOrWhiteSpace(token))
        {
            context.Response.StatusCode = StatusCodes.Status401Unauthorized;
            await context.Response.WriteAsync("No autorizado");
            return;
        }

        var tokenValido = await seguridadService.ValidarTokenAsync(token);

        if (!tokenValido)
        {
            context.Response.StatusCode = StatusCodes.Status401Unauthorized;
            await context.Response.WriteAsync("No autorizado");
            return;
        }

        context.Items["token"] = token;
        context.Items["usuario"] = ObtenerUsuarioDelToken(token);

        await _next(context);
    }

    private static string ObtenerUsuarioDelToken(string token)
    {
        try
        {
            var partes = token.Split('.');

            if (partes.Length != 3)
                return "desconocido";

            var payload = partes[1];

            var padding = 4 - (payload.Length % 4);

            if (padding != 4)
                payload += new string('=', padding);

            payload = payload
                .Replace('-', '+')
                .Replace('_', '/');

            var bytes = Convert.FromBase64String(payload);

            using var document = JsonDocument.Parse(
                Encoding.UTF8.GetString(bytes));

            if (document.RootElement.TryGetProperty(
                    "sub",
                    out var sub))
            {
                return sub.GetString() ?? "desconocido";
            }

            return "desconocido";
        }
        catch
        {
            return "desconocido";
        }
    }
}