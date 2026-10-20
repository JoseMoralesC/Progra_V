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
    private final OperacionPagoService operaciones = mock(OperacionPagoService.class);
    private final org.springframework.transaction.PlatformTransactionManager manager =
        mock(org.springframework.transaction.PlatformTransactionManager.class);

    @org.junit.jupiter.api.BeforeEach
    void prepararTransaccion() {
        when(manager.getTransaction(any())).thenReturn(new org.springframework.transaction.support.SimpleTransactionStatus());
    }

    @Test
    void creaPagoYConfirmaOperacionRemota() throws Exception {
        PagoRepository pagos = mock(PagoRepository.class);
        FacturaRepository facturas = mock(FacturaRepository.class);
        Factura factura = facturaPendiente();

        when(facturas.findById(1)).thenReturn(Optional.of(factura));
        Pago guardado = new Pago();
        var id = Pago.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(guardado, 5);
        guardado.setFacturaId(1);
        guardado.setMonto(new BigDecimal("30600.00"));
        guardado.setEstado(Pago.APLICADO);
        when(pagos.saveAndFlush(any(Pago.class))).thenReturn(guardado);
        when(pagos.findById(5)).thenReturn(Optional.of(guardado));
        var op = new OperacionPagoService.Operacion("prueba", 5, 1, "PAGO", "PENDIENTE", "PAGADA", "PENDIENTE");
        when(operaciones.crear(5, 1, false)).thenReturn(op);

        PagoService servicio = new PagoService(pagos, facturas, operaciones, manager);

        PagoResponse response = servicio.crear(new CrearPagoRequest(1));

        assertEquals(new BigDecimal("30600.00"), response.monto());
        assertEquals(Pago.APLICADO, response.estado());
        org.mockito.Mockito.verify(operaciones).exigirCompletada(op);
    }

    @Test
    void rechazaFacturaNoPendiente() {
        FacturaRepository facturas = mock(FacturaRepository.class);
        Factura factura = facturaPendiente();
        factura.setEstado(Factura.ANULADA);
        when(facturas.findById(1)).thenReturn(Optional.of(factura));

        PagoService servicio = new PagoService(mock(PagoRepository.class), facturas, operaciones, manager);

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.crear(new CrearPagoRequest(1))
        );

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
    }

    @Test
    void reversaPagoYConfirmaOperacionRemota() throws Exception {
        PagoRepository pagos = mock(PagoRepository.class);
        FacturaRepository facturas = mock(FacturaRepository.class);
        Pago pago = new Pago();
        var id = Pago.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(pago, 5);
        pago.setFacturaId(1);
        pago.setEstado(Pago.APLICADO);
        Factura factura = facturaPendiente();
        factura.setEstado(Factura.PAGADA);

        when(pagos.findById(5)).thenReturn(Optional.of(pago));
        when(facturas.findById(1)).thenReturn(Optional.of(factura));
        when(pagos.saveAndFlush(any(Pago.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        var op = new OperacionPagoService.Operacion("reverso", 5, 1, "REVERSO", "PAGADA", "PENDIENTE", "PENDIENTE");
        when(operaciones.crear(5, 1, true)).thenReturn(op);
        PagoService servicio = new PagoService(pagos, facturas, operaciones, manager);

        PagoResponse response = servicio.reversar(5);

        assertEquals(Pago.ANULADO, response.estado());
        org.mockito.Mockito.verify(operaciones).exigirCompletada(op);
    }

    private Factura facturaPendiente() {
        Factura factura = new Factura();
        factura.setPeriodoId(2);
        factura.setTotal(new BigDecimal("30600.00"));
        factura.setEstado(Factura.PENDIENTE);
        return factura;
    }
}
