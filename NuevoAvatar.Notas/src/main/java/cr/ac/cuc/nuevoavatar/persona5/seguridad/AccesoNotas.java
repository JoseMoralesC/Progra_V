package cr.ac.cuc.nuevoavatar.persona5.seguridad;

import cr.ac.cuc.nuevoavatar.persona5.academico.Grupo;
import cr.ac.cuc.nuevoavatar.persona5.academico.GrupoRepository;
import cr.ac.cuc.nuevoavatar.persona5.matricula.Matricula;
import cr.ac.cuc.nuevoavatar.persona5.matricula.MatriculaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AccesoNotas {
    private final HttpServletRequest request;
    private final GrupoRepository grupos;
    private final MatriculaRepository matriculas;

    public AccesoNotas(HttpServletRequest request, GrupoRepository grupos, MatriculaRepository matriculas) {
        this.request = request;
        this.grupos = grupos;
        this.matriculas = matriculas;
    }

    public SesionActual profesor() {
        Object identidad = request.getAttribute(AutorizacionInterceptor.SESSION_ATTRIBUTE);
        if (!(identidad instanceof SesionActual sesion))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autorizado");
        if (!"PROFESOR".equals(sesion.rol()) || sesion.profesorId() == null)
            throw denegado();
        return sesion;
    }

    public Grupo grupo(Integer grupoId) {
        SesionActual sesion = profesor();
        Grupo grupo = grupos.findById(grupoId).orElseThrow(AccesoNotas::denegado);
        if (!sesion.profesorId().equals(grupo.getProfesorId())) throw denegado();
        return grupo;
    }

    public void matricula(Integer matriculaId) {
        profesor();
        Matricula matricula = matriculas.findById(matriculaId).orElseThrow(AccesoNotas::denegado);
        Grupo grupo = grupo(matricula.getGrupoId());
        if (!grupo.getCursoId().equals(matricula.getCursoId()) ||
                !grupo.getPeriodoId().equals(matricula.getPeriodoId())) throw denegado();
    }

    public void consulta(Integer cursoId, Integer grupoId) {
        if (!grupo(grupoId).getCursoId().equals(cursoId)) throw denegado();
    }

    private static ResponseStatusException denegado() {
        return new ResponseStatusException(HttpStatus.FORBIDDEN, "Grupo u operacion no autorizado");
    }
}
