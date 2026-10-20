using System.Net;
using Microsoft.Extensions.Configuration;
using NuevoAvatar.Integracion;
using NuevoAvatar.Grupo.Nuevo.Entities;
using NuevoAvatar.Grupo.Nuevo.Services;
using Xunit;

public class GrupoServiceTest
{
    [Fact]
    public async Task RechazaCursoInexistenteAntesDeGuardar()
    {
        var factory = new ClienteSimulado();
        var config = new ConfigurationBuilder().AddInMemoryCollection(new Dictionary<string,string?> {
            ["SERVICES_INTERNAL_KEY"] = "clave-ficticia-solo-para-pruebas-locales",
            ["CURSO_BASE_URL"] = "http://curso.test"
        }).Build();
        var service = new GrupoService(null!, new DatosRemotos(factory,config));
        var error = await Assert.ThrowsAsync<ArgumentException>(() => service.CrearAsync(new GrupoRequest {
            NumeroGrupo=1, CursoId=10, ProfesorId=20, Horario="Lunes 18:00", Cupo=30, PeriodoId=1
        }));
        Assert.Contains("Curso",error.Message);
        Assert.Equal("http://curso.test/interno/datos/Curso?CursoId=10",factory.Url);
    }

    private sealed class ClienteSimulado : HttpMessageHandler, IHttpClientFactory
    {
        public string? Url {get; private set;}
        public HttpClient CreateClient(string name) => new(this,false);
        protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request,CancellationToken token)
        {
            Url=request.RequestUri!.ToString();
            return Task.FromResult(new HttpResponseMessage(HttpStatusCode.OK) {
                Content=new StringContent("[]",System.Text.Encoding.UTF8,"application/json")
            });
        }
    }
}
