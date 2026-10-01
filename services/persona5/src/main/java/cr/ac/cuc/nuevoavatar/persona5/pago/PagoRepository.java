package cr.ac.cuc.nuevoavatar.persona5.pago;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepository extends JpaRepository<Pago, Integer> {

    List<Pago> findByPeriodoId(Integer periodoId);

    boolean existsByFacturaIdAndEstado(Integer facturaId, String estado);
}
