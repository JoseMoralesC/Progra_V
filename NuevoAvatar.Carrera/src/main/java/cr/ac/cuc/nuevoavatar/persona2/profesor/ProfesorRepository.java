package cr.ac.cuc.nuevoavatar.persona2.profesor;
import java.util.*;
import org.springframework.stereotype.Repository;
import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
@Repository
public class ProfesorRepository {
    private final DatosRemotos datos;
    public ProfesorRepository(DatosRemotos datos){this.datos=datos;}
    public List<Profesor> filtrar(Map<String,?> filtros){return datos.listar("Profesor","Profesor",filtros).stream().map(r->datos.convertir(r,Profesor.class)).toList();}
    public Optional<Profesor> findById(Integer id){return filtrar(Map.of("ProfesorId",id)).stream().findFirst();}
    public boolean existsById(Integer id){return findById(id).isPresent();}

}
