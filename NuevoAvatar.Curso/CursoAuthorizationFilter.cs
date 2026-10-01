using System.Text.Json;
using NuevoAvatar.Curso.Services;

namespace NuevoAvatar.Curso;

public sealed class CursoAuthorizationFilter(
    ISeguridadService seguridadService) : IEndpointFilter
{
    public async ValueTask<object?> InvokeAsync(
        EndpointFilterInvocationContext context,
        EndpointFilterDelegate next)
    {
        var authorization =
            context.HttpContext.Request.Headers.Authorization.ToString();

        if (string.IsNullOrWhiteSpace(authorization) ||
            !authorization.StartsWith(
                "Bearer ",
                StringComparison.OrdinalIgnoreCase))
        {
            return Results.Unauthorized();
        }

        var token = authorization["Bearer ".Length..].Trim();

        if (string.IsNullOrWhiteSpace(token))
        {
            return Results.Unauthorized();
        }

        var tokenValido =
            await seguridadService.ValidarTokenAsync(token);

        if (!tokenValido)
        {
            return Results.Unauthorized();
        }

        var usuario = ObtenerUsuarioDelToken(token);

        if (string.IsNullOrWhiteSpace(usuario))
        {
            return Results.Unauthorized();
        }

        object? resultado = null;
        Exception? excepcion = null;

        try
        {
            resultado = await next(context);
            return resultado;
        }
        catch (Exception exception)
        {
            excepcion = exception;
            throw;
        }
        finally
        {
            var descripcion = ConstruirDescripcion(
                context,
                resultado,
                excepcion);

            try
            {
                await seguridadService.RegistrarBitacoraAsync(
                    token,
                    usuario,
                    descripcion);
            }
            catch
            {
               
            }
        }
    }

    private static string? ObtenerUsuarioDelToken(string token)
    {
        try
        {
            var partes = token.Split('.');

            if (partes.Length != 3)
            {
                return null;
            }

            var payload = partes[1];

            var bytes = Base64UrlDecode(payload);

            using var document =
                JsonDocument.Parse(bytes);

            if (!document.RootElement.TryGetProperty(
                    "sub",
                    out var subject))
            {
                return null;
            }

            return subject.GetString();
        }
        catch
        {
            return null;
        }
    }

    private static byte[] Base64UrlDecode(string value)
    {
        var base64 = value
            .Replace('-', '+')
            .Replace('_', '/');

        switch (base64.Length % 4)
        {
            case 2:
                base64 += "==";
                break;

            case 3:
                base64 += "=";
                break;
        }

        return Convert.FromBase64String(base64);
    }

    private static string ConstruirDescripcion(
        EndpointFilterInvocationContext context,
        object? resultado,
        Exception? excepcion)
    {
        var request = context.HttpContext.Request;

        if (excepcion is not null)
        {
            return $"{request.Method} {request.Path} - Error: {excepcion.Message}";
        }

        return $"{request.Method} {request.Path}";
    }
}