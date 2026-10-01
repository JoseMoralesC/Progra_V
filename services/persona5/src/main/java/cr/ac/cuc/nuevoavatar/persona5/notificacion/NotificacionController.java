package cr.ac.cuc.nuevoavatar.persona5.notificacion;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notificar")
public class NotificacionController {

    private final NotificacionService servicio;

    public NotificacionController(NotificacionService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<NotificacionResponse> notificar(
            @Valid @RequestBody NotificacionRequest request) {
        return ResponseEntity.ok(servicio.notificar(request));
    }
}
