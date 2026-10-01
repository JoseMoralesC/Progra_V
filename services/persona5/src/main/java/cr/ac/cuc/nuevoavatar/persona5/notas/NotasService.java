package cr.ac.cuc.nuevoavatar.persona5.notas;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import cr.ac.cuc.nuevoavatar.persona5.academico.GrupoRepository;
import cr.ac.cuc.nuevoavatar.persona5.matricula.Matricula;
import cr.ac.cuc.nuevoavatar.persona5.matricula.MatriculaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class NotasService {

    private static final BigDecimal CIEN = new BigDecimal("100.00");

    private final DesgloseRubroRepository rubros;
    private final NotaRubroRepository notas;
    private final MatriculaRepository matriculas;
    private final GrupoRepository grupos;

    public NotasService(
            DesgloseRubroRepository rubros,
            NotaRubroRepository notas,
            MatriculaRepository matriculas,
            GrupoRepository grupos) {
        this.rubros = rubros;
        this.notas = notas;
        this.matriculas = matriculas;
        this.grupos = grupos;
    }

    @Transactional
    public List<RubroResponse> cargarDesglose(CargarDesgloseRequest request) {
        if (!grupos.existsById(request.grupoId())) {
            throw invalido("El grupo no existe");
        }
        validarSuma(request);

        List<DesgloseRubro> actuales = rubros.findByGrupoId(request.grupoId());
        List<Integer> idsActuales = actuales.stream().map(DesgloseRubro::getId).toList();
        if (!idsActuales.isEmpty() && notas.existsByRubroIdIn(idsActuales)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "No se pueden modificar rubros porque ya existen notas"
            );
        }

        rubros.deleteByGrupoId(request.grupoId());
        rubros.flush();

        List<DesgloseRubro> nuevos = request.rubros().stream()
            .map(rubro -> crearRubro(request.grupoId(), rubro))
            .toList();
        return rubros.saveAll(nuevos).stream().map(RubroResponse::desde).toList();
    }

    @Transactional
    public NotaResponse asignarNota(AsignarNotaRequest request) {
        Matricula matricula = matriculas.findById(request.matriculaId()).orElseThrow(() ->
            invalido("La matricula no existe")
        );
        if (!Matricula.ACTIVA.equals(matricula.getEstado())) {
            throw invalido("La matricula no esta activa");
        }

        DesgloseRubro rubro = rubros.findById(request.rubroId()).orElseThrow(() ->
            invalido("El rubro no existe")
        );
        if (!rubro.getGrupoId().equals(matricula.getGrupoId())) {
            throw invalido("El rubro no pertenece al grupo de la matricula");
        }

        NotaRubro nota = notas
            .findByMatriculaIdAndRubroId(request.matriculaId(), request.rubroId())
            .orElseGet(NotaRubro::new);
        nota.setMatriculaId(request.matriculaId());
        nota.setRubroId(request.rubroId());
        nota.setNota(request.nota());
        nota.setFechaRegistro(LocalDateTime.now());
        return NotaResponse.desde(notas.saveAndFlush(nota), rubro);
    }

    @Transactional(readOnly = true)
    public List<RubroResponse> obtenerDesglose(Integer grupoId) {
        return rubros.findByGrupoId(grupoId).stream().map(RubroResponse::desde).toList();
    }

    @Transactional(readOnly = true)
    public List<NotaResponse> obtenerNotas(
            String identificacionEstudiante,
            Integer cursoId,
            Integer grupoId) {
        Matricula matricula = matriculas
            .findFirstByIdentificacionEstudianteAndCursoIdAndGrupoIdAndEstado(
                identificacionEstudiante.trim(),
                cursoId,
                grupoId,
                Matricula.ACTIVA
            )
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No existe matricula activa para los datos indicados"
            ));

        Map<Integer, DesgloseRubro> rubrosPorId = rubros
            .findByGrupoId(grupoId)
            .stream()
            .collect(Collectors.toMap(DesgloseRubro::getId, Function.identity()));

        return notas.findByMatriculaId(matricula.getId())
            .stream()
            .filter(nota -> rubrosPorId.containsKey(nota.getRubroId()))
            .map(nota -> NotaResponse.desde(nota, rubrosPorId.get(nota.getRubroId())))
            .toList();
    }

    private void validarSuma(CargarDesgloseRequest request) {
        BigDecimal total = request.rubros().stream()
            .map(CargarDesgloseRequest.RubroRequest::porcentaje)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (total.compareTo(CIEN) != 0) {
            throw invalido("La sumatoria de rubros debe ser 100");
        }
    }

    private DesgloseRubro crearRubro(
            Integer grupoId,
            CargarDesgloseRequest.RubroRequest request) {
        DesgloseRubro rubro = new DesgloseRubro();
        rubro.setGrupoId(grupoId);
        rubro.setNombre(request.nombre().trim());
        rubro.setPorcentaje(request.porcentaje());
        rubro.setFechaCreacion(LocalDateTime.now());
        return rubro;
    }

    private ResponseStatusException invalido(String mensaje) {
        return new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, mensaje);
    }
}
