package cr.ac.cuc.nuevoavatar.persona2.curso;

import java.util.List;

import cr.ac.cuc.nuevoavatar.persona2.carrera.CarreraRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CursoService {

    private final CursoRepository cursos;
    private final CarreraRepository carreras;

    public CursoService(
            CursoRepository cursos,
            CarreraRepository carreras) {
        this.cursos = cursos;
        this.carreras = carreras;
    }

    @Transactional
    public CursoResponse crear(CursoRequest request) {
        validarCarrera(request.carreraId());
        Curso curso = new Curso();
        asignar(curso, request);
        return CursoResponse.desde(cursos.saveAndFlush(curso));
    }

    @Transactional(readOnly = true)
    public List<CursoResponse> listar(Integer carreraId) {
        List<Curso> resultado = carreraId == null
            ? cursos.findAll()
            : cursos.findByCarreraId(carreraId);
        return resultado.stream().map(CursoResponse::desde).toList();
    }

    @Transactional(readOnly = true)
    public CursoResponse obtener(Integer id) {
        return CursoResponse.desde(buscarO404(id));
    }

    @Transactional
    public CursoResponse modificar(Integer id, CursoRequest request) {
        validarCarrera(request.carreraId());
        Curso curso = buscarO404(id);
        asignar(curso, request);
        return CursoResponse.desde(cursos.saveAndFlush(curso));
    }

    @Transactional
    public void eliminar(Integer id) {
        Curso curso = buscarO404(id);
        try {
            cursos.delete(curso);
            cursos.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "El curso tiene grupos relacionados",
                ex
            );
        }
    }

    private Curso buscarO404(Integer id) {
        return cursos.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado")
        );
    }

    private void validarCarrera(Integer carreraId) {
        if (!carreras.existsById(carreraId)) {
            throw new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_CONTENT,
                "La carrera no existe"
            );
        }
    }

    private void asignar(Curso curso, CursoRequest request) {
        curso.setCarreraId(request.carreraId());
        curso.setNivel(request.nivel().byteValue());
        curso.setNombre(request.nombre().trim());
    }
}
