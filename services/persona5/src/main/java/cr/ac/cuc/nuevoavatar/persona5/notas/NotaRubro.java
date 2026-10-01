package cr.ac.cuc.nuevoavatar.persona5.notas;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "NotaRubro", schema = "matricula")
public class NotaRubro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NotaRubroId")
    private Integer id;

    @Column(name = "MatriculaId", nullable = false)
    private Integer matriculaId;

    @Column(name = "RubroId", nullable = false)
    private Integer rubroId;

    @Column(name = "Nota", nullable = false, precision = 5, scale = 2)
    private BigDecimal nota;

    @Column(name = "FechaRegistro", nullable = false)
    private LocalDateTime fechaRegistro;

    public Integer getId() { return id; }
    public Integer getMatriculaId() { return matriculaId; }
    public void setMatriculaId(Integer matriculaId) { this.matriculaId = matriculaId; }
    public Integer getRubroId() { return rubroId; }
    public void setRubroId(Integer rubroId) { this.rubroId = rubroId; }
    public BigDecimal getNota() { return nota; }
    public void setNota(BigDecimal nota) { this.nota = nota; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
