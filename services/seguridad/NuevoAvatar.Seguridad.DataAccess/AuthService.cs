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

        public AuthService(SeguridadDbContext context, JwtSettings settings)
        {
            _context = context;
            _settings = settings;
        }

        public async Task<LoginResponse?> Login(string email, string password)
        {
            var usuario = await _context.Usuarios.FirstOrDefaultAsync(u => u.Email == email);
            if (usuario == null) return null;

            bool claveOk;
            try { claveOk = BCrypt.Net.BCrypt.Verify(password, usuario.PasswordHash); }
            catch { claveOk = false; }
            if (!claveOk) return null;

            var respuesta = await GenerarTokens(usuario.Email);
            respuesta.UsuarioID = usuario.Email;
            return respuesta;
        }

        public async Task<LoginResponse?> Refresh(string refreshToken)
        {
            var sesion = await _context.SesionesToken
                .FirstOrDefaultAsync(s => s.RefreshToken == refreshToken);

            if (sesion == null || sesion.Revocado || sesion.FechaExpiracion < DateTime.Now)
                return null;

            sesion.Revocado = true;
            await _context.SaveChangesAsync();

            return await GenerarTokens(sesion.EmailUsuario);
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
            var p = await _context.Parametros.FindAsync(id);
            return p != null && int.TryParse(p.Valor, out var v) && v > 0 ? v : porDefecto;
        }
    }
}
