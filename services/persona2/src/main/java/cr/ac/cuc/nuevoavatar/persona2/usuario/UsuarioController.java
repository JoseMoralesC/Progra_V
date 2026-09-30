package cr.ac.cuc.nuevoavatar.persona2.usuario;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService servicio;

    public UsuarioController(UsuarioService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(
            @Valid @RequestBody CrearUsuarioRequest request) {

        UsuarioResponse creado = servicio.crear(request);

        return ResponseEntity
            .created(URI.create("/usuario/" + creado.email()))
            .body(creado);
    }

    @GetMapping
    public List<UsuarioResponse> listar(
            @RequestParam(required = false) String identificacion,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String tipo) {

        return servicio.listar(identificacion, nombre, tipo);
    }

    @GetMapping("/{email}")
    public UsuarioResponse obtener(@PathVariable String email) {
        return servicio.obtener(email);
    }

    @PutMapping("/{email}")
    public UsuarioResponse modificar(
            @PathVariable String email,
            @Valid @RequestBody ActualizarUsuarioRequest request) {

        return servicio.modificar(email, request);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> eliminar(@PathVariable String email) {
        servicio.eliminar(email);
        return ResponseEntity.noContent().build();
    }
}