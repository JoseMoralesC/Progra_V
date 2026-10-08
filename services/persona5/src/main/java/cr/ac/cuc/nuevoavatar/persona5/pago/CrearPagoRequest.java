package cr.ac.cuc.nuevoavatar.persona5.pago;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CrearPagoRequest(
    @NotNull @Positive Integer facturaId
) {
}
