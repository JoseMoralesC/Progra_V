package cr.ac.cuc.nuevoavatar.persona5.pago;

import java.sql.*;
import java.util.*;
import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
import org.springframework.jdbc.core.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** Outbox en la misma transaccion que Pago; reintento seguro tras reinicios. */
@Service
public class OperacionPagoService {
    private final JdbcTemplate jdbc; private final DatosRemotos datos; private final TransactionTemplate tx;
    public OperacionPagoService(JdbcTemplate jdbc,DatosRemotos datos,org.springframework.transaction.PlatformTransactionManager manager){this.jdbc=jdbc;this.datos=datos;this.tx=new TransactionTemplate(manager);}
    public record Operacion(String id,int pagoId,int facturaId,String tipo,String estadoEsperado,String estadoDestino,String estado){}
    private Operacion row(ResultSet r,int n)throws SQLException{return new Operacion(r.getString("OperacionId"),r.getInt("PagoId"),r.getInt("FacturaId"),r.getString("Tipo"),r.getString("EstadoEsperado"),r.getString("EstadoDestino"),r.getString("Estado"));}
    public Operacion obtener(String id){return jdbc.query("SELECT * FROM finanzas.OperacionPago WHERE OperacionId=?",this::row,id).stream().findFirst().orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Operacion no encontrada"));}
    public Optional<Operacion> pendiente(int factura){return jdbc.query("SELECT * FROM finanzas.OperacionPago WHERE FacturaId=? AND Estado='PENDIENTE'",this::row,factura).stream().findFirst();}
    public void bloquear(int factura){
        // La lectura de tabla inicia la transaccion implicita antes del bloqueo.
        Integer r=jdbc.queryForObject("DECLARE @inicio int;SELECT @inicio=COUNT(*) FROM finanzas.Pago WHERE PagoId=-1;DECLARE @r int;EXEC @r=sys.sp_getapplock @Resource=?,@LockMode='Exclusive',@LockOwner='Transaction',@LockTimeout=5000;SELECT @r",Integer.class,"pago:"+factura);
        if(r==null || r<0)throw new ResponseStatusException(HttpStatus.CONFLICT,"La factura tiene otra operacion en curso");
    }
    public Operacion crear(int pago,int factura,boolean reverso){
        String id=UUID.randomUUID().toString();
        jdbc.update("INSERT finanzas.OperacionPago(OperacionId,PagoId,FacturaId,Tipo,EstadoEsperado,EstadoDestino) VALUES(?,?,?,?,?,?)",id,pago,factura,reverso?"REVERSO":"PAGO",reverso?"PAGADA":"PENDIENTE",reverso?"PENDIENTE":"PAGADA");
        return obtener(id);
    }
    public void exigirCompletada(Operacion op){
        procesar(op.id());var actual=obtener(op.id());
        if("FALLIDA".equals(actual.estado()))throw new ResponseStatusException(HttpStatus.CONFLICT,"La factura cambio de estado; la operacion fue compensada");
        if(!"COMPLETADA".equals(actual.estado()))throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"Operacion registrada; la actualizacion de factura se reintentara automaticamente");
    }
    @Scheduled(fixedDelayString="${pago.reintento-ms:3000}")
    public void recuperar(){
        for(String id:jdbc.queryForList("SELECT TOP(25) CONVERT(varchar(36),OperacionId) FROM finanzas.OperacionPago WHERE Estado='PENDIENTE' ORDER BY Creada",String.class)) {
            try{procesar(id);}catch(RuntimeException ignored){/* La fila permanece pendiente para el siguiente intento. */}
        }
    }
    public synchronized void procesar(String id){
        jdbc.execute((ConnectionCallback<Void>)connection->{
            String resource="operacion-pago:"+obtener(id).facturaId();
            try(var lock=connection.prepareStatement("DECLARE @r int;EXEC @r=sys.sp_getapplock @Resource=?,@LockMode='Exclusive',@LockOwner='Session',@LockTimeout=0;SELECT @r")){
                lock.setString(1,resource);try(var result=lock.executeQuery()){result.next();if(result.getInt(1)<0)return null;}
            }
            try {
                var op=obtener(id);if(!"PENDIENTE".equals(op.estado()))return null;
                jdbc.update("UPDATE finanzas.OperacionPago SET Intentos=Intentos+1,Actualizada=sysutcdatetime() WHERE OperacionId=?",id);
                try {
                    datos.cliente("Factura").post().uri("/interno/operaciones-pago/{id}",id).retrieve().toBodilessEntity();
                    jdbc.update("UPDATE finanzas.OperacionPago SET Estado='COMPLETADA',UltimoError=NULL,Actualizada=sysutcdatetime() WHERE OperacionId=? AND Estado='PENDIENTE'",id);
                }catch(RestClientResponseException ex){
                    if(ex.getStatusCode().value()==409){
                        tx.executeWithoutResult(status->{
                            jdbc.update("UPDATE finanzas.Pago SET Estado=? WHERE PagoId=?","PAGO".equals(op.tipo())?"ANULADO":"APLICADO",op.pagoId());
                            jdbc.update("UPDATE finanzas.OperacionPago SET Estado='FALLIDA',UltimoError='Conflicto de estado de factura',Actualizada=sysutcdatetime() WHERE OperacionId=?",id);
                        });
                    } else registrarFallo(id);
                }catch(org.springframework.web.client.RestClientException ex){registrarFallo(id);}
            } finally {
                try(var unlock=connection.prepareStatement("EXEC sys.sp_releaseapplock @Resource=?,@LockOwner='Session'")){unlock.setString(1,resource);unlock.execute();}
            }
            return null;
        });
    }
    private void registrarFallo(String id){jdbc.update("UPDATE finanzas.OperacionPago SET UltimoError='Factura no disponible; reintento pendiente',Actualizada=sysutcdatetime() WHERE OperacionId=?",id);}
}
