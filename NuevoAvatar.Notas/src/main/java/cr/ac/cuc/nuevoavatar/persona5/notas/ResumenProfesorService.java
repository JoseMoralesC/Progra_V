package cr.ac.cuc.nuevoavatar.persona5.notas;

import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ResumenProfesorService {
    private final DatosRemotos datos;

    public ResumenProfesorService(DatosRemotos datos) { this.datos = datos; }

    public List<GrupoProfesorResponse> obtener(Integer profesorId, Integer periodoId) {
        Map<String, ?> filtros = periodoId == null ? Map.of("ProfesorId", profesorId)
            : Map.of("ProfesorId", profesorId, "PeriodoId", periodoId);
        var resultado = new ArrayList<GrupoProfesorResponse>();
        LocalDate hoy = LocalDate.now();
        for (var grupo : datos.listar("Grupo", "Grupo", filtros)) {
            int periodo = DatosRemotos.entero(grupo, "PeriodoId");
            var datosPeriodo = datos.buscar("Periodo", "Periodo", "PeriodoId", periodo)
                .orElseThrow(() -> new IllegalStateException("Periodo del grupo no disponible"));
            if (periodoId == null && (hoy.isBefore(DatosRemotos.fecha(datosPeriodo, "FechaInicio")) ||
                    hoy.isAfter(DatosRemotos.fecha(datosPeriodo, "FechaFin")))) continue;
            int curso = DatosRemotos.entero(grupo, "CursoId");
            var datosCurso = datos.buscar("Curso", "Curso", "CursoId", curso)
                .orElseThrow(() -> new IllegalStateException("Curso del grupo no disponible"));
            resultado.add(new GrupoProfesorResponse(DatosRemotos.entero(grupo, "GrupoId"),
                DatosRemotos.entero(grupo, "NumeroGrupo"), curso, DatosRemotos.texto(datosCurso, "Nombre"),
                periodo, DatosRemotos.texto(grupo, "Horario")));
        }
        return List.copyOf(resultado);
    }

    public record GrupoProfesorResponse(Integer grupoId, Integer numeroGrupo, Integer cursoId,
            String curso, Integer periodoId, String horario) { }
}
