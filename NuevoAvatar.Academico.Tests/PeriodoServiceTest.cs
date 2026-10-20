using NuevoAvatar.Periodo.Nuevo.Entities;
using NuevoAvatar.Periodo.Nuevo.Services;
using Xunit;

public class PeriodoServiceTest
{
    [Fact]
    public async Task RechazaFechaFinalAnteriorAlInicioAntesDeGuardar()
    {
        var service=new PeriodoService(null!,new PeriodoValidator());
        await Assert.ThrowsAsync<ArgumentException>(()=>service.CrearAsync(new PeriodoRequest {
            Anio=2026,NumeroPeriodo=1,FechaInicio=new DateTime(2026,1,2),FechaFin=new DateTime(2026,1,1)
        }));
    }

    [Fact]
    public void ContratoActualPermiteFechasIguales()
    {
        new PeriodoValidator().Validar(new PeriodoRequest {
            Anio=2026,NumeroPeriodo=1,FechaInicio=new DateTime(2026,1,1),FechaFin=new DateTime(2026,1,1)
        });
    }
}
