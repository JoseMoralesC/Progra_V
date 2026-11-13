using System.Net.Http.Headers;
using System.Net.Http.Json;
using System.Text.Json;

namespace NuevoAvatar.Gateway;

public static class FacturaDashboard
{
    private sealed record ConteoFacturas(long Pendientes);

    public static async Task<IResult> Obtener(HttpContext context, IHttpClientFactory clientes)
    {
        var periodoId = int.Parse(context.Request.Query["periodoId"]!);
        try
        {
            using var cliente = clientes.CreateClient("dashboard-Factura");
            using var solicitud = new HttpRequestMessage(HttpMethod.Get, $"/factura/pendientes?periodoId={periodoId}");
            solicitud.Headers.Authorization = AuthenticationHeaderValue.Parse(context.Request.Headers.Authorization.ToString());
            using var respuesta = await cliente.SendAsync(solicitud, context.RequestAborted);
            respuesta.EnsureSuccessStatusCode();
            var conteo = await respuesta.Content.ReadFromJsonAsync<ConteoFacturas>(cancellationToken: context.RequestAborted);
            if (conteo is null || conteo.Pendientes < 0)
                throw new JsonException("Respuesta de facturas incompleta");
            return Results.Ok(new { pendientes = conteo.Pendientes });
        }
        catch (Exception error) when (error is HttpRequestException or TaskCanceledException or JsonException)
        {
            context.RequestAborted.ThrowIfCancellationRequested();
            return Results.Json(new { error = "No se pudo consultar el indicador de facturas." }, statusCode: 503);
        }
    }
}
