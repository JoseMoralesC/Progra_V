package cr.ac.cuc.nuevoavatar.persona4.expediente;

import java.util.List;
import java.util.Locale;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
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
    private final cr.ac.cuc.nuevoavatar.integracion.DatosRemotos remotos;
    private final cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClient seguridad;
    private final Validator validator;

    public ExpedienteService(ExpedienteRepository repositorio, ExpedienteValidador validador,
            DireccionClient direcciones, BitacoraService bitacora, cr.ac.cuc.nuevoavatar.integracion.DatosRemotos remotos,
            cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClient seguridad, Validator validator) {
        this.repositorio = repositorio;
        this.validador = validador;
        this.direcciones = direcciones;
        this.bitacora = bitacora;
        this.remotos = remotos;
        this.seguridad = seguridad;
        this.validator = validator;
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
        if(datos != null && datos.identificacion()!=null && !identificacion.equals(datos.identificacion().trim())) remotos.sinReferencias("Expediente","Estudiante",identificacion);
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

    @Transactional(readOnly = true)
    public DatosExpediente obtenerPropio(String token, String usuario) {
        DatosExpediente propio = resolverPropio(token, usuario);
        bitacora.consulta(token, usuario, "expediente propio");
        return propio;
    }

    @Transactional
    public DatosExpediente modificarPropio(ActualizacionExpedientePropio cambios, String token, String usuario) {
        validarCambios(cambios);
        DatosExpediente anterior = resolverPropio(token, usuario);
        direcciones.validar(token, cambios.provinciaId(), cambios.cantonId(), cambios.distritoId());
        String anteriorJson = bitacora.comoJson(datosAuditables(anterior));
        if (repositorio.modificarPropio(anterior.identificacion(), cambios) == 0) throw noEncontrado();
        DatosExpediente actual = buscar(anterior.identificacion());
        bitacora.modificacion(token, usuario, "expediente propio", anteriorJson,
                bitacora.comoJson(datosAuditables(actual)));
        return actual;
    }

    private DatosExpediente resolverPropio(String token, String usuario) {
        var sesion = seguridad.obtenerSesion(token);
        if (sesion.usuarioID() == null || !sesion.usuarioID().equalsIgnoreCase(usuario)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La identidad de sesión no coincide con el token");
        }
        if (sesion.rol() == null || !"ESTUDIANTE".equals(sesion.rol().strip().toUpperCase(Locale.ROOT))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La operación requiere el rol ESTUDIANTE");
        }
        if (sesion.identificacion() == null || sesion.identificacion().isBlank()
                || sesion.tipoIdentificacion() == null || sesion.tipoIdentificacion().isBlank()) throw noEncontrado();
        List<DatosExpediente> vinculados = repositorio.obtenerVinculados(
                sesion.usuarioID(), sesion.tipoIdentificacion(), sesion.identificacion());
        if (sesion.estudianteId() == null || sesion.estudianteId().isBlank()) {
            if (vinculados.isEmpty()) throw noEncontrado();
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    vinculados.size() > 1 ? "El usuario tiene un vínculo ambiguo con expedientes"
                            : "Autenticación no confirmó un vínculo único con el expediente");
        }
        if (!sesion.identificacion().equals(sesion.estudianteId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El vínculo de sesión con el expediente es inconsistente");
        }
        if (vinculados.isEmpty()) throw noEncontrado();
        if (vinculados.size() != 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario tiene un vínculo ambiguo con expedientes");
        }
        return vinculados.get(0);
    }

    private void validarCambios(ActualizacionExpedientePropio cambios) {
        if (cambios == null) throw new IllegalArgumentException("Los datos editables son requeridos");
        var errores = validator.validate(cambios);
        if (!errores.isEmpty()) throw new ConstraintViolationException(errores);
    }

    private Object datosAuditables(DatosExpediente expediente) {
        return new DatosEditables(expediente.provinciaId(), expediente.cantonId(), expediente.distritoId(),
                expediente.otrasSenas(), expediente.telefonos());
    }

    private record DatosEditables(Integer provinciaId, Integer cantonId, Integer distritoId,
            String otrasSenas, List<String> telefonos) { }

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

