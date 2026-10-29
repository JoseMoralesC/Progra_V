package cr.ac.cuc.nuevoavatar.persona5.notas;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cr.ac.cuc.nuevoavatar.persona5.academico.Grupo;
import cr.ac.cuc.nuevoavatar.persona5.academico.GrupoRepository;
import cr.ac.cuc.nuevoavatar.persona5.error.ApiExceptionHandler;
import cr.ac.cuc.nuevoavatar.persona5.matricula.Matricula;
import cr.ac.cuc.nuevoavatar.persona5.matricula.MatriculaRepository;
import cr.ac.cuc.nuevoavatar.persona5.seguridad.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

class NotasHttpTest {
    private final NotasService servicio = mock(NotasService.class);

    @Test void estudianteNoPuedeAsignarNotaPorHttp() throws Exception {
        mvc("ESTUDIANTE", null, 9).perform(post("/asignarnotarubro")
            .header("Authorization", "Bearer valido").contentType("application/json")
            .content("{\"matriculaId\":5,\"rubroId\":2,\"nota\":75}"))
            .andExpect(status().isForbidden());
        verifyNoInteractions(servicio);
    }

    @Test void profesorAjenoNoPuedeModificarNotaPorHttp() throws Exception {
        mvc("PROFESOR", 10, 9).perform(put("/asignarnotarubro")
            .header("Authorization", "Bearer valido").contentType("application/json")
            .content("{\"matriculaId\":5,\"rubroId\":2,\"nota\":75}"))
            .andExpect(status().isForbidden());
        verifyNoInteractions(servicio);
    }

    @Test void profesorPropioUsaOperacionCorrecta() throws Exception {
        mvc("PROFESOR", 9, 9).perform(put("/asignarnotarubro")
            .header("Authorization", "Bearer valido").contentType("application/json")
            .content("{\"matriculaId\":5,\"rubroId\":2,\"nota\":75}"))
            .andExpect(status().isOk());
        verify(servicio).asignarNota(any(AsignarNotaRequest.class), eq(true));
    }

    @Test void notaInvalidaNoLlegaALaEscritura() throws Exception {
        mvc("PROFESOR", 9, 9).perform(post("/asignarnotarubro")
            .header("Authorization", "Bearer valido").contentType("application/json")
            .content("{\"matriculaId\":5,\"rubroId\":2,\"nota\":0}"))
            .andExpect(status().isBadRequest());
        verifyNoInteractions(servicio);
    }

    @Test void sinTokenNoPuedeConsultarGrupo() throws Exception {
        mvc("PROFESOR", 9, 9).perform(get("/obtenernotasgrupo").param("grupoId", "1"))
            .andExpect(status().isUnauthorized());
        verifyNoInteractions(servicio);
    }

    @Test void grupoInvalidoDevuelve400() throws Exception {
        mvc("PROFESOR", 9, 9).perform(get("/obtenernotasgrupo")
            .header("Authorization", "Bearer valido").param("grupoId", "-1"))
            .andExpect(status().isBadRequest());
        verifyNoInteractions(servicio);
    }

    private MockMvc mvc(String rol, Integer profesor, int propietario) {
        var seguridad = mock(SeguridadClient.class);
        when(seguridad.validar(anyString())).thenReturn(true);
        when(seguridad.obtenerSesion(anyString())).thenReturn(new SesionActual("real@cuc.ac.cr", "Real", rol,
            "CEDULA", "123", null, profesor));
        var request = mock(HttpServletRequest.class);
        when(request.getAttribute(anyString())).thenAnswer(i ->
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getAttribute(i.getArgument(0)));
        var grupos = mock(GrupoRepository.class);
        var matriculas = mock(MatriculaRepository.class);
        var grupo = mock(Grupo.class);
        when(grupo.getProfesorId()).thenReturn(propietario);
        when(grupo.getCursoId()).thenReturn(2);
        when(grupo.getPeriodoId()).thenReturn(3);
        when(grupos.findById(1)).thenReturn(Optional.of(grupo));
        Matricula matricula = new Matricula();
        matricula.setGrupoId(1); matricula.setCursoId(2); matricula.setPeriodoId(3);
        when(matriculas.findById(5)).thenReturn(Optional.of(matricula));
        return MockMvcBuilders.standaloneSetup(new NotasController(servicio, new AccesoNotas(request, grupos, matriculas)))
            .setControllerAdvice(new ApiExceptionHandler()).addInterceptors(new AutorizacionInterceptor(seguridad)).build();
    }
}
