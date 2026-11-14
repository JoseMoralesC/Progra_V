using System.Net.Http.Headers;
using System.Net.Http.Json;

namespace NuevoAvatar.Grupo.Nuevo.Services;

public class SeguridadService : ISeguridadService
{
    private readonly HttpClient _httpClient;
    private readonly NuevoAvatar.Integracion.AuditoriaLocal _auditoria;

    public SeguridadService(HttpClient httpClient, NuevoAvatar.Integracion.AuditoriaLocal auditoria)
    {
        _httpClient = httpClient;
        _auditoria = auditoria;
    }

    public async Task<bool> ValidarTokenAsync(string token)
    {
        using var request = new HttpRequestMessage(
            HttpMethod.Get,
            "/validate");

        request.Headers.Authorization =
            new AuthenticationHeaderValue("Bearer", token);

        using var response = await _httpClient.SendAsync(request);

        return response.IsSuccessStatusCode;
    }

    public async Task RegistrarBitacoraAsync(
        string token,
        string usuario,
        string descripcion)
    {
        await _auditoria.Registrar(usuario, descripcion);
    }
}