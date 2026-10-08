package cr.ac.cuc.nuevoavatar.persona5.pago;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Pago", schema = "finanzas")
public class Pago {

    public static final String APLICADO = "APLICADO";
    public static final String ANULADO = "ANULADO";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PagoId")
    private Integer id;

    @Column(name = "FacturaId", nullable = false)
    private Integer facturaId;

    @Column(name = "PeriodoId", nullable = false)
    private Integer periodoId;

    @Column(name = "Monto", nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(name = "Estado", nullable = false, length = 20)
    private String estado = APLICADO;

    @Column(name = "FechaPago", nullable = false)
    private LocalDateTime fechaPago;

    public Integer getId() { return id; }
    public Integer getFacturaId() { return facturaId; }
    public void setFacturaId(Integer facturaId) { this.facturaId = facturaId; }
    public Integer getPeriodoId() { return periodoId; }
    public void setPeriodoId(Integer periodoId) { this.periodoId = periodoId; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }
}
