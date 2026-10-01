package cr.ac.cuc.nuevoavatar.persona2.carrera;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "Carrera", schema = "academico")
public class Carrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CarreraId")
    private Integer id;

    @Nationalized
    @Column(name = "Nombre", length = 150, nullable = false)
    private String nombre;

    @Column(name = "InstitucionId", nullable = false)
    private Integer institucionId;

    @Column(name = "DirectorProfesorId", nullable = false)
    private Integer directorProfesorId;

    public Integer getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getInstitucionId() { return institucionId; }
    public void setInstitucionId(Integer institucionId) {
        this.institucionId = institucionId;
    }

    public Integer getDirectorProfesorId() {
        return directorProfesorId;
    }
    public void setDirectorProfesorId(Integer directorProfesorId) {
        this.directorProfesorId = directorProfesorId;
    }
}