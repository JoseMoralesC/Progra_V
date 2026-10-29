package cr.ac.cuc.nuevoavatar.persona5.notas;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class ResumenProfesorTest {
    private final DatosRemotos datos = mock(DatosRemotos.class);

    @Test void resumenConsultaSoloProfesorPropioYPeriodoActivo() {
        var grupo = Map.<String, Object>of("GrupoId", 1, "NumeroGrupo", 2, "CursoId", 3,
            "PeriodoId", 4, "Horario", "Lunes 18:00");
        when(datos.listar("Grupo", "Grupo", Map.of("ProfesorId", 9))).thenReturn(List.of(grupo));
        when(datos.buscar("Periodo", "Periodo", "PeriodoId", 4)).thenReturn(Optional.of(Map.of(
            "FechaInicio", LocalDate.now().minusDays(1).toString(), "FechaFin", LocalDate.now().plusDays(1).toString())));
        when(datos.buscar("Curso", "Curso", "CursoId", 3)).thenReturn(Optional.of(Map.of("Nombre", "Programacion")));
        var resultado = new ResumenProfesorService(datos).obtener(9, null);
        assertEquals(1, resultado.size());
        assertEquals("Programacion", resultado.getFirst().curso());
        verify(datos).listar("Grupo", "Grupo", Map.of("ProfesorId", 9));
    }

    @Test void gruposDePeriodosCerradosNoAparecenEnResumenActual() {
        when(datos.listar("Grupo", "Grupo", Map.of("ProfesorId", 9))).thenReturn(List.of(Map.of("PeriodoId", 4)));
        when(datos.buscar("Periodo", "Periodo", "PeriodoId", 4)).thenReturn(Optional.of(Map.of(
            "FechaInicio", LocalDate.now().minusDays(20).toString(), "FechaFin", LocalDate.now().minusDays(1).toString())));
        assertTrue(new ResumenProfesorService(datos).obtener(9, null).isEmpty());
        verify(datos, never()).buscar(eq("Curso"), anyString(), anyString(), any());
    }

    @Test void falloDeGrupoNoSePresentaComoResumenSinDatos() {
        when(datos.listar("Grupo", "Grupo", Map.of("ProfesorId", 9))).thenThrow(
            new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE));
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, assertThrows(ResponseStatusException.class,
            () -> new ResumenProfesorService(datos).obtener(9, null)).getStatusCode());
    }
}
