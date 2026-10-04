package cr.ac.cuc.nuevoavatar.persona5.matricula;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/matricula")
public class MatriculaController {

    private final MatriculaService servicio;

    public MatriculaController(MatriculaService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<MatriculaResponse> crear(
            @Valid @RequestBody MatriculaRequest request) {
        MatriculaResponse creada = servicio.crear(request);
        return ResponseEntity.created(URI.create("/matricula/" + creada.id())).body(creada);
    }

    @PutMapping("/{id}")
    public MatriculaResponse modificar(
            @PathVariable Integer id,
            @Valid @RequestBody MatriculaRequest request) {
        return servicio.modificar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<MatriculaResponse> listarPorCursoYGrupo(
            @RequestParam @Positive Integer cursoId,
            @RequestParam @Positive Integer grupoId) {
        return servicio.listarPorCursoYGrupo(cursoId, grupoId);
    }
}
