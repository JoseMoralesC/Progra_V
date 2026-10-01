using NuevoAvatar.Periodo.Nuevo;
using NuevoAvatar.Periodo.Nuevo.Repository;
using NuevoAvatar.Periodo.Nuevo.Services;

var builder = WebApplication.CreateBuilder(args);

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