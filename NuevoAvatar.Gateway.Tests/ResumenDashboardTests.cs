using System.Net;
using System.Net.Http.Json;
using System.Text.Json;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using NuevoAvatar.Gateway;
using NuevoAvatar.Seguridad.Models;
using Xunit;

namespace NuevoAvatar.Gateway.Tests;

public class ResumenDashboardTests : IAsyncLifetime
{
    private WebApplication backend = null!, gateway = null!;
    private HttpClient cliente = null!;
    private readonly List<Bitacora> registros = [];
    private readonly List<Correo> correos = [];
    private readonly HashSet<string> permisos = ["ADM17", "ADM4", "ADM22"];
    private readonly List<string> consultas = [];
    private bool notasCaidas, bitacoraCaida, notificacionesCaidas;

    public async Task InitializeAsync()
    {
        var builder = WebApplication.CreateBuilder();
        builder.WebHost.UseUrls("http://127.0.0.1:0");
        builder.Logging.ClearProviders();
        backend = builder.Build();
        backend.MapGet("/validate", (HttpContext c) => c.Request.Headers.Authorization == "Bearer invalido"
            ? Results.Unauthorized() : Results.Ok(true));
        backend.MapGet("/sesion", (HttpContext c) => Results.Ok(new {
            usuarioID = c.Request.Headers.Authorization == "Bearer profesorB" ? "bea@cuc.ac.cr" : "ana@cuc.ac.cr",
            nombre = "Profesor", rol = c.Request.Headers.Authorization == "Bearer admin" ? "ADMINISTRADOR" :
                c.Request.Headers.Authorization == "Bearer estudiante" ? "ESTUDIANTE" : "PROFESOR"
        }));
        backend.MapGet("/modulo/menu", () => Results.Ok(permisos.Select(id => new {
            idModulo = id, nombreModulo = id, activo = true, orden = 1
        })));
        backend.MapGet("/bitacora", () => bitacoraCaida ? Results.StatusCode(503) : Results.Ok(registros));
        backend.MapGet("/profesor/grupos", (HttpContext c) => {
            consultas.Add(c.Request.Path + c.Request.QueryString);
            return notasCaidas ? Results.StatusCode(503) : Results.Ok(c.Request.Headers.Authorization == "Bearer profesorB"
                ? new[] { new { grupoId = 20 } } : new[] { new { grupoId = 10 }, new { grupoId = 11 } });
        });
        backend.MapGet("/obtenernotasgrupo", (int grupoId) => {
            consultas.Add("matriculas:" + grupoId);
            return Results.Ok(new[] { new { matriculaId = grupoId * 100 } });
        });
        backend.MapGet("/obtenerdesglose", (int grupoId) => {
            consultas.Add("rubros:" + grupoId);
            return Results.Ok(new[] { new { id = grupoId * 10, grupoId } });
        });
        backend.MapGet("/notificar/historial", (HttpContext c, int page, int size) => {
            consultas.Add(c.Request.Path + c.Request.QueryString);
            if (notificacionesCaidas) return Results.StatusCode(503);
            var destinatario = c.Request.Query["destinatario"].ToString();
            var lista = correos.Where(n => n.Destinatarios.Contains(destinatario, StringComparison.OrdinalIgnoreCase))
                .OrderByDescending(n => n.Fecha).ToList();
            return Results.Ok(new { items = lista.Skip(page * size).Take(size), total = lista.Count });
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

    private void Evento(int id, string usuario, string descripcion)
    {
        registros.Add(new Bitacora { IdBitacora = id, Usuario = usuario, Descripcion = descripcion,
            FechaHora = new DateTime(2026, 11, 8, 12, 0, 0).AddMinutes(id) });
    }

    private async Task<HttpResponseMessage> Consultar(string? token, string ruta)
    {
        using var solicitud = new HttpRequestMessage(HttpMethod.Get, ruta);
        if (token is not null) solicitud.Headers.Authorization = new("Bearer", token);
        solicitud.Headers.Add("X-Usuario", "ajeno@cuc.ac.cr");
        solicitud.Headers.Add("X-Rol", "ADMINISTRADOR");
        return await cliente.SendAsync(solicitud);
    }

    [Fact]
    public async Task ProfesorVeAccionesPropiasYGruposAutorizadosSinDatosAdministrativos()
    {
        Evento(1, "ana@cuc.ac.cr", "El usuario consulta /obtenernotasgrupo");
        Evento(2, "otro@cuc.ac.cr", "Alta de nota. JSON nuevo: {\"matriculaId\":1000,\"rubroId\":100,\"nota\":95}");
        Evento(3, "otro@cuc.ac.cr", "Alta de nota. JSON nuevo: {\"matriculaId\":2000,\"rubroId\":200,\"nota\":80}");
        Evento(4, "otro@cuc.ac.cr", "Alta de desglose. JSON nuevo: {\"grupoId\":10,\"rubros\":[]}");
        Evento(5, "ana@cuc.ac.cr", "El usuario consulta /usuario");
        Evento(6, "otro@cuc.ac.cr", "Registro sin formato. JSON: {\"grupoId\":10}");
        Evento(7, "otro@cuc.ac.cr", "Modificacion de nota. JSON anterior: {\"matriculaId\":1000,\"rubroId\":100}. JSON actual: {\"matriculaId\":2000,\"rubroId\":200}");
        Evento(8, "otro@cuc.ac.cr", "Alta de nota. JSON nuevo: {\"matriculaId\":1000,\"rubroId\":110}");
        Evento(9, "otro@cuc.ac.cr", "Alta de desglose. JSON nuevo: {\"grupoId\":99}");
        Evento(10, "otro@cuc.ac.cr", "Alta de nota. JSON nuevo: {\"matriculaId\":1000,\"rubroId\":100");
        using var respuesta = await Consultar("profesorA", "/gateway/admin/bitacora?vista=dashboard&usuario=otro@cuc.ac.cr&grupoId=20&idRol=ADMINISTRADOR");
        Assert.Equal(HttpStatusCode.OK, respuesta.StatusCode);
        var eventos = await respuesta.Content.ReadFromJsonAsync<List<ResumenDashboard.Evento>>();
        Assert.Equal(new long[] { 4, 2, 1 }, eventos!.Select(e => e.IdBitacora));
        Assert.DoesNotContain("rubros:20", consultas);
        var texto = await respuesta.Content.ReadAsStringAsync();
        Assert.DoesNotContain("matriculaId", texto);
        Assert.DoesNotContain("95", texto);
        using var otraCuenta = await Consultar("profesorB", "/gateway/admin/bitacora?vista=dashboard");
        var ajenos = await otraCuenta.Content.ReadFromJsonAsync<List<ResumenDashboard.Evento>>();
        Assert.Equal(new long[] { 3 }, ajenos!.Select(e => e.IdBitacora));
    }

    [Fact]
    public async Task AdministradorRecibeDiezEventosDeModulosPermitidosOrdenadosDespuesDeFiltrar()
    {
        permisos.Clear(); permisos.Add("ADM4");
        for (var i = 1; i <= 15; i++) Evento(i, "otro@cuc.ac.cr", "El usuario consulta /usuario");
        Evento(99, "otro@cuc.ac.cr", "El usuario consulta /rol");
        Evento(100, "otro@cuc.ac.cr", "Texto desconocido que incluye /usuario");
        using var respuesta = await Consultar("admin", "/gateway/admin/bitacora?vista=dashboard&size=1000&modulo=ADM5");
        var eventos = await respuesta.Content.ReadFromJsonAsync<List<ResumenDashboard.Evento>>();
        Assert.Equal(Enumerable.Range(6, 10).Reverse().Select(i => (long)i), eventos!.Select(e => e.IdBitacora));
        permisos.Clear();
        using var sinPermisos = await Consultar("admin", "/gateway/admin/bitacora?vista=dashboard");
        Assert.Empty((await sinPermisos.Content.ReadFromJsonAsync<List<ResumenDashboard.Evento>>())!);
    }

    [Theory]
    [InlineData(null, "/gateway/admin/bitacora?vista=dashboard", 401)]
    [InlineData("invalido", "/gateway/admin/bitacora?vista=dashboard", 401)]
    [InlineData("estudiante", "/gateway/admin/bitacora?vista=dashboard", 403)]
    [InlineData("profesorA", "/gateway/admin/bitacora", 403)]
    [InlineData("profesorA", "/gateway/admin/bitacora/1?vista=dashboard", 403)]
    [InlineData("profesorA", "/gateway/admin/bitacora?vista=dashboard&vista=global", 403)]
    [InlineData("profesorA", "/gateway/fin/notificar/historial", 403)]
    [InlineData("profesorA", "/gateway/fin/notificar/1?vista=dashboard", 403)]
    [InlineData("estudiante", "/gateway/fin/notificar/historial?vista=dashboard", 403)]
    public async Task ResumenNoAbreLaConsultaGlobalNiElDetalle(string? token, string ruta, int esperado)
    {
        using var respuesta = await Consultar(token, ruta);
        Assert.Equal(esperado, (int)respuesta.StatusCode);
        Assert.Empty(consultas);
    }

    [Theory]
    [InlineData("/gateway/admin/bitacora?vista=dashboard")]
    [InlineData("/gateway/fin/notificar/historial?vista=dashboard")]
    public async Task ProfesorNoPuedeEscribirAunqueIndiqueVistaDashboard(string ruta)
    {
        cliente.DefaultRequestHeaders.Authorization = new("Bearer", "profesorA");
        using var respuesta = await cliente.PostAsJsonAsync(ruta, new { usuario = "otro@cuc.ac.cr" });
        Assert.Equal(HttpStatusCode.Forbidden, respuesta.StatusCode);
    }

    [Fact]
    public async Task CorreosSeFiltranPorDestinatarioCompletoAntesDelLimite()
    {
        var fecha = new DateTimeOffset(2026, 11, 8, 12, 0, 0, TimeSpan.Zero);
        for (var i = 0; i < 100; i++) correos.Add(new Correo(i + 1, "xana@cuc.ac.cr", "Ajeno", "ENVIADA", fecha.AddMinutes(100 - i)));
        for (var i = 0; i < 12; i++) correos.Add(new Correo(101 + i, "ANA@cuc.ac.cr,otro@cuc.ac.cr", "Aviso " + i, "ENVIADA", fecha.AddMinutes(-i)));
        correos.Add(new Correo(300, "ana@cuc.ac.cr", "No enviado", "ERROR", fecha.AddMinutes(200)));
        correos.Add(new Correo(400, "bea@cuc.ac.cr", "Otro profesor", "ENVIADA", fecha));
        permisos.Clear();
        using var respuesta = await Consultar("profesorA", "/gateway/fin/notificar/historial?vista=dashboard&destinatario=bea@cuc.ac.cr&size=1000");
        Assert.Equal(HttpStatusCode.OK, respuesta.StatusCode);
        var json = await respuesta.Content.ReadFromJsonAsync<JsonElement>();
        Assert.Equal(10, json.GetProperty("items").GetArrayLength());
        Assert.Equal(101, json.GetProperty("items")[0].GetProperty("id").GetInt32());
        Assert.Equal(110, json.GetProperty("items")[9].GetProperty("id").GetInt32());
        Assert.DoesNotContain("destinatarios", json.ToString());
        Assert.DoesNotContain("html", json.ToString());
        Assert.Contains(consultas, c => c.Contains("page=1"));
        Assert.All(consultas, c => Assert.Contains("destinatario=ana%40cuc.ac.cr", c));
        using var otraCuenta = await Consultar("profesorB", "/gateway/fin/notificar/historial?vista=dashboard");
        var otra = await otraCuenta.Content.ReadFromJsonAsync<JsonElement>();
        Assert.Equal(400, Assert.Single(otra.GetProperty("items").EnumerateArray()).GetProperty("id").GetInt32());
    }

    [Fact]
    public async Task NotificacionesAdministrativasRequierenPermisoDelModulo()
    {
        permisos.Remove("ADM22");
        using var respuesta = await Consultar("admin", "/gateway/fin/notificar/historial?vista=dashboard");
        Assert.Equal(HttpStatusCode.Forbidden, respuesta.StatusCode);
        Assert.Empty(consultas);
    }

    [Theory]
    [InlineData("notas")]
    [InlineData("bitacora")]
    [InlineData("notificacion")]
    public async Task FalloDeDependenciaDevuelveErrorSinExponerListaGlobal(string servicio)
    {
        notasCaidas = servicio == "notas";
        bitacoraCaida = servicio == "bitacora";
        notificacionesCaidas = servicio == "notificacion";
        Evento(1, "otro@cuc.ac.cr", "Alta de desglose. JSON nuevo: {\"grupoId\":10}");
        using var respuesta = await Consultar("profesorA", servicio == "notificacion"
            ? "/gateway/fin/notificar/historial?vista=dashboard" : "/gateway/admin/bitacora?vista=dashboard");
        Assert.Equal(HttpStatusCode.ServiceUnavailable, respuesta.StatusCode);
        Assert.DoesNotContain("grupoId", await respuesta.Content.ReadAsStringAsync());
    }

    [Fact]
    public async Task DatosVaciosDevuelvenListasVacias()
    {
        using var bitacoras = await Consultar("profesorA", "/gateway/admin/bitacora?vista=dashboard");
        Assert.Equal("[]", await bitacoras.Content.ReadAsStringAsync());
        using var notificaciones = await Consultar("profesorA", "/gateway/fin/notificar/historial?vista=dashboard");
        var datos = await notificaciones.Content.ReadFromJsonAsync<JsonElement>();
        Assert.Empty(datos.GetProperty("items").EnumerateArray());
    }

    public async Task DisposeAsync()
    {
        cliente.Dispose(); await gateway.DisposeAsync(); await backend.DisposeAsync();
    }
    private sealed record Correo(long Id, string Destinatarios, string Asunto, string Estado, DateTimeOffset Fecha);
}
