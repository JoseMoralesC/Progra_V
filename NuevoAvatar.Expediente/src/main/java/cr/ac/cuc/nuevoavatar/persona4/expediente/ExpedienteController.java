package cr.ac.cuc.nuevoavatar.persona4.expediente;

import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper mapper;

    public ExpedienteController(ExpedienteService servicio, ObjectMapper mapper) {
        this.servicio = servicio;
        this.mapper = mapper;
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

    @GetMapping("/propio")
    public DatosExpediente obtenerPropio(@RequestParam java.util.Map<String, String> parametros,
            @RequestAttribute(TOKEN_ATTRIBUTE) String token, @RequestAttribute(USER_ATTRIBUTE) String usuario) {
        if (!parametros.isEmpty()) throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.BAD_REQUEST, "La consulta propia no admite identificadores seleccionables");
        return servicio.obtenerPropio(token, usuario);
    }

    @PutMapping("/propio")
    public DatosExpediente modificarPropio(@RequestBody JsonNode cuerpo,
            @RequestAttribute(TOKEN_ATTRIBUTE) String token, @RequestAttribute(USER_ATTRIBUTE) String usuario) {
        Set<String> permitidos = Set.of("telefonos", "provinciaId", "cantonId", "distritoId", "otrasSenas");
        if (cuerpo == null || !cuerpo.isObject()) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "El cuerpo debe ser un objeto JSON");
        }
        var campos = cuerpo.fieldNames();
        while (campos.hasNext()) {
            if (!permitidos.contains(campos.next())) {
                throw new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.BAD_REQUEST, "El cuerpo contiene campos no editables");
            }
        }
        try {
            ActualizacionExpedientePropio cambios = mapper.treeToValue(cuerpo, ActualizacionExpedientePropio.class);
            return servicio.modificarPropio(cambios, token, usuario);
        } catch (com.fasterxml.jackson.core.JsonProcessingException | IllegalArgumentException ex) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "Los datos editables tienen un formato invalido", ex);
        }
    }
}

