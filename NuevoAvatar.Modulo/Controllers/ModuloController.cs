using Microsoft.AspNetCore.Mvc;
using NuevoAvatar.Seguridad.Abstract;
using NuevoAvatar.Seguridad.Models;
using NuevoAvatarSeguridad.Filters;
using System.Text.RegularExpressions;
using NuevoAvatar.Integracion;

namespace NuevoAvatarSeguridad.Controllers
{
    [ApiController]
    [Route("modulo")]
    [TokenAuthorize]
    public class ModuloController : SeguridadControllerBase
    {
        private readonly IModuloService _service;
        private readonly DatosRemotos _datos;

        public ModuloController(IModuloService service, IBitacoraService bitacora, DatosRemotos datos) : base(bitacora)
        {
            _service = service;
            _datos = datos;
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
            Normalizar(modulo);
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
            Normalizar(modulo);
            var error = Validar(modulo);
            if (error != null) return BadRequest(error);

            var existente = await _service.ObtenerPorId(idModulo);
            if (existente == null) return NotFound("El módulo no existe");

            var anterior = ComoJson(existente);
            existente.NombreModulo = modulo.NombreModulo;
            existente.Activo = modulo.Activo;
            existente.Orden = modulo.Orden;
            var actual = await _service.Modificar(existente);

            await Registrar($"Modificación de modulo. Anterior: {anterior} Actual: {ComoJson(actual)}");
            return Ok(actual);
        }

        [HttpGet("menu/{idRol}")]
        public async Task<IActionResult> ObtenerMenu(string idRol) => Ok(await _service.ObtenerMenu(idRol.ToUpperInvariant()));

        // Contrato GTW2/ADM1: el ID real del rol se consulta en Usuario, nunca
        // se confunde con el nombre canónico ADMINISTRADOR/PROFESOR/ESTUDIANTE.
        [HttpGet("menu")]
        public async Task<IActionResult> ObtenerMenuPropio()
        {
            Response.Headers.CacheControl = "no-store";
            var email = HttpContext.Items["usuario"]?.ToString();
            if (string.IsNullOrWhiteSpace(email)) return Unauthorized();
            var cuentas = await _datos.Listar("Usuario", "usuario", new() { ["email"] = email });
            if (cuentas.Count != 1) return StatusCode(403, new { error = "Cuenta sin rol autorizado" });
            var rol = DatosRemotos.Valor(cuentas[0], "id_rol").ToString().Trim().ToUpperInvariant();
            if (string.IsNullOrWhiteSpace(rol)) return StatusCode(403, new { error = "Cuenta sin rol autorizado" });
            var menu = await _service.ObtenerMenu(rol);
            await Registrar("El usuario consulta su menu de modulos");
            return Ok(menu);
        }

        [HttpGet("{idModulo}/roles")]
        public async Task<IActionResult> ObtenerRoles(string idModulo)
        {
            if (await _service.ObtenerPorId(idModulo) is null) return NotFound("El módulo no existe");
            return Ok(await _service.ObtenerRoles(idModulo));
        }

        [HttpPut("{idModulo}/roles")]
        public async Task<IActionResult> ReemplazarRoles(string idModulo, [FromBody] List<string>? roles)
        {
            if (await _service.ObtenerPorId(idModulo) is null) return NotFound("El módulo no existe");
            var normalizados = (roles ?? []).Where(r => !string.IsNullOrWhiteSpace(r)).Select(r => r.Trim().ToUpperInvariant()).Distinct().ToArray();
            foreach (var rol in normalizados)
                if ((await _datos.Listar("Rol", "rol", new() { ["id_rol"] = rol })).Count == 0)
                    return UnprocessableEntity($"El rol {rol} no existe");
            var anterior = await _service.ObtenerRoles(idModulo);
            await _service.ReemplazarRoles(idModulo, normalizados);
            await Registrar($"Asignación de roles al módulo {idModulo}. Anterior: {ComoJson(anterior)} Actual: {ComoJson(normalizados)}");
            return NoContent();
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

            if (!Regex.IsMatch(m.IdModulo, @"^[A-Z0-9_]{2,20}$"))
                return "El identificador debe tener de 2 a 20 letras mayúsculas, números o guion bajo";

            if (!Regex.IsMatch(m.NombreModulo, @"^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$"))
                return "El nombre del módulo solo puede tener letras y espacios";
            if (m.Orden < 0) return "El orden no puede ser negativo";

            return null;
        }

        private static void Normalizar(Modulo modulo)
        {
            modulo.IdModulo = modulo.IdModulo.Trim().ToUpperInvariant();
            modulo.NombreModulo = modulo.NombreModulo.Trim();
        }
    }
}

