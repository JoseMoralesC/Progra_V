package cr.ac.cuc.nuevoavatar.persona2.profesor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProfesorRepository extends JpaRepository<Profesor, Integer>, JpaSpecificationExecutor<Profesor> {
}
