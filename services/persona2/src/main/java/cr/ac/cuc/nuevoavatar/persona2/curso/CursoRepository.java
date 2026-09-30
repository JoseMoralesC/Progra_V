package cr.ac.cuc.nuevoavatar.persona2.curso;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Integer> {
    List<Curso> findByCarreraId(Integer carreraId);
}
