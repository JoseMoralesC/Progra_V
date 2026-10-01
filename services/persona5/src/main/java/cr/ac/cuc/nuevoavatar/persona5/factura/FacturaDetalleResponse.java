package cr.ac.cuc.nuevoavatar.persona5.factura;

import java.math.BigDecimal;

public record FacturaDetalleResponse(
    Integer id,
    String descripcion,
    BigDecimal monto
) {
    public static FacturaDetalleResponse desde(FacturaDetalle detalle) {
        return new FacturaDetalleResponse(
            detalle.getId(),
            detalle.getDescripcion(),
            detalle.getMonto()
        );
    }
}
