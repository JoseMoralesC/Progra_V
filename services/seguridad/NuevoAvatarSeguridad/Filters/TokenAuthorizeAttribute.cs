using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;
using NuevoAvatar.Seguridad.Abstract;

namespace NuevoAvatarSeguridad.Filters
{
    public class TokenAuthorizeAttribute : Attribute, IAuthorizationFilter
    {
        public void OnAuthorization(AuthorizationFilterContext context)
        {
            var header = context.HttpContext.Request.Headers.Authorization.ToString();
            var token = header.StartsWith("Bearer ", StringComparison.OrdinalIgnoreCase)
                ? header.Substring(7).Trim()
                : header.Trim();

            var auth = context.HttpContext.RequestServices.GetRequiredService<IAuthService>();
            var email = string.IsNullOrWhiteSpace(token) ? null : auth.ValidarToken(token);

            if (email == null)
            {
                context.Result = new UnauthorizedObjectResult("No autorizado");
                return;
            }

            context.HttpContext.Items["usuario"] = email;
        }
    }
}
