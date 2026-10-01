package cr.ac.cuc.nuevoavatar.persona5.notas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DesgloseRubroRepository
        extends JpaRepository<DesgloseRubro, Integer> {

    List<DesgloseRubro> findByGrupoId(Integer grupoId);

    void deleteByGrupoId(Integer grupoId);
}
