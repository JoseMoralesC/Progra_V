package cr.ac.cuc.nuevoavatar.persona2.curso;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/curso")
public class CursoController {

    private final CursoService servicio;

    public CursoController(CursoService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<CursoResponse> crear(
            @Valid @RequestBody CursoRequest request) {
        CursoResponse creado = servicio.crear(request);
        return ResponseEntity.created(URI.create("/curso/" + creado.id())).body(creado);
    }

    @GetMapping
    public List<CursoResponse> listar(
            @RequestParam(required = false) Integer carreraId) {
        return servicio.listar(carreraId);
    }

    @GetMapping("/{id}")
    public CursoResponse obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PutMapping("/{id}")
    public CursoResponse modificar(
            @PathVariable Integer id,
            @Valid @RequestBody CursoRequest request) {
        return servicio.modificar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
