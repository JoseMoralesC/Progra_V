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

        public async Task<PaginaBitacora> ObtenerPagina(int page, int size, long? hastaId = null)
        {
            if (page < 0 || size is < 1 or > 100 || (long)page * size > int.MaxValue || hastaId < 0)
                throw new ArgumentOutOfRangeException(nameof(page));
            var corte = hastaId ?? await _context.Bitacoras.MaxAsync(b => (long?)b.IdBitacora) ?? 0;
            var consulta = _context.Bitacoras.AsNoTracking().Where(b => b.IdBitacora <= corte);
            var total = await consulta.CountAsync();
            // Ordenar primero las claves evita arrastrar descripciones extensas por el ordenamiento de todo el historial.
            var ids = await consulta.OrderByDescending(b => b.FechaHora)
                .ThenByDescending(b => b.IdBitacora).Select(b => b.IdBitacora).Skip(page * size).Take(size).ToListAsync();
            var registros = ids.Count == 0 ? new List<Bitacora>() :
                await _context.Bitacoras.AsNoTracking().Where(b => ids.Contains(b.IdBitacora)).ToListAsync();
            var items = registros.OrderByDescending(b => b.FechaHora).ThenByDescending(b => b.IdBitacora).ToList();
            return new PaginaBitacora(items, total, page, size, corte);
        }

        public async Task<Bitacora> Crear(Bitacora bitacora)
        {
            if (bitacora.IdEvento.HasValue) {
                var existente = await _context.Bitacoras.AsNoTracking().FirstOrDefaultAsync(b => b.IdEvento == bitacora.IdEvento);
                if (existente is not null) return existente;
            }
            bitacora.Descripcion = NuevoAvatar.Integracion.ProteccionAuditoria.Limpiar(bitacora.Descripcion);
            if (!bitacora.IdEvento.HasValue || bitacora.FechaHora == default) bitacora.FechaHora = DateTime.Now;
            _context.Bitacoras.Add(bitacora);
            try { await _context.SaveChangesAsync(); }
            catch (DbUpdateException error) when (bitacora.IdEvento.HasValue &&
                error.InnerException is Microsoft.Data.SqlClient.SqlException { Number: 2601 or 2627 }) {
                _context.Entry(bitacora).State = EntityState.Detached;
                return await _context.Bitacoras.AsNoTracking().SingleAsync(b => b.IdEvento == bitacora.IdEvento);
            }
            return bitacora;
        }
    }
}

