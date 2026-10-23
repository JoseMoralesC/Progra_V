# Integracion de servicios con bases independientes

Los 22 proyectos `NuevoAvatar.*` de la raiz son las aplicaciones activas. Cada servicio conecta a `PrograV_<Servicio>` con su cuenta `svc_avatar_<servicio>`. Las carpetas agrupadas bajo `services/` se conservan como referencia historica y no utilizan este nuevo cableado.

La base `PrograV` permanece como origen de la migracion. Los servicios adaptados no la consultan. La distribucion de las 26 tablas de negocio se mantiene en `db/por_servicio/distribucion.json`.

## Configuracion y arranque

El `.env` local conserva las coordenadas de SQL Server por Tailscale y las credenciales administrativas para tareas de migracion. Contiene ademas `SERVICES_INTERNAL_KEY` y las variables `SERVICE_DB_USER_<SERVICIO>` / `SERVICE_DB_PASSWORD_<SERVICIO>`, que no se versionan ni se muestran en los reportes.

Cada aplicacion selecciona las credenciales correspondientes a su servicio. Java usa `IntegracionBootstrap` y .NET selecciona las variables de servicio al construir su conexion. Esto tambien se aplica al ejecutar desde el IDE; el antiguo `DB_NAME=PrograV` no se usa como destino de las aplicaciones independientes.

Ejemplos desde la raiz:

```powershell
powershell -NoProfile -File tools/ejecutar-servicio.ps1 -Servicio Usuario -Verificar
powershell -NoProfile -File tools/ejecutar-servicio.ps1 -Servicio Usuario
```

Los cinco archivos `ejecutar_persona*.bat` arrancan los servicios por integrante. `tools/servicios.json` contiene los 22 servicios, sus puertos y bases. `tools/detener-servicios.ps1` conserva la comprobacion de pertenencia al repositorio antes de detener procesos.

La infraestructura de correo sigue usando las variables `MAIL_*`. La suite de integracion no envia correos.

## Cuentas SQL

Las cuentas humanas `jose`, `ramses`, `fabian` y `alejandro` tienen acceso a las 22 bases separadas. `jose` conserva la propiedad preexistente de esas bases y su usuario efectivo es `dbo`. Las otras tres cuentas reciben `db_datareader`, `db_datawriter`, `CONNECT` y `EXECUTE`: pueden consultar, insertar, actualizar y eliminar datos, ademas de ejecutar procedimientos. Estos permisos no incluyen crear o modificar la estructura de las tablas. Las cuentas humanas son distintas de las cuentas limitadas de cada microservicio.

`python tools/preparar-usuarios-remotos.py` audita las 88 combinaciones de cuenta/base mediante `EXECUTE AS LOGIN`, verificando los permisos efectivos sobre todas las tablas, incluida la tabla tecnica de Pago. Tambien comprueba que las cuentas esten habilitadas y sin bloqueo o vencimiento de clave. `--aplicar` agrega los usuarios y permisos que corresponden sin cambiar contrasenas, propiedad de bases, roles del servidor ni permisos del origen. El reporte sin claves queda en `docs/primer_avance/pruebas/usuarios-remotos.json`. La comprobacion de permisos no autentica desde los equipos personales: cada integrante debe utilizar su clave actual y tener conectividad Tailscale con SQL Server.

`tools/preparar-cuentas.py --aplicar` crea los logins y usuarios de servicio con `db_datareader` y `db_datawriter` exclusivamente en su base. No concede `db_owner`, permisos DDL ni acceso a la base original. Para ejecutarlo se necesita una cuenta con `ALTER ANY LOGIN` y permiso para administrar los usuarios de destino.

Las contrasenas se generan y guardan localmente antes de crear cada login para permitir recuperar una ejecucion interrumpida. Las cuentas existentes no se borran ni se les cambia la contrasena automaticamente. Si faltan `JWT_KEY` o `SERVICES_INTERNAL_KEY`, la preparacion genera sus valores locales; conserva los existentes.

## Contratos internos

Las fuentes compartidas estan en `NuevoAvatar.Integracion` para .NET e `integracion/src/main/java` para Java. Ambos usan `integracion/contratos.json`, que declara los recursos que posee cada servicio, sus campos permitidos y referencias entrantes.

Los endpoints `/interno/**` exigen `X-Servicio-Key`. No dependen de un token de usuario: de esa forma Autenticacion puede verificar credenciales y obtener parametros antes de emitir el primer JWT.

| Contrato | Funcion |
|---|---|
| `GET /interno/estado` | Identifica servicio y base efectiva; requiere clave interna. |
| `GET /interno/datos/{recurso}` | Consulta de recursos del propietario con filtros de igualdad permitidos. |
| `GET /interno/referencias/{recurso}?campo=...&valor=...` | Comprueba referencias sin exponer los registros. |
| `POST Usuario/interno/credenciales` | Verifica BCrypt y devuelve solo el email si coincide. |
| `POST Bitacora/interno/bitacora` | Registro de acciones .NET sin consultar tablas de otro servicio. |
| `GET Pago/interno/operaciones-pago/{id}` | Estado persistido de una operacion tecnica. |
| `POST Factura/interno/operaciones-pago/{id}` | Aplica una operacion confirmada por su propietario. |

Los recursos y columnas SQL se resuelven desde una lista fija, no desde SQL enviado por el consumidor. Los filtros se parametrizan. Usuario no expone `password_hash`; Autenticacion no expone refresh tokens. Las rutas de consulta de datos no aceptan operaciones de escritura genericas.

Las bases/entidades ajenas dejan de ser repositorios JPA locales. Sus adaptadores consultan las APIs internas. Las uniones de Historial y Listado se realizan con respuestas de los propietarios. Expediente conserva sus dos tablas y reconstruye provincia/canton consultando Direccion.

## Validacion de referencias

Las creaciones y modificaciones validan las referencias externas por API. Los borrados fisicos comprueban las referencias entrantes; si una dependencia no responde, se rechaza la operacion en lugar de asumir que no hay referencias. Las FK internas permanecen en SQL Server.

La eliminacion de Matricula sigue siendo una anulacion, por lo que no elimina las referencias historicas. Cambiar la identificacion de un estudiante con referencias externas se rechaza con 409: una cascada SQL ya no puede atravesar las bases y requiere una operacion de negocio coordinada diferente.

Las comprobaciones HTTP de referencias son previas a la escritura. No equivalen a una FK o transaccion distribuida ante operaciones simultaneas de alta/borrado en servicios diferentes. Para garantizar ese caso se necesita un protocolo adicional de reserva de referencias o una politica de baja logica acordada. La recuperacion descrita a continuacion cubre especificamente Pago/Factura.

## Recuperacion de Pago y Factura

La migracion adicional `db/por_servicio/migraciones/001_operaciones_pago.sql` agrega `finanzas.OperacionPago` en la base de Pago. Es la tabla tecnica autorizada para recuperacion; no cambia las columnas, montos ni registros migrados.

1. Pago serializa las operaciones de una factura con `sp_getapplock` y registra el pago/reverso y su operacion pendiente en la misma transaccion local.
2. Tras confirmar esa transaccion, intenta comunicar la operacion a Factura.
3. Factura bloquea su propia fila y vuelve a consultar la operacion en Pago antes de cambiar su estado. Un evento ya completado no vuelve a modificarla.
4. Pago marca la operacion COMPLETADA al recibir confirmacion. Si la comunicacion falla queda PENDIENTE y un proceso programado vuelve a intentarlo, tambien despues de reiniciar el servicio.
5. Un conflicto funcional de estado en Factura marca FALLIDA la operacion y compensa el estado de Pago. No se borran pagos ni se cambian montos.

Los workers se coordinan mediante un bloqueo SQL de sesion por factura en la base de Pago. No mantienen una transaccion local de escritura abierta durante la llamada HTTP. El receptor valida el estado de la operacion bajo su propio bloqueo de fila para impedir que una entrega antigua deshaga operaciones posteriores.

Durante una interrupcion puede existir consistencia temporal: Pago ya tiene la operacion persistida y Factura todavia no la aplico. En ese caso la solicitud devuelve 503 con indicacion de recuperacion pendiente; no informa que ambas escrituras terminaron. La frecuencia de reintento predeterminada es de tres segundos, configurable con `pago.reintento-ms`.

## Verificacion reproducible

Compilar los nueve proyectos .NET y empaquetar los trece proyectos Maven con `tools/compilar-servicios.ps1` antes de ejecutar:

```powershell
python tools/probar-integracion.py
```

La suite comprueba las cuentas contra las bases reales y crea 22 bases temporales `PrograV_IT_<identificador>_<Servicio>`. Copia la estructura/datos de origen y usa las mismas cuentas de servicio con permisos temporales en sus bases de prueba correspondientes. Los fixtures academicos, el usuario de prueba y las operaciones financieras se escriben exclusivamente en esas bases.

Arranca procesos ocultos y acotados de memoria, por grupos de dependencias. Comprueba consultas, autorizacion, referencias, matricula/notas/historial/listado y pagos. Un proxy de prueba permite leer Factura pero simula 503 al aplicar el pago; tras reiniciar Pago, comprueba recuperacion automatica, reentregas antiguas y concurrencia.

Al terminar detiene solo los procesos que inicio y elimina exclusivamente las bases temporales creadas por esa ejecucion. Los logs locales quedan bajo `tmp/`, ignorado por Git. El reporte sin credenciales esta en `docs/primer_avance/pruebas/integracion-separada.json`.

Si SQL Server deja de responder durante la limpieza, el reporte conserva los nombres de las bases temporales pendientes. Al recuperar la conexion, ejecutar `python tools/probar-integracion.py --limpiar-pendientes docs/primer_avance/pruebas/integracion-separada.json` (o el reporte de la fase correspondiente). La opcion valida el prefijo unico y los servicios antes de eliminar; no acepta bases de negocio. El campo `temporary_databases_cleaned` indica si terminaron de retirarse. Limpiar no cambia el resultado funcional de una prueba fallida.

`tools/migrar-bases.py --verificar` sigue comparando las 26 tablas originales. Permite expresamente la tabla tecnica adicional de Pago, cuya funcionalidad se prueba en la suite. Los datos de negocio y el esquema original de `PrograV` se conservan.
