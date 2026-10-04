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
    public class SesionTokenService : ISesionTokenService
    {
        private readonly SeguridadDbContext _context;

        public SesionTokenService(SeguridadDbContext context)
        {
            _context = context;
        }

        public async Task<List<SesionToken>> ObtenerTodos()
        {
            return await _context.SesionesToken.ToListAsync();
        }

        public async Task<SesionToken?> ObtenerPorId(int idToken)
        {
            return await _context.SesionesToken.FindAsync(idToken);
        }

        public async Task<SesionToken> Crear(SesionToken sesionToken)
        {
            _context.SesionesToken.Add(sesionToken);
            await _context.SaveChangesAsync();
            return sesionToken;
        }

        public async Task<SesionToken> Modificar(SesionToken sesionToken)
        {
            _context.SesionesToken.Update(sesionToken);
            await _context.SaveChangesAsync();
            return sesionToken;
        }

        public async Task<SesionToken> Eliminar(SesionToken sesionToken)
        {
            _context.SesionesToken.Remove(sesionToken);
            await _context.SaveChangesAsync();
            return sesionToken;
        }
    }
}
