package cr.ac.cuc.nuevoavatar.persona5.pago;

import java.time.LocalDateTime;
import java.util.List;

import cr.ac.cuc.nuevoavatar.persona5.factura.Factura;
import cr.ac.cuc.nuevoavatar.persona5.factura.FacturaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PagoService {

    private final PagoRepository pagos;
    private final FacturaRepository facturas;

    public PagoService(
            PagoRepository pagos,
            FacturaRepository facturas) {
        this.pagos = pagos;
        this.facturas = facturas;
    }

    @Transactional
    public PagoResponse crear(CrearPagoRequest request) {
        Factura factura = facturas.findById(request.facturaId()).orElseThrow(() ->
            invalido("La factura no existe")
        );
        if (!Factura.PENDIENTE.equals(factura.getEstado())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Solo se pueden pagar facturas pendientes"
            );
        }
        if (pagos.existsByFacturaIdAndEstado(request.facturaId(), Pago.APLICADO)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Ya existe un pago aplicado para esta factura"
            );
        }

        Pago pago = new Pago();
        pago.setFacturaId(request.facturaId());
        pago.setPeriodoId(factura.getPeriodoId());
        pago.setMonto(factura.getTotal());
        pago.setEstado(Pago.APLICADO);
        pago.setFechaPago(LocalDateTime.now());

        factura.setEstado(Factura.PAGADA);
        facturas.saveAndFlush(factura);

        return PagoResponse.desde(pagos.saveAndFlush(pago));
    }

    @Transactional
    public PagoResponse reversar(Integer id) {
        Pago pago = buscarO404(id);
        if (Pago.ANULADO.equals(pago.getEstado())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "El pago ya esta anulado"
            );
        }

        Factura factura = facturas.findById(pago.getFacturaId()).orElseThrow(() ->
            invalido("La factura no existe")
        );
        pago.setEstado(Pago.ANULADO);
        factura.setEstado(Factura.PENDIENTE);
        facturas.saveAndFlush(factura);
        return PagoResponse.desde(pagos.saveAndFlush(pago));
    }

    @Transactional(readOnly = true)
    public PagoResponse obtener(Integer id) {
        return PagoResponse.desde(buscarO404(id));
    }

    @Transactional(readOnly = true)
    public List<PagoResponse> listarPorPeriodo(Integer periodoId) {
        return pagos.findByPeriodoId(periodoId)
            .stream()
            .map(PagoResponse::desde)
            .toList();
    }

    private Pago buscarO404(Integer id) {
        return pagos.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Pago no encontrado")
        );
    }

    private ResponseStatusException invalido(String mensaje) {
        return new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, mensaje);
    }
}
