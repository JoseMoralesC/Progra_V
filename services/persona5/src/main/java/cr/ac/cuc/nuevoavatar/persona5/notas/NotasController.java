package cr.ac.cuc.nuevoavatar.persona5.notas;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class NotasController {

    private final NotasService servicio;

    public NotasController(NotasService servicio) {
        this.servicio = servicio;
    }

    @PostMapping("/cargardesglose")
    public List<RubroResponse> cargarDesglose(
            @Valid @RequestBody CargarDesgloseRequest request) {
        return servicio.cargarDesglose(request);
    }

    @PostMapping("/asignarnotarubro")
    public NotaResponse asignarNota(
            @Valid @RequestBody AsignarNotaRequest request) {
        return servicio.asignarNota(request);
    }

    @PutMapping("/asignarnotarubro")
    public NotaResponse modificarNota(
            @Valid @RequestBody AsignarNotaRequest request) {
        return servicio.asignarNota(request);
    }

    @GetMapping("/obtenerdesglose")
    public List<RubroResponse> obtenerDesglose(
            @RequestParam @Positive Integer grupoId) {
        return servicio.obtenerDesglose(grupoId);
    }

    @GetMapping("/obtenernotas")
    public List<NotaResponse> obtenerNotas(
            @RequestParam String identificacionEstudiante,
            @RequestParam @Positive Integer cursoId,
            @RequestParam @Positive Integer grupoId) {
        return servicio.obtenerNotas(identificacionEstudiante, cursoId, grupoId);
    }
}
