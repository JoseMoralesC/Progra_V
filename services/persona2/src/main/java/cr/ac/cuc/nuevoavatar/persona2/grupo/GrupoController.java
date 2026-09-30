package cr.ac.cuc.nuevoavatar.persona2.grupo;

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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/grupo")
public class GrupoController {

    private final GrupoService servicio;

    public GrupoController(GrupoService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<GrupoResponse> crear(
            @Valid @RequestBody GrupoRequest request) {
        GrupoResponse creado = servicio.crear(request);
        return ResponseEntity.created(URI.create("/grupo/" + creado.id())).body(creado);
    }

    @GetMapping
    public List<GrupoResponse> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public GrupoResponse obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PutMapping("/{id}")
    public GrupoResponse modificar(
            @PathVariable Integer id,
            @Valid @RequestBody GrupoRequest request) {
        return servicio.modificar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
