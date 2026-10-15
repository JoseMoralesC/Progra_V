using System.Net.Http.Headers;
using System.Net.Http.Json;

namespace NuevoAvatar.Curso.Services;

public sealed class SeguridadService(HttpClient httpClient)
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
        using var request = new HttpRequestMessage(
            HttpMethod.Post,
            (Environment.GetEnvironmentVariable("BITACORA_BASE_URL") ?? Environment.GetEnvironmentVariable("BITACORA_URL") ?? "http://localhost:5159") + "/bitacora");

        request.Headers.Authorization =
            new AuthenticationHeaderValue(
                "Bearer",
                token);

        request.Content = JsonContent.Create(new
        {
            Usuario = usuario,
            Descripcion = descripcion
        });

        using var bitacoraResponse = await httpClient.SendAsync(request);
        bitacoraResponse.EnsureSuccessStatusCode();
    }
}