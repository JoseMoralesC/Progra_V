using System.Net.Http.Headers;
using System.Net.Http.Json;
using System.Text.Json;
using System.Text.RegularExpressions;
using Microsoft.Extensions.DependencyInjection;

namespace NuevoAvatar.Gateway.Seguridad;

public sealed record ModuloPermitido(string IdModulo, string NombreModulo, bool Activo, int Orden);
public sealed class PermisosNoDisponiblesException : Exception;

public static class PermisosGateway
{
    public const string ClienteModulo = "gateway-modulos";
    public const string ClaveMenu = "gateway.menu";

    public static IServiceCollection AddPermisosGateway(this IServiceCollection services, Uri modulo)
    {
        services.AddHttpClient(ClienteModulo, client => {
            client.BaseAddress = modulo;
            client.Timeout = TimeSpan.FromSeconds(8);
        }).ConfigurePrimaryHttpMessageHandler(() => new HttpClientHandler { AllowAutoRedirect = false });
        return services;
    }

    public static async Task<List<ModuloPermitido>> Obtener(IHttpClientFactory clientes,
        AuthenticationHeaderValue bearer, CancellationToken cancellation)
    {
        try
        {
            using var client = clientes.CreateClient(ClienteModulo);
            using var request = new HttpRequestMessage(HttpMethod.Get, "/modulo/menu");
            request.Headers.Authorization = bearer;
            using var response = await client.SendAsync(request, cancellation);
            response.EnsureSuccessStatusCode();
            var menu = await response.Content.ReadFromJsonAsync<List<ModuloPermitido>>(cancellationToken: cancellation);
            if (menu is null || menu.Any(m => m is null || string.IsNullOrWhiteSpace(m.IdModulo) ||
                !Regex.IsMatch(m.IdModulo, "^[A-Z0-9_]{2,20}$") || string.IsNullOrWhiteSpace(m.NombreModulo) || m.Orden < 0))
                throw new JsonException("Menu incompleto");
            return menu.Where(m => m.Activo).OrderBy(m => m.Orden).ThenBy(m => m.NombreModulo).ToList();
        }
        catch (Exception ex) when (ex is HttpRequestException or JsonException or TaskCanceledException)
        {
            cancellation.ThrowIfCancellationRequested();
            throw new PermisosNoDisponiblesException();
        }
    }
}
