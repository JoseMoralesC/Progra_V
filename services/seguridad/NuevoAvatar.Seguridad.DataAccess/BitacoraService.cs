using Microsoft.EntityFrameworkCore;
using NuevoAvatar.Seguridad.Abstract;
using NuevoAvatar.Seguridad.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NuevoAvatar.Seguridad.DataAccess
{
    public class BitacoraService : IBitacoraService
    {
        private readonly SeguridadDbContext _context;

        public BitacoraService(SeguridadDbContext context)
        {
            _context = context;
        }

        public async Task<List<Bitacora>> ObtenerTodos()
        {
            return await _context.Bitacoras
                .OrderByDescending(b => b.FechaHora)
                .ToListAsync();
        }

        public async Task<Bitacora?> ObtenerPorId(long idBitacora)
        {
            return await _context.Bitacoras.FindAsync(idBitacora);
        }

        public async Task<Bitacora> Crear(Bitacora bitacora)
        {
            bitacora.FechaHora = DateTime.Now;
            _context.Bitacoras.Add(bitacora);
            await _context.SaveChangesAsync();
            return bitacora;
        }
    }
}
