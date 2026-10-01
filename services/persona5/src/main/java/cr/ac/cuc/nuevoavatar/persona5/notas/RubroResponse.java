package cr.ac.cuc.nuevoavatar.persona5.notas;

import java.math.BigDecimal;

public record RubroResponse(
    Integer id,
    Integer grupoId,
    String nombre,
    BigDecimal porcentaje
) {
    public static RubroResponse desde(DesgloseRubro rubro) {
        return new RubroResponse(
            rubro.getId(),
            rubro.getGrupoId(),
            rubro.getNombre(),
            rubro.getPorcentaje()
        );
    }
}
