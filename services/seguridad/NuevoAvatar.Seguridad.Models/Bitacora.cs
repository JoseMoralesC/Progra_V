using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NuevoAvatar.Seguridad.Models
{
    public class Bitacora
    {
        public long IdBitacora { get; set; }
        public string Usuario { get; set; }=string.Empty;
        public string Descripcion { get; set; }=string.Empty;
        public DateTime FechaHora { get; set; }
    }
}
