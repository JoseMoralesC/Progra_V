# Auditoría de avance 4 - Proyecto Nuevo Avatar V2

Revisión: cuarta auditoría general del **segundo avance**. El número 4 identifica la revisión del informe.
Versión examinada: `4eeb3362dc1071fb4fb9bb046a9ea0e195724460`, más el árbol de trabajo local. No presento los cambios locales como publicados ni incorporados en ese commit.
Antecedente: [tercera auditoría](auditoria_avance3.md).
Base de requisitos: [PDF V2, 22 páginas](<../requerimientos/Proyecto%20Nuevo%20Avatar%20V2%20%281%29.pdf>), [PDF V1 como antecedente de servicios](<../../primer_avance/requerimientos/Proyecto%20Nuevo%20Avatar%20V1.pdf>), [roadmap A2](<../roadmaps/roadmap%202.md>), [backlog](../planificacion/backlog.md), [criterios indivisibles](../planificacion/subhistorias.md), [historias estudiantiles](../requerimientos/web-estudiantes.md) y [contratos](../planificacion/contratos.md).

Actualizo el levantamiento con las fuentes y ejecuciones del corte actual. Mantengo responsables y formato de seguimiento; describo el estado disponible y los faltantes que todavía requieren atención. El detalle de continuidad queda en [pendientes_auditoria4.md](pendientes_auditoria4.md). Los resultados y sus límites están en [evidencias4](evidencias4/README.md).

## Resumen

Encuentro disponibles el ingreso por gateway, la restauración de sesión, renovación y salida compartidas, Notas dentro de la plantilla y las seis pantallas de Ramses con sus rutas y permisos. Los componentes administrativos y estudiantiles conservan su interfaz de sesión; no se modificaron los formularios de negocio de Alejandro y Fabian en esta revisión del corte.

Los mantenimientos disponen de paginación configurable, mensajes y confirmaciones. Profesor valida formato telefónico y duplicados equivalentes; Periodo contempla año/número y comprobación transaccional en su repositorio; las bitácoras incluyen estados de los cambios. Correo distingue el envío realizado del fallo de historial para evitar que un error de registro induzca a reenviar.

Persisten faltantes en la aceptación integral: las reglas y confirmaciones propias del estudiante, la política parametrizable de contraseña, responsive y registro de navegación, algunos controles administrativos, el indicador de facturas y la evidencia con infraestructura real. Por ello **no declaro un cumplimiento del 100% ni cierres integrales de las veinte historias**.

La estimación de desarrollo disponible del segundo avance es **84,8333%, aproximadamente 85%**. Las barras de aportes se calculan por historia y complejidad. Las barras de pruebas se presentan aparte y no aumentan esas estimaciones.

## Alcance auditado

Mantengo exclusivamente las veinte historias del segundo avance y sus 120 puntos:

| Responsable | HU comprometidas                       | Puntos |
| ----------- | -------------------------------------- | -----: |
| Jose        | ADM2, ADM17, GTW1, GTW2                |     32 |
| Ramses      | ADM7, ADM9, ADM10, ADM11, ADM12, ADM22 |     30 |
| Alejandro   | ADM1, ADM24, WEB1, WEB2, WEB4          |     28 |
| Fabian      | ADM3, ADM4, ADM5, ADM6, ADM8           |     30 |
| Total       | 20                                     |    120 |

Revisé documentación, PDF, inventario general, fuentes del corte, configuración, rutas, pantallas, autorización, persistencia, bitácoras y pruebas. La comprobación general de límites de datos recorre los servicios heredados como infraestructura de apoyo; no acredita funcionalidades adicionales a este compromiso. Las matrículas, grupos y facturas se consideran solo datos de apoyo a notas e indicadores exigidos.

### Acuerdos del equipo que mantengo

Uso el PDF como requisito oficial: una decisión técnica del equipo no elimina sus criterios. Para estudiantes mantengo WEB1/WEB2/WEB4, redactadas por el equipo ante la omisión comunicada del profesor, y reutilizo plantilla, ingreso y perfil para conservar semejanza con la administración.

Reconozco la autoría de Alejandro en la plantilla UI/UX. Cada HU mantiene un único responsable y no sumo otra vez componentes comunes. La numeración antigua de carpetas Java no cambia el reparto vigente. Las barras representan desarrollo disponible frente al compromiso individual, no cantidad de pruebas ni calificación.

### Mi criterio de verificación

Distingo implementación inspeccionada, comportamiento ejecutado localmente y aceptación integral. Las pruebas de navegador usan API interceptada; las HTTP del gateway usan Kestrel/YARP con destinos controlados; Java combina H2 y dobles según la suite. Los casos no se presentan como evidencia de SQL Server o SMTP reales.

No apliqué migraciones ni asignaciones a las bases compartidas, no envié correos reales y no comprobé tablero ni aprobación remota de PR. La falta de esta evidencia no significa que el equipo no haya realizado esas actividades: significa que no las acredito en este informe.

## Criterios generales del PDF y roadmap

| Criterio / fuente                  | Estado actual                       | Observación de cierre                                                                                                                                           |
| ---------------------------------- | ----------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Reparto A2                         | Conservado                          | Veinte HU, 120 puntos, responsables sin cambios.                                                                                                                 |
| Web en React, p. 21                | Disponible                          | Las tres aplicaciones React construyen. La entrada principal contiene las rutas del corte; las aplicaciones aisladas sirven a comprobaciones complementarias.    |
| Gateway único, pp. 15-16          | Disponible en el cliente principal  | Ingreso, identidad, menú, refresh y operaciones usan `/gateway`; no hay proxy directo del portal a Usuario/Rol/Auth. Falta recorrido con propietarios reales. |
| Login y sesión, p. 5              | Verificados localmente              | Formato/dominio, mensaje requerido, restauración, renovación, rechazo, salida y respuestas tardías. Recordar sesión sigue siendo opcional en el PDF.         |
| Plantilla y navegación, pp. 4-5   | Parcial                             | Destinos A2 de Notas y mantenimientos disponibles; menú usa nombre/orden del backend. Responsive y bitácora de rutas/errores UI pendientes.                    |
| Autorización                      | Verificada localmente en gateway    | Permisos por perfil/módulo y consultas auxiliares acotadas. Algunos controles de páginas administrativas de Fabian siguen pendientes por URL directa.          |
| Mantenimientos, pp. 17-19          | Disponible en las áreas revisadas  | Paginación TAMPAG, máximo quince, regreso y confirmaciones. Falta aceptación completa por criterio con servicios reales.                                      |
| Notas, p. 11                       | Integrada localmente                | Componente, desglose, calificaciones, permisos y bloqueo por grupo. Concurrencia/rollback/timeout de SQL Server sin acreditar.                                   |
| Perfil, p. 13                      | Parcial                             | Perfil y BCrypt disponibles; política parametrizable, cierre de sesiones del cliente y JSON de actualización pendientes.                                       |
| Portal estudiantil                 | Parcial                             | Datos propios disponibles; quedan reglas de prematrícula y confirmaciones, entre otros puntos de Alejandro.                                                     |
| Bitácoras, pp. 16-17              | Implementación ampliada disponible | Estados en los mantenimientos revisados; verificación real pendiente. Perfil y navegación conservan sus faltantes específicos.                                |
| Datos, pp. 20-21                   | Scripts disponibles                 | Aplicación y aprobación de migraciones/asignaciones no acreditadas. No requiero nuevas tablas para el bloqueo de Periodo o Notas de este corte.                |
| Evidencia y entregables, pp. 21-22 | Parcial                             | Faltan recorridos reales, capturas por criterio, paquete documental y comprobación de entrega remota.                                                           |

## Comparación contra auditorías anteriores

Mantengo las auditorías anteriores como antecedentes y confronto sus conclusiones con las fuentes actuales. Las pantallas de Fabian y el portal de Alejandro ya no se describen como ausentes. Las rutas de Notas y los mantenimientos están disponibles en la entrada principal. La existencia de código y pruebas locales no sustituye la aceptación de los criterios pendientes.

| Área                               | Dictamen del corte actual                                                                                                                                        |
| ----------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Jose                                | Contratos, sesión, gateway y Notas disponibles; verificación local favorable. Integración real y evidencia final pendientes.                                  |
| Ramses                              | Seis mantenimientos y adaptaciones disponibles; verificación local favorable. Propietarios, parámetros, persistencia, concurrencia y correo reales pendientes. |
| Alejandro                           | Plantilla y flujos propios disponibles parcialmente; conserva los faltantes de sus cinco historias.                                                              |
| Fabian                              | Cinco pantallas disponibles; conserva controles, indicadores y aceptación real pendientes.                                                                      |
| Historias acreditadas integralmente | 0/20. No equivale a ausencia de trabajo.                                                                                                                         |

## Auditoría por persona y trazabilidad por HU

### Persona 1 - Jose

Responsabilidad: **4 HU, 32 puntos**. Mantengo la estimación en 95% por historia: la implementación disponible no acredita por sí sola el cierre con el entorno real y los consumidores pendientes.

| HU / fuente      | Estado disponible                                                                                                                                                                   | Verificación pendiente                                                                                            | Desarrollo estimado |
| ---------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------ | ------------------: |
| ADM2 / p. 5      | Formulario institucional, mensaje requerido, gateway, identidad/menú, restauración, refresh compartido y salida inmediata. La interfaz useAuth se conserva para los consumidores. | Tres perfiles y registro de login/logout con servicios reales; continuidad con los flujos completos de ambas webs. |                 95% |
| ADM17 / p. 11    | Notas montada en la plantilla, regreso, grupos propios, desglose/calificaciones, estados y bloqueo transaccional por grupo.                                                         | SQL Server con dos conexiones/instancias, errores y bitácora persistida; recorrido final desde ADM3.              |                 95% |
| GTW1 / pp. 15-16 | YARP, rutas REST, perfiles/operaciones propias y conservación de estados HTTP.                                                                                                     | Destinos/configuración reales y recorrido de los consumidores finales.                                            |                 95% |
| GTW2 / p. 16     | Validación, sesión, módulo activo, rechazo de identidad inyectada y permisos auxiliares de lectura limitados a sus dependencias.                                                 | Revocación e inactividad con datos reales; aclaración de la referencia SRV7 del documento.                       |                 95% |

### Persona 2 - Ramses

Responsabilidad: **6 HU, 30 puntos**. Mantengo 90% de desarrollo estimado en cada historia; la comprobación real y la aceptación completa por criterio continúan abiertas.

| HU / fuente   | Estado disponible                                                                                                                                               | Verificación pendiente                                                                                                            | Desarrollo estimado |
| ------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------- | ------------------: |
| ADM7 / p. 8   | CRUD, estado/orden, asociación de roles, menú propio, auditoría de consultas y paginación. Formulario y registro guardado disponibles en la entrada común. | Persistencia, revocación, inactividad y registros reales de las operaciones.                                                      |                 90% |
| ADM9 / p. 8   | Institución/director, búsqueda, bloqueo por cursos, paginación y estados de cambios. Consultas auxiliares no conceden escritura de instituciones/profesores. | Relaciones y búsqueda con propietarios reales; capturas por criterio.                                                             |                 90% |
| ADM10 / p. 8  | CRUD, filtros carrera/nivel, validaciones, relaciones, paginación y estados JSON.                                                                              | Registro real, restricciones de eliminación y recorrido completo.                                                                 |                 90% |
| ADM11 / p. 9  | Edad, dominio por parámetro, formato local, duplicados equivalentes, búsqueda, orden, paginación y estados de cambios.                                       | DOMPROF, grupos activos y persistencia con propietarios reales.                                                                    |                 90% |
| ADM12 / p. 9  | Fechas y estados derivados, año/número, exclusión del propio registro y transacción serializable con bloqueos en el repositorio.                            | Dos conexiones/instancias SQL reales, límites, rollback y errores. La prueba concurrente en memoria solo demuestra una instancia. |                 90% |
| ADM22 / p. 12 | Destinatarios, HTML sanitizado, confirmación, parámetros, historial paginado y distinción de envío sin historial.                                           | SMTP y SQL reales, incluidos fallos simultáneos y presentación en ADM3.                                                          |                 90% |

### Persona 3 - Alejandro

Responsabilidad: **5 HU, 28 puntos**. Mantengo la autoría de la plantilla y las estimaciones de sus flujos propios; las dependencias compartidas no generan puntos adicionales.

| HU / fuente         | Estado disponible                                                                   | Faltante vigente                                                                                                                                            | Desarrollo estimado |
| ------------------- | ----------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------: |
| ADM1 / pp. 4-5      | Layout común, encabezado, menú, identidad, notificaciones y breadcrumbs.          | Responsive, registro de rutas/errores y cierre visual de las dos experiencias. En 390 px el corte previo mostró desbordamiento; el layout no se modificó. |                 70% |
| ADM24 / p. 13       | Lectura de perfil, nombre editable y contraseña con BCrypt.                        | Política parametrizable, cierre de sesiones del cliente y estados JSON del cambio de nombre.                                                               |                 65% |
| WEB1 / equipo C1-C6 | Vínculo del expediente, cantidad de solicitudes, errores y accesos propios.        | Recorrido real, registro de consulta y evidencia de sus estados.                                                                                            |                 85% |
| WEB2 / equipo C1-C6 | Expediente propio, campos protegidos y edición acotada con selectores encadenados. | Confirmar antes de PUT y demostrar correspondencia territorial/propiedad con servicios reales.                                                              |                 80% |
| WEB4 / equipo C1-C6 | CRUD propio y oferta de primer nivel/futura.                                        | Comparar curso/carrera en servidor, evitar duplicados entre solicitudes, confirmar DELETE y paginar.                                                        |                 60% |

### Persona 4 - Fabian

Responsabilidad: **5 HU, 30 puntos**. Sus formularios de negocio se conservan. La sesión común mantiene el contrato que consumen esas páginas.

| HU / fuente    | Estado disponible                                                                                         | Faltante vigente                                                                                 | Desarrollo estimado |
| -------------- | --------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------ | ------------------: |
| ADM3 / pp. 5-6 | Dashboard, grupos, destino de Notas, actividad, soporte y perfil.                                         | Indicador de facturas con consulta acotada; datos reales y evidencia completa de ambos perfiles. |                 80% |
| ADM4 / pp. 6-7 | JPA, filtros, orden/paginación, altas/edición, detalle y estados de auditoría.                         | Controles por perfil/permiso en URL, migración 005 y aceptación real.                          |                 80% |
| ADM5 / p. 7    | Roles y matriz persistible, validación de duplicados y migración 004.                                   | Revocación real y actualización del menú tras guardar sin depender de remontar la página.    |                 80% |
| ADM6 / pp. 7-8 | CRUD, límites, paginación y registro anterior/actual.                                                   | Pruebas reales y contrato de parámetros de contraseña con ADM24.                               |                 85% |
| ADM8 / p. 8    | Búsqueda, formularios, confirmación, paginación, auditoría y comprobación compartida de referencias. | Bloqueo de página sin permiso y demostración de referencias/persistencia reales.               |                 85% |

## Hallazgos y diferencias

Actualizo los hallazgos según el comportamiento disponible. Conservo los IDs para relacionarlos con el primer levantamiento, sin presentar una lista antigua de ausencias como si siguiera vigente.

| Referencia             | Estado del corte                                         | Observación / responsable                                                                                                                                                              |
| ---------------------- | -------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| A2-4-01 / 02 / 03 / 05 | Implementación disponible; aceptación real pendiente   | Sesión por gateway, Notas y seis mantenimientos montados; formato/mensaje de login y restauración verificados localmente. Jose/Ramses según HU; consumidores coordinan integración. |
| A2-4-04 / 09           | Parcial                                                  | Menú usa nombre/orden y distingue fallo; faltan responsive y registro de rutas/errores UI. Alejandro ADM1.                                                                             |
| A2-4-06                | Abierto                                                  | Expediente guarda y prematrícula cancela sin confirmación previa. Alejandro WEB2/WEB4.                                                                                                |
| A2-4-07                | Abierto                                                  | Prematrícula no compara curso/carrera ni impide duplicados entre solicitudes en servidor. Alejandro WEB4.                                                                              |
| A2-4-08                | Abierto                                                  | Política fija, cierre de sesiones del cliente y JSON de perfil pendientes. Alejandro ADM24.                                                                                            |
| A2-4-10                | Abierto en páginas administrativas                      | Ocultar menú no bloquea por URL los controles del perfil o módulo equivocado. El gateway conserva su autorización. Fabian y coordinación con ADM1.                                  |
| A2-4-11                | Parcial                                                  | Mantenimientos de Ramses paginados; prematrícula propia conserva lista completa. Alejandro WEB4 mantiene ese faltante.                                                                 |
| A2-4-12 / 13 / 14 / 15 | Implementación disponible; comprobación real pendiente | Teléfonos, estados JSON, regla/transacción de periodos y resultados de correo presentes. Ramses por HU.                                                                               |
| A2-4-16                | Parcial                                                  | Suites de Jose y Ramses alineadas con las rutas actuales; conserva el fallo administrativo que identifique la ejecución final. No elimino criterios para obtener aprobación.          |
| A2-4-17                | Abierto                                                  | Migraciones/asignaciones, SQL/SMTP, seis recorridos y evidencia final no acreditados. Cada responsable de HU.                                                                           |
| A2-4-18                | Abierto                                                  | Enlaces ausentes y paquete documental A2 sin consolidar. Equipo.                                                                                                                        |

### Dependencias que afectan al cierre

Como equipo mantengo una sesión común y un gateway. La entrada compartida ya permite verificar localmente Notas y los mantenimientos. Para cerrar las historias faltan las experiencias completas de Alejandro y Fabian y una demostración con datos reales. No redistribuyo sus pendientes ni los doy por resueltos desde las pruebas de otra persona.

La referencia SRV7 del PDF V2 no tiene una correspondencia inequívoca en el V1 consultado. La dejo como aclaración pendiente; no abro una excepción de seguridad por suposición.

## Verificación ejecutada

El [registro actual de ejecuciones](evidencias4/resultados-conservados.md#resultados-actuales) contiene el balance definitivo por caso e integrante. Conservo una ejecución final por caso; los resultados anteriores y las repeticiones no se suman.

| Conjunto                            |                                     Resultado actual | Alcance y límite                                                                                  |
| ----------------------------------- | ---------------------------------------------------: | -------------------------------------------------------------------------------------------------- |
| Java del corte e Integración       |                                        287 aprobadas | Maven package SUCCESS; H2/dobles. Institución compila sin suite propia localizada.                |
| Autenticación .NET                 |                                      29/29 aprobadas | Tokens, sesión y controladores; dependencias controladas.                                         |
| Gateway .NET                        |                                    132/132 aprobadas | Routing, autorización, dashboard y dependencias auxiliares con destinos simulados.                |
| Curso/Periodo .NET                  |                                      19/19 aprobadas | Estados JSON y reglas, incluida concurrencia de una instancia en memoria; sin certificar SQL real. |
| Node Jose / Ramses                  |                              10/10 y 14/14 aprobadas | Contratos de sesión y validaciones/paginación de clientes.                                       |
| Navegador aislado Jose / Ramses     |                                  6/6 y 6/6 aprobadas | Comprobaciones complementarias con API interceptada.                                               |
| Navegador del portal principal      |                             45 aprobadas, 2 fallidas | Sesión/Notas, mantenimientos y regresión administrativa; API interceptada.                       |
| Builds React                        |                                        3/3 aprobados | Entrada principal y aplicaciones aisladas.                                                         |
| Lint principal                      |              Sin errores, cinco advertencias previas | InicioEstudiante, Prematrícula y NotificationContext; fuera de los formularios intervenidos.      |
| Artefactos Java                     |                                      8/8 verificados | JAR y contrato compartido revisados; no equivale a despliegue.                                     |
| Límites SQL/JPA                    | 22 servicios, 297 fuentes, 0 infracciones detectadas | Analizador estático existente con salida separada.                                                |
| Recorridos SQL Server / SMTP reales |                                        No ejecutados | No se aplican migraciones ni se envían correos reales.                                            |

**Balance final: 548 aprobadas y 2 fallidas de 550 casos.** Los fallos corresponden al flujo de edición/búsqueda y al bloqueo sin permiso de Instituciones (ADM8, Fabian). No se modifica ese formulario ni se declara fallida su persistencia real a partir de un escenario simulado.

Las pruebas locales comprueban el comportamiento indicado, no toda la infraestructura. Las de SQL de Notas usan dobles; la concurrencia de Periodo en memoria demuestra una instancia. Falta acreditar el bloqueo real entre conexiones y la persistencia de bitácoras con SQL Server. Los casos de correo usan transporte simulado y no envían correos.

### Pruebas ejecutadas por integrante

Asigno por la HU vigente que respalda cada caso, no por el nombre antiguo de la carpeta ni por quien ejecutó la revisión. En Notas, 42 corresponden a ADM17 de Jose y tres de resumen del profesor a ADM3 de Fabian. Los casos de dependencias de módulos de Ramses se identifican por el módulo probado; las pruebas compartidas se cuentan una sola vez.

| Integrante | Pruebas ejecutadas                                                                                                       | Aprobadas | Fallidas | Total |
| ---------- | ------------------------------------------------------------------------------------------------------------------------ | --------: | -------: | ----: |
| Jose       | Notas 42; autenticación/gateway 70; Node 10; navegador aislado 6; sesión/Notas del portal 27                           |       155 |        0 |   155 |
| Ramses     | Carrera 3; Profesor 4; Notificación 5; académico/menú/permisos auxiliares 36; Node 14; navegador aislado 6; portal 10 |        78 |        0 |    78 |
| Alejandro  | Perfil propio 4; Expediente 135; Prematrícula 78                                                                        |       217 |        0 |   217 |
| Fabian     | Usuario/seguridad/bitácora 10; resumen del profesor 3; dashboard/permisos 74; portal administrativo 10                  |        95 |        2 |    97 |
| Compartida | Biblioteca Integración 3                                                                                                |         3 |        0 |     3 |
| Total      | Una ejecución final por caso                                                                                            |       548 |        2 |   550 |

### Barras del resultado de pruebas

La fórmula es **aprobadas / ejecutadas**. Estas barras no son porcentaje de criterios del PDF cubiertos, historias terminadas, aportes ni nota. Un 100% de casos ejecutados no acredita el 100% de una historia.

| Integrante | Barra de pruebas           |         Resultado |
| ---------- | -------------------------- | ----------------: |
| Jose       | `[####################]` | 155/155 = 100,00% |
| Ramses     | `[####################]` |   78/78 = 100,00% |
| Alejandro  | `[####################]` | 217/217 = 100,00% |
| Fabian     | `[####################]` |    95/97 = 97,94% |
| Compartida | `[####################]` |     3/3 = 100,00% |
| Total      | `[####################]` |  548/550 = 99,64% |

### Evidencia documental y entregables

| Entregable             | Estado actual                                                                                                                                                              |
| ---------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Análisis/diseño      | Planificación, contratos y antecedentes disponibles; falta paquete consolidado A2 con diagramas de BD/casos de uso/clases, conclusiones, recomendaciones y bibliografía. |
| Scripts                | Migraciones y preparación manual disponibles; aplicación y aprobación reales no comprobadas.                                                                            |
| Código y artefactos   | Versión base identificada y cambios locales; compilación/empaquetado disponibles. No acredito publicación o aprobación remota.                                         |
| Evidencia por criterio | Suites y fichas aportan comprobación local; faltan recorridos/capturas finales de las veinte HU.                                                                          |
| Guía de ejecución    | La guía React describe la entrada común. Falta consolidar el paquete y resolver enlaces ausentes de los índices anteriores.                                             |
| Tablero / PR final     | Estado externo no verificado.                                                                                                                                              |

## Barras de progreso por persona

Estas barras comparan **el aporte de desarrollo disponible con el compromiso de cada integrante**. No dependen del número de pruebas. Mantengo la escala anterior: 0% sin desarrollo, 25% base, 50% componentes parciales, 75% implementación principal con pendientes y 100% desarrollo completo e integrado. Los valores intermedios son estimaciones de seguimiento, no una nota ni un conteo exacto de criterios.

### Base del cálculo por historia

| Responsable | Estimación de sus HU                             | Fundamento del corte                                                                                                      |
| ----------- | ------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------- |
| Jose        | ADM2 95%; ADM17 95%; GTW1 95%; GTW2 95%           | Implementación y recorridos locales disponibles; faltan demostración real y aceptación con los consumidores completos. |
| Ramses      | ADM7/ADM9/ADM10/ADM11/ADM12/ADM22: 90% cada una   | Mantenimientos y reglas disponibles; falta cerrar cada criterio con propietarios, datos, auditoría y correo reales.      |
| Alejandro   | ADM1 70%; ADM24 65%; WEB1 85%; WEB2 80%; WEB4 60% | Conservo sus estimaciones de desarrollo propio y sus faltantes. No vuelvo a sumar dependencias comunes.                   |
| Fabian      | ADM3 80%; ADM4 80%; ADM5 80%; ADM6 85%; ADM8 85%  | Pantallas disponibles con controles, indicadores y aceptación pendientes.                                                |

Fórmula: **suma(puntos de HU × desarrollo estimado) / puntos asignados**.

| Integrante | Barra de aportes           | Estimación | Cálculo           |
| ---------- | -------------------------- | ----------: | ------------------ |
| Jose       | `[###################-]` |         95% | 30,4/32            |
| Ramses     | `[##################--]` |         90% | 27/30              |
| Alejandro  | `[##############------]` |         72% | 20,1/28 = 71,7857% |
| Fabian     | `[################----]` |         81% | 24,3/30            |

**Progreso ponderado A2: `[#################---]` = 84,8333%, aproximadamente 85%.** La cuenta es `(30,4 + 27 + 20,1 + 24,3) / 120`. Las unidades equivalentes expresan seguimiento del trabajo, no puntos académicos obtenidos.

## Avance del proyecto

| Indicador                                                       |             Resultado | Interpretación                                                       |
| --------------------------------------------------------------- | --------------------: | --------------------------------------------------------------------- |
| Historias del corte revisadas                                   |                 20/20 | Mismo reparto y compromiso.                                           |
| Desarrollo estimado                                             |              84,8333% | Se basa en implementación disponible por HU.                         |
| Cierre integral acreditado                                      |                  0/20 | Faltan criterios/evidencia y aceptación real.                        |
| Entrada común para Notas y mantenimientos                      | Disponible localmente | Con sesión y permisos compartidos; no acredita infraestructura real. |
| Recorridos finales del roadmap acreditados con servicios reales | 0/6 en esta revisión | No sumo recorridos simulados como aceptación final.                  |
| Cumplimiento del 100%                                           |         No acreditado | Pendientes propios y dependencias de integración siguen abiertos.    |

## Prioridades para continuar

| Orden | Trabajo pendiente                                                                          | Responsable                                             |
| ----: | ------------------------------------------------------------------------------------------ | ------------------------------------------------------- |
|     1 | Completar responsive y registro de navegación/errores de la plantilla.                    | Alejandro ADM1                                          |
|     2 | Cerrar confirmaciones y reglas de los trámites propios; política y sesiones del perfil.  | Alejandro por HU; coordinación Fabian para parámetros |
|     3 | Cerrar permisos de página, indicador de facturas y actualización del menú tras cambios. | Fabian por HU; coordinación Jose en contratos          |
|     4 | Verificar notas/periodos concurrentes, bitácoras, parámetros y correo con entorno real.  | Jose y Ramses según HU; equipo prepara el escenario    |
|     5 | Demostrar los seis recorridos y completar capturas/documentación por criterio.            | Los cuatro integrantes                                  |

## Estado al cierre de la revisión

Mantengo el segundo avance **en verificación parcial**, con implementación local disponible en las áreas de Jose y Ramses y faltantes concretos en la integración completa y en las historias de Alejandro y Fabian. No acredito un 100% a partir de compilaciones o pruebas simuladas. La estimación de aportes y el resultado de pruebas conservan dimensiones distintas, con responsables y límites visibles.
