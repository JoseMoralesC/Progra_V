using NuevoAvatar.Seguridad.Abstract;
using NuevoAvatar.Seguridad.Models;

namespace NuevoAvatarSeguridad;

public sealed class AuditoriaSesionMiddleware(RequestDelegate next, ILogger<AuditoriaSesionMiddleware> log)
{
    public async Task InvokeAsync(HttpContext context, IBitacoraService bitacora)
    {
        var fallo = false;
        try { await next(context); }
        catch { fallo = true; throw; }
        finally
        {
            var ruta = context.Request.Path.Value;
            bool login = HttpMethods.IsPost(context.Request.Method) && ruta == "/login";
            bool refresh = HttpMethods.IsPost(context.Request.Method) && ruta == "/refresh";
            bool logout = HttpMethods.IsPost(context.Request.Method) && ruta == "/logout";
            // /sesion es una comprobación interna del Gateway por cada petición, no un movimiento del usuario.
            if (login || refresh || logout)
            {
                // No guardar password, Authorization, refresh_token ni cuerpos de respuesta.
                // En un login fallido la identidad es la cuenta declarada, no una identidad autenticada.
                var usuario = context.Items["usuario"]?.ToString() ??
                    (login ? context.Request.Headers["usuario"].ToString().Trim() : "anonimo");
                if (string.IsNullOrWhiteSpace(usuario)) usuario = "anonimo";
                var estado = fallo ? 500 : context.Response.StatusCode;
                string accion = login ? (estado is >= 200 and < 300 ? "Inicio de sesion exitoso" : "Inicio de sesion fallido") :
                    refresh ? "Renovacion de sesion" : logout ? "Cierre de sesion" : "El usuario consulta su sesion";
                try
                {
                    await bitacora.Crear(new Bitacora { Usuario = usuario,
                        Descripcion = $"{accion}. Estado HTTP: {estado}" });
                }
                catch (Exception)
                {
                    log.LogError("No fue posible registrar la auditoria de sesion para {Ruta}", ruta);
                    throw;
                }
            }
        }
    }
}
