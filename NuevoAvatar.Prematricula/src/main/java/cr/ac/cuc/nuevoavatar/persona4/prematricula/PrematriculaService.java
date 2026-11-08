package cr.ac.cuc.nuevoavatar.persona4.prematricula;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteRepository;
import cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraService;
import cr.ac.cuc.nuevoavatar.persona4.seguridad.SeguridadClient;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PrematriculaService {
    private final PrematriculaRepository repositorio;
    private final ExpedienteRepository estudiantes;
    private final PrematriculaValidador validador;
    private final Validator validator;
    private final BitacoraService bitacora;
    private final Clock reloj;
    private final SeguridadClient seguridad;

    public PrematriculaService(PrematriculaRepository repositorio, ExpedienteRepository estudiantes,
            PrematriculaValidador validador, Validator validator, BitacoraService bitacora, Clock reloj,
            SeguridadClient seguridad) {
        this.repositorio = repositorio;
        this.estudiantes = estudiantes;
        this.validador = validador;
        this.validator = validator;
        this.bitacora = bitacora;
        this.reloj = reloj;
        this.seguridad = seguridad;
    }

    @Transactional
    public PrematriculaResponse crear(PrematriculaRequest datos, String token, String usuario) {
        validarAdministrador(token, usuario);
        validar(datos);
        Integer id = repositorio.crear(datos);
        var creada = buscar(id);
        bitacora.creacion(token, usuario, "prematricula", bitacora.comoJson(creada));
        return creada;
    }

    @Transactional
    public PrematriculaResponse modificar(Integer id, PrematriculaRequest datos, String token, String usuario) {
        validarAdministrador(token, usuario);
        var anterior = buscar(id);
        validar(datos);
        String anteriorJson = bitacora.comoJson(anterior);
        if (repositorio.modificar(id, datos) == 0) throw noEncontrada();
        var actual = buscar(id);
        bitacora.modificacion(token, usuario, "prematricula", anteriorJson, bitacora.comoJson(actual));
        return actual;
    }

    @Transactional
    public void eliminar(Integer id, String token, String usuario) {
        validarAdministrador(token, usuario);
        var anterior = buscar(id);
        String eliminadoJson = bitacora.comoJson(anterior);
        if (repositorio.eliminar(id) == 0) throw noEncontrada();
        bitacora.eliminacion(token, usuario, "prematricula", eliminadoJson);
    }

    @Transactional(readOnly = true)
    public List<PrematriculaResponse> obtenerTodos(String token, String usuario) {
        validarAdministrador(token, usuario);
        var datos = repositorio.obtenerTodos();
        bitacora.consulta(token, usuario, "prematriculas");
        return datos;
    }

    @Transactional(readOnly = true)
    public PrematriculaResponse obtener(Integer id, String token, String usuario) {
        validarAdministrador(token, usuario);
        var datos = buscar(id);
        bitacora.consulta(token, usuario, "prematricula");
        return datos;
    }

    @Transactional(readOnly = true)
    public OpcionesPrematriculaResponse opcionesPropias(String token, String usuario) {
        identidadEstudiante(token, usuario);
        var opciones = repositorio.opciones(LocalDate.now(reloj));
        bitacora.consulta(token, usuario, "opciones de prematricula propia");
        return opciones;
    }

    @Transactional(readOnly = true)
    public List<PrematriculaResponse> obtenerPropias(String token, String usuario) {
        String identificacion = identidadEstudiante(token, usuario);
        var solicitudes = repositorio.obtenerPropios(identificacion);
        bitacora.consulta(token, usuario, "prematriculas propias");
        return solicitudes;
    }

    @Transactional(readOnly = true)
    public PrematriculaResponse obtenerPropia(Integer id, String token, String usuario) {
        String identificacion = identidadEstudiante(token, usuario);
        var solicitud = repositorio.obtenerPropio(id, identificacion).orElseThrow(this::noEncontrada);
        bitacora.consulta(token, usuario, "prematricula propia");
        return solicitud;
    }

    @Transactional
    public PrematriculaResponse crearPropia(PrematriculaPropiaRequest datos, String token, String usuario) {
        String identificacion = identidadEstudiante(token, usuario);
        var solicitud = convertir(datos, identificacion);
        validar(solicitud);
        Integer id = repositorio.crear(solicitud);
        var creada = repositorio.obtenerPropio(id, identificacion).orElseThrow(this::noEncontrada);
        bitacora.creacion(token, usuario, "prematricula propia", bitacora.comoJson(creada));
        return creada;
    }

    @Transactional
    public PrematriculaResponse modificarPropia(Integer id, PrematriculaPropiaRequest cambios,
            String token, String usuario) {
        String identificacion = identidadEstudiante(token, usuario);
        var anterior = repositorio.obtenerPropio(id, identificacion).orElseThrow(this::noEncontrada);
        var solicitud = convertir(cambios, identificacion);
        validar(solicitud);
        String anteriorJson = bitacora.comoJson(anterior);
        if (repositorio.modificarPropio(id, identificacion, cambios) == 0) throw noEncontrada();
        var actual = repositorio.obtenerPropio(id, identificacion).orElseThrow(this::noEncontrada);
        bitacora.modificacion(token, usuario, "prematricula propia", anteriorJson, bitacora.comoJson(actual));
        return actual;
    }

    @Transactional
    public void cancelarPropia(Integer id, String token, String usuario) {
        String identificacion = identidadEstudiante(token, usuario);
        var anterior = repositorio.obtenerPropio(id, identificacion).orElseThrow(this::noEncontrada);
        String eliminadoJson = bitacora.comoJson(anterior);
        if (repositorio.eliminarPropio(id, identificacion) == 0) throw noEncontrada();
        bitacora.eliminacion(token, usuario, "prematricula propia", eliminadoJson);
    }

    private PrematriculaRequest convertir(PrematriculaPropiaRequest datos, String identificacion) {
        if (datos == null) throw invalido("Los datos de prematricula son requeridos");
        var errores = validator.validate(datos);
        if (!errores.isEmpty()) throw new ConstraintViolationException(errores);
        if (datos.cursos() != null && datos.cursos().stream().distinct().count() != datos.cursos().size()) {
            throw invalido("No se puede repetir un curso en la solicitud");
        }
        return new PrematriculaRequest(identificacion, datos.carreraId(), datos.cursos(),
                datos.observaciones(), datos.periodoId());
    }

    private String identidadEstudiante(String token, String usuario) {
        SeguridadClient.SesionActual sesion = seguridad.obtenerSesion(token);
        if (sesion.usuarioID() == null || !sesion.usuarioID().equalsIgnoreCase(usuario)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La identidad de sesión no coincide con el token");
        }
        if (!"ESTUDIANTE".equals(sesion.rol().strip().toUpperCase(Locale.ROOT))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La operación requiere el rol ESTUDIANTE");
        }
        if (sesion.estudianteId() == null || sesion.estudianteId().isBlank()
                || sesion.identificacion() == null || sesion.identificacion().isBlank()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay un expediente vinculado a la sesión");
        }
        if (!sesion.estudianteId().equals(sesion.identificacion())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El vínculo de sesión con el expediente es inconsistente");
        }
        if (!estudiantes.existe(sesion.estudianteId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay un expediente vinculado a la sesión");
        }
        return sesion.estudianteId();
    }

    private void validarAdministrador(String token, String usuario) {
        SeguridadClient.SesionActual sesion = seguridad.obtenerSesion(token);
        if (sesion.usuarioID() == null || !sesion.usuarioID().equalsIgnoreCase(usuario)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La identidad de sesión no coincide con el token");
        }
        if (!"ADMINISTRADOR".equals(sesion.rol().strip().toUpperCase(Locale.ROOT))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La operación requiere el rol ADMINISTRADOR");
        }
    }

    private void validar(PrematriculaRequest datos) {
        var errores = validator.validate(datos);
        if (!errores.isEmpty()) throw new ConstraintViolationException(errores);
        if (!estudiantes.existe(datos.identificacionEstudiante().trim())) throw invalido("El estudiante no existe");
        if (!repositorio.existeCarrera(datos.carreraId())) throw invalido("La carrera no existe");

        // Nivel y fecha se leen de la oferta academica, no del cuerpo de la solicitud.
        var cursos = new ArrayList<DatosPrematricula.Curso>();
        for (Integer cursoId : datos.cursos()) {
            cursos.add(repositorio.obtenerCurso(cursoId).orElseThrow(() -> invalido("El curso no existe")));
        }
        var periodo = repositorio.obtenerPeriodo(datos.periodoId())
                .orElseThrow(() -> invalido("El periodo no existe"));
        validador.validar(new DatosPrematricula(datos.identificacionEstudiante(), datos.carreraId(),
                cursos, datos.observaciones(), periodo), LocalDate.now(reloj));
    }

    private PrematriculaResponse buscar(Integer id) {
        return repositorio.obtener(id).orElseThrow(this::noEncontrada);
    }

    private ResponseStatusException invalido(String mensaje) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensaje);
    }

    private ResponseStatusException noEncontrada() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Prematricula no encontrada");
    }
}

