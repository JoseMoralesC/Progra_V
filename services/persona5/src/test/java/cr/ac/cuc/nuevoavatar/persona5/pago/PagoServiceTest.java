package cr.ac.cuc.nuevoavatar.persona5.pago;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import cr.ac.cuc.nuevoavatar.persona5.factura.Factura;
import cr.ac.cuc.nuevoavatar.persona5.factura.FacturaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class PagoServiceTest {

    @Test
    void creaPagoYMarcaFacturaPagada() {
        PagoRepository pagos = mock(PagoRepository.class);
        FacturaRepository facturas = mock(FacturaRepository.class);
        Factura factura = facturaPendiente();

        when(facturas.findById(1)).thenReturn(Optional.of(factura));
        when(pagos.saveAndFlush(any(Pago.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        PagoService servicio = new PagoService(pagos, facturas);

        PagoResponse response = servicio.crear(new CrearPagoRequest(1));

        assertEquals(new BigDecimal("30600.00"), response.monto());
        assertEquals(Pago.APLICADO, response.estado());
        assertEquals(Factura.PAGADA, factura.getEstado());
    }

    @Test
    void rechazaFacturaNoPendiente() {
        FacturaRepository facturas = mock(FacturaRepository.class);
        Factura factura = facturaPendiente();
        factura.setEstado(Factura.ANULADA);
        when(facturas.findById(1)).thenReturn(Optional.of(factura));

        PagoService servicio = new PagoService(mock(PagoRepository.class), facturas);

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.crear(new CrearPagoRequest(1))
        );

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
    }

    @Test
    void reversaPagoYDevuelveFacturaAPendiente() {
        PagoRepository pagos = mock(PagoRepository.class);
        FacturaRepository facturas = mock(FacturaRepository.class);
        Pago pago = new Pago();
        pago.setFacturaId(1);
        pago.setEstado(Pago.APLICADO);
        Factura factura = facturaPendiente();
        factura.setEstado(Factura.PAGADA);

        when(pagos.findById(5)).thenReturn(Optional.of(pago));
        when(facturas.findById(1)).thenReturn(Optional.of(factura));
        when(pagos.saveAndFlush(any(Pago.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        PagoService servicio = new PagoService(pagos, facturas);

        PagoResponse response = servicio.reversar(5);

        assertEquals(Pago.ANULADO, response.estado());
        assertEquals(Factura.PENDIENTE, factura.getEstado());
    }

    private Factura facturaPendiente() {
        Factura factura = new Factura();
        factura.setPeriodoId(2);
        factura.setTotal(new BigDecimal("30600.00"));
        factura.setEstado(Factura.PENDIENTE);
        return factura;
    }
}
