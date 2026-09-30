package cr.ac.cuc.nuevoavatar.persona2.periodo;

import java.time.LocalDate;

public record PeriodoResponse(
    Integer id,
    Integer anio,
    Integer numeroPeriodo,
    LocalDate fechaInicio,
    LocalDate fechaFin
) {
    public static PeriodoResponse desde(Periodo periodo) {
        return new PeriodoResponse(
            periodo.getId(),
            periodo.getAnio().intValue(),
            Byte.toUnsignedInt(periodo.getNumeroPeriodo()),
            periodo.getFechaInicio(),
            periodo.getFechaFin()
        );
    }
}
