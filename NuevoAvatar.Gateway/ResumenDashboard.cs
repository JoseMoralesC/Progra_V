using System.Net.Http.Headers;
using System.Text.Json;
using NuevoAvatar.Gateway.Seguridad;
using NuevoAvatar.Seguridad.Models;

namespace NuevoAvatar.Gateway;

public static class ResumenDashboard
{
    private const int Limite = 10;
    public sealed record Evento(long IdBitacora, string Usuario, string Descripcion, DateTime FechaHora);
    public sealed record Envio(long Id, string Asunto, string Estado, DateTimeOffset Fecha);

    public static async Task<IResult> Obtener(HttpContext context, IHttpClientFactory clientes, string servicio)
    {
        if (context.Items["sesion"] is not SesionActual sesion ||
            context.Items[PermisosGateway.ClaveMenu] is not List<ModuloPermitido> menu)
            return Results.Unauthorized();
        if (sesion.Rol is not ("ADMINISTRADOR" or "PROFESOR")) return Results.StatusCode(403);
        if (servicio == "Notificacion" && sesion.Rol == "ADMINISTRADOR" && !menu.Any(m => m.IdModulo == "ADM22"))
            return Results.StatusCode(403);
        try {
            return servicio == "Bitacora"
                ? Results.Ok(await Bitacoras(context, clientes, sesion, menu))
                : Results.Ok(new { items = await Notificaciones(context, clientes, sesion) });
        } catch (Exception error) when (error is HttpRequestException or TaskCanceledException or JsonException) {
            context.RequestAborted.ThrowIfCancellationRequested();
            return Results.Json(new { error = "No se pudo consultar el resumen del dashboard." }, statusCode: 503);
        }
    }

    private static async Task<List<Evento>> Bitacoras(HttpContext context, IHttpClientFactory clientes,
        SesionActual sesion, List<ModuloPermitido> menu)
    {
        var datos = await Consultar<List<Bitacora>>(context, clientes, "dashboard-bitacora", "/bitacora");
        if (datos.Any(b => b is null || string.IsNullOrWhiteSpace(b.Usuario) || b.Descripcion is null || b.IdBitacora <= 0))
            throw new JsonException("Bitácora incompleta");
        var permisos = menu.ToDictionary(m => m.IdModulo, m => m.NombreModulo);
        var candidatos = datos.Select(b => (Registro: b, Modulo: ClasificacionBitacora.Modulo(b.Descripcion)))
            .Where(b => b.Modulo is not null && (permisos.ContainsKey(b.Modulo) ||
                b.Modulo == "ADM3" && EsPropio(b.Registro, sesion)))
            .OrderByDescending(b => b.Registro.FechaHora).ThenByDescending(b => b.Registro.IdBitacora).ToList();
        var grupos = new HashSet<int>();
        var matriculas = new Dictionary<int, int>();
        var rubros = new Dictionary<int, int>();
        if (sesion.Rol == "PROFESOR" && candidatos.Any(b => b.Modulo == "ADM17" && !EsPropio(b.Registro, sesion))) {
            var autorizados = await Consultar<List<Grupo>>(context, clientes, "dashboard-Notas", "/profesor/grupos");
            foreach (var grupo in autorizados) {
                if (grupo is null || grupo.GrupoId <= 0) throw new JsonException("Grupo inválido");
                grupos.Add(grupo.GrupoId);
                var alumnos = await Consultar<List<Matricula>>(context, clientes, "dashboard-Notas", $"/obtenernotasgrupo?grupoId={grupo.GrupoId}");
                var desglose = await Consultar<List<Rubro>>(context, clientes, "dashboard-Notas", $"/obtenerdesglose?grupoId={grupo.GrupoId}");
                foreach (var alumno in alumnos) {
                    if (alumno is null || alumno.MatriculaId <= 0 ||
                        matriculas.TryGetValue(alumno.MatriculaId, out var anterior) && anterior != grupo.GrupoId)
                        throw new JsonException("Matrícula inválida");
                    matriculas[alumno.MatriculaId] = grupo.GrupoId;
                }
                foreach (var rubro in desglose) {
                    if (rubro is null || rubro.Id <= 0 || rubro.GrupoId != grupo.GrupoId ||
                        rubros.TryGetValue(rubro.Id, out var anterior) && anterior != grupo.GrupoId)
                        throw new JsonException("Rubro inválido");
                    rubros[rubro.Id] = grupo.GrupoId;
                }
            }
        }
        return candidatos.Where(b => sesion.Rol == "ADMINISTRADOR" ||
                b.Modulo == "ADM3" && EsPropio(b.Registro, sesion) ||
                b.Modulo == "ADM17" && (EsPropio(b.Registro, sesion) ||
                    ClasificacionBitacora.PerteneceAGrupos(b.Registro.Descripcion, grupos, matriculas, rubros)))
            .Take(Limite).Select(b => new Evento(b.Registro.IdBitacora, b.Registro.Usuario,
                ClasificacionBitacora.Accion(b.Registro.Descripcion) + " · " +
                (b.Modulo == "ADM3" ? "Dashboard" : permisos[b.Modulo!]), b.Registro.FechaHora)).ToList();
    }

    private static async Task<List<Envio>> Notificaciones(HttpContext context, IHttpClientFactory clientes, SesionActual sesion)
    {
        var resultado = new List<Envio>();
        var profesor = sesion.Rol == "PROFESOR";
        var size = profesor ? 100 : Limite;
        for (var pagina = 0; ; pagina++) {
            var ruta = $"/notificar/historial?page={pagina}&size={size}";
            if (profesor) ruta += "&estado=ENVIADA&destinatario=" + Uri.EscapeDataString(sesion.UsuarioID);
            var historial = await Consultar<Historial>(context, clientes, "dashboard-Notificacion", ruta);
            if (historial.Items is null || historial.Total < 0 || historial.Items.Count > size)
                throw new JsonException("Historial inválido");
            foreach (var envio in historial.Items) {
                if (envio is null || envio.Id <= 0 || envio.Asunto is null || envio.Estado is null || envio.Destinatarios is null)
                    throw new JsonException("Notificación incompleta");
                // El servicio busca por coincidencia parcial; la autorización exige el correo completo.
                if (profesor && (envio.Estado != "ENVIADA" || !envio.Destinatarios.Split(',')
                    .Any(correo => correo.Trim().Equals(sesion.UsuarioID, StringComparison.OrdinalIgnoreCase)))) continue;
                resultado.Add(new Envio(envio.Id, envio.Asunto, envio.Estado, envio.Fecha));
            }
            if (resultado.Count >= Limite || (long)(pagina + 1) * size >= historial.Total) break;
            if (historial.Items.Count == 0) throw new JsonException("Paginación inconsistente");
        }
        return resultado.OrderByDescending(n => n.Fecha).ThenByDescending(n => n.Id).Take(Limite).ToList();
    }

    private static bool EsPropio(Bitacora registro, SesionActual sesion) =>
        registro.Usuario.Equals(sesion.UsuarioID, StringComparison.OrdinalIgnoreCase);

    private static async Task<T> Consultar<T>(HttpContext context, IHttpClientFactory clientes, string nombre, string ruta)
    {
        using var cliente = clientes.CreateClient(nombre);
        using var solicitud = new HttpRequestMessage(HttpMethod.Get, ruta);
        solicitud.Headers.Authorization = AuthenticationHeaderValue.Parse(context.Request.Headers.Authorization.ToString());
        using var respuesta = await cliente.SendAsync(solicitud, context.RequestAborted);
        respuesta.EnsureSuccessStatusCode();
        var datos = await respuesta.Content.ReadFromJsonAsync<T>(cancellationToken: context.RequestAborted);
        return datos ?? throw new JsonException("Respuesta vacía");
    }

    private sealed record Grupo(int GrupoId);
    private sealed record Matricula(int MatriculaId);
    private sealed record Rubro(int Id, int GrupoId);
    private sealed record Notificacion(long Id, string Destinatarios, string Asunto, string Estado, DateTimeOffset Fecha);
    private sealed record Historial(List<Notificacion> Items, long Total);
}
