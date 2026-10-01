package cr.ac.cuc.nuevoavatar.persona5.pago;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagoResponse(
    Integer id,
    Integer facturaId,
    Integer periodoId,
    BigDecimal monto,
    String estado,
    LocalDateTime fechaPago
) {
    public static PagoResponse desde(Pago pago) {
        return new PagoResponse(
            pago.getId(),
            pago.getFacturaId(),
            pago.getPeriodoId(),
            pago.getMonto(),
            pago.getEstado(),
            pago.getFechaPago()
        );
    }
}
