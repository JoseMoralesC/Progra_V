package cr.ac.cuc.nuevoavatar.persona4.academico;
import java.util.*;
import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
import static cr.ac.cuc.nuevoavatar.integracion.DatosRemotos.*;
import org.springframework.stereotype.Repository;
@Repository
public class ListadoEstudiantesRepository {
    private final DatosRemotos datos;
    public ListadoEstudiantesRepository(DatosRemotos datos){this.datos=datos;}
    public List<Grupo> obtenerGrupos(Integer periodoId){
        return datos.listar("Grupo","Grupo",Map.of("PeriodoId",periodoId)).stream().map(g->{
            var curso=datos.buscar("Curso","Curso","CursoId",entero(g,"CursoId")).orElseThrow();
            var carrera=datos.buscar("Carrera","Carrera","CarreraId",entero(curso,"CarreraId")).orElseThrow();
            return new Grupo(entero(g,"GrupoId"),entero(g,"NumeroGrupo"),entero(g,"CursoId"),texto(curso,"Nombre"),texto(carrera,"Nombre"));
        }).toList();
    }
    public Optional<Estudiante> obtenerEstudiante(String id){return datos.buscar("Expediente","Estudiante","Identificacion",id)
        .map(e->new Estudiante(texto(e,"TipoIdentificacion"),texto(e,"Identificacion"),texto(e,"NombreCompleto")));}
    public record Grupo(Integer id,Integer numero,Integer cursoId,String curso,String carrera){}
    public record Estudiante(String tipoIdentificacion,String identificacion,String nombreCompleto){}
}
