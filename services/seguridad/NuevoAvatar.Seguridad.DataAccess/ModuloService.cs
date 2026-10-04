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
    public class ModuloService : IModuloService
    {
        private readonly SeguridadDbContext _context;

        public ModuloService(SeguridadDbContext context)
        {
            _context = context;
        }

        public async Task<List<Modulo>> ObtenerTodos()
        {
            return await _context.Modulos.ToListAsync();
        }

        public async Task<Modulo?> ObtenerPorId(string idModulo)
        {
            return await _context.Modulos.FindAsync(idModulo);
        }

        public async Task<Modulo> Crear(Modulo modulo)
        {
            _context.Modulos.Add(modulo);
            await _context.SaveChangesAsync();
            return modulo;
        }

        public async Task<Modulo> Modificar(Modulo modulo)
        {
            _context.Modulos.Update(modulo);
            await _context.SaveChangesAsync();
            return modulo;
        }

        public async Task<Modulo> Eliminar(Modulo modulo)
        {
            _context.Modulos.Remove(modulo);
            await _context.SaveChangesAsync();
            return modulo;
        }
    }
}
