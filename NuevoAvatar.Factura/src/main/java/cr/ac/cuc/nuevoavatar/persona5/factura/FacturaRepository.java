package cr.ac.cuc.nuevoavatar.persona5.factura;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaRepository extends JpaRepository<Factura, Integer> {

    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select f from Factura f where f.id=?1")
    java.util.Optional<Factura> bloquearPorId(Integer id);

    List<Factura> findByPeriodoId(Integer periodoId);

    boolean existsByMatriculaIdAndEstadoNot(Integer matriculaId, String estado);
}

