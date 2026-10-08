package cr.ac.cuc.nuevoavatar.persona2.grupo;

import java.util.List;

import cr.ac.cuc.nuevoavatar.persona2.curso.CursoRepository;
import cr.ac.cuc.nuevoavatar.persona2.periodo.PeriodoRepository;
import cr.ac.cuc.nuevoavatar.persona2.profesor.ProfesorRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GrupoService {

    private final GrupoRepository grupos;
    private final CursoRepository cursos;
    private final ProfesorRepository profesores;
    private final PeriodoRepository periodos;

    public GrupoService(
            GrupoRepository grupos,
            CursoRepository cursos,
            ProfesorRepository profesores,
            PeriodoRepository periodos) {
        this.grupos = grupos;
        this.cursos = cursos;
        this.profesores = profesores;
        this.periodos = periodos;
    }

    @Transactional
    public GrupoResponse crear(GrupoRequest request) {
        validarReferencias(request);
        Grupo grupo = new Grupo();
        asignar(grupo, request);
        return GrupoResponse.desde(grupos.saveAndFlush(grupo));
    }

    @Transactional(readOnly = true)
    public List<GrupoResponse> listar() {
        return grupos.findAll().stream().map(GrupoResponse::desde).toList();
    }

    @Transactional(readOnly = true)
    public GrupoResponse obtener(Integer id) {
        return GrupoResponse.desde(buscarO404(id));
    }

    @Transactional
    public GrupoResponse modificar(Integer id, GrupoRequest request) {
        validarReferencias(request);
        Grupo grupo = buscarO404(id);
        asignar(grupo, request);
        return GrupoResponse.desde(grupos.saveAndFlush(grupo));
    }

    @Transactional
    public void eliminar(Integer id) {
        Grupo grupo = buscarO404(id);
        try {
            grupos.delete(grupo);
            grupos.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "El grupo tiene registros relacionados",
                ex
            );
        }
    }

    private Grupo buscarO404(Integer id) {
        return grupos.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Grupo no encontrado")
        );
    }

    private void validarReferencias(GrupoRequest request) {
        if (!cursos.existsById(request.cursoId())) {
            throw referenciaInvalida("El curso no existe");
        }
        if (!profesores.existsById(request.profesorId())) {
            throw referenciaInvalida("El profesor no existe");
        }
        if (!periodos.existsById(request.periodoId())) {
            throw referenciaInvalida("El periodo no existe");
        }
    }

    private ResponseStatusException referenciaInvalida(String mensaje) {
        return new ResponseStatusException(
            HttpStatus.UNPROCESSABLE_CONTENT,
            mensaje
        );
    }

    private void asignar(Grupo grupo, GrupoRequest request) {
        grupo.setNumeroGrupo(request.numeroGrupo());
        grupo.setCursoId(request.cursoId());
        grupo.setProfesorId(request.profesorId());
        grupo.setHorario(request.horario().trim());
        grupo.setCupo(request.cupo());
        grupo.setPeriodoId(request.periodoId());
    }
}
