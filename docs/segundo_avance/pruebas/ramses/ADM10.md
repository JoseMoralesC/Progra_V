# ADM10 — Mantenimiento de cursos

- **ID y título:** ADM10 — Mantenimiento de cursos
- **Origen:** PDF Proyecto Nuevo Avatar V2, p. 8 y reglas comunes pp. 16–19
- **Responsable único:** Ramses (Persona 2)
- **Revisor de apoyo:** Pendiente de revisión cruzada en recorrido integrado
- **Puntos acordados:** 5 puntos (Avance 2)
- **Dependencias y contratos:** `NuevoAvatar.Curso`, `NuevoAvatar.Carrera`, `NuevoAvatar.Grupo`, `NuevoAvatar.Matricula`, `NuevoAvatar.Prematricula`, `NuevoAvatar.Parametro` (`TAMPAG`), `NuevoAvatar.Bitacora`
- **Estado actual:** En verificación local (código, bitácoras JSON, paginación y pruebas locales completas; pendiente captura con servicios propietarios reales)

## Matriz de criterios de aceptación

| HU | Criterio y fuente | Responsable | Precondiciones y pasos | Esperado | Observado | Captura/registro | Versión | Estado |
|---|---|---|---|---|---|---|---|---|
| ADM10 | C1 (PDF p. 8): Listado con filtro combinable por carrera y nivel | Ramses | Seleccionar carrera y/o nivel (1–12) en `Cursos.jsx` | Envía `carreraId` y `nivel` a `/gateway/admin/curso` y filtra en repositorio | Verificado en `CursoFiltrosTest.CombinaCarreraYNivelEnRepositorio` y `cursos.test.js` | `CursoFiltrosTest.cs`, `cursos.test.js` | Rama `ramses` | Parcial con verificación local |
| ADM10 | C2 (PDF p. 8): CRUD con validaciones de nivel entre 1 y 12, nombre con letras/espacios y carrera existente | Ramses | Crear y editar curso con nivel válido (1–12) e intentar nivel 0 o 13 o carrera inexistente | Crea/actualiza con mensaje de éxito; rechaza nivel fuera de rango o carrera inexistente con `422` | Verificado en `CursoFiltrosTest.RechazaFiltrosFueraDeRango`, `ValidacionesRequestTest.cs` y `cursos.test.js` | `NuevoAvatar.Academico.Tests` | Rama `ramses` | Parcial con verificación local |
| ADM10 | C3 (PDF p. 8): Bloqueo de eliminación si hay grupos/matrículas/prematrículas asociados | Ramses | Intentar eliminar un curso con referencias activas | Bloquea el borrado con `409 Conflict` mediante `DatosRemotos.SinReferencias("Curso", "Curso", id)` | Implementado en `CursoService.DeleteAsync` y `CursoAuthorizationFilter.cs` | `NuevoAvatar.Curso/Services/CursoService.cs` | Rama `ramses` | Parcial con verificación local |
| ADM10 | C4 (PDF pp. 8, 16–17): Bitácoras en todas las operaciones con JSON nuevo, anterior/actual y eliminado | Ramses | Consultar, crear, modificar y eliminar cursos | Registra `"El usuario consulta ..."` en `GET`, JSON de curso creado en `POST`, `Anterior`/`Actual` en `PUT` y JSON eliminado en `DELETE` | Verificado en `CursoFiltrosTest.PriorizaDetalleJsonEnBitacoraDeCurso` | `CursoEndpoint.cs`, `CursoAuthorizationFilter.cs` | Rama `ramses` | Parcial con verificación local |
| ADM10 | Regla común (PDF pp. 17–19): Paginación parametrizada (máx. 15), mensajes de éxito y confirmación de baja | Ramses | Navegar listado paginado en `Cursos.jsx` y abrir modal de eliminación | Lee `TAMPAG` (tope 15), muestra controles `Anterior`/`Siguiente`, mensajes de éxito y confirmación `¿Realmente desea eliminar el elemento seleccionado?` | Implementado y verificado en `Cursos.jsx` y Playwright | `web/ramses/src/Cursos.jsx` | Rama `ramses` | Parcial con verificación local |

## Verificación automatizada

- `dotnet test NuevoAvatar.Academico.Tests/NuevoAvatar.Academico.Tests.csproj --no-restore --verbosity minimal`
- `npm test`, `npm run build` y `npm run test:ui -- --reporter=line` desde `web/ramses`
- Usa la estructura existente de `PrograV_Curso`; no requiere migración SQL adicional.
