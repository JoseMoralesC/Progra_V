using Yarp.ReverseProxy.Forwarder;

namespace NuevoAvatar.Gateway;

public sealed class TransformacionRuta(string externa, string interna) : HttpTransformer
{
    public override async ValueTask TransformRequestAsync(HttpContext context, HttpRequestMessage proxy,
        string destino, CancellationToken cancellationToken)
    {
        await base.TransformRequestAsync(context, proxy, destino, cancellationToken);
        var sufijo = context.Request.Path.Value![externa.Length..];
        proxy.RequestUri = RequestUtilities.MakeDestinationAddress(destino, interna + sufijo, context.Request.QueryString);
        proxy.Headers.Host = null;
        // Credenciales de login solo viajan al endpoint de login. No aceptar identidad reenviada.
        if (!externa.Equals("/gateway/auth/login", StringComparison.OrdinalIgnoreCase))
        {
            proxy.Headers.Remove("usuario");
            proxy.Headers.Remove("contrasena");
        }
        foreach (var header in proxy.Headers.Select(h => h.Key).Where(h =>
            h.StartsWith("X-Forwarded-", StringComparison.OrdinalIgnoreCase) ||
            h.Equals("Forwarded", StringComparison.OrdinalIgnoreCase)).ToArray()) proxy.Headers.Remove(header);
    }

    public override async ValueTask<bool> TransformResponseAsync(HttpContext context, HttpResponseMessage? response,
        CancellationToken cancellationToken)
    {
        var copiar = await base.TransformResponseAsync(context, response, cancellationToken);
        if (response?.Headers.Location is { } location)
        {
            var path = location.IsAbsoluteUri ? location.AbsolutePath : location.OriginalString;
            var mismoDestino = !location.IsAbsoluteUri || location.GetLeftPart(UriPartial.Authority) ==
                response.RequestMessage?.RequestUri?.GetLeftPart(UriPartial.Authority);
            if (mismoDestino && (path == interna || path.StartsWith(interna + "/", StringComparison.OrdinalIgnoreCase)))
                context.Response.Headers.Location = externa + path[interna.Length..] +
                    (location.IsAbsoluteUri ? location.Query + location.Fragment : "");
        }
        return copiar;
    }
}
