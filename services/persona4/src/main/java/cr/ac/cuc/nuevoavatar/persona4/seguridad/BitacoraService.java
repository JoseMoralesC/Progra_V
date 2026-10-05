package cr.ac.cuc.nuevoavatar.persona4.seguridad;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class BitacoraService {
    private final SeguridadClient seguridad;
    private final ObjectMapper mapper;

    public BitacoraService(SeguridadClient seguridad, ObjectMapper mapper) {
        this.seguridad = seguridad;
        this.mapper = mapper;
    }

    /** Conserva los datos de una entidad antes de que la operacion la modifique. */
    public String comoJson(Object registro) {
        try {
            return mapper.writeValueAsString(registro);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("No fue posible preparar el registro de bitacora", ex);
        }
    }

    public void creacion(String token, String usuario, String elemento, String nuevoJson) {
        seguridad.registrarBitacora(token, usuario, "Creacion de " + elemento + ": " + nuevoJson);
    }

    public void modificacion(String token, String usuario, String elemento,
            String anteriorJson, String actualJson) {
        seguridad.registrarBitacora(token, usuario, "Modificacion de " + elemento
                + ". Anterior: " + anteriorJson + " Actual: " + actualJson);
    }

    public void eliminacion(String token, String usuario, String elemento, String eliminadoJson) {
        seguridad.registrarBitacora(token, usuario, "Eliminacion de " + elemento + ": " + eliminadoJson);
    }

    public void consulta(String token, String usuario, String elemento) {
        seguridad.registrarBitacora(token, usuario, "El usuario consulta " + elemento);
    }

    public void errorTecnico(String token, String usuario, String operacion, Exception error) {
        // El mensaje original puede contener credenciales o datos de conexion.
        seguridad.registrarBitacora(token, usuario, "Error tecnico en " + operacion
                + ": " + error.getClass().getSimpleName());
    }
}
