using NuevoAvatar.Integracion;

namespace NuevoAvatar.Gateway;

public static class ConsultasDashboard
{
    public static bool EsIndicador(HttpContext contexto) =>
        HttpMethods.IsGet(contexto.Request.Method) && contexto.Request.Query["vista"] == "dashboard" &&
        contexto.Request.Path.Value is "/gateway/admin/usuario" or "/gateway/admin/carrera" or
            "/gateway/admin/curso" or "/gateway/admin/periodo" or "/gateway/mat/profesor/grupos";

    public static void Marcar(HttpContext contexto, HttpRequestMessage solicitud)
    {
        var clave = contexto.RequestServices?.GetService<IConfiguration>()?["SERVICES_INTERNAL_KEY"];
        if (string.IsNullOrEmpty(clave)) return;
        solicitud.Headers.Remove(ConsultaAutomatica.Cabecera);
        solicitud.Headers.Remove("X-Servicio-Key");
        solicitud.Headers.Add(ConsultaAutomatica.Cabecera, "dashboard");
        solicitud.Headers.Add("X-Servicio-Key", clave);
    }
}
