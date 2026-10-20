package cr.ac.cuc.nuevoavatar.persona4.expediente;
import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
import org.springframework.stereotype.Repository;
@Repository
public class ExpedienteRepository {
    private final DatosRemotos datos;
    public ExpedienteRepository(DatosRemotos datos){this.datos=datos;}
    public boolean existe(String id){return datos.buscar("Expediente","Estudiante","Identificacion",id).isPresent();}
}
