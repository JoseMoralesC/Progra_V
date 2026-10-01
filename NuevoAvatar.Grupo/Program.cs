using NuevoAvatar.Grupo.Nuevo;
using NuevoAvatar.Grupo.Nuevo.Repository;
using NuevoAvatar.Grupo.Nuevo.Services;

var builder = WebApplication.CreateBuilder(args);

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

var app = builder.Build();

app.UseMiddleware<GrupoAuthorizationMiddleware>();

app.MapGrupoEndpoints();

app.Run();