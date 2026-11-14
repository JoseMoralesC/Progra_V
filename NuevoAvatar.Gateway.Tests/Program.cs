using System.Net;
using System.Net.Http.Json;
using Microsoft.Extensions.Logging.Abstractions;
using NuevoAvatar.Gateway;
using NuevoAvatar.Gateway.Seguridad;
using NuevoAvatar.Seguridad.Abstract;
using NuevoAvatar.Seguridad.Models;
using NuevoAvatarSeguridad;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using NuevoAvatar.Integracion;

var verificaciones = 0;
if (args.Contains("--entrega-sql")) { await PruebasEntrega.Ejecutar(); return; }
void Igual<T>(T esperado, T actual) {
    if (!EqualityComparer<T>.Default.Equals(esperado, actual))
        throw new Exception($"Esperado: {esperado}; recibido: {actual}");
    verificaciones++;
}

foreach (var (descripcion, modulo, accion, resultado) in new[] {
    ("Inicio de sesion exitoso. Estado HTTP: 201", "SESION", "Inicio de sesión", "Exitoso"),
    ("Intento de login. Estado HTTP: 401", "SESION", "Inicio de sesión", "Fallido"),
    ("Cierre de sesion. Estado HTTP: 200", "SESION", "Cierre de sesión", "Exitoso"),
    ("Modificación de usuario. JSON: {}. Estado HTTP: 200", "ADM4", "Usuario actualizado", "Exitoso"),
    ("PUT /usuario/1. Estado HTTP: 422", "ADM4", "Actualización de usuario fallida", "Fallido"),
    ("DELETE /usuario/1. Estado HTTP: 403", "ADM4", "Acceso denegado", "Denegado"),
    ("DELETE /usuario/1. Estado HTTP: 500", "ADM4", "Eliminación de usuario fallida", "Fallido"),
    ("Eliminación de usuario: {}", "ADM4", "Eliminación de usuario", "Sin especificar"),
    ("El usuario consulta roles. Resultado: Exitoso", "ADM5", "Roles consultados", "Exitoso"),
    ("Creación de carrera: {}. Estado HTTP: 201", "ADM9", "Carrera creada", "Exitoso"),
    ("El usuario consulta /usuario. Estado HTTP: 200", "ADM4", "Usuarios consultados", "Exitoso"),
    ("El usuario consulta el dashboard. Resultado: Exitoso", "ADM3", "Dashboard consultado", "Exitoso")
}) {
    Igual(modulo, ClasificacionBitacora.Modulo(descripcion));
    Igual(accion, ClasificacionBitacora.Mensaje(descripcion, modulo));
    Igual(resultado, ClasificacionBitacora.Resultado(descripcion));
}

var nota = "Alta de nota. JSON nuevo: {\"matriculaId\":1,\"rubroId\":2}. Resultado: Exitoso";
Igual<string?>(null, ClasificacionBitacora.Modulo("El usuario consulta su sesion. Estado HTTP: 200"));
Igual(true, ClasificacionBitacora.PerteneceAGrupos(nota, [3], new Dictionary<int,int>{{1,3}}, new Dictionary<int,int>{{2,3}}));
Igual(false, ClasificacionBitacora.PerteneceAGrupos(nota, [4], new Dictionary<int,int>{{1,3}}, new Dictionary<int,int>{{2,3}}));

var registros = new List<Bitacora> {
    new() { IdBitacora = 1, Usuario = "propio", Descripcion = "Inicio de sesion exitoso. Estado HTTP: 201", FechaHora = DateTime.Today },
    new() { IdBitacora = 2, Usuario = "otro", Descripcion = "Inicio de sesion fallido. Estado HTTP: 401", FechaHora = DateTime.Today },
    new() { IdBitacora = 3, Usuario = "otro", Descripcion = "Modificación de usuario. Estado HTTP: 200", FechaHora = DateTime.Today },
    new() { IdBitacora = 4, Usuario = "propio", Descripcion = "DELETE /usuario. Estado HTTP: 403", FechaHora = DateTime.Today }
};
var clientes = new Clientes(registros);
DefaultHttpContext Contexto(string rol, params string[] permisos) {
    var ctx = new DefaultHttpContext();
    ctx.Request.Headers.Authorization = "Bearer prueba";
    ctx.Items["sesion"] = new SesionActual("propio", "Nombre", rol, "", "", null, null);
    ctx.Items[PermisosGateway.ClaveMenu] = permisos.Select(id => new ModuloPermitido(id, id, true, 1)).ToList();
    return ctx;
}
async Task<List<ResumenDashboard.Evento>> Resumen(DefaultHttpContext ctx) {
    var valor = ((IValueHttpResult)await ResumenDashboard.Obtener(ctx, clientes, "Bitacora")).Value;
    return valor is ResumenDashboard.HistorialBitacora historial ? historial.Items : (List<ResumenDashboard.Evento>)valor!;
}
var admin = await Resumen(Contexto("ADMINISTRADOR", "ADM4"));
Igual("4,3,2,1", string.Join(',', admin.Select(e => e.IdBitacora)));
Igual(registros[2].Descripcion, admin.Single(e => e.IdBitacora == 3).Descripcion);
Igual(registros[2].FechaHora, admin.Single(e => e.IdBitacora == 3).FechaHora);
Igual("{\"password\":\"[OCULTO]\",\"nombre\":\"Visible\"}", NuevoAvatar.Integracion.ProteccionAuditoria.Limpiar("{\"password\":\"secreto\",\"nombre\":\"Visible\"}"));
var auditor = await Resumen(Contexto("ADMINISTRADOR", "ADM23"));
Igual("4,3,2,1", string.Join(',', auditor.Select(e => e.IdBitacora)));
var profesor = await Resumen(Contexto("PROFESOR", "ADM17"));
Igual("4,1", string.Join(',', profesor.Select(e => e.IdBitacora)));
Igual("Acceso restringido", profesor[0].Modulo);
for (int i = 5; i < 25; i++) registros.Add(new() {
    IdBitacora = i, Usuario = "propio", Descripcion = "El usuario consulta usuarios", FechaHora = DateTime.Today
});
admin = await Resumen(Contexto("ADMINISTRADOR", "ADM4"));
Igual(24, admin.Count);
Igual(24L, admin[0].IdBitacora);

// Cada ID debe ser accesible, incluso sin módulos en el menú y sin clasificación.
registros.Add(new() { IdBitacora = 25, Usuario = "tercero", Descripcion = "Evento histórico no reconocido", FechaHora = DateTime.Today });
registros.Add(new() { IdBitacora = 26, Usuario = "otro", Descripcion = "El usuario consulta su sesion. Estado HTTP: 200", FechaHora = DateTime.Today });
var ids = new List<long>();
for (int page = 0; page < 3; page++) {
    var contexto = Contexto("ADMINISTRADOR");
    contexto.Request.QueryString = new QueryString($"?vista=dashboard&page={page}&size=10");
    var resultado = (ResumenDashboard.HistorialBitacora)((IValueHttpResult)await ResumenDashboard.Obtener(contexto, clientes, "Bitacora")).Value!;
    Igual(26, resultado.Total);
    Igual(page, resultado.Page);
    Igual(10, resultado.Size);
    ids.AddRange(resultado.Items.Select(e => e.IdBitacora));
    if (page == 0) {
        Igual("Sin clasificar", resultado.Items.First(e => e.IdBitacora == 25).Modulo);
        Igual("Actividad registrada", resultado.Items.First(e => e.IdBitacora == 25).Accion);
    }
}
Igual(26, ids.Count);
Igual(26, ids.Distinct().Count());
Igual(string.Join(',', registros.OrderByDescending(b => b.FechaHora).ThenByDescending(b => b.IdBitacora).Select(b => b.IdBitacora)), string.Join(',', ids));
foreach (var query in new[] { "?page=-1", "?page=x", "?size=0", "?size=101", "?page=2147483647&size=100", "?hastaId=-1", "?hastaId=x" }) {
    var invalido = Contexto("ADMINISTRADOR");
    invalido.Request.QueryString = new QueryString(query);
    var respuesta = await ResumenDashboard.Obtener(invalido, clientes, "Bitacora");
    Igual(400, ((IStatusCodeHttpResult)respuesta).StatusCode);
}
var fuera = Contexto("ADMINISTRADOR");
fuera.Request.QueryString = new QueryString("?page=10&size=10");
var fueraResultado = (ResumenDashboard.HistorialBitacora)((IValueHttpResult)await ResumenDashboard.Obtener(fuera, clientes, "Bitacora")).Value!;
Igual(0, fueraResultado.Items.Count);
Igual(26, fueraResultado.Total);
// Un movimiento nuevo no desplaza las páginas de una consulta ya iniciada.
registros.Add(new() { IdBitacora = 27, Usuario = "nuevo", Descripcion = "El usuario consulta usuarios", FechaHora = DateTime.Today.AddMinutes(1) });
var estable = Contexto("ADMINISTRADOR");
estable.Request.QueryString = new QueryString("?page=1&size=10&hastaId=26");
var estableResultado = (ResumenDashboard.HistorialBitacora)((IValueHttpResult)await ResumenDashboard.Obtener(estable, clientes, "Bitacora")).Value!;
Igual(26, estableResultado.Total);
Igual(26L, estableResultado.HastaId);
Igual(string.Join(',', ids.Skip(10).Take(10)), string.Join(',', estableResultado.Items.Select(e => e.IdBitacora)));
var actualizado = (ResumenDashboard.HistorialBitacora)((IValueHttpResult)await ResumenDashboard.Obtener(Contexto("ADMINISTRADOR"), clientes, "Bitacora")).Value!;
Igual(27, actualizado.Total);
Igual(27L, actualizado.Items[0].IdBitacora);
var estudiante = await ResumenDashboard.Obtener(Contexto("ESTUDIANTE"), clientes, "Bitacora");
Igual(403, ((IStatusCodeHttpResult)estudiante).StatusCode);
var profesorConPagina = Contexto("PROFESOR", "ADM17");
profesorConPagina.Request.QueryString = new QueryString("?vista=dashboard&page=0&size=100");
Igual("4,1", string.Join(',', (await Resumen(profesorConPagina)).Select(b => b.IdBitacora)));
registros.Clear();
var vacio = (ResumenDashboard.HistorialBitacora)((IValueHttpResult)await ResumenDashboard.Obtener(Contexto("ADMINISTRADOR"), clientes, "Bitacora")).Value!;
Igual(0, vacio.Total);
Igual(0, vacio.Items.Count);
Igual(0L, vacio.HastaId);

var bitacora = new Memoria();
var login = new DefaultHttpContext();
login.Request.Path = "/login";
login.Request.Method = "POST";
login.Request.Headers["usuario"] = "cuenta@ejemplo.cr";
login.Request.Headers["contrasena"] = "NO_GUARDAR";
var middleware = new AuditoriaSesionMiddleware(ctx => { ctx.Response.StatusCode = 401; return Task.CompletedTask; }, NullLogger<AuditoriaSesionMiddleware>.Instance);
await middleware.InvokeAsync(login, bitacora);
Igual("cuenta@ejemplo.cr", bitacora.Registros.Single().Usuario);
Igual("Fallido", ClasificacionBitacora.Resultado(bitacora.Registros.Single().Descripcion));
Igual(false, bitacora.Registros.Single().Descripcion.Contains("NO_GUARDAR"));
login.Request.Path = "/sesion";
login.Request.Method = "GET";
await middleware.InvokeAsync(login, bitacora);
Igual(1, bitacora.Registros.Count);
login.Request.Path = "/login";
login.Request.Method = "POST";
var exito = new AuditoriaSesionMiddleware(ctx => {
    ctx.Items["usuario"] = "validado"; ctx.Response.StatusCode = 201; return Task.CompletedTask;
}, NullLogger<AuditoriaSesionMiddleware>.Instance);
await exito.InvokeAsync(login, bitacora);
Igual("validado", bitacora.Registros.Last().Usuario);
Igual("Exitoso", ClasificacionBitacora.Resultado(bitacora.Registros.Last().Descripcion));
var excepcion = new AuditoriaSesionMiddleware(_ => throw new InvalidOperationException("fallo"), NullLogger<AuditoriaSesionMiddleware>.Instance);
try { await excepcion.InvokeAsync(login, bitacora); } catch (InvalidOperationException) { }
Igual("Fallido", ClasificacionBitacora.Resultado(bitacora.Registros.Last().Descripcion));

var capturas = new Capturas();
var denegado = Contexto("PROFESOR");
denegado.Request.Path = "/gateway/admin/usuario";
denegado.Request.Method = "DELETE";
var fallos = new AuditoriaFallosGateway(ctx => { ctx.Response.StatusCode = 403; return Task.CompletedTask; }, NullLogger<AuditoriaFallosGateway>.Instance);
await fallos.InvokeAsync(denegado, capturas);
Igual(1, capturas.Cuerpos.Count);
Igual(true, capturas.Cuerpos[0].Contains("DELETE /usuario. Estado HTTP: 403"));
Igual(true, capturas.Cuerpos[0].Contains("propio"));
denegado.Items[AuditoriaFallosGateway.ServicioEnviado] = "Usuario";
await fallos.InvokeAsync(denegado, capturas);
Igual(1, capturas.Cuerpos.Count);
denegado.Items.Remove("sesion");
denegado.Items.Remove(AuditoriaFallosGateway.ServicioEnviado);
await fallos.InvokeAsync(denegado, capturas);
Igual(1, capturas.Cuerpos.Count);
// Una marca externa no basta; solo las lecturas exitosas de indicadores autorizados se omiten.
var configDashboard = new ConfigurationBuilder().AddInMemoryCollection(new Dictionary<string,string?> {
    ["SERVICES_INTERNAL_KEY"] = "clave-de-prueba-solo-para-estas-verificaciones"
}).Build();
var consulta = new DefaultHttpContext {
    RequestServices = new ServiceCollection().AddSingleton<IConfiguration>(configDashboard).BuildServiceProvider()
};
consulta.Request.Method = "GET";
consulta.Request.Path = "/gateway/admin/usuario";
consulta.Request.QueryString = new QueryString("?vista=dashboard");
Igual(true, ConsultasDashboard.EsIndicador(consulta));
consulta.Request.Headers[ConsultaAutomatica.Cabecera] = "dashboard";
Igual(false, ConsultaAutomatica.EsCargaDashboard(consulta, 200));
consulta.Request.Headers["X-Servicio-Key"] = "incorrecta";
Igual(false, ConsultaAutomatica.EsCargaDashboard(consulta, 200));
using (var solicitud = new HttpRequestMessage(HttpMethod.Get, "http://prueba/usuario")) {
    ConsultasDashboard.Marcar(consulta, solicitud);
    consulta.Request.Headers["X-Servicio-Key"] = solicitud.Headers.GetValues("X-Servicio-Key").Single();
}
Igual(true, ConsultaAutomatica.EsCargaDashboard(consulta, 200));
foreach (var estado in new[] { 401, 403, 404, 500 }) Igual(false, ConsultaAutomatica.EsCargaDashboard(consulta, estado));
foreach (var metodo in new[] { "POST", "PUT", "PATCH", "DELETE" }) {
    consulta.Request.Method = metodo;
    Igual(false, ConsultaAutomatica.EsCargaDashboard(consulta, 200));
    Igual(false, ConsultasDashboard.EsIndicador(consulta));
}
consulta.Request.Method = "GET";
consulta.Request.QueryString = QueryString.Empty;
Igual(false, ConsultasDashboard.EsIndicador(consulta));
consulta.Request.QueryString = new QueryString("?vista=dashboard");
consulta.Request.Path = "/gateway/admin/usuario/1";
Igual(false, ConsultasDashboard.EsIndicador(consulta));
// El proxy retira las marcas externas incluso en consultas normales de un módulo.
using (var solicitud = new HttpRequestMessage()) {
    await new TransformacionRuta("/gateway/admin/usuario", "/usuario").TransformRequestAsync(consulta, solicitud, "http://prueba", CancellationToken.None);
    Igual(false, solicitud.Headers.Contains(ConsultaAutomatica.Cabecera));
    Igual(false, solicitud.Headers.Contains("X-Servicio-Key"));
}
Console.WriteLine($"{verificaciones} verificaciones de bitácora completadas.");

sealed class Clientes(List<Bitacora> registros) : IHttpClientFactory {
    public HttpClient CreateClient(string name) => new(new Respuesta(registros)) { BaseAddress = new Uri("http://prueba") };
}
sealed class Respuesta(List<Bitacora> registros) : HttpMessageHandler {
    protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken) {
        if (request.RequestUri!.AbsolutePath == "/bitacora/pagina") {
            var query = Microsoft.AspNetCore.WebUtilities.QueryHelpers.ParseQuery(request.RequestUri.Query);
            int page = int.Parse(query["page"]!), size = int.Parse(query["size"]!);
            long hastaId = query.ContainsKey("hastaId") ? long.Parse(query["hastaId"]!) : registros.Select(b => b.IdBitacora).DefaultIfEmpty(0).Max();
            var filtrados = registros.Where(b => b.IdBitacora <= hastaId).ToList();
            var items = filtrados.OrderByDescending(b => b.FechaHora).ThenByDescending(b => b.IdBitacora)
                .Skip(page * size).Take(size).ToList();
            return Task.FromResult(new HttpResponseMessage(HttpStatusCode.OK) { Content = JsonContent.Create(new PaginaBitacora(items, filtrados.Count, page, size, hastaId)) });
        }
        return Task.FromResult(new HttpResponseMessage(HttpStatusCode.OK) { Content = JsonContent.Create(registros) });
    }
}
sealed class Memoria : IBitacoraService {
    public List<Bitacora> Registros { get; } = [];
    public Task<Bitacora> Crear(Bitacora entrada) { Registros.Add(entrada); return Task.FromResult(entrada); }
    public Task<List<Bitacora>> ObtenerTodos() => Task.FromResult(Registros);
    public Task<PaginaBitacora> ObtenerPagina(int page, int size, long? hastaId = null) => throw new NotSupportedException();
    public Task<Bitacora?> ObtenerPorId(long id) => Task.FromResult(Registros.FirstOrDefault(r => r.IdBitacora == id));
}
sealed class Capturas : HttpMessageHandler, IHttpClientFactory, NuevoAvatar.Integracion.IAuditoriaLocal {
    public List<string> Cuerpos { get; } = [];
    public Task<NuevoAvatar.Integracion.EventoPendiente> Registrar(string usuario, string descripcion) {
        Cuerpos.Add(System.Text.Json.JsonSerializer.Serialize(new { usuario, descripcion }));
        return Task.FromResult(new NuevoAvatar.Integracion.EventoPendiente(Guid.NewGuid(), usuario, descripcion, DateTime.Now));
    }
    public HttpClient CreateClient(string name) => new(this, disposeHandler: false) { BaseAddress = new Uri("http://prueba") };
    protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken) {
        Cuerpos.Add(await request.Content!.ReadAsStringAsync(cancellationToken));
        return new HttpResponseMessage(HttpStatusCode.Created);
    }
}
