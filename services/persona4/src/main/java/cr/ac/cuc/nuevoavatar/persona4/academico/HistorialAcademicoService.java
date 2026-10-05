package cr.ac.cuc.nuevoavatar.persona4.academico;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import cr.ac.cuc.nuevoavatar.persona4.seguridad.BitacoraService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class HistorialAcademicoService {
    private final HistorialAcademicoRepository repositorio;
    private final MatriculaNotasClient notas;
    private final BitacoraService bitacora;

    public HistorialAcademicoService(HistorialAcademicoRepository repositorio,
            MatriculaNotasClient notas, BitacoraService bitacora) {
        this.repositorio = repositorio;
        this.notas = notas;
        this.bitacora = bitacora;
    }

    @Transactional(readOnly = true)
    public List<HistorialAcademicoResponse> obtener(String tipo, String identificacion,
            String token, String usuario) {
        if (tipo == null || tipo.isBlank() || identificacion == null || identificacion.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El tipo y la identificacion del estudiante son obligatorios");
        }
        String numero = identificacion.trim();
        if (!repositorio.existeEstudiante(tipo.trim(), numero)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El estudiante no existe con los datos indicados");
        }
        var intentos = repositorio.obtenerIntentos(numero);
        var grupos = new HashSet<String>();
        for (var intento : intentos) {
            // MAT5 identifica el intento por curso/grupo; una ambiguedad no debe mezclar notas.
            if (!grupos.add(intento.cursoId() + ":" + intento.grupoId())) {
                throw inconsistencia("El servicio de notas no distingue estas matriculas del mismo curso y grupo");
            }
        }
        var resultado = new ArrayList<HistorialAcademicoResponse>();
        for (var intento : intentos) {
            var calificaciones = notas.obtenerNotas(token, numero, intento.cursoId(), intento.grupoId());
            resultado.add(new HistorialAcademicoResponse(intento.cursoId(), intento.nombreCurso(),
                    acumular(intento.matriculaId(), calificaciones)));
        }
        bitacora.consulta(token, usuario, "historial academico");
        return resultado;
    }

    private BigDecimal acumular(Integer matriculaId, List<MatriculaNotasClient.Nota> calificaciones) {
        if (calificaciones == null) throw inconsistencia("El servicio de notas no devolvio una lista valida");
        BigDecimal acumulado = BigDecimal.ZERO;
        BigDecimal porcentaje = BigDecimal.ZERO;
        var rubros = new HashSet<Integer>();
        for (var calificacion : calificaciones) {
            if (calificacion == null || !Objects.equals(matriculaId, calificacion.matriculaId())
                    || calificacion.rubroId() == null || !rubros.add(calificacion.rubroId())
                    || calificacion.nota() == null || calificacion.porcentaje() == null
                    || calificacion.nota().compareTo(BigDecimal.ONE) < 0
                    || calificacion.nota().compareTo(BigDecimal.valueOf(100)) > 0
                    || calificacion.porcentaje().signum() < 0
                    || calificacion.porcentaje().compareTo(BigDecimal.valueOf(100)) > 0) {
                throw inconsistencia("El servicio de notas devolvio calificaciones inconsistentes");
            }
            porcentaje = porcentaje.add(calificacion.porcentaje());
            // Fabian confirmo acumulado sobre 100; los rubros pendientes no aportan puntos.
            acumulado = acumulado.add(calificacion.nota().multiply(calificacion.porcentaje()).movePointLeft(2));
        }
        if (porcentaje.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw inconsistencia("Los porcentajes de las notas superan 100");
        }
        return acumulado;
    }

    private ResponseStatusException inconsistencia(String mensaje) {
        return new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, mensaje);
    }
}
