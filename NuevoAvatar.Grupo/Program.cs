using NuevoAvatar.Integracion;
using DotNetEnv;
using NuevoAvatar.Grupo.Nuevo;
using NuevoAvatar.Grupo.Nuevo.Repository;
using NuevoAvatar.Grupo.Nuevo.Services;

Env.NoClobber().TraversePath().Load();

var builder = WebApplication.CreateBuilder(args);

var dbHost = builder.Configuration["DB_HOST"];
var dbPort = builder.Configuration["DB_PORT"] ?? "1433";
var dbName = builder.Configuration["SERVICE_DB_NAME"] ?? "PrograV_Grupo";
var dbUsername = builder.Configuration["SERVICE_DB_USER_GRUPO"];
var dbPassword = builder.Configuration["SERVICE_DB_PASSWORD_GRUPO"];

if (string.IsNullOrWhiteSpace(dbHost) ||
    string.IsNullOrWhiteSpace(dbName) ||
    string.IsNullOrWhiteSpace(dbUsername) ||
    string.IsNullOrWhiteSpace(dbPassword))
{
    throw new InvalidOperationException(
        "Faltan las variables DB_HOST, DB_NAME, DB_USERNAME o DB_PASSWORD en el archivo .env.");
}

builder.Configuration["ConnectionStrings:DefaultConnection"] =
    $"Server={dbHost},{dbPort};" +
    $"Database={dbName};" +
    $"User Id={dbUsername};" +
    $"Password={dbPassword};" +
    "TrustServerCertificate=True;";

builder.Services.AddSingleton<IDbConnectionFactory, DbConnectionFactory>();

builder.Services.AddScoped<IGrupoRepository, GrupoRepository>();

builder.Services.AddScoped<IGrupoService, GrupoService>();

builder.Services.AddHttpClient<ISeguridadService, SeguridadService>(client =>
{
    var baseUrl = builder.Configuration["Seguridad:BaseUrl"]
        ?? throw new InvalidOperationException(
            "Falta la configuración 'Seguridad:BaseUrl'.");

    client.BaseAddress = new Uri(baseUrl);
});

builder.Services.AddIntegracion();
var app = builder.Build();
app.MapIntegracion("Grupo", builder.Configuration.GetConnectionString("DefaultConnection")!);

app.UseMiddleware<GrupoAuthorizationMiddleware>();

app.MapGrupoEndpoints();

app.Run();