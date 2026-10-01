package cr.ac.cuc.nuevoavatar.persona5.notas;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CargarDesgloseRequest(
    @NotNull @Positive Integer grupoId,
    @NotEmpty List<@Valid RubroRequest> rubros
) {
    public record RubroRequest(
        @NotBlank @Size(max = 100) String nombre,
        @NotNull @DecimalMin("0.01") @DecimalMax("100.00") BigDecimal porcentaje
    ) {
    }
}
