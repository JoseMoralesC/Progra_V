using System.Globalization;
using System.Text;
using System.Text.Json;
using System.Text.RegularExpressions;

namespace NuevoAvatar.Gateway;

public static class ClasificacionBitacora
{
    private static readonly Dictionary<string, string> Entidades = new() {
        ["usuario"] = "ADM4", ["rol"] = "ADM5", ["parametro"] = "ADM6", ["modulo"] = "ADM7",
        ["institucion"] = "ADM8", ["carrera"] = "ADM9", ["curso"] = "ADM10", ["profesor"] = "ADM11",
        ["periodo"] = "ADM12", ["grupo"] = "ADM13", ["prematricula"] = "ADM14", ["matricula"] = "ADM15",
        ["expediente"] = "ADM16", ["nota"] = "ADM17", ["desglose"] = "ADM17", ["factura"] = "ADM20",
        ["pago"] = "ADM21", ["notificacion"] = "ADM22"
    };

    public static string? Modulo(string descripcion)
    {
        var texto = Normalizar(descripcion);
        if (texto == "el usuario consulta el dashboard") return "ADM3";
        if (texto == "el usuario consulta historial academico") return "ADM18";
        if (texto == "el usuario consulta listado de estudiantes") return "ADM19";
        if (Regex.IsMatch(texto, @"^el usuario consulta periodo [1-9][0-9]*\z")) return "ADM12";
        if (texto == "consulta de todos los grupos." ||
            Regex.IsMatch(texto, @"^consulta del grupo [1-9][0-9]*\.\z")) return "ADM13";
        if (texto.StartsWith("envio de notificacion: ", StringComparison.Ordinal)) return "ADM22";
        if (texto.StartsWith("asignacion de roles al modulo ", StringComparison.Ordinal) ||
            texto.StartsWith("el usuario consulta roles del modulo ", StringComparison.Ordinal) ||
            texto.StartsWith("el usuario consulta menu del rol ", StringComparison.Ordinal) ||
            texto == "el usuario consulta su menu de modulos") return "ADM7";

        var consulta = Regex.Match(texto, @"^el usuario consulta (\w+)$");
        if (consulta.Success) {
            var entidad = consulta.Groups[1].Value;
            if (Entidades.TryGetValue(entidad, out var modulo)) return modulo;
            foreach (var (singular, id) in Entidades)
                if (entidad == singular + "s" || entidad == singular + "es") return id;
        }
        var cambio = Regex.Match(texto, @"^(?:alta|creacion|modificacion|eliminacion) (?:de|del) (\w+)(?:[.: ]|$)");
        if (cambio.Success && Entidades.TryGetValue(cambio.Groups[1].Value, out var identificado)) return identificado;

        var ruta = Regex.Match(texto, @"^(?:el usuario consulta |(?:get|post|put|delete) |eliminacion solicitada en |error tecnico en (?:get|post|put|delete) )(/[a-z0-9/_-]+)");
        if (!ruta.Success) return null;
        return CatalogoRutas.Todas.Where(r => r.Modulo is not null)
            .OrderByDescending(r => r.Interna.Length)
            .FirstOrDefault(r => ruta.Groups[1].Value == r.Interna ||
                (r.Subrutas && ruta.Groups[1].Value.StartsWith(r.Interna + "/", StringComparison.Ordinal)))?.Modulo;
    }

    public static string Accion(string descripcion)
    {
        var texto = Normalizar(descripcion);
        if (texto.StartsWith("el usuario consulta ") || texto.StartsWith("consulta ") || texto.StartsWith("get ")) return "Consulta";
        if (texto.StartsWith("alta ") || texto.StartsWith("creacion ") || texto.StartsWith("post ")) return "Registro";
        if (texto.StartsWith("modificacion ") || texto.StartsWith("put ") || texto.StartsWith("asignacion ")) return "Actualización";
        if (texto.StartsWith("eliminacion ") || texto.StartsWith("delete ")) return "Eliminación";
        if (texto.StartsWith("envio ")) return "Envío";
        return "Evento";
    }

    // Solo los formatos emitidos por AuditoriaNotas acreditan relaciones de grupo.
    public static bool PerteneceAGrupos(string descripcion, HashSet<int> grupos,
        IReadOnlyDictionary<int, int> matriculas, IReadOnlyDictionary<int, int> rubros)
    {
        var prefijo = Regex.Match(descripcion, @"^(Alta de|Modificacion de) (nota|desglose)\. JSON (nuevo|anterior): ");
        if (!prefijo.Success) return false;
        var pendiente = descripcion[prefijo.Length..];
        var estados = new List<JsonElement>();
        try {
            while (true) {
                var bytes = Encoding.UTF8.GetBytes(pendiente);
                var lector = new Utf8JsonReader(bytes);
                using var documento = JsonDocument.ParseValue(ref lector);
                estados.Add(documento.RootElement.Clone());
                pendiente = Encoding.UTF8.GetString(bytes[(int)lector.BytesConsumed..]);
                if (pendiente.Length == 0) break;
                const string actual = ". JSON actual: ";
                if (!pendiente.StartsWith(actual, StringComparison.Ordinal) || estados.Count != 1) return false;
                pendiente = pendiente[actual.Length..];
            }
            if (estados.Count != (prefijo.Groups[1].Value == "Alta de" ? 1 : 2)) return false;
            foreach (var estado in estados) {
                if (prefijo.Groups[2].Value == "desglose") {
                    if (!Entero(estado, "grupoId", out var grupo) || !grupos.Contains(grupo)) return false;
                } else {
                    if (!Entero(estado, "matriculaId", out var matricula) || !Entero(estado, "rubroId", out var rubro) ||
                        !matriculas.TryGetValue(matricula, out var grupoMatricula) ||
                        !rubros.TryGetValue(rubro, out var grupoRubro) || grupoMatricula != grupoRubro ||
                        !grupos.Contains(grupoMatricula)) return false;
                }
            }
            return true;
        } catch (JsonException) { return false; }
    }

    private static bool Entero(JsonElement elemento, string nombre, out int valor)
    {
        valor = 0;
        return elemento.ValueKind == JsonValueKind.Object && elemento.TryGetProperty(nombre, out var campo) &&
            campo.ValueKind == JsonValueKind.Number && campo.TryGetInt32(out valor) && valor > 0;
    }

    private static string Normalizar(string texto) => new string(texto.Normalize(NormalizationForm.FormD)
        .Where(c => CharUnicodeInfo.GetUnicodeCategory(c) != UnicodeCategory.NonSpacingMark).ToArray()).ToLowerInvariant();
}
