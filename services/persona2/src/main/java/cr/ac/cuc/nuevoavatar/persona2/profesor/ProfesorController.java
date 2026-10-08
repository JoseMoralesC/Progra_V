package cr.ac.cuc.nuevoavatar.persona2.profesor;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profesor")
public class ProfesorController {

    private final ProfesorService servicio;

    public ProfesorController(ProfesorService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<ProfesorResponse> crear(
            @Valid @RequestBody ProfesorRequest request) {

        ProfesorResponse creado = servicio.crear(request);

        return ResponseEntity
            .created(URI.create("/profesor/" + creado.id()))
            .body(creado);
    }

    @GetMapping
    public List<ProfesorResponse> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public ProfesorResponse obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PutMapping("/{id}")
    public ProfesorResponse modificar(
            @PathVariable Integer id,
            @Valid @RequestBody ProfesorRequest request) {

        return servicio.modificar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}