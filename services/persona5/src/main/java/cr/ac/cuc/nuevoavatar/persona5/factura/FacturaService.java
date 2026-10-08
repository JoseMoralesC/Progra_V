package cr.ac.cuc.nuevoavatar.persona5.factura;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import cr.ac.cuc.nuevoavatar.persona5.matricula.Matricula;
import cr.ac.cuc.nuevoavatar.persona5.matricula.MatriculaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FacturaService {

    private static final BigDecimal COSTO_CURSO = new BigDecimal("30000.00");
    private static final BigDecimal IMPUESTO = new BigDecimal("0.02");
    private static final String DETALLE = "Servicios estudiantiles";

    private final FacturaRepository facturas;
    private final FacturaDetalleRepository detalles;
    private final MatriculaRepository matriculas;

    public FacturaService(
            FacturaRepository facturas,
            FacturaDetalleRepository detalles,
            MatriculaRepository matriculas) {
        this.facturas = facturas;
        this.detalles = detalles;
        this.matriculas = matriculas;
    }

    @Transactional
    public FacturaResponse crear(CrearFacturaRequest request) {
        Matricula matricula = matriculas.findById(request.matriculaId()).orElseThrow(() ->
            invalido("La matricula no existe")
        );
        if (!Matricula.ACTIVA.equals(matricula.getEstado())) {
            throw invalido("La matricula no esta activa");
        }
        if (facturas.existsByMatriculaIdAndEstadoNot(
                request.matriculaId(),
                Factura.ANULADA)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Ya existe una factura activa para esta matricula"
            );
        }

        Factura factura = new Factura();
        factura.setMatriculaId(matricula.getId());
        factura.setIdentificacionEstudiante(matricula.getIdentificacionEstudiante());
        factura.setPeriodoId(matricula.getPeriodoId());
        factura.setSubtotal(COSTO_CURSO);
        factura.setImpuesto(COSTO_CURSO.multiply(IMPUESTO).setScale(2, RoundingMode.HALF_UP));
        factura.setTotal(factura.getSubtotal().add(factura.getImpuesto()));
        factura.setEstado(Factura.PENDIENTE);
        factura.setFechaFactura(LocalDateTime.now());

        Factura guardada = facturas.saveAndFlush(factura);
        FacturaDetalle detalle = new FacturaDetalle();
        detalle.setFacturaId(guardada.getId());
        detalle.setDescripcion(DETALLE);
        detalle.setMonto(COSTO_CURSO);

        return FacturaResponse.desde(
            guardada,
            List.of(detalles.saveAndFlush(detalle))
        );
    }

    @Transactional
    public FacturaResponse reversar(Integer id) {
        Factura factura = buscarO404(id);
        if (Factura.PAGADA.equals(factura.getEstado())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "No se puede reversar una factura pagada"
            );
        }
        factura.setEstado(Factura.ANULADA);
        Factura guardada = facturas.saveAndFlush(factura);
        return respuesta(guardada);
    }

    @Transactional(readOnly = true)
    public FacturaResponse obtener(Integer id) {
        return respuesta(buscarO404(id));
    }

    @Transactional(readOnly = true)
    public List<FacturaResponse> listarPorPeriodo(Integer periodoId) {
        return facturas.findByPeriodoId(periodoId)
            .stream()
            .map(this::respuesta)
            .toList();
    }

    private Factura buscarO404(Integer id) {
        return facturas.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada")
        );
    }

    private FacturaResponse respuesta(Factura factura) {
        return FacturaResponse.desde(
            factura,
            detalles.findByFacturaId(factura.getId())
        );
    }

    private ResponseStatusException invalido(String mensaje) {
        return new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, mensaje);
    }
}
