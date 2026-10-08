package cr.ac.cuc.nuevoavatar.persona4.prematricula;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import cr.ac.cuc.nuevoavatar.persona4.expediente.ExpedienteRepository;
import cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraService;
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

    public PrematriculaService(PrematriculaRepository repositorio, ExpedienteRepository estudiantes,
            PrematriculaValidador validador, Validator validator, BitacoraService bitacora, Clock reloj) {
        this.repositorio = repositorio;
        this.estudiantes = estudiantes;
        this.validador = validador;
        this.validator = validator;
        this.bitacora = bitacora;
        this.reloj = reloj;
    }

    @Transactional
    public PrematriculaResponse crear(PrematriculaRequest datos, String token, String usuario) {
        validar(datos);
        Integer id = repositorio.crear(datos);
        var creada = buscar(id);
        bitacora.creacion(token, usuario, "prematricula", bitacora.comoJson(creada));
        return creada;
    }

    @Transactional
    public PrematriculaResponse modificar(Integer id, PrematriculaRequest datos, String token, String usuario) {
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
        var anterior = buscar(id);
        String eliminadoJson = bitacora.comoJson(anterior);
        if (repositorio.eliminar(id) == 0) throw noEncontrada();
        bitacora.eliminacion(token, usuario, "prematricula", eliminadoJson);
    }

    @Transactional(readOnly = true)
    public List<PrematriculaResponse> obtenerTodos(String token, String usuario) {
        var datos = repositorio.obtenerTodos();
        bitacora.consulta(token, usuario, "prematriculas");
        return datos;
    }

    @Transactional(readOnly = true)
    public PrematriculaResponse obtener(Integer id, String token, String usuario) {
        var datos = buscar(id);
        bitacora.consulta(token, usuario, "prematricula");
        return datos;
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
