package cr.ac.cuc.nuevoavatar.persona2.periodo;

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
@RequestMapping("/periodo")
public class PeriodoController {

    private final PeriodoService servicio;

    public PeriodoController(PeriodoService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<PeriodoResponse> crear(
            @Valid @RequestBody PeriodoRequest request) {
        PeriodoResponse creado = servicio.crear(request);
        return ResponseEntity.created(URI.create("/periodo/" + creado.id())).body(creado);
    }

    @GetMapping
    public List<PeriodoResponse> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public PeriodoResponse obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PutMapping("/{id}")
    public PeriodoResponse modificar(
            @PathVariable Integer id,
            @Valid @RequestBody PeriodoRequest request) {
        return servicio.modificar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
