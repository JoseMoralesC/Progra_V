package cr.ac.cuc.nuevoavatar.persona5.seguridad;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class JwtUsuario {

    private static final Pattern SUBJECT = Pattern.compile("\\\"sub\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"");

    private JwtUsuario() {
    }

    static String obtener(String bearerToken) {
        try {
            String token = bearerToken.substring(7).trim();
            String[] partes = token.split("\\.");
            if (partes.length != 3) {
                return "desconocido";
            }
            String json = new String(
                Base64.getUrlDecoder().decode(partes[1]),
                StandardCharsets.UTF_8
            );
            Matcher matcher = SUBJECT.matcher(json);
            return matcher.find() ? matcher.group(1) : "desconocido";
        } catch (RuntimeException ex) {
            return "desconocido";
        }
    }
}
