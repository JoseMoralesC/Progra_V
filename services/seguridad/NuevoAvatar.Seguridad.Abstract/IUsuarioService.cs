using NuevoAvatar.Seguridad.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NuevoAvatar.Seguridad.Abstract
{
    public interface IUsuarioService
    {
        Task<List<Usuario>> ObtenerTodos();
        Task<Usuario?> ObtenerPorId(string email);
        Task<Usuario> Crear(Usuario usuario);
        Task<Usuario> Modificar(Usuario usuario);
        Task<Usuario> Eliminar(Usuario usuario);
    }
}
