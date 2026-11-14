using System.Net;
using System.Net.Http.Json;
using Microsoft.Data.SqlClient;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging.Abstractions;
using NuevoAvatar.Integracion;
using NuevoAvatar.Seguridad.DataAccess;
using NuevoAvatar.Seguridad.Models;

static class PruebasEntrega
{
    public static async Task Ejecutar()
    {
        // Solo bases temporales creadas por esta prueba; nunca se borra una base de negocio.
        var servidor = new SqlConnectionStringBuilder {
            DataSource = Environment.GetEnvironmentVariable("DB_HOST") + "," + (Environment.GetEnvironmentVariable("DB_PORT") ?? "1433"),
            UserID = Environment.GetEnvironmentVariable("DB_USERNAME"), Password = Environment.GetEnvironmentVariable("DB_PASSWORD"),
            InitialCatalog = "master", TrustServerCertificate = true
        };
        var nombre = "PrograV_PruebaAuditoria_" + Guid.NewGuid().ToString("N");
        async Task Sql(string cadena, string sql) {
            await using var c = new SqlConnection(cadena); await c.OpenAsync();
            using var cmd = c.CreateCommand(); cmd.CommandText = sql; await cmd.ExecuteNonQueryAsync();
        }
        await Sql(servidor.ConnectionString, $"CREATE DATABASE [{nombre}]");
        var db = new SqlConnectionStringBuilder(servidor.ConnectionString) { InitialCatalog = nombre }.ConnectionString;
        try {
            await Sql(db, "CREATE SCHEMA general");
            await Sql(db, "CREATE TABLE general.bitacora(id_bitacora bigint IDENTITY PRIMARY KEY,usuario varchar(150) NOT NULL,descripcion varchar(max) NOT NULL,fecha_hora datetime NOT NULL)");
            var config = new ConfigurationBuilder().AddInMemoryCollection(new Dictionary<string,string?> {
                ["BITACORA_BASE_URL"] = "http://receptor-de-prueba", ["SERVICES_INTERNAL_KEY"] = new string('x',32)
            }).Build();
            var receptor = new Receptor(db);
            var cola = new AuditoriaLocal(config, receptor, NullLogger<AuditoriaLocal>.Instance);
            cola.Configurar(db, true);
            cola.Configurar(db, false);
            async Task<int> Contar(string tabla) {
                await using var c = new SqlConnection(db); await c.OpenAsync();
                using var cmd = c.CreateCommand(); cmd.CommandText = "SELECT COUNT(*) FROM general." + tabla;
                return Convert.ToInt32(await cmd.ExecuteScalarAsync());
            }
            void Comprobar(bool condicion, string nombre) { if (!condicion) throw new Exception(nombre); Console.WriteLine("OK: " + nombre); }
            var evento = await cola.Registrar("prueba", "Modificación de usuario. JSON: {\"nombre\":\"Nuevo\",\"password\":\"NO_GUARDAR\"}. Resultado: Exitoso");
            Comprobar(await Contar("auditoria_pendiente") == 1 && await Contar("bitacora") == 0, "Receptor caído: el evento queda persistido");
            var reiniciada = new AuditoriaLocal(config, receptor, NullLogger<AuditoriaLocal>.Instance);
            reiniciada.Configurar(db, false);
            receptor.Disponible = true; receptor.PerderConfirmacion = true;
            await Sql(db, "UPDATE general.auditoria_pendiente SET proximo_intento=SYSUTCDATETIME()");
            await reiniciada.Entregar(default);
            Comprobar(await Contar("auditoria_pendiente") == 1 && await Contar("bitacora") == 1, "Reinicio: entrega recuperada aunque se pierde la confirmación");
            await Sql(db, "UPDATE general.auditoria_pendiente SET proximo_intento=SYSUTCDATETIME()");
            await reiniciada.Entregar(default);
            Comprobar(await Contar("auditoria_pendiente") == 0 && await Contar("bitacora") == 1, "Reintento sin duplicar y borrado solo tras confirmación");
            await using var contexto = new SeguridadDbContext(new DbContextOptionsBuilder<SeguridadDbContext>().UseSqlServer(db).Options);
            var guardado = await contexto.Bitacoras.AsNoTracking().SingleAsync();
            Comprobar(guardado.IdEvento == evento.IdEvento && guardado.Descripcion == evento.Descripcion && !guardado.Descripcion.Contains("NO_GUARDAR"), "Identidad y descripción conservadas sin secretos");
            var pagina = await new BitacoraService(contexto).ObtenerPagina(0,25);
            Comprobar(pagina.Items.Single().IdBitacora == guardado.IdBitacora && pagina.Items[0].Descripcion == guardado.Descripcion, "La consulta devuelve el mismo registro escrito");
            var http = new DefaultHttpContext(); http.Items["usuario"] = "prueba";
            var transaccional = new AuditoriaTransaccion(new HttpContextAccessor { HttpContext = http });
            await Sql(db, "CREATE TABLE dbo.negocio(id int PRIMARY KEY,nombre nvarchar(50)); INSERT dbo.negocio VALUES(1,'Anterior')");
            await using (var c = new SqlConnection(db)) {
                await c.OpenAsync(); using var tx = c.BeginTransaction();
                using var cmd = c.CreateCommand(); cmd.Transaction=tx; cmd.CommandText="UPDATE dbo.negocio SET nombre='Nuevo' WHERE id=1"; await cmd.ExecuteNonQueryAsync();
                await transaccional.Registrar(c,tx,"Modificación de usuario", new { nombre="Anterior" },new { nombre="Nuevo" });
                tx.Rollback();
            }
            Comprobar(await Contar("auditoria_pendiente") == 0, "Rollback: se revierte también el evento pendiente");
            await Sql(db, "CREATE SCHEMA seguridad");
            await Sql(db, "CREATE TABLE seguridad.rol(id_rol varchar(20) PRIMARY KEY,nombre_rol varchar(100) NOT NULL); CREATE TABLE seguridad.parametro(id_parametro varchar(10) PRIMARY KEY,valor varchar(500) NOT NULL)");
            await using (var ef = new SeguridadDbContext(new DbContextOptionsBuilder<SeguridadDbContext>().UseSqlServer(db).Options, transaccional)) {
                ef.Roles.Add(new Rol { IdRol="PRUEBA", NombreRol="Anterior" });
                await ef.SaveChangesAsync();
                Comprobar(await Contar("auditoria_pendiente") == 1, "Entity Framework: escritura y evento confirmados juntos");
                await using var tx = await ef.Database.BeginTransactionAsync();
                (await ef.Roles.SingleAsync()).NombreRol="No confirmar";
                await ef.SaveChangesAsync();
                await tx.RollbackAsync();
            }
            await using (var ef = new SeguridadDbContext(new DbContextOptionsBuilder<SeguridadDbContext>().UseSqlServer(db).Options, transaccional)) {
                Comprobar((await ef.Roles.AsNoTracking().SingleAsync()).NombreRol == "Anterior" && await Contar("auditoria_pendiente") == 1, "Entity Framework: rollback no deja una auditoría de éxito");
                ef.Parametros.Add(new Parametro { IdParametro="MAILPASS", Valor="NO_GUARDAR_CORREO" });
                await ef.SaveChangesAsync();
                await using var c = new SqlConnection(db); await c.OpenAsync();
                using var cmd = c.CreateCommand(); cmd.CommandText="SELECT descripcion FROM general.auditoria_pendiente WHERE descripcion LIKE '%parametro%'";
                var detalle=(string)(await cmd.ExecuteScalarAsync())!;
                Comprobar(!detalle.Contains("NO_GUARDAR_CORREO") && detalle.Contains("[OCULTO]"), "La auditoría transaccional oculta parámetros secretos");
            }
            Console.WriteLine("Pruebas SQL de entrega, recuperación, idempotencia y rollback completadas.");
        } finally {
            SqlConnection.ClearAllPools();
            await Sql(servidor.ConnectionString, $"ALTER DATABASE [{nombre}] SET SINGLE_USER WITH ROLLBACK IMMEDIATE; DROP DATABASE [{nombre}]");
        }
    }

    sealed class Receptor(string cadena) : HttpMessageHandler, IHttpClientFactory
    {
        public bool Disponible, PerderConfirmacion;
        public HttpClient CreateClient(string name) => new(this,false);
        protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken) {
            if (!Disponible) throw new HttpRequestException("Receptor de prueba desconectado");
            var evento = await request.Content!.ReadFromJsonAsync<Bitacora>(cancellationToken);
            await using var db = new SeguridadDbContext(new DbContextOptionsBuilder<SeguridadDbContext>().UseSqlServer(cadena).Options);
            var guardado = await new BitacoraService(db).Crear(evento!);
            if (PerderConfirmacion) { PerderConfirmacion=false; throw new HttpRequestException("Confirmación de prueba perdida"); }
            return new HttpResponseMessage(HttpStatusCode.OK) { Content = JsonContent.Create(guardado) };
        }
    }
}
