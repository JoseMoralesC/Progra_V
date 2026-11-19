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
        var texto = TextoBase(descripcion);
        if (EsSesion(texto)) return "SESION";
        if (texto.StartsWith("actualizacion del perfil propio") || texto.StartsWith("cambio de contrasena del perfil propio")) return "ADM4";
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

        var ruta = Regex.Match(texto, @"^(?:el usuario consulta |(?:get|post|put|delete) |eliminacion solicitada en |error tecnico en (?:(?:get|post|put|delete) )?)(/[a-z0-9/_-]+)");
        if (!ruta.Success) return null;
        return CatalogoRutas.Todas.Where(r => r.Modulo is not null)
            .OrderByDescending(r => r.Interna.Length)
            .FirstOrDefault(r => ruta.Groups[1].Value == r.Externa ||
                (r.Subrutas && ruta.Groups[1].Value.StartsWith(r.Externa + "/", StringComparison.Ordinal)) ||
                ruta.Groups[1].Value == r.Interna ||
                (r.Subrutas && ruta.Groups[1].Value.StartsWith(r.Interna + "/", StringComparison.Ordinal)))?.Modulo;
    }

    public static string Accion(string descripcion)
    {
        var texto = Normalizar(descripcion);
        if (texto.StartsWith("el usuario consulta ") || texto.StartsWith("consulta ") || texto.StartsWith("get ")) return "Consulta";
        if (texto.StartsWith("alta ") || texto.StartsWith("creacion ") || texto.StartsWith("post ")) return "Registro";
        if (texto.StartsWith("modificacion ") || texto.StartsWith("put ") || texto.StartsWith("asignacion ")) return "Actualización";
        if (texto.StartsWith("actualizacion ") || texto.StartsWith("cambio de contrasena ")) return "Actualización";
        if (texto.StartsWith("eliminacion ") || texto.StartsWith("delete ")) return "Eliminación";
        if (texto.StartsWith("envio ")) return "Envío";
        return "Evento";
    }

    // El resultado solo se afirma cuando el registro contiene evidencia explícita.
    public static string Resultado(string descripcion)
    {
        var texto = Normalizar(descripcion);
        if (texto.EndsWith(". resultado: exitoso", StringComparison.Ordinal)) return "Exitoso";
        var estado = Regex.Match(texto, @"\. estado(?: http)?: ([1-5][0-9]{2})\z");
        if (estado.Success) {
            var codigo = int.Parse(estado.Groups[1].Value, CultureInfo.InvariantCulture);
            return codigo == 403 ? "Denegado" : codigo >= 400 ? "Fallido" :
                codigo >= 200 && codigo < 300 ? "Exitoso" : "Sin especificar";
        }
        if (texto.StartsWith("error tecnico en ") || texto.StartsWith("inicio de sesion fallido")) return "Fallido";
        if (texto.StartsWith("inicio de sesion exitoso")) return "Exitoso";
        return "Sin especificar";
    }

    public static string Mensaje(string descripcion, string modulo)
    {
        var texto = TextoBase(descripcion);
        var resultado = Resultado(descripcion);
        if (modulo == "SESION") {
            var accionSesion = texto.StartsWith("intento de login") || texto.StartsWith("inicio de sesion")
                ? "Inicio de sesión" : texto.StartsWith("renovacion de sesion")
                ? "Renovación de sesión" : texto.StartsWith("cierre de sesion")
                ? "Cierre de sesión" : "Sesión consultada";
            return accionSesion;
        }
        if (resultado == "Denegado") return "Acceso denegado";
        if (texto.StartsWith("asignacion de roles al modulo ")) return resultado switch {
            "Exitoso" => "Permisos de módulo actualizados",
            "Fallido" => "Actualización de permisos de módulo fallida",
            _ => "Actualización de permisos de módulo"
        };
        var accion = Accion(descripcion);
        var (singular, plural, femenino) = modulo switch {
            "ADM3" => ("Dashboard", "Dashboard", false),
            "ADM4" => ("Usuario", "Usuarios", false), "ADM5" => ("Rol", "Roles", false),
            "ADM6" => ("Parámetro", "Parámetros", false), "ADM7" => ("Módulo", "Módulos", false),
            "ADM8" => ("Institución", "Instituciones", true), "ADM9" => ("Carrera", "Carreras", true),
            "ADM10" => ("Curso", "Cursos", false), "ADM11" => ("Profesor", "Profesores", false),
            "ADM12" => ("Período", "Períodos", false), "ADM13" => ("Grupo", "Grupos", false),
            "ADM14" => ("Prematrícula", "Prematrículas", true), "ADM15" => ("Matrícula", "Matrículas", true),
            "ADM16" => ("Expediente", "Expedientes", false), "ADM17" => ("Nota", "Notas", true),
            "ADM18" => ("Historial académico", "Historial académico", false),
            "ADM19" => ("Listado de estudiantes", "Listado de estudiantes", false),
            "ADM20" => ("Factura", "Facturas", true), "ADM21" => ("Pago", "Pagos", false),
            "ADM22" => ("Notificación", "Notificaciones", true), _ => ("Información", "Información", true)
        };
        if (resultado == "Fallido") return accion switch {
            "Consulta" => $"Consulta de {plural.ToLowerInvariant()} fallida",
            "Registro" => $"Creación de {singular.ToLowerInvariant()} fallida",
            "Actualización" => $"Actualización de {singular.ToLowerInvariant()} fallida",
            "Eliminación" => $"Eliminación de {singular.ToLowerInvariant()} fallida",
            _ => "Operación fallida"
        };
        if (accion == "Consulta") return plural + (modulo is "ADM3" or "ADM18" or "ADM19" ? " consultado" : femenino ? " consultadas" : " consultados");
        // Los eventos históricos sin resultado no se presentan como cambios confirmados.
        if (resultado != "Exitoso") return accion switch {
            "Registro" => $"Creación de {singular.ToLowerInvariant()}",
            "Actualización" => $"Actualización de {singular.ToLowerInvariant()}",
            "Eliminación" => $"Eliminación de {singular.ToLowerInvariant()}",
            "Envío" => "Envío de notificación", _ => "Actividad registrada"
        };
        return accion switch {
            "Registro" => singular + (femenino ? " creada" : " creado"),
            "Actualización" => singular + (femenino ? " actualizada" : " actualizado"),
            "Eliminación" => singular + (femenino ? " eliminada" : " eliminado"),
            "Envío" => "Notificación enviada", _ => "Actividad registrada"
        };
    }

    private static bool EsSesion(string texto) =>
        new[] { "intento de login", "inicio de sesion", "renovacion de sesion", "cierre de sesion" }
            .Any(prefijo => texto.StartsWith(prefijo, StringComparison.Ordinal));

    private static string TextoBase(string descripcion) => Regex.Replace(Normalizar(descripcion),
        @"\. (?:estado(?: http)?: [1-5][0-9]{2}|resultado: exitoso)\z", "");

    // Solo los formatos emitidos por AuditoriaNotas acreditan relaciones de grupo.
    public static bool PerteneceAGrupos(string descripcion, HashSet<int> grupos,
        IReadOnlyDictionary<int, int> matriculas, IReadOnlyDictionary<int, int> rubros)
    {
        descripcion = Regex.Replace(descripcion, @"\. (?:Estado HTTP: [1-5][0-9]{2}|Resultado: Exitoso)\z", "");
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
