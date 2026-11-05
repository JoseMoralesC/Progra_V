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

        await NuevoAvatar.Integracion.IntegracionExtensions.ValidarEliminacion(context.HttpContext,"Curso");
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
            catch (Exception bitacoraEx)
            {
                context.HttpContext.RequestServices
                    .GetService<ILogger<CursoAuthorizationFilter>>()
                    ?.LogError(bitacoraEx, "No fue posible registrar la bitácora de curso.");
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

    public static string ConstruirDescripcion(
        EndpointFilterInvocationContext context,
        object? resultado,
        Exception? excepcion)
    {
        return ConstruirDescripcionHttp(context.HttpContext, excepcion);
    }

    public static string ConstruirDescripcionHttp(
        HttpContext httpContext,
        Exception? excepcion)
    {
        var request = httpContext.Request;

        if (excepcion is not null)
        {
            return $"{request.Method} {request.Path} - Error: {excepcion.Message}";
        }

        if (httpContext.Items.TryGetValue("bitacora", out var detalle) &&
            detalle is string texto &&
            !string.IsNullOrWhiteSpace(texto))
        {
            return texto;
        }

        if (HttpMethods.IsGet(request.Method))
        {
            return $"El usuario consulta {request.Path}";
        }

        return $"{request.Method} {request.Path}";
    }
}