package cr.ac.cuc.nuevoavatar.persona4.expediente;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

@Repository
public class ExpedienteRepository {
    private static final String CONSULTA = """
            SELECT e.Identificacion, e.TipoIdentificacion, e.Email, e.NombreCompleto,
                   e.FechaNacimiento, c.ProvinciaId, d.CantonId, e.DistritoId,
                   e.OtrasSenas, t.Telefono
            FROM matricula.Estudiante e
            JOIN matricula.Distrito d ON d.DistritoId = e.DistritoId
            JOIN matricula.Canton c ON c.CantonId = d.CantonId
            LEFT JOIN matricula.EstudianteTelefono t ON t.IdentificacionEstudiante = e.Identificacion
            """;
    private final JdbcTemplate jdbc;

    public ExpedienteRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<DatosExpediente> obtenerTodos() {
        return jdbc.query(CONSULTA + " ORDER BY e.Identificacion, t.EstudianteTelefonoId",
                (ResultSetExtractor<List<DatosExpediente>>) this::leer);
    }

    public Optional<DatosExpediente> obtener(String identificacion) {
        List<DatosExpediente> encontrados = jdbc.query(
                CONSULTA + " WHERE e.Identificacion = ? ORDER BY t.EstudianteTelefonoId",
                (ResultSetExtractor<List<DatosExpediente>>) this::leer, identificacion);
        return encontrados.stream().findFirst();
    }

    public boolean existe(String identificacion) {
        Integer cantidad = jdbc.queryForObject(
                "SELECT COUNT(*) FROM matricula.Estudiante WHERE Identificacion = ?",
                Integer.class, identificacion);
        return cantidad != null && cantidad > 0;
    }

    public void crear(DatosExpediente datos) {
        jdbc.update("""
                INSERT INTO matricula.Estudiante
                    (Identificacion, TipoIdentificacion, Email, NombreCompleto,
                     FechaNacimiento, DistritoId, OtrasSenas)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """, datos.identificacion(), datos.tipoIdentificacion(), datos.email(),
                datos.nombreCompleto(), datos.fechaNacimiento(), datos.distritoId(), datos.otrasSenas());
        guardarTelefonos(datos);
    }

    public int modificar(String identificacionAnterior, DatosExpediente datos) {
        int modificados = jdbc.update("""
                UPDATE matricula.Estudiante SET Identificacion = ?, TipoIdentificacion = ?,
                    Email = ?, NombreCompleto = ?, FechaNacimiento = ?, DistritoId = ?, OtrasSenas = ?
                WHERE Identificacion = ?
                """, datos.identificacion(), datos.tipoIdentificacion(), datos.email(),
                datos.nombreCompleto(), datos.fechaNacimiento(), datos.distritoId(),
                datos.otrasSenas(), identificacionAnterior);
        if (modificados > 0) {
            jdbc.update("DELETE FROM matricula.EstudianteTelefono WHERE IdentificacionEstudiante = ?",
                    datos.identificacion());
            guardarTelefonos(datos);
        }
        return modificados;
    }

    public int eliminar(String identificacion) {
        return jdbc.update("DELETE FROM matricula.Estudiante WHERE Identificacion = ?", identificacion);
    }

    private void guardarTelefonos(DatosExpediente datos) {
        for (String telefono : datos.telefonos()) {
            jdbc.update("INSERT INTO matricula.EstudianteTelefono (IdentificacionEstudiante, Telefono) VALUES (?, ?)",
                    datos.identificacion(), telefono);
        }
    }

    private List<DatosExpediente> leer(ResultSet filas) throws SQLException {
        var expedientes = new LinkedHashMap<String, DatosExpediente>();
        while (filas.next()) {
            String identificacion = filas.getString("Identificacion");
            DatosExpediente expediente = expedientes.get(identificacion);
            if (expediente == null) {
                expediente = new DatosExpediente(filas.getString("TipoIdentificacion"), identificacion,
                        filas.getString("Email"), filas.getString("NombreCompleto"),
                        filas.getDate("FechaNacimiento").toLocalDate(), filas.getInt("ProvinciaId"),
                        filas.getInt("CantonId"), filas.getInt("DistritoId"), filas.getString("OtrasSenas"),
                        new ArrayList<>());
                expedientes.put(identificacion, expediente);
            }
            String telefono = filas.getString("Telefono");
            if (telefono != null) expediente.telefonos().add(telefono);
        }
        return new ArrayList<>(expedientes.values());
    }
}
