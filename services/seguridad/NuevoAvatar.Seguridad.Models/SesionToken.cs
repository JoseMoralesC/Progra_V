using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NuevoAvatar.Seguridad.Models
{
    public class SesionToken
    {
        public int IdToken { get; set; }
        public string EmailUsuario { get; set; }=string.Empty;
        public string RefreshToken { get; set; }= string.Empty;

        public DateTime FechaCreacion { get; set; }
        public DateTime FechaExpiracion { get; set; }
        public bool Revocado { get; set; }
    }
}
