package cr.ac.cuc.nuevoavatar.persona5.notas;

import java.sql.Types;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.server.ResponseStatusException;

/** Bloqueo SQL compartido por todas las escrituras de un grupo, hasta commit/rollback. */
@Component
public class BloqueoGrupoNotas {
    private final JdbcTemplate jdbc;

    public BloqueoGrupoNotas(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public void adquirir(Integer grupoId) {
        if (grupoId == null || grupoId <= 0) throw new IllegalArgumentException("Grupo inválido");
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("El bloqueo de notas requiere una transacción activa");
        }
        Integer resultado = jdbc.execute((ConnectionCallback<Integer>) connection -> {
            if (connection.getAutoCommit()) throw new IllegalStateException("La conexión debe participar en la transacción");
            // JDBC puede estar en modo manual sin haber iniciado aún una transacción
            // en SQL Server. La lectura vacía materializa la transacción implícita
            // sin leer rubros ni anidar BEGIN TRANSACTION; Spring conserva su cierre.
            try (var inicio = connection.createStatement()) {
                inicio.execute("SELECT TOP (0) RubroId FROM matricula.DesgloseRubro");
            }
            try (var llamada = connection.prepareCall("{? = call sys.sp_getapplock(?, ?, ?, ?)}")) {
                llamada.registerOutParameter(1, Types.INTEGER);
                llamada.setString(2, "NuevoAvatar.Notas.grupo:" + grupoId);
                llamada.setString(3, "Exclusive");
                llamada.setString(4, "Transaction");
                llamada.setInt(5, 5000);
                llamada.execute();
                return llamada.getInt(1);
            }
        });
        // 0: adquirido inmediatamente; 1: adquirido después de esperar.
        if (resultado == null || resultado < 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "El grupo está siendo modificado. Vuelva a consultar antes de reintentar.");
        }
    }
}
