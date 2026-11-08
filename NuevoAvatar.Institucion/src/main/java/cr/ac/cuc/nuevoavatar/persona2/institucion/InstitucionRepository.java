package cr.ac.cuc.nuevoavatar.persona2.institucion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InstitucionRepository
        extends JpaRepository<Institucion, Integer> {

    List<Institucion> findByNombreContainingIgnoreCaseOrderByNombreAsc(String nombre);
}
