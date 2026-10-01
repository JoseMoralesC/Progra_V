package cr.ac.cuc.nuevoavatar.persona5.factura;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaRepository extends JpaRepository<Factura, Integer> {

    List<Factura> findByPeriodoId(Integer periodoId);

    boolean existsByMatriculaIdAndEstadoNot(Integer matriculaId, String estado);
}
