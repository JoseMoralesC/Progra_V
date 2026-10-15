package cr.ac.cuc.nuevoavatar.persona4.academico;
import java.util.*;
import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
import org.springframework.stereotype.Repository;
import static cr.ac.cuc.nuevoavatar.integracion.DatosRemotos.*;
@Repository
public class HistorialAcademicoRepository {
    private final DatosRemotos datos;
    public HistorialAcademicoRepository(DatosRemotos datos){this.datos=datos;}
    public boolean existeEstudiante(String tipo,String id){return !datos.listar("Expediente","Estudiante",Map.of("Identificacion",id,"TipoIdentificacion",tipo)).isEmpty();}
    public List<Intento> obtenerIntentos(String id){
        return datos.listar("Matricula","Matricula",Map.of("IdentificacionEstudiante",id,"Estado","ACTIVA")).stream()
            .sorted(Comparator.comparingInt((Map<String,Object> r)->entero(r,"PeriodoId")).thenComparingInt(r->entero(r,"MatriculaId")))
            .map(m->{int curso=entero(m,"CursoId");
                var c=datos.buscar("Curso","Curso","CursoId",curso).orElseThrow(()->new IllegalStateException("Curso no encontrado"));
                return new Intento(entero(m,"MatriculaId"),curso,entero(m,"GrupoId"),texto(c,"Nombre"));}).toList();
    }
    public record Intento(Integer matriculaId,Integer cursoId,Integer grupoId,String nombreCurso){}
}
