package cr.ac.cuc.nuevoavatar.persona5.academico;
import java.util.*;
import org.springframework.stereotype.Repository;
import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
@Repository
public class GrupoRepository {
    private final DatosRemotos datos;
    public GrupoRepository(DatosRemotos datos){this.datos=datos;}
    public List<Grupo> filtrar(Map<String,?> filtros){return datos.listar("Grupo","Grupo",filtros).stream().map(r->datos.convertir(r,Grupo.class)).toList();}
    public Optional<Grupo> findById(Integer id){return filtrar(Map.of("GrupoId",id)).stream().findFirst();}
    public boolean existsById(Integer id){return findById(id).isPresent();}

}
