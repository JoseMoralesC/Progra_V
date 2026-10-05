package cr.ac.cuc.nuevoavatar.persona4.expediente;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

@Component
public class DireccionClient {
    private final RestClient cliente;

    @Autowired
    public DireccionClient(RestClient.Builder builder, @Value("${direccion.service.url}") String baseUrl) {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(5000);
        cliente = builder.baseUrl(baseUrl).requestFactory(factory).build();
    }

    DireccionClient(RestClient cliente) {
        this.cliente = cliente;
    }

    public void validar(String token, Integer provinciaId, Integer cantonId, Integer distritoId) {
        try {
            List<Provincia> provincias = cliente.get().uri("/provincias")
                    .header(HttpHeaders.AUTHORIZATION, token).retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            if (provincias == null) throw respuestaIncompleta();
            if (provincias.stream().noneMatch(p -> provinciaId.equals(p.provinciaId()))) {
                throw direccionInvalida();
            }
            List<Canton> cantones = cliente.get().uri("/cantones/{provincia}", provinciaId)
                    .header(HttpHeaders.AUTHORIZATION, token).retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            if (cantones == null) throw respuestaIncompleta();
            if (cantones.stream().noneMatch(c -> cantonId.equals(c.cantonId()) && provinciaId.equals(c.provinciaId()))) {
                throw direccionInvalida();
            }
            List<Distrito> distritos = cliente.get().uri("/distritos/{provincia}/{canton}", provinciaId, cantonId)
                    .header(HttpHeaders.AUTHORIZATION, token).retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            if (distritos == null) throw respuestaIncompleta();
            if (distritos.stream().noneMatch(d -> distritoId.equals(d.distritoId()) && cantonId.equals(d.cantonId()))) {
                throw direccionInvalida();
            }
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "No fue posible validar la direccion con MAT4", ex);
        }
    }

    private ResponseStatusException direccionInvalida() {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "La provincia, el canton y el distrito no corresponden a una direccion valida");
    }

    private ResponseStatusException respuestaIncompleta() {
        return new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "El servicio de direcciones no devolvio los datos requeridos");
    }

    public record Provincia(Integer provinciaId, String nombre) {}
    public record Canton(Integer cantonId, Integer provinciaId, String nombre) {}
    public record Distrito(Integer distritoId, Integer cantonId, String nombre) {}
}
