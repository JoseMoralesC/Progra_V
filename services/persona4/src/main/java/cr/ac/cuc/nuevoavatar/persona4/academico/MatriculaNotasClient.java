package cr.ac.cuc.nuevoavatar.persona4.academico;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class MatriculaNotasClient {
    private final RestClient cliente;

    @Autowired
    public MatriculaNotasClient(RestClient.Builder builder, @Value("${matricula.service.url}") String baseUrl) {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(5000);
        cliente = builder.baseUrl(baseUrl).requestFactory(factory).build();
    }

    MatriculaNotasClient(RestClient cliente) {
        this.cliente = cliente;
    }

    public List<Matricula> obtenerMatriculas(String token, Integer cursoId, Integer grupoId) {
        try {
            List<Matricula> respuesta = cliente.get().uri(uri -> uri.path("/matricula")
                    .queryParam("cursoId", cursoId).queryParam("grupoId", grupoId).build())
                    .header(HttpHeaders.AUTHORIZATION, token).retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            if (respuesta == null) throw respuestaIncompleta();
            return respuesta;
        } catch (RestClientException ex) {
            throw noDisponible(ex);
        }
    }

    public List<Nota> obtenerNotas(String token, String identificacion, Integer cursoId, Integer grupoId) {
        try {
            List<Nota> respuesta = cliente.get().uri(uri -> uri.path("/obtenernotas")
                    .queryParam("identificacionEstudiante", "{identificacion}")
                    .queryParam("cursoId", cursoId).queryParam("grupoId", grupoId).build(identificacion))
                    .header(HttpHeaders.AUTHORIZATION, token).retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            if (respuesta == null) throw respuestaIncompleta();
            return respuesta;
        } catch (RestClientException ex) {
            throw noDisponible(ex);
        }
    }

    public List<Rubro> obtenerDesglose(String token, Integer grupoId) {
        try {
            List<Rubro> respuesta = cliente.get().uri(uri -> uri.path("/obtenerdesglose")
                    .queryParam("grupoId", grupoId).build())
                    .header(HttpHeaders.AUTHORIZATION, token).retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            if (respuesta == null) throw respuestaIncompleta();
            return respuesta;
        } catch (RestClientException ex) {
            throw noDisponible(ex);
        }
    }

    private ResponseStatusException respuestaIncompleta() {
        return new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "El servicio de matricula y notas no devolvio una lista valida");
    }

    private ResponseStatusException noDisponible(RestClientException causa) {
        return new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "No fue posible consultar el servicio de matricula y notas", causa);
    }

    public record Matricula(Integer id, String identificacionEstudiante, Integer cursoId,
            Integer grupoId, Integer periodoId, String estado, LocalDateTime fechaMatricula) {}

    public record Nota(Integer id, Integer matriculaId, Integer rubroId, String rubro,
            BigDecimal porcentaje, BigDecimal nota) {}

    public record Rubro(Integer id, Integer grupoId, String nombre, BigDecimal porcentaje) {}
}
