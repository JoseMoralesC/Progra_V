using NuevoAvatar.Seguridad.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NuevoAvatar.Seguridad.Abstract
{
    public interface IRolService
    {
        Task<List<Rol>> ObtenerTodos();
        Task<Rol?> ObtenerPorId(string idRol);
        Task<Rol> Crear(Rol rol);
        Task<Rol> Modificar(Rol rol);
        Task<Rol> Eliminar(Rol rol);

    }
}
