using Microsoft.AspNetCore.Mvc;
using NuevoAvatar.Seguridad.Abstract;
using NuevoAvatar.Seguridad.Models;
using System.Text.Json;

namespace NuevoAvatarSeguridad.Controllers
{
    public abstract class SeguridadControllerBase : ControllerBase
    {
        private readonly IBitacoraService _bitacora;

        protected SeguridadControllerBase(IBitacoraService bitacora)
        {
            _bitacora = bitacora;
        }

        protected static string ComoJson(object objeto) => JsonSerializer.Serialize(objeto);

        protected async Task Registrar(string descripcion)
        {
            if (NuevoAvatar.Integracion.ConsultaAutomatica.EsCargaDashboard(HttpContext, 200)) return;
            if (HttpContext.Items[NuevoAvatar.Integracion.AuditoriaTransaccion.EscrituraRegistrada] is true) return;
            await _bitacora.Crear(new Bitacora
            {
                Usuario = HttpContext.Items["usuario"]?.ToString() ?? "desconocido",
                Descripcion = descripcion.StartsWith("El usuario consulta ", StringComparison.Ordinal)
                    ? descripcion : descripcion + ". Resultado: Exitoso"
            });
        }
    }
}

