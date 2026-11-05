package cr.ac.cuc.nuevoavatar.persona5.notas;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cr.ac.cuc.nuevoavatar.persona5.academico.Grupo;
import cr.ac.cuc.nuevoavatar.persona5.academico.GrupoRepository;
import cr.ac.cuc.nuevoavatar.persona5.matricula.Matricula;
import cr.ac.cuc.nuevoavatar.persona5.matricula.MatriculaRepository;
import cr.ac.cuc.nuevoavatar.persona5.seguridad.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.server.ResponseStatusException;

class SeguridadNotasTest {
    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final GrupoRepository grupos = mock(GrupoRepository.class);
    private final MatriculaRepository matriculas = mock(MatriculaRepository.class);
    private final AccesoNotas acceso = new AccesoNotas(request, grupos, matriculas);

    @Test void estudianteNoPuedeEscribirNiConsultarNotasDelProfesor() {
        sesion("ESTUDIANTE", null);
        assertEquals(HttpStatus.FORBIDDEN, assertThrows(ResponseStatusException.class,
            () -> acceso.matricula(1)).getStatusCode());
        verifyNoInteractions(matriculas, grupos);
    }

    @Test void headersDelClienteNoOtorganIdentidad() {
        request.addHeader("X-Rol", "PROFESOR");
        request.addHeader("X-Profesor-Id", "9");
        assertEquals(HttpStatus.UNAUTHORIZED, assertThrows(ResponseStatusException.class,
            () -> acceso.grupo(1)).getStatusCode());
    }

    @Test void profesorNoPuedeConsultarOtroGrupo() {
        sesion("PROFESOR", 9);
        grupo(10, 2, 3);
        assertEquals(HttpStatus.FORBIDDEN, assertThrows(ResponseStatusException.class,
            () -> acceso.grupo(1)).getStatusCode());
    }

    @Test void profesorPuedeAccederSoloAGrupoPropio() {
        sesion("PROFESOR", 9);
        Grupo propio = grupo(9, 2, 3);
        assertSame(propio, acceso.grupo(1));
        assertEquals(HttpStatus.FORBIDDEN, assertThrows(ResponseStatusException.class,
            () -> acceso.consulta(999, 1)).getStatusCode());
    }

    @Test void matriculaConPeriodoDiferenteEsRechazada() {
        sesion("PROFESOR", 9);
        grupo(9, 2, 3);
        Matricula m = new Matricula();
        m.setGrupoId(1); m.setCursoId(2); m.setPeriodoId(99);
        when(matriculas.findById(5)).thenReturn(Optional.of(m));
        assertEquals(HttpStatus.FORBIDDEN, assertThrows(ResponseStatusException.class,
            () -> acceso.matricula(5)).getStatusCode());
    }

    @Test void dependenciaCaidaNoSeConvierteEnGrupoVacio() {
        sesion("PROFESOR", 9);
        when(grupos.findById(1)).thenThrow(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE));
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, assertThrows(ResponseStatusException.class,
            () -> acceso.grupo(1)).getStatusCode());
    }

    @Test void interceptorUsaIdentidadValidadaYNoElPayloadDelNavegador() throws Exception {
        SeguridadClient seguridad = mock(SeguridadClient.class);
        request.addHeader("Authorization", "Bearer payload-no-confiable");
        when(seguridad.validar(anyString())).thenReturn(true);
        when(seguridad.obtenerSesion(anyString())).thenReturn(new SesionActual("real@cuc.ac.cr", "Real",
            "PROFESOR", "CEDULA", "123", null, 9));
        assertTrue(new AutorizacionInterceptor(seguridad).preHandle(request, new MockHttpServletResponse(), new Object()));
        assertEquals("real@cuc.ac.cr", request.getAttribute(AutorizacionInterceptor.USER_ATTRIBUTE));
    }

    @Test void tokenExpiradoNoResuelveIdentidad() throws Exception {
        SeguridadClient seguridad = mock(SeguridadClient.class);
        request.addHeader("Authorization", "Bearer expirado");
        when(seguridad.validar(anyString())).thenReturn(false);
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertFalse(new AutorizacionInterceptor(seguridad).preHandle(request, response, new Object()));
        assertEquals(401, response.getStatus());
        verify(seguridad, never()).obtenerSesion(anyString());
    }

    private void sesion(String rol, Integer id) {
        request.setAttribute(AutorizacionInterceptor.SESSION_ATTRIBUTE,
            new SesionActual("cuenta@cuc.ac.cr", "Cuenta", rol, "CEDULA", "123", null, id));
    }
    private Grupo grupo(int profesor, int curso, int periodo) {
        Grupo grupo = mock(Grupo.class);
        when(grupo.getProfesorId()).thenReturn(profesor);
        when(grupo.getCursoId()).thenReturn(curso);
        when(grupo.getPeriodoId()).thenReturn(periodo);
        when(grupos.findById(1)).thenReturn(Optional.of(grupo));
        return grupo;
    }
}
