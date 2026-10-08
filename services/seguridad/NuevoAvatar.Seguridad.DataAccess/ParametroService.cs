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
    public class ParametroService : IParametroService
    {
        private readonly SeguridadDbContext _context;

        public ParametroService(SeguridadDbContext context)
        {
            _context = context;
        }

        public async Task<List<Parametro>> ObtenerTodos()
        {
            return await _context.Parametros.ToListAsync();
        }

        public async Task<Parametro?> ObtenerPorId(string idParametro)
        {
            return await _context.Parametros.FindAsync(idParametro);
        }

        public async Task<Parametro> Crear(Parametro parametro)
        {
            _context.Parametros.Add(parametro);
            await _context.SaveChangesAsync();
            return parametro;
        }

        public async Task<Parametro> Modificar(Parametro parametro)
        {
            _context.Parametros.Update(parametro);
            await _context.SaveChangesAsync();
            return parametro;
        }

        public async Task<Parametro> Eliminar(Parametro parametro)
        {
            _context.Parametros.Remove(parametro);
            await _context.SaveChangesAsync();
            return parametro;
        }
    }
}
