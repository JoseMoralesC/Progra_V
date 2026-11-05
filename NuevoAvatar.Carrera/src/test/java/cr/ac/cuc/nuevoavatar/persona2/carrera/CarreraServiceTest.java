package cr.ac.cuc.nuevoavatar.persona2.carrera;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
import cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilter;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

class CarreraServiceTest {
    private final CarreraRepository carreras = mock(CarreraRepository.class);
    private final DatosRemotos datos = mock(DatosRemotos.class);
    private final CarreraService servicio = new CarreraService(carreras, datos);

    @Test
    void validaInstitucionYProfesorEnSusServiciosPropietarios() {
        CarreraRequest request = new CarreraRequest("Ingeniería", 4, 7);
        when(carreras.saveAndFlush(any(Carrera.class))).thenAnswer(x -> x.getArgument(0));
        servicio.crear(request);
        verify(datos).exigir("Institucion", "Institucion", "InstitucionId", 4);
        verify(datos).exigir("Profesor", "Profesor", "ProfesorId", 7);
    }

    @Test
    void bloqueaEliminarCuandoExistenCursos() {
        Carrera carrera = mock(Carrera.class);
        when(carreras.findById(3)).thenReturn(Optional.of(carrera));
        when(datos.referencias("Curso", "Curso", "CarreraId", 3)).thenReturn(true);
        ResponseStatusException error = assertThrows(ResponseStatusException.class, () -> servicio.eliminar(3));
        assertEquals(409, error.getStatusCode().value());
        verify(carreras, never()).delete(any());
    }

    @Test
    void registraSnapshotsJsonEnModificacionYEliminacion() {
        MockHttpServletRequest http = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(http));
        try {
            Carrera existente = new Carrera();
            existente.setNombre("Informática");
            existente.setInstitucionId(1);
            existente.setDirectorProfesorId(2);
            when(carreras.findById(5)).thenReturn(Optional.of(existente));
            when(carreras.saveAndFlush(any(Carrera.class))).thenAnswer(x -> x.getArgument(0));

            servicio.modificar(5, new CarreraRequest("Ingeniería de Software", 1, 3));
            String mod = String.valueOf(http.getAttribute(BitacoraFilter.DETALLE_ATTRIBUTE));
            assertTrue(mod.contains("Anterior:"));
            assertTrue(mod.contains("Informática"));
            assertTrue(mod.contains("Actual:"));
            assertTrue(mod.contains("Ingeniería de Software"));

            servicio.eliminar(5);
            String elim = String.valueOf(http.getAttribute(BitacoraFilter.DETALLE_ATTRIBUTE));
            assertTrue(elim.contains("Eliminación de carrera:"));
            assertTrue(elim.contains("Ingeniería de Software"));
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }
}
