using Microsoft.AspNetCore.Mvc;
using NuevoAvatar.Seguridad.Abstract;
using NuevoAvatar.Seguridad.Models;
using NuevoAvatarSeguridad.Filters;
using System.Text.RegularExpressions;

namespace NuevoAvatarSeguridad.Controllers
{
    [ApiController]
    [Route("parametro")]
    [TokenAuthorize]
    public class ParametroController : SeguridadControllerBase
    {
        private const string ValorOculto = "********";
        private readonly IParametroService _service;

        public ParametroController(IParametroService service, IBitacoraService bitacora) : base(bitacora)
        {
            _service = service;
        }

        [HttpGet]
        public async Task<IActionResult> ObtenerTodos()
        {
            var lista = await _service.ObtenerTodos();
            await Registrar("El usuario consulta parametros");
            return Ok(lista.Select(OcultarSecreto));
        }

        [HttpGet("{idParametro}")]
        public async Task<IActionResult> ObtenerPorId(string idParametro)
        {
            var p = await _service.ObtenerPorId(idParametro);
            if (p == null) return NotFound("El parámetro no existe");
            await Registrar("El usuario consulta parametro");
            return Ok(OcultarSecreto(p));
        }

        [HttpPost]
        public async Task<IActionResult> Crear([FromBody] Parametro parametro)
        {
            var error = Validar(parametro);
            if (error != null) return BadRequest(error);

            if (await _service.ObtenerPorId(parametro.IdParametro) != null)
                return Conflict("Ya existe un parámetro con ese identificador");

            var creado = await _service.Crear(parametro);
            await Registrar($"Creación de parametro: {creado.IdParametro}");
            return CreatedAtAction(nameof(ObtenerPorId), new { idParametro = creado.IdParametro }, OcultarSecreto(creado));
        }

        [HttpPut("{idParametro}")]
        public async Task<IActionResult> Modificar(string idParametro, [FromBody] Parametro parametro)
        {
            parametro.IdParametro = idParametro;
            var error = Validar(parametro);
            if (error != null) return BadRequest(error);

            var existente = await _service.ObtenerPorId(idParametro);
            if (existente == null) return NotFound("El parámetro no existe");

            // La máscara que recibe la interfaz significa "conservar el secreto".
            // Un valor nuevo sí reemplaza la clave, pero nunca se devuelve ni audita.
            if (!EsSecreto(idParametro) || parametro.Valor != ValorOculto)
                existente.Valor = parametro.Valor;
            var actual = await _service.Modificar(existente);

            await Registrar($"Modificación de parametro: {idParametro}");
            return Ok(OcultarSecreto(actual));
        }

        [HttpDelete("{idParametro}")]
        public async Task<IActionResult> Eliminar(string idParametro)
        {
            var existente = await _service.ObtenerPorId(idParametro);
            if (existente == null) return NotFound("El parámetro no existe");

            var eliminado = await _service.Eliminar(existente);
            await Registrar($"Eliminación de parametro: {idParametro}");
            return Ok(OcultarSecreto(eliminado));
        }

        private static string? Validar(Parametro p)
        {
            if (string.IsNullOrWhiteSpace(p.IdParametro) || string.IsNullOrWhiteSpace(p.Valor))
                return "Todos los datos son requeridos y no pueden estar vacíos";

            if (p.IdParametro.Length > 10 || !Regex.IsMatch(p.IdParametro, @"^[A-Z]+$"))
                return "El identificador debe ser texto de máximo 10 caracteres, solo letras en mayúscula";

            if (p.Valor.Length > 500)
                return "El valor puede tener máximo 500 caracteres";

            return null;
        }

        private static bool EsSecreto(string idParametro) =>
            idParametro.Equals("MAILPASS", StringComparison.OrdinalIgnoreCase);

        private static Parametro OcultarSecreto(Parametro parametro) => new()
        {
            IdParametro = parametro.IdParametro,
            Valor = EsSecreto(parametro.IdParametro) ? ValorOculto : parametro.Valor
        };
    }
}

