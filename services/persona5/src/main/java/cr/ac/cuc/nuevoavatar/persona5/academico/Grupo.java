package cr.ac.cuc.nuevoavatar.persona5.academico;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Grupo", schema = "academico")
public class Grupo {

    @Id
    @Column(name = "GrupoId")
    private Integer id;

    @Column(name = "CursoId", nullable = false)
    private Integer cursoId;

    @Column(name = "PeriodoId", nullable = false)
    private Integer periodoId;

    public Integer getId() { return id; }
    public Integer getCursoId() { return cursoId; }
    public Integer getPeriodoId() { return periodoId; }
}
