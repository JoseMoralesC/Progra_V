package cr.ac.cuc.nuevoavatar.persona4.prematricula;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import static cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptor.TOKEN_ATTRIBUTE;
import static cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptor.USER_ATTRIBUTE;

@RestController
@RequestMapping("/prematricula")
public class PrematriculaController {
    private final PrematriculaService servicio;

    public PrematriculaController(PrematriculaService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<PrematriculaResponse> crear(@Valid @RequestBody PrematriculaRequest datos,
            @RequestAttribute(TOKEN_ATTRIBUTE) String token, @RequestAttribute(USER_ATTRIBUTE) String usuario) {
        var creada = servicio.crear(datos, token, usuario);
        var ubicacion = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(creada.id()).toUri();
        return ResponseEntity.created(ubicacion).body(creada);
    }

    @PutMapping("/{id}")
    public PrematriculaResponse modificar(@PathVariable Integer id, @Valid @RequestBody PrematriculaRequest datos,
            @RequestAttribute(TOKEN_ATTRIBUTE) String token, @RequestAttribute(USER_ATTRIBUTE) String usuario) {
        return servicio.modificar(id, datos, token, usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id,
            @RequestAttribute(TOKEN_ATTRIBUTE) String token, @RequestAttribute(USER_ATTRIBUTE) String usuario) {
        servicio.eliminar(id, token, usuario);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<PrematriculaResponse> obtenerTodos(@RequestAttribute(TOKEN_ATTRIBUTE) String token,
            @RequestAttribute(USER_ATTRIBUTE) String usuario) {
        return servicio.obtenerTodos(token, usuario);
    }

    @GetMapping("/{id}")
    public PrematriculaResponse obtener(@PathVariable Integer id,
            @RequestAttribute(TOKEN_ATTRIBUTE) String token, @RequestAttribute(USER_ATTRIBUTE) String usuario) {
        return servicio.obtener(id, token, usuario);
    }
}
