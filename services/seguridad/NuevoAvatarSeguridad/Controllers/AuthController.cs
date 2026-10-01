using Microsoft.AspNetCore.Mvc;
using NuevoAvatar.Seguridad.Abstract;
using NuevoAvatar.Seguridad.Models;

namespace NuevoAvatarSeguridad.Controllers
{
    [ApiController]
    public class AuthController : ControllerBase
    {
        private readonly IAuthService _auth;

        public AuthController(IAuthService auth)
        {
            _auth = auth;
        }

        [HttpPost("/login")]
        public async Task<IActionResult> Login(
            [FromHeader(Name = "usuario")] string? usuario,
            [FromHeader(Name = "contrasena")] string? contrasena)
        {
            if (string.IsNullOrWhiteSpace(usuario) || string.IsNullOrWhiteSpace(contrasena))
                return BadRequest("Todos los datos son requeridos y no pueden ser nulos o blancos");

            var respuesta = await _auth.Login(usuario, contrasena);
            if (respuesta == null)
                return Unauthorized("Usuario y/o contraseña incorrectos");

            return StatusCode(201, respuesta);
        }

        [HttpPost("/refresh")]
        public async Task<IActionResult> Refresh([FromBody] RefreshRequest request)
        {
            if (string.IsNullOrWhiteSpace(request?.RefreshToken))
                return Unauthorized("No autorizado");

            var respuesta = await _auth.Refresh(request.RefreshToken);
            if (respuesta == null)
                return Unauthorized("No autorizado");

            return StatusCode(201, respuesta);
        }

        [HttpGet("/validate")]
        public IActionResult Validate([FromQuery] string? token)
        {
            if (string.IsNullOrWhiteSpace(token))
            {
                var header = Request.Headers.Authorization.ToString();
                token = header.StartsWith("Bearer ", StringComparison.OrdinalIgnoreCase)
                    ? header.Substring(7).Trim()
                    : header.Trim();
            }

            if (string.IsNullOrWhiteSpace(token) || _auth.ValidarToken(token) == null)
                return Unauthorized();

            return Ok(true);
        }

    
    
    }
}
