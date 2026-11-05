package cr.ac.cuc.nuevoavatar.persona5.notas;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cr.ac.cuc.nuevoavatar.persona5.seguridad.AccesoNotas;

@RestController
public class NotasController {

    private final NotasService servicio;
    private final AccesoNotas acceso;

    public NotasController(NotasService servicio, AccesoNotas acceso) {
        this.servicio = servicio;
        this.acceso = acceso;
    }

    @PostMapping("/cargardesglose")
    public List<RubroResponse> cargarDesglose(
            @Valid @RequestBody CargarDesgloseRequest request) {
        acceso.grupo(request.grupoId());
        return servicio.cargarDesglose(request);
    }

    @PostMapping("/asignarnotarubro")
    public NotaResponse asignarNota(
            @Valid @RequestBody AsignarNotaRequest request) {
        acceso.matricula(request.matriculaId());
        return servicio.asignarNota(request, false);
    }

    @PutMapping("/asignarnotarubro")
    public NotaResponse modificarNota(
            @Valid @RequestBody AsignarNotaRequest request) {
        acceso.matricula(request.matriculaId());
        return servicio.asignarNota(request, true);
    }

    @GetMapping("/obtenerdesglose")
    public List<RubroResponse> obtenerDesglose(
            @RequestParam @Positive Integer grupoId) {
        acceso.grupo(grupoId);
        return servicio.obtenerDesglose(grupoId);
    }

    @GetMapping("/obtenernotas")
    public List<NotaResponse> obtenerNotas(
            @RequestParam String identificacionEstudiante,
            @RequestParam @Positive Integer cursoId,
            @RequestParam @Positive Integer grupoId) {
        acceso.consulta(cursoId, grupoId);
        return servicio.obtenerNotas(identificacionEstudiante, cursoId, grupoId);
    }

    @GetMapping("/obtenernotasgrupo")
    public List<NotasEstudianteResponse> obtenerNotasGrupo(@RequestParam @Positive Integer grupoId) {
        acceso.grupo(grupoId);
        return servicio.obtenerNotasGrupo(grupoId);
    }
}

