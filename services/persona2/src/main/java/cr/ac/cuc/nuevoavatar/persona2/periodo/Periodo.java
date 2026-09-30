package cr.ac.cuc.nuevoavatar.persona2.periodo;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Periodo", schema = "academico")
public class Periodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PeriodoId")
    private Integer id;

    @Column(name = "Anio", nullable = false)
    private Short anio;

    @Column(name = "NumeroPeriodo", nullable = false)
    private Byte numeroPeriodo;

    @Column(name = "FechaInicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "FechaFin", nullable = false)
    private LocalDate fechaFin;

    public Integer getId() { return id; }
    public Short getAnio() { return anio; }
    public void setAnio(Short anio) { this.anio = anio; }
    public Byte getNumeroPeriodo() { return numeroPeriodo; }
    public void setNumeroPeriodo(Byte numeroPeriodo) { this.numeroPeriodo = numeroPeriodo; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
}
