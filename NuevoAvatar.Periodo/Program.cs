using DotNetEnv;
using NuevoAvatar.Periodo.Nuevo;
using NuevoAvatar.Periodo.Nuevo.Repository;
using NuevoAvatar.Periodo.Nuevo.Services;

Env.TraversePath().Load();

var builder = WebApplication.CreateBuilder(args);

var dbHost = builder.Configuration["DB_HOST"];
var dbPort = builder.Configuration["DB_PORT"] ?? "1433";
var dbName = builder.Configuration["DB_NAME"];
var dbUsername = builder.Configuration["DB_USERNAME"];
var dbPassword = builder.Configuration["DB_PASSWORD"];

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

builder.Services.AddScoped<IPeriodoRepository, PeriodoRepository>();

builder.Services.AddScoped<IPeriodoValidator, PeriodoValidator>();

builder.Services.AddScoped<IPeriodoService, PeriodoService>();

builder.Services.AddHttpClient<ISeguridadService, SeguridadService>(client =>
{
    var baseUrl = builder.Configuration["Seguridad:BaseUrl"]
        ?? throw new InvalidOperationException(
            "Falta la configuración 'Seguridad:BaseUrl'.");

    client.BaseAddress = new Uri(baseUrl);
});

var app = builder.Build();

app.UseMiddleware<GrupoAuthorizationMiddleware>();

app.MapPeriodoEndpoints();

app.Run();