# Pruebas reales de ADM4 y ADM6

Fecha: 11 de noviembre de 2026. Rama local: `fabian/auditoria4-local`.
Base actualizada desde `origin/dev` en `e45cfdd`.

Las pruebas usaron una sesión administrativa y los servicios conectados a SQL Server
por Tailscale. Los scripts reciben las credenciales mediante variables de entorno;
no las contienen ni imprimen tokens.

| Historia | Comprobación | Resultado |
| --- | --- | --- |
| ADM4 | Alta de una cuenta exclusiva de prueba | HTTP 201 |
| ADM4 | Consulta del detalle con fecha de modificación | HTTP 200; datos y fecha presentes |
| ADM4 | Edición y nueva consulta | HTTP 200; cambio persistido y fecha presente |
| ADM4 | Baja y consulta posterior | HTTP 204 y después HTTP 404 |
| ADM4 | Bitácora de la cuenta de prueba | Seis eventos posteriores al inicio de la prueba |
| ADM6 | Alta de un parámetro exclusivo de prueba | HTTP 201 |
| ADM6 | Consulta y edición con nueva consulta | HTTP 200; valores persistidos |
| ADM6 | Baja y consulta posterior | HTTP 200 y después HTTP 404 |
| ADM6 | Bitácora del parámetro de prueba | Tres eventos registrados |

Los registros temporales se eliminaron al terminar. La comprobación no modificó
cuentas, roles, parámetros ni instituciones existentes.

Scripts reproducibles: `adm4_usuarios_real.ps1` y `adm6_parametros_real.ps1`.
Ambos abortan si encuentran previamente su identificador de prueba y hacen una
limpieza final si ocurre un error después del alta.

## Pendientes que no se resuelven con estas pruebas

- La cuenta administrativa disponible no tiene `ADM20`; el indicador de facturas
  del dashboard no puede consultar ese servicio y muestra «No disponible».
- `PrograV_Notificacion` aún no contiene `general.Notificacion`; el servicio de
  notificaciones no inicia y su resumen en ADM3 devuelve 503.
- El índice `UX_rol_nombre_rol` de ADM5 aún no existe. Su script versionado es
  `db/por_servicio/migraciones/004_rol_nombre_unico.sql`; el usuario SQL de esta
  computadora no tiene permiso para ejecutarlo.
- La base de roles contiene `ADMIN` y `ESTUDIANTE`, sin un rol `PROFESOR` para
  comprobar el dashboard docente con una sesión real.
- La revocación real de permisos de ADM5 y el bloqueo de borrado por referencias
  de ADM8 todavía necesitan una prueba coordinada en la base compartida.
