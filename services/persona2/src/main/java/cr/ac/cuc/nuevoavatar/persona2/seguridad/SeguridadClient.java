package cr.ac.cuc.nuevoavatar.persona2.seguridad;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class SeguridadClient {

    private static final Logger LOG = LoggerFactory.getLogger(SeguridadClient.class);
    private final RestClient cliente;

    public SeguridadClient(@Value("${seguridad.service.url}") String baseUrl) {
        this.cliente = RestClient.builder().baseUrl(baseUrl).build();
    }

    public boolean validar(String bearerToken) {
        try {
            Boolean respuesta = cliente.get()
                .uri("/validate")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .retrieve()
                .body(Boolean.class);
            return Boolean.TRUE.equals(respuesta);
        } catch (RestClientException ex) {
            LOG.warn("No fue posible validar el token con Seguridad: {}", ex.getMessage());
            return false;
        }
    }

    public void registrarBitacora(
            String bearerToken,
            String usuario,
            String descripcion) {
        try {
            cliente.post()
                .uri("/bitacora")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .body(Map.of(
                    "usuario", usuario,
                    "descripcion", descripcion
                ))
                .retrieve()
                .toBodilessEntity();
        } catch (RestClientException ex) {
            LOG.error("No fue posible registrar la bitácora: {}", ex.getMessage());
        }
    }
}
