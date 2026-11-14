using System.Data;
using System.Data.Common;
using System.Text.Json;
using Microsoft.AspNetCore.Http;

namespace NuevoAvatar.Integracion;

public sealed class AuditoriaTransaccion(IHttpContextAccessor contexto)
{
    public const string EscrituraRegistrada = "auditoria.escritura";
    public async Task Registrar(IDbConnection conexion, IDbTransaction transaccion, string accion, object? anterior, object? actual)
    {
        var usuario = contexto.HttpContext?.Items["usuario"]?.ToString();
        if (contexto.HttpContext is not null && string.IsNullOrWhiteSpace(usuario))
            throw new InvalidOperationException("Falta identidad para auditar la escritura.");
        using var comando = ((DbConnection)conexion).CreateCommand();
        comando.Transaction = (DbTransaction)transaccion;
        comando.CommandText = "INSERT INTO general.auditoria_pendiente(id_evento,usuario,descripcion,fecha_hora) VALUES(@id,@usuario,@descripcion,@fecha)";
        void Agregar(string nombre, object valor) { var p = comando.CreateParameter(); p.ParameterName = nombre; p.Value = valor; comando.Parameters.Add(p); }
        Agregar("@id", Guid.NewGuid());
        Agregar("@usuario", usuario ?? "sistema");
        Agregar("@descripcion", ProteccionAuditoria.Limpiar(accion + ". JSON: " + JsonSerializer.Serialize(new { anterior, actual }) + ". Resultado: Exitoso"));
        Agregar("@fecha", DateTime.Now);
        await comando.ExecuteNonQueryAsync();
        if (contexto.HttpContext is not null) contexto.HttpContext.Items[EscrituraRegistrada] = true;
    }
}
