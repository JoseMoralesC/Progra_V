package cr.ac.cuc.nuevoavatar.persona2.grupo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Grupo", schema = "academico")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GrupoId")
    private Integer id;

    @Column(name = "NumeroGrupo", nullable = false)
    private Integer numeroGrupo;

    @Column(name = "CursoId", nullable = false)
    private Integer cursoId;

    @Column(name = "ProfesorId", nullable = false)
    private Integer profesorId;

    @Column(name = "Horario", nullable = false, length = 100)
    private String horario;

    @Column(name = "Cupo", nullable = false)
    private Integer cupo;

    @Column(name = "PeriodoId", nullable = false)
    private Integer periodoId;

    public Integer getId() { return id; }
    public Integer getNumeroGrupo() { return numeroGrupo; }
    public void setNumeroGrupo(Integer numeroGrupo) { this.numeroGrupo = numeroGrupo; }
    public Integer getCursoId() { return cursoId; }
    public void setCursoId(Integer cursoId) { this.cursoId = cursoId; }
    public Integer getProfesorId() { return profesorId; }
    public void setProfesorId(Integer profesorId) { this.profesorId = profesorId; }
    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }
    public Integer getCupo() { return cupo; }
    public void setCupo(Integer cupo) { this.cupo = cupo; }
    public Integer getPeriodoId() { return periodoId; }
    public void setPeriodoId(Integer periodoId) { this.periodoId = periodoId; }
}
