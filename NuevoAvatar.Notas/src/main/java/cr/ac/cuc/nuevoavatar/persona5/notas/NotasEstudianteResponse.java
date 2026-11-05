package cr.ac.cuc.nuevoavatar.persona5.notas;

import java.util.List;

public record NotasEstudianteResponse(Integer matriculaId, String identificacionEstudiante,
        List<NotaResponse> notas) {
}
