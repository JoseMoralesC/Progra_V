using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using NuevoAvatar.Seguridad.Abstract;
using NuevoAvatar.Seguridad.Models;
using NuevoAvatarSeguridad.Controllers;
using Xunit;

namespace NuevoAvatarSeguridad.Tests;

public class AuthControllerTests
{
    [Fact]
    public async Task Login_RechazaDatosVacios()
    {
        var controller = CrearController(new AuthFalso());

        var resultado = await controller.Login(" ", null);

        Assert.IsType<BadRequestObjectResult>(resultado);
    }

    [Fact]
    public async Task Login_Devuelve401ParaCredencialesInvalidas()
    {
        var controller = CrearController(new AuthFalso());

        var resultado = await controller.Login("persona@cuc.cr", "incorrecta");

        Assert.IsType<UnauthorizedObjectResult>(resultado);
    }

    [Fact]
    public async Task Login_Devuelve201ParaCredencialesValidas()
    {
        var respuesta = new LoginResponse
        {
            AccessToken = "jwt",
            RefreshToken = "refresh",
            ExpiresIn = DateTime.UtcNow.AddMinutes(15)
        };
        var controller = CrearController(new AuthFalso(respuesta, "persona@cuc.cr"));

        var resultado = await controller.Login("persona@cuc.cr", "correcta");

        var creado = Assert.IsType<ObjectResult>(resultado);
        Assert.Equal(StatusCodes.Status201Created, creado.StatusCode);
        Assert.Same(respuesta, creado.Value);
    }

    [Fact]
    public void Validate_LeeBearerYDevuelveTrue()
    {
        var controller = CrearController(new AuthFalso(null, "persona@cuc.cr"));
        controller.Request.Headers.Authorization = "Bearer token-valido";

        var resultado = controller.Validate(null);

        var ok = Assert.IsType<OkObjectResult>(resultado);
        Assert.Equal(true, ok.Value);
    }

    [Fact]
    public void Validate_RechazaTokenInvalido()
    {
        var controller = CrearController(new AuthFalso());
        controller.Request.Headers.Authorization = "Bearer token-invalido";

        Assert.IsType<UnauthorizedResult>(controller.Validate(null));
    }

    private static AuthController CrearController(IAuthService auth)
    {
        return new AuthController(auth)
        {
            ControllerContext = new ControllerContext
            {
                HttpContext = new DefaultHttpContext()
            }
        };
    }

    private sealed class AuthFalso : IAuthService
    {
        private readonly LoginResponse? _login;
        private readonly string? _usuarioValidado;

        public AuthFalso(
            LoginResponse? login = null,
            string? usuarioValidado = null)
        {
            _login = login;
            _usuarioValidado = usuarioValidado;
        }

        public Task<LoginResponse?> Login(string email, string password) =>
            Task.FromResult(_login);

        public Task<LoginResponse?> Refresh(string refreshToken) =>
            Task.FromResult(_login);

        public string? ValidarToken(string token) => _usuarioValidado;
    }
}
