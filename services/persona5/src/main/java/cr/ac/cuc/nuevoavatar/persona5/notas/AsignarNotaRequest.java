package cr.ac.cuc.nuevoavatar.persona5.notas;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AsignarNotaRequest(
    @NotNull @Positive Integer matriculaId,
    @NotNull @Positive Integer rubroId,
    @NotNull @DecimalMin("1.00") @DecimalMax("100.00") BigDecimal nota
) {
}
