namespace NuevoAvatar.Seguridad.Models;

public sealed class RolModulo
{
    public string IdModulo { get; set; } = string.Empty;
    public string IdRol { get; set; } = string.Empty;
    public Modulo? Modulo { get; set; }
}
