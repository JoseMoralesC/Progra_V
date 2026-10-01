package cr.ac.cuc.nuevoavatar.persona5.matricula;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Matricula", schema = "matricula")
public class Matricula {

    public static final String ACTIVA = "ACTIVA";
    public static final String ANULADA = "ANULADA";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MatriculaId")
    private Integer id;

    @Column(name = "IdentificacionEstudiante", nullable = false, length = 30)
    private String identificacionEstudiante;

    @Column(name = "CursoId", nullable = false)
    private Integer cursoId;

    @Column(name = "GrupoId", nullable = false)
    private Integer grupoId;

    @Column(name = "PeriodoId", nullable = false)
    private Integer periodoId;

    @Column(name = "Estado", nullable = false, length = 20)
    private String estado = ACTIVA;

    @Column(name = "FechaMatricula", nullable = false)
    private LocalDateTime fechaMatricula;

    public Integer getId() { return id; }
    public String getIdentificacionEstudiante() { return identificacionEstudiante; }
    public void setIdentificacionEstudiante(String identificacionEstudiante) {
        this.identificacionEstudiante = identificacionEstudiante;
    }
    public Integer getCursoId() { return cursoId; }
    public void setCursoId(Integer cursoId) { this.cursoId = cursoId; }
    public Integer getGrupoId() { return grupoId; }
    public void setGrupoId(Integer grupoId) { this.grupoId = grupoId; }
    public Integer getPeriodoId() { return periodoId; }
    public void setPeriodoId(Integer periodoId) { this.periodoId = periodoId; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaMatricula() { return fechaMatricula; }
    public void setFechaMatricula(LocalDateTime fechaMatricula) {
        this.fechaMatricula = fechaMatricula;
    }
}
