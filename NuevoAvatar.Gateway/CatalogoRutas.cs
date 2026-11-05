namespace NuevoAvatar.Gateway;

public sealed record RutaGateway(string Externa, string Interna, string Servicio, int Puerto,
    string[] Verbos, string[] Roles, bool Subrutas = false)
{
    public string Patron => Externa + (Subrutas ? "/{**resto}" : "");
    // IDs estables del contrato rol-módulo; el nombre visible puede cambiar.
    public string? Modulo => Servicio switch {
        "Usuario" => "ADM4", "Rol" => "ADM5", "Parametro" => "ADM6", "Modulo" => "ADM7",
        "Institucion" => "ADM8", "Carrera" => "ADM9", "Curso" => "ADM10", "Profesor" => "ADM11",
        "Periodo" => "ADM12", "Grupo" => "ADM13", "Prematricula" => "ADM14", "Matricula" => "ADM15",
        "Expediente" => "ADM16", "Notas" => "ADM17", "HistorialAcademico" => "ADM18",
        "ListadoEstudiantes" => "ADM19", "Factura" => "ADM20", "Pago" => "ADM21",
        "Notificacion" => "ADM22", "Bitacora" => "ADM23",
        "Autenticacion" or "Direccion" => null,
        _ => throw new InvalidOperationException($"Falta definir el modulo del servicio {Servicio}")
    };
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
        // La variante con ID es administrativa. /gateway/menu sin ID se resuelve
        // localmente para la identidad autenticada y nunca acepta un rol del navegador.
        rutas.Add(new("/gateway/menu", "/modulo/menu", "Modulo", 5162, ["GET"], Admin, true));
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
