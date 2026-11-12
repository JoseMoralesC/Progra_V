package cr.ac.cuc.nuevoavatar.persona2.profesor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cr.ac.cuc.nuevoavatar.integracion.DatosRemotos;
import cr.ac.cuc.nuevoavatar.persona2.seguridad.BitacoraFilter;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

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

    @Test
    void exigeFormatoLocalDeTelefonosYEvitaDuplicados() {
        assertEquals(List.of("8888-1111", "22223333"), servicio.normalizarTelefonos(List.of(" 8888-1111 ", "22223333")));
        assertThrows(ResponseStatusException.class, () -> servicio.normalizarTelefonos(List.of("8888")));
        assertThrows(ResponseStatusException.class, () -> servicio.normalizarTelefonos(List.of("8888-1111", "8888-1111")));
        assertThrows(ResponseStatusException.class, () -> servicio.normalizarTelefonos(List.of("8888-1111", "+506 88881111")));
        assertThrows(ResponseStatusException.class, () -> servicio.normalizarTelefonos(List.of("88881111", "506-8888-1111")));
    }

    @Test
    void rechazaProfesorMenorDeEdad() {
        ProfesorRequest request = new ProfesorRequest(
            "Cédula", "101110111", "joven@cuc.ac.cr", "Persona Joven",
            LocalDate.now().minusYears(18).plusDays(1), List.of("8888-1111")
        );

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class, () -> servicio.crear(request)
        );

        assertEquals(422, error.getStatusCode().value());
        verifyNoInteractions(repositorio);
    }

    @Test
    void rechazaCorreoFueraDelDominioConfigurado() {
        when(datos.buscar("Parametro", "parametro", "id_parametro", "DOMPROF"))
            .thenReturn(Optional.of(Map.of("valor", "cuc.ac.cr")));
        ProfesorRequest request = new ProfesorRequest(
            "Cédula", "101110111", "persona@externo.cr", "Persona Externa",
            LocalDate.of(1990, 1, 1), List.of("8888-1111")
        );

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class, () -> servicio.crear(request)
        );

        assertEquals(422, error.getStatusCode().value());
        verifyNoInteractions(repositorio);
    }

    @Test
    void registraSnapshotsJsonEnBitacoraDeProfesor() {
        MockHttpServletRequest http = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(http));
        try {
            when(datos.buscar("Parametro", "parametro", "id_parametro", "DOMPROF"))
                .thenReturn(Optional.of(Map.of("valor", "cuc.ac.cr")));
            Profesor p = new Profesor();
            p.setTipoIdentificacion("Cédula");
            p.setIdentificacion("101110111");
            p.setEmail("ana@cuc.ac.cr");
            p.setNombreCompleto("Ana Mora");
            p.setFechaNacimiento(LocalDate.of(1990, 1, 1));
            p.reemplazarTelefonos(List.of("8888-1111"));
            when(repositorio.findById(4)).thenReturn(Optional.of(p));
            when(repositorio.saveAndFlush(any(Profesor.class))).thenAnswer(x -> x.getArgument(0));
            when(datos.listar("Grupo", "Grupo", Map.of("ProfesorId", 4))).thenReturn(List.of());

            servicio.modificar(4, new ProfesorRequest("Cédula", "101110111", "ana@cuc.ac.cr", "Ana María Mora", LocalDate.of(1990, 1, 1), List.of("8888-2222")));
            String mod = String.valueOf(http.getAttribute(BitacoraFilter.DETALLE_ATTRIBUTE));
            assertTrue(mod.contains("Anterior:"));
            assertTrue(mod.contains("8888-1111"));
            assertTrue(mod.contains("Actual:"));
            assertTrue(mod.contains("8888-2222"));

            servicio.eliminar(4);
            String elim = String.valueOf(http.getAttribute(BitacoraFilter.DETALLE_ATTRIBUTE));
            assertTrue(elim.contains("Eliminación de profesor:"));
            assertTrue(elim.contains("Ana María Mora"));
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }
}
