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
    private final AuditoriaNotas auditoria;

    public NotasService(
            DesgloseRubroRepository rubros,
            NotaRubroRepository notas,
            MatriculaRepository matriculas,
            GrupoRepository grupos,
            AuditoriaNotas auditoria) {
        this.rubros = rubros;
        this.notas = notas;
        this.matriculas = matriculas;
        this.grupos = grupos;
        this.auditoria = auditoria;
    }

    @Transactional
    public List<RubroResponse> cargarDesglose(CargarDesgloseRequest request) {
        if (!grupos.existsById(request.grupoId())) {
            throw invalido("El grupo no existe");
        }
        validarSuma(request);

        List<DesgloseRubro> actuales = rubros.findByGrupoId(request.grupoId());
        List<RubroResponse> anterior = actuales.stream().map(RubroResponse::desde).toList();
        List<Integer> idsActuales = actuales.stream().map(rubro -> java.util.Objects.requireNonNull(rubro).getId()).toList();
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
        var guardados = rubros.saveAll(nuevos);
        rubros.flush();
        var resultado = guardados.stream().map(RubroResponse::desde).toList();
        auditoria.registrarDesglose(request.grupoId(), anterior, resultado);
        return resultado;
    }

    @Transactional
    public NotaResponse asignarNota(AsignarNotaRequest request, boolean modificar) {
        if (request.nota() == null || request.nota().compareTo(BigDecimal.ONE) < 0 ||
                request.nota().compareTo(CIEN) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La nota debe estar entre 1 y 100");
        }
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

        var existente = notas.findByMatriculaIdAndRubroId(request.matriculaId(), request.rubroId());
        if (!modificar && existente.isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La nota ya existe; utilice PUT");
        if (modificar && existente.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La nota no existe; utilice POST");
        NotaRubro nota = existente.orElseGet(NotaRubro::new);
        NotaResponse anterior = existente.isPresent() ? NotaResponse.desde(nota, rubro) : null;
        nota.setMatriculaId(request.matriculaId());
        nota.setRubroId(request.rubroId());
        nota.setNota(request.nota());
        nota.setFechaRegistro(LocalDateTime.now());
        NotaResponse actual = NotaResponse.desde(notas.saveAndFlush(nota), rubro);
        auditoria.registrar(anterior, actual);
        return actual;
    }

    @Transactional(readOnly = true)
    public List<NotasEstudianteResponse> obtenerNotasGrupo(Integer grupoId) {
        var grupo = grupos.findById(grupoId).orElseThrow(() -> invalido("El grupo no existe"));
        return matriculas.findByCursoIdAndGrupoIdAndEstado(grupo.getCursoId(), grupoId, Matricula.ACTIVA)
            .stream().map(m -> new NotasEstudianteResponse(m.getId(), m.getIdentificacionEstudiante(),
                obtenerNotas(m.getIdentificacionEstudiante(), grupo.getCursoId(), grupoId))).toList();
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
            .collect(Collectors.toMap(rubro -> java.util.Objects.requireNonNull(rubro).getId(), Function.identity()));

        return notas.findByMatriculaId(matricula.getId())
            .stream()
            .filter(nota -> rubrosPorId.containsKey(nota.getRubroId()))
            .map(nota -> NotaResponse.desde(nota, rubrosPorId.get(nota.getRubroId())))
            .toList();
    }

    private void validarSuma(CargarDesgloseRequest request) {
        BigDecimal total = request.rubros().stream()
            .map(rubro -> java.util.Objects.requireNonNull(rubro).porcentaje())
            .reduce(BigDecimal.ZERO, (acumulado, porcentaje) -> java.util.Objects.requireNonNull(acumulado).add(porcentaje));
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

