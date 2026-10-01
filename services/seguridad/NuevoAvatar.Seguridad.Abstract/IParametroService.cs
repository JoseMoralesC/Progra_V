using NuevoAvatar.Seguridad.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NuevoAvatar.Seguridad.Abstract
{
    public interface IParametroService
    {
        Task<List<Parametro>> ObtenerTodos();
        Task<Parametro?>ObtenerPorId(string idParametro);
        Task<Parametro> Crear(Parametro parametro);
        Task<Parametro> Modificar(Parametro parametro);
        Task<Parametro> Eliminar(Parametro parametro);
    }
}
