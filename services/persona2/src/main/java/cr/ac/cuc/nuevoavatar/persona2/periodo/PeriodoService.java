package cr.ac.cuc.nuevoavatar.persona2.periodo;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PeriodoService {

    private final PeriodoRepository periodos;

    public PeriodoService(PeriodoRepository periodos) {
        this.periodos = periodos;
    }

    @Transactional
    public PeriodoResponse crear(PeriodoRequest request) {
        validarFechas(request);
        Periodo periodo = new Periodo();
        asignar(periodo, request);
        try {
            return PeriodoResponse.desde(periodos.saveAndFlush(periodo));
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Ya existe ese número de periodo para el año indicado",
                ex
            );
        }
    }

    @Transactional(readOnly = true)
    public List<PeriodoResponse> listar() {
        return periodos.findAll().stream().map(PeriodoResponse::desde).toList();
    }

    @Transactional(readOnly = true)
    public PeriodoResponse obtener(Integer id) {
        return PeriodoResponse.desde(buscarO404(id));
    }

    @Transactional
    public PeriodoResponse modificar(Integer id, PeriodoRequest request) {
        validarFechas(request);
        Periodo periodo = buscarO404(id);
        asignar(periodo, request);
        try {
            return PeriodoResponse.desde(periodos.saveAndFlush(periodo));
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Ya existe ese número de periodo para el año indicado",
                ex
            );
        }
    }

    @Transactional
    public void eliminar(Integer id) {
        Periodo periodo = buscarO404(id);
        try {
            periodos.delete(periodo);
            periodos.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "El periodo tiene grupos relacionados",
                ex
            );
        }
    }

    private Periodo buscarO404(Integer id) {
        return periodos.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Periodo no encontrado")
        );
    }

    private void validarFechas(PeriodoRequest request) {
        if (!request.fechaFin().isAfter(request.fechaInicio())) {
            throw new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_CONTENT,
                "La fecha final debe ser posterior a la fecha inicial"
            );
        }
    }

    private void asignar(Periodo periodo, PeriodoRequest request) {
        periodo.setAnio(request.anio().shortValue());
        periodo.setNumeroPeriodo((byte) request.numeroPeriodo().intValue());
        periodo.setFechaInicio(request.fechaInicio());
        periodo.setFechaFin(request.fechaFin());
    }
}
