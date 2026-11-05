# ADM11 — Mantenimiento de profesores

- **ID y título:** ADM11 — Mantenimiento de profesores
- **Origen:** PDF Proyecto Nuevo Avatar V2, p. 9 y reglas comunes pp. 16–19
- **Responsable único:** Ramses (Persona 2)
- **Revisor de apoyo:** Pendiente de revisión cruzada en recorrido integrado
- **Puntos acordados:** 5 puntos (Avance 2)
- **Dependencias y contratos:** `NuevoAvatar.Profesor`, `NuevoAvatar.Parametro` (`DOMPROF`, `TAMPAG`), `NuevoAvatar.Grupo`, `NuevoAvatar.Periodo`, `NuevoAvatar.Bitacora`
- **Estado actual:** En verificación local (validación de teléfono local, bitácoras JSON, paginación parametrizada y pruebas locales completas; pendiente captura con servicios propietarios reales)

## Matriz de criterios de aceptación

| HU | Criterio y fuente | Responsable | Precondiciones y pasos | Esperado | Observado | Captura/registro | Versión | Estado |
|---|---|---|---|---|---|---|---|---|
| ADM11 | C1 (PDF p. 9): ABM con validaciones de mayoría de edad, email con dominio parametrizable (`DOMPROF`), nombre solo letras/espacios y teléfonos requeridos en formato local sin repetir | Ramses | Crear y editar profesor con teléfonos locales de 8 dígitos (`8888-1111` / `22223333`); probar menor de edad, dominio distinto a `DOMPROF`, teléfono corto (`8888`) o repetido | Persiste datos válidos y muestra mensaje de éxito; rechaza con `400/422` edad menor a 18, dominio inválido, teléfono sin formato local de 8 dígitos o duplicado | Verificado en `ProfesorServiceTest.exigeFormatoLocalDeTelefonosYEvitaDuplicados` y `profesores.test.js` | `ProfesorServiceTest.java`, `profesores.test.js` | Rama `ramses` | Parcial con verificación local |
| ADM11 | C2 (PDF p. 9): Búsqueda por identificación/nombre/email, paginación parametrizada (máx. 15) y orden | Ramses | Buscar texto y cambiar campo/dirección de orden en `Profesores.jsx` | Consulta `/gateway/admin/profesor/buscar` con `size` leído de `TAMPAG` (tope 15), ordena por campos permitidos y navega entre páginas | Verificado en `profesores.test.js` y `ProfesorService.buscar` | `Profesores.jsx`, `profesores-api.js` | Rama `ramses` | Parcial con verificación local |
| ADM11 | C3 (PDF p. 9): Impedir eliminar si dicta grupos en periodos activos (`fechaInicio <= hoy <= fechaFin`) | Ramses | Intentar eliminar profesor con grupo en periodo activo vs profesor con grupos solo en periodos cerrados | Bloquea con `409 Conflict` cuando existe al menos un grupo en periodo activo; permite cuando todos están cerrados | Verificado en `ProfesorServiceTest.bloqueaCuandoHoyEstaDentroDelPeriodoInclusivo` y `permiteCuandoTodosLosPeriodosEstanCerrados` | `ProfesorServiceTest.java` | Rama `ramses` | Parcial con verificación local |
| ADM11 | C4 (PDF pp. 9, 16–17): Bitácoras en todas las operaciones con JSON nuevo, anterior/actual y eliminado | Ramses | Crear, modificar, consultar y eliminar profesor | Registra consulta en `GET` y snapshots JSON reales de `ProfesorResponse` en `POST`, `PUT` (`Anterior`/`Actual`) y `DELETE` | Verificado en `ProfesorServiceTest.registraSnapshotsJsonEnBitacoraDeProfesor` | `BitacoraFilter.java`, `ProfesorService.java` | Rama `ramses` | Parcial con verificación local |

## Verificación automatizada

- `.\NuevoAvatar.Notas\mvnw.cmd -B -pl integracion,NuevoAvatar.Profesor -am test` (4 pruebas en `ProfesorServiceTest` aprobadas)
- `npm test`, `npm run build` y `npm run test:ui -- --reporter=line` desde `web/ramses`
- Usa la estructura existente de `PrograV_Profesor`; no requiere migración SQL adicional.
