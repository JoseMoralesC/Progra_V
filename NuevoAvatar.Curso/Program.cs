using NuevoAvatar.Curso;
using NuevoAvatar.Curso.Repository;
using NuevoAvatar.Curso.Services;
using Microsoft.AspNetCore.Diagnostics;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddSingleton<IDbConnectionFactory, DbConnectionFactory>();
builder.Services.AddScoped<ICursoRepository, CursoRepository>();
builder.Services.AddScoped<ICursoService, CursoService>();
builder.Services.AddScoped<CursoValidator>();

var app = builder.Build();

app.UseExceptionHandler(handler => handler.Run(async context =>
{
    var exception = context.Features.Get<IExceptionHandlerFeature>()?.Error;
    context.RequestServices.GetRequiredService<ILogger<Program>>().LogError(exception, "Error procesando la solicitud HTTP.");
    context.Response.StatusCode = StatusCodes.Status500InternalServerError;
    await context.Response.WriteAsJsonAsync(new { error = "Ocurrió un error al procesar la solicitud." });
}));

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();
app.MapCursoEndpoints();

app.Run();
