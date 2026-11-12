package cr.ac.cuc.nuevoavatar.persona5.notas;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.Types;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.support.TransactionTemplate;

/** Dos conexiones reales; solo usa un bloqueo temporal, sin cambiar tablas. */
@EnabledIfEnvironmentVariable(named = "NOTAS_SQL_TEST_URL", matches = ".+")
class BloqueoGrupoNotasSqlServerTest {
    @Test
    void primeraSentenciaAdquiereBloqueoHastaRollbackSinTransaccionesAnidadas() throws Exception {
        var ds = new DriverManagerDataSource(System.getenv("NOTAS_SQL_TEST_URL"),
                System.getenv("NOTAS_SQL_TEST_USER"), System.getenv("NOTAS_SQL_TEST_PASSWORD"));
        var jdbc = new JdbcTemplate(ds);
        var bloqueo = new BloqueoGrupoNotas(jdbc);
        var transaccion = new TransactionTemplate(new DataSourceTransactionManager(ds));
        int grupo = ThreadLocalRandom.current().nextInt(1_000_000_000, Integer.MAX_VALUE);
        String recurso = "NuevoAvatar.Notas.grupo:" + grupo;

        try (Connection otra = ds.getConnection()) {
            otra.setAutoCommit(false);
            transaccion.executeWithoutResult(estado -> {
                // Debe funcionar antes de cualquier SELECT/INSERT local.
                bloqueo.adquirir(grupo);
                bloqueo.adquirir(grupo);
                assertEquals(1, jdbc.queryForObject("SELECT @@TRANCOUNT", Integer.class));
                assertEquals("Exclusive", jdbc.queryForObject(
                        "SELECT APPLOCK_MODE('public', ?, 'Transaction')", String.class, recurso));
                assertEquals(-1, intentar(otra, recurso), "Otra conexión debe esperar hasta rollback");
                estado.setRollbackOnly();
            });
            assertTrue(intentar(otra, recurso) >= 0, "Rollback debe liberar el bloqueo");
            otra.rollback();
        }
    }

    private int intentar(Connection conexion, String recurso) {
        try (var inicio = conexion.createStatement();
                var llamada = conexion.prepareCall("{? = call sys.sp_getapplock(?, ?, ?, ?)}")) {
            inicio.execute("SELECT TOP (0) RubroId FROM matricula.DesgloseRubro");
            llamada.registerOutParameter(1, Types.INTEGER);
            llamada.setString(2, recurso);
            llamada.setString(3, "Exclusive");
            llamada.setString(4, "Transaction");
            llamada.setInt(5, 0);
            llamada.execute();
            return llamada.getInt(1);
        } catch (Exception error) {
            throw new IllegalStateException("No se pudo comprobar el bloqueo SQL", error);
        }
    }
}
