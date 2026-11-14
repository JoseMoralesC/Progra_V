# Escritura y lectura de bitácora

La descripción almacenada y la descripción que devuelve el historial corresponden al mismo ID de bitácora. El campo `accion` es solamente una etiqueta legible; el detalle desplegable conserva `descripcion`, `fechaHora`, `idBitacora` e `idEvento`. Las credenciales se ocultan antes de persistir eventos nuevos y al mostrar registros históricos.

## Entrega persistente

Cada servicio emisor utiliza `general.auditoria_pendiente` en su propia base. El Gateway comparte la cola de Autenticación. La cola no almacena Bearer, contraseñas ni tokens de sesión. La entrega usa la clave interna de servicios y conserva un GUID de evento durante todos los reintentos.

El receptor guarda ese GUID en `PrograV_Bitacora.general.bitacora.id_evento`. Un índice único permite reenviar después de una caída o una confirmación perdida sin crear otra fila. La cola solo elimina el pendiente al recibir el mismo GUID y un ID de bitácora válido. Si el receptor está caído, el evento queda en la base emisora y el trabajador reintenta; también se recupera al reiniciar el servicio.

Los procesos de negocio y sus eventos se guardan juntos en las rutas de escritura integradas:

- EF: roles, parámetros, módulos y asignaciones de roles a módulos.
- ADO/Dapper: cursos, grupos y períodos.
- Spring: usuarios y perfil, instituciones, profesores, carreras, expedientes, prematrículas y notas/desgloses.
- Spring: matrícula, factura y registro/reverso local de pago; la conciliación de factura registra su propia operación interna con actor `servicio:Pago`.

El correo SMTP es un efecto externo: su envío no participa en una transacción SQL. La auditoría registra el resultado observado; no convierte SMTP y SQL en una transacción distribuida. Las consultas, autenticación, navegación y errores se encolan en sus puntos de captura; la cola asegura entrega después de quedar persistidos. No se afirma capturar una petición que nunca alcanzó esos puntos ni un proceso que termina antes de encolarla.

## Activación y cambios en SQL

El arranque valida primero que cada servicio apunta a su base propia y prepara la infraestructura de forma idempotente. Requiere permisos para crear la tabla y, en Bitácora, añadir `id_evento` y su índice. Los scripts están en:

- `integracion/auditoria-pendiente.sql`: ejecutar en cada base emisora.
- `db/migraciones/bitacora-entrega-fiable.sql`: ejecutar en `PrograV_Bitacora`.

Los scripts no borran el historial. Si las cuentas del entorno tienen permisos restringidos, un administrador debe preparar el esquema antes del arranque. Se evita iniciar con una auditoría aparentemente activa pero sin almacenamiento persistente.

`python tools/preparar-auditoria.py` aplica estos scripts en las bases del manifiesto y concede al usuario de cada servicio únicamente `SELECT`, `INSERT`, `UPDATE` y `DELETE` sobre su tabla de pendientes. Usa `AUDITORIA_ADMIN_USER`/`AUDITORIA_ADMIN_PASSWORD` si están definidos, o las credenciales generales de `.env`. No concede permisos de creación de esquema a los servicios.

Cada instancia del equipo debe ejecutar esta versión y apuntar mediante `BITACORA_BASE_URL` al receptor que utiliza la base compartida. `localhost` significa la máquina de esa instancia. Los cambios directos en SQL o realizados por otra aplicación no generan automáticamente estos eventos; no se instalaron triggers de auditoría.

## Consulta y actualización

El administrador puede recorrer todos los registros, incluidos los no clasificables. El profesor mantiene sus restricciones. Las páginas de historial conservan un corte por ID para que las inserciones concurrentes no desplacen los registros. Cada diez segundos, cuando la pestaña está visible, se comprueba si hay movimientos nuevos y se muestra un aviso. El administrador pulsa «Actualizar historial» para incorporarlos; la comprobación no desmonta la tabla, cierra sus detalles ni reemplaza sus filas. Ante un fallo se conservan los datos ya cargados.

Las consultas automáticas del menú usadas para autorizar cada petición no se registran como acciones del usuario, para evitar llenar el historial durante su propia lectura.

Los indicadores del inicio (usuarios, carreras, cursos, períodos, facturas y grupos del profesor), el resumen de notificaciones y las lecturas auxiliares del resumen se identifican como cargas automáticas. El gateway autoriza normalmente cada petición, retira las cabeceras internas suministradas por el navegador y añade su propia marca únicamente en las rutas GET del dashboard previstas. El servicio emisor exige además la clave interna compartida y una respuesta 2xx antes de omitir su registro como consulta del usuario. Las consultas normales al abrir un módulo, las escrituras y los errores conservan su auditoría. La visita al inicio mantiene su evento de dashboard. Esta corrección no borra ni reclasifica registros históricos.

`python tools/probar-dashboard.py` verifica el refresco con datos ficticios y un servidor Vite aislado, sin acceder a la DB. Requiere Playwright para Python y Chromium. `PruebaConsultaAutomatica` verifica en Java la marca confiable y que las escrituras y errores nunca se omitan.

## Verificación

Sin dependencias externas:

```powershell
dotnet run --project NuevoAvatar.Gateway.Tests/NuevoAvatar.Gateway.Tests.csproj -c Release
```

Pruebas de entrega y transacciones con SQL Server:

```powershell
. ./tools/urls-entorno.ps1
Cargar-EntornoLocal
dotnet run --project NuevoAvatar.Gateway.Tests/NuevoAvatar.Gateway.Tests.csproj -c Release -- --entrega-sql
```

La prueba SQL crea una base temporal de nombre único, valida caída del receptor, recuperación, confirmación perdida, idempotencia, coincidencia de lectura/escritura, rollback y ocultación de secretos, y elimina únicamente esa base. Requiere permisos para crear y eliminar su base de prueba. Java cuenta además con `PruebaAuditoriaTransaccion`, que verifica con `JdbcTemplate` y `TransactionTemplate` el commit y rollback conjuntos con el receptor desconectado.

Para seguir un movimiento concreto, registrar usuario, proceso, instancia y hora; localizar su `id_evento` en la cola emisora o en la bitácora central, y comparar ese mismo ID y descripción con el detalle del frontend. Un pendiente no es una entrega confirmada.
