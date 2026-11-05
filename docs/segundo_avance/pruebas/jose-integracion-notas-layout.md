# ADM17 — Notas dentro de la plantilla React

Fecha: 4 de noviembre de 2026 (America/Mexico_City).
Responsable ADM17: Jose. Plantilla UI/UX y componentes base: Alejandro.
Base: `034ca8b13928ffa1709d69e0b04bd70ceb99d1f0`, con los incrementos locales de
sesión y notas todavía sin commit al registrar esta ficha.
Estado: integración frontend implementada y verificada localmente; cierre
integral de ADM17 pendiente.

## Desarrollo incorporado

La ruta `/notas` de `NuevoAvatar.Estudiante.React` monta el componente existente
`web/jose/src/Notas.jsx` dentro de `MainLayout`. Recibe la misma instancia de
`ClienteSesion` que utiliza el login; no se crea otro cliente ni almacenamiento
de tokens. Las consultas y escrituras siguen pasando por `/gateway/mat/...`.

El profesor puede abrir la pantalla desde el menú o el inicio. El layout conserva
nombre/rol reales y salida, marca Notas como ruta activa, presenta Inicio / Notas
en breadcrumbs y ofrece Volver al inicio. La pantalla también se puede abrir
directamente o recargar con una sesión de profesor válida.

Administrador y estudiante no reciben esos accesos. Si escriben `/notas`, se
informa acceso restringido y no se monta `Notas` ni se consulta su API. Sin sesión
se regresa al login. La autorización real continúa en gateway y servicio: este
control de navegación no reemplaza la comprobación de profesor/grupo en backend.

Se conserva el componente de negocio: selección de grupos autorizados, consulta
del desglose y notas por grupo/estudiante, confirmaciones, carga/reemplazo del
desglose, creación/modificación de notas con POST/PUT y bloqueo de rubros cuando
hay notas. Los ajustes CSS se limitan a `.notas-integradas` para integrarlo con
la plantilla sin alterar otros módulos.

## Verificación local

Pruebas en navegador con API interceptada, sin escrituras en SQL o llamadas a
servicios reales. Se comprueba el token de la sesión en las solicitudes de notas
y los cuerpos/verbos de las operaciones del recorrido integrado.

| Escenario | Resultado esperado y comprobado |
| --- | --- |
| Profesor entra desde menú y acceso rápido | Notas aparece en el layout con identidad, ruta activa y breadcrumbs; regreso y recarga funcionan |
| Administrador / estudiante por URL directa | Acceso restringido, sin controles ni consultas a las API de notas |
| Carga de desglose | POST con grupo/rubros y resultado recargado en pantalla |
| Asignación y modificación de nota | POST y PUT con matrícula/rubro/nota; después de asignar se bloquea el desglose |
| Dependencia caída durante consulta | Mensaje de error, sin escritura habilitada; reintento recupera datos sin perder sesión |
| Profesor sin grupos | Estado vacío explícito sin formularios de escritura |
| Token rechazado y refresh fallido | Se retira el módulo, se limpian credenciales y se vuelve a login |
| Salida desde notas y regreso del navegador | La pantalla protegida no reaparece al volver atrás |

Comandos desde la raíz:

```powershell
npm.cmd run test:ui --prefix NuevoAvatar.Estudiante.React -- --reporter=line
npm.cmd run build --prefix NuevoAvatar.Estudiante.React
npm.cmd run lint --prefix NuevoAvatar.Estudiante.React
```

Resultado final: **24/24 pruebas de navegador aprobadas** (nueve de integración
de notas y quince de sesión), **build aprobado** y **lint sin errores**, con la
advertencia previa de Fast Refresh en `NotificationContext.jsx`. La captura
local del recorrido con datos simulados se genera en `test-results` de la
aplicación; no se presenta como evidencia de servicios reales.

## Cómo abrirlo y pendientes

Desde `NuevoAvatar.Estudiante.React`, ejecutar `npm.cmd run dev` y abrir
`http://127.0.0.1:4175`. Ingresar con una cuenta de profesor y seleccionar Notas.
Para un recorrido real deben estar configurados y activos gateway, sesión/identidad,
Notas y sus servicios propietarios de grupos/cursos/periodos/matrículas, además
de Bitácora. La aplicación no inicia servicios ni crea datos de prueba.

Sigue pendiente comprobar grupos/matrículas reales, propiedad de datos, persistencia
de notas y snapshots de bitácora, conflictos concurrentes y bloqueo del desglose
al asignar la primera nota. También quedan captura por criterio, versión comprometida
y revisión de apoyo. El dashboard del profesor completo sigue correspondiendo a
Fabian en ADM3; ahora puede enlazar esta ruta cuando se incorpore.

No se actualizan retroactivamente las barras de la tercera auditoría: este es un
incremento posterior de desarrollo, separado de la cantidad de pruebas realizadas.

Incremento posterior: la [concurrencia y los permisos dinámicos](jose-concurrencia-permisos.md)
añaden bloqueo transaccional por grupo y exigencia de ADM17 activo. Su
[contrato para el equipo](../planificacion/permisos-rol-modulo.md) sustituye la
dependencia anterior de acordar cómo conectar las asignaciones con el gateway.
