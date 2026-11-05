using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NuevoAvatar.Seguridad.Models
{
    public class Modulo
    {
        public string IdModulo { get; set; }=string.Empty;
        public string NombreModulo {  get; set; }=string.Empty;
        public bool Activo { get; set; } = true;
        public int Orden { get; set; }
        public List<string> Roles { get; set; } = [];
    }
}

