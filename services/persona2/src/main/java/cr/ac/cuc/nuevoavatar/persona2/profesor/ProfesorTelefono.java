package cr.ac.cuc.nuevoavatar.persona2.profesor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ProfesorTelefono", schema = "academico")
public class ProfesorTelefono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProfesorTelefonoId")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ProfesorId", nullable = false)
    private Profesor profesor;

    @Column(name = "Telefono", length = 30, nullable = false)
    private String telefono;

    public Integer getId() { return id; }

    public Profesor getProfesor() { return profesor; }
    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}