using Microsoft.AspNetCore.Diagnostics;
using NuevoAvatar.Curso;
using NuevoAvatar.Curso.Repository;
using NuevoAvatar.Curso.Services;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.AddHttpClient<ISeguridadService, SeguridadService>(client =>
{
    client.BaseAddress = new Uri(
        builder.Configuration["Servicios:Seguridad"]
        ?? throw new InvalidOperationException(
            "No se configuró Servicios:Seguridad."));
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