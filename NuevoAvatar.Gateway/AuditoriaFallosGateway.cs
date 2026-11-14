using System.Net.Http.Headers;
using NuevoAvatar.Seguridad.Models;

namespace NuevoAvatar.Gateway;

// Captura rechazos del Gateway y errores del destino sin almacenar cuerpos ni credenciales.
public sealed class AuditoriaFallosGateway(RequestDelegate next, ILogger<AuditoriaFallosGateway> log)
{
    public const string ServicioEnviado = "gateway.servicioEnviado";
    private static readonly HashSet<string> AuditanRespuesta = [
        "Usuario", "Institucion", "Profesor", "Carrera", "Matricula", "Notas", "Factura", "Pago", "Notificacion", "Curso"
    ];
    public async Task InvokeAsync(HttpContext context, NuevoAvatar.Integracion.IAuditoriaLocal auditoria)
    {
        await next(context);
        if (context.Response.StatusCode < 400 || context.Items["sesion"] is not SesionActual sesion)
            return;
        // Estos servicios ya registran su respuesta final; los rechazos previos del Gateway sí se auditan aquí.
        if (context.Items[ServicioEnviado] is string servicio && AuditanRespuesta.Contains(servicio) &&
            context.Response.StatusCode != 502) return;
        var ruta = CatalogoRutas.Todas.OrderByDescending(r => r.Externa.Length).FirstOrDefault(r =>
            r.Modulo is not null && r.Servicio != "Bitacora" && r.Verbos.Contains(context.Request.Method) &&
            (context.Request.Path == r.Externa || r.Subrutas && context.Request.Path.StartsWithSegments(r.Externa)));
        if (ruta is null) return;
        try {
            await auditoria.Registrar(sesion.UsuarioID, $"{context.Request.Method} {ruta.Interna}. Estado HTTP: {context.Response.StatusCode}");
        } catch (Exception error) when (error is HttpRequestException or TaskCanceledException) {
            log.LogError("No fue posible registrar la operación fallida en Bitácora.");
        }
    }
}
