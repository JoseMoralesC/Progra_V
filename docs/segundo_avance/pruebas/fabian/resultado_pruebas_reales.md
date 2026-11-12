# Pruebas reales de ADM3, ADM4, ADM5, ADM6 y ADM8

Fecha: 11 de noviembre de 2026. Rama de trabajo: `fabian`.

Las pruebas utilizaron la aplicación local, el gateway y SQL Server compartido por
Tailscale. Los ejecutores reciben las credenciales mediante variables de entorno;
no las contienen ni imprimen tokens. Los datos temporales se eliminaron al terminar.

| Historia | Comprobación | Resultado |
| --- | --- | --- |
| ADM3 | Indicadores contra las consultas reales | Usuarios 8, carreras 5, cursos 4, períodos activos 1, facturas pendientes 0 |
| ADM3 | Facturas | Consulta acotada por `periodoId` y `vista=dashboard`; devuelve solo el conteo pendiente con permiso ADM3 |
| ADM3 | Dashboard administrador | Diez bitácoras recientes visibles; captura guardada |
| ADM3 | Dashboard profesor | Sesión docente, grupo activo y curso comprobados; diez eventos propios, máximo diez; capturas guardadas |
| ADM3 | Notificaciones de ambos perfiles | HTTP 200; lista vacía porque no hay envíos registrados; capturas guardadas |
| ADM4 | Alta, detalle, edición y baja de usuario | HTTP 201, 200, 200 y 204; consulta posterior 404; fecha de modificación presente |
| ADM4 | Bloqueo de API y URL con cuenta estudiantil | HTTP 403 y redirección al portal estudiantil |
| ADM5 | Revocación y restauración de `ADM8` | Menú actualizado sin recargar; operación revocada HTTP 403; permiso original restaurado |
| ADM5 | Índice único de nombres de roles | `UX_rol_nombre_rol` presente en `PrograV_Rol` |
| ADM6 | Alta, consulta, edición y baja de parámetro | HTTP 201, 200, 200 y 200; consulta posterior 404 |
| ADM8 | Edición y persistencia de institución | HTTP 200 y lectura posterior con el valor actualizado |
| ADM8 | Baja con carrera vinculada | HTTP 409; institución conservada hasta eliminar la referencia |
| ADM8 | Bloqueo de API y URL con cuenta estudiantil | HTTP 403 y redirección al portal estudiantil |

Las pruebas de ADM4 y ADM6 registraron seis y tres eventos de bitácora,
respectivamente. La migración `005_usuario_fecha_modificacion.sql` está reflejada
en la base real. La política de contraseña vigente y su relación con ADM24 se
documentan en `docs/segundo_avance/requerimientos/politica-contrasena-adm24.md`.

## Comprobación posterior a la auditoría 5

Fecha: 12 de noviembre de 2026. La consulta de facturas del dashboard se limitó
en el gateway a `GET /gateway/fin/factura?periodoId=N&vista=dashboard`. Requiere
ADM3 y responde únicamente con `pendientes`. Las demás operaciones de Factura
conservan su permiso ADM20. El ejecutor de ADM3 ya no asigna ADM20 ni modifica
permisos. El recorrido real devolvió usuarios 8, carreras 7, cursos 6, períodos
activos 1 y facturas pendientes 0. Los números corresponden a ese momento de la
base compartida y pueden cambiar.

La lectura de `PrograV_Parametro` confirmó `PASSMIN=8`, `PASSMAX=30`,
`PASSMAY=true` y `PASSNUM=true`, en concordancia con la política documentada.
No se modificaron esos parámetros. Las pruebas de interfaz de Dashboard,
Instituciones, Parámetros y Usuarios terminaron con nueve casos aprobados.

Las capturas de ADM3 están en `evidencias/adm3_indicadores.png`,
`evidencias/adm3_bitacoras_admin.png`, `evidencias/adm3_profesor.png`,
`evidencias/adm3_bitacoras_profesor.png`,
`evidencias/adm3_notificaciones_admin.png` y
`evidencias/adm3_notificaciones_profesor.png`.
Las pruebas se reproducen con los scripts de este directorio y
`NuevoAvatar.Estudiante.React/tests/reales/`.

## Migraciones de la base compartida

Los scripts `db/por_servicio/migraciones/003_notificacion_historial.sql` y
`db/por_servicio/migraciones/004_rol_nombre_unico.sql` fueron aplicados por el
equipo. Se comprobó la existencia de `general.Notificacion` en
`PrograV_Notificacion` y del índice `UX_rol_nombre_rol` en `PrograV_Rol`.
El servicio de notificaciones inició con validación del esquema y el resumen de
ADM3 respondió HTTP 200 para ambos perfiles. La lista está vacía porque no hay
notificaciones registradas; no se efectuó un envío de correo en esta prueba.

El rol `PROFESOR`, la cuenta docente y su grupo se crearon solo para comprobar
ADM3 y se eliminaron. Las asignaciones originales de módulos quedaron restauradas.
