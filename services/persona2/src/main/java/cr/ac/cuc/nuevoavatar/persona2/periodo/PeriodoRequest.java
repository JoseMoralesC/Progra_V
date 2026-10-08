package cr.ac.cuc.nuevoavatar.persona2.periodo;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PeriodoRequest(
    @NotNull @Min(2000) @Max(9999) Integer anio,
    @NotNull @Min(1) @Max(127) Integer numeroPeriodo,
    @NotNull LocalDate fechaInicio,
    @NotNull LocalDate fechaFin
) {
}
