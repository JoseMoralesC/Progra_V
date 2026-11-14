using System.Net.Http.Headers;
using System.Text.Json;
using NuevoAvatar.Gateway.Seguridad;
using NuevoAvatar.Seguridad.Models;

namespace NuevoAvatar.Gateway;

public static class ResumenDashboard
{
    private const int Limite = 10;
    public sealed record Evento(long IdBitacora, string Usuario, string Accion, string Modulo,
        string Resultado, DateTime FechaHora, string Descripcion, Guid? IdEvento);
    public sealed record HistorialBitacora(List<Evento> Items, int Total, int Page, int Size, long HastaId);
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
            // El administrador consulta el historial completo; su menú no recorta eventos de otros módulos.
            if (servicio == "Bitacora" && sesion.Rol == "ADMINISTRADOR")
                return await HistorialAdministrador(context, clientes, menu);
            return servicio == "Bitacora"
                ? Results.Ok(await Bitacoras(context, clientes, sesion, menu))
                : Results.Ok(new { items = await Notificaciones(context, clientes, sesion) });
        } catch (Exception error) when (error is HttpRequestException or TaskCanceledException or JsonException) {
            context.RequestAborted.ThrowIfCancellationRequested();
            return Results.Json(new { error = "No se pudo consultar el resumen del dashboard." }, statusCode: 503);
        }
    }

    private static async Task<IResult> HistorialAdministrador(HttpContext context, IHttpClientFactory clientes,
        List<ModuloPermitido> menu)
    {
        var page = 0;
        var size = 25;
        long hastaId = 0;
        if (context.Request.Query.ContainsKey("page") && !int.TryParse(context.Request.Query["page"], out page) ||
            context.Request.Query.ContainsKey("size") && !int.TryParse(context.Request.Query["size"], out size) ||
            context.Request.Query.ContainsKey("hastaId") && !long.TryParse(context.Request.Query["hastaId"], out hastaId) ||
            page < 0 || size is < 1 or > 100 || (long)page * size > int.MaxValue || hastaId < 0)
            return Results.BadRequest(new { error = "Página o tamaño de página inválidos." });
        var corte = context.Request.Query.ContainsKey("hastaId") ? $"&hastaId={hastaId}" : "";
        var datos = await Consultar<PaginaBitacora>(context, clientes, "dashboard-bitacora", $"/bitacora/pagina?page={page}&size={size}{corte}");
        if (datos.Items is null || datos.Total < 0 || datos.Page != page || datos.Size != size ||
            datos.HastaId < 0 || corte.Length > 0 && datos.HastaId != hastaId ||
            datos.Items.Count > size || datos.Items.Any(b => b is null || b.IdBitacora <= 0 || b.IdBitacora > datos.HastaId))
            throw new JsonException("Historial de bitácora inválido");
        var nombres = menu.ToDictionary(m => m.IdModulo, m => m.NombreModulo);
        var items = datos.Items.Select(b => {
            var modulo = ClasificacionBitacora.Modulo(b.Descripcion ?? "");
            var nombre = modulo switch {
                "SESION" => "Autenticación", "ADM3" => "Dashboard", null => "Sin clasificar",
                _ => nombres.GetValueOrDefault(modulo) ?? CatalogoRutas.Todas.FirstOrDefault(r => r.Modulo == modulo)?.Servicio ?? "Sistema"
            };
            return new Evento(b.IdBitacora, b.Usuario,
                ClasificacionBitacora.Mensaje(b.Descripcion ?? "", modulo ?? ""), nombre,
                ClasificacionBitacora.Resultado(b.Descripcion ?? ""), b.FechaHora,
                NuevoAvatar.Integracion.ProteccionAuditoria.Limpiar(b.Descripcion ?? ""), b.IdEvento);
        }).ToList();
        return Results.Ok(new HistorialBitacora(items, datos.Total, page, size, datos.HastaId));
    }

    private static async Task<List<Evento>> Bitacoras(HttpContext context, IHttpClientFactory clientes,
        SesionActual sesion, List<ModuloPermitido> menu)
    {
        var datos = await Consultar<List<Bitacora>>(context, clientes, "dashboard-bitacora", "/bitacora");
        if (datos.Any(b => b is null || string.IsNullOrWhiteSpace(b.Usuario) || b.Descripcion is null || b.IdBitacora <= 0))
            throw new JsonException("Bitácora incompleta");
        var permisos = menu.ToDictionary(m => m.IdModulo, m => m.NombreModulo);
        var candidatos = datos.Select(b => (Registro: b, Modulo: ClasificacionBitacora.Modulo(b.Descripcion)))
            .Where(b => b.Modulo is not null && (b.Modulo == "SESION"
                ? EsPropio(b.Registro, sesion) || sesion.Rol == "ADMINISTRADOR" && permisos.ContainsKey("ADM23")
                : permisos.ContainsKey(b.Modulo) ||
                ClasificacionBitacora.Resultado(b.Registro.Descripcion) == "Denegado" && EsPropio(b.Registro, sesion) ||
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
                ClasificacionBitacora.Resultado(b.Registro.Descripcion) == "Denegado" && EsPropio(b.Registro, sesion) ||
                b.Modulo == "SESION" && EsPropio(b.Registro, sesion) ||
                b.Modulo == "ADM3" && EsPropio(b.Registro, sesion) ||
                b.Modulo == "ADM17" && (EsPropio(b.Registro, sesion) ||
                    ClasificacionBitacora.PerteneceAGrupos(b.Registro.Descripcion, grupos, matriculas, rubros)))
            .Take(Limite).Select(b => new Evento(b.Registro.IdBitacora, b.Registro.Usuario,
                ClasificacionBitacora.Mensaje(b.Registro.Descripcion, b.Modulo!),
                b.Modulo == "SESION" ? "Autenticación" : b.Modulo == "ADM3" ? "Dashboard" :
                    permisos.GetValueOrDefault(b.Modulo!, "Acceso restringido"),
                ClasificacionBitacora.Resultado(b.Registro.Descripcion), b.Registro.FechaHora,
                NuevoAvatar.Integracion.ProteccionAuditoria.Limpiar(b.Registro.Descripcion), b.Registro.IdEvento)).ToList();
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
        ConsultasDashboard.Marcar(context, solicitud);
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
