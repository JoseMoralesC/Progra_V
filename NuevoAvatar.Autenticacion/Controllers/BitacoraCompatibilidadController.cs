using Microsoft.AspNetCore.Mvc;

namespace NuevoAvatarSeguridad.Controllers;

// Mantengo la ruta anterior para los clientes existentes de Alejandro.
[ApiController]
[Route("bitacora")]
public class BitacoraCompatibilidadController : ControllerBase
{
    private static readonly HttpClient Cliente = new() { Timeout = TimeSpan.FromSeconds(10) };

    [HttpGet]
    [HttpGet("{id:long}")]
    [HttpPost]
    public async Task<IActionResult> Enviar()
    {
        var destino = Environment.GetEnvironmentVariable("BITACORA_URL") ?? "http://localhost:5159";
        using var mensaje = new HttpRequestMessage(new HttpMethod(Request.Method), destino + Request.Path + Request.QueryString);
        if (Request.Headers.TryGetValue("Authorization", out var token))
            mensaje.Headers.TryAddWithoutValidation("Authorization", token.ToString());
        if (Request.Method == "POST")
        {
            using var lector = new StreamReader(Request.Body);
            mensaje.Content = new StringContent(await lector.ReadToEndAsync(), System.Text.Encoding.UTF8, "application/json");
        }
        try
        {
            using var respuesta = await Cliente.SendAsync(mensaje, HttpContext.RequestAborted);
            return new ContentResult { StatusCode = (int)respuesta.StatusCode, ContentType = respuesta.Content.Headers.ContentType?.ToString() ?? "application/json", Content = await respuesta.Content.ReadAsStringAsync() };
        }
        catch (HttpRequestException) { return StatusCode(503, "Bitacora no disponible"); }
        catch (TaskCanceledException) { return StatusCode(503, "Bitacora no disponible"); }
    }
}
