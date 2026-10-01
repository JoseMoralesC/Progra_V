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
            await _bitacora.Crear(new Bitacora
            {
                Usuario = HttpContext.Items["usuario"]?.ToString() ?? "desconocido",
                Descripcion = descripcion
            });
        }
    }
}
