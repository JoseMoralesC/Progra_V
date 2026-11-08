using NuevoAvatar.Gateway;
using Xunit;

namespace NuevoAvatar.Gateway.Tests;

public class ClasificacionBitacoraTests
{
    [Theory]
    [InlineData("El usuario consulta periodo 15", "ADM12")]
    [InlineData("El usuario consulta periodos", "ADM12")]
    [InlineData("Consulta de todos los grupos.", "ADM13")]
    [InlineData("Consulta del grupo 12.", "ADM13")]
    [InlineData("El usuario consulta historial academico", "ADM18")]
    [InlineData("El usuario consulta historial académico", "ADM18")]
    [InlineData("El usuario consulta listado de estudiantes", "ADM19")]
    [InlineData("El usuario consulta /obtenernotasgrupo", "ADM17")]
    [InlineData("GET /usuario/ana", "ADM4")]
    public void ClasificaConsultasEmitidasPorLosServicios(string descripcion, string modulo)
    {
        Assert.Equal(modulo, ClasificacionBitacora.Modulo(descripcion));
        Assert.Equal("Consulta", ClasificacionBitacora.Accion(descripcion));
    }

    [Theory]
    [InlineData("Consulta de grupos y periodos")]
    [InlineData("El usuario consulta periodo desconocido")]
    [InlineData("El usuario consulta periodo 1 de otro módulo")]
    [InlineData("Consulta del grupo 12. JSON: {\"ruta\":\"/usuario\"}")]
    [InlineData("Registro sin formato. JSON: {\"ruta\":\"/usuario\"}")]
    [InlineData("{\"descripcion\":\"El usuario consulta /usuario\"}")]
    [InlineData("Referencia a GET /usuario")]
    public void OmiteTextosAmbiguosYReferenciasDentroDeDatos(string descripcion)
    {
        Assert.Null(ClasificacionBitacora.Modulo(descripcion));
    }
}
