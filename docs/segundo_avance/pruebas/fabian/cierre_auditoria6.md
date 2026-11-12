# Seguimiento de auditoría 6: administración

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

## Verificación real pendiente

La evidencia anterior está en `resultado_pruebas_reales.md` y corresponde a su fecha y datos. No se considera repetida después de la limpieza de datos comunicada por el equipo.

- La consulta real confirmó que `fabian@cuc.cr` coincide con su expediente en correo, tipo e identificación. No se alteraron las cuentas preparadas por José. Falta comprobar el recorrido desde el navegador con esa cuenta.
- Repetir permisos/revocación con dos cuentas sobre la versión integrada, conservando las asignaciones originales.
- TAMPAG confirmado en la base compartida con valor 15 después de aplicar el script guardado.
- Completar la aceptación real de filtros, orden, paginación, errores y bitácoras con la versión final de los servicios.
- Coordinar con el equipo el menú de presentación de las veinte historias y el paquete documental común.

El PDF de ADM6 repite el texto de cambios de permisos al hablar de parámetros. Se conserva el detalle anterior/actual de parámetros normales y se registra la operación de MAILPASS mediante su identificador, sin serializar la contraseña. No se cambiaron endpoints ni la lectura interna de SMTP.

La comprobación HTTP posterior no llegó a ejecutarse por un bloqueo del entorno de ejecución. No se presenta como prueba aprobada ni como fallo del sistema. Se conserva la aceptación real pendiente indicada arriba.
