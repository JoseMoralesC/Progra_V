package cr.ac.cuc.nuevoavatar.persona2.carrera;

import java.util.List;

import cr.ac.cuc.nuevoavatar.persona2.profesor.ProfesorRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CarreraService {

    private final CarreraRepository carreras;
    private final ProfesorRepository profesores;
    private final cr.ac.cuc.nuevoavatar.integracion.DatosRemotos datos;

    public CarreraService(
            CarreraRepository carreras,
            ProfesorRepository profesores,
            cr.ac.cuc.nuevoavatar.integracion.DatosRemotos datos) {
        this.carreras = carreras;
        this.profesores = profesores;
        this.datos = datos;
    }

    @Transactional
    public CarreraResponse crear(CarreraRequest request) {
        validarReferencias(request);

        Carrera carrera = new Carrera();
        asignarDatos(carrera, request);

        try {
            return CarreraResponse.desde(carreras.saveAndFlush(carrera));
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "No se pudo crear la carrera", ex
            );
        }
    }

    @Transactional(readOnly = true)
    public List<CarreraResponse> listar(Integer institucionId) {
        List<Carrera> resultado = institucionId == null
            ? carreras.findAll()
            : carreras.findByInstitucionId(institucionId);

        return resultado.stream()
            .map(CarreraResponse::desde)
            .toList();
    }

    @Transactional(readOnly = true)
    public CarreraResponse obtener(Integer id) {
        return CarreraResponse.desde(buscarO404(id));
    }

    @Transactional
    public CarreraResponse modificar(Integer id, CarreraRequest request) {
        Carrera carrera = buscarO404(id);
        validarReferencias(request);
        asignarDatos(carrera, request);

        try {
            return CarreraResponse.desde(carreras.saveAndFlush(carrera));
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "No se pudo modificar la carrera", ex
            );
        }
    }

    @Transactional
    public void eliminar(Integer id) {
        Carrera carrera = buscarO404(id);

        try {
            carreras.delete(carrera);
            carreras.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "La carrera tiene registros relacionados",
                ex
            );
        }
    }

    private Carrera buscarO404(Integer id) {
        return carreras.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Carrera no encontrada"
            ));
    }

    private void validarReferencias(CarreraRequest request) {
        datos.exigir("Institucion","Institucion","InstitucionId",request.institucionId());

        if (!profesores.existsById(request.directorProfesorId())) {
            throw new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_CONTENT,
                "El profesor director no existe"
            );
        }
    }

    private void asignarDatos(Carrera carrera, CarreraRequest request) {
        carrera.setNombre(request.nombre().trim());
        carrera.setInstitucionId(request.institucionId());
        carrera.setDirectorProfesorId(request.directorProfesorId());
    }
}
