using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using NuevoAvatar.Seguridad.Abstract;
using NuevoAvatar.Seguridad.Models;
using NuevoAvatarSeguridad.Controllers;
using Xunit;

namespace NuevoAvatarSeguridad.Tests;

public class ParametroConfidencialidadTests
{
    [Fact]
    public async Task ConsultarMailpassDevuelveMascara()
    {
        var servicio = new ParametrosFalsos(new Parametro { IdParametro = "MAILPASS", Valor = "secreto-real" });
        var controller = CrearController(servicio, new BitacoraFalsa());

        var resultado = Assert.IsType<OkObjectResult>(await controller.ObtenerPorId("MAILPASS"));
        var parametro = Assert.IsType<Parametro>(resultado.Value);

        Assert.Equal("********", parametro.Valor);
        Assert.Equal("secreto-real", servicio.Actual!.Valor);
    }

    [Fact]
    public async Task GuardarMascaraConservaSecretoYNoLoAudita()
    {
        var servicio = new ParametrosFalsos(new Parametro { IdParametro = "MAILPASS", Valor = "secreto-real" });
        var bitacora = new BitacoraFalsa();
        var controller = CrearController(servicio, bitacora);

        var resultado = Assert.IsType<OkObjectResult>(await controller.Modificar(
            "MAILPASS", new Parametro { IdParametro = "MAILPASS", Valor = "********" }));
        var parametro = Assert.IsType<Parametro>(resultado.Value);

        Assert.Equal("secreto-real", servicio.Actual!.Valor);
        Assert.Equal("********", parametro.Valor);
        Assert.DoesNotContain("secreto-real", bitacora.UltimaDescripcion);
        Assert.DoesNotContain("********", bitacora.UltimaDescripcion);
    }

    private static ParametroController CrearController(IParametroService servicio, IBitacoraService bitacora) =>
        new(servicio, bitacora)
        {
            ControllerContext = new ControllerContext { HttpContext = new DefaultHttpContext() }
        };

    private sealed class ParametrosFalsos(Parametro? actual) : IParametroService
    {
        public Parametro? Actual { get; private set; } = actual;
        public Task<List<Parametro>> ObtenerTodos() => Task.FromResult(Actual is null ? [] : new List<Parametro> { Actual });
        public Task<Parametro?> ObtenerPorId(string idParametro) => Task.FromResult(Actual);
        public Task<Parametro> Crear(Parametro parametro) { Actual = parametro; return Task.FromResult(parametro); }
        public Task<Parametro> Modificar(Parametro parametro) { Actual = parametro; return Task.FromResult(parametro); }
        public Task<Parametro> Eliminar(Parametro parametro) { Actual = null; return Task.FromResult(parametro); }
    }

    private sealed class BitacoraFalsa : IBitacoraService
    {
        public string UltimaDescripcion { get; private set; } = string.Empty;
        public Task<List<Bitacora>> ObtenerTodos() => Task.FromResult(new List<Bitacora>());
        public Task<Bitacora?> ObtenerPorId(long idBitacora) => Task.FromResult<Bitacora?>(null);
        public Task<Bitacora> Crear(Bitacora bitacora) { UltimaDescripcion = bitacora.Descripcion; return Task.FromResult(bitacora); }
    }
}
