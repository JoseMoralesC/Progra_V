using System.Net;
using System.Net.Http.Json;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Configuration;
using NuevoAvatar.Curso.Entities;
using NuevoAvatar.Curso.Repository;
using NuevoAvatar.Curso.Services;
using NuevoAvatar.Integracion;
using Xunit;
using CursoEntity = NuevoAvatar.Curso.Entities.Curso;

public class CursoAuditoriaTest
{
    [Fact]
    public async Task AltaConservaElRegistroPersistidoEnJson()
    {
        var (service, http, _) = Crear();
        await service.CreateAsync(new CursoRequest { CarreraId = 1, Nivel = 1, Nombre = "Programación" });
        var registro = Assert.IsType<string>(http.Items["curso.cambio"]);
        Assert.Contains("Creación", registro);
        Assert.Contains("CursoId", registro);
        Assert.Contains("Actual:", registro);
    }

    [Fact]
    public async Task ModificacionConservaAnteriorYActual()
    {
        var (service, http, _) = Crear();
        Assert.True(await service.UpdateAsync(1, new CursoRequest { CarreraId = 1, Nivel = 2, Nombre = "Bases de datos" }));
        var registro = Assert.IsType<string>(http.Items["curso.cambio"]);
        Assert.Contains("Anterior:", registro);
        Assert.Contains("Actual:", registro);
        Assert.Contains("Matemática", System.Text.RegularExpressions.Regex.Unescape(registro));
        Assert.Contains("Bases de datos", registro);
    }

    [Fact]
    public async Task EliminacionConservaElRegistroEliminado()
    {
        var (service, http, repo) = Crear();
        Assert.True(await service.DeleteAsync(1));
        Assert.Null(repo.Actual);
        var registro = Assert.IsType<string>(http.Items["curso.cambio"]);
        Assert.Contains("Eliminación", registro);
        Assert.Contains("CursoId", registro);
    }

    private static (CursoService, DefaultHttpContext, Repo) Crear()
    {
        var http = new DefaultHttpContext();
        var repo = new Repo();
        var datos = new DatosRemotos(new Clientes(), new ConfigurationBuilder().AddInMemoryCollection(
            new Dictionary<string, string?> { ["SERVICES_INTERNAL_KEY"] = new string('x', 32) }).Build());
        return (new CursoService(repo, new CursoValidator(), datos, new HttpContextAccessor { HttpContext = http }), http, repo);
    }

    private sealed class Clientes : HttpMessageHandler, IHttpClientFactory
    {
        public HttpClient CreateClient(string name) => new(this, false);
        protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellation) =>
            Task.FromResult(new HttpResponseMessage(HttpStatusCode.OK) {
                Content = request.RequestUri!.AbsolutePath.Contains("/referencias/")
                    ? JsonContent.Create(false) : JsonContent.Create(new[] { new { CarreraId = 1 } })
            });
    }

    private sealed class Repo : ICursoRepository
    {
        public CursoEntity? Actual = new() { CursoId = 1, CarreraId = 1, Nivel = 1, Nombre = "Matemática" };
        public Task<CursoEntity?> GetByIdAsync(int id) => Task.FromResult(Actual);
        public Task<IReadOnlyList<CursoEntity>> GetAllAsync() => Task.FromResult<IReadOnlyList<CursoEntity>>([]);
        public Task<IReadOnlyList<CursoEntity>> GetFilteredAsync(int? carreraId, byte? nivel) => GetAllAsync();
        public Task<IReadOnlyList<CursoEntity>> GetByCarreraIdAsync(int id) => GetAllAsync();
        public Task<int> CreateAsync(CursoRequest r) { Guardar(r); return Task.FromResult(1); }
        public Task<bool> UpdateAsync(int id, CursoRequest r) { Guardar(r); return Task.FromResult(true); }
        public Task<bool> DeleteAsync(int id) { Actual = null; return Task.FromResult(true); }
        private void Guardar(CursoRequest r) => Actual = new() { CursoId = 1, CarreraId = r.CarreraId, Nivel = r.Nivel, Nombre = r.Nombre };
    }
}
