package cr.ac.cuc.nuevoavatar.persona5.factura;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CrearFacturaRequest(
    @NotNull @Positive Integer matriculaId
) {
}
