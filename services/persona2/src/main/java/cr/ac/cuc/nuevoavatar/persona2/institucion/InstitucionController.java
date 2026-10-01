package cr.ac.cuc.nuevoavatar.persona2.institucion;

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
@RequestMapping("/institucion")
public class InstitucionController {

    private final InstitucionService servicio;

    public InstitucionController(InstitucionService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<InstitucionResponse> crear(
            @Valid @RequestBody InstitucionRequest request) {
        InstitucionResponse creada = servicio.crear(request);
        return ResponseEntity
            .created(URI.create("/institucion/" + creada.id()))
            .body(creada);
    }

    @GetMapping
    public List<InstitucionResponse> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public InstitucionResponse obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PutMapping("/{id}")
    public InstitucionResponse modificar(
            @PathVariable Integer id,
            @Valid @RequestBody InstitucionRequest request) {
        return servicio.modificar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
