package cr.ac.cuc.nuevoavatar.persona5.academico;
import java.util.*;
import org.springframework.stereotype.Repository;
import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
@Repository
public class PeriodoRepository {
    private final DatosRemotos datos;
    public PeriodoRepository(DatosRemotos datos){this.datos=datos;}
    public List<Periodo> filtrar(Map<String,?> filtros){return datos.listar("Periodo","Periodo",filtros).stream().map(r->datos.convertir(r,Periodo.class)).toList();}
    public Optional<Periodo> findById(Integer id){return filtrar(Map.of("PeriodoId",id)).stream().findFirst();}
    public boolean existsById(Integer id){return findById(id).isPresent();}

}
