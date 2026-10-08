package cr.ac.cuc.nuevoavatar.persona4.academico;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptor.TOKEN_ATTRIBUTE;
import static cr.ac.cuc.nuevoavatar.persona4.seguridad.AutorizacionInterceptor.USER_ATTRIBUTE;

@RestController
public class HistorialAcademicoController {
    private final HistorialAcademicoService servicio;

    public HistorialAcademicoController(HistorialAcademicoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/historialacademico")
    public List<HistorialAcademicoResponse> obtener(@RequestParam String tipoIdentificacion,
            @RequestParam String identificacion, @RequestAttribute(TOKEN_ATTRIBUTE) String token,
            @RequestAttribute(USER_ATTRIBUTE) String usuario) {
        return servicio.obtener(tipoIdentificacion, identificacion, token, usuario);
    }
}
