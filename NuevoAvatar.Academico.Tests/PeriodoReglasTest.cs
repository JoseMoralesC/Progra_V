using NuevoAvatar.Periodo.Nuevo.Entities;
using NuevoAvatar.Periodo.Nuevo.Repository;
using NuevoAvatar.Periodo.Nuevo.Services;
using Xunit;

public class PeriodoReglasTest
{
    [Fact]
    public void EstadoSeDerivaDeFechas()
    {
        var hoy = DateTime.Today;
        Assert.Equal("ACTIVO", new PeriodoRequest { FechaInicio = hoy, FechaFin = hoy }.Estado);
        Assert.Equal("FUTURO", new PeriodoRequest { FechaInicio = hoy.AddDays(1), FechaFin = hoy.AddDays(2) }.Estado);
        Assert.Equal("CERRADO", new PeriodoRequest { FechaInicio = hoy.AddDays(-2), FechaFin = hoy.AddDays(-1) }.Estado);
    }

    [Fact]
    public async Task DuplicadoProduceConflicto()
    {
        var repo = new Repo();
        repo.Items.Add(Periodo(1));
        var s = new PeriodoService(repo, new PeriodoValidator());
        await Assert.ThrowsAsync<PeriodoConflictoException>(() => s.CrearAsync(Periodo(1)));
        Assert.Single(repo.Items);
    }

    [Fact]
    public async Task PermiteFechasSuperpuestasSiElNumeroEsDistinto()
    {
        var repo = new Repo();
        repo.Items.Add(Periodo(1));
        var s = new PeriodoService(repo, new PeriodoValidator());
        await s.CrearAsync(Periodo(2));
        Assert.Equal(2, repo.Items.Count);
    }

    [Fact]
    public async Task EditarExcluyeElRegistroPropioYConservaOtrosNumeros()
    {
        var repo = new Repo();
        repo.Items.Add(Periodo(1));
        repo.Items.Add(Periodo(2));
        var s = new PeriodoService(repo, new PeriodoValidator());
        Assert.True(await s.ModificarAsync(2, Periodo(2)));
        await Assert.ThrowsAsync<PeriodoConflictoException>(() => s.ModificarAsync(2, Periodo(1)));
    }

    [Fact]
    public async Task DosAltasEnLaMismaInstanciaNoDuplicanElPeriodo()
    {
        var repo = new Repo();
        var s = new PeriodoService(repo, new PeriodoValidator());
        async Task<bool> Alta()
        {
            try { await s.CrearAsync(Periodo(1)); return true; }
            catch (PeriodoConflictoException) { return false; }
        }
        var resultados = await Task.WhenAll(Alta(), Alta());
        Assert.Single(resultados, x => x);
        Assert.Single(repo.Items);
    }

    private static PeriodoRequest Periodo(byte numero) => new() {
        PeriodoId = numero, Anio = 2026, NumeroPeriodo = numero,
        FechaInicio = new(2026, 1, 1), FechaFin = new(2026, 2, 1)
    };

    private sealed class Repo : IPeriodoRepository
    {
        public List<PeriodoRequest> Items = [];
        public Task<bool> ExisteDuplicadoAsync(short anio, byte numero, int? excluirId = null) =>
            Task.FromResult(Items.Any(x => x.Anio == anio && x.NumeroPeriodo == numero && x.PeriodoId != excluirId));
        public Task<bool> ExisteSolapamientoAsync(short anio, byte numero, DateTime inicio, DateTime fin, int? excluirId = null) =>
            Task.FromResult(Items.Any(x => x.Anio == anio && x.NumeroPeriodo == numero && x.PeriodoId != excluirId &&
                x.FechaInicio <= fin && x.FechaFin >= inicio));
        public Task<IEnumerable<PeriodoRequest>> ObtenerTodosAsync() => Task.FromResult<IEnumerable<PeriodoRequest>>(Items);
        public Task<PeriodoRequest?> ObtenerPorIdAsync(int id) => Task.FromResult(Items.FirstOrDefault(x => x.PeriodoId == id));
        public async Task<int> CrearAsync(PeriodoRequest r) { await Task.Yield(); Items.Add(r); return r.PeriodoId; }
        public Task<bool> ModificarAsync(int id, PeriodoRequest r) => Task.FromResult(true);
        public Task<bool> EliminarAsync(int id) => Task.FromResult(true);
    }
}
