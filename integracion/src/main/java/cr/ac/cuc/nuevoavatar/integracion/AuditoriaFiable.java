package cr.ac.cuc.nuevoavatar.integracion;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/** Encola en la base propia. Cuando existe una transacción Spring, el INSERT participa en ella. */
public final class AuditoriaFiable implements AutoCloseable {
    public static final String ENCOLADA = "auditoria.movimiento.encolado";
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(AuditoriaFiable.class);
    private final JdbcTemplate jdbc;
    private final RestClient receptor;
    private final String clave;
    private final String baseEsperada;
    private final ScheduledExecutorService trabajador = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "entrega-auditoria"); t.setDaemon(true); return t;
    });

    public AuditoriaFiable(JdbcTemplate jdbc, Environment env) {
        this.jdbc = jdbc;
        this.clave = env.getRequiredProperty("SERVICES_INTERNAL_KEY");
        this.baseEsperada = env.getProperty("SERVICE_DB_NAME", "PrograV_" + env.getRequiredProperty("integration.service"));
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000); factory.setReadTimeout(8000);
        this.receptor = RestClient.builder().baseUrl(env.getRequiredProperty("BITACORA_BASE_URL")).requestFactory(factory).build();
    }

    public void iniciar() throws Exception {
        if (!baseEsperada.equals(jdbc.queryForObject("SELECT DB_NAME()", String.class)))
            throw new IllegalStateException("La cola de auditoría no apunta a la base propia del servicio");
        try (var recurso = new ClassPathResource("auditoria-pendiente.sql").getInputStream()) {
            jdbc.execute(new String(recurso.readAllBytes(), StandardCharsets.UTF_8));
        }
        trabajador.scheduleWithFixedDelay(this::procesarSeguro, 1, 5, TimeUnit.SECONDS);
    }

    public void registrar(String usuario, String descripcion) {
        String limpio = descripcion.replaceAll(
            "(?i)(\"(?:password[^\"]*|contrasena[^\"]*|contraseña[^\"]*|confirmacionContrasena|currentPassword|newPassword|confirmPassword|access_token|refresh_token|token|authorization|jwt_key)\"\\s*:\\s*)\"(?:\\\\.|[^\"\\\\])*\"", "$1\"[OCULTO]\"");
        jdbc.update("INSERT INTO general.auditoria_pendiente(id_evento,usuario,descripcion,fecha_hora) VALUES(?,?,?,?)",
            UUID.randomUUID().toString(), usuario, limpio, Timestamp.valueOf(LocalDateTime.now()));
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override public void afterCommit() { trabajador.execute(AuditoriaFiable.this::procesarSeguro); }
            });
        } else trabajador.execute(this::procesarSeguro);
    }

    public static void registrarCambioHttp(jakarta.servlet.http.HttpServletRequest solicitud, String descripcion) {
        Object usuario = solicitud.getAttribute("seguridad.usuario");
        if (usuario == null) throw new IllegalStateException("Falta identidad para auditar el movimiento");
        var contexto = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(solicitud.getServletContext());
        contexto.getBean(AuditoriaFiable.class).registrar(usuario.toString(), descripcion + ". Resultado: Exitoso");
        solicitud.setAttribute(ENCOLADA, Boolean.TRUE);
    }

    public static void registrarMovimientoHttp(String descripcion) {
        var atributos = org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
        if (!(atributos instanceof org.springframework.web.context.request.ServletRequestAttributes solicitud))
            throw new IllegalStateException("Falta contexto para auditar el movimiento");
        registrarCambioHttp(solicitud.getRequest(), descripcion);
    }

    private synchronized void procesarSeguro() {
        try {
            var filas = jdbc.query("SELECT TOP (50) id_evento,usuario,descripcion,fecha_hora FROM general.auditoria_pendiente WHERE proximo_intento<=SYSUTCDATETIME() ORDER BY fecha_hora,id_evento",
                (rs, n) -> Map.<String,Object>of("idEvento", rs.getString(1), "usuario", rs.getString(2),
                    "descripcion", rs.getString(3), "fechaHora", rs.getTimestamp(4).toLocalDateTime().toString()));
            for (var evento : filas) {
                try {
                    var recibo = receptor.post().uri("/interno/bitacora").header("X-Servicio-Key", clave)
                        .contentType(MediaType.APPLICATION_JSON).body(evento).retrieve().body(Map.class);
                    if (recibo == null || !evento.get("idEvento").toString().equalsIgnoreCase(Objects.toString(recibo.get("idEvento"))) ||
                        !(recibo.get("idBitacora") instanceof Number id) || id.longValue() <= 0)
                        throw new IllegalStateException("Confirmación de auditoría inválida");
                    jdbc.update("DELETE FROM general.auditoria_pendiente WHERE id_evento=?", evento.get("idEvento"));
                } catch (Exception error) {
                    jdbc.update("UPDATE general.auditoria_pendiente SET intentos=intentos+1,proximo_intento=DATEADD(second,10,SYSUTCDATETIME()) WHERE id_evento=?", evento.get("idEvento"));
                    LOG.warn("Auditoría {} pendiente de entrega; se reintentará", evento.get("idEvento"));
                    break;
                }
            }
        } catch (Exception error) { LOG.error("No se pudo procesar la cola persistente de auditoría"); }
    }

    @Override public void close() { trabajador.shutdownNow(); }
}
