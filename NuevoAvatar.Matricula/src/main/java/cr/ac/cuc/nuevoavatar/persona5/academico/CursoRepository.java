package cr.ac.cuc.nuevoavatar.persona5.academico;
import java.util.*;
import org.springframework.stereotype.Repository;
import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
@Repository
public class CursoRepository {
    private final DatosRemotos datos;
    public CursoRepository(DatosRemotos datos){this.datos=datos;}
    public List<Curso> filtrar(Map<String,?> filtros){return datos.listar("Curso","Curso",filtros).stream().map(r->datos.convertir(r,Curso.class)).toList();}
    public Optional<Curso> findById(Integer id){return filtrar(Map.of("CursoId",id)).stream().findFirst();}
    public boolean existsById(Integer id){return findById(id).isPresent();}

}
