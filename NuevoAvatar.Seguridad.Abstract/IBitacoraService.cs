using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using NuevoAvatar.Seguridad.Models;

namespace NuevoAvatar.Seguridad.Abstract
{
    public interface IBitacoraService
    {
        Task<List<Bitacora>> ObtenerTodos();
        Task<PaginaBitacora> ObtenerPagina(int page, int size, long? hastaId = null);
        Task<Bitacora?> ObtenerPorId(long idBitacora);
        Task<Bitacora>Crear(Bitacora bitacora);
        
    }
}

