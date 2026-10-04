package cr.ac.cuc.nuevoavatar.persona2.curso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Curso", schema = "academico")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CursoId")
    private Integer id;

    @Column(name = "CarreraId", nullable = false)
    private Integer carreraId;

    @Column(name = "Nivel", nullable = false)
    private Byte nivel;

    @Column(name = "Nombre", nullable = false, length = 150)
    private String nombre;

    public Integer getId() { return id; }
    public Integer getCarreraId() { return carreraId; }
    public void setCarreraId(Integer carreraId) { this.carreraId = carreraId; }
    public Byte getNivel() { return nivel; }
    public void setNivel(Byte nivel) { this.nivel = nivel; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
