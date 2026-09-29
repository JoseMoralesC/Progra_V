using Microsoft.AspNetCore.Mvc;
using NuevoAvatar.Seguridad.Abstract;
using NuevoAvatar.Seguridad.Models;
using NuevoAvatarSeguridad.Filters;

namespace NuevoAvatarSeguridad.Controllers
{
    [ApiController]
    [Route("bitacora")]
    [TokenAuthorize]
    public class BitacoraController : ControllerBase
    {
        private readonly IBitacoraService _service;

        public BitacoraController(IBitacoraService service)
        {
            _service = service;
        }

        [HttpGet]
        public async Task<IActionResult> ObtenerTodos()
        {
            return Ok(await _service.ObtenerTodos());
        }

        [HttpGet("{id:long}")]
        public async Task<IActionResult> ObtenerPorId(long id)
        {
            var b = await _service.ObtenerPorId(id);
            if (b == null) return NotFound("La bitácora no existe");
            return Ok(b);
        }

        [HttpPost]
        public async Task<IActionResult> Crear([FromBody] Bitacora bitacora)
        {
            if (string.IsNullOrWhiteSpace(bitacora.Usuario) || string.IsNullOrWhiteSpace(bitacora.Descripcion))
                return BadRequest("Todos los datos son requeridos y no pueden estar vacíos");

            var creada = await _service.Crear(new Bitacora
            {
                Usuario = bitacora.Usuario,
                Descripcion = bitacora.Descripcion
            });

            return CreatedAtAction(nameof(ObtenerPorId), new { id = creada.IdBitacora }, creada);
        }
    }
}
