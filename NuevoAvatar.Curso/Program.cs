using DotNetEnv;
using Microsoft.AspNetCore.Diagnostics;
using NuevoAvatar.Curso;
using NuevoAvatar.Curso.Repository;
using NuevoAvatar.Curso.Services;

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

builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.AddHttpClient<ISeguridadService, SeguridadService>(client =>
{
    var baseUrl = builder.Configuration["Seguridad:BaseUrl"]
        ?? throw new InvalidOperationException(
            "Falta la configuración 'Seguridad:BaseUrl'.");

    client.BaseAddress = new Uri(baseUrl);
});

builder.Services.AddSingleton<IDbConnectionFactory, DbConnectionFactory>();
builder.Services.AddScoped<ICursoRepository, CursoRepository>();
builder.Services.AddScoped<ICursoService, CursoService>();
builder.Services.AddScoped<CursoValidator>();

var app = builder.Build();

app.UseExceptionHandler(handler => handler.Run(async context =>
{
    var exception =
        context.Features.Get<IExceptionHandlerFeature>()?.Error;

    context.RequestServices
        .GetRequiredService<ILogger<Program>>()
        .LogError(
            exception,
            "Error procesando la solicitud HTTP.");

    context.Response.StatusCode =
        StatusCodes.Status500InternalServerError;

    await context.Response.WriteAsJsonAsync(
        new
        {
            error =
                "Ocurrió un error al procesar la solicitud."
        });
}));

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.MapCursoEndpoints();

app.Run();