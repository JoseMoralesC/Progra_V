package cr.ac.cuc.nuevoavatar.persona2.carrera;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrera")
public class CarreraController {

    private final CarreraService servicio;

    public CarreraController(CarreraService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<CarreraResponse> crear(
            @Valid @RequestBody CarreraRequest request) {
        CarreraResponse creada = servicio.crear(request);
        return ResponseEntity
            .created(URI.create("/carrera/" + creada.id()))
            .body(creada);
    }

    @GetMapping
    public List<CarreraResponse> listar(
            @RequestParam(required = false) Integer institucionId) {
        return servicio.listar(institucionId);
    }

    @GetMapping("/{id}")
    public CarreraResponse obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PutMapping("/{id}")
    public CarreraResponse modificar(
            @PathVariable Integer id,
            @Valid @RequestBody CarreraRequest request) {
        return servicio.modificar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}