package cr.ac.cuc.nuevoavatar.persona5.factura;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaDetalleRepository
        extends JpaRepository<FacturaDetalle, Integer> {

    List<FacturaDetalle> findByFacturaId(Integer facturaId);
}
