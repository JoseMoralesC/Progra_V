using NuevoAvatar.Integracion;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using NuevoAvatar.Seguridad.Abstract;
using NuevoAvatar.Seguridad.Models;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace NuevoAvatar.Seguridad.DataAccess
{
    public class AuthService : IAuthService
    {
        private readonly SeguridadDbContext _context;
        private readonly JwtSettings _settings;
        private readonly DatosRemotos _remotos;

        public AuthService(SeguridadDbContext context, JwtSettings settings, DatosRemotos remotos)
        {
            _context = context;
            _settings = settings;
            _remotos = remotos;
        }

        public async Task<LoginResponse?> Login(string email, string password)
        {
            var datos = await _remotos.Post("Usuario", "/interno/credenciales", new { email, contrasena = password });
            if (!datos.TryGetProperty("email", out var cuenta)) return null;
            var respuesta = await GenerarTokens(cuenta.GetString()!);
            respuesta.UsuarioID = cuenta.GetString()!;
            return respuesta;
        }

        public async Task<LoginResponse?> Refresh(string refreshToken)
        {
            await using var transaccion = await _context.Database.BeginTransactionAsync();
            var sesion = await _context.SesionesToken
                .FirstOrDefaultAsync(s => s.RefreshToken == refreshToken);

            if (sesion == null || sesion.Revocado || sesion.FechaExpiracion < DateTime.Now)
                return null;

            var modificadas = await _context.SesionesToken
                .Where(s => s.IdToken == sesion.IdToken && !s.Revocado && s.FechaExpiracion >= DateTime.Now)
                .ExecuteUpdateAsync(update => update.SetProperty(s => s.Revocado, true));
            if (modificadas != 1) return null;
            var respuesta = await GenerarTokens(sesion.EmailUsuario);
            await transaccion.CommitAsync();
            return respuesta;
        }

        public string? ValidarToken(string token)
        {
            try
            {
                var handler = new JwtSecurityTokenHandler();
                handler.InboundClaimTypeMap.Clear();

                var principal = handler.ValidateToken(token, new TokenValidationParameters
                {
                    ValidateIssuerSigningKey = true,
                    IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_settings.Key)),
                    ValidateIssuer = false,
                    ValidateAudience = false,
                    ValidateLifetime = true,
                    ClockSkew = TimeSpan.Zero
                }, out _);

                return principal.FindFirst(JwtRegisteredClaimNames.Sub)?.Value;
            }
            catch
            {
                return null;
            }
        }

        private async Task<LoginResponse> GenerarTokens(string email)
        {
            int minutosJwt = await LeerParametro("EXPJWT", 5);
            int minutosRefresh = await LeerParametro("EXPREFRESH", 60);

            if (minutosRefresh <= minutosJwt) throw new InvalidOperationException("EXPREFRESH debe superar EXPJWT");
            var venceUtc = DateTime.UtcNow.AddMinutes(minutosJwt);
            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_settings.Key));
            var credenciales = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

            var jwt = new JwtSecurityToken(
                claims: new[] { new Claim(JwtRegisteredClaimNames.Sub, email) },
                expires: venceUtc,
                signingCredentials: credenciales);

            var refresh = Convert.ToBase64String(RandomNumberGenerator.GetBytes(64));

            _context.SesionesToken.Add(new SesionToken
            {
                EmailUsuario = email,
                RefreshToken = refresh,
                FechaCreacion = DateTime.Now,
                FechaExpiracion = DateTime.Now.AddMinutes(minutosRefresh),
                Revocado = false
            });
            await _context.SaveChangesAsync();

            return new LoginResponse
            {
                ExpiresIn = venceUtc.ToLocalTime(),
                AccessToken = new JwtSecurityTokenHandler().WriteToken(jwt),
                RefreshToken = refresh
            };
        }

        private async Task<int> LeerParametro(string id, int porDefecto)
        {
            var datos = await _remotos.Listar("Parametro", "parametro", new() { ["id_parametro"] = id });
            return datos.Count > 0 && int.TryParse(DatosRemotos.Valor(datos[0], "valor").GetString(), out var v) && v > 0 ? v : porDefecto;
        }
    }
}

