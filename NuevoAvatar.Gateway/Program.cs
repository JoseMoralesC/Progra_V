using NuevoAvatar.Gateway;
using NuevoAvatar.Integracion;

ConfiguracionEntorno.Cargar();
var app = GatewayApplication.Create(args, builder =>
    builder.WebHost.UseUrls(ConfiguracionEntorno.Escucha(builder.Configuration, "GATEWAY")));
await app.RunAsync();
