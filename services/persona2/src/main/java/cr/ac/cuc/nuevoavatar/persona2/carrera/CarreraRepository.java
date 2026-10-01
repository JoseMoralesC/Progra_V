package cr.ac.cuc.nuevoavatar.persona2.carrera;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarreraRepository extends JpaRepository<Carrera, Integer> {
    List<Carrera> findByInstitucionId(Integer institucionId);
}