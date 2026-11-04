using Microsoft.AspNetCore.Http;
using NuevoAvatar.Curso;
using NuevoAvatar.Curso.Entities;
using NuevoAvatar.Curso.Repository;
using NuevoAvatar.Curso.Services;
using Xunit;
using CursoEntity = NuevoAvatar.Curso.Entities.Curso;

namespace NuevoAvatar.Academico.Tests;

public class CursoFiltrosTest
{
    [Fact]
    public async Task CombinaCarreraYNivelEnRepositorio()
    {
        var repo = new RepositorioFalso();
        var servicio = new CursoService(repo, new CursoValidator(), null!);
        await servicio.GetFilteredAsync(3, 2);
        Assert.Equal((3, (byte?)2), repo.Filtro);
    }

    [Theory]
    [InlineData(0, 1)]
    [InlineData(1, 13)]
    public async Task RechazaFiltrosFueraDeRango(int carrera, byte nivel)
    {
        var servicio = new CursoService(new RepositorioFalso(), new CursoValidator(), null!);
        await Assert.ThrowsAsync<CursoValidationException>(() => servicio.GetFilteredAsync(carrera, nivel));
    }

    [Fact]
    public void PriorizaDetalleJsonEnBitacoraDeCurso()
    {
        var http = new DefaultHttpContext();
        http.Request.Method = "PUT";
        http.Request.Path = "/curso/4";
        http.Items["bitacora"] = "Modificación de curso. Anterior: {\"CursoId\":4} Actual: {\"CursoId\":4}";
        Assert.Contains("Anterior:", CursoAuthorizationFilter.ConstruirDescripcionHttp(http, null));

        var consulta = new DefaultHttpContext();
        consulta.Request.Method = "GET";
        consulta.Request.Path = "/curso";
        Assert.Equal("El usuario consulta /curso", CursoAuthorizationFilter.ConstruirDescripcionHttp(consulta, null));
    }

    private sealed class RepositorioFalso : ICursoRepository
    {
        public (int?, byte?) Filtro { get; private set; }
        public Task<IReadOnlyList<CursoEntity>> GetFilteredAsync(int? carreraId, byte? nivel) { Filtro = (carreraId, nivel); return Task.FromResult<IReadOnlyList<CursoEntity>>([]); }
        public Task<IReadOnlyList<CursoEntity>> GetAllAsync() => Task.FromResult<IReadOnlyList<CursoEntity>>([]);
        public Task<CursoEntity?> GetByIdAsync(int id) => Task.FromResult<CursoEntity?>(null);
        public Task<IReadOnlyList<CursoEntity>> GetByCarreraIdAsync(int id) => Task.FromResult<IReadOnlyList<CursoEntity>>([]);
        public Task<int> CreateAsync(CursoRequest curso) => Task.FromResult(1);
        public Task<bool> UpdateAsync(int id, CursoRequest curso) => Task.FromResult(true);
        public Task<bool> DeleteAsync(int id) => Task.FromResult(true);
    }
}
