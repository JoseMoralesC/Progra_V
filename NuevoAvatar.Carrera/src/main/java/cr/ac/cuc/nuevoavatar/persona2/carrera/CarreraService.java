package cr.ac.cuc.nuevoavatar.persona2.carrera;

import cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilter;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CarreraService {

    private final CarreraRepository carreras;
    private final cr.ac.cuc.nuevoavatar.integracion.DatosRemotos datos;

    public CarreraService(
            CarreraRepository carreras,
            cr.ac.cuc.nuevoavatar.integracion.DatosRemotos datos) {
        this.carreras = carreras;
        this.datos = datos;
    }

    @Transactional
    public CarreraResponse crear(CarreraRequest request) {
        validarReferencias(request);

        Carrera carrera = new Carrera();
        asignarDatos(carrera, request);

        try {
            CarreraResponse creada = CarreraResponse.desde(carreras.saveAndFlush(carrera));
            BitacoraFilter.auditar("Creación de carrera: " + BitacoraFilter.comoJson(creada));
            return creada;
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
        CarreraResponse anterior = CarreraResponse.desde(carrera);
        validarReferencias(request);
        asignarDatos(carrera, request);

        try {
            CarreraResponse actual = CarreraResponse.desde(carreras.saveAndFlush(carrera));
            BitacoraFilter.auditar(
                "Modificación de carrera. Anterior: " + BitacoraFilter.comoJson(anterior)
                + " Actual: " + BitacoraFilter.comoJson(actual)
            );
            return actual;
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "No se pudo modificar la carrera", ex
            );
        }
    }

    @Transactional
    public void eliminar(Integer id) {
        Carrera carrera = buscarO404(id);
        CarreraResponse eliminada = CarreraResponse.desde(carrera);

        if (datos.referencias("Curso", "Curso", "CarreraId", id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La carrera tiene cursos relacionados");
        }

        try {
            carreras.delete(carrera);
            carreras.flush();
            BitacoraFilter.auditar("Eliminación de carrera: " + BitacoraFilter.comoJson(eliminada));
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

        datos.exigir("Profesor", "Profesor", "ProfesorId", request.directorProfesorId());
    }

    private void asignarDatos(Carrera carrera, CarreraRequest request) {
        carrera.setNombre(request.nombre().trim());
        carrera.setInstitucionId(request.institucionId());
        carrera.setDirectorProfesorId(request.directorProfesorId());
    }
}
