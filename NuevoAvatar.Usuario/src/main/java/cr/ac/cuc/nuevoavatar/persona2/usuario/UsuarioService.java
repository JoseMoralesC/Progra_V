package cr.ac.cuc.nuevoavatar.persona2.usuario;

import java.util.List;
import java.util.Locale;
import java.util.LinkedHashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilter;
import cr.ac.cuc.nuevoavatar.persona2.seguridad.SeguridadClient;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    private static final int MAX_BCRYPT_PASSWORD_BYTES = 72;

    private final UsuarioRepository usuarios;
    private final cr.ac.cuc.nuevoavatar.integracion.DatosRemotos datos;
    private final BCryptPasswordEncoder encoder;
    private final SeguridadClient seguridad;

    public UsuarioService(
            UsuarioRepository usuarios,
            cr.ac.cuc.nuevoavatar.integracion.DatosRemotos datos,
            BCryptPasswordEncoder encoder,
            SeguridadClient seguridad) {
        this.usuarios = usuarios;
        this.datos = datos;
        this.encoder = encoder;
        this.seguridad = seguridad;
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
    public UsuarioResponse obtenerPerfilPropio(String sujetoAutenticado) {
        return UsuarioResponse.desde(buscarO404(sujetoAutenticado));
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
    public UsuarioResponse actualizarPerfilPropio(
            String bearerOriginal,
            String sujetoAutenticado,
            ActualizarPerfilPropioRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es requerido");
        }
        Usuario usuario = buscarO404(sujetoAutenticado);
        usuario.setNombre(request.nombre().trim());
        UsuarioResponse actual = UsuarioResponse.desde(usuarios.saveAndFlush(usuario));
        seguridad.registrarBitacoraConfirmada(bearerOriginal, sujetoAutenticado,
                "Actualización del perfil propio. Campo modificado: nombre");
        BitacoraFilter.marcarRegistroConfirmado();
        return actual;
    }

    @Transactional
    public void cambiarContrasenaPropia(
            String bearerOriginal,
            String sujetoAutenticado,
            CambiarContrasenaPropiaRequest request) {
        if (request == null || request.contrasenaActual() == null || request.contrasenaActual().isBlank()
                || request.contrasenaNueva() == null || request.contrasenaNueva().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Completa ambas contraseñas");
        }
        validarPoliticaContrasena(request.contrasenaNueva());
        validarLimiteBcrypt(request.contrasenaNueva());

        Usuario usuario = buscarO404(sujetoAutenticado);
        boolean actualCoincide;
        try {
            actualCoincide = encoder.matches(request.contrasenaActual(), usuario.getPasswordHash());
        } catch (IllegalArgumentException ex) {
            actualCoincide = false;
        }
        if (!actualCoincide) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña actual no es correcta");
        }

        usuario.setPasswordHash(encoder.encode(request.contrasenaNueva()));
        usuarios.saveAndFlush(usuario);
        seguridad.registrarBitacoraConfirmada(bearerOriginal, sujetoAutenticado,
                "Cambio de contraseña del perfil propio. Campo modificado: contrasena");
        BitacoraFilter.marcarRegistroConfirmado();
    }

    private void validarLimiteBcrypt(String contrasena) {
        int bytesUtf8 = contrasena.getBytes(StandardCharsets.UTF_8).length;
        if (bytesUtf8 > MAX_BCRYPT_PASSWORD_BYTES) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña no puede superar 72 bytes en UTF-8 al usar BCrypt");
        }
    }

    private void validarPoliticaContrasena(String contrasena) {
        int longitud = contrasena.codePointCount(0, contrasena.length());
        boolean mayuscula = contrasena.codePoints().anyMatch(c ->
                Character.getType(c) == Character.UPPERCASE_LETTER);
        boolean minuscula = contrasena.codePoints().anyMatch(c ->
                Character.getType(c) == Character.LOWERCASE_LETTER);
        boolean numero = contrasena.codePoints().anyMatch(c ->
                Character.getType(c) == Character.DECIMAL_DIGIT_NUMBER);
        boolean especial = contrasena.codePoints().anyMatch(this::esCaracterEspecial);
        if (longitud < 8 || !mayuscula || !minuscula || !numero || !especial) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La nueva contraseña debe tener al menos 8 caracteres, una mayúscula, "
                    + "una minúscula, un número y un carácter especial");
        }
    }

    private boolean esCaracterEspecial(int caracter) {
        int tipo = Character.getType(caracter);
        return tipo == Character.CONNECTOR_PUNCTUATION
                || tipo == Character.DASH_PUNCTUATION
                || tipo == Character.START_PUNCTUATION
                || tipo == Character.END_PUNCTUATION
                || tipo == Character.INITIAL_QUOTE_PUNCTUATION
                || tipo == Character.FINAL_QUOTE_PUNCTUATION
                || tipo == Character.OTHER_PUNCTUATION
                || tipo == Character.MATH_SYMBOL
                || tipo == Character.CURRENCY_SYMBOL
                || tipo == Character.MODIFIER_SYMBOL
                || tipo == Character.OTHER_SYMBOL;
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

