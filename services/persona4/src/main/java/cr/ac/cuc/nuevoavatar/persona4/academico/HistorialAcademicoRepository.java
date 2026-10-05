package cr.ac.cuc.nuevoavatar.persona4.academico;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HistorialAcademicoRepository {
    private final JdbcTemplate jdbc;

    public HistorialAcademicoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public boolean existeEstudiante(String tipo, String identificacion) {
        return jdbc.queryForObject("""
                SELECT COUNT(*) FROM matricula.Estudiante
                WHERE TipoIdentificacion = ? AND Identificacion = ?
                """, Integer.class, tipo, identificacion) > 0;
    }

    public List<Intento> obtenerIntentos(String identificacion) {
        return jdbc.query("""
                SELECT m.MatriculaId, m.CursoId, m.GrupoId, c.Nombre
                FROM matricula.Matricula m
                JOIN academico.Curso c ON c.CursoId = m.CursoId
                WHERE m.IdentificacionEstudiante = ? AND m.Estado = 'ACTIVA'
                ORDER BY m.PeriodoId, m.MatriculaId
                """, (fila, numero) -> new Intento(fila.getInt("MatriculaId"),
                fila.getInt("CursoId"), fila.getInt("GrupoId"), fila.getString("Nombre")), identificacion);
    }

    public record Intento(Integer matriculaId, Integer cursoId, Integer grupoId, String nombreCurso) {}
}
