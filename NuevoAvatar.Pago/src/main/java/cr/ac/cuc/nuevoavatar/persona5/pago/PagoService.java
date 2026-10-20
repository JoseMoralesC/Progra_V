package cr.ac.cuc.nuevoavatar.persona5.pago;
import java.time.LocalDateTime;
import java.util.*;
import cr.ac.cuc.nuevoavatar.persona5.factura.Factura;
import cr.ac.cuc.nuevoavatar.persona5.factura.FacturaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.server.ResponseStatusException;
@Service
public class PagoService {
    private final PagoRepository pagos;private final FacturaRepository facturas;
    private final OperacionPagoService operaciones;private final TransactionTemplate tx;
    public PagoService(PagoRepository pagos,FacturaRepository facturas,OperacionPagoService operaciones,org.springframework.transaction.PlatformTransactionManager manager){this.pagos=pagos;this.facturas=facturas;this.operaciones=operaciones;this.tx=new TransactionTemplate(manager);}
    public PagoResponse crear(CrearPagoRequest request){
        var op=tx.execute(status->{
            operaciones.bloquear(request.facturaId());
            var pendiente=operaciones.pendiente(request.facturaId());
            if(pendiente.isPresent()){
                if(!"PAGO".equals(pendiente.get().tipo()))throw conflicto("Hay un reverso pendiente");
                return pendiente.get();
            }
            var factura=facturas.findById(request.facturaId()).orElseThrow(()->invalido("La factura no existe"));
            if(!Factura.PENDIENTE.equals(factura.getEstado()))throw conflicto("Solo se pueden pagar facturas pendientes");
            if(pagos.existsByFacturaIdAndEstado(request.facturaId(),Pago.APLICADO))throw conflicto("Ya existe un pago aplicado");
            var pago=new Pago();pago.setFacturaId(request.facturaId());pago.setPeriodoId(factura.getPeriodoId());pago.setMonto(factura.getTotal());pago.setEstado(Pago.APLICADO);pago.setFechaPago(LocalDateTime.now());
            pago=pagos.saveAndFlush(pago);
            return operaciones.crear(pago.getId(),pago.getFacturaId(),false);
        });
        operaciones.exigirCompletada(op);return obtener(op.pagoId());
    }
    public PagoResponse reversar(Integer id){
        var op=tx.execute(status->{
            var pago=buscar(id);operaciones.bloquear(pago.getFacturaId());
            var pendiente=operaciones.pendiente(pago.getFacturaId());
            if(pendiente.isPresent()){
                if(!"REVERSO".equals(pendiente.get().tipo()) || pendiente.get().pagoId()!=id.intValue())throw conflicto("Hay otra operacion pendiente");
                return pendiente.get();
            }
            if(Pago.ANULADO.equals(pago.getEstado()))throw conflicto("El pago ya esta anulado");
            var factura=facturas.findById(pago.getFacturaId()).orElseThrow(()->invalido("La factura no existe"));
            if(!Factura.PAGADA.equals(factura.getEstado()))throw conflicto("La factura no esta pagada");
            pago.setEstado(Pago.ANULADO);pagos.saveAndFlush(pago);
            return operaciones.crear(pago.getId(),pago.getFacturaId(),true);
        });
        operaciones.exigirCompletada(op);return obtener(id);
    }
    public PagoResponse obtener(Integer id){return PagoResponse.desde(buscar(id));}
    public List<PagoResponse> listarPorPeriodo(Integer periodoId){return pagos.findByPeriodoId(periodoId).stream().map(PagoResponse::desde).toList();}
    private Pago buscar(Integer id){return pagos.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Pago no encontrado"));}
    private ResponseStatusException conflicto(String m){return new ResponseStatusException(HttpStatus.CONFLICT,m);}
    private ResponseStatusException invalido(String m){return new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,m);}
}
