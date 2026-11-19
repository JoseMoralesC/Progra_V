using System.Net.Http.Headers;
using System.Net.Http.Json;

namespace NuevoAvatar.Curso.Services;

public sealed class SeguridadService(HttpClient httpClient, NuevoAvatar.Integracion.AuditoriaLocal auditoria)
    : ISeguridadService
{
    public async Task<bool> ValidarTokenAsync(string token)
    {
        using var request = new HttpRequestMessage(
            HttpMethod.Get,
            "/validate");

        request.Headers.Authorization =
            new AuthenticationHeaderValue(
                "Bearer",
                token);

        using var response =
            await httpClient.SendAsync(request);

        return response.IsSuccessStatusCode;
    }

    public async Task RegistrarBitacoraAsync(
        string token,
        string usuario,
        string descripcion)
    {
        await auditoria.Registrar(usuario, descripcion);
    }
}