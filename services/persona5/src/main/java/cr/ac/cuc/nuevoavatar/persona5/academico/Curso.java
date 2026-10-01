package cr.ac.cuc.nuevoavatar.persona5.academico;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Curso", schema = "academico")
public class Curso {

    @Id
    @Column(name = "CursoId")
    private Integer id;

    public Integer getId() { return id; }
}
