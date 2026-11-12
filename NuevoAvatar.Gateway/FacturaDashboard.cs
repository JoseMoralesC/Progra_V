using System.Net.Http.Headers;
using System.Net.Http.Json;
using System.Text.Json;

namespace NuevoAvatar.Gateway;

public static class FacturaDashboard
{
    private sealed record Factura(string Estado);

    public static async Task<IResult> Obtener(HttpContext context, IHttpClientFactory clientes)
    {
        var periodoId = int.Parse(context.Request.Query["periodoId"]!);
        try
        {
            using var cliente = clientes.CreateClient("dashboard-Factura");
            using var solicitud = new HttpRequestMessage(HttpMethod.Get, $"/factura?periodoId={periodoId}");
            solicitud.Headers.Authorization = AuthenticationHeaderValue.Parse(context.Request.Headers.Authorization.ToString());
            using var respuesta = await cliente.SendAsync(solicitud, context.RequestAborted);
            respuesta.EnsureSuccessStatusCode();
            var facturas = await respuesta.Content.ReadFromJsonAsync<List<Factura>>(cancellationToken: context.RequestAborted);
            if (facturas is null || facturas.Any(factura => factura is null || factura.Estado is null))
                throw new JsonException("Respuesta de facturas incompleta");
            return Results.Ok(new { pendientes = facturas.Count(factura =>
                factura.Estado.Equals("PENDIENTE", StringComparison.OrdinalIgnoreCase)) });
        }
        catch (Exception error) when (error is HttpRequestException or TaskCanceledException or JsonException)
        {
            context.RequestAborted.ThrowIfCancellationRequested();
            return Results.Json(new { error = "No se pudo consultar el indicador de facturas." }, statusCode: 503);
        }
    }
}
