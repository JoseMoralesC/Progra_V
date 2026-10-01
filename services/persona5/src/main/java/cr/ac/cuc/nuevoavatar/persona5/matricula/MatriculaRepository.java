package cr.ac.cuc.nuevoavatar.persona5.matricula;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {

    List<Matricula> findByCursoIdAndGrupoIdAndEstado(
        Integer cursoId,
        Integer grupoId,
        String estado
    );

    boolean existsByIdentificacionEstudianteAndCursoIdAndGrupoIdAndPeriodoIdAndEstado(
        String identificacionEstudiante,
        Integer cursoId,
        Integer grupoId,
        Integer periodoId,
        String estado
    );

    Optional<Matricula> findFirstByIdentificacionEstudianteAndCursoIdAndGrupoIdAndPeriodoIdAndEstado(
        String identificacionEstudiante,
        Integer cursoId,
        Integer grupoId,
        Integer periodoId,
        String estado
    );

    Optional<Matricula> findFirstByIdentificacionEstudianteAndCursoIdAndGrupoIdAndEstado(
        String identificacionEstudiante,
        Integer cursoId,
        Integer grupoId,
        String estado
    );
}
