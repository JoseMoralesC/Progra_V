package cr.ac.cuc.nuevoavatar.persona2.usuario;

import java.util.Map;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/** Solo invocable con la clave interna; no expone el hash de la cuenta. */
@RestController
public class CredencialesInternasController {
    private final UsuarioRepository usuarios;
    private final BCryptPasswordEncoder encoder;
    public CredencialesInternasController(UsuarioRepository usuarios, BCryptPasswordEncoder encoder){this.usuarios=usuarios;this.encoder=encoder;}
    public record Credenciales(String email,String contrasena){
        @Override public String toString(){return "Credenciales[contenido oculto]";}
    }
    @PostMapping("/interno/credenciales")
    public Map<String,String> verificar(@RequestBody Credenciales credenciales){
        if(credenciales.email()==null || credenciales.contrasena()==null) return Map.of();
        String email=credenciales.email().trim();
        var usuario=usuarios.findById(email);
        if(usuario.isEmpty() && !email.contains("@")) usuario=usuarios.findById(email+"@cuc.cr");
        try {
            if(usuario.isPresent() && encoder.matches(credenciales.contrasena(),usuario.get().getPasswordHash()))
                return Map.of("email",usuario.get().getEmail());
        } catch(IllegalArgumentException ignored){}
        return Map.of();
    }
}
