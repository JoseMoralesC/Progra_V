using System.Net;
using NuevoAvatar.Gateway.Seguridad;
using Yarp.ReverseProxy.Forwarder;

namespace NuevoAvatar.Gateway;

public static class GatewayApplication
{
    public static WebApplication Create(string[] args, Action<WebApplicationBuilder>? configurar = null)
    {
        var builder = WebApplication.CreateBuilder(args);
        configurar?.Invoke(builder);
        var auth = Destino(builder.Configuration, "Autenticacion", 5158);
        builder.Services.AddSeguridadGateway(new Uri(auth));
        builder.Services.AddHttpForwarder();
        builder.Services.AddSingleton(new HttpMessageInvoker(new SocketsHttpHandler
        {
            AllowAutoRedirect = false, UseCookies = false, UseProxy = false,
            AutomaticDecompression = DecompressionMethods.None,
            ConnectTimeout = TimeSpan.FromSeconds(5)
        }));
        var app = builder.Build();
        app.UseRouting();
        app.UseWhen(c => c.Request.Path.StartsWithSegments("/gateway"), branch => branch.UseSeguridadGateway());
        app.MapGet("/health", () => Results.Ok(new { servicio = "Gateway", estado = "disponible" }));
        foreach (var ruta in CatalogoRutas.Todas)
        {
            var destino = Destino(builder.Configuration, ruta.Servicio, ruta.Puerto);
            var transformer = new TransformacionRuta(ruta.Externa, ruta.Interna);
            var config = new ForwarderRequestConfig { ActivityTimeout = TimeSpan.FromSeconds(30) };
            app.MapMethods(ruta.Patron, ruta.Verbos, async (HttpContext context, IHttpForwarder proxy,
                HttpMessageInvoker cliente) =>
            {
                context.Response.Headers.CacheControl = "no-store";
                var error = await proxy.SendAsync(context, destino, cliente, config, transformer);
                if (error != ForwarderError.None && !context.Response.HasStarted && !context.RequestAborted.IsCancellationRequested)
                {
                    context.Response.Clear();
                    context.Response.StatusCode = 502;
                    context.Response.Headers.CacheControl = "no-store";
                    await context.Response.WriteAsJsonAsync(new { status = 502, error = "Servicio destino no disponible" });
                }
            }).WithMetadata(new AccesoGateway(ruta.Roles));
        }
        return app;
    }

    private static string Destino(IConfiguration config, string servicio, int puerto)
    {
        var valor = config[$"Destinos:{servicio}"] ?? $"http://localhost:{puerto}";
        if (!Uri.TryCreate(valor, UriKind.Absolute, out var uri) ||
            (uri.Scheme != "http" && uri.Scheme != "https") || !string.IsNullOrEmpty(uri.UserInfo) ||
            uri.AbsolutePath != "/" || !string.IsNullOrEmpty(uri.Query) || !string.IsNullOrEmpty(uri.Fragment))
            throw new InvalidOperationException($"Destino invalido para {servicio}; indicar un origen HTTP(S).");
        return valor.TrimEnd('/');
    }
}
