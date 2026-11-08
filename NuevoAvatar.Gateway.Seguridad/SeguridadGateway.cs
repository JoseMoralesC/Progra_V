using System.Net;
using System.Net.Http.Headers;
using System.Net.Http.Json;
using System.Security.Claims;
using System.Text.Json;
using System.Text.RegularExpressions;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.DependencyInjection;
using NuevoAvatar.Seguridad.Models;

namespace NuevoAvatar.Gateway.Seguridad;

// GTW1 adjunta esta politica a cada ruta protegida del unico proxy.
// Sin politica explicita, la ruta no tiene acceso. Los permisos finos son del dominio.
public sealed record AccesoGateway(string[] Roles, string? Modulo = null, bool ConsultarMenu = false,
    string? RutaResumen = null)
{
    public AccesoGateway(string rol) : this([rol]) { }

    public bool EsResumen(HttpContext context) => RutaResumen is not null &&
        HttpMethods.IsGet(context.Request.Method) && context.Request.Path == RutaResumen &&
        context.Request.Query["vista"] == "dashboard";

    public bool PermiteOperacionAuxiliar(HttpContext context, List<ModuloPermitido> menu)
    {
        var request = context.Request;
        bool Tiene(string id) => menu.Any(m => m.IdModulo == id);
        if (HttpMethods.IsGet(request.Method))
        {
            if (Modulo == "ADM5" && request.Path == "/gateway/admin/rol") return Tiene("ADM4");
            if (Modulo == "ADM7" && request.Path == "/gateway/admin/modulo") return Tiene("ADM5");
            if (Modulo == "ADM6" && request.Path == "/gateway/admin/parametro/TAMPAG")
                return Tiene("ADM4") || Tiene("ADM5") || Tiene("ADM8");
        }
        // La matriz de ADM5 comparte la asignación existente de roles a módulos.
        return Modulo == "ADM7" && Tiene("ADM5") &&
            (HttpMethods.IsGet(request.Method) || HttpMethods.IsPut(request.Method)) &&
            Regex.IsMatch(request.Path.Value ?? "", @"^/gateway/admin/modulo/[A-Z0-9_]{2,20}/roles\z");
    }
}

public sealed class SeguridadGatewayMiddleware(RequestDelegate next)
{
    public const string ClienteAuth = "gateway-autenticacion";

    public async Task InvokeAsync(HttpContext context, IHttpClientFactory clientes)
    {
        foreach (var header in new[] { "X-Servicio-Key", "X-Usuario", "X-Rol", "X-Profesor-Id", "X-Estudiante-Id" })
            context.Request.Headers.Remove(header);

        var path = Uri.UnescapeDataString(context.Request.Path.Value ?? "");
        if (path.Split('/', StringSplitOptions.RemoveEmptyEntries)
            .Any(segmento => segmento.Equals("interno", StringComparison.OrdinalIgnoreCase)))
        {
            await Error(context, 404, "Recurso no disponible");
            return;
        }
        if (HttpMethods.IsPost(context.Request.Method) &&
            (path == "/gateway/auth/login" || path == "/gateway/auth/refresh"))
        {
            await next(context);
            return;
        }
        var authorization = context.Request.Headers.Authorization.ToString();
        if (!AuthenticationHeaderValue.TryParse(authorization, out var bearer) ||
            !bearer.Scheme.Equals("Bearer", StringComparison.OrdinalIgnoreCase) || string.IsNullOrWhiteSpace(bearer.Parameter))
        {
            await Error(context, 401, "No autorizado");
            return;
        }
        AccesoGateway? acceso = null;
        try
        {
            using var client = clientes.CreateClient(ClienteAuth);
            using var validate = new HttpRequestMessage(HttpMethod.Get, "/validate");
            validate.Headers.Authorization = bearer;
            using var respuesta = await client.SendAsync(validate, context.RequestAborted);
            if (respuesta.StatusCode == HttpStatusCode.Unauthorized)
            {
                await Error(context, 401, "No autorizado");
                return;
            }
            respuesta.EnsureSuccessStatusCode();
            if (!await respuesta.Content.ReadFromJsonAsync<bool>(cancellationToken: context.RequestAborted))
            {
                await Error(context, 401, "No autorizado");
                return;
            }
            acceso = context.GetEndpoint()?.Metadata.GetMetadata<AccesoGateway>();
            if (acceso is null || acceso.Roles.Length == 0)
            {
                await Error(context, 403, "Ruta sin politica de acceso");
                return;
            }
            using var request = new HttpRequestMessage(HttpMethod.Get, "/sesion");
            request.Headers.Authorization = bearer;
            using var response = await client.SendAsync(request, context.RequestAborted);
            if (response.StatusCode is HttpStatusCode.Unauthorized or HttpStatusCode.Forbidden)
            {
                await Error(context, (int)response.StatusCode, "No autorizado");
                return;
            }
            response.EnsureSuccessStatusCode();
            var sesion = await response.Content.ReadFromJsonAsync<SesionActual>(cancellationToken: context.RequestAborted);
            if (sesion is null || string.IsNullOrWhiteSpace(sesion.UsuarioID) ||
                string.IsNullOrWhiteSpace(sesion.Rol)) throw new JsonException("Sesion incompleta");
            var rolPermitido = acceso.EsResumen(context)
                ? sesion.Rol is "ADMINISTRADOR" or "PROFESOR"
                : acceso.Roles.Contains(sesion.Rol, StringComparer.Ordinal);
            if (!rolPermitido)
            {
                await Error(context, 403, "Rol sin permiso para esta operacion");
                return;
            }
            context.User = new ClaimsPrincipal(new ClaimsIdentity(new[] {
                new Claim(ClaimTypes.NameIdentifier, sesion.UsuarioID),
                new Claim(ClaimTypes.Name, sesion.Nombre),
                new Claim(ClaimTypes.Role, sesion.Rol)
            }, "SesionGateway"));
            context.Items["sesion"] = sesion;
        }
        catch (Exception ex) when (ex is HttpRequestException or TaskCanceledException or JsonException)
        {
            if (context.RequestAborted.IsCancellationRequested) throw;
            await Error(context, 503, "Autenticacion no disponible");
            return;
        }
        if (acceso!.Modulo is not null || acceso.ConsultarMenu || acceso.EsResumen(context))
        {
            try
            {
                var menu = await PermisosGateway.Obtener(clientes, bearer, context.RequestAborted);
                context.Items[PermisosGateway.ClaveMenu] = menu;
                if (!acceso.EsResumen(context) && acceso.Modulo is not null && !menu.Any(m => m.IdModulo == acceso.Modulo) &&
                    !acceso.PermiteOperacionAuxiliar(context, menu))
                {
                    await Error(context, 403, "Modulo sin permiso para esta cuenta");
                    return;
                }
            }
            catch (PermisosNoDisponiblesException)
            {
                await Error(context, 503, "Permisos no disponibles");
                return;
            }
        }
        // Fuera del catch: un fallo del destino no se atribuye a Autenticacion.
        await next(context);
    }

    private static Task Error(HttpContext context, int status, string error)
    {
        context.Response.StatusCode = status;
        context.Response.Headers.CacheControl = "no-store";
        if (status == 401) context.Response.Headers.WWWAuthenticate = "Bearer";
        return context.Response.WriteAsJsonAsync(new { status, error });
    }
}

public static class SeguridadGatewayExtensions
{
    public static IServiceCollection AddSeguridadGateway(this IServiceCollection services, Uri autenticacion)
    {
        services.AddHttpClient(SeguridadGatewayMiddleware.ClienteAuth, client =>
        {
            client.BaseAddress = autenticacion;
            client.Timeout = TimeSpan.FromSeconds(8);
        }).ConfigurePrimaryHttpMessageHandler(() => new HttpClientHandler { AllowAutoRedirect = false });
        return services;
    }

    public static IApplicationBuilder UseSeguridadGateway(this IApplicationBuilder app) =>
        app.UseMiddleware<SeguridadGatewayMiddleware>();
}
