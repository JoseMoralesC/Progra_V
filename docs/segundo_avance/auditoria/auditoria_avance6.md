# Auditoría de avance 6 - Proyecto Nuevo Avatar V2

Fecha: 12 de noviembre de 2026.

Actualización del mismo corte: incorporo el recorrido con las credenciales suministradas, servicios activos, navegador y SQL Server reales. El detalle queda en el [recorrido real de esta auditoría](evidencias6/recorrido-real/README.md). Mantengo el consecutivo 6 y el alcance del segundo avance.

Seguimiento posterior solicitado por Jose: corregí H11, el rechazo de nombres de rubro repetidos en Notas. La nueva verificación dio **55/55 pruebas de Notas, 3/3 de contratos y 9/9 comprobaciones reales aprobadas**. El [cierre de H11](evidencias6/cierre-jose-h11/README.md) conserva el detalle. Los conteos anteriores describen el diagnóstico previo; no sumo otra vez sus casos repetidos.

## Resumen

Revisé nuevamente el proyecto y repetí las pruebas sobre la versión integrada. Encuentro avances respecto de la auditoría 5: el indicador de facturas ya tiene una consulta limitada al dashboard, los ejecutores aislados terminan correctamente y el equipo incorporó evidencia real de mantenimientos, concurrencia de períodos y envío y recepción de correo.

**Todavía no considero la entrega lista para presentarla como un cumplimiento del 100%.** Siguen pendientes los duplicados y la paginación configurable de prematrícula. Además, encuentro una regresión en el inicio estudiantil: consulta la información, pero dejó de mostrar el estado del expediente, el número de solicitudes y los mensajes de vacío o error. También se oculta el pie común en expediente y perfil estudiantil.

En el corte local ejecuté **576 casos: 548 aprobados y 28 fallidos**. En la actualización ejecuté **156 comprobaciones reales adicionales: 144 aprobadas y 12 fallidas**. El conjunto contado reúne **732 comprobaciones, 692 aprobadas y 40 fallidas**; no equivale a cuarenta defectos, porque varios casos observan el mismo problema desde API y navegador. Separo los errores funcionales de los escenarios que quedaron desactualizados después de los cambios visuales. Las pruebas tienen barras propias; no las utilizo para medir aportes individuales.

Con las credenciales pude ingresar como administrador y estudiante. La cuenta estudiantil suministrada tiene un expediente con el mismo correo, pero difieren su identificación y tipo de documento; por eso no obtiene un vínculo válido. Sin corregir esa cuenta, preparé otra temporal coherente y comprobé contacto, alta, modificación y cancelación de prematrícula. También recorrí Notas con un profesor temporal. Confirmé duplicados y el 500 ante rubros repetidos; encontré una edición de correo que no puede guardar y la contraseña SMTP visible en Parámetros. No incluí ese valor en las evidencias.

Estimo el desarrollo comprometido en **92,0417%, aproximadamente 92%**. Explico el cálculo por historia más adelante. Dejé los faltantes, sus causas y responsables en [pendientes de la auditoría 6](pendientes_auditoria6.md). En esta revisión no corregí código de la aplicación ni sus pruebas existentes.

## Alcance auditado

Como criterio de equipo, mantengo únicamente las veinte historias y los 120 puntos del segundo avance:

| Responsable | Historias comprometidas                | Puntos |
| ----------- | -------------------------------------- | -----: |
| Jose        | ADM2, ADM17, GTW1, GTW2                |     32 |
| Ramses      | ADM7, ADM9, ADM10, ADM11, ADM12, ADM22 |     30 |
| Alejandro   | ADM1, ADM24, WEB1, WEB2, WEB4          |     28 |
| Fabian      | ADM3, ADM4, ADM5, ADM6, ADM8           |     30 |
| Total       | 20 historias                           |    120 |

Revisé la estructura general, los proyectos .NET y Java, las tres aplicaciones React, los componentes compartidos, rutas, permisos, reglas de negocio, bitácoras, scripts, arranque y documentación. Contrasté las partes que atienden estas historias con el PDF y la planificación. Considero los servicios heredados como apoyo de identidad, oferta, notas e indicadores; no les asigno historias adicionales.

### Acuerdos del equipo que mantengo

Tomo como base el [PDF de requisitos](<../requerimientos/Proyecto%20Nuevo%20Avatar%20V2%20%281%29.pdf>), el [roadmap del segundo avance](<../roadmaps/roadmap%202.md>), el [reparto](../planificacion/backlog.md), los [contratos](../planificacion/contratos.md) y los [criterios por historia](../planificacion/subhistorias.md).

Ante la omisión de la web estudiantil comunicada por el profesor, utilizo exclusivamente WEB1, WEB2 y WEB4 de la [especificación elaborada por el equipo](../requerimientos/web-estudiantes.md). Mantengo la decisión de acercar ambas webs mediante la misma plantilla, identidad, navegación, avisos, confirmaciones y perfil, respetando las operaciones propias de cada usuario. Reutilizar esa base no duplica puntos.

### Mi criterio de verificación

Distingo inspección del código, pruebas con respuestas controladas y evidencia con servicios reales. El corte inicial incluyó suites locales, diagnóstico estudiantil, la prueba SQL de Notas y seis consultas de lectura. Después, con la nueva solicitud y las credenciales, inicié veinte procesos de negocio/gateway y un servicio heredado de apoyo necesario para consultar referencias al borrar períodos. Los 21 utilizaron los destinos compartidos del manifiesto, incluido el historial de Notificación.

Recorrí administrador, estudiante suministrado, estudiante temporal vinculado y profesor temporal. Preparé registros propios para comprobar operaciones sin alterar las cuentas ni expedientes suministrados. Retiré los datos temporales y sus sesiones, conservé las bitácoras y detuve los procesos que había iniciado. No apliqué migraciones, no cambié parámetros institucionales ni permisos de las cuentas suministradas y no envié correos adicionales. Las credenciales llegaron a los ejecutores en memoria y quedaron fuera de los informes y capturas.

La evidencia real actual amplía la aceptación, pero no demuestra todos los criterios y fallos posibles del sistema. Conservo aparte las evidencias anteriores del equipo; no las sumo otra vez a las comprobaciones nuevas.

## Criterios generales del PDF y roadmap

| Criterio / fuente                                     | Estado que encuentro                                                       | Condición pendiente                                                                                                                                                            |
| ----------------------------------------------------- | -------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Reparto del segundo avance                            | Conservado: 20 historias y 120 puntos                                      | Mantener este mismo compromiso en la presentación.                                                                                                                             |
| React y gateway, p. 21                                | Compilan los tres clientes; .NET termina aprobado                          | Vincular los artefactos y la guía al entorno definitivo.                                                                                                                       |
| Plantilla, pp. 4-5                                    | Base compartida, rutas, avisos y adaptación de tamaño                    | Resolver doble ruta activa, identidad móvil y pie omitido en dos vistas estudiantiles.                                                                                         |
| Login y sesión, p. 5                                 | Pruebas actuales aprobadas y recorrido real previo disponible              | Completar el expediente de aceptación por criterio.                                                                                                                            |
| Usuarios, roles, parámetros e instituciones, pp. 6-8 | Implementados; indicador con permiso acotado                               | Actualizar escenarios de interfaz y consolidar evidencia del mismo entorno.                                                                                                     |
| Oferta, pp. 8-9                                       | Mantenimientos y nuevas evidencias reales disponibles                      | Completar criterios de relaciones, borrado, filtros, validaciones y auditoría.                                                                                                 |
| Notas, p. 11                                          | 46/46 aprobadas, incluida regresión con SQL real                          | Tratar rubros repetidos con respuesta funcional. La secuencia HTTP inversa ya quedó comprobada.                                                                                |
| Correo, p. 12                                         | El equipo documentó envío, recepción e historial; parámetros presentes | Acordar la base de historial y completar los escenarios de fallo restantes.                                                                                                     |
| Perfil, p. 13                                         | Política parametrizable y salida entre pestañas                          | Actualizar tres pruebas Java y resolver la edición de correo ofrecida sin operación disponible. El cambio de contraseña temporal, hash y bitácora sí quedaron comprobados. |
| WEB1 del equipo                                       | Regresión confirmada en el resumen del inicio                             | Mostrar estado, cantidad, vacío, falta de vínculo y error.                                                                                                                    |
| WEB2 del equipo                                       | Edición acotada y confirmación disponibles                               | Contacto temporal guardado y recargado; queda corregir el vínculo de la cuenta suministrada y completar los demás criterios territoriales.                                    |
| WEB4 del equipo                                       | Propiedad, carrera, nivel y fecha disponibles                              | Impedir duplicados entre solicitudes y consumir paginación configurable.                                                                                                       |
| Bitácoras, pp. 16-17                                 | Hay código y evidencias identificadas por operación                      | Reunir la matriz por criterio; no equiparar un conteo de registros con todos los JSON exigidos.                                                                                 |
| Listados, pp. 17-19                                   | Paginación en mantenimientos                                              | Prematrícula conserva diez elementos fijos y no usa el parámetro común.                                                                                                      |
| Entregables, pp. 20-22                                | Código, scripts, planificación y pruebas disponibles                     | Consolidar análisis/diseño, diagramas, matriz de aceptación, enlaces y comprobantes de entrega.                                                                              |

## Comparación contra auditorías anteriores

| Punto anterior                                              | Resultado de esta revisión                                                                                                                                      |
| ----------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Bloqueo transaccional de Notas                              | Conservo el cierre de la auditoría 5 y lo respaldé con una nueva ejecución SQL aprobada.                                                                      |
| Doce fallos de sesión/Notas de Jose                        | No reaparecieron: 27/27 en la plantilla y 6/6 en su cliente aislado.                                                                                             |
| Cierre del cliente aislado de Ramses                        | Ahora terminó con 6/6 y sin errores generales.                                                                                                                  |
| Indicador de facturas condicionado por un permiso adicional | La consulta del dashboard devuelve solo el conteo y exige ADM3; siete nuevos casos de gateway pasan. Retiro ese faltante de desarrollo.                          |
| Parámetros de contraseña sin confirmar                    | Confirmé por lectura PASSMIN=8, PASSMAX=30, PASSMAY=true y PASSNUM=true. No falta desarrollarlos; siguen desactualizadas tres pruebas de perfil.                |
| Correo sin envío real acreditado                           | Reconozco la evidencia del equipo del 12 de noviembre, incluida la recepción. No repetí el envío.                                                             |
| Historial remoto de notificaciones ausente                  | La tabla compartida ya existe. El arranque de presentación conserva el destino local; falta unificar la decisión operativa.                                    |
| Inicio estudiantil con resumen                              | Dejó de mostrarlo en esta versión. Lo registro como regresión, no como faltante que ya existía en el corte anterior.                                         |
| Confirmación de expediente y cancelación                  | Las dos continúan disponibles y sus comprobaciones de diagnóstico pasan.                                                                                       |
| Duplicados, tamaño de página, menú e identidad móvil    | Mantengo abiertos los hallazgos; los tres últimos se reprodujeron en navegador.                                                                                 |
| Pruebas administrativas                                     | Las rutas de auditoría se prepararon en varios escenarios, pero bienvenida, menús de acciones y tabla del dashboard volvieron a desajustar las comprobaciones. |

## Auditoría por persona y trazabilidad por HU

### Persona 1 - Jose

Responsabilidad: **4 historias, 32 puntos**. Mantengo el reconocimiento de sus correcciones y distingo el corte local, los antecedentes y el recorrido real actual.

| HU / fuente      | Estado disponible                                                                                                | Pendiente para cierre                                                                                                                                        | Desarrollo estimado |
| ---------------- | ---------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------ | ------------------: |
| ADM2 / p. 5      | Ingreso, identidad, restauración, refresh y salida; 17 escenarios de sesión aprobados.                         | Consolidar evidencia por criterio sobre la versión de presentación.                                                                                        |                 95% |
| ADM17 / p. 11    | Desglose/notas integrados; 46 casos Java y 10 de interfaz aprobados; bloqueo con dos conexiones reales aprobado. | Validar rubros repetidos con respuesta clara. La secuencia HTTP inversa quedó comprobada con 200 para reemplazo y 422 para la nota sobre el rubro anterior. |                 95% |
| GTW1 / pp. 15-16 | Enrutamiento y manejo de fallos cubiertos; recorrido real previo identificado.                                   | Guía común de arranque, destinos y versión de artefactos.                                                                                                 |                 95% |
| GTW2 / p. 16     | Sesión, permisos, propiedad y rechazos cubiertos; suite de gateway aprobada.                                    | Módulo inactivo en entorno aislado y aclaración documental de SRV7.                                                                                        |                 95% |

### Persona 2 - Ramses

Responsabilidad: **6 historias, 30 puntos**. Reconozco las nuevas evidencias de CRUD, concurrencia de períodos y correo. Los nueve fallos de la plantilla afectan la preparación de sus escenarios frente al modal de bienvenida; no los convierto automáticamente en nueve defectos de los servicios.

| HU / fuente   | Estado disponible                                                                                                     | Pendiente para cierre                                                                             | Desarrollo estimado |
| ------------- | --------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------- | ------------------: |
| ADM7 / p. 8   | Módulos, estado, orden, asignación y consulta de roles; recorrido real documentado.                                 | Escenarios de interfaz actuales y configuración común de permisos.                              |                 90% |
| ADM9 / p. 8   | Carreras con institución/director, filtros y restricciones; CRUD real documentado.                                   | Evidencia completa de relaciones, director, borrado y JSON por criterio.                          |                 90% |
| ADM10 / p. 8  | Cursos con nivel/carrera y filtros; consulta y CRUD reales documentados.                                              | Acreditar todos los bloqueos por referencias y la aceptación de interfaz.                        |                 90% |
| ADM11 / p. 9  | Validaciones de profesor y búsqueda; CRUD real documentado.                                                          | Completar dominio parametrizado, edad, teléfonos y grupos activos con sus evidencias.            |                 90% |
| ADM12 / p. 9  | Fechas/estado y conflicto; el equipo obtuvo 201 y 409 ante altas simultáneas.                                        | Completar la matriz de fechas, solapamientos y auditoría; no asumir prueba entre dos instancias. |                 90% |
| ADM22 / p. 12 | Destinatarios, sanitización, confirmación e historial; configuración SMTP preparada y envío recibido documentado. | Unificar historial local/compartido y acreditar fallos de transporte y persistencia.              |                 95% |

### Persona 3 - Alejandro

Responsabilidad: **5 historias, 28 puntos**. Reconozco plantilla, modales, perfil, edición de contacto y confirmaciones. Reduzco la estimación de WEB1 porque la vista actual perdió información exigida, aunque conserve las consultas.

| HU / fuente         | Estado disponible                                                         | Faltante vigente                                                                                                                                         | Desarrollo estimado |
| ------------------- | ------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------: |
| ADM1 / pp. 4-5      | Plantilla compartida y seis mediciones sin desbordamiento en los inicios. | Ruta activa única, identidad en móvil, pie común y revisión de formularios/tablas/diálogos.                                                         |                 90% |
| ADM24 / p. 13       | Perfil, política configurable, BCrypt y limpieza de pestañas.           | Actualizar tres pruebas Java y resolver la edición de correo no disponible. El cambio real de contraseña temporal y su bitácora quedaron comprobados. |                 90% |
| WEB1 / equipo C1-C6 | Inicio, identidad y accesos propios; las consultas siguen ejecutándose.  | Restituir resumen del expediente, cantidad, vacío, falta de vínculo y errores visibles.                                                                |                 75% |
| WEB2 / equipo C1-C6 | Campos protegidos, edición acotada y confirmación antes de PUT.         | Contacto temporal guardado y recargado; cuenta suministrada con identidad inconsistente. Completar evidencia territorial/anterior-actual.                |                 95% |
| WEB4 / equipo C1-C6 | Identidad propia, validaciones de oferta y cancelación confirmada.       | Duplicados entre solicitudes, también al editar y en paralelo; paginación configurable.                                                                |                 75% |

### Persona 4 - Fabian

Responsabilidad: **5 historias, 30 puntos**. Reconozco la corrección del permiso del indicador y los parámetros reales de contraseña. Las ocho pruebas fallidas de su interfaz requieren revisar la bienvenida, los selectores de acciones y las expectativas de la nueva tabla.

| HU / fuente    | Estado disponible                                                                             | Pendiente para cierre                                                                                                           | Desarrollo estimado |
| -------------- | --------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------- | ------------------: |
| ADM3 / pp. 5-6 | Dashboard por perfil e indicador limitado al conteo; siete casos nuevos de permiso aprobados. | Actualizar dos comprobaciones de bitácora y consolidar evidencia actual por criterio.                                          |                 95% |
| ADM4 / pp. 6-7 | Usuarios, filtros, roles, detalle, cambios y controles por pantalla.                          | Adaptar el recorrido de interfaz y reunir restricciones/orden/paginación/errores.                                              |                 95% |
| ADM5 / p. 7    | Matriz de permisos y actualización del menú; índice único de roles confirmado.            | Actualizar recorrido y acreditar nuevamente revocación con otra cuenta.                                                        |                 95% |
| ADM6 / pp. 7-8 | Parámetros y validaciones; política de contraseña presente en SQL compartido.              | Evitar la exposición de MAILPASS en la tabla y revisar su tratamiento al auditar; completar la aceptación de administración. |                 90% |
| ADM8 / p. 8    | Instituciones, búsqueda, confirmaciones y bloqueo por carreras documentados.                 | Actualizar tres escenarios y completar evidencia de restricción/dependencia caída.                                            |                 95% |

## Hallazgos y diferencias

| ID  | Prioridad / estado                            | Hallazgo y causa                                                                                                                                                                                                                               | Responsable                                                          |
| --- | --------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------- |
| H01 | Alta; reproducido con servicios reales        | Dos solicitudes iguales del estudiante temporal fueron aceptadas con 201. La validación no contrasta estudiante/curso/período con otras solicitudes. Ambas se retiraron.                                                                     | Alejandro, WEB4                                                      |
| H02 | Alta; reproducido                             | Preparé TAMPAG=2 y se mostraron diez solicitudes: la vista mantiene una constante. En la lectura compartida tampoco encontré TAMPAG; no modifiqué el entorno.                                                                               | Alejandro, WEB4; Fabian prepara el parámetro común                 |
| H03 | Media; reproducido                            | Inicio y Mi expediente quedan activos simultáneamente por coincidencia de prefijo.                                                                                                                                                            | Alejandro, ADM1                                                      |
| H04 | Media; reproducido                            | La regla móvil oculta nombre y rol en la barra de usuario.                                                                                                                                                                                    | Alejandro, ADM1                                                      |
| H05 | Alta de verificación; vigente                | Tres pruebas de perfil no preparan la política remota y esperan reglas anteriores; reciben 503 o no llegan a comprobar el caso previsto.                                                                                                      | Alejandro, ADM24                                                     |
| H06 | Alta de verificación; actualizado            | Diecisiete escenarios de la plantilla fallan: bienvenida sin cerrar, acciones reorganizadas y expectativas de bitácora anteriores. No corregí escenarios ni inferí fallos de negocio solo por sus tiempos agotados.                         | Fabian y Ramses en sus suites; Alejandro coordina el contrato visual |
| H07 | Resuelto en código y pruebas locales         | La consulta del indicador ya exige ADM3, limita parámetros y devuelve solo el conteo.                                                                                                                                                         | Fabian, ADM3; coordinación con Jose                                 |
| H08 | Parcialmente cerrado                          | Inicié y recorrí el entorno compartido, incluido el historial de Notificación. El ejecutor de presentación sigue configurándolo local; falta alinear la guía. La baja de período necesita también su servicio heredado de referencias. | Ramses; coordinación del equipo                                     |
| H09 | Alta para entrega; vigente                    | Falta consolidar el paquete documental actualizado y encontré seis destinos de documentación ausentes.                                                                                                                                       | Los cuatro integrantes                                               |
| H10 | Resuelto; regresión SQL aprobada             | El bloqueo de Notas conserva el inicio de transacción corregido. Dos conexiones confirmaron retención y liberación sin cambiar tablas.                                                                                                      | Jose, ADM17                                                          |
| H11 | Corregido y verificado posteriormente         | Duplicados rechazados con mensaje claro antes de escribir; la equivalencia detectada por SQL devuelve 409 con rollback. Verificación: 55/55 de Notas y 9/9 comprobaciones reales aprobadas.                                                   | Jose, ADM17                                                          |
| H12 | Alta; regresión reproducida                  | El inicio estudiantil almacena estados y cantidad, pero el JSX solo muestra enlaces. No comunica disponibilidad, vacío, falta de vínculo o caída.                                                                                           | Alejandro, WEB1                                                      |
| H13 | Alta para delimitar la entrega                | La navegación contiene un mantenimiento administrativo de expedientes ajeno a las veinte historias y hay una asignación de su módulo en SQL. Requiere delimitar el menú de presentación; no lo evalúo ni le asigno puntos.               | Alejandro y Fabian                                                   |
| H14 | Media; regresión reproducida                 | `StudentLayout` desactiva el pie de página en expediente y perfil. En expediente comprobé que no existe el pie común.                                                                                                                     | Alejandro, ADM1                                                      |
| H15 | Alta para presentación; datos inconsistentes | Usuario y Expediente comparten el correo de la cuenta suministrada, pero difieren en identificación y tipo. La vinculación exige los tres campos y devuelve 404. No corregí esos registros.                                                 | Fabian, cuenta; Alejandro, expediente; Jose apoya identidad          |
| H16 | Media; reproducido en navegador real          | El perfil estudiantil ofrece editar correo, pero al guardar informa que el backend no lo permite. La acción no tiene una operación compatible.                                                                                               | Alejandro, ADM24                                                     |
| H17 | Alta de confidencialidad; comprobado          | MAILPASS se devuelve y representa como valor legible en Parámetros. Registré solo indicadores de presencia/exposición y oculté la tabla en la captura.                                                                                     | Fabian, ADM6; Ramses coordina SMTP                                   |

Dejo ubicaciones, causas, propuestas de cierre y dependencias en [pendientes_auditoria6.md](pendientes_auditoria6.md). Mantengo separados un defecto reproducido, un riesgo identificado por inspección y una aceptación que todavía necesita evidencia.

### Dependencias que afectan al cierre

La plantilla es compartida: sus cambios requieren coordinación con los escenarios administrativos. Los parámetros sostienen contraseña, correo y paginación; tenerlos en una base no asegura que todos los procesos consulten ese mismo destino. La integración de oferta necesita los servicios propietarios de referencias para distinguir un conflicto de una dependencia caída. Como equipo mantengo un solo entorno de presentación identificado y evidencia ligada a su versión antes de declarar cierre.

## Verificación ejecutada

Ejecuté .NET, Java con continuación tras fallos, Node, compilación de las tres aplicaciones, revisión estática y navegador. La suite Java terminó con error por los tres casos de perfil, pero continuó con los otros módulos: no quedaron ocultos detrás de ese fallo. Institución no aportó casos Java en esta selección; su verificación local está en la interfaz y la evidencia previa del equipo.

La primera ejecución de la plantilla alcanzó el límite global: 11 aprobadas, 5 fallidas y 31 omitidas. Repetí los 47 casos con tres trabajadores y diez segundos por caso: terminaron 30 aprobados y 17 fallidos. Para descartar que ese límite reducido causara fallos, los confirmé con el tiempo normal de treinta segundos por caso y seis minutos de límite global. El resultado fue nuevamente **30 aprobados y 17 fallidos, sin omisiones ni errores generales**. Conservo los tres registros y cuento solo esta última ejecución. Interpreto los tiempos agotados junto con sus trazas; no los trato como defectos nuevos sin comprobar su causa.

En el recorrido estudiantil de auditoría cierro la bienvenida como lo haría el usuario y uso el botón actual de guardado; mantengo las expectativas de los requisitos. Sus 17 casos incluyen cuatro estados del inicio, seis tamaños/perfiles, confirmaciones, ruta activa, identidad móvil, pie, paginación y salida entre pestañas. Utiliza respuestas controladas y no escribe en los servicios reales.

La revisión SQL/JPA examinó **22 servicios y 298 fuentes, sin infracciones detectadas por sus reglas**. El validador aceptó los trece JAR disponibles. Estas comprobaciones no certifican todas las reglas de negocio ni equivalen a volver a desplegar cada artefacto. El análisis estático terminó con código cero y **18 advertencias**, principalmente sobre efectos de React; no las cuento como pruebas fallidas.

### Pruebas ejecutadas por integrante

El [registro por caso](evidencias6/resultados-conservados.md#resultados) permite revisar estado, grupo, responsable y evidencia. Atribuyo las comprobaciones al compromiso actual, aunque el nombre histórico de una carpeta sea diferente. De los 139 casos del gateway, asigno a Fabian los siete nuevos sobre el indicador ADM3 y a Jose los 132 restantes, conservando la base anterior y sin duplicarlos.

| Integrante     | Pruebas ejecutadas en esta auditoría                                                                                                                                 | Estado                                                                                          |
| -------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------- |
| Alejandro      | Expediente 135/135; Prematrícula 79/79; perfil propio 1/4; recorrido estudiantil 9/17. **224 aprobadas y 11 fallidas de 235**.                                 | Tres fallos de preparación de perfil y ocho fallos funcionales/de presentación del recorrido. |
| Fabian         | Usuario, sin perfil propio, 10/10; indicador/permiso del gateway 7/7; interfaz administrativa 2/10. **19 aprobadas y 8 fallidas de 27**.                        | Escenarios desajustados frente a bienvenida, acciones y bitácora.                              |
| Jose           | Autenticación 29/29; gateway 132/132; contratos 3/3; Notas 46/46; Node 10/10; sesión/Notas en plantilla 27/27; cliente aislado 6/6. **253 aprobadas de 253**. | Sin fallos en los casos ejecutados; la regresión SQL sí se ejecutó, no quedó omitida.       |
| Ramses         | Cursos/períodos 19/19; Carrera 3/3; Profesor 4/4; Notificación 5/5; Node 14/14; interfaz principal 1/10; aislada 6/6. **52 aprobadas y 9 fallidas de 61**.    | Nueve escenarios de la plantilla afectados por su preparación ante la bienvenida.              |
| Total incluido | **548 aprobadas y 28 fallidas de 576**.                                                                                                                         | Ningún caso omitido en el resultado final incluido.                                            |

También terminaron aprobadas dos comprobaciones heredadas de grupos que dejo fuera del compromiso y de las barras. Compilaciones, lectura SQL, artefactos y revisión estática se documentan aparte y no inflan el número de casos.

### Barras del resultado de pruebas

Calculo estas barras como **casos aprobados / casos incluidos por integrante**. No expresan cobertura de requisitos, aportes ni una comparación de calidad personal. Las barras tienen veinte posiciones y se redondean visualmente; la fracción y el porcentaje son la referencia.

| Integrante | Barra de pruebas           | Aprobadas / incluidas | Resultado |
| ---------- | -------------------------- | --------------------: | --------: |
| Alejandro  | `[###################-]` |               224/235 |    95,32% |
| Fabian     | `[##############------]` |                 19/27 |    70,37% |
| Jose       | `[####################]` |               253/253 |   100,00% |
| Ramses     | `[#################---]` |                 52/61 |    85,25% |
| Total      | `[###################-]` |               548/576 |    95,14% |

### Evidencia real incorporada como antecedente

| Responsable | Evidencia disponible                                                                                                                                          | Cómo la utilizo                                                                                                                               |
| ----------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| Jose        | [Seguimiento de sesión, gateway y Notas](../pruebas/jose-cierre-auditoria5/README.md): 45/45 comprobaciones globales; detalle de navegador 14/14 ya incluido. | Reconozco su alcance anterior. No sumo esas comprobaciones a los 576 ni cuento dos veces el navegador.                                         |
| Fabian      | [Recorridos de ADM3/ADM4/ADM5/ADM6/ADM8](../pruebas/fabian/resultado_pruebas_reales.md), con actualización del 12 de noviembre.                               | Reconozco persistencia, permisos e indicador del momento descrito; no reutilizo sus conteos como valores actuales de la base.                  |
| Ramses      | [Evidencia real del 12 de noviembre](../pruebas/ramses/evidencias-reales/2026-11-12/README.md): CRUD 26/26, concurrencia de períodos y correo recibido.       | Reconozco el envío posterior que supera el párrafo previo de SMTP sin configurar; no lo presento como un envío repetido en esta auditoría. |

### Recorrido real adicional con las credenciales suministradas

Conservo aquí el resultado anterior a corregir H11. Su nuevo estado y las comprobaciones posteriores están en el seguimiento específico; no borro ni recalculo este antecedente.

Ejecuté **156 comprobaciones: 144 aprobadas y 12 fallidas**, con [detalle por caso](evidencias6/recorrido-real/resultados-conservados.md#resultados-reales), [resumen y límites](evidencias6/recorrido-real/README.md), capturas con datos ocultos y constancias de limpieza. Estas barras pertenecen al recorrido real y permanecen separadas de las 576 pruebas locales anteriores.

| Integrante | Qué comprobé en el sistema activo                                                                  | Aprobadas / incluidas | Fallidas | Barra de pruebas reales    | Resultado |
| ---------- | ---------------------------------------------------------------------------------------------------- | --------------------: | -------: | -------------------------- | --------: |
| Alejandro  | Perfil, contraseña, expediente, prematrícula, resumen y delimitación del menú.                   |                 21/31 |       10 | `[##############------]` |    67,74% |
| Fabian     | Administración, indicador, formularios, parámetro temporal, restricciones y confidencialidad SMTP. |                 20/21 |        1 | `[###################-]` |    95,24% |
| Jose       | Ingreso, restauración, permisos, datos ajenos, salida, Notas, concurrencia y persistencia.          |                 79/80 |        1 | `[####################]` |    98,75% |
| Ramses     | Oferta, módulos, roles de módulo, filtros, restricciones, períodos e historial compartido.        |                 24/24 |        0 | `[####################]` |   100,00% |
| Total      | Recorrido adicional sin duplicar contenedores.                                                       |               144/156 |       12 | `[##################--]` |    92,31% |

En Alejandro los diez casos fallidos corresponden al acceso ajeno al compromiso en el menú, tres consultas y dos vistas de la cuenta suministrada sin vínculo coherente, los resúmenes de inicio de dos cuentas, el duplicado de prematrícula y la edición de correo no disponible. El fallo de Fabian es la exposición de MAILPASS; el de Jose es la respuesta 500 ante rubros repetidos. No interpreto esas diez observaciones de Alejandro como diez defectos distintos ni como una nota individual. Las barras se redondean visualmente; la fracción es la referencia.

En Notas excluyo del conteo el contenedor de navegador y cuento sus catorce casos detallados una sola vez. Los intentos iniciales de preparación están identificados y fuera del conteo final. Confirmé el orden inverso de concurrencia: el reemplazo obtuvo 200 y la nota que apuntaba al rubro anterior obtuvo 422, sin datos parciales. Esto completa la condición del orden HTTP inverso pendiente en el corte anterior.

Los formularios administrativos abrieron al cerrar la bienvenida y utilizar las acciones actuales. Persistí la edición de un parámetro y el borrado de la institución temporal vinculada fue bloqueado. Con el estudiante temporal guardé y recargué contacto, creé, modifiqué y cancelé una solicitud; la cuenta suministrada conservó su inconsistencia original. Cambié únicamente la contraseña de otra cuenta temporal: pude ingresar con la nueva, la anterior fue rechazada y comprobé BCrypt y bitácora.

Consulté el historial compartido de correo y abrí/cancelé su formulario. **No ejecuté un envío SMTP adicional**; conservo para ese criterio la evidencia anterior de envío y recepción del equipo. Retiré cuentas, expedientes, solicitudes, registros y sesiones temporales; conservé las bitácoras y detuve los 21 procesos propios. Los accesos suministrados siguieron funcionando al cierre. Las barras de aportes permanecen sin cambios: el seguimiento agrega verificación y hallazgos, no desarrollo nuevo.

### Evidencia documental y entregables

| Entregable                      | Resultado de mi revisión                                                                                                                                                                                                                                              |
| ------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Requisitos y reparto            | PDF, criterios estudiantiles y veinte historias identificados.                                                                                                                                                                                                         |
| Análisis y diseño actualizado | Hay planificación, contratos y materiales anteriores; no localicé un paquete consolidado que cubra todos los apartados de pp. 21-22 para este corte.                                                                                                                 |
| Diagramas y conclusiones        | Deben quedar ligados al análisis actual, junto con recomendaciones y bibliografía; no doy por actualizado el documento maestro anterior.                                                                                                                             |
| Scripts y cambios de datos      | Disponibles. La aplicación observada de algunos cambios no sustituye el comprobante de aprobación exigido por el curso.                                                                                                                                              |
| Guía de ejecución             | Existen ejecutores; falta resolver y explicar el destino de Notificación, el parámetro de página y la delimitación del menú.                                                                                                                                      |
| Pruebas por criterio            | Amplias suites y evidencias reales parciales; falta reunir criterio, esperado, observado, captura y versión de las veinte historias.                                                                                                                                  |
| Tablero y PR a versión final   | Revisé el historial local, incluidos merges; no verifiqué estado del tablero ni aprobación remota. No los declaro inexistentes.                                                                                                                                     |
| Enlaces                         | Encontré seis destinos ausentes: `docs/desarrollo-java.md`, `docs/limpieza-proyecto.md`, `contexto-desarrollo.md`, `desarrollo-jose.md`, `pruebas/criterio-evidencia.md` y `pruebas/jose-concurrencia-permisos.md` bajo sus referencias correspondientes. |

## Barras de progreso por persona

Estas barras comparan **el desarrollo disponible con el compromiso de cada integrante**. Conservo la escala de las auditorías anteriores: 0% sin desarrollo, 25% base, 50% componentes parciales, 75% implementación principal con pendientes y 100% desarrollo completo e integrado. Los valores intermedios son estimaciones razonadas; no son una nota ni un porcentaje automático de cumplimiento del PDF.

### Base del cálculo por historia

| Responsable | Estimación de sus historias                      | Fundamento                                                                                                                     |
| ----------- | ------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------ |
| Jose        | ADM2/ADM17/GTW1/GTW2: 95% cada una                | Mantengo el aporte reconocido; repetir pruebas aprobadas no agrega desarrollo.                                                 |
| Ramses      | ADM7/ADM9/ADM10/ADM11/ADM12: 90%; ADM22: 95%      | Mantengo los cinco mantenimientos; reconozco en correo la configuración operativa e integración de envío ahora disponibles. |
| Alejandro   | ADM1 90%; ADM24 90%; WEB1 75%; WEB2 95%; WEB4 75% | Mantengo los aportes disponibles y reduzco WEB1 de 95% a 75% por la información retirada de la vista.                         |
| Fabian      | ADM3/ADM4/ADM5/ADM8: 95%; ADM6: 90%               | Mantengo su estimación; reconozco la corrección del indicador sin convertir siete pruebas nuevas en puntos extra.            |

Fórmula: **suma(puntos de historia × desarrollo estimado) / puntos asignados**. ADM1 pesa ocho puntos y las otras historias de Alejandro cinco; cada historia de Ramses pesa cinco. Mantengo los pesos del backlog para Jose y Fabian.

| Integrante | Barra de aportes           | Estimación | Cálculo   |
| ---------- | -------------------------- | ----------: | ---------- |
| Jose       | `[###################-]` |      95,00% | 30,4 / 32  |
| Ramses     | `[##################--]` |    90,8333% | 27,25 / 30 |
| Alejandro  | `[#################---]` |    87,3214% | 24,45 / 28 |
| Fabian     | `[###################-]` |      94,50% | 28,35 / 30 |

**Progreso ponderado: `[##################--]` = 92,0417%, aproximadamente 92%.** La cuenta es `(30,4 + 27,25 + 24,45 + 28,35) / 120`. Frente al 92,6667% anterior, WEB1 resta un punto ponderado y ADM22 suma 0,25. No cambio aportes por tener más o menos pruebas aprobadas. Las barras se redondean a veinte posiciones.

## Avance del proyecto

| Indicador                           | Resultado                                      | Interpretación                                                                                      |
| ----------------------------------- | ---------------------------------------------- | ---------------------------------------------------------------------------------------------------- |
| Historias revisadas del corte       | 20/20                                          | Todas tienen revisión de fuentes, requisitos y estado; no equivale a veinte aceptaciones completas. |
| Desarrollo estimado                 | 92,0417%                                       | Comparación ponderada de aportes con el compromiso individual.                                      |
| Pruebas locales iniciales           | 548/576 aprobadas                              | Resultado técnico del conjunto ejecutado y atribuido.                                               |
| Recorrido real adicional            | 144/156 aprobadas                              | Servicios, navegador y SQL reales; 12 observaciones fallidas documentadas.                           |
| Conjunto de comprobaciones contadas | 692/732 aprobadas                              | Suma de ambos grupos, sin repetir el contenedor de navegador de Notas.                               |
| Plantilla compartida                | Disponible, con diferencias                    | Las webs conservan una base común; hay regresiones concretas de información y pie.                 |
| Recorridos reales                   | Evidencia ampliada, de versiones identificadas | No certifico los seis recorridos completos sobre esta versión con las suites controladas.           |
| Cumplimiento del 100%               | No acreditado                                  | Persisten faltantes funcionales, de aceptación y de entrega documental.                             |

## Prioridades para continuar

| Orden | Trabajo pendiente                                                                                                            | Responsable                                     |
| ----: | ---------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------- |
|     1 | Proteger la credencial SMTP expuesta en Parámetros y revisar su tratamiento al auditar.                                     | Fabian y Ramses                                 |
|     2 | Acordar y preparar los datos correctos de identificación de la cuenta estudiantil de presentación.                         | Fabian y Alejandro; apoyo de Jose               |
|     3 | Recuperar información y estados del inicio estudiantil; impedir duplicados de prematrícula y usar el tamaño configurable. | Alejandro; Fabian prepara el parámetro común  |
|     4 | Conservar identidad móvil, ruta activa única y pie común; delimitar navegación de presentación.                         | Alejandro; coordinación de permisos con Fabian |
|     5 | Actualizar pruebas del perfil y los escenarios afectados por bienvenida, acciones y bitácora.                               | Alejandro, Fabian y Ramses según historia      |
|     6 | Consolidar el cierre de H11 y completar los acuerdos/evidencias pendientes de permisos.                                      | Jose                                            |
|     7 | Unificar el destino del historial y la guía del entorno; completar aceptación de mantenimientos y fallos de correo.        | Ramses y equipo                                 |
|     8 | Consolidar documentación, matriz por criterio, enlaces y comprobantes de entrega.                                           | Los cuatro integrantes                          |

## Estado al cierre de la revisión

Dejo el segundo avance **en verificación, con pendientes que impiden presentarlo como terminado al 100%**. El recorrido real confirma operaciones administrativas, contacto y trámites de una cuenta coherente, perfil/contraseña y Notas. También confirma duplicados, la respuesta 500 y los problemas de identidad, edición de correo y confidencialidad indicados. Como criterio de equipo, mantengo el cierre de esos puntos y la revisión documental antes de declarar lista la entrega. No corregí errores ni datos de las cuentas suministradas; los registros temporales se retiraron.
