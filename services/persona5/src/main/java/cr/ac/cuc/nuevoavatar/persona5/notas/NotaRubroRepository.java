package cr.ac.cuc.nuevoavatar.persona5.notas;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaRubroRepository extends JpaRepository<NotaRubro, Integer> {

    boolean existsByRubroIdIn(List<Integer> rubroIds);

    List<NotaRubro> findByMatriculaId(Integer matriculaId);

    Optional<NotaRubro> findByMatriculaIdAndRubroId(
        Integer matriculaId,
        Integer rubroId
    );
}
