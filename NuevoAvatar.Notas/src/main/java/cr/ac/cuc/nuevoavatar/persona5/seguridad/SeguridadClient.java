package cr.ac.cuc.nuevoavatar.persona5.seguridad;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@Component
public class SeguridadClient {

    private static final Logger LOG = LoggerFactory.getLogger(SeguridadClient.class);
    private final RestClient cliente;
    private final RestClient bitacora;

    public SeguridadClient(@Value("${seguridad.service.url}") String baseUrl,
            @Value("${bitacora.service.url}") String bitacoraUrl) {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(8000);
        this.cliente = RestClient.builder().baseUrl(baseUrl).requestFactory(factory).build();
        this.bitacora = RestClient.builder().baseUrl(bitacoraUrl).requestFactory(factory).build();
    }

    public boolean validar(String bearerToken) {
        try {
            Boolean respuesta = cliente.get()
                .uri("/validate")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .retrieve()
                .body(Boolean.class);
            return Boolean.TRUE.equals(respuesta);
        } catch (HttpClientErrorException.Unauthorized ex) {
            return false;
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Autenticacion no disponible");
        }
    }

    public SesionActual obtenerSesion(String bearerToken) {
        try {
            SesionActual sesion = cliente.get().uri("/sesion")
                .header(HttpHeaders.AUTHORIZATION, bearerToken).retrieve().body(SesionActual.class);
            if (sesion == null || sesion.usuarioID() == null || sesion.rol() == null)
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Identidad no disponible");
            return sesion;
        } catch (HttpClientErrorException.Unauthorized ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autorizado");
        } catch (HttpClientErrorException.Forbidden ex) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cuenta sin acceso autorizado");
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Autenticacion no disponible");
        }
    }

    public void registrarBitacora(
            String bearerToken,
            String usuario,
            String descripcion) {
        try {
            bitacora.post()
                .uri("/bitacora")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .body(Map.of(
                    "usuario", usuario,
                    "descripcion", descripcion
                ))
                .retrieve()
                .toBodilessEntity();
        } catch (RestClientException ex) {
            LOG.error("No fue posible registrar la bitacora: {}", ex.getMessage());
        }
    }
}

