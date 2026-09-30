package cr.ac.cuc.nuevoavatar.persona2.usuario;

import java.util.List;
import java.util.Locale;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarios;
    private final JdbcTemplate jdbc;
    private final BCryptPasswordEncoder encoder;

    public UsuarioService(
            UsuarioRepository usuarios,
            JdbcTemplate jdbc,
            BCryptPasswordEncoder encoder) {
        this.usuarios = usuarios;
        this.jdbc = jdbc;
        this.encoder = encoder;
    }

    @Transactional
    public UsuarioResponse crear(CrearUsuarioRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        String rol = request.idRol().trim().toUpperCase(Locale.ROOT);

        if (usuarios.existsById(email)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "El email ya está registrado"
            );
        }

        validarRolYDominio(email, rol);

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setTipoIdentificacion(request.tipoIdentificacion().trim());
        usuario.setIdentificacion(request.identificacion().trim());
        usuario.setNombre(request.nombre().trim());
        usuario.setIdRol(rol);
        usuario.setPasswordHash(encoder.encode(request.contrasena()));

        try {
            return UsuarioResponse.desde(usuarios.saveAndFlush(usuario));
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "No se pudo crear el usuario", ex
            );
        }
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtener(String email) {
        return UsuarioResponse.desde(buscarO404(email));
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar(
            String identificacion,
            String nombre,
            String tipo) {

        Specification<Usuario> filtro = (root, query, cb) ->
            cb.conjunction();

        if (tieneTexto(identificacion)) {
            String valor = "%" + identificacion.trim().toLowerCase(Locale.ROOT) + "%";
            filtro = filtro.and((root, query, cb) ->
                cb.like(cb.lower(root.get("identificacion")), valor));
        }

        if (tieneTexto(nombre)) {
            String valor = "%" + nombre.trim().toLowerCase(Locale.ROOT) + "%";
            filtro = filtro.and((root, query, cb) ->
                cb.like(cb.lower(root.get("nombre")), valor));
        }

        if (tieneTexto(tipo)) {
            String valor = "%" + tipo.trim().toLowerCase(Locale.ROOT) + "%";
            filtro = filtro.and((root, query, cb) ->
                cb.like(cb.lower(root.get("tipoIdentificacion")), valor));
        }

        return usuarios.findAll(filtro).stream()
            .map(UsuarioResponse::desde)
            .toList();
    }

    @Transactional
    public UsuarioResponse modificar(
            String email,
            ActualizarUsuarioRequest request) {

        Usuario usuario = buscarO404(email);
        String rol = request.idRol().trim().toUpperCase(Locale.ROOT);

        validarRolYDominio(usuario.getEmail(), rol);

        usuario.setTipoIdentificacion(request.tipoIdentificacion().trim());
        usuario.setIdentificacion(request.identificacion().trim());
        usuario.setNombre(request.nombre().trim());
        usuario.setIdRol(rol);

        if (request.contrasena() != null) {
            if (request.contrasena().isBlank()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La contraseña no puede ser vacía"
                );
            }
            usuario.setPasswordHash(encoder.encode(request.contrasena()));
        }

        try {
            return UsuarioResponse.desde(usuarios.saveAndFlush(usuario));
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "No se pudo modificar el usuario", ex
            );
        }
    }

    @Transactional
    public void eliminar(String email) {
        Usuario usuario = buscarO404(email);

        try {
            usuarios.delete(usuario);
            usuarios.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "El usuario tiene registros relacionados",
                ex
            );
        }
    }

    private Usuario buscarO404(String email) {
        return usuarios.findById(email.trim().toLowerCase(Locale.ROOT))
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado"
            ));
    }

    private void validarRolYDominio(String email, String rol) {
        Integer cantidad = jdbc.queryForObject(
            "SELECT COUNT(*) FROM seguridad.rol WHERE id_rol = ?",
            Integer.class,
            rol
        );

        if (cantidad == null || cantidad == 0) {
            throw new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_CONTENT, "El rol no existe"
            );
        }

        if (email.endsWith("@cuc.cr")) {
            if (!rol.equals("ESTUDIANTE")) {
                throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_CONTENT,
                    "El dominio cuc.cr requiere el rol ESTUDIANTE"
                );
            }
        } else if (email.endsWith("@cuc.ac.cr")) {
            if (!rol.equals("PROFESOR") && !rol.equals("ADMIN")) {
                throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_CONTENT,
                    "El dominio cuc.ac.cr requiere PROFESOR o ADMIN"
                );
            }
        } else {
            throw new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_CONTENT,
                "El email debe pertenecer a cuc.cr o cuc.ac.cr"
            );
        }
    }

    private boolean tieneTexto(String valor) {
        return valor != null && !valor.isBlank();
    }
}
