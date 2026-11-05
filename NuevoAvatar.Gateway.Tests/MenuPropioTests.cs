using System.Net;
using System.Net.Http.Json;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using NuevoAvatar.Integracion;
using NuevoAvatar.Seguridad.Abstract;
using NuevoAvatar.Seguridad.Models;
using NuevoAvatarSeguridad.Controllers;
using Xunit;

namespace NuevoAvatar.Gateway.Tests;

public class MenuPropioTests
{
    [Fact]
    public async Task UsaElIdRealDelRolDeLaCuentaYNuncaElRolDeLaQuery()
    {
        var servicio = new Modulos();
        var bitacora = new Bitacoras();
        var datos = new DatosRemotos(new Clientes(), new ConfigurationBuilder().AddInMemoryCollection(
            new Dictionary<string, string?> { ["SERVICES_INTERNAL_KEY"] = new string('x', 32) }).Build());
        var controller = new ModuloController(servicio, bitacora, datos) {
            ControllerContext = new ControllerContext { HttpContext = new DefaultHttpContext() }
        };
        controller.HttpContext.Items["usuario"] = "docente@cuc.ac.cr";
        controller.HttpContext.Request.QueryString = new QueryString("?idRol=ADMINISTRADOR");
        var resultado = Assert.IsType<OkObjectResult>(await controller.ObtenerMenuPropio());
        Assert.Equal("ROL_DOCENTE_02", servicio.RolConsultado);
        Assert.Single(Assert.IsType<List<Modulo>>(resultado.Value));
        Assert.Contains("su menu", bitacora.Ultima!.Descripcion);
        Assert.Equal("docente@cuc.ac.cr", bitacora.Ultima.Usuario);
    }

    [Fact]
    public async Task SinIdentidadNoConsultaNingunRol()
    {
        var servicio = new Modulos();
        var controller = new ModuloController(servicio, new Bitacoras(), null!) {
            ControllerContext = new ControllerContext { HttpContext = new DefaultHttpContext() }
        };
        Assert.IsType<UnauthorizedResult>(await controller.ObtenerMenuPropio());
        Assert.Null(servicio.RolConsultado);
    }

    private sealed class Clientes : HttpMessageHandler, IHttpClientFactory
    {
        public HttpClient CreateClient(string name) => new(this, false);
        protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellation)
        {
            Assert.Contains("email=docente%40cuc.ac.cr", request.RequestUri!.Query);
            Assert.DoesNotContain("ADMINISTRADOR", request.RequestUri.ToString());
            return Task.FromResult(new HttpResponseMessage(HttpStatusCode.OK) {
                Content = JsonContent.Create(new[] { new { email = "docente@cuc.ac.cr", id_rol = "ROL_DOCENTE_02" } })
            });
        }
    }

    private sealed class Modulos : IModuloService
    {
        public string? RolConsultado;
        public Task<List<Modulo>> ObtenerMenu(string idRol) {
            RolConsultado = idRol;
            return Task.FromResult(new List<Modulo> { new() { IdModulo = "ADM17", NombreModulo = "Notas" } });
        }
        public Task<List<Modulo>> ObtenerTodos() => throw new NotImplementedException();
        public Task<Modulo?> ObtenerPorId(string id) => throw new NotImplementedException();
        public Task<Modulo> Crear(Modulo value) => throw new NotImplementedException();
        public Task<Modulo> Modificar(Modulo value) => throw new NotImplementedException();
        public Task<Modulo> Eliminar(Modulo value) => throw new NotImplementedException();
        public Task<List<string>> ObtenerRoles(string id) => throw new NotImplementedException();
        public Task ReemplazarRoles(string id, IReadOnlyCollection<string> roles) => throw new NotImplementedException();
    }

    private sealed class Bitacoras : IBitacoraService
    {
        public Bitacora? Ultima;
        public Task<Bitacora> Crear(Bitacora value) { Ultima = value; return Task.FromResult(value); }
        public Task<List<Bitacora>> ObtenerTodos() => throw new NotImplementedException();
        public Task<Bitacora?> ObtenerPorId(long id) => throw new NotImplementedException();
    }
}
