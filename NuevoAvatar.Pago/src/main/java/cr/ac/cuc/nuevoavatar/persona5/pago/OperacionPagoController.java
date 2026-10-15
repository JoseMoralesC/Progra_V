package cr.ac.cuc.nuevoavatar.persona5.pago;
import org.springframework.web.bind.annotation.*;
@RestController
public class OperacionPagoController {
    private final OperacionPagoService service;
    public OperacionPagoController(OperacionPagoService service){this.service=service;}
    @GetMapping("/interno/operaciones-pago/{id}")
    public OperacionPagoService.Operacion obtener(@PathVariable String id){return service.obtener(id);}
}
