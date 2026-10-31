package cr.ac.cuc.nuevoavatar.persona2.profesor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ProfesorServiceTest {
    private final ProfesorRepository repositorio = mock(ProfesorRepository.class);
    private final DatosRemotos datos = mock(DatosRemotos.class);
    private final ProfesorService servicio = new ProfesorService(repositorio, datos);

    @Test
    void bloqueaCuandoHoyEstaDentroDelPeriodoInclusivo() {
        when(datos.listar("Grupo", "Grupo", Map.of("ProfesorId", 7)))
            .thenReturn(List.of(Map.of("PeriodoId", 3)));
        when(datos.buscar("Periodo", "Periodo", "PeriodoId", 3))
            .thenReturn(Optional.of(Map.of("FechaInicio", "2026-10-01", "FechaFin", "2026-10-31")));
        assertTrue(servicio.tieneGruposActivos(7, LocalDate.of(2026, 10, 31)));
    }

    @Test
    void permiteCuandoTodosLosPeriodosEstanCerrados() {
        when(datos.listar("Grupo", "Grupo", Map.of("ProfesorId", 7)))
            .thenReturn(List.of(Map.of("PeriodoId", 2)));
        when(datos.buscar("Periodo", "Periodo", "PeriodoId", 2))
            .thenReturn(Optional.of(Map.of("FechaInicio", "2025-01-01", "FechaFin", "2025-12-31")));
        assertFalse(servicio.tieneGruposActivos(7, LocalDate.of(2026, 10, 31)));
    }
}
