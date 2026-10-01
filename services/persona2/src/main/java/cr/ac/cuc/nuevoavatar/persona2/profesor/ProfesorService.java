package cr.ac.cuc.nuevoavatar.persona2.profesor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProfesorService {

    private final ProfesorRepository repositorio;
    private final JdbcTemplate jdbc;

    public ProfesorService(ProfesorRepository repositorio, JdbcTemplate jdbc) {
        this.repositorio = repositorio;
        this.jdbc = jdbc;
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
    public ProfesorResponse obtener(Integer id) {
        return ProfesorResponse.desde(buscarO404(id));
    }

    @Transactional
    public ProfesorResponse modificar(Integer id, ProfesorRequest request) {
        validar(request);

        Profesor profesor = buscarO404(id);
        asignarDatos(profesor, request);

        // Eliminar primero los teléfonos anteriores evita conflictos
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

        try {
            repositorio.delete(profesor);
            repositorio.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "El profesor está relacionado con una carrera o un grupo",
                ex
            );
        }
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

        String dominio;
        try {
            dominio = jdbc.queryForObject(
                """
                SELECT valor
                FROM seguridad.parametro
                WHERE id_parametro = 'DOMPROF'
                """,
                String.class
            );
        } catch (EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Falta configurar el parámetro DOMPROF",
                ex
            );
        }

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