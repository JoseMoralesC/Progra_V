package cr.ac.cuc.nuevoavatar.persona4.prematricula;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class PrematriculaRepository {
    private static final String CONSULTA = """
            SELECT p.PrematriculaId, p.IdentificacionEstudiante, p.CarreraId, p.PeriodoId,
                   p.Observaciones, pc.CursoId
            FROM matricula.Prematricula p
            LEFT JOIN matricula.PrematriculaCurso pc ON pc.PrematriculaId = p.PrematriculaId
            """;
    private final JdbcTemplate jdbc;
    private final cr.ac.cuc.nuevoavatar.integracion.DatosRemotos datos;

    public PrematriculaRepository(JdbcTemplate jdbc, cr.ac.cuc.nuevoavatar.integracion.DatosRemotos datos) {
        this.jdbc = jdbc;
        this.datos = datos;
    }

    public List<PrematriculaResponse> obtenerTodos() {
        return jdbc.query(CONSULTA + " ORDER BY p.PrematriculaId, pc.PrematriculaCursoId",
                (ResultSetExtractor<List<PrematriculaResponse>>) this::leer);
    }

    public Optional<PrematriculaResponse> obtener(Integer id) {
        List<PrematriculaResponse> encontrados = jdbc.query(
                CONSULTA + " WHERE p.PrematriculaId = ? ORDER BY pc.PrematriculaCursoId",
                (ResultSetExtractor<List<PrematriculaResponse>>) this::leer, id);
        return encontrados.stream().findFirst();
    }

    public boolean existeCarrera(Integer id) { return datos.buscar("Carrera","Carrera","CarreraId",id).isPresent(); }
    public Optional<DatosPrematricula.Curso> obtenerCurso(Integer id) {
        return datos.buscar("Curso","Curso","CursoId",id).map(r -> new DatosPrematricula.Curso(id,cr.ac.cuc.nuevoavatar.integracion.DatosRemotos.entero(r,"Nivel")));
    }
    public Optional<DatosPrematricula.Periodo> obtenerPeriodo(Integer id) {
        return datos.buscar("Periodo","Periodo","PeriodoId",id).map(r -> new DatosPrematricula.Periodo(id,cr.ac.cuc.nuevoavatar.integracion.DatosRemotos.fecha(r,"FechaInicio")));
    }

    public Integer crear(PrematriculaRequest datos) {
        var llave = new GeneratedKeyHolder();
        jdbc.update(conexion -> {
            var sentencia = conexion.prepareStatement("""
                    INSERT INTO matricula.Prematricula
                        (IdentificacionEstudiante, CarreraId, PeriodoId, Observaciones)
                    VALUES (?, ?, ?, ?)
                    """, Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, datos.identificacionEstudiante().trim());
            sentencia.setInt(2, datos.carreraId());
            sentencia.setInt(3, datos.periodoId());
            sentencia.setString(4, datos.observaciones());
            return sentencia;
        }, llave);
        Number id = llave.getKey();
        if (id == null) throw new IllegalStateException("No se obtuvo la llave de la prematricula");
        guardarCursos(id.intValue(), datos.cursos());
        return id.intValue();
    }

    public int modificar(Integer id, PrematriculaRequest datos) {
        int cantidad = jdbc.update("""
                UPDATE matricula.Prematricula SET IdentificacionEstudiante = ?,
                    CarreraId = ?, PeriodoId = ?, Observaciones = ? WHERE PrematriculaId = ?
                """, datos.identificacionEstudiante().trim(), datos.carreraId(), datos.periodoId(),
                datos.observaciones(), id);
        if (cantidad > 0) {
            jdbc.update("DELETE FROM matricula.PrematriculaCurso WHERE PrematriculaId = ?", id);
            guardarCursos(id, datos.cursos());
        }
        return cantidad;
    }

    public int eliminar(Integer id) {
        return jdbc.update("DELETE FROM matricula.Prematricula WHERE PrematriculaId = ?", id);
    }

    private void guardarCursos(Integer id, List<Integer> cursos) {
        for (Integer cursoId : cursos) {
            jdbc.update("INSERT INTO matricula.PrematriculaCurso (PrematriculaId, CursoId) VALUES (?, ?)", id, cursoId);
        }
    }

    private List<PrematriculaResponse> leer(ResultSet filas) throws SQLException {
        var prematriculas = new LinkedHashMap<Integer, PrematriculaResponse>();
        while (filas.next()) {
            Integer id = filas.getInt("PrematriculaId");
            var prematricula = prematriculas.get(id);
            if (prematricula == null) {
                prematricula = new PrematriculaResponse(id, filas.getString("IdentificacionEstudiante"),
                        filas.getInt("CarreraId"), new ArrayList<>(), filas.getString("Observaciones"),
                        filas.getInt("PeriodoId"));
                prematriculas.put(id, prematricula);
            }
            Integer cursoId = filas.getObject("CursoId", Integer.class);
            if (cursoId != null) prematricula.cursos().add(cursoId);
        }
        return new ArrayList<>(prematriculas.values());
    }
}

