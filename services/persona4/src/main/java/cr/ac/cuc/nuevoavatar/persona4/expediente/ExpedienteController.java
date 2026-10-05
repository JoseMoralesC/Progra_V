package cr.ac.cuc.nuevoavatar.persona4.expediente;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import static cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptor.TOKEN_ATTRIBUTE;
import static cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptor.USER_ATTRIBUTE;

@RestController
@RequestMapping("/expediente")
public class ExpedienteController {
    private final ExpedienteService servicio;

    public ExpedienteController(ExpedienteService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<DatosExpediente> crear(@Valid @RequestBody DatosExpediente datos,
            @RequestAttribute(TOKEN_ATTRIBUTE) String token, @RequestAttribute(USER_ATTRIBUTE) String usuario) {
        var creado = servicio.crear(datos, token, usuario);
        var ubicacion = ServletUriComponentsBuilder.fromCurrentRequest().path("/{identificacion}")
                .buildAndExpand(creado.identificacion()).encode().toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{identificacion}")
    public DatosExpediente modificar(@PathVariable String identificacion, @Valid @RequestBody DatosExpediente datos,
            @RequestAttribute(TOKEN_ATTRIBUTE) String token, @RequestAttribute(USER_ATTRIBUTE) String usuario) {
        return servicio.modificar(identificacion, datos, token, usuario);
    }

    @DeleteMapping("/{identificacion}")
    public ResponseEntity<Void> eliminar(@PathVariable String identificacion,
            @RequestAttribute(TOKEN_ATTRIBUTE) String token, @RequestAttribute(USER_ATTRIBUTE) String usuario) {
        servicio.eliminar(identificacion, token, usuario);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<DatosExpediente> obtenerTodos(@RequestAttribute(TOKEN_ATTRIBUTE) String token,
            @RequestAttribute(USER_ATTRIBUTE) String usuario) {
        return servicio.obtenerTodos(token, usuario);
    }

    @GetMapping("/{identificacion}")
    public DatosExpediente obtener(@PathVariable String identificacion,
            @RequestAttribute(TOKEN_ATTRIBUTE) String token, @RequestAttribute(USER_ATTRIBUTE) String usuario) {
        return servicio.obtener(identificacion, token, usuario);
    }
}
