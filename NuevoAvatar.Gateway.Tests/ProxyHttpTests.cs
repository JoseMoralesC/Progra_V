using System.Net;
using System.Net.Http.Json;
using System.Text;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using NuevoAvatar.Gateway;
using Xunit;

namespace NuevoAvatar.Gateway.Tests;

// Kestrel y YARP reales en puertos efimeros; propietarios/auth simulados, sin SQL.
public class ProxyHttpTests : IAsyncLifetime
{
    [Fact]
    public void ServicioNuevoSinContratoDeModuloNoPuedeQuedarExentoAccidentalmente()
    {
        var ruta = new RutaGateway("/gateway/nuevo", "/nuevo", "ServicioNuevo", 9999, ["GET"], ["ADMINISTRADOR"]);
        Assert.Throws<InvalidOperationException>(() => ruta.Modulo);
    }
    private WebApplication backend = null!, gateway = null!;
    private HttpClient cliente = null!;
    private int negocios;
    private readonly List<NuevoAvatar.Seguridad.Models.Bitacora> visitas = [];
    private int estadoBitacora = 201;
    private readonly HashSet<string> permisos = CatalogoRutas.Todas.Where(r => r.Modulo is not null)
        .Select(r => r.Modulo!).ToHashSet();
    private int estadoPermisos = 200;
    private bool moduloActivo = true, menuInvalido;
    public async Task InitializeAsync()
    {
        var builder = WebApplication.CreateBuilder();
        builder.WebHost.UseUrls("http://127.0.0.1:0");
        builder.Logging.ClearProviders();
        backend = builder.Build();
        backend.MapGet("/validate", (HttpContext c) =>
            c.Request.Headers.Authorization == "Bearer invalido" ? Results.Unauthorized() : Results.Ok(true));
        backend.MapGet("/sesion", (HttpContext c) => Results.Ok(new {
            usuarioID = "persona@cuc.ac.cr", nombre = "Persona",
            rol = c.Request.Headers.Authorization == "Bearer profesor" ? "PROFESOR" :
                c.Request.Headers.Authorization == "Bearer estudiante" ? "ESTUDIANTE" : "ADMINISTRADOR"
        }));
        backend.MapPost("/login", () => Results.Json(new { access_token = "prueba" }, statusCode: 201));
        backend.MapPost("/refresh", () => Results.Json(new { access_token = "renovado" }, statusCode: 201));
        backend.MapGet("/modulo/menu", (HttpContext c) => {
            Assert.StartsWith("Bearer ", c.Request.Headers.Authorization.ToString());
            Assert.False(c.Request.Headers.ContainsKey("X-Rol"));
            if (estadoPermisos != 200) return Results.StatusCode(estadoPermisos);
            if (menuInvalido) return Results.Json(new { modulos = "incorrecto" });
            return Results.Json(permisos.Select(id => new { idModulo = id, nombreModulo = id, activo = moduloActivo, orden = 0 }));
        });
        backend.MapPost("/bitacora", async (HttpContext c) => {
            if (estadoBitacora != 201) return Results.StatusCode(estadoBitacora);
            var registro = await c.Request.ReadFromJsonAsync<NuevoAvatar.Seguridad.Models.Bitacora>();
            Assert.NotNull(registro);
            visitas.Add(registro!);
            return Results.Json(registro, statusCode: 201);
        });
        backend.Map("/{**path}", async (HttpContext c) =>
        {
            Interlocked.Increment(ref negocios);
            if (c.Request.Method == "DELETE") { c.Response.StatusCode = 204; return; }
            if (c.Request.Method == "POST")
            {
                c.Response.StatusCode = 201;
                c.Response.Headers.Location = $"{c.Request.Scheme}://{c.Request.Host}{c.Request.Path}/42";
            }
            await c.Response.WriteAsJsonAsync(new {
                path = c.Request.Path.Value, query = c.Request.QueryString.Value,
                body = await new StreamReader(c.Request.Body).ReadToEndAsync(),
                authorization = c.Request.Headers.Authorization.ToString(),
                identidadInyectada = c.Request.Headers.ContainsKey("X-Rol") || c.Request.Headers.ContainsKey("X-Servicio-Key"),
                forwarded = c.Request.Headers.ContainsKey("X-Forwarded-Host")
            });
        });
        await backend.StartAsync();
        gateway = GatewayApplication.Create([], b => {
            b.WebHost.UseUrls("http://127.0.0.1:0");
            b.Logging.ClearProviders();
            b.Configuration.AddInMemoryCollection(CatalogoRutas.Todas.Select(r => r.Servicio).Distinct()
                .Select(s => new KeyValuePair<string, string?>("Destinos:" + s, backend.Urls.Single())));
        });
        await gateway.StartAsync();
        cliente = new HttpClient(new HttpClientHandler { AllowAutoRedirect = false }) { BaseAddress = new Uri(gateway.Urls.Single()) };
    }

    [Theory]
    [InlineData("GET", 200)] [InlineData("POST", 201)] [InlineData("PUT", 200)] [InlineData("DELETE", 204)]
    public async Task ConservaVerboCuerpoQueryRespuestaYLocation(string metodo, int status)
    {
        using var request = new HttpRequestMessage(new HttpMethod(metodo), "/gateway/admin/usuario?nombre=Ana%20Maria");
        request.Headers.Authorization = new("Bearer", "admin");
        request.Headers.Add("X-Rol", "ADMINISTRADOR"); request.Headers.Add("X-Servicio-Key", "inyectada");
        request.Headers.Add("X-Forwarded-Host", "ajeno.invalid");
        if (metodo is "POST" or "PUT") request.Content = new StringContent("{\"nombre\":\"Ana\"}", Encoding.UTF8, "application/json");
        using var response = await cliente.SendAsync(request);
        Assert.Equal(status, (int)response.StatusCode);
        if (status == 204) { Assert.Empty(await response.Content.ReadAsStringAsync()); return; }
        var echo = await response.Content.ReadFromJsonAsync<Eco>();
        Assert.Equal("/usuario", echo!.Path);
        Assert.Equal("?nombre=Ana%20Maria", echo.Query);
        Assert.Equal("Bearer admin", echo.Authorization);
        Assert.False(echo.IdentidadInyectada); Assert.False(echo.Forwarded);
        if (metodo is "POST" or "PUT") Assert.Contains("Ana", echo.Body);
        if (metodo == "POST") Assert.Equal("/gateway/admin/usuario/42", response.Headers.Location?.OriginalString);
    }

    [Theory]
    [InlineData("GET")]
    [InlineData("PUT")]
    public async Task ExpedientePropioRequiereEstudianteYReenviaVerboCuerpoYBearer(string metodo)
    {
        using var request = new HttpRequestMessage(new HttpMethod(metodo), "/gateway/expediente/propio");
        request.Headers.Authorization = new("Bearer", "estudiante");
        if (metodo == "PUT") request.Content = new StringContent(
            "{\"telefonos\":[\"777\"],\"provinciaId\":1,\"cantonId\":2,\"distritoId\":3,\"otrasSenas\":\"Casa\"}",
            Encoding.UTF8, "application/json");

        using var response = await cliente.SendAsync(request);

        Assert.Equal(HttpStatusCode.OK, response.StatusCode);
        var echo = await response.Content.ReadFromJsonAsync<Eco>();
        Assert.Equal("/expediente/propio", echo!.Path);
        Assert.Equal("Bearer estudiante", echo.Authorization);
        if (metodo == "PUT") Assert.Contains("\"distritoId\":3", echo.Body);
    }

    [Theory]
    [InlineData(null, "/gateway/admin/usuario", 401)]
    [InlineData(null, "/gateway/expediente/propio", 401)]
    [InlineData("invalido", "/gateway/admin/usuario", 401)]
    [InlineData("estudiante", "/gateway/admin/usuario", 403)]
    [InlineData("admin", "/gateway/expediente/propio", 403)]
    [InlineData("profesor", "/gateway/expediente/propio", 403)]
    [InlineData("profesor", "/gateway/admin/expediente/123", 403)]
    [InlineData("estudiante", "/gateway/mat/obtenernotas?grupoId=1", 403)]
    [InlineData("admin", "/gateway/admin/usuario/interno/datos", 404)]
    [InlineData("admin", "/gateway/admin/usuario/%69nterno/datos", 404)]
    public async Task RechazaAntesDeLlegarAlNegocio(string? token, string ruta, int status)
    {
        using var request = new HttpRequestMessage(HttpMethod.Get, ruta);
        if (token is not null) request.Headers.Authorization = new("Bearer", token);
        using var response = await cliente.SendAsync(request);
        Assert.Equal(status, (int)response.StatusCode);
        Assert.Equal(0, negocios);
    }

    [Fact]
    public async Task RoutingDeTodasLasFamiliasTieneDestinoYPolitica()
    {
        foreach (var ruta in CatalogoRutas.Todas.Where(r => r.Servicio != "Autenticacion"))
        {
            using var request = new HttpRequestMessage(new HttpMethod(ruta.Verbos[0]), ruta.Externa + (ruta.Subrutas ? "/7" : ""));
            var token = ruta.Roles.Contains("ADMINISTRADOR") ? "admin" :
                ruta.Roles.Contains("ESTUDIANTE") ? "estudiante" : "profesor";
            request.Headers.Authorization = new("Bearer", token);
            using var response = await cliente.SendAsync(request);
            Assert.True(response.IsSuccessStatusCode, ruta.Externa + ": " + response.StatusCode);
            var echo = await response.Content.ReadFromJsonAsync<Eco>();
            Assert.Equal(ruta.Interna + (ruta.Subrutas ? "/7" : ""), echo!.Path);
        }
        Assert.Equal(22, CatalogoRutas.Todas.Select(r => r.Servicio).Distinct().Count());
    }

    [Theory] [InlineData("login")] [InlineData("refresh")]
    public async Task SoloOperacionesPublicasAtraviesanSinToken(string operacion)
    {
        using var response = await cliente.PostAsJsonAsync("/gateway/auth/" + operacion, new { });
        Assert.Equal(HttpStatusCode.Created, response.StatusCode);
    }

    [Fact]
    public async Task DependenciaAuthCaidaDevuelve503()
    {
        await backend.StopAsync();
        using var request = new HttpRequestMessage(HttpMethod.Get, "/gateway/admin/usuario");
        request.Headers.Authorization = new("Bearer", "admin");
        using var response = await cliente.SendAsync(request);
        Assert.Equal(HttpStatusCode.ServiceUnavailable, response.StatusCode);
        Assert.Contains("Autenticacion no disponible", await response.Content.ReadAsStringAsync());
    }

    [Fact]
    public async Task DestinoCaidoDevuelve502SinConfundirConAuth()
    {
        await using var aislado = GatewayApplication.Create([], b => {
            b.WebHost.UseUrls("http://127.0.0.1:0"); b.Logging.ClearProviders();
            b.Configuration.AddInMemoryCollection(new Dictionary<string,string?> {
                ["Destinos:Autenticacion"] = backend.Urls.Single(), ["Destinos:Modulo"] = backend.Urls.Single(),
                ["Destinos:Usuario"] = "http://127.0.0.1:1" });
        });
        await aislado.StartAsync();
        using var http = new HttpClient { BaseAddress = new Uri(aislado.Urls.Single()) };
        http.DefaultRequestHeaders.Authorization = new("Bearer", "admin");
        using var response = await http.GetAsync("/gateway/admin/usuario");
        Assert.Equal(HttpStatusCode.BadGateway, response.StatusCode);
        Assert.Contains("Servicio destino no disponible", await response.Content.ReadAsStringAsync());
    }

    [Fact]
    public async Task RetirarPermisoBloqueaLaSiguientePeticionInclusoParaAdministrador()
    {
        cliente.DefaultRequestHeaders.Authorization = new("Bearer", "admin");
        using var primera = await cliente.GetAsync("/gateway/admin/usuario");
        Assert.Equal(HttpStatusCode.OK, primera.StatusCode);
        permisos.Remove("ADM4");
        using var segunda = await cliente.GetAsync("/gateway/admin/usuario");
        Assert.Equal(HttpStatusCode.Forbidden, segunda.StatusCode);
        Assert.Equal(1, negocios);
        using var menu = await cliente.GetAsync("/gateway/menu");
        Assert.DoesNotContain("ADM4", await menu.Content.ReadAsStringAsync());
    }

    [Theory]
    [InlineData("profesor", "/gateway/admin/usuario")]
    [InlineData("estudiante", "/gateway/mat/obtenernotas?grupoId=1")]
    public async Task AsignacionDeModuloNoAmpliaElPerfilPermitido(string token, string path)
    {
        cliente.DefaultRequestHeaders.Authorization = new("Bearer", token);
        using var respuesta = await cliente.GetAsync(path);
        Assert.Equal(HttpStatusCode.Forbidden, respuesta.StatusCode);
        Assert.Equal(0, negocios);
    }

    [Fact]
    public async Task ModuloInactivoNiegaApiYSeRetiraDelMenu()
    {
        moduloActivo = false;
        cliente.DefaultRequestHeaders.Authorization = new("Bearer", "profesor");
        using var respuesta = await cliente.GetAsync("/gateway/mat/profesor/grupos");
        Assert.Equal(HttpStatusCode.Forbidden, respuesta.StatusCode);
        using var menu = await cliente.GetAsync("/gateway/menu");
        Assert.Equal("[]", await menu.Content.ReadAsStringAsync());
        Assert.Equal(0, negocios);
    }

    [Theory]
    [InlineData(503, false)] [InlineData(403, false)] [InlineData(200, true)]
    public async Task FalloORespuestaInvalidaDePermisosNoPermiteLlegarAlNegocio(int status, bool invalido)
    {
        estadoPermisos = status; menuInvalido = invalido;
        cliente.DefaultRequestHeaders.Authorization = new("Bearer", "admin");
        using var respuesta = await cliente.GetAsync("/gateway/admin/usuario");
        Assert.Equal(HttpStatusCode.ServiceUnavailable, respuesta.StatusCode);
        Assert.Contains("Permisos no disponibles", await respuesta.Content.ReadAsStringAsync());
        Assert.Equal(0, negocios);
    }

    [Fact]
    public async Task MenuPropioNoAceptaRolDeQueryYNuncaRequierePermisoAdministrativo()
    {
        permisos.Clear(); permisos.Add("ADM17");
        cliente.DefaultRequestHeaders.Authorization = new("Bearer", "profesor");
        using var respuesta = await cliente.GetAsync("/gateway/menu?idRol=ADMINISTRADOR");
        Assert.Equal(HttpStatusCode.OK, respuesta.StatusCode);
        Assert.Contains("ADM17", await respuesta.Content.ReadAsStringAsync());
        using var otro = await cliente.GetAsync("/gateway/menu/ADMINISTRADOR");
        Assert.Equal(HttpStatusCode.Forbidden, otro.StatusCode);
        Assert.Equal(0, negocios);
    }

    [Theory]
    [InlineData("admin")]
    [InlineData("profesor")]
    public async Task DashboardRegistraIdentidadDeSesionSinAceptarUsuarioODescripcionDelCliente(string token)
    {
        cliente.DefaultRequestHeaders.Authorization = new("Bearer", token);
        using var respuesta = await cliente.GetAsync("/gateway/menu?vista=dashboard&usuario=otro@cuc.cr&descripcion=falso");
        Assert.Equal(HttpStatusCode.OK, respuesta.StatusCode);
        var registro = Assert.Single(visitas);
        Assert.Equal("persona@cuc.ac.cr", registro.Usuario);
        Assert.Equal("El usuario consulta el dashboard", registro.Descripcion);
        Assert.Equal("no-store", respuesta.Headers.CacheControl?.ToString());
    }

    [Fact]
    public async Task MenuNormalNoRegistraVisitaAlDashboard()
    {
        cliente.DefaultRequestHeaders.Authorization = new("Bearer", "profesor");
        using var respuesta = await cliente.GetAsync("/gateway/menu");
        Assert.Equal(HttpStatusCode.OK, respuesta.StatusCode);
        Assert.Empty(visitas);
    }

    [Theory]
    [InlineData(null, 401)]
    [InlineData("invalido", 401)]
    [InlineData("estudiante", 403)]
    public async Task DashboardNoRegistraVisitasSinPerfilPermitido(string? token, int esperado)
    {
        if (token is not null) cliente.DefaultRequestHeaders.Authorization = new("Bearer", token);
        using var respuesta = await cliente.GetAsync("/gateway/menu?vista=dashboard");
        Assert.Equal(esperado, (int)respuesta.StatusCode);
        Assert.Empty(visitas);
    }

    [Fact]
    public async Task FalloDeBitacoraNoConfirmaElRegistroDelDashboard()
    {
        estadoBitacora = 500;
        cliente.DefaultRequestHeaders.Authorization = new("Bearer", "admin");
        using var respuesta = await cliente.GetAsync("/gateway/menu?vista=dashboard");
        Assert.Equal(HttpStatusCode.ServiceUnavailable, respuesta.StatusCode);
        Assert.Contains("dashboard", await respuesta.Content.ReadAsStringAsync());
        Assert.Empty(visitas);
    }

    public async Task DisposeAsync()
    {
        cliente.Dispose(); await gateway.DisposeAsync(); await backend.DisposeAsync();
    }
    private sealed record Eco(string Path, string Query, string Body, string Authorization, bool IdentidadInyectada, bool Forwarded);
}
