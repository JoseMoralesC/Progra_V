package cr.ac.cuc.nuevoavatar.persona5.factura;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Factura", schema = "finanzas")
public class Factura {

    public static final String PENDIENTE = "PENDIENTE";
    public static final String PAGADA = "PAGADA";
    public static final String ANULADA = "ANULADA";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FacturaId")
    private Integer id;

    @Column(name = "MatriculaId", nullable = false)
    private Integer matriculaId;

    @Column(name = "IdentificacionEstudiante", nullable = false, length = 30)
    private String identificacionEstudiante;

    @Column(name = "PeriodoId", nullable = false)
    private Integer periodoId;

    @Column(name = "Subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "Impuesto", nullable = false, precision = 12, scale = 2)
    private BigDecimal impuesto;

    @Column(name = "Total", nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "Estado", nullable = false, length = 20)
    private String estado = PENDIENTE;

    @Column(name = "FechaFactura", nullable = false)
    private LocalDateTime fechaFactura;

    public Integer getId() { return id; }
    public Integer getMatriculaId() { return matriculaId; }
    public void setMatriculaId(Integer matriculaId) { this.matriculaId = matriculaId; }
    public String getIdentificacionEstudiante() { return identificacionEstudiante; }
    public void setIdentificacionEstudiante(String identificacionEstudiante) {
        this.identificacionEstudiante = identificacionEstudiante;
    }
    public Integer getPeriodoId() { return periodoId; }
    public void setPeriodoId(Integer periodoId) { this.periodoId = periodoId; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getImpuesto() { return impuesto; }
    public void setImpuesto(BigDecimal impuesto) { this.impuesto = impuesto; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaFactura() { return fechaFactura; }
    public void setFechaFactura(LocalDateTime fechaFactura) {
        this.fechaFactura = fechaFactura;
    }
}
