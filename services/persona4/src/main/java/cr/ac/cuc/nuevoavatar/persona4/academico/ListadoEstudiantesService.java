package cr.ac.cuc.nuevoavatar.persona4.academico;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ListadoEstudiantesService {
    private final ListadoEstudiantesRepository repositorio;
    private final MatriculaNotasClient matriculas;
    private final BitacoraService bitacora;

    public ListadoEstudiantesService(ListadoEstudiantesRepository repositorio,
            MatriculaNotasClient matriculas, BitacoraService bitacora) {
        this.repositorio = repositorio;
        this.matriculas = matriculas;
        this.bitacora = bitacora;
    }

    @Transactional(readOnly = true)
    public List<ListadoEstudianteResponse> obtener(Integer periodoId, String token, String usuario) {
        var resultado = new ArrayList<ListadoEstudianteResponse>();
        for (var grupo : repositorio.obtenerGrupos(periodoId)) {
            for (var matricula : matriculas.obtenerMatriculas(token, grupo.cursoId(), grupo.id())) {
                if (matricula == null || matricula.identificacionEstudiante() == null
                        || matricula.identificacionEstudiante().isBlank() || matricula.periodoId() == null
                        || matricula.cursoId() == null || matricula.grupoId() == null || matricula.estado() == null) {
                    throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                            "El servicio de matricula devolvio datos incompletos");
                }
                // El contrato de MAT2 consulta por curso/grupo; ACA2 acota el periodo solicitado.
                if (!Objects.equals(matricula.periodoId(), periodoId)
                        || !Objects.equals(matricula.cursoId(), grupo.cursoId())
                        || !Objects.equals(matricula.grupoId(), grupo.id())
                        || !"ACTIVA".equals(matricula.estado())) continue;
                var estudiante = repositorio.obtenerEstudiante(matricula.identificacionEstudiante().trim())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                "No fue posible relacionar la matricula con un expediente"));
                resultado.add(new ListadoEstudianteResponse(estudiante.tipoIdentificacion(),
                        estudiante.identificacion(), estudiante.nombreCompleto(), grupo.carrera(),
                        grupo.curso(), grupo.numero()));
            }
        }
        bitacora.consulta(token, usuario, "listado de estudiantes");
        return resultado;
    }
}
