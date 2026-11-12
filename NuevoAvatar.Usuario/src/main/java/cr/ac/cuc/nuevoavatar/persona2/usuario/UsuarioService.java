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
    private static final String PARAMETRO_PASSMIN = "PASSMIN";
    private static final String PARAMETRO_PASSMAX = "PASSMAX";
    private static final String PARAMETRO_PASSMAY = "PASSMAY";
    private static final String PARAMETRO_PASSNUM = "PASSNUM";

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
        String nombreAnterior = usuario.getNombre();
        usuario.setNombre(request.nombre().trim());
        UsuarioResponse actual = UsuarioResponse.desde(usuarios.saveAndFlush(usuario));
        String descripcion = BitacoraFilter.descripcionCambio(
                "Actualización del perfil propio",
                java.util.Map.of("nombre", nombreAnterior),
                java.util.Map.of("nombre", actual.nombre()));
        seguridad.registrarBitacoraConfirmada(bearerOriginal, sujetoAutenticado, descripcion);
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
        PoliticaContrasenaResponse politica = leerPoliticaContrasena();
        validarPoliticaContrasena(request.contrasenaNueva(), politica);
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

    public PoliticaContrasenaResponse obtenerPoliticaContrasena(String usuarioAutenticado) {
        if (usuarioAutenticado == null || usuarioAutenticado.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No hay una sesión autenticada");
        }
        return leerPoliticaContrasena();
    }

    private PoliticaContrasenaResponse leerPoliticaContrasena() {
        int minimo = enteroParametro(PARAMETRO_PASSMIN);
        int maximo = enteroParametro(PARAMETRO_PASSMAX);
        boolean requiereMayuscula = booleanoParametro(PARAMETRO_PASSMAY);
        boolean requiereNumero = booleanoParametro(PARAMETRO_PASSNUM);
        if (minimo > maximo) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "La política institucional de contraseña tiene límites incompatibles");
        }
        return new PoliticaContrasenaResponse(minimo, maximo, requiereMayuscula,
                requiereNumero, MAX_BCRYPT_PASSWORD_BYTES);
    }

    private int enteroParametro(String id) {
        String valor = requerirParametro(id);
        try {
            int numero = Integer.parseInt(valor);
            if (numero > 0) return numero;
        } catch (NumberFormatException ignored) {
            // Se informa como política incompleta o inválida, sin aplicar valores predeterminados.
        }
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "El parámetro institucional " + id + " no contiene un entero positivo válido");
    }

    private boolean booleanoParametro(String id) {
        String valor = requerirParametro(id);
        if ("true".equalsIgnoreCase(valor)) return true;
        if ("false".equalsIgnoreCase(valor)) return false;
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "El parámetro institucional " + id + " debe ser true o false");
    }

    private String requerirParametro(String id) {
        Map<String, Object> parametro = datos.buscar("Parametro", "parametro", "id_parametro", id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "Falta el parámetro institucional requerido " + id));
        String valor = cr.ac.cuc.nuevoavatar.integracion.DatosRemotos.texto(parametro, "valor");
        if (valor == null || valor.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Falta el parámetro institucional requerido " + id);
        }
        return valor;
    }

    private void validarPoliticaContrasena(String contrasena, PoliticaContrasenaResponse politica) {
        int longitud = contrasena.codePointCount(0, contrasena.length());
        boolean mayuscula = contrasena.codePoints().anyMatch(c ->
                Character.getType(c) == Character.UPPERCASE_LETTER);
        boolean numero = contrasena.codePoints().anyMatch(c ->
                Character.getType(c) == Character.DECIMAL_DIGIT_NUMBER);
        if (longitud < politica.minLength() || longitud > politica.maxLength()
                || (politica.requireUppercase() && !mayuscula)
                || (politica.requireNumber() && !numero)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    mensajePoliticaInvalida(politica));
        }
    }

    private String mensajePoliticaInvalida(PoliticaContrasenaResponse politica) {
        StringBuilder mensaje = new StringBuilder("La nueva contraseña debe tener entre ")
                .append(politica.minLength()).append(" y ").append(politica.maxLength()).append(" caracteres");
        if (politica.requireUppercase()) mensaje.append(", al menos una mayúscula");
        if (politica.requireNumber()) mensaje.append(", al menos un número");
        return mensaje.toString();
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
                HttpStatus.UNPROCESSABLE_CONTENT, "El rol no existe"));
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

