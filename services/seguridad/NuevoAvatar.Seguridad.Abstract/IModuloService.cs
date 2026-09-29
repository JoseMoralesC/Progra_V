using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using NuevoAvatar.Seguridad.Models;
namespace NuevoAvatar.Seguridad.Abstract
{
    public interface IModuloService
    {
        Task<List<Modulo>> ObtenerTodos();
        Task<Modulo?> ObtenerPorId(string idModulo);
        Task<Modulo> Crear(Modulo modulo);
        Task<Modulo> Modificar(Modulo modulo);
        Task<Modulo> Eliminar(Modulo modulo);
    }
}
