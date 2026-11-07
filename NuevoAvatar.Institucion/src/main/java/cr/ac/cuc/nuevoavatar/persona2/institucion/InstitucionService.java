package cr.ac.cuc.nuevoavatar.persona2.institucion;

import java.util.List;
import cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilter;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class InstitucionService {

    private final InstitucionRepository instituciones;

    public InstitucionService(InstitucionRepository instituciones) {
        this.instituciones = instituciones;
    }

    @Transactional
    public InstitucionResponse crear(InstitucionRequest request) {
        Institucion institucion = new Institucion();
        institucion.setNombre(request.nombre().trim());
        InstitucionResponse actual = InstitucionResponse.desde(instituciones.saveAndFlush(institucion));
        BitacoraFilter.cambio("Creación de institución", null, actual);
        return actual;
    }

    @Transactional(readOnly = true)
    public List<InstitucionResponse> listar(String nombre) {
        List<Institucion> resultado = nombre == null || nombre.isBlank()
            ? instituciones.findAll()
            : instituciones.findByNombreContainingIgnoreCaseOrderByNombreAsc(nombre.trim());
        return resultado.stream()
            .map(InstitucionResponse::desde)
            .toList();
    }

    @Transactional(readOnly = true)
    public InstitucionResponse obtener(Integer id) {
        return InstitucionResponse.desde(buscarO404(id));
    }

    @Transactional
    public InstitucionResponse modificar(
            Integer id,
            InstitucionRequest request) {
        Institucion institucion = buscarO404(id);
        InstitucionResponse anterior = InstitucionResponse.desde(institucion);
        institucion.setNombre(request.nombre().trim());
        InstitucionResponse actual = InstitucionResponse.desde(instituciones.saveAndFlush(institucion));
        BitacoraFilter.cambio("Modificación de institución", anterior, actual);
        return actual;
    }

    @Transactional
    public void eliminar(Integer id) {
        Institucion institucion = buscarO404(id);
        try {
            instituciones.delete(institucion);
            instituciones.flush();
            BitacoraFilter.cambio("Eliminación de institución", InstitucionResponse.desde(institucion), null);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "La institución tiene carreras relacionadas",
                ex
            );
        }
    }

    private Institucion buscarO404(Integer id) {
        return instituciones.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Institución no encontrada"
            ));
    }
}

