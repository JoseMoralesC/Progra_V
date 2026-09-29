using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using NuevoAvatar.Seguridad.Abstract;
using NuevoAvatar.Seguridad.Models;
using NuevoAvatarSeguridad.Filters;
using System.Text.RegularExpressions;

namespace NuevoAvatarSeguridad.Controllers
{
    [ApiController]
    [Route("rol")]
    [TokenAuthorize]
    public class RolController : SeguridadControllerBase
    {
        private readonly IRolService _rolService;

        public RolController(IRolService rolService, IBitacoraService bitacora) : base(bitacora)
        {
            _rolService = rolService;
        }

        [HttpGet]
        public async Task<IActionResult> ObtenerTodos()
        {
            var roles = await _rolService.ObtenerTodos();
            await Registrar("El usuario consulta roles");
            return Ok(roles);
        }

        [HttpGet("{idRol}")]
        public async Task<IActionResult> ObtenerPorId(string idRol)
        {
            var rol = await _rolService.ObtenerPorId(idRol);
            if (rol == null) return NotFound("El rol no existe");
            await Registrar("El usuario consulta rol");
            return Ok(rol);
        }

        [HttpPost]
        public async Task<IActionResult> Crear([FromBody] Rol rol)
        {
            var error = Validar(rol);
            if (error != null) return BadRequest(error);

            if (await _rolService.ObtenerPorId(rol.IdRol) != null)
                return Conflict("Ya existe un rol con ese identificador");

            var creado = await _rolService.Crear(rol);
            await Registrar($"Creación de rol: {ComoJson(creado)}");
            return CreatedAtAction(nameof(ObtenerPorId), new { idRol = creado.IdRol }, creado);
        }

        [HttpPut("{idRol}")]
        public async Task<IActionResult> Modificar(string idRol, [FromBody] Rol rol)
        {
            rol.IdRol = idRol;
            var error = Validar(rol);
            if (error != null) return BadRequest(error);

            var existente = await _rolService.ObtenerPorId(idRol);
            if (existente == null) return NotFound("El rol no existe");

            var anterior = ComoJson(existente);   // se serializa ANTES de modificar
            existente.NombreRol = rol.NombreRol;
            var actual = await _rolService.Modificar(existente);

            await Registrar($"Modificación de rol. Anterior: {anterior} Actual: {ComoJson(actual)}");
            return Ok(actual);
        }

        [HttpDelete("{idRol}")]
        public async Task<IActionResult> Eliminar(string idRol)
        {
            var existente = await _rolService.ObtenerPorId(idRol);
            if (existente == null) return NotFound("El rol no existe");

            try
            {
                var eliminado = await _rolService.Eliminar(existente);
                await Registrar($"Eliminación de rol: {ComoJson(eliminado)}");
                return Ok(eliminado);
            }
            catch (DbUpdateException)
            {
                return Conflict("No se puede eliminar el rol porque tiene usuarios asociados");
            }
        }

        private static string? Validar(Rol rol)
        {
            if (string.IsNullOrWhiteSpace(rol.IdRol) || string.IsNullOrWhiteSpace(rol.NombreRol))
                return "Todos los datos son requeridos y no pueden estar vacíos";

            if (!Regex.IsMatch(rol.NombreRol, @"^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$"))
                return "El nombre del rol solo puede tener letras y espacios";

            return null;
        }
    }
}
