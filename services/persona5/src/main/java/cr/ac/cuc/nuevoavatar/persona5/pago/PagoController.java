package cr.ac.cuc.nuevoavatar.persona5.pago;

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
@RequestMapping("/pago")
public class PagoController {

    private final PagoService servicio;

    public PagoController(PagoService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<PagoResponse> crear(
            @Valid @RequestBody CrearPagoRequest request) {
        PagoResponse creado = servicio.crear(request);
        return ResponseEntity.created(URI.create("/pago/" + creado.id())).body(creado);
    }

    @PutMapping("/{id}/reversar")
    public PagoResponse reversar(@PathVariable Integer id) {
        return servicio.reversar(id);
    }

    @GetMapping("/{id}")
    public PagoResponse obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @GetMapping
    public List<PagoResponse> listarPorPeriodo(
            @RequestParam @Positive Integer periodoId) {
        return servicio.listarPorPeriodo(periodoId);
    }
}
