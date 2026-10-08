package cr.ac.cuc.nuevoavatar.persona5.factura;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import cr.ac.cuc.nuevoavatar.persona5.matricula.Matricula;
import cr.ac.cuc.nuevoavatar.persona5.matricula.MatriculaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class FacturaServiceTest {

    @Test
    void creaFacturaPendienteConImpuesto() {
        FacturaRepository facturas = mock(FacturaRepository.class);
        FacturaDetalleRepository detalles = mock(FacturaDetalleRepository.class);
        MatriculaRepository matriculas = mock(MatriculaRepository.class);

        when(matriculas.findById(1)).thenReturn(Optional.of(matriculaActiva()));
        when(facturas.saveAndFlush(any(Factura.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(detalles.saveAndFlush(any(FacturaDetalle.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        FacturaService servicio = new FacturaService(facturas, detalles, matriculas);

        FacturaResponse response = servicio.crear(new CrearFacturaRequest(1));

        assertEquals(new BigDecimal("30000.00"), response.subtotal());
        assertEquals(new BigDecimal("600.00"), response.impuesto());
        assertEquals(new BigDecimal("30600.00"), response.total());
        assertEquals(Factura.PENDIENTE, response.estado());
        assertEquals("Servicios estudiantiles", response.detalles().get(0).descripcion());
    }

    @Test
    void rechazaMatriculaInactiva() {
        MatriculaRepository matriculas = mock(MatriculaRepository.class);
        Matricula matricula = matriculaActiva();
        matricula.setEstado(Matricula.ANULADA);
        when(matriculas.findById(1)).thenReturn(Optional.of(matricula));

        FacturaService servicio = new FacturaService(
            mock(FacturaRepository.class),
            mock(FacturaDetalleRepository.class),
            matriculas
        );

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.crear(new CrearFacturaRequest(1))
        );

        assertEquals(HttpStatus.UNPROCESSABLE_CONTENT, error.getStatusCode());
    }

    @Test
    void noReversaFacturaPagada() {
        FacturaRepository facturas = mock(FacturaRepository.class);
        Factura factura = new Factura();
        factura.setEstado(Factura.PAGADA);
        when(facturas.findById(1)).thenReturn(Optional.of(factura));

        FacturaService servicio = new FacturaService(
            facturas,
            mock(FacturaDetalleRepository.class),
            mock(MatriculaRepository.class)
        );

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.reversar(1)
        );

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
    }

    private Matricula matriculaActiva() {
        Matricula matricula = new Matricula();
        matricula.setIdentificacionEstudiante("1-111");
        matricula.setPeriodoId(2);
        matricula.setEstado(Matricula.ACTIVA);
        return matricula;
    }
}
