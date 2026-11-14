using Microsoft.Extensions.Configuration;

namespace NuevoAvatar.Integracion;

public static class ConfiguracionEntorno
{
    // El entorno del proceso tiene prioridad. Nunca se ejecuta el contenido del .env.
    public static void Cargar()
    {
        for (var carpeta = new DirectoryInfo(Directory.GetCurrentDirectory()); carpeta is not null; carpeta = carpeta.Parent)
        {
            var archivo = Path.Combine(carpeta.FullName, ".env");
            if (!File.Exists(archivo)) continue;
            foreach (var linea in File.ReadLines(archivo))
            {
                var dato = linea.Trim();
                if (dato.StartsWith("export ")) dato = dato[7..].TrimStart();
                if (dato.StartsWith('#')) continue;
                var separador = dato.IndexOf('=');
                if (separador <= 0) continue;
                var nombre = dato[..separador].Trim();
                if (Environment.GetEnvironmentVariable(nombre) is not null) continue;
                dato = dato[(separador + 1)..].Trim();
                if (dato.Length > 0 && (dato[0] == '\'' || dato[0] == '"'))
                {
                    var cierre = dato.IndexOf(dato[0], 1);
                    dato = cierre > 0 ? dato[1..cierre] : "";
                }
                else dato = dato.Split('#', 2)[0].Trim();
                Environment.SetEnvironmentVariable(nombre, dato);
            }
            break;
        }
    }

    public static string Origen(IConfiguration config, string nombre) => Validar(config[nombre], nombre);
    public static string Origen(string nombre) => Validar(Environment.GetEnvironmentVariable(nombre), nombre);

    public static string Escucha(IConfiguration config, string servicio)
    {
        var origen = Origen(config, servicio + "_BASE_URL");
        return string.Equals(config["NUEVOAVATAR_HTTPS_PROFILE"], "true", StringComparison.OrdinalIgnoreCase)
            ? Origen(config, servicio + "_HTTPS_BASE_URL") + ";" + origen
            : origen;
    }

    private static string Validar(string? valor, string nombre)
    {
        if (!Uri.TryCreate(valor, UriKind.Absolute, out var uri) ||
            (uri.Scheme != "http" && uri.Scheme != "https") || string.IsNullOrEmpty(uri.Host) ||
            uri.Port <= 0 || uri.UserInfo.Length > 0 || uri.AbsolutePath != "/" || uri.Query.Length > 0 || uri.Fragment.Length > 0)
            throw new InvalidOperationException($"Configura {nombre} en el .env raiz con un origen HTTP(S) valido, sin rutas, parametros ni credenciales.");
        return uri.GetLeftPart(UriPartial.Authority);
    }
}
