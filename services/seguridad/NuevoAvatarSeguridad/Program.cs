using DotNetEnv;
using Microsoft.EntityFrameworkCore;
using Microsoft.OpenApi.Models;
using NuevoAvatar.Seguridad.Abstract;
using NuevoAvatar.Seguridad.DataAccess;
using NuevoAvatar.Seguridad.Models;

Env.TraversePath().Load();

var builder = WebApplication.CreateBuilder(args);

builder.Logging.ClearProviders();
builder.Logging.AddConsole();
builder.Logging.AddDebug();

var dbHost = builder.Configuration["DB_HOST"];
var dbPort = builder.Configuration["DB_PORT"] ?? "1433";
var dbName = builder.Configuration["DB_NAME"];
var dbUsername = builder.Configuration["DB_USERNAME"];
var dbPassword = builder.Configuration["DB_PASSWORD"];

if (!string.IsNullOrWhiteSpace(dbHost) &&
    !string.IsNullOrWhiteSpace(dbName) &&
    !string.IsNullOrWhiteSpace(dbUsername) &&
    !string.IsNullOrWhiteSpace(dbPassword))
{
    builder.Configuration["ConnectionStrings:SeguridadDb"] =
        $"Server={dbHost},{dbPort};" +
        $"Database={dbName};" +
        $"User Id={dbUsername};" +
        $"Password={dbPassword};" +
        "Encrypt=False;" +
        "TrustServerCertificate=True;";
}

if (!string.IsNullOrWhiteSpace(builder.Configuration["JWT_KEY"]))
{
    builder.Configuration["Jwt:Key"] = builder.Configuration["JWT_KEY"];
}

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen(c =>
{
    c.AddSecurityDefinition("Bearer", new OpenApiSecurityScheme
    {
        Name = "Authorization",
        Type = SecuritySchemeType.Http,
        Scheme = "bearer",
        BearerFormat = "JWT",
        In = ParameterLocation.Header,
        Description = "Pega solo el access_token"
    });
    c.AddSecurityRequirement(new OpenApiSecurityRequirement
    {
        {
            new OpenApiSecurityScheme
            {
                Reference = new OpenApiReference { Type = ReferenceType.SecurityScheme, Id = "Bearer" }
            },
            Array.Empty<string>()
        }
    });
});

builder.Services.AddDbContext<SeguridadDbContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("SeguridadDb")));

builder.Services.AddScoped<IRolService, RolService>();
builder.Services.AddScoped<IParametroService, ParametroService>();
builder.Services.AddScoped<IModuloService, ModuloService>();
builder.Services.AddScoped<IUsuarioService, UsuarioService>();
builder.Services.AddScoped<ISesionTokenService, SesionTokenService>();
builder.Services.AddScoped<IBitacoraService, BitacoraService>();
builder.Services.AddSingleton(new JwtSettings
{
    Key = builder.Configuration["Jwt:Key"]
          ?? throw new InvalidOperationException("Falta Jwt:Key en secrets.json")
});
builder.Services.AddScoped<IAuthService, AuthService>();

var app = builder.Build();

// Registra en bitácora cualquier error técnico no controlado
app.Use(async (context, next) =>
{
    try
    {
        await next();
    }
    catch (Exception ex)
    {
        try
        {
            var bitacora = context.RequestServices.GetRequiredService<IBitacoraService>();
            await bitacora.Crear(new Bitacora
            {
                Usuario = context.Items["usuario"]?.ToString() ?? "sistema",
                Descripcion = $"Error técnico en {context.Request.Path}: {ex.Message}"
            });
        }
        catch { }

        if (!context.Response.HasStarted)
        {
            context.Response.StatusCode = 500;
            await context.Response.WriteAsync("Error interno del servidor");
        }
    }
});

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();
app.UseAuthorization();
app.MapControllers();
app.Run();
