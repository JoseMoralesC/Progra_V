package cr.ac.cuc.nuevoavatar.persona5.factura;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record FacturaResponse(
    Integer id,
    Integer matriculaId,
    String identificacionEstudiante,
    Integer periodoId,
    BigDecimal subtotal,
    BigDecimal impuesto,
    BigDecimal total,
    String estado,
    LocalDateTime fechaFactura,
    List<FacturaDetalleResponse> detalles
) {
    public static FacturaResponse desde(
            Factura factura,
            List<FacturaDetalle> detalles) {
        return new FacturaResponse(
            factura.getId(),
            factura.getMatriculaId(),
            factura.getIdentificacionEstudiante(),
            factura.getPeriodoId(),
            factura.getSubtotal(),
            factura.getImpuesto(),
            factura.getTotal(),
            factura.getEstado(),
            factura.getFechaFactura(),
            detalles.stream().map(FacturaDetalleResponse::desde).toList()
        );
    }
}
