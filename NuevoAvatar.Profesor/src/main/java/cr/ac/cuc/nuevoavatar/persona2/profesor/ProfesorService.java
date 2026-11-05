package cr.ac.cuc.nuevoavatar.persona2.profesor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProfesorService {

    private final ProfesorRepository repositorio;
    private final cr.ac.cuc.nuevoavatar.integracion.DatosRemotos datos;

    public ProfesorService(ProfesorRepository repositorio, cr.ac.cuc.nuevoavatar.integracion.DatosRemotos datos) {
        this.repositorio = repositorio;
        this.datos = datos;
    }

    @Transactional
    public ProfesorResponse crear(ProfesorRequest request) {
        validar(request);

        Profesor profesor = new Profesor();
        asignarDatos(profesor, request);
        profesor.reemplazarTelefonos(normalizarTelefonos(request.telefonos()));

        try {
            return ProfesorResponse.desde(
                repositorio.saveAndFlush(profesor)
            );
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "El email o la identificación ya está registrada",
                ex
            );
        }
    }

    @Transactional(readOnly = true)
    public List<ProfesorResponse> listar() {
        return repositorio.findAll().stream()
            .map(ProfesorResponse::desde)
            .toList();
    }

    @Transactional(readOnly = true)
    public PaginaResponse<ProfesorResponse> buscar(String texto, int page, int size, String sort, String direction) {
        if (page < 0 || size < 1 || size > 100) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Paginación inválida");
        Set<String> permitidos = Set.of("nombreCompleto", "identificacion", "email", "fechaNacimiento");
        if (!permitidos.contains(sort)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ordenamiento inválido");
        if (!Set.of("asc", "desc").contains(direction.toLowerCase(Locale.ROOT))) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dirección de ordenamiento inválida");
        Sort orden = Sort.by("desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC, sort);
        String filtro = texto == null ? "" : texto.trim().toLowerCase(Locale.ROOT);
        Specification<Profesor> spec = (root, query, cb) -> filtro.isBlank() ? cb.conjunction() : cb.or(
            cb.like(cb.lower(root.get("nombreCompleto")), "%" + filtro + "%"),
            cb.like(cb.lower(root.get("identificacion")), "%" + filtro + "%"),
            cb.like(cb.lower(root.get("email")), "%" + filtro + "%")
        );
        var pagina = repositorio.findAll(spec, PageRequest.of(page, size, orden)).map(ProfesorResponse::desde);
        return PaginaResponse.desde(pagina);
    }

    @Transactional(readOnly = true)
    public ProfesorResponse obtener(Integer id) {
        return ProfesorResponse.desde(buscarO404(id));
    }

    @Transactional
    public ProfesorResponse modificar(Integer id, ProfesorRequest request) {
        validar(request);

        Profesor profesor = buscarO404(id);
        asignarDatos(profesor, request);

        // Elimino primero los teléfonos anteriores para evitar conflictos
        // con la restricción UNIQUE (ProfesorId, Telefono).
        profesor.getTelefonos().clear();
        repositorio.flush();

        profesor.reemplazarTelefonos(normalizarTelefonos(request.telefonos()));

        try {
            return ProfesorResponse.desde(
                repositorio.saveAndFlush(profesor)
            );
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "El email o la identificación ya está registrada",
                ex
            );
        }
    }

    @Transactional
    public void eliminar(Integer id) {
        Profesor profesor = buscarO404(id);

        if (tieneGruposActivos(id, LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El profesor tiene grupos activos");
        }

        try {
            repositorio.delete(profesor);
            repositorio.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "No fue posible eliminar el profesor por una referencia activa",
                ex
            );
        }
    }

    boolean tieneGruposActivos(Integer profesorId, LocalDate hoy) {
        var grupos = datos.listar("Grupo", "Grupo", java.util.Map.of("ProfesorId", profesorId));
        for (var grupo : grupos) {
            Object periodoId = cr.ac.cuc.nuevoavatar.integracion.DatosRemotos.valor(grupo, "PeriodoId");
            var periodo = datos.buscar("Periodo", "Periodo", "PeriodoId", periodoId);
            if (periodo.isEmpty()) throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "El periodo del grupo no está disponible");
            LocalDate inicio = cr.ac.cuc.nuevoavatar.integracion.DatosRemotos.fecha(periodo.get(), "FechaInicio");
            LocalDate fin = cr.ac.cuc.nuevoavatar.integracion.DatosRemotos.fecha(periodo.get(), "FechaFin");
            if (!hoy.isBefore(inicio) && !hoy.isAfter(fin)) return true;
        }
        return false;
    }

    private Profesor buscarO404(Integer id) {
        return repositorio.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Profesor no encontrado"
            ));
    }

    private void validar(ProfesorRequest request) {
        if (request.fechaNacimiento().isAfter(
                LocalDate.now().minusYears(18))) {
            throw new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_CONTENT,
                "El profesor debe ser mayor de edad"
            );
        }

        var parametro = datos.buscar("Parametro","parametro","id_parametro","DOMPROF").orElseThrow(() -> new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"Falta DOMPROF"));
        String dominio = cr.ac.cuc.nuevoavatar.integracion.DatosRemotos.texto(parametro,"valor");

        if (dominio == null || dominio.isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "El parámetro DOMPROF está vacío"
            );
        }

        String email = request.email().trim().toLowerCase(Locale.ROOT);
        String sufijo = "@" + dominio.trim().toLowerCase(Locale.ROOT);

        if (!email.endsWith(sufijo)) {
            throw new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_CONTENT,
                "El email debe pertenecer al dominio " + dominio
            );
        }

        normalizarTelefonos(request.telefonos());
    }

   private List<String> normalizarTelefonos(List<String> telefonos) {
    List<String> numeros = new java.util.ArrayList<>();

    for (String telefono : telefonos) {
        numeros.add(telefono.trim());
    }

    Set<String> unicos = new HashSet<>(numeros);
    if (unicos.size() != numeros.size()) {
        throw new ResponseStatusException(
            HttpStatus.UNPROCESSABLE_CONTENT,
            "No repita un teléfono del mismo profesor"
        );
    }

    return numeros;
}
    private void asignarDatos(Profesor profesor, ProfesorRequest request) {
        profesor.setTipoIdentificacion(
            request.tipoIdentificacion().trim()
        );
        profesor.setIdentificacion(
            request.identificacion().trim()
        );
        profesor.setEmail(
            request.email().trim().toLowerCase(Locale.ROOT)
        );
        profesor.setNombreCompleto(
            request.nombreCompleto().trim()
        );
        profesor.setFechaNacimiento(
            request.fechaNacimiento()
        );
    }
}

