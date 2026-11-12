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
        builder.Services.AddPermisosGateway(new Uri(Destino(builder.Configuration, "Modulo", 5162)));
        builder.Services.AddHttpClient("dashboard-bitacora", client => {
            client.BaseAddress = new Uri(Destino(builder.Configuration, "Bitacora", 5159));
            client.Timeout = TimeSpan.FromSeconds(8);
        }).ConfigurePrimaryHttpMessageHandler(() => new HttpClientHandler { AllowAutoRedirect = false });
        foreach (var (servicio, puerto) in new[] { ("Notificacion", 8095), ("Notas", 8092), ("Factura", 8093) })
            builder.Services.AddHttpClient("dashboard-" + servicio, client => {
                client.BaseAddress = new Uri(Destino(builder.Configuration, servicio, puerto));
                client.Timeout = TimeSpan.FromSeconds(8);
            }).ConfigurePrimaryHttpMessageHandler(() => new HttpClientHandler { AllowAutoRedirect = false });
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
        app.MapPost("/gateway/auditoria/interfaz", AuditoriaInterfaz.Registrar)
            .WithMetadata(new AccesoGateway(["ADMINISTRADOR", "PROFESOR", "ESTUDIANTE"]));
        app.MapGet("/gateway/menu", async (HttpContext context, IHttpClientFactory clientes) => {
            context.Response.Headers.CacheControl = "no-store";
            if (context.Request.Query["vista"] == "dashboard")
            {
                var resultado = await AuditoriaDashboard.Registrar(context, clientes);
                if (resultado is not null) return resultado;
            }
            return Results.Ok(context.Items[PermisosGateway.ClaveMenu]);
        }).WithMetadata(new AccesoGateway(["ADMINISTRADOR", "PROFESOR", "ESTUDIANTE"], ConsultarMenu: true));
        foreach (var ruta in CatalogoRutas.Todas)
        {
            var destino = Destino(builder.Configuration, ruta.Servicio, ruta.Puerto);
            var transformer = new TransformacionRuta(ruta.Externa, ruta.Interna);
            var config = new ForwarderRequestConfig { ActivityTimeout = TimeSpan.FromSeconds(30) };
            var rutaResumen = ruta.Servicio switch {
                "Bitacora" => "/gateway/admin/bitacora",
                "Notificacion" => "/gateway/fin/notificar/historial",
                _ => null
            };
            var acceso = new AccesoGateway(ruta.Roles, ruta.Modulo, RutaResumen: rutaResumen);
            app.MapMethods(ruta.Patron, ruta.Verbos, async (HttpContext context, IHttpForwarder proxy,
                HttpMessageInvoker cliente, IHttpClientFactory clientes) =>
            {
                context.Response.Headers.CacheControl = "no-store";
                if (acceso.EsResumen(context))
                {
                    var resumen = await ResumenDashboard.Obtener(context, clientes, ruta.Servicio);
                    await resumen.ExecuteAsync(context);
                    return;
                }
                if (acceso.EsIndicadorFacturas(context))
                {
                    var indicador = await FacturaDashboard.Obtener(context, clientes);
                    await indicador.ExecuteAsync(context);
                    return;
                }
                var error = await proxy.SendAsync(context, destino, cliente, config, transformer);
                if (error != ForwarderError.None && !context.Response.HasStarted && !context.RequestAborted.IsCancellationRequested)
                {
                    context.Response.Clear();
                    context.Response.StatusCode = 502;
                    context.Response.Headers.CacheControl = "no-store";
                    await context.Response.WriteAsJsonAsync(new { status = 502, error = "Servicio destino no disponible" });
                }
            }).WithMetadata(acceso);
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
