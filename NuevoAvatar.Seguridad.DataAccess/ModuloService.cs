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
            return await _context.Modulos.AsNoTracking().OrderBy(m => m.Orden).ThenBy(m => m.NombreModulo).ToListAsync();
        }

        public async Task<Modulo?> ObtenerPorId(string idModulo)
        {
            return await _context.Modulos.FindAsync(idModulo);
        }

        public async Task<Modulo> Crear(Modulo modulo)
        {
            modulo.Roles = [];
            _context.Modulos.Add(modulo);
            await _context.SaveChangesAsync();
            return modulo;
        }

        public async Task<Modulo> Modificar(Modulo modulo)
        {
            await _context.SaveChangesAsync();
            return modulo;
        }

        public async Task<Modulo> Eliminar(Modulo modulo)
        {
            _context.Modulos.Remove(modulo);
            await _context.SaveChangesAsync();
            return modulo;
        }

        public Task<List<string>> ObtenerRoles(string idModulo) =>
            _context.RolesModulo.AsNoTracking().Where(x => x.IdModulo == idModulo)
                .OrderBy(x => x.IdRol).Select(x => x.IdRol).ToListAsync();

        public async Task ReemplazarRoles(string idModulo, IReadOnlyCollection<string> roles)
        {
            var anteriores = await _context.RolesModulo.Where(x => x.IdModulo == idModulo).ToListAsync();
            var nuevos = roles.Select(r => r.ToUpperInvariant()).ToHashSet(StringComparer.OrdinalIgnoreCase);
            _context.RolesModulo.RemoveRange(anteriores.Where(r => !nuevos.Contains(r.IdRol)));
            var existentes = anteriores.Select(r => r.IdRol).ToHashSet(StringComparer.OrdinalIgnoreCase);
            await _context.RolesModulo.AddRangeAsync(nuevos.Where(r => !existentes.Contains(r))
                .Select(r => new RolModulo { IdModulo = idModulo, IdRol = r }));
            await _context.SaveChangesAsync();
        }

        public Task<List<Modulo>> ObtenerMenu(string idRol) =>
            _context.RolesModulo.AsNoTracking().Where(x => x.IdRol == idRol && x.Modulo!.Activo)
                .Select(x => x.Modulo!).OrderBy(x => x.Orden).ThenBy(x => x.NombreModulo).ToListAsync();
    }
}

