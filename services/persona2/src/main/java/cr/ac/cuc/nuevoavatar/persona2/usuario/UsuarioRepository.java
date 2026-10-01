package cr.ac.cuc.nuevoavatar.persona2.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UsuarioRepository
        extends JpaRepository<Usuario, String>,
                JpaSpecificationExecutor<Usuario> {
}