using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using Microsoft.AspNetCore.Http;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging.Abstractions;
using Microsoft.IdentityModel.Tokens;
using NuevoAvatar.Seguridad.Abstract;
using NuevoAvatar.Seguridad.DataAccess;
using NuevoAvatar.Seguridad.Models;
using NuevoAvatarSeguridad;
using Xunit;

namespace NuevoAvatarSeguridad.Tests;

public class TokenYAuditoriaTests
{
    [Fact]
    public void TokenExpiradoYFirmaAjenaNoSonValidos()
    {
        string key = new('k', 64);
        using var db = new SeguridadDbContext(new DbContextOptionsBuilder<SeguridadDbContext>().Options);
        var servicio = new AuthService(db, new JwtSettings { Key = key }, null!);
        Assert.Equal("real@cuc.ac.cr", servicio.ValidarToken(Token(key, false)));
        Assert.Null(servicio.ValidarToken(Token(key, true)));
        Assert.Null(servicio.ValidarToken(Token(new string('x', 64), false)));
        Assert.Null(servicio.ValidarToken("texto-que-no-es-jwt"));
    }

    [Theory]
    [InlineData("/login", "POST", 401)]
    [InlineData("/login", "POST", 201)]
    [InlineData("/refresh", "POST", 201)]
    [InlineData("/sesion", "GET", 200)]
    public async Task AuditoriaNoIncluyeCredenciales(string ruta, string metodo, int status)
    {
        var context = new DefaultHttpContext();
        context.Request.Path = ruta;
        context.Request.Method = metodo;
        context.Request.Headers.Authorization = "Bearer SECRETO_TOKEN";
        context.Request.Headers["contrasena"] = "SECRETO_PASSWORD";
        context.Request.Body = new MemoryStream(Encoding.UTF8.GetBytes("{\"refresh_token\":\"SECRETO_REFRESH\"}"));
        var bitacora = new BitacoraFalsa();
        var middleware = new AuditoriaSesionMiddleware(c => { c.Response.StatusCode = status; return Task.CompletedTask; },
            NullLogger<AuditoriaSesionMiddleware>.Instance);
        await middleware.InvokeAsync(context, bitacora);
        Assert.NotNull(bitacora.Registro);
        Assert.Contains(status.ToString(), bitacora.Registro.Descripcion);
        Assert.DoesNotContain("SECRETO", bitacora.Registro.Descripcion);
    }

    private static string Token(string key, bool expirado)
    {
        var jwt = new JwtSecurityToken(claims: new[] { new Claim("sub", "real@cuc.ac.cr") },
            notBefore: DateTime.UtcNow.AddMinutes(-10), expires: DateTime.UtcNow.AddMinutes(expirado ? -1 : 5),
            signingCredentials: new SigningCredentials(new SymmetricSecurityKey(Encoding.UTF8.GetBytes(key)), SecurityAlgorithms.HmacSha256));
        return new JwtSecurityTokenHandler().WriteToken(jwt);
    }

    private sealed class BitacoraFalsa : IBitacoraService
    {
        public Bitacora? Registro { get; private set; }
        public Task<Bitacora> Crear(Bitacora entrada) { Registro = entrada; return Task.FromResult(entrada); }
        public Task<List<Bitacora>> ObtenerTodos() => throw new NotSupportedException();
        public Task<Bitacora?> ObtenerPorId(long id) => throw new NotSupportedException();
    }
}
