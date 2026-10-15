package cr.ac.cuc.nuevoavatar.persona5.factura;
import java.util.*;
import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
public class OperacionPagoController {
    private final JdbcTemplate jdbc; private final DatosRemotos datos;
    public OperacionPagoController(JdbcTemplate jdbc,DatosRemotos datos){this.jdbc=jdbc;this.datos=datos;}
    @PostMapping("/interno/operaciones-pago/{id}")
    @Transactional
    public Map<String,String> aplicar(@PathVariable String id){
        var op=operacion(id);
        int factura=((Number)op.get("facturaId")).intValue();
        var estados=jdbc.queryForList("SELECT Estado FROM finanzas.Factura WITH(UPDLOCK,HOLDLOCK) WHERE FacturaId=?",String.class,factura);
        if(estados.isEmpty())throw new ResponseStatusException(HttpStatus.CONFLICT,"Factura no existe");
        // Comprobar de nuevo bajo el bloqueo: un reintento antiguo no revierte operaciones posteriores.
        op=operacion(id);
        if(!"PENDIENTE".equals(op.get("estado")))return Map.of("estado",estados.getFirst());
        String expected=op.get("estadoEsperado").toString(),desired=op.get("estadoDestino").toString();
        if(!Set.of("PENDIENTE","PAGADA").contains(desired))throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if(estados.getFirst().equals(desired))return Map.of("estado",desired);
        if(!estados.getFirst().equals(expected))throw new ResponseStatusException(HttpStatus.CONFLICT,"Estado de factura incompatible");
        jdbc.update("UPDATE finanzas.Factura SET Estado=? WHERE FacturaId=?",desired,factura);
        return Map.of("estado",desired);
    }
    @SuppressWarnings("unchecked")
    private Map<String,Object> operacion(String id){
        try{return datos.cliente("Pago").get().uri("/interno/operaciones-pago/{id}",id).retrieve().body(Map.class);}
        catch(org.springframework.web.client.RestClientException ex){throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"No fue posible verificar la operacion de pago",ex);}
    }
}
