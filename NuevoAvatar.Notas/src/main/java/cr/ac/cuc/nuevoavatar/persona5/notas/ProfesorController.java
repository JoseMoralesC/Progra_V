package cr.ac.cuc.nuevoavatar.persona5.notas;

import cr.ac.cuc.nuevoavatar.persona5.seguridad.AccesoNotas;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfesorController {
    private final AccesoNotas acceso;
    private final ResumenProfesorService resumen;

    public ProfesorController(AccesoNotas acceso, ResumenProfesorService resumen) {
        this.acceso = acceso;
        this.resumen = resumen;
    }

    @GetMapping("/profesor/grupos")
    public List<ResumenProfesorService.GrupoProfesorResponse> grupos(
            @RequestParam(required = false) @Positive Integer periodoId) {
        return resumen.obtener(acceso.profesor().profesorId(), periodoId);
    }
}
