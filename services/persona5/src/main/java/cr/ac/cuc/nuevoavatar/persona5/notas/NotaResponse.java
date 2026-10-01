package cr.ac.cuc.nuevoavatar.persona5.notas;

import java.math.BigDecimal;

public record NotaResponse(
    Integer id,
    Integer matriculaId,
    Integer rubroId,
    String rubro,
    BigDecimal porcentaje,
    BigDecimal nota
) {
    public static NotaResponse desde(
            NotaRubro nota,
            DesgloseRubro rubro) {
        return new NotaResponse(
            nota.getId(),
            nota.getMatriculaId(),
            nota.getRubroId(),
            rubro.getNombre(),
            rubro.getPorcentaje(),
            nota.getNota()
        );
    }
}
