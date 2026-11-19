package cr.ac.cuc.nuevoavatar.integracion;

import java.net.URI;

public final class UrlsEntorno {
    private UrlsEntorno() {}

    public static URI origen(String valor, String nombre) {
        try {
            URI uri = URI.create(valor);
            if (!("http".equals(uri.getScheme()) || "https".equals(uri.getScheme())) ||
                uri.getHost() == null || uri.getRawUserInfo() != null ||
                !(uri.getRawPath().isEmpty() || "/".equals(uri.getRawPath())) ||
                uri.getRawQuery() != null || uri.getRawFragment() != null ||
                uri.getPort() == 0 || uri.getPort() > 65535) throw new IllegalArgumentException();
            return URI.create(uri.getScheme() + "://" + uri.getRawAuthority());
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new IllegalStateException("Configura " + nombre + " en el .env raiz con un origen HTTP(S) valido, sin rutas, parametros ni credenciales.");
        }
    }
}
