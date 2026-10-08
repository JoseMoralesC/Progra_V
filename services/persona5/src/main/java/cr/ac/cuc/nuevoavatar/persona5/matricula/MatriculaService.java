package cr.ac.cuc.nuevoavatar.persona5.matricula;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import cr.ac.cuc.nuevoavatar.persona5.academico.CursoRepository;
import cr.ac.cuc.nuevoavatar.persona5.academico.Grupo;
import cr.ac.cuc.nuevoavatar.persona5.academico.GrupoRepository;
import cr.ac.cuc.nuevoavatar.persona5.academico.Periodo;
import cr.ac.cuc.nuevoavatar.persona5.academico.PeriodoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MatriculaService {

    private final MatriculaRepository matriculas;
    private final CursoRepository cursos;
    private final GrupoRepository grupos;
    private final PeriodoRepository periodos;

    public MatriculaService(
            MatriculaRepository matriculas,
            CursoRepository cursos,
            GrupoRepository grupos,
            PeriodoRepository periodos) {
        this.matriculas = matriculas;
        this.cursos = cursos;
        this.grupos = grupos;
        this.periodos = periodos;
    }

    @Transactional
    public MatriculaResponse crear(MatriculaRequest request) {
        validar(request, null);
        Matricula matricula = new Matricula();
        matricula.setFechaMatricula(LocalDateTime.now());
        asignar(matricula, request);
        return MatriculaResponse.desde(matriculas.saveAndFlush(matricula));
    }

    @Transactional
    public MatriculaResponse modificar(Integer id, MatriculaRequest request) {
        Matricula matricula = buscarO404(id);
        validar(request, id);
        asignar(matricula, request);
        return MatriculaResponse.desde(matriculas.saveAndFlush(matricula));
    }

    @Transactional
    public void eliminar(Integer id) {
        Matricula matricula = buscarO404(id);
        matricula.setEstado(Matricula.ANULADA);
        matriculas.saveAndFlush(matricula);
    }

    @Transactional(readOnly = true)
    public List<MatriculaResponse> listarPorCursoYGrupo(
            Integer cursoId,
            Integer grupoId) {
        return matriculas.findByCursoIdAndGrupoIdAndEstado(
                cursoId,
                grupoId,
                Matricula.ACTIVA
            )
            .stream()
            .map(MatriculaResponse::desde)
            .toList();
    }

    private Matricula buscarO404(Integer id) {
        return matriculas.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Matricula no encontrada")
        );
    }

    private void validar(MatriculaRequest request, Integer idActual) {
        if (!cursos.existsById(request.cursoId())) {
            throw invalido("El curso no existe");
        }

        Grupo grupo = grupos.findById(request.grupoId()).orElseThrow(() ->
            invalido("El grupo no existe")
        );
        if (!grupo.getCursoId().equals(request.cursoId())) {
            throw invalido("El grupo no pertenece al curso indicado");
        }
        if (!grupo.getPeriodoId().equals(request.periodoId())) {
            throw invalido("El grupo no pertenece al periodo indicado");
        }

        Periodo periodo = periodos.findById(request.periodoId()).orElseThrow(() ->
            invalido("El periodo no existe")
        );
        if (!periodoActivo(periodo)) {
            throw invalido("El periodo no esta activo");
        }

        boolean duplicada = matriculas
            .findFirstByIdentificacionEstudianteAndCursoIdAndGrupoIdAndPeriodoIdAndEstado(
                request.identificacionEstudiante().trim(),
                request.cursoId(),
                request.grupoId(),
                request.periodoId(),
                Matricula.ACTIVA
            )
            .filter(matricula -> !matricula.getId().equals(idActual))
            .isPresent();
        if (duplicada) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "El estudiante ya tiene una matricula activa para ese curso y grupo"
            );
        }
    }

    private boolean periodoActivo(Periodo periodo) {
        LocalDate hoy = LocalDate.now();
        return !hoy.isBefore(periodo.getFechaInicio())
            && !hoy.isAfter(periodo.getFechaFin());
    }

    private ResponseStatusException invalido(String mensaje) {
        return new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, mensaje);
    }

    private void asignar(Matricula matricula, MatriculaRequest request) {
        matricula.setIdentificacionEstudiante(
            request.identificacionEstudiante().trim()
        );
        matricula.setCursoId(request.cursoId());
        matricula.setGrupoId(request.grupoId());
        matricula.setPeriodoId(request.periodoId());
        matricula.setEstado(Matricula.ACTIVA);
    }
}
