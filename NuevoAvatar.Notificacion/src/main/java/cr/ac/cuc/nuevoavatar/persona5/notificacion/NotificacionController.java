package cr.ac.cuc.nuevoavatar.persona5.notificacion;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;import org.springframework.web.bind.annotation.RequestParam;import jakarta.servlet.http.HttpServletRequest;import java.util.Map;import cr.ac.cuc.nuevoavatar.persona5.seguridad.AutorizacionInterceptor;

@RestController
@RequestMapping("/notificar")
public class NotificacionController {

    private final NotificacionService servicio;

    public NotificacionController(NotificacionService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<NotificacionResponse> notificar(
            @Valid @RequestBody NotificacionRequest request,HttpServletRequest http) {
        return ResponseEntity.ok(servicio.notificar(request,String.valueOf(http.getAttribute(AutorizacionInterceptor.USER_ATTRIBUTE))));
    }
    @GetMapping("/historial") public Map<String,Object> historial(@RequestParam(defaultValue="0")int page,@RequestParam(defaultValue="10")int size,@RequestParam(defaultValue="")String destinatario,@RequestParam(defaultValue="")String estado){return servicio.historial(page,size,destinatario,estado);}
}

