using Microsoft.AspNetCore.Mvc;
using NuevoAvatar.Seguridad.Abstract;
using NuevoAvatar.Seguridad.Models;
using NuevoAvatarSeguridad.Filters;
using System.Text.RegularExpressions;

namespace NuevoAvatarSeguridad.Controllers
{
    [ApiController]
    [Route("modulo")]
    [TokenAuthorize]
    public class ModuloController : SeguridadControllerBase
    {
        private readonly IModuloService _service;

        public ModuloController(IModuloService service, IBitacoraService bitacora) : base(bitacora)
        {
            _service = service;
        }

        [HttpGet]
        public async Task<IActionResult> ObtenerTodos()
        {
            var lista = await _service.ObtenerTodos();
            await Registrar("El usuario consulta modulos");
            return Ok(lista);
        }

        [HttpGet("{idModulo}")]
        public async Task<IActionResult> ObtenerPorId(string idModulo)
        {
            var m = await _service.ObtenerPorId(idModulo);
            if (m == null) return NotFound("El módulo no existe");
            await Registrar("El usuario consulta modulo");
            return Ok(m);
        }

        [HttpPost]
        public async Task<IActionResult> Crear([FromBody] Modulo modulo)
        {
            var error = Validar(modulo);
            if (error != null) return BadRequest(error);

            if (await _service.ObtenerPorId(modulo.IdModulo) != null)
                return Conflict("Ya existe un módulo con ese identificador");

            var creado = await _service.Crear(modulo);
            await Registrar($"Creación de modulo: {ComoJson(creado)}");
            return CreatedAtAction(nameof(ObtenerPorId), new { idModulo = creado.IdModulo }, creado);
        }

        [HttpPut("{idModulo}")]
        public async Task<IActionResult> Modificar(string idModulo, [FromBody] Modulo modulo)
        {
            modulo.IdModulo = idModulo;
            var error = Validar(modulo);
            if (error != null) return BadRequest(error);

            var existente = await _service.ObtenerPorId(idModulo);
            if (existente == null) return NotFound("El módulo no existe");

            var anterior = ComoJson(existente);
            existente.NombreModulo = modulo.NombreModulo;
            var actual = await _service.Modificar(existente);

            await Registrar($"Modificación de modulo. Anterior: {anterior} Actual: {ComoJson(actual)}");
            return Ok(actual);
        }

        [HttpDelete("{idModulo}")]
        public async Task<IActionResult> Eliminar(string idModulo)
        {
            var existente = await _service.ObtenerPorId(idModulo);
            if (existente == null) return NotFound("El módulo no existe");

            var eliminado = await _service.Eliminar(existente);
            await Registrar($"Eliminación de modulo: {ComoJson(eliminado)}");
            return Ok(eliminado);
        }

        private static string? Validar(Modulo m)
        {
            if (string.IsNullOrWhiteSpace(m.IdModulo) || string.IsNullOrWhiteSpace(m.NombreModulo))
                return "Todos los datos son requeridos y no pueden estar vacíos";

            if (!Regex.IsMatch(m.NombreModulo, @"^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$"))
                return "El nombre del módulo solo puede tener letras y espacios";

            return null;
        }
    }
}
