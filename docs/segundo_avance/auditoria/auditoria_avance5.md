# Auditoría de avance 5 - Proyecto Nuevo Avatar V2

> Estado al 13 de noviembre de 2026: las pruebas automatizadas y sus ejecutores fueron retirados. Las referencias a archivos de pruebas y comandos de ejecucion de este documento son historicas; los resultados y capturas se conservan como evidencia. El detalle de los retiros esta en `docs/mapeo-pruebas/limpieza-realizada.csv`.


> Nota de mantenimiento (13 de noviembre de 2026): las pruebas automatizadas de `NuevoAvatar.Estudiante.React`, su configuración de Playwright y los ejecutores Python asociados se retiraron del proyecto. Las referencias a esos archivos y comandos en este informe corresponden a ejecuciones históricas y ya no son instrucciones vigentes. Se conservan los resultados y evidencias.


Actualización del 12 de noviembre de 2026: incorporo las correcciones de Jose y sus pruebas con servicios y SQL Server reales. El detalle está en el [seguimiento de Jose](../pruebas/jose-cierre-auditoria5/README.md) y los faltantes vigentes en [pendientes de la auditoría 5](pendientes_auditoria5.md). Conservo el consecutivo y el alcance del segundo avance.


## Resumen

Encuentro un segundo avance más completo que el descrito en la revisión anterior: ambas webs comparten plantilla, el expediente confirma antes de guardar, prematrícula confirma su cancelación, el perfil consulta una política de contraseña parametrizable y el cierre de sesión se comunica entre pestañas. También encuentro controles administrativos por pantalla, actualización del menú y un indicador de facturas implementado.

**Todavía no considero la entrega lista para presentarla como un cumplimiento del 100%.** Mantengo abiertos el rechazo de prematrículas duplicadas entre solicitudes, la paginación configurable del mismo listado, detalles de navegación e identidad en móvil y la preparación reproducible del entorno. Además, encontré pruebas desactualizadas y falta consolidar la evidencia de aceptación y el paquete documental del segundo avance.

Estimo el aporte de desarrollo en **92,6667%, aproximadamente 93%**. Esta cifra compara lo desarrollado con el compromiso individual; no resulta de contar pruebas aprobadas. Los resultados de pruebas tienen sus propias barras más adelante. Reconozco la corrección del bloqueo transaccional de Notas y las doce pruebas de Jose que ya pasan; mantengo la estimación de desarrollo mientras se completan sus condiciones de aceptación, sin aumentarla por el número de pruebas.

## Alcance auditado

Como equipo mantengo únicamente las veinte historias y 120 puntos de este corte:

| Responsable | Historias comprometidas                | Puntos |
| ----------- | -------------------------------------- | -----: |
| Jose        | ADM2, ADM17, GTW1, GTW2                |     32 |
| Ramses      | ADM7, ADM9, ADM10, ADM11, ADM12, ADM22 |     30 |
| Alejandro   | ADM1, ADM24, WEB1, WEB2, WEB4          |     28 |
| Fabian      | ADM3, ADM4, ADM5, ADM6, ADM8           |     30 |
| Total       | 20 historias                           |    120 |

Revisé el inventario del repositorio, documentación, requisitos, proyectos .NET y Java, tres aplicaciones React, rutas, sesión, permisos, reglas de negocio, consultas, scripts de datos, arranque y pruebas. Profundicé en las partes que atienden estas historias. Los servicios heredados se consideran apoyo de identidad, oferta, notas e indicadores; su existencia no agrega historias a esta entrega. Las carpetas antiguas de consumo web no sustituyen el portal React que revisé.

### Acuerdos del equipo que mantengo

Tomo el PDF como requisito oficial. Ante la omisión de la web estudiantil comunicada por el profesor, mantengo los criterios WEB1, WEB2 y WEB4 redactados por el equipo. Compruebo su semejanza con administración mediante la misma plantilla, encabezado, menú, avisos, confirmaciones y perfil, conservando las operaciones y los datos propios del estudiante.

Reconozco la plantilla UI/UX de Alejandro y mantengo un responsable por historia. Atribuyo las adaptaciones necesarias al compromiso actual, aunque una carpeta conserve la numeración antigua de otro integrante. No duplico puntos por componentes compartidos ni convierto una ejecución exitosa en una historia terminada.

### Mi criterio de verificación

Distingo la inspección, las pruebas con respuestas controladas y las ejecuciones reales. El corte inicial incluyó suites locales; en la actualización de Jose también ejecuté los tres perfiles y Notas contra servicios y SQL Server reales, además de una prueba de bloqueo con dos conexiones independientes. Mantengo separados los límites de cada evidencia. No acredité un envío SMTP real.

El corte inicial fue de diagnóstico. Después, con autorización, corregí las pruebas y el bloqueo de Notas de Jose, y ajusté el arranque/cierre de sus ejecutores. Levanté el entorno real y preparé un profesor, rol, permiso, grupos y matrículas temporales para recorrerlo. Retiré esos datos al terminar y conservé las bitácoras; los conteos de asignaciones, usuarios, notas y grupos volvieron a 14, 8, 4 y 3. No apliqué migraciones ni envié correos. Las credenciales no se copiaron a las evidencias y detuve los servicios que había iniciado. No verifiqué tablero ni aprobación remota del PR.

## Criterios generales del PDF y roadmap

| Criterio / fuente                   | Estado actual                              | Observación de cierre                                                                                                                                           |
| ----------------------------------- | ------------------------------------------ | ---------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Reparto del segundo avance          | Conservado                                 | Veinte historias, responsables y puntos sin cambios.                                                                                                             |
| Web React y gateway YARP, p. 21     | Disponible                                 | Construyeron las tres aplicaciones React; el portal principal consume `/gateway`.                                                                              |
| Login y sesión, p. 5 | Comprobados localmente y con servicios reales | Tres perfiles, restauración, renovación, salida, JWT vencido y registros del recorrido disponibles. |
| Plantilla, pp. 4-5                  | Avanzada, con diferencias                  | No encontré desbordamiento en las seis vistas de inicio medidas. En móvil se ocultan nombre/rol; el inicio estudiantil también queda activo en una ruta hija. |
| Usuarios, roles y permisos, pp. 6-8 | Implementados                              | Los controles por pantalla y la actualización del menú ya están en las fuentes; las pruebas de interfaz necesitan ponerse al día.                            |
| Oferta y correo, pp. 8-9 y 12       | Implementados con aceptación pendiente    | Persistencia, relaciones, concurrencia real y envío SMTP requieren evidencia final.                                                                             |
| Notas, p. 11 | Corregidas y verificadas con SQL real | Corregí el inicio de transacción previo al bloqueo. Comprobé dos conexiones SQL, operaciones simultáneas, espera agotada, rollback, persistencia y bitácoras. |
| Perfil, p. 13                       | Implementación ampliada                   | Ya existe política por parámetros y limpieza entre pestañas. Tres pruebas Java conservan la preparación anterior y fallan.                                   |
| Web estudiantil del equipo          | Parcial                                    | Expediente y confirmaciones disponibles; duplicados y paginación de prematrícula siguen pendientes.                                                            |
| Bitácoras, pp. 16-17 | Evidencia real de Jose incorporada | Notas registra altas y cambios con JSON nuevo/anterior/actual; los demás criterios conservan sus pendientes de aceptación. |
| Listados, pp. 17-19                 | Diferencia concreta                        | Prematrícula usa diez registros fijos; el acuerdo común exige tamaño parametrizable y máximo quince.                                                         |
| Datos y entregables, pp. 20-22      | Parciales                                  | Scripts presentes; faltan una preparación consistente del entorno y un paquete documental actualizado y verificable.                                            |

## Comparación contra auditorías anteriores

| Pendiente anterior                            | Resultado que encuentro en este corte                                                                                                          |
| --------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| Desbordamiento de la plantilla                | No se reprodujo en los inicios de administración y estudiante a 390, 768 y 1366 px. Esto no certifica todos los formularios en esos tamaños. |
| Registro de rutas y errores | La llamada de auditoría ya se contempla y comprueba en sesión/Notas de Jose. Los escenarios administrativos de Fabian siguen pendientes de actualización. |
| Expediente sin confirmación                  | Ya muestra confirmación y permite volver al formulario sin enviar PUT.                                                                        |
| Cancelación sin confirmación                | Ya espera confirmación antes de DELETE.                                                                                                       |
| Curso ajeno a la carrera                      | Ya se contrasta en `PrematriculaValidador` con datos recuperados del servicio propietario.                                                   |
| Listado estudiantil sin paginación           | Ya pagina; todavía utiliza una constante de diez y no el parámetro acordado.                                                                 |
| Política fija de contraseña                 | Ya se consulta en servidor y React. Falta actualizar sus pruebas y acreditar los valores en el entorno de presentación.                       |
| Cierre entre pestañas y JSON de perfil       | Ya encuentro comunicación de salida y auditoría anterior/actual del nombre.                                                                  |
| Permisos de página y actualización de menú | Ya existen en `AdminPage` y en el contrato de actualización tras guardar permisos.                                                          |
| Indicador de facturas ausente                 | Ya existe y contempla períodos cerrados; su disponibilidad depende de una asignación que el script inicial no entrega.                       |
| Evidencia real inexistente | Incorporé recorridos nuevos de Jose con los tres perfiles, SQL y gateway; las evidencias anteriores del equipo conservan su alcance original. |

## Auditoría por persona y trazabilidad por HU

### Persona 1 - Jose

Responsabilidad: **4 historias, 32 puntos**. Corregí el fallo real del bloqueo de Notas, actualicé los escenarios de sesión y resolví el cierre de sus ejecutores. Reconozco las nuevas comprobaciones reales; mantengo el 95% de aporte estimado por historia sin convertir las pruebas aprobadas en puntos de desarrollo.

| HU / fuente | Estado disponible | Verificación o documentación pendiente | Desarrollo estimado |
| --- | --- | --- | ---: |
| ADM2 / p. 5 | Ingreso, identidad, menú, recarga, renovación y salida de administrador, estudiante y profesor comprobados con servicios reales. Las pruebas de expiración pasan. | Consolidar la evidencia completa por criterio en el paquete final. | 95% |
| ADM17 / p. 11 | Desglose y notas operativos en la plantilla; bloqueo corregido. SQL real confirma persistencia, concurrencia, espera agotada, rollback y bitácoras. | Reproducir también el orden inverso de la carrera HTTP y revisar la respuesta 500 ante rubros repetidos. | 95% |
| GTW1 / pp. 15-16 | Recorridos por gateway real; destinos temporales inaccesibles confirman 502 para negocio y 503 para autenticación. | Consolidar la guía del entorno definitivo de presentación. | 95% |
| GTW2 / p. 16 | JWT vencido, perfiles no autorizados, grupo ajeno y revocación del permiso temporal comprobados. | Aclarar SRV7 y completar evidencia real de módulo inactivo sin afectar un módulo compartido. | 95% |

### Persona 2 - Ramses

Responsabilidad: **6 historias, 30 puntos**. Reconozco los seis mantenimientos integrados y la nueva evidencia de ingreso, módulos e historial. Conservo la estimación porque todavía falta demostrar la aceptación completa de cada mantenimiento y del correo.

| HU / fuente   | Estado disponible                                                                                         | Verificación pendiente                                                               | Desarrollo estimado |
| ------------- | --------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------- | ------------------: |
| ADM7 / p. 8   | Módulos, estado/orden, roles, listado y modal en la entrada común.                                      | Unificar el entorno de permisos: una evidencia utiliza base local alternativa.        |                 90% |
| ADM9 / p. 8   | Carreras con institución/director, filtros y bloqueo por cursos.                                         | Relaciones, búsqueda del director, persistencia y bitácoras reales por criterio.    |                 90% |
| ADM10 / p. 8  | Cursos, nivel, carrera, filtros, paginación y auditoría.                                                | Eliminación con grupos vinculados y recorrido completo con propietarios reales.      |                 90% |
| ADM11 / p. 9  | Edad, dominio configurable, nombre, teléfonos y restricciones.                                           | Parámetros, grupos activos y operaciones reales con evidencia identificada.          |                 90% |
| ADM12 / p. 9  | Fechas, estados derivados y comprobación de solapamiento con bloqueo en repositorio.                     | Concurrencia y rollback entre conexiones SQL reales.                                  |                 90% |
| ADM22 / p. 12 | Validación de destinatarios, HTML sanitizado, confirmación, historial y manejo de envío sin historial. | Envío SMTP real y fallo de transporte/historial; un historial vacío no lo acredita. |                 90% |

### Persona 3 - Alejandro

Responsabilidad: **5 historias, 28 puntos**. Reconozco los avances en plantilla, perfil y expediente. Mantengo separadas sus obligaciones de las suites antiguas del servicio que reutiliza.

| HU / fuente         | Estado disponible                                                                                  | Faltante vigente                                                                                                  | Desarrollo estimado |
| ------------------- | -------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------- | ------------------: |
| ADM1 / pp. 4-5      | Plantilla común, adaptación de tamaños, avisos, breadcrumbs y auditoría de interfaz.           | Una sola ruta activa en estudiante; nombre/rol accesibles en la barra móvil; revisión de todos los formularios. |                 90% |
| ADM24 / p. 13       | Perfil propio, política por parámetros, BCrypt, auditoría y salida entre pestañas.             | Actualizar pruebas al contrato actual y demostrar política/bitácora en entorno real.                            |                 90% |
| WEB1 / equipo C1-C6 | Inicio con expediente, solicitudes y accesos propios; diferencia vacío/error y falta de vínculo. | Recorrido real y evidencia completa de los estados y consultas.                                                   |                 95% |
| WEB2 / equipo C1-C6 | Edición acotada, selects territoriales, confirmación y auditoría.                               | Aceptación real con dos estudiantes y correspondencia territorial.                                               |                 95% |
| WEB4 / equipo C1-C6 | Identidad propia, nivel/carrera/período, cambios y cancelación confirmada.                       | Evitar duplicados entre solicitudes, también al editar y simultáneamente; paginación configurable.             |                 75% |

### Persona 4 - Fabian

Responsabilidad: **5 historias, 30 puntos**. Las fuentes actuales y las evidencias reales documentadas por el equipo permiten retirar varios pendientes de desarrollo de la auditoría anterior. No presento esas evidencias históricas como nuevas ejecuciones mías.

| HU / fuente    | Estado disponible                                                                               | Faltante vigente                                                                             | Desarrollo estimado |
| -------------- | ----------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------- | ------------------: |
| ADM3 / pp. 5-6 | Dashboard de ambos perfiles, indicador de facturas, actividad, soporte y acceso a notas/perfil. | Hacer reproducible el permiso del indicador y consolidar evidencia por criterio.             |                 95% |
| ADM4 / pp. 6-7 | Usuarios, filtros, roles, auditoría básica y bloqueo por pantalla.                            | Poner al día pruebas de interfaz y cerrar evidencia de filtros/orden/paginación y errores. |                 95% |
| ADM5 / p. 7    | Matriz persistible, revocación y actualización del menú tras guardar.                        | Preparación consistente de permisos y aceptación reproducible con otra cuenta.             |                 95% |
| ADM6 / pp. 7-8 | Parámetros, validaciones, paginación y auditoría.                                            | Confirmar valores y acuerdo operativo de la política de contraseña con Alejandro.          |                 90% |
| ADM8 / p. 8    | Instituciones, búsqueda, modales, controles de acceso y restricción por carreras.             | Actualizar pruebas y completar evidencia del contrato de eliminación y dependencia caída.  |                 95% |

## Hallazgos y diferencias

| ID  | Prioridad               | Hallazgo y causa                                                                                                                                                                                                                                                                                                                                                              | Responsable del segundo avance                                                             |
| --- | ----------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------ |
| H01 | Alta                    | No encontré rechazo de duplicados estudiante/curso/período entre solicitudes. El servicio valida la oferta y el cuerpo actual, pero el repositorio crea otra cabecera sin consultar solicitudes previas. No localicé una restricción equivalente entre cabeceras en los scripts revisados. Es una conclusión de inspección, pendiente de reproducción en la base real. | Alejandro, WEB4                                                                            |
| H02 | Alta                    | Reproduje diez solicitudes visibles con TAMPAG=2 preparado. `SOLICITUDES_POR_PAGINA = 10` evita consumir el parámetro.                                                                                                                                                                                                                                                     | Alejandro, WEB4                                                                            |
| H03 | Media                   | En expediente estudiantil quedan activos Inicio y Mi expediente. `NavLink` solo utiliza coincidencia exacta para `/inicio`; el prefijo `/estudiante` también coincide con sus rutas hijas.                                                                                                                                                                             | Alejandro, ADM1                                                                            |
| H04 | Media                   | En 390 px la barra oculta nombre y rol. La regla `.user-info { display: none; }` se aplica hasta 600 px. El ajuste de ancho no demuestra por sí solo todos los criterios de la plantilla.                                                                                                                                                                                  | Alejandro, ADM1                                                                            |
| H05 | Alta para verificación | Tres casos de `UsuarioPerfilPropioTest` fallan: falta preparar los parámetros de contraseña y se conserva una expectativa anterior de complejidad. El servicio devuelve 503 por PASSMIN ausente antes de lo que las pruebas esperan validar. No concluyo que el cambio de contraseña real esté roto por estos resultados.                                               | Alejandro, ADM24; coordinación Fabian, ADM6                                               |
| H06 | Resuelto para Jose; pendiente para Fabian | Las doce pruebas fallidas de sesión/Notas ya pasan al preparar la auditoría de interfaz y acotar la expiración. Los seis casos administrativos de Fabian conservan el resultado anterior; no los repetí en este seguimiento. | Jose ADM2/ADM17: resuelto; Fabian según historia: pendiente |
| H07 | Alta para presentación | El indicador de facturas depende de un permiso no incluido en la inicialización acordada. La evidencia de Fabian usa una asignación adicional; el ejecutor `adm3_indicadores.mjs` conserva esa asignación si termina correctamente. Una instalación basada solo en el script inicial puede mostrar «No disponible».                                                   | Fabian, ADM3/ADM5; coordinación Jose, GTW2                                                |
| H08 | Parcialmente verificado | El recorrido de Jose utilizó las bases compartidas y confirmó permisos y operaciones reales. La evidencia alternativa local de Ramses sigue requiriendo una guía común y aceptación específica de sus mantenimientos. | Cada propietario según historia |
| H09 | Alta para entrega       | No localicé un paquete documental actualizado del segundo avance que reúna análisis/diseño, diagramas, conclusiones, recomendaciones y bibliografía. Los índices conservan enlaces ausentes y planificación conjunta.                                                                                                                                                  | Los cuatro integrantes                                                                     |
| H10 | Corregido y verificado | Notas devolvía 409 sin competencia porque JDBC todavía no había iniciado una transacción SQL al pedir el bloqueo. Materialicé la transacción con una lectura vacía y comprobé adquisición, retención y liberación con SQL real. | Jose, ADM17 |
| H11 | Media; pendiente | Dos rubros de igual nombre provocan 500 al incumplir la restricción de datos. El rollback conserva el desglose anterior; falta tratar ese caso como una validación o conflicto con un mensaje claro. | Jose, ADM17 |

El detalle, las ubicaciones y la condición para cerrar cada hallazgo quedan en el [documento de pendientes](pendientes_auditoria5.md). Las correcciones autorizadas de Jose están identificadas; el resto de hallazgos mantiene su estado de diagnóstico.

### Dependencias que afectan al cierre

La preparación de permisos condiciona el menú y los indicadores. Los parámetros de contraseña condicionan ADM24: si faltan, el rechazo del cambio es intencional y debe explicarse al presentar. Notas ya tiene evidencia real de bloqueo y concurrencia en el orden observado; queda la secuencia HTTP inversa. Períodos y correo conservan sus comprobaciones reales pendientes. La falta de una demostración no significa necesariamente falta de desarrollo, pero impide afirmar aceptación completa.

## Verificación ejecutada

Ejecuté las suites existentes de .NET y Java relacionadas con el corte, las pruebas Node de Jose y Ramses, compilación de las tres aplicaciones React, revisión estática del cliente y pruebas de navegador. Agregué un recorrido de diagnóstico estudiantil. En el seguimiento de Jose actualicé las pruebas al contrato real y añadí regresión SQL; conservé las comprobaciones originales y documenté los resultados anteriores.

La comprobación SQL/JPA examinó **22 servicios y 298 fuentes, sin infracciones detectadas** por sus reglas de acceso directo. Guardé su salida en esta auditoría sin sobrescribir la evidencia anterior. El validador de artefactos aceptó los trece JAR disponibles; no los vuelve equivalentes a una compilación o despliegue actualizado.

### Pruebas ejecutadas por integrante

El [registro actualizado por caso](evidencias5/resultados-conservados.md#resultados-actualizados) sustituye los resultados anteriores de Notas/contratos y navegador de Jose por las nuevas ejecuciones. Conservo su resultado previo de .NET y Node y el último resultado de los demás integrantes; no presento estos casos conservados como nuevas ejecuciones. El [registro inicial](evidencias5/resultados-conservados.md#resultados) mantiene el antecedente.

| Integrante | Pruebas disponibles en este corte | Estado |
| --- | --- | --- |
| Alejandro | Expediente 135/135; Prematrícula 79/79; perfil propio 1/4; recorrido de auditoría 10/13. **225 aprobadas y 6 fallidas de 231**. | Resultado anterior conservado; no repetí sus suites. |
| Fabian | Usuario, sin perfil propio, 10/10; interfaz administrativa 4/10. **14 aprobadas y 6 fallidas de 20**. | Resultado anterior conservado; seis escenarios de interfaz pendientes. |
| Jose | Autenticación 29/29, gateway 132/132 y Node 10/10 conservados; contratos 3/3, Notas 46/46, sesión/Notas en plantilla 27/27 y cliente aislado 6/6 repetidos y aprobados. **253 aprobadas de 253**. | Las doce pruebas se resolvieron; los dos ejecutores terminan sin errores generales. |
| Ramses | Cursos/períodos 19/19; Carrera 3/3; Profesor 4/4; Notificación 5/5; Node 14/14; interfaz principal 10/10 y aislada 6/6. **61 aprobadas de 61**. | Resultado anterior conservado; el cierre de su ejecutor aislado no se volvió a comprobar. |

El conjunto actualizado reúne **553 casos aprobados y 12 fallidos de 565 incluidos**. El total crece en uno por la nueva prueba SQL de Notas; las doce fallidas de Jose se sustituyen por su repetición aprobada, sin sumar ambos intentos.

Por separado, el [seguimiento real de Jose](../pruebas/jose-cierre-auditoria5/README.md) contiene **41 comprobaciones de servicios/SQL y confirmación del navegador, más cuatro de JWT y destinos inaccesibles: 45/45 aprobadas**. El detalle de navegador tiene **14/14 comprobaciones aprobadas** y ya está incluido en la confirmación global de ese recorrido; no lo sumo otra vez. Las credenciales permanecieron fuera de las evidencias y los datos temporales fueron retirados.

### Barras del resultado de pruebas

Calculo estas barras como **casos aprobados / casos incluidos en el corte por integrante**. Expresan los resultados disponibles, con su fecha y procedencia; no son cobertura, aportes de desarrollo ni una comparación de calidad entre integrantes.

| Integrante | Barra de pruebas | Aprobadas / incluidas | Resultado |
| --- | --- | ---: | ---: |
| Alejandro | `[###################-]` | 225/231 | 97,40% |
| Fabian | `[##############------]` | 14/20 | 70,00% |
| Jose | `[####################]` | 253/253 | 100,00% |
| Ramses | `[####################]` | 61/61 | 100,00% |

| Comprobación real adicional de Jose | Barra | Resultado |
| --- | --- | ---: |
| Servicios/SQL y escenarios de gateway | `[####################]` | 45/45 |
| Detalle de navegador incluido en el recorrido anterior | `[####################]` | 14/14 |

Las dos comprobaciones heredadas de grupos siguen fuera de estas barras. Las compilaciones, estilo y artefactos tampoco inflan el conteo. Los tiempos agotados de los primeros ejecutores quedan como antecedente; los de Jose se resolvieron y el del cliente aislado de Ramses sigue sin nueva comprobación.

### Evidencia documental y entregables

| Entregable                       | Resultado de mi revisión                                                                                                                                                        |
| -------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Requisitos y reparto             | PDF y criterios del equipo disponibles; veinte historias identificadas en este informe.                                                                                          |
| Análisis y diseño actualizado  | Hay planificación, contratos, un diagrama general y un documento maestro anterior. No acredité un paquete actualizado completo del corte con todos los apartados de pp. 21-22. |
| Scripts de datos                 | Disponibles. Aplicación, parámetros, asignaciones y aprobación deben quedar ligados al entorno que se presentará.                                                            |
| Código y guía de arranque | El entorno de Jose se levantó y sus servicios se probaron. Falta consolidar una guía reproducible para toda la entrega y sus parámetros/permisos. |
| Pruebas por criterio | Incorporé el seguimiento real y capturas de Jose. Sigue pendiente reunir la matriz completa por criterio y versión de las veinte historias. |
| Tablero y PR a la versión final | No verificados en el servicio remoto. No los declaro inexistentes.                                                                                                               |
| Enlaces de consulta              | Siguen ausentes `docs/desarrollo-java.md`, `docs/limpieza-proyecto.md`, `docs/segundo_avance/contexto-desarrollo.md` y `docs/segundo_avance/desarrollo-jose.md`.         |

## Barras de progreso por persona

Estas barras comparan **el desarrollo disponible con el compromiso de cada integrante**. Conservo la escala anterior: 0% sin desarrollo, 25% base, 50% componentes parciales, 75% implementación principal con pendientes y 100% desarrollo completo e integrado. Los valores intermedios son estimaciones razonadas; no son una nota ni una medición automática del PDF.

### Base del cálculo por historia

| Responsable | Estimación de sus historias                      | Fundamento                                                                                                                          |
| ----------- | ------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------- |
| Jose        | ADM2 95%; ADM17 95%; GTW1 95%; GTW2 95%           | Bloqueo corregido e integración real demostrada en los escenarios indicados; mantengo las condiciones de aceptación pendientes y la aclaración SRV7.                              |
| Ramses      | ADM7/ADM9/ADM10/ADM11/ADM12/ADM22: 90% cada una   | Mantenimientos disponibles; nueva evidencia parcial, sin aceptación completa del correo y las relaciones/concurrencia reales.      |
| Alejandro   | ADM1 90%; ADM24 90%; WEB1 95%; WEB2 95%; WEB4 75% | Reconozco responsive, auditoría, política, salida y confirmaciones; descuento faltantes concretos de navegación y prematrícula. |
| Fabian      | ADM3 95%; ADM4 95%; ADM5 95%; ADM6 90%; ADM8 95%  | Reconozco controles e indicador y la evidencia posterior; quedan preparación reproducible, parámetros y aceptación final.        |

Fórmula: **suma(puntos de historia × desarrollo estimado) / puntos asignados**.

| Integrante | Barra de aportes           | Estimación | Cálculo   |
| ---------- | -------------------------- | ----------: | ---------- |
| Jose       | `[###################-]` |         95% | 30,4 / 32  |
| Ramses     | `[##################--]` |         90% | 27 / 30    |
| Alejandro  | `[##################--]` |    90,8929% | 25,45 / 28 |
| Fabian     | `[###################-]` |       94,5% | 28,35 / 30 |

**Progreso ponderado del segundo avance: `[###################-]` = 92,6667%, aproximadamente 93%.** La cuenta es `(30,4 + 27 + 25,45 + 28,35) / 120`. No añadí puntos por las pruebas ejecutadas ni por volver a utilizar la plantilla o los servicios.

## Avance del proyecto

| Indicador                                                            | Resultado     | Interpretación                                                                                                                                                 |
| -------------------------------------------------------------------- | ------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Historias revisadas del corte                                        | 20/20         | Revisadas documentalmente y en sus fuentes; no significa veinte historias aceptadas.                                                                            |
| Desarrollo estimado                                                  | 92,6667%      | Estimación ponderada de los aportes actuales.                                                                                                                  |
| Cierre integral que puedo acreditar | 0/20 | La evidencia técnica de Jose se amplió; aún falta reunir la aceptación completa por criterio y resolver las condiciones documentadas. No equivale a ausencia de desarrollo. |
| Plantilla compartida                                                 | Disponible    | Administración y estudiante utilizan la misma base visual y de navegación.                                                                                    |
| Recorridos del roadmap demostrados con servicios reales | 2/6 | Login/sesión de los tres perfiles y dashboard del profesor → Notas → regreso. Los otros cuatro recorridos no se acreditan con estas pruebas. |
| Cumplimiento del 100%                                                | No acreditado | Hay faltantes funcionales, de preparación de pruebas y de entrega documental.                                                                                  |

## Prioridades para continuar

| Orden | Trabajo pendiente                                                                                              | Responsable                                                              |
| ----: | -------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------ |
|     1 | Impedir prematrículas duplicadas y consumir paginación configurable.                                         | Alejandro, WEB4                                                          |
|     2 | Ajustar ruta activa y acceso al nombre/rol en móvil.                                                          | Alejandro, ADM1                                                          |
| 3 | Actualizar las pruebas pendientes de contraseña y administración; revisar los fallos restantes. | Alejandro y Fabian según historia |
|     4 | Acordar un entorno de presentación, parámetros y permisos reproducibles, incluido el indicador de facturas.  | Fabian, Ramses y Jose según historia; Alejandro valida sus consumidores |
| 5 | Completar los otros cuatro recorridos, la secuencia inversa de concurrencia de Notas, módulo inactivo y correo reales. | Cada responsable de historia |
|     6 | Consolidar evidencia por criterio, documentación, guía y comprobantes de entrega.                            | Los cuatro integrantes                                                   |

## Estado al cierre de la revisión

Dejo el segundo avance **en verificación, con pendientes que impiden presentarlo como terminado al 100%**. Reconozco las correcciones de Jose, sus pruebas aprobadas y los recorridos reales. Los demás hallazgos conservan el seguimiento y las evidencias de su último corte. Como equipo mantengo la decisión de cerrar los faltantes de cada responsable, reunir evidencia del mismo entorno y completar la documentación antes de afirmar que la entrega está lista.
