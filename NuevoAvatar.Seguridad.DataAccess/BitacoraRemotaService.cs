using NuevoAvatar.Integracion;
using NuevoAvatar.Seguridad.Abstract;
using NuevoAvatar.Seguridad.Models;

namespace NuevoAvatar.Seguridad.DataAccess;

public sealed class BitacoraRemotaService(AuditoriaLocal auditoria) : IBitacoraService
{
    public Task<List<Bitacora>> ObtenerTodos() => throw new NotSupportedException("Consultar el servicio Bitacora");
    public Task<PaginaBitacora> ObtenerPagina(int page, int size, long? hastaId = null) => throw new NotSupportedException("Consultar el servicio Bitacora");
    public Task<Bitacora?> ObtenerPorId(long id) => throw new NotSupportedException("Consultar el servicio Bitacora");
    public async Task<Bitacora> Crear(Bitacora entrada)
    {
        var evento = await auditoria.Registrar(entrada.Usuario, entrada.Descripcion);
        return new Bitacora { IdEvento=evento.IdEvento, Usuario=evento.Usuario, Descripcion=evento.Descripcion, FechaHora=evento.FechaHora };
    }
}
