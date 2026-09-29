using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using NuevoAvatar.Seguridad.Models;

namespace NuevoAvatar.Seguridad.Abstract
{
    public interface ISesionTokenService
    {
        Task<List<SesionToken>> ObtenerTodos();
        Task<SesionToken?> ObtenerPorId(int idtoken);
        Task<SesionToken>Crear(SesionToken sesiontoken);
        Task<SesionToken> Modificar(SesionToken sesiontoken);
        Task<SesionToken> Eliminar(SesionToken sesiontoken);
    }
}
