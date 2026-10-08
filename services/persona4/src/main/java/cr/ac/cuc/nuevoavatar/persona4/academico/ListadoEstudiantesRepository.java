package cr.ac.cuc.nuevoavatar.persona4.academico;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ListadoEstudiantesRepository {
    private final JdbcTemplate jdbc;

    public ListadoEstudiantesRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Grupo> obtenerGrupos(Integer periodoId) {
        return jdbc.query("""
                SELECT g.GrupoId, g.NumeroGrupo, g.CursoId, c.Nombre AS Curso, ca.Nombre AS Carrera
                FROM academico.Grupo g
                JOIN academico.Curso c ON c.CursoId = g.CursoId
                JOIN academico.Carrera ca ON ca.CarreraId = c.CarreraId
                WHERE g.PeriodoId = ?
                ORDER BY g.GrupoId
                """, (fila, numero) -> new Grupo(fila.getInt("GrupoId"), fila.getInt("NumeroGrupo"),
                        fila.getInt("CursoId"), fila.getString("Curso"), fila.getString("Carrera")), periodoId);
    }

    public Optional<Estudiante> obtenerEstudiante(String identificacion) {
        return jdbc.query("""
                SELECT TipoIdentificacion, Identificacion, NombreCompleto
                FROM matricula.Estudiante WHERE Identificacion = ?
                """, (fila, numero) -> new Estudiante(fila.getString("TipoIdentificacion"),
                        fila.getString("Identificacion"), fila.getString("NombreCompleto")), identificacion)
                .stream().findFirst();
    }

    public record Grupo(Integer id, Integer numero, Integer cursoId, String curso, String carrera) {}
    public record Estudiante(String tipoIdentificacion, String identificacion, String nombreCompleto) {}
}
