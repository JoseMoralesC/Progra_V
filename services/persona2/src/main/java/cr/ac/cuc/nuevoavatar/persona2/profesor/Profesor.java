package cr.ac.cuc.nuevoavatar.persona2.profesor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "Profesor", schema = "academico")
public class Profesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProfesorId")
    private Integer id;

    @Column(name = "TipoIdentificacion", length = 30, nullable = false)
    private String tipoIdentificacion;

    @Column(name = "Identificacion", length = 30, nullable = false)
    private String identificacion;

    @Column(name = "Email", length = 150, nullable = false)
    private String email;

    @Nationalized
    @Column(name = "NombreCompleto", length = 150, nullable = false)
    private String nombreCompleto;

    @Column(name = "FechaNacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @OneToMany(
        mappedBy = "profesor",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<ProfesorTelefono> telefonos = new ArrayList<>();

    public Integer getId() { return id; }

    public String getTipoIdentificacion() { return tipoIdentificacion; }
    public void setTipoIdentificacion(String valor) {
        this.tipoIdentificacion = valor;
    }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String valor) {
        this.identificacion = valor;
    }

    public String getEmail() { return email; }
    public void setEmail(String valor) { this.email = valor; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String valor) {
        this.nombreCompleto = valor;
    }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate valor) {
        this.fechaNacimiento = valor;
    }

    public List<ProfesorTelefono> getTelefonos() { return telefonos; }

    public void reemplazarTelefonos(List<String> numeros) {
        telefonos.clear();
        for (String numero : numeros) {
            ProfesorTelefono telefono = new ProfesorTelefono();
            telefono.setProfesor(this);
            telefono.setTelefono(numero.trim());
            telefonos.add(telefono);
        }
    }
}