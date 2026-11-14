using System.Security.Cryptography;
using System.Text;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace NuevoAvatar.Integracion;

// Solo el gateway añade esta marca, después de autorizar una lectura del inicio.
public static class ConsultaAutomatica
{
    public const string Cabecera = "X-Auditoria-Contexto";
    public static bool EsCargaDashboard(HttpContext contexto, int estado)
    {
        if (!HttpMethods.IsGet(contexto.Request.Method) || estado < 200 || estado >= 300 ||
            contexto.Request.Headers[Cabecera] != "dashboard") return false;
        var clave = contexto.RequestServices.GetRequiredService<IConfiguration>()["SERVICES_INTERNAL_KEY"];
        var recibida = contexto.Request.Headers["X-Servicio-Key"].ToString();
        return !string.IsNullOrEmpty(clave) && CryptographicOperations.FixedTimeEquals(
            Encoding.UTF8.GetBytes(clave), Encoding.UTF8.GetBytes(recibida));
    }
}
