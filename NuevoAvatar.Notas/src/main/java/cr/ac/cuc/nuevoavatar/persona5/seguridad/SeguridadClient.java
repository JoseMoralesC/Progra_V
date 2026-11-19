package cr.ac.cuc.nuevoavatar.persona5.seguridad;

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
    @org.springframework.beans.factory.annotation.Autowired
    private cr.ac.cuc.nuevoavatar.integracion.AuditoriaFiable auditoria;

    private final RestClient cliente;

    public SeguridadClient(@Value("${seguridad.service.url}") String baseUrl) {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(8000);
        this.cliente = RestClient.builder().baseUrl(baseUrl).requestFactory(factory).build();
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
        auditoria.registrar(usuario, descripcion);
    }
}

