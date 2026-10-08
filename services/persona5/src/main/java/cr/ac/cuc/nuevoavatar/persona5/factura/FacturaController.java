package cr.ac.cuc.nuevoavatar.persona5.factura;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/factura")
public class FacturaController {

    private final FacturaService servicio;

    public FacturaController(FacturaService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<FacturaResponse> crear(
            @Valid @RequestBody CrearFacturaRequest request) {
        FacturaResponse creada = servicio.crear(request);
        return ResponseEntity.created(URI.create("/factura/" + creada.id())).body(creada);
    }

    @PutMapping("/{id}/reversar")
    public FacturaResponse reversar(@PathVariable Integer id) {
        return servicio.reversar(id);
    }

    @GetMapping("/{id}")
    public FacturaResponse obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @GetMapping
    public List<FacturaResponse> listarPorPeriodo(
            @RequestParam @Positive Integer periodoId) {
        return servicio.listarPorPeriodo(periodoId);
    }
}
