using System.Net;
using System.Net.Http.Json;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using NuevoAvatar.Gateway;
using Xunit;

namespace NuevoAvatar.Gateway.Tests;

public class PermisosAuxiliaresTests : IAsyncLifetime
{
    private WebApplication backend = null!, gateway = null!;
    private HttpClient cliente = null!;
    private string permiso = "ADM4";
    private bool activo = true;
    private int solicitudesNegocio;
    private List<string> asignados = ["ADMIN"];

    public async Task InitializeAsync()
    {
        var builder = WebApplication.CreateBuilder();
        builder.WebHost.UseUrls("http://127.0.0.1:0");
        builder.Logging.ClearProviders();
        backend = builder.Build();
        backend.MapGet("/validate", (HttpContext c) => c.Request.Headers.Authorization == "Bearer invalido"
            ? Results.Unauthorized() : Results.Ok(true));
        backend.MapGet("/sesion", (HttpContext c) => Results.Ok(new {
            usuarioID = "persona@cuc.cr", nombre = "Persona",
            rol = c.Request.Headers.Authorization == "Bearer profesor" ? "PROFESOR" :
                c.Request.Headers.Authorization == "Bearer estudiante" ? "ESTUDIANTE" : "ADMINISTRADOR"
        }));
        backend.MapGet("/modulo/menu", () => Results.Ok(new[] {
            new { idModulo = permiso, nombreModulo = permiso, activo, orden = 1 }
        }));
        backend.MapGet("/modulo/{id}/roles", () => {
            solicitudesNegocio++;
            return Results.Ok(asignados);
        });
        backend.MapPut("/modulo/{id}/roles", async (HttpContext c) => {
            solicitudesNegocio++;
            asignados = await c.Request.ReadFromJsonAsync<List<string>>() ?? [];
            return Results.NoContent();
        });
        backend.Map("/{**path}", (HttpContext c) => {
            solicitudesNegocio++;
            return Results.Ok(new { path = c.Request.Path.Value });
        });
        await backend.StartAsync();
        gateway = GatewayApplication.Create([], b => {
            b.WebHost.UseUrls("http://127.0.0.1:0");
            b.Logging.ClearProviders();
            b.Configuration.AddInMemoryCollection(CatalogoRutas.Todas.Select(r => r.Servicio).Distinct()
                .Select(s => new KeyValuePair<string, string?>("Destinos:" + s, backend.Urls.Single())));
        });
        await gateway.StartAsync();
        cliente = new HttpClient { BaseAddress = new Uri(gateway.Urls.Single()) };
    }

    private async Task<HttpResponseMessage> Solicitar(string metodo, string ruta, string? token = "admin")
    {
        using var request = new HttpRequestMessage(new HttpMethod(metodo), ruta);
        if (token is not null) request.Headers.Authorization = new("Bearer", token);
        return await cliente.SendAsync(request);
    }

    [Theory]
    [InlineData("ADM4", "GET", "/gateway/admin/rol")]
    [InlineData("ADM4", "GET", "/gateway/admin/parametro/TAMPAG")]
    [InlineData("ADM5", "GET", "/gateway/admin/modulo")]
    [InlineData("ADM5", "GET", "/gateway/admin/modulo/ADM8/roles")]
    [InlineData("ADM5", "GET", "/gateway/admin/parametro/TAMPAG")]
    [InlineData("ADM8", "GET", "/gateway/admin/parametro/TAMPAG")]
    public async Task PermiteDependenciasExactasDelMantenimiento(string modulo, string metodo, string ruta)
    {
        permiso = modulo;
        using var response = await Solicitar(metodo, ruta);
        Assert.Equal(HttpStatusCode.OK, response.StatusCode);
        Assert.Equal(1, solicitudesNegocio);
    }

    [Theory]
    [InlineData(false)]
    [InlineData(true)]
    public async Task MatrizAdm5ConservaCuerpoYConsultaAsignaciones(bool vaciar)
    {
        permiso = "ADM5";
        using var request = new HttpRequestMessage(HttpMethod.Put, "/gateway/admin/modulo/ADM8/roles");
        request.Headers.Authorization = new("Bearer", "admin");
        string[] nuevos = vaciar ? [] : ["ADMIN", "PERSONAL"];
        request.Content = JsonContent.Create(nuevos);
        using var guardado = await cliente.SendAsync(request);
        Assert.Equal(HttpStatusCode.NoContent, guardado.StatusCode);
        using var lectura = await Solicitar("GET", "/gateway/admin/modulo/ADM8/roles");
        Assert.Equal(HttpStatusCode.OK, lectura.StatusCode);
        Assert.Equal(nuevos, await lectura.Content.ReadFromJsonAsync<List<string>>());
    }

    [Theory]
    [InlineData("ADM4", "POST", "/gateway/admin/rol")]
    [InlineData("ADM4", "PUT", "/gateway/admin/rol/ADMIN")]
    [InlineData("ADM4", "DELETE", "/gateway/admin/rol/ADMIN")]
    [InlineData("ADM4", "GET", "/gateway/admin/rol/ADMIN")]
    [InlineData("ADM4", "GET", "/gateway/admin/parametro")]
    [InlineData("ADM4", "GET", "/gateway/admin/parametro/DOMESTUD")]
    [InlineData("ADM4", "PUT", "/gateway/admin/parametro/TAMPAG")]
    [InlineData("ADM5", "POST", "/gateway/admin/modulo")]
    [InlineData("ADM5", "PUT", "/gateway/admin/modulo/ADM8")]
    [InlineData("ADM5", "DELETE", "/gateway/admin/modulo/ADM8")]
    [InlineData("ADM5", "GET", "/gateway/admin/modulo/ADM8")]
    [InlineData("ADM5", "GET", "/gateway/admin/modulo/menu/ADMIN")]
    [InlineData("ADM5", "GET", "/gateway/admin/modulo/ADM8/roles/ADMIN")]
    [InlineData("ADM5", "POST", "/gateway/admin/modulo/ADM8/roles")]
    [InlineData("ADM5", "DELETE", "/gateway/admin/modulo/ADM8/roles")]
    [InlineData("ADM8", "GET", "/gateway/admin/rol")]
    [InlineData("ADM8", "PUT", "/gateway/admin/modulo/ADM8/roles")]
    [InlineData("ADM9", "GET", "/gateway/admin/parametro/TAMPAG")]
    public async Task NoConcedeOtrasOperaciones(string modulo, string metodo, string ruta)
    {
        permiso = modulo;
        using var response = await Solicitar(metodo, ruta);
        Assert.Equal(HttpStatusCode.Forbidden, response.StatusCode);
        Assert.Equal(0, solicitudesNegocio);
    }

    [Theory]
    [InlineData(null, 401)]
    [InlineData("invalido", 401)]
    [InlineData("profesor", 403)]
    [InlineData("estudiante", 403)]
    public async Task DependenciasMantienenAutenticacionYRolAdministrador(string? token, int estado)
    {
        using var response = await Solicitar("GET", "/gateway/admin/rol", token);
        Assert.Equal(estado, (int)response.StatusCode);
        Assert.Equal(0, solicitudesNegocio);
    }

    [Fact]
    public async Task ModuloInactivoNoConcedeDependencias()
    {
        activo = false;
        using var response = await Solicitar("GET", "/gateway/admin/rol");
        Assert.Equal(HttpStatusCode.Forbidden, response.StatusCode);
        Assert.Equal(0, solicitudesNegocio);
    }

    [Theory]
    [InlineData("ADM6", "PUT", "/gateway/admin/parametro/TAMPAG")]
    [InlineData("ADM7", "POST", "/gateway/admin/modulo")]
    [InlineData("ADM5", "PUT", "/gateway/admin/rol/ADMIN")]
    public async Task ConservaPermisosPropiosDeCadaModulo(string modulo, string metodo, string ruta)
    {
        permiso = modulo;
        using var response = await Solicitar(metodo, ruta);
        Assert.Equal(HttpStatusCode.OK, response.StatusCode);
        Assert.Equal(1, solicitudesNegocio);
    }

    public async Task DisposeAsync()
    {
        cliente.Dispose();
        await gateway.DisposeAsync();
        await backend.DisposeAsync();
    }
}
