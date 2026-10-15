using System.Net;
using System.Net.Http.Json;
using System.Security.Cryptography;
using System.Text;
using System.Text.Json;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Http;
using Microsoft.Data.SqlClient;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace NuevoAvatar.Integracion;

public sealed class DependenciaNoDisponibleException(string servicio, Exception? inner = null)
    : Exception($"No fue posible consultar {servicio}",inner);
public sealed class ReferenciaUsadaException(string servicio) : Exception($"El registro tiene referencias en {servicio}");

public sealed class DatosRemotos
{
    public static readonly JsonSerializerOptions Json = new(JsonSerializerDefaults.Web) { PropertyNameCaseInsensitive = true };
    public static readonly JsonElement Contratos = JsonDocument.Parse(typeof(DatosRemotos).Assembly.GetManifestResourceStream("Contratos.json")!).RootElement.Clone();
    private readonly IHttpClientFactory factory;
    private readonly IConfiguration config;
    public DatosRemotos(IHttpClientFactory factory,IConfiguration config){this.factory=factory;this.config=config;}
    public HttpClient Cliente(string servicio)
    {
        var client=factory.CreateClient("integracion");
        client.BaseAddress=new Uri(config[servicio.ToUpperInvariant()+"_BASE_URL"] ?? $"http://localhost:{Contratos.GetProperty(servicio).GetProperty("port").GetInt32()}");
        client.Timeout=TimeSpan.FromSeconds(8);
        var key=config["SERVICES_INTERNAL_KEY"];
        if(string.IsNullOrWhiteSpace(key) || key.Length<32) throw new InvalidOperationException("Configurar SERVICES_INTERNAL_KEY");
        client.DefaultRequestHeaders.Add("X-Servicio-Key",key);
        return client;
    }
    public async Task<List<Dictionary<string,JsonElement>>> Listar(string servicio,string recurso,Dictionary<string,object?>? filtros=null)
    {
        var query=filtros==null?"": "?"+string.Join("&",filtros.Select(p=>Uri.EscapeDataString(p.Key)+"="+Uri.EscapeDataString(p.Value?.ToString()??"")));
        try {
            using var client=Cliente(servicio);
            using var response=await client.GetAsync("/interno/datos/"+Uri.EscapeDataString(recurso)+query);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<List<Dictionary<string,JsonElement>>>(Json) ?? throw new HttpRequestException("Respuesta vacia");
        } catch(Exception ex) when(ex is HttpRequestException or TaskCanceledException) {throw new DependenciaNoDisponibleException(servicio,ex);}
    }
    public async Task Exigir(string servicio,string recurso,string campo,object id)
    {
        if((await Listar(servicio,recurso,new(){[campo]=id})).Count==0) throw new ArgumentException($"La referencia a {servicio} no existe");
    }
    public async Task SinReferencias(string servicio,string recurso,object id)
    {
        foreach(var reference in Contratos.GetProperty(servicio).GetProperty("references").EnumerateArray()){
            if(reference.GetProperty("resource").GetString()!=recurso)continue;
            string target=reference.GetProperty("service").GetString()!;
            try {
                using var client=Cliente(target);
                string url="/interno/referencias/"+Uri.EscapeDataString(reference.GetProperty("dependent").GetString()!)+"?campo="+Uri.EscapeDataString(reference.GetProperty("field").GetString()!)+"&valor="+Uri.EscapeDataString(id.ToString()!);
                if(await client.GetFromJsonAsync<bool>(url))throw new ReferenciaUsadaException(target);
            }catch(Exception ex) when(ex is HttpRequestException or TaskCanceledException){throw new DependenciaNoDisponibleException(target,ex);}
        }
    }
    public async Task<JsonElement> Post(string servicio,string path,object body)
    {
        try {
            using var client=Cliente(servicio);
            using var response=await client.PostAsJsonAsync(path,body,Json);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadFromJsonAsync<JsonElement>(Json);
        } catch(Exception ex) when(ex is HttpRequestException or TaskCanceledException){throw new DependenciaNoDisponibleException(servicio,ex);}
    }
    public static JsonElement Valor(Dictionary<string,JsonElement> row,string key)=>row.First(p=>p.Key.Equals(key,StringComparison.OrdinalIgnoreCase)).Value;
}

public static class IntegracionExtensions
{
    public static async Task ValidarEliminacion(HttpContext context,string servicio)
    {
        if(!HttpMethods.IsDelete(context.Request.Method))return;
        var parts=context.Request.Path.Value!.Split('/',StringSplitOptions.RemoveEmptyEntries);
        if(parts.Length!=2 || parts[0].Equals("interno",StringComparison.OrdinalIgnoreCase))return;
        var resource=DatosRemotos.Contratos.GetProperty(servicio).GetProperty("resources").EnumerateObject().FirstOrDefault(r=>r.Name.Equals(parts[0],StringComparison.OrdinalIgnoreCase)).Name;
        if(resource!=null)await context.RequestServices.GetRequiredService<DatosRemotos>().SinReferencias(servicio,resource,Uri.UnescapeDataString(parts[1]));
    }
    public static IServiceCollection AddIntegracion(this IServiceCollection services)
    { services.AddHttpClient(); services.AddScoped<DatosRemotos>();return services; }
    public static void MapIntegracion(this WebApplication app,string servicio,string connectionString)
    {
        string database=new SqlConnectionStringBuilder(connectionString).InitialCatalog;
        if(database.Equals(app.Configuration["DB_NAME"]??"PrograV",StringComparison.OrdinalIgnoreCase))throw new InvalidOperationException("Un servicio no puede conectarse a la base de origen");
        if(database!=(app.Configuration["SERVICE_DB_NAME"]??"PrograV_"+servicio))throw new InvalidOperationException("La conexion no apunta a la base propia");
        var key=app.Configuration["SERVICES_INTERNAL_KEY"];
        if(string.IsNullOrWhiteSpace(key) || key.Length<32) throw new InvalidOperationException("Configurar SERVICES_INTERNAL_KEY");
        app.Use(async(context,next)=>{
            if(context.Request.Path.StartsWithSegments("/interno")){
                var received=context.Request.Headers["X-Servicio-Key"].ToString();
                if(!CryptographicOperations.FixedTimeEquals(SHA256.HashData(Encoding.UTF8.GetBytes(key)),SHA256.HashData(Encoding.UTF8.GetBytes(received)))){
                    context.Response.StatusCode=401;return;
                }
            }
            try { await next(); }
            catch(ReferenciaUsadaException ex){
                if(context.Response.HasStarted)throw;
                context.Response.StatusCode=409;await context.Response.WriteAsJsonAsync(new {error=ex.Message});
            }
            catch(DependenciaNoDisponibleException){
                if(context.Response.HasStarted) throw;
                context.Response.StatusCode=503;await context.Response.WriteAsJsonAsync(new {error="Servicio requerido no disponible"});
            }
        });
        app.MapGet("/interno/estado",async()=>{
            using var connection=new SqlConnection(connectionString);await connection.OpenAsync();using var command=connection.CreateCommand();command.CommandText="SELECT DB_NAME()";
            return Results.Ok(new {servicio, @base=(string)(await command.ExecuteScalarAsync())!});
        });
        app.MapGet("/interno/referencias/{resource}",async(string resource,string campo,string valor)=>{
            var resources=DatosRemotos.Contratos.GetProperty(servicio).GetProperty("resources");
            if(!resources.TryGetProperty(resource,out var contract))return Results.NotFound();
            var column=contract.GetProperty("columns").EnumerateArray().Select(c=>c.GetString()!).FirstOrDefault(c=>c.Equals(campo,StringComparison.OrdinalIgnoreCase));
            if(column==null)return Results.BadRequest();
            using var connection=new SqlConnection(connectionString);await connection.OpenAsync();using var command=connection.CreateCommand();
            command.CommandText="SELECT CASE WHEN EXISTS(SELECT 1 FROM "+contract.GetProperty("table").GetString()+" WHERE ["+column+"]=@v) THEN 1 ELSE 0 END";
            command.Parameters.AddWithValue("@v",valor);return Results.Ok(Convert.ToInt32(await command.ExecuteScalarAsync())!=0);
        });
        app.MapGet("/interno/datos/{resource}",async(string resource,HttpContext context)=>{
            var resources=DatosRemotos.Contratos.GetProperty(servicio).GetProperty("resources");
            if(!resources.TryGetProperty(resource,out var contract) || (contract.TryGetProperty("readable",out var readable) && !readable.GetBoolean())) return Results.NotFound();
            var columns=contract.GetProperty("columns").EnumerateArray().Select(c=>c.GetString()!).ToArray();
            using var connection=new SqlConnection(connectionString);await connection.OpenAsync();
            using var command=connection.CreateCommand();
            var predicates=new List<string>();int index=0;
            foreach(var filter in context.Request.Query){
                var column=columns.FirstOrDefault(c=>c.Equals(filter.Key,StringComparison.OrdinalIgnoreCase));
                if(column==null) return Results.BadRequest(new {error="Filtro desconocido"});
                var parameter="@p"+index++;predicates.Add($"[{column}]={parameter}");
                command.Parameters.AddWithValue(parameter,filter.Value.ToString());
            }
            command.CommandText="SELECT "+string.Join(",",columns.Select(c=>$"[{c}]"))+" FROM "+contract.GetProperty("table").GetString();
            if(predicates.Count>0)command.CommandText+=" WHERE "+string.Join(" AND ",predicates);
            command.CommandText+=" ORDER BY ["+contract.GetProperty("key").GetString()+"]";
            using var reader=await command.ExecuteReaderAsync();var records=new List<Dictionary<string,object?>>();
            while(await reader.ReadAsync()){
                var row=new Dictionary<string,object?>();for(int i=0;i<columns.Length;i++)row[columns[i]]=reader.IsDBNull(i)?null:reader.GetValue(i);
                records.Add(row);
            }
            return Results.Ok(records);
        });
    }
}
