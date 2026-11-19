package cr.ac.cuc.nuevoavatar.persona2;

import java.sql.DriverManager;
import java.util.*;
import org.springframework.core.env.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import cr.ac.cuc.nuevoavatar.integracion.AuditoriaFiable;

/** Prueba optativa con bases temporales; ejecutar con DB_HOST/DB_USERNAME/DB_PASSWORD. */
public final class PruebaAuditoriaTransaccion {
    public static void main(String[] args) throws Exception {
        String usuario = System.getenv("DB_USERNAME"), password = System.getenv("DB_PASSWORD");
        String origen = "jdbc:sqlserver://" + System.getenv("DB_HOST") + ":" + Objects.toString(System.getenv("DB_PORT"), "1433") + ";encrypt=true;trustServerCertificate=true;databaseName=";
        String nombre = "PrograV_PruebaAuditoriaJava_" + UUID.randomUUID().toString().replace("-", "");
        try (var master = DriverManager.getConnection(origen + "master", usuario, password)) {
            try (var cmd = master.createStatement()) { cmd.execute("CREATE DATABASE [" + nombre + "]"); }
            try {
                var dataSource = new DriverManagerDataSource(origen + nombre, usuario, password);
                var jdbc = new JdbcTemplate(dataSource);
                var env = new StandardEnvironment();
                env.getPropertySources().addFirst(new MapPropertySource("prueba", Map.of(
                    "SERVICE_DB_NAME", nombre, "integration.service", "Usuario", "SERVICES_INTERNAL_KEY", "x".repeat(32), "BITACORA_BASE_URL", "http://127.0.0.1:1")));
                var auditoria = new AuditoriaFiable(jdbc, env);
                auditoria.iniciar();
                try {
                    jdbc.execute("CREATE TABLE dbo.negocio(id int PRIMARY KEY,nombre varchar(30)); INSERT dbo.negocio VALUES(1,'Anterior')");
                    var tx = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
                    tx.execute(status -> {
                        jdbc.update("UPDATE dbo.negocio SET nombre='Nuevo' WHERE id=1");
                        auditoria.registrar("prueba", "Modificación de usuario. Resultado: Exitoso");
                        status.setRollbackOnly(); return null;
                    });
                    comprobar("Anterior".equals(jdbc.queryForObject("SELECT nombre FROM dbo.negocio WHERE id=1",String.class)) &&
                        Integer.valueOf(0).equals(jdbc.queryForObject("SELECT COUNT(*) FROM general.auditoria_pendiente",Integer.class)), "Rollback conjunto del cambio y la auditoría Java");
                    tx.execute(status -> {
                        jdbc.update("UPDATE dbo.negocio SET nombre='Nuevo' WHERE id=1");
                        auditoria.registrar("prueba", "Modificación de usuario. Resultado: Exitoso");
                        return null;
                    });
                    comprobar("Nuevo".equals(jdbc.queryForObject("SELECT nombre FROM dbo.negocio WHERE id=1",String.class)) &&
                        Integer.valueOf(1).equals(jdbc.queryForObject("SELECT COUNT(*) FROM general.auditoria_pendiente",Integer.class)), "Commit conjunto con receptor desconectado");
                } finally { auditoria.close(); }
            } finally {
                try (var cmd = master.createStatement()) { cmd.execute("ALTER DATABASE [" + nombre + "] SET SINGLE_USER WITH ROLLBACK IMMEDIATE; DROP DATABASE [" + nombre + "]"); }
            }
        }
    }
    private static void comprobar(boolean condicion, String texto) {
        if (!condicion) throw new IllegalStateException(texto);
        System.out.println("OK: " + texto);
    }
}
