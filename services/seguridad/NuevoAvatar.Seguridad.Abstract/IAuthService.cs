using NuevoAvatar.Seguridad.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NuevoAvatar.Seguridad.Abstract
{
    public interface IAuthService
    {
        Task<LoginResponse?> Login(string email, string password);
        Task<LoginResponse?> Refresh(string refreshToken);
        string? ValidarToken(string token);
    }
}
