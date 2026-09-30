package cr.ac.cuc.nuevoavatar.persona2.usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario", schema = "seguridad")
public class Usuario {

    @Id
    @Column(name = "email", length = 150, nullable = false)
    private String email;

    @Column(name = "tipo_identificacion", length = 30, nullable = false)
    private String tipoIdentificacion;

    @Column(name = "identificacion", length = 30, nullable = false)
    private String identificacion;

    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;

    @Column(name = "id_rol", length = 20, nullable = false)
    private String idRol;

    @Column(name = "password_hash", length = 255, nullable = false)
    private String passwordHash;

    public Usuario() {
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTipoIdentificacion() { return tipoIdentificacion; }
    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getIdRol() { return idRol; }
    public void setIdRol(String idRol) { this.idRol = idRol; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}