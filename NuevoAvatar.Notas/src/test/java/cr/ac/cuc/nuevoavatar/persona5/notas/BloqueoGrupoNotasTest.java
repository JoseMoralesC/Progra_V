package cr.ac.cuc.nuevoavatar.persona5.notas;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.server.ResponseStatusException;

class BloqueoGrupoNotasTest {
    @ParameterizedTest @ValueSource(ints = {0, 1, -1, -2, -3, -999})
    void exigeBloqueoExclusivoTransaccionalYRechazaResultadosNegativos(int resultado) throws Exception {
        DataSource ds = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        CallableStatement statement = mock(CallableStatement.class);
        when(ds.getConnection()).thenReturn(connection);
        when(connection.prepareCall(anyString())).thenReturn(statement);
        when(statement.getInt(1)).thenReturn(resultado);
        var bloqueo = new BloqueoGrupoNotas(new JdbcTemplate(ds));
        TransactionSynchronizationManager.setActualTransactionActive(true);
        try {
            if (resultado >= 0) assertDoesNotThrow(() -> bloqueo.adquirir(7));
            else assertEquals(409, assertThrows(ResponseStatusException.class, () -> bloqueo.adquirir(7)).getStatusCode().value());
            verify(statement).setString(2, "NuevoAvatar.Notas.grupo:7");
            verify(statement).setString(3, "Exclusive");
            verify(statement).setString(4, "Transaction");
            verify(statement).setInt(5, 5000);
            verify(statement).close();
        } finally {
            TransactionSynchronizationManager.setActualTransactionActive(false);
        }
    }

    @Test void fueraDeTransaccionNoIntentaEscribir() {
        var jdbc = mock(JdbcTemplate.class);
        assertThrows(IllegalStateException.class, () -> new BloqueoGrupoNotas(jdbc).adquirir(7));
        verifyNoInteractions(jdbc);
    }

    @Test void conexionEnAutocommitNoPuedeProtegerLaOperacion() throws Exception {
        DataSource ds = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        when(ds.getConnection()).thenReturn(connection);
        when(connection.getAutoCommit()).thenReturn(true);
        TransactionSynchronizationManager.setActualTransactionActive(true);
        try {
            assertThrows(IllegalStateException.class, () -> new BloqueoGrupoNotas(new JdbcTemplate(ds)).adquirir(7));
            verify(connection, never()).prepareCall(anyString());
        } finally { TransactionSynchronizationManager.setActualTransactionActive(false); }
    }
}
