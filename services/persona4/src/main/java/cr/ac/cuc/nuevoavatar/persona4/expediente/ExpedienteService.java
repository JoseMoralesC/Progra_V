package cr.ac.cuc.nuevoavatar.persona4.expediente;

import java.util.List;
import cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ExpedienteService {
    private final ExpedienteRepository repositorio;
    private final ExpedienteValidador validador;
    private final DireccionClient direcciones;
    private final BitacoraService bitacora;

    public ExpedienteService(ExpedienteRepository repositorio, ExpedienteValidador validador,
            DireccionClient direcciones, BitacoraService bitacora) {
        this.repositorio = repositorio;
        this.validador = validador;
        this.direcciones = direcciones;
        this.bitacora = bitacora;
    }

    @Transactional
    public DatosExpediente crear(DatosExpediente datos, String token, String usuario) {
        validador.validar(datos, token);
        datos = normalizar(datos);
        if (repositorio.existe(datos.identificacion())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un expediente con esa identificacion");
        }
        direcciones.validar(token, datos.provinciaId(), datos.cantonId(), datos.distritoId());
        repositorio.crear(datos);
        DatosExpediente creado = buscar(datos.identificacion());
        // GEN1 se invoca antes del commit; un fallo reportado revierte la escritura local.
        bitacora.creacion(token, usuario, "expediente", bitacora.comoJson(creado));
        return creado;
    }

    @Transactional
    public DatosExpediente modificar(String identificacion, DatosExpediente datos, String token, String usuario) {
        DatosExpediente anterior = buscar(identificacion);
        validador.validar(datos, token);
        datos = normalizar(datos);
        direcciones.validar(token, datos.provinciaId(), datos.cantonId(), datos.distritoId());
        String anteriorJson = bitacora.comoJson(anterior);
        if (repositorio.modificar(identificacion, datos) == 0) throw noEncontrado();
        DatosExpediente actual = buscar(datos.identificacion());
        bitacora.modificacion(token, usuario, "expediente", anteriorJson, bitacora.comoJson(actual));
        return actual;
    }

    @Transactional
    public void eliminar(String identificacion, String token, String usuario) {
        DatosExpediente anterior = buscar(identificacion);
        String eliminadoJson = bitacora.comoJson(anterior);
        if (repositorio.eliminar(identificacion) == 0) throw noEncontrado();
        bitacora.eliminacion(token, usuario, "expediente", eliminadoJson);
    }

    @Transactional(readOnly = true)
    public List<DatosExpediente> obtenerTodos(String token, String usuario) {
        var datos = repositorio.obtenerTodos();
        bitacora.consulta(token, usuario, "expedientes");
        return datos;
    }

    @Transactional(readOnly = true)
    public DatosExpediente obtener(String identificacion, String token, String usuario) {
        var datos = buscar(identificacion);
        bitacora.consulta(token, usuario, "expediente");
        return datos;
    }

    private DatosExpediente buscar(String identificacion) {
        return repositorio.obtener(identificacion).orElseThrow(this::noEncontrado);
    }

    private ResponseStatusException noEncontrado() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Expediente no encontrado");
    }

    private DatosExpediente normalizar(DatosExpediente datos) {
        return new DatosExpediente(datos.tipoIdentificacion(), datos.identificacion().trim(), datos.email(),
                datos.nombreCompleto(), datos.fechaNacimiento(), datos.provinciaId(), datos.cantonId(),
                datos.distritoId(), datos.otrasSenas(), datos.telefonos());
    }
}
