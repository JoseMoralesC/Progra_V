namespace NuevoAvatar.Gateway;

public sealed record RutaGateway(string Externa, string Interna, string Servicio, int Puerto,
    string[] Verbos, string[] Roles, bool Subrutas = false)
{
    public string Patron => Externa + (Subrutas ? "/{**resto}" : "");
}

public static class CatalogoRutas
{
    private static readonly string[] Todos = ["ADMINISTRADOR", "PROFESOR", "ESTUDIANTE"];
    private static readonly string[] Admin = ["ADMINISTRADOR"];
    private static readonly string[] Profesor = ["PROFESOR"];
    private static readonly string[] Crud = ["GET", "POST", "PUT", "DELETE"];
    public static IReadOnlyList<RutaGateway> Todas { get; } = Crear();

    private static List<RutaGateway> Crear()
    {
        var rutas = new List<RutaGateway>();
        foreach (var (path, verbos) in new (string, string[])[] {
            ("login", ["POST"]), ("refresh", ["POST"]), ("validate", ["GET"]),
            ("sesion", ["GET"]), ("logout", ["POST"]) })
            rutas.Add(new($"/gateway/auth/{path}", $"/{path}", "Autenticacion", 5158, verbos, Todos));

        foreach (var (recurso, servicio, puerto) in new[] {
            ("usuario", "Usuario", 8082), ("rol", "Rol", 5160), ("parametro", "Parametro", 5161),
            ("modulo", "Modulo", 5162), ("institucion", "Institucion", 8086), ("carrera", "Carrera", 8088),
            ("curso", "Curso", 5255), ("profesor", "Profesor", 8087), ("periodo", "Periodo", 5262),
            ("grupo", "Grupo", 5256), ("expediente", "Expediente", 8084) })
            rutas.Add(new($"/gateway/admin/{recurso}", $"/{recurso}", servicio, puerto, Crud, Admin, true));
        rutas.Add(new("/gateway/admin/user", "/usuario", "Usuario", 8082, Crud, Admin, true));
        foreach (var (recurso, servicio, puerto) in new[] {
            ("prematricula", "Prematricula", 8089), ("matricula", "Matricula", 8085),
            ("historialacademico", "HistorialAcademico", 8090), ("listadoestudiantes", "ListadoEstudiantes", 8091) })
            rutas.Add(new($"/gateway/mat/{recurso}", $"/{recurso}", servicio, puerto, Crud, Admin, true));
        foreach (var (recurso, servicio, puerto) in new[] {
            ("factura", "Factura", 8093), ("pago", "Pago", 8094), ("notificar", "Notificacion", 8095) })
            rutas.Add(new($"/gateway/fin/{recurso}", $"/{recurso}", servicio, puerto, Crud, Admin, true));
        rutas.Add(new("/gateway/admin/bitacora", "/bitacora", "Bitacora", 5159, ["GET", "POST"], Admin, true));
        foreach (var path in new[] { "provincias", "cantones", "distritos" })
            rutas.Add(new($"/gateway/mat/{path}", $"/{path}", "Direccion", 5227, ["GET"], Todos, true));
        foreach (var (path, verbos) in new (string, string[])[] {
            ("cargardesglose", ["POST"]), ("asignarnotarubro", ["POST", "PUT"]),
            ("obtenerdesglose", ["GET"]), ("obtenernotas", ["GET"]), ("obtenernotasgrupo", ["GET"]),
            ("profesor/grupos", ["GET"]) })
            rutas.Add(new($"/gateway/mat/{path}", $"/{path}", "Notas", 8092, verbos, Profesor));
        return rutas;
    }
}
