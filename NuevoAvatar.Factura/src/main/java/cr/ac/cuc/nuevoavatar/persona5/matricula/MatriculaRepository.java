package cr.ac.cuc.nuevoavatar.persona5.matricula;
import java.util.*;
import org.springframework.stereotype.Repository;
import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
@Repository
public class MatriculaRepository {
    private final DatosRemotos datos;
    public MatriculaRepository(DatosRemotos datos){this.datos=datos;}
    public List<Matricula> filtrar(Map<String,?> filtros){return datos.listar("Matricula","Matricula",filtros).stream().map(r->datos.convertir(r,Matricula.class)).toList();}
    public Optional<Matricula> findById(Integer id){return filtrar(Map.of("MatriculaId",id)).stream().findFirst();}
    public boolean existsById(Integer id){return findById(id).isPresent();}

    public List<Matricula> findByCursoIdAndGrupoIdAndEstado(Integer cursoId,Integer grupoId,String estado){return filtrar(Map.of("CursoId",cursoId,"GrupoId",grupoId,"Estado",estado));}
    public Optional<Matricula> findFirstByIdentificacionEstudianteAndCursoIdAndGrupoIdAndEstado(String id,Integer curso,Integer grupo,String estado){return filtrar(Map.of("IdentificacionEstudiante",id,"CursoId",curso,"GrupoId",grupo,"Estado",estado)).stream().findFirst();}
    public Optional<Matricula> findFirstByIdentificacionEstudianteAndCursoIdAndGrupoIdAndPeriodoIdAndEstado(String id,Integer curso,Integer grupo,Integer periodo,String estado){return filtrar(Map.of("IdentificacionEstudiante",id,"CursoId",curso,"GrupoId",grupo,"PeriodoId",periodo,"Estado",estado)).stream().findFirst();}
    public boolean existsByIdentificacionEstudianteAndCursoIdAndGrupoIdAndPeriodoIdAndEstado(String id,Integer curso,Integer grupo,Integer periodo,String estado){return findFirstByIdentificacionEstudianteAndCursoIdAndGrupoIdAndPeriodoIdAndEstado(id,curso,grupo,periodo,estado).isPresent();}

}
