using Microsoft.AspNetCore.Mvc;
using NuevoAvatar.Seguridad.Abstract;

namespace NuevoAvatarSeguridad.Controllers;

[ApiController]
public sealed class SesionController(IAuthService auth, IIdentidadSesionService identidad) : ControllerBase
{
    // ADM2 requiere invalidacion en cliente. Este endpoint acredita la salida en GEN1;
    // no afirma revocar access tokens JWT ya emitidos.
    [HttpPost("/logout")]
    public IActionResult Salir()
    {
        Response.Headers.CacheControl = "no-store";
        var bearer = Request.Headers.Authorization.ToString();
        if (!bearer.StartsWith("Bearer ", StringComparison.OrdinalIgnoreCase)) return Unauthorized();
        var usuario = auth.ValidarToken(bearer[7..].Trim());
        if (string.IsNullOrWhiteSpace(usuario)) return Unauthorized();
        HttpContext.Items["usuario"] = usuario;
        return NoContent();
    }

    [HttpGet("/sesion")]
    public async Task<IActionResult> Obtener()
    {
        Response.Headers.CacheControl = "no-store";
        var bearer = Request.Headers.Authorization.ToString();
        if (!bearer.StartsWith("Bearer ", StringComparison.OrdinalIgnoreCase)) return Unauthorized();
        var usuario = auth.ValidarToken(bearer[7..].Trim());
        if (string.IsNullOrWhiteSpace(usuario)) return Unauthorized();
        HttpContext.Items["usuario"] = usuario;
        var sesion = await identidad.Obtener(usuario);
        return sesion is null ? StatusCode(403, new { error = "Cuenta sin acceso autorizado" }) : Ok(sesion);
    }
}
