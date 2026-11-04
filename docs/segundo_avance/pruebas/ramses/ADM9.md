# ADM9 — Mantenimiento de carreras

- **ID y título:** ADM9 — Mantenimiento de carreras por institución
- **Origen:** PDF Proyecto Nuevo Avatar V2, p. 8 y reglas comunes pp. 16–19
- **Responsable único:** Ramses (Persona 2)
- **Revisor de apoyo:** Pendiente de revisión cruzada en recorrido integrado
- **Puntos acordados:** 5 puntos (Avance 2)
- **Dependencias y contratos:** `NuevoAvatar.Carrera`, `NuevoAvatar.Institucion`, `NuevoAvatar.Profesor`, `NuevoAvatar.Curso`, `NuevoAvatar.Parametro` (`TAMPAG`), `NuevoAvatar.Bitacora`
- **Estado actual:** En verificación local (código, bitácoras JSON, paginación y pruebas locales completas; pendiente captura con servicios propietarios reales en ejecución)

## Matriz de criterios de aceptación

| HU | Criterio y fuente | Responsable | Precondiciones y pasos | Esperado | Observado | Captura/registro | Versión | Estado |
|---|---|---|---|---|---|---|---|---|
| ADM9 | C1 (PDF p. 8): Listado con filtro por institución y CRUD con validación de nombre (solo letras y espacios) | Ramses | Filtrar por institución en `Carreras.jsx`, crear y editar carrera con nombre válido e intentar nombre con números | Filtra por `institucionId`, muestra mensaje de éxito al guardar/eliminar y rechaza nombres inválidos | Verificado en `carreras.test.js`, `CarreraServiceTest.java` y Playwright | `web/ramses/src/Carreras.jsx`, `carreras.test.js` | Rama `ramses` | Parcial con verificación local |
| ADM9 | C2 (PDF p. 8): Director debe existir como profesor (ACD6); selector con búsqueda | Ramses | Buscar profesor por nombre/identificación/correo en el modal de carrera y guardar | Consulta `/gateway/admin/profesor/buscar` en UI y valida existencia en `DatosRemotos.exigir("Profesor", ...)` e `"Institucion"` en servidor | Verificado en `CarreraServiceTest.validaInstitucionYProfesorEnSusServiciosPropietarios` y Playwright | `CarreraServiceTest.java`, `modulos.spec.js` | Rama `ramses` | Parcial con verificación local |
| ADM9 | C3 (PDF p. 8): Impedir eliminación si existen cursos vinculados (`409 Conflict`) | Ramses | Confirmar eliminación de una carrera con cursos asociados | Bloquea el borrado con `409 Conflict` (`"La carrera tiene cursos relacionados"`) sin eliminar el registro | Verificado en `CarreraServiceTest.bloqueaEliminarCuandoExistenCursos` | `CarreraServiceTest.java` | Rama `ramses` | Parcial con verificación local |
| ADM9 | C4 (PDF pp. 8, 16–17): Bitácoras en todas las operaciones con JSON nuevo, anterior/actual y eliminado | Ramses | Crear, modificar, consultar y eliminar carrera | Registra consulta en `GET` y snapshots JSON reales de la entidad en `POST`, `PUT` (`Anterior` y `Actual`) y `DELETE` (`Eliminación de carrera`) | Verificado en `CarreraServiceTest.registraSnapshotsJsonEnModificacionYEliminacion` | `BitacoraFilter.java`, `CarreraService.java` | Rama `ramses` | Parcial con verificación local |
| ADM9 | Regla común (PDF pp. 17–19): Listado paginado parametrizable con máximo de 15 elementos y confirmación de eliminación | Ramses | Navegar páginas del listado de carreras y abrir modal de confirmación | Paginación con `TAMPAG` (máx. 15) y diálogo `¿Realmente desea eliminar el elemento seleccionado?` | Implementado y compilado en `Carreras.jsx` | `web/ramses/src/Carreras.jsx` | Rama `ramses` | Parcial con verificación local |

## Verificación automatizada

- `.\NuevoAvatar.Notas\mvnw.cmd -B -pl integracion,NuevoAvatar.Carrera -am test` (3 pruebas en `CarreraServiceTest` aprobadas)
- `npm test`, `npm run build` y `npm run test:ui -- --reporter=line` desde `web/ramses`
- Usa la estructura existente de `PrograV_Carrera`; no requiere migración SQL adicional.
