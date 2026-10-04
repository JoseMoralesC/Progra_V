package cr.ac.cuc.nuevoavatar.persona2.institucion;

import java.util.List;

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
        return InstitucionResponse.desde(
            instituciones.saveAndFlush(institucion)
        );
    }

    @Transactional(readOnly = true)
    public List<InstitucionResponse> listar() {
        return instituciones.findAll().stream()
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
        institucion.setNombre(request.nombre().trim());
        return InstitucionResponse.desde(
            instituciones.saveAndFlush(institucion)
        );
    }

    @Transactional
    public void eliminar(Integer id) {
        Institucion institucion = buscarO404(id);
        try {
            instituciones.delete(institucion);
            instituciones.flush();
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
