# Seguimiento de auditoría 6: administración

> Estado al 13 de noviembre de 2026: las pruebas automatizadas y sus ejecutores fueron retirados. Las referencias a archivos de pruebas y comandos de ejecucion de este documento son historicas; los resultados y capturas se conservan como evidencia. El detalle de los retiros esta en `docs/mapeo-pruebas/limpieza-realizada.csv`.


> Nota de mantenimiento (13 de noviembre de 2026): las pruebas automatizadas de `NuevoAvatar.Estudiante.React`, su configuración de Playwright y los ejecutores Python asociados se retiraron del proyecto. Las referencias a esos archivos y comandos en este informe corresponden a ejecuciones históricas y ya no son instrucciones vigentes. Se conservan los resultados y evidencias.


Fecha: 12 de noviembre de 2026. Base integrada: `6c16558`.

## Cambios comprobados

| Historia | Corrección o comprobación | Evidencia |
| --- | --- | --- |
| ADM3 | Tabla de bitácoras: acción y módulo por separado, diez registros; métricas, profesor y errores de dependencias | Cuatro escenarios en `dashboard.spec.js` aprobados |
| ADM4 | Restaurado el orden por columnas exigido en la página 6 del PDF, retirado durante la integración visual; se conserva el menú de acciones y selector de tipo de identificación | `usuarios.spec.js`: orden, filtros de dominio/rol, paginación, alta, edición y detalle aprobados |
| ADM5 | Recorrido actualizado a bienvenida, cierre exacto del modal y menú de acciones; mantiene cancelación, persistencia y actualización inmediata del menú | `roles.spec.js` aprobado con respuestas controladas |
| ADM6 | Se conserva la protección de MAILPASS introducida por el equipo. Los parámetros normales recuperan JSON de alta/baja y anterior/actual en modificaciones | 32/32 pruebas de seguridad; `parametros.spec.js` aprobado |
| ADM8 | Bienvenida y menú de acciones actuales; espera al guardado antes de otra búsqueda. Dependencia caída visible y recuperación de la consulta | 4/4 escenarios de instituciones aprobados |

Las once pruebas de interfaz de estas cinco historias quedaron aprobadas: diez en el recorrido conjunto y las cuatro de Instituciones en la última ejecución, tres de ellas repetidas. No se suman las repetidas como casos nuevos. La suite del Gateway terminó con 139/139 aprobadas y la compilación de producción de React finalizó correctamente.

Las pruebas de interfaz interceptan las llamadas de red y utilizan datos en memoria. Las pruebas .NET utilizan dobles de prueba. Estos resultados acreditan regresión local; no son una nueva ejecución sobre SQL compartido. Las identificaciones del recorrido de usuarios tienen nueve dígitos y no se insertan en la base.

## Parámetro de paginación

Se conserva `db/por_servicio/configuracion/parametro_tampag.sql`. Crea TAMPAG con valor 15 únicamente si falta, conserva cualquier valor existente y protege la comprobación con una transacción. Quince coincide con el máximo y respaldo usado por los mantenimientos administrativos. El script apunta a `PrograV_Parametro`.

Después de restablecer Tailscale, se aplicó el script y se confirmó `TAMPAG=15` en SQL Server compartido. El consumo de TAMPAG en WEB4 sigue correspondiendo a Alejandro; preparar el parámetro no sustituye ese consumo.

## Verificación real posterior

La evidencia anterior está en `resultado_pruebas_reales.md` y corresponde a su fecha y datos. No se considera repetida después de la limpieza de datos comunicada por el equipo.

- La consulta real confirmó que `fabian@cuc.cr` coincide con su expediente en correo, tipo e identificación. Ambas cuentas ingresaron desde el navegador con su perfil correcto. El expediente propio respondió HTTP 200. No se alteraron las cuentas preparadas por José.
- La cuenta estudiantil recibió HTTP 403 en Usuarios, Roles, Parámetros e Instituciones. La URL administrativa redirigió al inicio estudiantil.
- Se retiró y restauró ADM8 desde la matriz administrativa. El menú se actualizó sin recargar; una baja sobre un ID previamente confirmado como inexistente devolvió 403 tras revocar. Después de restaurar, la consulta devolvió 200. Las asignaciones finales coinciden con las originales.
- TAMPAG confirmado en la base compartida con valor 15 después de aplicar el script guardado.
- Completar la aceptación real de filtros, orden, paginación, errores y bitácoras con la versión final de los servicios.
- Coordinar con el equipo el menú de presentación de las veinte historias y el paquete documental común.

El PDF de ADM6 repite el texto de cambios de permisos al hablar de parámetros. Se conserva el detalle anterior/actual de parámetros normales y se registra la operación de MAILPASS mediante su identificador, sin serializar la contraseña. No se cambiaron endpoints ni la lectura interna de SMTP.

El recorrido posterior terminó con **21/21 comprobaciones reales aprobadas**. El detalle está en `resultado_cierre6_real.json` y el ejecutor en `NuevoAvatar.Estudiante.React/tests/reales/cierre_auditoria6.mjs`. También se verificaron la lectura de los cuatro mantenimientos, la máscara de MAILPASS en API y tabla, y el cambio de orden por columna en Usuarios con registros reales.

Para reproducirlo, el proceso requiere `TEST_USER`, `TEST_PASSWORD`, `STUDENT_USER` y `STUDENT_PASSWORD`. `PROBAR_REVOCACION=1` activa la retirada temporal y restauración de ADM8; debe ejecutarse en una ventana coordinada porque el permiso pertenece al rol compartido. El ejecutor conserva las asignaciones originales y las comprueba al finalizar. No contiene credenciales ni imprime tokens. Se ejecuta desde `NuevoAvatar.Estudiante.React` mediante `node tests/reales/cierre_auditoria6.mjs`.

El entorno de esta comprobación inició Autenticación, Bitácora, Rol, Parámetro, Módulo, Usuario, Institución, Expediente, Carrera, Dirección y Gateway. Usuario y Expediente se enlazaron a `127.0.0.1` porque Tailscale ya ocupaba sus puertos en la interfaz de red; no se alteró la configuración de Tailscale. La lectura del expediente requiere Dirección. No se crearon estudiantes, instituciones ni registros de negocio. Se conservaron los registros de auditoría producidos por las operaciones probadas.

Este recorrido cierra las comprobaciones de ingreso y revocación que estaban bloqueadas por el entorno. No sustituye la matriz completa de aceptación ni el paquete documental compartido del equipo.
