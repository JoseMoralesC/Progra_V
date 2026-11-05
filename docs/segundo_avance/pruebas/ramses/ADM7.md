# ADM7 — Mantenimiento de módulos

- **ID y título:** ADM7 — Mantenimiento de módulos visibles en el menú
- **Origen:** PDF Proyecto Nuevo Avatar V2, p. 8 y reglas comunes pp. 16–19
- **Responsable único:** Ramses (Persona 2)
- **Revisor de apoyo:** Pendiente de revisión cruzada en recorrido integrado
- **Puntos acordados:** 5 puntos (Avance 2)
- **Dependencias y contratos:** `NuevoAvatar.Modulo`, `NuevoAvatar.Rol` (`/gateway/admin/rol`), `NuevoAvatar.Parametro` (`TAMPAG`), `NuevoAvatar.Bitacora`, `db/por_servicio/migraciones/002_modulo_v2.sql`
- **Estado actual:** En verificación local (código y pruebas locales completas; pendiente captura con base remota y consumo integrado en ADM1/ADM5/GTW2)

## Matriz de criterios de aceptación

| HU | Criterio y fuente | Responsable | Precondiciones y pasos | Esperado | Observado | Captura/registro | Versión | Estado |
|---|---|---|---|---|---|---|---|---|
| ADM7 | C1 (PDF p. 8): CRUD de módulos con ID, nombre (solo letras y espacios), estado (activo/inactivo) y orden | Ramses | Ingresar como `ADMINISTRADOR`, crear, editar y eliminar un módulo desde `Modulos.jsx`; intentar nombre con dígitos o orden negativo | Alta, edición y baja con confirmación (`¿Realmente desea eliminar el elemento seleccionado?`), mensaje de éxito y rechazo de datos inválidos | Validado en pruebas Node, Playwright y controlador .NET | `web/ramses/tests/modulos.test.js`, `tests/ui/modulos.spec.js` (captura real pendiente) | Rama `ramses` | Parcial con verificación local |
| ADM7 | C2 (PDF p. 8): Asociación módulo ↔ rol para construir menú dinámico (`GET /gateway/menu/{idRol}`, `GET/PUT /gateway/admin/modulo/{idModulo}/roles`) | Ramses | Abrir modal de roles de un módulo, asignar roles existentes y consultar `/gateway/menu/{idRol}` | Reemplaza asignaciones en `seguridad.rol_modulo`, rechaza roles inexistentes con `422` y devuelve solo módulos activos ordenados por `orden` y `nombreModulo` | Implementado en `ModuloController.cs` y `ModuloService.cs`; serialización probada en `modulos.test.js` | `NuevoAvatar.Modulo/Controllers/ModuloController.cs` | Rama `ramses` | Parcial con verificación local |
| ADM7 | C3 (PDF pp. 8, 16–17): Bitácoras en todas las operaciones (altas, modificaciones con JSON anterior/actual, bajas con JSON eliminado y consultas de módulos, menú y roles) | Ramses | Ejecutar `GET /modulo`, `GET /modulo/{id}`, `GET /modulo/menu/{idRol}`, `GET /modulo/{id}/roles`, `POST`, `PUT` y `DELETE` | Registra `"El usuario consulta ..."` en lecturas y snapshots JSON en creación, edición, reemplazo de roles y eliminación | Implementado en `ModuloController.cs` (líneas 28, 37, 52, 73, 81, 89, 106, 117) | `NuevoAvatar.Modulo/Controllers/ModuloController.cs` | Rama `ramses` | Parcial con verificación local |
| ADM7 | Regla común (PDF pp. 17–19): Listado paginado parametrizable con máximo de 15 elementos por página | Ramses | Configurar `TAMPAG` en Parámetros y recorrer páginas con botones `Anterior` y `Siguiente` | Lee `TAMPAG` desde `/gateway/admin/parametro/TAMPAG`, aplica tope máximo de 15 y navega entre páginas | Verificado en `modulos.test.js` y `Modulos.jsx` | `web/ramses/src/modulos-api.js`, `Modulos.jsx` | Rama `ramses` | Parcial con verificación local |

## Verificación automatizada

- `dotnet build NuevoAvatar.Modulo/NuevoAvatar.Modulo.csproj --no-restore --verbosity minimal`
- `npm test` y `npm run build` desde `web/ramses`
- `npm run test:ui -- --reporter=line` desde `web/ramses`
- Migración idempotente en `db/por_servicio/migraciones/002_modulo_v2.sql` (pendiente aplicar y capturar en instancia SQL compartida por Tailscale).
