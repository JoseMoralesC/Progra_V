using NuevoAvatar.Integracion;
using NuevoAvatar.Seguridad.Abstract;
using NuevoAvatar.Seguridad.Models;

namespace NuevoAvatar.Seguridad.DataAccess;

public sealed class BitacoraRemotaService(DatosRemotos remotos) : IBitacoraService
{
    public Task<List<Bitacora>> ObtenerTodos() => throw new NotSupportedException("Consultar el servicio Bitacora");
    public Task<Bitacora?> ObtenerPorId(long id) => throw new NotSupportedException("Consultar el servicio Bitacora");
    public async Task<Bitacora> Crear(Bitacora entrada)
    {
        var response=await remotos.Post("Bitacora","/interno/bitacora",new {entrada.Usuario,entrada.Descripcion});
        return System.Text.Json.JsonSerializer.Deserialize<Bitacora>(response.GetRawText(),DatosRemotos.Json)!;
    }
}
