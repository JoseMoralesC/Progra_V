using System.Net;
using System.Net.Http.Json;
using System.Text.Json;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using NuevoAvatar.Gateway.Seguridad;
using NuevoAvatar.Integracion;
using NuevoAvatar.Seguridad.Abstract;
using NuevoAvatar.Seguridad.DataAccess;
using NuevoAvatar.Seguridad.Models;
using NuevoAvatarSeguridad.Controllers;
using Xunit;

namespace NuevoAvatarSeguridad.Tests;

public class SesionYGatewayTests
{
    [Theory]
    [InlineData("Bearer valido", 204)]
    [InlineData("Bearer invalido", 401)]
    [InlineData("", 401)]
    public void LogoutExigeTokenYConservaUsuarioParaAuditoria(string header, int status)
    {
        var contexto = Contexto("/logout");
        contexto.Request.Headers.Authorization = header;
        var controller = new SesionController(new AuthFalso(), new IdentidadFalsa()) {
            ControllerContext = new ControllerContext { HttpContext = contexto }
        };
        var resultado = Assert.IsAssignableFrom<StatusCodeResult>(controller.Salir());
        Assert.Equal(status, resultado.StatusCode);
        if (status == 204) Assert.Equal("real@cuc.ac.cr", contexto.Items["usuario"]);
        else Assert.False(contexto.Items.ContainsKey("usuario"));
    }
    [Theory]
    [InlineData("PROFESOR", "p@cuc.ac.cr", 9, null)]
    [InlineData("ESTUDIANTE", "e@cuc.cr", null, "123")]
    public async Task Identidad_VinculaPorEmailTipoEIdentificacion(string rol, string email, int? profesor, string? estudiante)
    {
        var consultados = new List<Uri>();
        var factory = new Clientes(request =>
        {
            var uri = request.RequestUri!;
            consultados.Add(uri);
            return Json(uri.AbsolutePath switch
            {
                "/interno/datos/usuario" => new[] { new { email, nombre = "Nombre", identificacion = "123", tipo_identificacion = "CEDULA", id_rol = "R" } },
                "/interno/datos/rol" => (object)new[] { new { nombre_rol = rol } },
                _ => new[] { new { ProfesorId = 9 } }
            });
        });
        var identidad = new IdentidadSesionService(Remotos(factory));
        var sesion = await identidad.Obtener(email);
        Assert.Equal(profesor, sesion!.ProfesorId);
        Assert.Equal(estudiante, sesion.EstudianteId);
        var query = consultados.Last().Query;
        Assert.Contains("Identificacion=123", query);
        Assert.Contains("TipoIdentificacion=CEDULA", query);
        Assert.Contains("Email=" + Uri.EscapeDataString(email), query);
    }

    [Fact]
    public async Task Identidad_NoVinculaOtroExpedienteYNoAsumeRolPorId()
    {
        var factory = new Clientes(request => Json(request.RequestUri!.AbsolutePath switch
        {
            "/interno/datos/usuario" => new[] { new { email = "e@cuc.cr", nombre = "E", identificacion = "123", tipo_identificacion = "CEDULA", id_rol = "ADMIN" } },
            "/interno/datos/rol" => (object)new[] { new { nombre_rol = "ESTUDIANTE" } },
            _ => Array.Empty<object>()
        }));
        var sesion = await new IdentidadSesionService(Remotos(factory)).Obtener("e@cuc.cr");
        Assert.Equal("ESTUDIANTE", sesion!.Rol);
        Assert.Null(sesion.EstudianteId);
        Assert.Null(sesion.ProfesorId);
    }

    [Fact]
    public async Task Sesion_NoConfiaEnHeadersDeIdentidad()
    {
        var identidad = new IdentidadFalsa();
        var controller = new SesionController(new AuthFalso(), identidad)
        { ControllerContext = new ControllerContext { HttpContext = Contexto("/sesion") } };
        controller.Request.Headers["X-Usuario"] = "victima@cuc.cr";
        Assert.IsType<UnauthorizedResult>(await controller.Obtener());
        Assert.Null(identidad.Consultado);
        controller.Request.Headers.Authorization = "Bearer valido";
        var ok = Assert.IsType<OkObjectResult>(await controller.Obtener());
        Assert.Equal("real@cuc.ac.cr", identidad.Consultado);
        Assert.IsType<SesionActual>(ok.Value);
        Assert.Equal("no-store", controller.Response.Headers.CacheControl);
    }

    [Theory]
    [InlineData("/gateway/auth/login", "POST", 200, 0)]
    [InlineData("/gateway/auth/refresh", "POST", 200, 0)]
    [InlineData("/gateway/auth/login", "GET", 401, 0)]
    [InlineData("/gateway/mat/obtenernotas", "GET", 401, 0)]
    [InlineData("/gateway/mat/interno/datos/NotaRubro", "GET", 404, 0)]
    [InlineData("/gateway/mat/%69nterno/datos/NotaRubro", "GET", 404, 0)]
    public async Task Gateway_ExcepcionesSonExplicitas(string ruta, string verbo, int status, int llamadas)
    {
        var contexto = Contexto(ruta);
        contexto.Request.Method = verbo;
        var factory = new Clientes(_ => throw new InvalidOperationException("No debe consultar Auth"));
        bool destino = false;
        await new SeguridadGatewayMiddleware(_ => { destino = true; return Task.CompletedTask; }).InvokeAsync(contexto, factory);
        Assert.Equal(status, contexto.Response.StatusCode);
        Assert.Equal(status == 200, destino);
        Assert.Equal(llamadas, factory.Llamadas);
    }

    [Theory]
    [InlineData("PROFESOR", "PROFESOR", true, 200)]
    [InlineData("ESTUDIANTE", "PROFESOR", true, 403)]
    [InlineData("PROFESOR", "PROFESOR", false, 401)]
    [InlineData("PROFESOR", null, true, 403)]
    public async Task Gateway_ValidaTokenYRolAntesDelDestino(string rol, string? permitido, bool valido, int status)
    {
        var context = Contexto("/gateway/mat/obtenernotas");
        context.Request.Headers.Authorization = "Bearer token";
        context.Request.Headers["X-Rol"] = "PROFESOR";
        context.Request.Headers["X-Servicio-Key"] = "clave-inyectada";
        if (permitido is not null)
            context.SetEndpoint(new Endpoint(_ => Task.CompletedTask, new EndpointMetadataCollection(new AccesoGateway(permitido)), "prueba"));
        var factory = new Clientes(r => r.RequestUri!.AbsolutePath == "/validate" ? Json(valido) : Json(Sesion(rol)));
        bool destino = false;
        await new SeguridadGatewayMiddleware(_ => { destino = true; return Task.CompletedTask; }).InvokeAsync(context, factory);
        Assert.Equal(status, context.Response.StatusCode);
        Assert.Equal(status == 200, destino);
        Assert.False(context.Request.Headers.ContainsKey("X-Servicio-Key"));
        Assert.False(context.Request.Headers.ContainsKey("X-Rol"));
        if (destino) Assert.True(context.User.IsInRole("PROFESOR"));
    }

    [Fact]
    public async Task Gateway_DependenciaCaidaNoPermiteLlegarAlDestino()
    {
        var context = Contexto("/gateway/admin/usuario");
        context.Request.Headers.Authorization = "Bearer token";
        var factory = new Clientes(_ => throw new HttpRequestException("Conexion fallida"));
        await new SeguridadGatewayMiddleware(_ => throw new Exception("No debe pasar")).InvokeAsync(context, factory);
        Assert.Equal(503, context.Response.StatusCode);
    }

    private static DatosRemotos Remotos(Clientes factory) => new(factory, new ConfigurationBuilder()
        .AddInMemoryCollection(new Dictionary<string, string?> { ["SERVICES_INTERNAL_KEY"] = new string('k', 32) }).Build());
    private static SesionActual Sesion(string rol) => new("real@cuc.ac.cr", "Real", rol, "CEDULA", "123", null, 9);
    private static HttpResponseMessage Json(object valor) => new(HttpStatusCode.OK) { Content = JsonContent.Create(valor) };
    private static DefaultHttpContext Contexto(string ruta)
    {
        var context = new DefaultHttpContext();
        context.Request.Path = ruta;
        context.Response.Body = new MemoryStream();
        return context;
    }
    private sealed class Clientes(Func<HttpRequestMessage, HttpResponseMessage> respuesta) : IHttpClientFactory
    {
        public int Llamadas { get; private set; }
        public HttpClient CreateClient(string name) => new(new Handler(r => { Llamadas++; return respuesta(r); }))
            { BaseAddress = new Uri("http://localhost:5158") };
    }
    private sealed class Handler(Func<HttpRequestMessage, HttpResponseMessage> respuesta) : HttpMessageHandler
    {
        protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken) => Task.FromResult(respuesta(request));
    }
    private sealed class AuthFalso : IAuthService
    {
        public string? ValidarToken(string token) => token == "valido" ? "real@cuc.ac.cr" : null;
        public Task<LoginResponse?> Login(string email, string password) => throw new NotSupportedException();
        public Task<LoginResponse?> Refresh(string refreshToken) => throw new NotSupportedException();
    }
    private sealed class IdentidadFalsa : IIdentidadSesionService
    {
        public string? Consultado { get; private set; }
        public Task<SesionActual?> Obtener(string email) { Consultado = email; return Task.FromResult<SesionActual?>(Sesion("PROFESOR")); }
    }
}
