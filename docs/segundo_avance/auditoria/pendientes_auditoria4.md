# Pendientes de la auditoría 4 - Segundo avance

Fecha: 10 de noviembre de 2026. Versión base: `4eeb3362dc1071fb4fb9bb046a9ea0e195724460`, más el árbol local.
Informe: [auditoría 4](auditoria_avance4.md). Evidencia: [resultados actuales](evidencias4/resultados_actuales.json).

Mantengo aquí únicamente el levantamiento vigente de faltantes y verificaciones. La existencia de implementación y pruebas locales no constituye aceptación integral. Como equipo conservo las veinte historias del segundo avance, sus responsables y el criterio de no sumar dos veces dependencias comunes.

## Jose

### J1. Acreditar el ciclo de sesión con propietarios reales — ADM2, GTW1 y GTW2

**Prioridad alta de verificación.** El ingreso, identidad, menú, restauración, renovación y salida están disponibles por gateway. La interfaz useAuth que consumen las páginas se conserva y las pruebas locales comprueban los tres perfiles, los errores y las respuestas tardías.

Mantengo pendiente un recorrido con cuentas reales: login → inicio del perfil → operación autorizada → renovación → salida. Debemos demostrar que retirar un permiso o desactivar un módulo bloquea la siguiente operación, que un rol incorrecto no obtiene datos y que una dependencia caída no produce un éxito ficticio. Hace falta contrastar los registros persistidos de login/logout, no solo las llamadas simuladas.

La continuidad con las experiencias completas de Alejandro y Fabian sigue siendo una dependencia de aceptación. Recordar sesión es opcional en el PDF; la restauración disponible no agrega otra HU ni puntos.

### J2. Demostrar notas y concurrencia en SQL Server — ADM17

**Prioridad alta de verificación.** Notas tiene destino funcional en la plantilla, acceso del profesor, regreso, desglose/calificaciones y restricciones por grupo. Las pruebas de servidor y navegador están disponibles.

Falta comprobar con dos conexiones o instancias reales la primera nota frente al reemplazo de rubros, dos altas del mismo par matrícula/rubro, timeout y rollback. El bloqueo SQL está implementado; los dobles de las pruebas no acreditan su comportamiento en la base compartida. Debemos revisar antes/después y los registros reales de Bitácora, además de completar el recorrido desde el dashboard final de Fabian.

### J3. Cerrar evidencia y aclaración del contrato — GTW1 y GTW2

**Prioridad alta para aceptación.** Los destinos, cuentas y asignaciones deben demostrarse en el entorno acordado. No propongo volver a ejecutar una inicialización de permisos sobre revocaciones administradas ni aplicar migraciones sin verificar su estado.

La referencia SRV7 del PDF V2 no tiene una correspondencia inequívoca en el documento de servicios consultado. Mantengo la aclaración pendiente; no debe convertirse por deducción en una ruta pública. Falta la ficha final por criterio con esperado, observado, captura y versión del recorrido real.

## Ramses

### R1. Acreditar los seis mantenimientos con sus servicios reales

**ADM7, ADM9, ADM10, ADM11, ADM12 y ADM22. Prioridad alta de verificación.** Las seis rutas están disponibles en la entrada común con sesión, permisos y regreso. Los listados usan paginación parametrizada y los formularios conservan confirmaciones y mensajes. El registro guardado se busca en el listado para mostrar la página correspondiente.

Falta demostrar cada criterio con propietarios reales: TAMPAG y su máximo de quince; instituciones/profesores/carreras de los selectores; restricciones de eliminación; filtros, orden y persistencia después de guardar. Las consultas auxiliares de lectura no conceden mantenimiento de sus recursos. Debemos comprobar también ausencia de permiso, vencimiento y caída de dependencias.

### R2. Verificar bitácoras persistidas — ADM7, ADM9, ADM10, ADM11 y ADM12

**Prioridad alta de verificación.** Encuentro registros de consultas y estados JSON de los cambios en las áreas revisadas. Falta contrastarlos en Bitácora real para alta, actualización, eliminación y error técnico, con usuario/fecha y estados exigidos por el PDF.

La entrega no puede acreditar el registro solo porque una prueba haya comprobado la llamada. Si el servicio de Bitácora está caído, debemos demostrar el resultado real y documentar cualquier diferencia entre negocio confirmado y registro pendiente.

### R3. Comprobar profesores con los parámetros y relaciones reales — ADM11

**Prioridad alta de verificación.** Formato local y teléfonos equivalentes con guiones/prefijo están validados. Las pruebas cubren edad, duplicados y grupos activos con dependencias controladas.

Mantengo pendiente comprobar DOMPROF, búsquedas/orden/paginación, edición de teléfonos y rechazo de borrado cuando dicta grupos activos, con datos de sus propietarios. Las capturas deben identificar el criterio y la versión, sin exponer datos sensibles.

### R4. Acreditar concurrencia de periodos entre conexiones — ADM12

**Prioridad alta de verificación.** La regla consulta mismo año/número, excluye el propio registro al editar y el repositorio usa transacción serializable con bloqueos. La prueba simultánea local demuestra una instancia del servicio con repositorio en memoria.

Faltan dos conexiones o instancias SQL reales, números distintos con fechas superpuestas, extremos de fecha, duplicado año/número y rollback ante fallo. No presento el semáforo de una instancia como garantía distribuida ni la prueba en memoria como prueba de SQL Server.

### R5. Verificar SMTP, historial y consumo del dashboard — ADM22

**Prioridad alta de verificación.** La implementación distingue envío realizado sin historial, intenta registrar errores de configuración/transporte y comunica que no debe reenviarse un mensaje ya enviado. Los escenarios de pruebas usan un transporte simulado.

Falta demostrar envío e historial reales, configuración ausente, rechazo SMTP y fallo del historial después de enviar. Si también falla SQL al intentar guardar el error, no podemos afirmar que ese intento quedó persistido. Debemos comprobar qué mensaje recibe el usuario y cómo aparece el resultado en el dashboard de Fabian. Ninguna comprobación de esta revisión envió correos reales.

## Alejandro

### A1. Responsive y registro de navegación — ADM1

**Prioridad alta.** Mantengo el reconocimiento de la plantilla compartida. El corte previo midió desbordamiento horizontal en 390 px; no se modificó el layout. Faltan comprobar desktop/tablet/móvil y registrar acceso, rutas críticas y errores de interfaz. La navegación estudiantil también debe mantener una única opción activa al entrar a rutas hijas.

### A2. Confirmación de cambios del expediente — WEB2

**Prioridad alta.** Guardar cambios envía PUT sin confirmación previa, mientras WEB2 C4 la exige. Propongo confirmar/cancelar sin perder el formulario y mostrar el resultado persistido. Faltan correspondencia territorial y propiedad con servicios reales; los endpoints y comprobaciones locales de identidad ya existen.

### A3. Reglas y cancelación de prematrícula — WEB4

**Prioridad alta.** El cliente filtra cursos por carrera, pero el servidor revisado no hace la comparación equivalente. Tampoco localicé validación de solicitudes previas estudiante/curso/periodo ni una restricción equivalente entre solicitudes en el SQL disponible. Rechazar un curso repetido dentro del body no resuelve esa duplicidad.

Propongo validar relación y unicidad al crear/editar, incluido el caso simultáneo, siguiendo el proceso del proyecto si requiere datos. La cancelación envía DELETE sin confirmación y el listado sigue sin paginación. Estos puntos no se modificaron en el formulario ni servicio de negocio durante el presente corte.

### A4. Parámetros, cierre de sesiones y bitácora del perfil — ADM24

**Prioridad alta.** React y Usuario conservan una política fija de contraseña. El documento inicial del equipo la explica, pero no reemplaza el requisito de longitud/complejidad parametrizable. Propongo acordar los parámetros con Fabian y conservar el límite técnico de BCrypt sin registrar credenciales.

Falta demostrar cierre de sesiones en el cliente y requerimiento de login en los contextos correspondientes. El cambio de nombre registra solo el campo modificado, sin JSON anterior/actual. El PDF no obliga literalmente a salir de forma automática después de cambiar contraseña; no uso esa comprobación complementaria para inventar un requisito.

### A5. Recorrido real del inicio estudiantil — WEB1

**Prioridad de verificación.** Estado del expediente, cantidad de solicitudes y accesos propios están disponibles. Faltan cuentas y servicios reales, usuario sin vínculo, error de dependencia, sesión vencida y consulta registrada. No propongo llenar tarjetas con datos ficticios ni rehacer lo disponible.

## Fabian

### F1. Indicadores y aceptación del dashboard — ADM3

**Prioridad alta.** El dashboard dispone de destino funcional de Notas. Mantengo pendiente la comprobación completa de ambos perfiles con servicios reales y capturas por criterio.

Facturas pendientes sigue dependiendo de permisos a recursos globales que la preparación inicial A2 no concede. Propongo coordinar con Jose una consulta acotada al indicador requerido, sin habilitar un mantenimiento adicional. Deben verificarse periodos activos/cerrados, totales, dependencia caída y ausencia de permisos.

### F2. Permisos de página y actualización del menú — ADM4 y ADM5

**Prioridad alta.** Las páginas administrativas conservan controles accesibles por URL bajo un perfil general; el gateway mantiene su rechazo de datos no autorizados. La prueba de Instituciones sin permiso continúa fallando porque la página no presenta el bloqueo esperado y conserva sus controles.

Propongo completar restricciones por página, estados de acceso y actualización del menú después de guardar la matriz. La persistencia de permisos debe comprobarse con otra cuenta real, incluyendo revocación e inactividad. No se modificaron esos formularios ni sus pruebas para aprobar el corte.

### F3. Revisión de edición/búsqueda en Instituciones — ADM8

**Prioridad alta.** La ejecución final no encontró «Instituto Mejorado» después de editar y buscar inmediatamente. El guardado limpia filtros y recarga el listado de forma asíncrona, mientras la búsqueda permanece disponible: hay que revisar la coordinación de esas respuestas y el momento en que el escenario continúa.

Registro el resultado y esa causa probable encontrada en la inspección; no afirmo que una edición haya fallado en SQL Server, porque la prueba usa API interceptada. Propongo comprobar primero la secuencia y la persistencia observada, distinguir defecto de interfaz de sincronización de prueba y corregir lo que corresponda. Las [capturas actuales](evidencias4/instituciones-edicion.png) y el [registro de casos](evidencias4/resultados_actuales.json) conservan el resultado.

### F4. Persistencia y evidencia de mantenimientos — ADM4, ADM5, ADM6 y ADM8

**Prioridad alta para cierre.** Falta comprobar migraciones 004/005, persistencia entre consultas y bitácoras reales. Institución tiene comprobación de referencias en el interceptor compartido; no la declaro ausente por no encontrarse dentro de InstitucionService. Debe demostrarse con y sin carreras y con la dependencia caída.

No localicé suite propia del servicio de Institución. Las pruebas de interfaz complementan su revisión, pero no sustituyen esa aceptación. Parámetros debe coordinar con ADM24 el contrato de política de contraseña.

## Pendientes del equipo

| Orden | Trabajo pendiente | Responsable de continuidad | Condición de salida |
| ---: | --- | --- | --- |
| 1 | Completar las historias pendientes de plantilla, trámites propios, perfil y administración | Alejandro y Fabian según HU; apoyo de contratos de Jose/Ramses | Experiencias completas sobre la entrada común. |
| 2 | Revisar migraciones, asignaciones y cuentas del entorno acordado | Cada propietario; coordinación de los cuatro | Preparación comprobada, sin reaplicar inicialización sobre permisos administrados. |
| 3 | Demostrar los seis recorridos del roadmap | Cada responsable de HU | Datos y servicios reales, errores, vencimiento, datos ajenos y bitácoras. |
| 4 | Completar evidencia por criterio | Cada integrante | Fuente, esperado, observado, captura, versión y revisión. |
| 5 | Consolidar documentación A2 y entrega | Equipo | Diagramas, scripts, guía, conclusiones, recomendaciones, bibliografía, tablero y PR comprobables. |

Permanecen enlaces a documentos ausentes en README raíz e índice A2: `docs/desarrollo-java.md`, `docs/limpieza-proyecto.md`, `contexto-desarrollo.md` y `desarrollo-jose.md`. Propongo ajustar las referencias vigentes conservando las auditorías históricas.

Los artefactos Java seleccionados están empaquetados y verificados localmente. Ese resultado no acredita un despliegue ni permite declarar completas las veinte historias. Mantengo el cierre integral abierto hasta demostrar los criterios y la integración con las partes pendientes de Alejandro y Fabian.
