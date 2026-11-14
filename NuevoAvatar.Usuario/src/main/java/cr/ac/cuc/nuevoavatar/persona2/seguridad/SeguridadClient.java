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
    @org.springframework.beans.factory.annotation.Autowired
    private cr.ac.cuc.nuevoavatar.integracion.AuditoriaFiable auditoria;


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
        auditoria.registrar(usuario, descripcion);
    }

    /**
     * La mutación y el evento pendiente se persisten en la misma transacción local.
     * La entrega al receptor se confirma y reintenta por ID sin duplicar el movimiento.
     */
    public void registrarBitacoraConfirmada(
            String bearerToken,
            String usuario,
            String descripcion) {
        auditoria.registrar(usuario, descripcion + ". Resultado: Exitoso");
    }

    public enum ResultadoValidacionToken {
        VALIDO,
        INVALIDO,
        NO_DISPONIBLE
    }
}

