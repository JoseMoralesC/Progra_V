# ADM12 — Mantenimiento de periodos

- **ID y título:** ADM12 — Mantenimiento de periodos
- **Origen:** PDF Proyecto Nuevo Avatar V2, p. 9 y reglas comunes pp. 16–19
- **Responsable único:** Ramses (Persona 2)
- **Revisor de apoyo:** Pendiente de revisión cruzada en recorrido integrado
- **Puntos acordados:** 5 puntos (Avance 2)
- **Dependencias y contratos:** `NuevoAvatar.Periodo`, `NuevoAvatar.Grupo`, `NuevoAvatar.Matricula`, `NuevoAvatar.Prematricula`, `NuevoAvatar.Factura`, `NuevoAvatar.Pago`, `NuevoAvatar.Parametro` (`TAMPAG`), `NuevoAvatar.Bitacora`
- **Estado actual:** En verificación local (regla de solapamiento y concurrencia, bitácoras JSON, paginación, estados y pruebas locales completas; pendiente captura con servicios propietarios reales)

## Regla de unicidad y solapamiento acordada

- **Unicidad `(Anio, NumeroPeriodo)`:** No se permiten dos periodos con el mismo año y número (`ExisteDuplicadoAsync` y restricción SQL `UQ_Periodo_Anio_Numero`).
- **No solapamiento temporal en el mismo año:** Dentro de un mismo `Anio`, distintos `NumeroPeriodo` están permitidos siempre que sus rangos `[FechaInicio, FechaFin]` no se solapen de forma inclusiva (`FechaInicio <= @FechaFin AND FechaFin >= @FechaInicio`). Las escrituras verifican esta condición con bloqueo serializable (`UPDLOCK, HOLDLOCK`) y semáforo de servicio para impedir inserciones/ediciones concurrentes en conflicto (`409 Conflict`).

## Matriz de criterios de aceptación

| HU | Criterio y fuente | Responsable | Precondiciones y pasos | Esperado | Observado | Captura/registro | Versión | Estado |
|---|---|---|---|---|---|---|---|---|
| ADM12 | C1 (PDF p. 9): CRUD con Año, Número de periodo y Fecha de inicio/fin (`fin >= inicio`) | Ramses | Crear y editar periodo en `Periodos.jsx`; intentar `fechaFin < fechaInicio` | Persiste con mensaje de éxito; rechaza fechas invertidas en cliente y servidor (`PeriodoServiceTest`) | Verificado en `PeriodoServiceTest.cs`, `periodos.test.js` y Playwright | `PeriodoServiceTest.cs`, `periodos.test.js` | Rama `ramses` | Parcial con verificación local |
| ADM12 | C2 (PDF p. 9): Estados derivados `FUTURO` (`inicio > hoy`), `ACTIVO` (`hoy` entre `inicio` y `fin`) y `CERRADO` (`fin < hoy`), con filtros por año y estado | Ramses | Consultar periodos y filtrar por año y estado en `Periodos.jsx` | Calcula `Estado` dinámicamente según la fecha actual y filtra el listado | Verificado en `PeriodoReglasTest.EstadoSeDerivaDeFechas` y `periodos.test.js` | `PeriodoReglasTest.cs` | Rama `ramses` | Parcial con verificación local |
| ADM12 | C3 (PDF p. 9): Impedir duplicados de año/número y solapamientos de fechas, incluyendo concurrencia, y bloquear baja con referencias | Ramses | Intentar crear/editar periodo duplicado o con fechas traslapadas (incluso en paralelo) o borrar periodo referenciado | Devuelve `409 Conflict` sin cambios parciales | Verificado en `PeriodoReglasTest.DuplicadoProduceConflicto` y `ProtegeContraEscriturasConcurrentesConSolapamiento` | `PeriodoReglasTest.cs`, `PeriodoRepository.cs` | Rama `ramses` | Parcial con verificación local |
| ADM12 | C4 (PDF pp. 9, 16–19): Bitácoras con JSON (`creado`, `anterior/actual`, `eliminado`), paginación parametrizada (máx. 15) y estados de carga/vacío/éxito | Ramses | Consultar, crear, modificar y eliminar periodos; recorrer páginas | Registra consultas y snapshots JSON en `PeriodoEndpoint.cs`; muestra estados de carga/vacío, paginación y mensajes de éxito en `Periodos.jsx` | Implementado y compilado en `PeriodoEndpoint.cs` y `Periodos.jsx` | `PeriodoEndpoint.cs`, `Periodos.jsx` | Rama `ramses` | Parcial con verificación local |

## Verificación automatizada

- `dotnet test NuevoAvatar.Academico.Tests/NuevoAvatar.Academico.Tests.csproj --no-restore --verbosity minimal` (17 pruebas aprobadas)
- `npm test`, `npm run build` y `npm run test:ui -- --reporter=line` desde `web/ramses`
- Usa la estructura existente de `PrograV_Periodo`; no requiere migración SQL adicional.
