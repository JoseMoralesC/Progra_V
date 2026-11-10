package cr.ac.cuc.nuevoavatar.persona4.prematricula;

import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import static cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptor.TOKEN_ATTRIBUTE;
import static cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptor.USER_ATTRIBUTE;

@RestController
@RequestMapping("/prematricula")
public class PrematriculaController {
    private final PrematriculaService servicio;
    private final ObjectMapper mapper;

    public PrematriculaController(PrematriculaService servicio, ObjectMapper mapper) {
        this.servicio = servicio;
        this.mapper = mapper;
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

    @GetMapping("/propio/opciones")
    public OpcionesPrematriculaResponse opcionesPropias(
            @RequestAttribute(TOKEN_ATTRIBUTE) String token, @RequestAttribute(USER_ATTRIBUTE) String usuario) {
        return servicio.opcionesPropias(token, usuario);
    }

    @GetMapping("/propio")
    public List<PrematriculaResponse> obtenerPropias(
            @RequestAttribute(TOKEN_ATTRIBUTE) String token, @RequestAttribute(USER_ATTRIBUTE) String usuario) {
        return servicio.obtenerPropias(token, usuario);
    }

    @GetMapping("/propio/{id}")
    public PrematriculaResponse obtenerPropia(@PathVariable Integer id,
            @RequestAttribute(TOKEN_ATTRIBUTE) String token, @RequestAttribute(USER_ATTRIBUTE) String usuario) {
        return servicio.obtenerPropia(id, token, usuario);
    }

    @PostMapping("/propio")
    public ResponseEntity<PrematriculaResponse> crearPropia(@RequestBody JsonNode cuerpo,
            @RequestAttribute(TOKEN_ATTRIBUTE) String token, @RequestAttribute(USER_ATTRIBUTE) String usuario) {
        var creada = servicio.crearPropia(cambios(cuerpo), token, usuario);
        var ubicacion = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(creada.id()).toUri();
        return ResponseEntity.created(ubicacion).body(creada);
    }

    @PutMapping("/propio/{id}")
    public PrematriculaResponse modificarPropia(@PathVariable Integer id, @RequestBody JsonNode cuerpo,
            @RequestAttribute(TOKEN_ATTRIBUTE) String token, @RequestAttribute(USER_ATTRIBUTE) String usuario) {
        return servicio.modificarPropia(id, cambios(cuerpo), token, usuario);
    }

    @DeleteMapping("/propio/{id}")
    public ResponseEntity<Void> cancelarPropia(@PathVariable Integer id,
            @RequestAttribute(TOKEN_ATTRIBUTE) String token, @RequestAttribute(USER_ATTRIBUTE) String usuario) {
        servicio.cancelarPropia(id, token, usuario);
        return ResponseEntity.noContent().build();
    }

    private PrematriculaPropiaRequest cambios(JsonNode cuerpo) {
        Set<String> permitidos = Set.of("carreraId", "cursos", "observaciones", "periodoId");
        if (cuerpo == null || !cuerpo.isObject()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cuerpo debe ser un objeto JSON");
        }
        var campos = cuerpo.fieldNames();
        while (campos.hasNext()) {
            if (!permitidos.contains(campos.next())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cuerpo contiene campos no permitidos");
            }
        }
        try {
            return mapper.treeToValue(cuerpo, PrematriculaPropiaRequest.class);
        } catch (JsonProcessingException | IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los datos de prematricula tienen un formato invalido", ex);
        }
    }
}

