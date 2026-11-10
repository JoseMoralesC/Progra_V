package cr.ac.cuc.nuevoavatar.persona2.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
import cr.ac.cuc.nuevoavatar.persona2.seguridad.SeguridadClient;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

class UsuarioPerfilPropioTest {

    @Test
    void actualizaSoloElNombreDeLaCuentaDerivadaDelSujeto() {
        UsuarioRepository repositorio = mock(UsuarioRepository.class);
        Usuario usuario = new Usuario();
        usuario.setEmail("ana@cuc.cr");
        usuario.setNombre("Ana Quesada");
        usuario.setTipoIdentificacion("DIMEX");
        usuario.setIdentificacion("123456789");
        usuario.setIdRol("EST");
        usuario.setPasswordHash("hash-existente");
        when(repositorio.findById("ana@cuc.cr")).thenReturn(Optional.of(usuario));
        when(repositorio.saveAndFlush(usuario)).thenReturn(usuario);

        UsuarioService servicio = new UsuarioService(repositorio, mock(DatosRemotos.class),
                mock(BCryptPasswordEncoder.class), mock(SeguridadClient.class));
        UsuarioResponse respuesta = servicio.actualizarPerfilPropio(
                "Bearer valido", "ana@cuc.cr", new ActualizarPerfilPropioRequest(" Ana Morales "));

        assertEquals("Ana Morales", respuesta.nombre());
        assertEquals("ana@cuc.cr", usuario.getEmail());
        assertEquals("DIMEX", usuario.getTipoIdentificacion());
        assertEquals("123456789", usuario.getIdentificacion());
        assertEquals("EST", usuario.getIdRol());
        assertEquals("hash-existente", usuario.getPasswordHash());
        assertNull(usuario.getFechaModificacion());
        verify(repositorio).saveAndFlush(usuario);
    }

    @Test
    void cambiaContrasenaDelSujetoYGuardaSoloHashBcrypt() {
        UsuarioRepository repositorio = mock(UsuarioRepository.class);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Usuario usuario = usuario(encoder.encode("ActualSeguro1!"));
        when(repositorio.findById("ana@cuc.cr")).thenReturn(Optional.of(usuario));
        when(repositorio.saveAndFlush(usuario)).thenReturn(usuario);
        UsuarioService servicio = new UsuarioService(repositorio, mock(DatosRemotos.class), encoder,
                mock(SeguridadClient.class));

        servicio.cambiarContrasenaPropia("Bearer valido", "ana@cuc.cr",
                new CambiarContrasenaPropiaRequest("ActualSeguro1!", "NuevaSegura2#"));

        assertFalse("ActualSeguro1!".equals(usuario.getPasswordHash()));
        assertTrue(encoder.matches("NuevaSegura2#", usuario.getPasswordHash()));
        assertFalse(encoder.matches("ActualSeguro1!", usuario.getPasswordHash()));
        assertEquals("ana@cuc.cr", usuario.getEmail());
        assertEquals("123456789", usuario.getIdentificacion());
        verify(repositorio).saveAndFlush(usuario);
    }

    @Test
    void rechazaContrasenaActualIncorrectaSinPersistir() {
        UsuarioRepository repositorio = mock(UsuarioRepository.class);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Usuario usuario = usuario(encoder.encode("ActualSeguro1!"));
        when(repositorio.findById("ana@cuc.cr")).thenReturn(Optional.of(usuario));
        UsuarioService servicio = new UsuarioService(repositorio, mock(DatosRemotos.class), encoder,
                mock(SeguridadClient.class));

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> servicio.cambiarContrasenaPropia("Bearer valido", "ana@cuc.cr",
                        new CambiarContrasenaPropiaRequest("incorrecta", "NuevaSegura2#")));

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        verify(repositorio, never()).saveAndFlush(usuario);
    }

    @Test
    void validaLongitudMayusculaMinusculaNumeroYCaracterEspecialEnServidor() {
        UsuarioService servicio = new UsuarioService(mock(UsuarioRepository.class),
                mock(DatosRemotos.class), new BCryptPasswordEncoder(), mock(SeguridadClient.class));
        for (String invalida : new String[] {
                "Ab1!", "lowercase1!", "UPPERCASE1!", "Uppercase!!", "Uppercase1", "Upper case1"
        }) {
            ResponseStatusException error = assertThrows(ResponseStatusException.class,
                    () -> servicio.cambiarContrasenaPropia("Bearer valido", "ana@cuc.cr",
                            new CambiarContrasenaPropiaRequest("ActualSeguro1!", invalida)), invalida);
            assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode(), invalida);
        }
    }

    private static Usuario usuario(String hash) {
        Usuario usuario = new Usuario();
        usuario.setEmail("ana@cuc.cr");
        usuario.setNombre("Ana Quesada");
        usuario.setTipoIdentificacion("DIMEX");
        usuario.setIdentificacion("123456789");
        usuario.setIdRol("EST");
        usuario.setPasswordHash(hash);
        return usuario;
    }
}
