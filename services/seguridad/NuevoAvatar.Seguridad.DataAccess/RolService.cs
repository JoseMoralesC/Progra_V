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
    public class RolService : IRolService
    {
        private readonly SeguridadDbContext _context;

        public RolService(SeguridadDbContext context)
        {
            _context = context;
        }

        public async Task<List<Rol>> ObtenerTodos()
        {
            return await _context.Roles.ToListAsync();
        }

        public async Task<Rol?> ObtenerPorId(string idRol)
        {
            return await _context.Roles.FindAsync(idRol);
        }

        public async Task<Rol> Crear(Rol rol)
        {
            _context.Roles.Add(rol);
            await _context.SaveChangesAsync();
            return rol;
        }

        public async Task<Rol> Modificar(Rol rol)
        {
            _context.Roles.Update(rol);
            await _context.SaveChangesAsync();
            return rol;
        }

        public async Task<Rol> Eliminar(Rol rol)
        {
            _context.Roles.Remove(rol);
            await _context.SaveChangesAsync();
            return rol;
        }
    }

}
