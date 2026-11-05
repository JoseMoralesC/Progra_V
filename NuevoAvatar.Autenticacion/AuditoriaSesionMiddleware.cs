using NuevoAvatar.Seguridad.Abstract;
using NuevoAvatar.Seguridad.Models;

namespace NuevoAvatarSeguridad;

public sealed class AuditoriaSesionMiddleware(RequestDelegate next, ILogger<AuditoriaSesionMiddleware> log)
{
    public async Task InvokeAsync(HttpContext context, IBitacoraService bitacora)
    {
        try { await next(context); }
        finally
        {
            var ruta = context.Request.Path.Value;
            bool login = HttpMethods.IsPost(context.Request.Method) && ruta == "/login";
            bool refresh = HttpMethods.IsPost(context.Request.Method) && ruta == "/refresh";
            bool consulta = HttpMethods.IsGet(context.Request.Method) && ruta == "/sesion";
            bool logout = HttpMethods.IsPost(context.Request.Method) && ruta == "/logout";
            if (login || refresh || consulta || logout)
            {
                // No guardar password, Authorization, refresh_token ni cuerpos de respuesta.
                var usuario = context.Items["usuario"]?.ToString() ?? "anonimo";
                string accion = login ? "Intento de login" : refresh ? "Renovacion de sesion" :
                    logout ? "Cierre de sesion en cliente" : "El usuario consulta su sesion";
                try
                {
                    await bitacora.Crear(new Bitacora { Usuario = usuario,
                        Descripcion = $"{accion}. Estado HTTP: {context.Response.StatusCode}" });
                }
                catch (Exception)
                {
                    log.LogError("No fue posible registrar la auditoria de sesion para {Ruta}", ruta);
                }
            }
        }
    }
}
