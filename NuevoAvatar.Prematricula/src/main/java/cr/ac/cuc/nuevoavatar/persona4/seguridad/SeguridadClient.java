package cr.ac.cuc.nuevoavatar.persona4.seguridad;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

@Component
public class SeguridadClient {
    private final RestClient cliente;
    private final RestClient bitacora;
    private final RestClient parametros;

    @Autowired
    public SeguridadClient(RestClient.Builder builder,
            @Value("${seguridad.service.url}") String baseUrl,
            @Value("${bitacora.service.url}") String bitacoraUrl,
            @Value("${parametro.service.url:http://localhost:5161}") String parametroUrl,
            @Value("${seguridad.connect-timeout-ms}") int connectTimeout,
            @Value("${seguridad.read-timeout-ms}") int readTimeout) {
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);
        this.cliente = builder.baseUrl(java.util.Objects.requireNonNull(baseUrl)).requestFactory(requestFactory).build();
        this.bitacora = builder.clone().baseUrl(java.util.Objects.requireNonNull(bitacoraUrl)).requestFactory(requestFactory).build();
        this.parametros = builder.clone().baseUrl(java.util.Objects.requireNonNull(parametroUrl)).requestFactory(requestFactory).build();
    }

    SeguridadClient(RestClient cliente) {
        this.cliente = cliente;
        this.bitacora = cliente;
        this.parametros = cliente;
    }

    public boolean validar(String autorizacion) {
        try {
            return Boolean.TRUE.equals(cliente.get().uri("/validate")
                    .header(HttpHeaders.AUTHORIZATION, autorizacion)
                    .retrieve().body(Boolean.class));
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 401) {
                return false;
            }
            throw noDisponible("validar el token", ex);
        } catch (RestClientException ex) {
            throw noDisponible("validar el token", ex);
        }
    }

    public String obtenerParametro(String autorizacion, String idParametro) {
        try {
            Parametro parametro = parametros.get().uri("/parametro/{id}", idParametro)
                    .header(HttpHeaders.AUTHORIZATION, autorizacion)
                    .retrieve().body(Parametro.class);
            if (parametro == null || parametro.valor() == null || parametro.valor().isBlank()) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "El parametro requerido no tiene un valor configurado");
            }
            return parametro.valor();
        } catch (RestClientException ex) {
            throw noDisponible("consultar el parametro requerido", ex);
        }
    }

    public void registrarBitacora(String autorizacion, String usuario, String descripcion) {
        try {
            bitacora.post().uri("/bitacora")
                    .header(HttpHeaders.AUTHORIZATION, autorizacion)
                    .contentType(java.util.Objects.requireNonNull(MediaType.APPLICATION_JSON))
                    .body(java.util.Objects.requireNonNull(Map.of("usuario", usuario, "descripcion", descripcion)))
                    .retrieve().toBodilessEntity();
        } catch (RestClientException ex) {
            throw noDisponible("registrar la bitacora", ex);
        }
    }

    private ResponseStatusException noDisponible(String operacion, RestClientException causa) {
        return new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "No fue posible " + operacion + " con el servicio de seguridad", causa);
    }

    public record Parametro(String idParametro, String valor) {
    }
}

