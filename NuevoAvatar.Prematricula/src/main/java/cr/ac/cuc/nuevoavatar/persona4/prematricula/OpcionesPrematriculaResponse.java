package cr.ac.cuc.nuevoavatar.persona4.prematricula;

import java.time.LocalDate;
import java.util.List;

public record OpcionesPrematriculaResponse(
        List<CarreraOpcion> carreras,
        List<CursoOpcion> cursos,
        List<PeriodoOpcion> periodos) {
    public record CarreraOpcion(Integer id, String nombre) { }
    public record CursoOpcion(Integer id, Integer carreraId, String nombre) { }
    public record PeriodoOpcion(Integer id, LocalDate fechaInicio) { }
}
