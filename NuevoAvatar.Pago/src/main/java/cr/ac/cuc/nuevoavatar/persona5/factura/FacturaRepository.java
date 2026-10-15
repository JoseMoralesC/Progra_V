package cr.ac.cuc.nuevoavatar.persona5.factura;
import java.util.*;
import org.springframework.stereotype.Repository;
import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
@Repository
public class FacturaRepository {
    private final DatosRemotos datos;
    public FacturaRepository(DatosRemotos datos){this.datos=datos;}
    public List<Factura> filtrar(Map<String,?> filtros){return datos.listar("Factura","Factura",filtros).stream().map(r->datos.convertir(r,Factura.class)).toList();}
    public Optional<Factura> findById(Integer id){return filtrar(Map.of("FacturaId",id)).stream().findFirst();}
    public boolean existsById(Integer id){return findById(id).isPresent();}

}
