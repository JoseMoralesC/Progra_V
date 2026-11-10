package cr.ac.cuc.nuevoavatar.persona2.seguridad;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class SeguridadClient {

    private static final Logger LOG = LoggerFactory.getLogger(SeguridadClient.class);
    private final RestClient cliente;
    private final RestClient bitacora;

    public SeguridadClient(@Value("${seguridad.service.url}") String baseUrl,
            @Value("${bitacora.service.url}") String bitacoraUrl) {
        this.cliente = RestClient.builder().baseUrl(baseUrl).build();
        this.bitacora = RestClient.builder().baseUrl(bitacoraUrl).build();
    }

    public ResultadoValidacionToken validar(String bearerToken) {
        try {
            Boolean respuesta = cliente.get()
                .uri("/validate")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .retrieve()
                .body(Boolean.class);
            return Boolean.TRUE.equals(respuesta)
                ? ResultadoValidacionToken.VALIDO
                : ResultadoValidacionToken.INVALIDO;
        } catch (HttpClientErrorException ex) {
            // El contrato de /validate responde 401 cuando el token no es válido.
            if (ex.getStatusCode().value() == HttpStatus.UNAUTHORIZED.value()) {
                return ResultadoValidacionToken.INVALIDO;
            }
            LOG.warn("Autenticación respondió con un error al validar el token");
            return ResultadoValidacionToken.NO_DISPONIBLE;
        } catch (RestClientException ex) {
            LOG.warn("No fue posible validar el token con Autenticación");
            return ResultadoValidacionToken.NO_DISPONIBLE;
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
            LOG.error("No fue posible registrar la bitácora: {}", ex.getMessage());
        }
    }

    /**
     * ADM24 requiere que una mutación no se confirme al cliente sin que Bitácora
     * haya aceptado la creación del evento (201 Created).
     */
    public void registrarBitacoraConfirmada(
            String bearerToken,
            String usuario,
            String descripcion) {
        try {
            var respuesta = bitacora.post()
                .uri("/bitacora")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .body(Map.of("usuario", usuario, "descripcion", descripcion))
                .retrieve()
                .toBodilessEntity();
            if (respuesta.getStatusCode().value() != HttpStatus.CREATED.value()) {
                throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Bitácora no confirmó la persistencia del evento");
            }
        } catch (RestClientException ex) {
            LOG.error("Bitácora no confirmó el evento ADM24");
            throw new org.springframework.web.server.ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "No fue posible confirmar el registro en Bitácora");
        }
    }

    public enum ResultadoValidacionToken {
        VALIDO,
        INVALIDO,
        NO_DISPONIBLE
    }
}

