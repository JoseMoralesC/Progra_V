package cr.ac.cuc.nuevoavatar.persona4.academico;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptor.TOKEN_ATTRIBUTE;
import static cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptor.USER_ATTRIBUTE;

@RestController
public class ListadoEstudiantesController {
    private final ListadoEstudiantesService servicio;

    public ListadoEstudiantesController(ListadoEstudiantesService servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/listadoestudiantes")
    public List<ListadoEstudianteResponse> obtener(@RequestParam Integer periodoId,
            @RequestAttribute(TOKEN_ATTRIBUTE) String token, @RequestAttribute(USER_ATTRIBUTE) String usuario) {
        return servicio.obtener(periodoId, token, usuario);
    }
}
