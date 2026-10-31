package cr.ac.cuc.nuevoavatar.persona2.carrera;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
import java.util.Optional;
import org.junit.jupiter.api.Test;
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
}
