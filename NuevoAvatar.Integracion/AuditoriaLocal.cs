using System.Net.Http.Json;
using System.Text.Json;
using System.Text.RegularExpressions;
using Microsoft.Data.SqlClient;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

namespace NuevoAvatar.Integracion;

public sealed record EventoPendiente(Guid IdEvento, string Usuario, string Descripcion, DateTime FechaHora);
public interface IAuditoriaLocal { Task<EventoPendiente> Registrar(string usuario, string descripcion); }

public static class ProteccionAuditoria
{
    public static string Limpiar(string texto) => Regex.Replace(texto,
        "(\"(?:password[^\"]*|contrasena[^\"]*|contraseña[^\"]*|confirmacionContrasena|currentPassword|newPassword|confirmPassword|access_token|refresh_token|token|authorization|jwt_key)\"\\s*:\\s*)\"(?:\\\\.|[^\"\\\\])*\"",
        "$1\"[OCULTO]\"", RegexOptions.IgnoreCase);
}

// La fila solo se elimina cuando el receptor confirma el mismo ID y devuelve un ID de bitácora.
public sealed class AuditoriaLocal(IConfiguration config, IHttpClientFactory clientes, ILogger<AuditoriaLocal> log) : IAuditoriaLocal
{
    private string? cadena;
    private readonly SemaphoreSlim entrega = new(1, 1);
    public void Configurar(string connectionString, bool receptor)
    {
        cadena = receptor ? null : connectionString;
        using var conexion = new SqlConnection(connectionString);
        conexion.Open();
        using var comando = conexion.CreateCommand();
        using var recurso = typeof(AuditoriaLocal).Assembly.GetManifestResourceStream(receptor ? "AuditoriaReceptor.sql" : "AuditoriaPendiente.sql")!;
        using var lector = new StreamReader(recurso);
        comando.CommandText = lector.ReadToEnd();
        comando.ExecuteNonQuery();
    }

    public async Task<EventoPendiente> Registrar(string usuario, string descripcion)
    {
        if (cadena is null) throw new InvalidOperationException("No se configuró la cola de auditoría del servicio.");
        var evento = new EventoPendiente(Guid.NewGuid(), usuario, ProteccionAuditoria.Limpiar(descripcion), DateTime.Now);
        await using var conexion = new SqlConnection(cadena);
        await conexion.OpenAsync();
        using var comando = conexion.CreateCommand();
        comando.CommandText = "INSERT INTO general.auditoria_pendiente(id_evento,usuario,descripcion,fecha_hora) VALUES(@id,@usuario,@descripcion,@fecha)";
        comando.Parameters.AddWithValue("@id", evento.IdEvento);
        comando.Parameters.AddWithValue("@usuario", evento.Usuario);
        comando.Parameters.AddWithValue("@descripcion", evento.Descripcion);
        comando.Parameters.AddWithValue("@fecha", evento.FechaHora);
        await comando.ExecuteNonQueryAsync();
        // Una falla remota conserva la fila; una falla local de persistencia sí se propaga.
        await Entregar(CancellationToken.None);
        return evento;
    }

    public async Task Entregar(CancellationToken cancelacion)
    {
        if (cadena is null || !await entrega.WaitAsync(0, cancelacion)) return;
        try {
            var pendientes = new List<EventoPendiente>();
            await using var conexion = new SqlConnection(cadena);
            await conexion.OpenAsync(cancelacion);
            using (var comando = conexion.CreateCommand()) {
                comando.CommandText = "SELECT TOP (50) id_evento,usuario,descripcion,fecha_hora FROM general.auditoria_pendiente WHERE proximo_intento<=SYSUTCDATETIME() ORDER BY fecha_hora,id_evento";
                await using var lector = await comando.ExecuteReaderAsync(cancelacion);
                while (await lector.ReadAsync(cancelacion)) pendientes.Add(new(lector.GetGuid(0), lector.GetString(1), lector.GetString(2), lector.GetDateTime(3)));
            }
            foreach (var evento in pendientes) {
                try {
                    using var cliente = clientes.CreateClient();
                    cliente.Timeout = TimeSpan.FromSeconds(8);
                    using var solicitud = new HttpRequestMessage(HttpMethod.Post, ConfiguracionEntorno.Origen(config, "BITACORA_BASE_URL") + "/interno/bitacora");
                    solicitud.Headers.Add("X-Servicio-Key", config["SERVICES_INTERNAL_KEY"]);
                    solicitud.Content = JsonContent.Create(evento);
                    using var respuesta = await cliente.SendAsync(solicitud, cancelacion);
                    respuesta.EnsureSuccessStatusCode();
                    var recibo = await respuesta.Content.ReadFromJsonAsync<JsonElement>(cancellationToken: cancelacion);
                    if (recibo.GetProperty("idEvento").GetGuid() != evento.IdEvento || recibo.GetProperty("idBitacora").GetInt64() <= 0)
                        throw new JsonException("Confirmación de auditoría inválida.");
                    using var borrar = conexion.CreateCommand();
                    borrar.CommandText = "DELETE FROM general.auditoria_pendiente WHERE id_evento=@id";
                    borrar.Parameters.AddWithValue("@id", evento.IdEvento);
                    await borrar.ExecuteNonQueryAsync(cancelacion);
                } catch (Exception error) when (error is HttpRequestException or TaskCanceledException or JsonException or KeyNotFoundException) {
                    cancelacion.ThrowIfCancellationRequested();
                    using var reintento = conexion.CreateCommand();
                    reintento.CommandText = "UPDATE general.auditoria_pendiente SET intentos=intentos+1,proximo_intento=DATEADD(second,10,SYSUTCDATETIME()) WHERE id_evento=@id";
                    reintento.Parameters.AddWithValue("@id", evento.IdEvento);
                    await reintento.ExecuteNonQueryAsync(cancelacion);
                    log.LogWarning("Auditoría {Evento} pendiente de entrega; se reintentará.", evento.IdEvento);
                    break;
                }
            }
        } finally { entrega.Release(); }
    }
}

public sealed class EntregaAuditoriaWorker(AuditoriaLocal auditoria, ILogger<EntregaAuditoriaWorker> log) : BackgroundService
{
    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        using var reloj = new PeriodicTimer(TimeSpan.FromSeconds(5));
        while (!stoppingToken.IsCancellationRequested) {
            try { await auditoria.Entregar(stoppingToken); }
            catch (OperationCanceledException) when (stoppingToken.IsCancellationRequested) { break; }
            catch (Exception) { log.LogError("No se pudo procesar la cola persistente de auditoría."); }
            try { if (!await reloj.WaitForNextTickAsync(stoppingToken)) break; }
            catch (OperationCanceledException) { break; }
        }
    }
}
