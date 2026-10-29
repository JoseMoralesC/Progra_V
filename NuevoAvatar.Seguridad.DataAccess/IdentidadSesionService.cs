using NuevoAvatar.Integracion;
using NuevoAvatar.Seguridad.Abstract;
using NuevoAvatar.Seguridad.Models;
using System.Text.Json;

namespace NuevoAvatar.Seguridad.DataAccess;

// La identidad se obtiene de los propietarios, nunca de headers de perfil del cliente.
public sealed class IdentidadSesionService(DatosRemotos datos) : IIdentidadSesionService
{
    public async Task<SesionActual?> Obtener(string email)
    {
        var usuarios = await datos.Listar("Usuario", "usuario", new() { ["email"] = email });
        if (usuarios.Count != 1) return null;
        var cuenta = usuarios[0];
        var roles = await datos.Listar("Rol", "rol", new() { ["id_rol"] = DatosRemotos.Valor(cuenta, "id_rol").ToString() });
        if (roles.Count != 1) return null;
        var rol = Texto(roles[0], "nombre_rol").Trim().ToUpperInvariant() switch
        {
            "ADMIN" or "ADMINISTRADOR" => "ADMINISTRADOR",
            "PROFESOR" => "PROFESOR",
            "ESTUDIANTE" => "ESTUDIANTE",
            _ => null
        };
        if (rol is null) return null;
        string identificacion = Texto(cuenta, "identificacion");
        string tipo = Texto(cuenta, "tipo_identificacion");
        string? estudiante = null;
        int? profesor = null;
        if (rol is "ESTUDIANTE" or "PROFESOR")
        {
            var registros = await datos.Listar(
                rol == "ESTUDIANTE" ? "Expediente" : "Profesor",
                rol == "ESTUDIANTE" ? "Estudiante" : "Profesor",
                new() { ["Email"] = email, ["Identificacion"] = identificacion, ["TipoIdentificacion"] = tipo });
            if (registros.Count == 1)
            {
                if (rol == "ESTUDIANTE") estudiante = identificacion;
                else profesor = DatosRemotos.Valor(registros[0], "ProfesorId").GetInt32();
            }
        }
        return new SesionActual(Texto(cuenta, "email"), Texto(cuenta, "nombre"), rol,
            tipo, identificacion, estudiante, profesor);
    }

    private static string Texto(Dictionary<string, JsonElement> fila, string campo) =>
        DatosRemotos.Valor(fila, campo).ToString();
}
