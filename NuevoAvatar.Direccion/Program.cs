using NuevoAvatar.Direccion.Nuevo;
using NuevoAvatar.Direccion.Nuevo.Repository;
using NuevoAvatar.Direccion.Nuevo.Services;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddSingleton<IDbConnectionFactory, DbConnectionFactory>();

builder.Services.AddScoped<IDireccionRepository, DireccionRepository>();

builder.Services.AddScoped<IDireccionService, DireccionService>();

builder.Services.AddHttpClient<ISeguridadService, SeguridadService>(client =>
{
    var baseUrl = builder.Configuration["Seguridad:BaseUrl"]
        ?? throw new InvalidOperationException(
            "Falta la configuración 'Seguridad:BaseUrl'.");

    client.BaseAddress = new Uri(baseUrl);
});

var app = builder.Build();

app.UseMiddleware<GrupoAuthorizationMiddleware>();

app.MapDireccionEndpoints();

app.Run();