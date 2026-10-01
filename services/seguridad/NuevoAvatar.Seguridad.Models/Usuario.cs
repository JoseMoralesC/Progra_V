using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NuevoAvatar.Seguridad.Models
{
    public class Usuario
    {
        public string Email { get; set; }=string.Empty;
        public string TipoIdentificacion { get; set; }=string.Empty;
        public string Identificacion { get; set; }=string.Empty; 
        public string Nombre {  get; set; }=string.Empty;
        public string IdRol { get; set; }=string.Empty;
        public string PasswordHash { get; set; }=string.Empty;
    }
}
