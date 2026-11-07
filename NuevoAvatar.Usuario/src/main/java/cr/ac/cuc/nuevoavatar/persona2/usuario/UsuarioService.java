package cr.ac.cuc.nuevoavatar.persona2.usuario;

import java.util.List;
import java.util.Locale;
import java.util.LinkedHashMap;
import java.util.Map;
import cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilter;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarios;
    private final cr.ac.cuc.nuevoavatar.integracion.DatosRemotos datos;
    private final BCryptPasswordEncoder encoder;

    public UsuarioService(
            UsuarioRepository usuarios,
            cr.ac.cuc.nuevoavatar.integracion.DatosRemotos datos,
            BCryptPasswordEncoder encoder) {
        this.usuarios = usuarios;
        this.datos = datos;
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
            UsuarioResponse actual = UsuarioResponse.desde(usuarios.saveAndFlush(usuario));
            BitacoraFilter.cambio("Creación de usuario", null, estado(usuario));
            return actual;
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
        Map<String, Object> anterior = estado(usuario);

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
            UsuarioResponse actual = UsuarioResponse.desde(usuarios.saveAndFlush(usuario));
            BitacoraFilter.cambio("Modificación de usuario", anterior, estado(usuario));
            return actual;
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
            BitacoraFilter.cambio("Eliminación de usuario", estado(usuario), null);
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
        var registro = datos.buscar("Rol", "rol", "id_rol", rol)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_ENTITY, "El rol no existe"));
        String perfil = cr.ac.cuc.nuevoavatar.integracion.DatosRemotos
            .texto(registro, "nombre_rol").trim().toUpperCase(Locale.ROOT);

        if (email.endsWith("@cuc.cr")) {
            if (!perfil.equals("ESTUDIANTE")) {
                throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_CONTENT,
                    "El dominio cuc.cr requiere el rol ESTUDIANTE"
                );
            }
        } else if (email.endsWith("@cuc.ac.cr")) {
            if (!perfil.equals("PROFESOR") && !perfil.equals("ADMIN")
                    && !perfil.equals("ADMINISTRADOR")) {
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

    private Map<String, Object> estado(Usuario usuario) {
        Map<String, Object> datos = new LinkedHashMap<>();
        datos.put("email", usuario.getEmail());
        datos.put("tipoIdentificacion", usuario.getTipoIdentificacion());
        datos.put("identificacion", usuario.getIdentificacion());
        datos.put("nombre", usuario.getNombre());
        datos.put("idRol", usuario.getIdRol());
        datos.put("fechaModificacion", usuario.getFechaModificacion() == null
            ? null : usuario.getFechaModificacion().toString());
        return datos;
    }
}

