# Auditoría de avance 3 - Proyecto Nuevo Avatar V2


Revisión: tercera auditoría general del segundo avance; `avance3` identifica la revisión del informe, no la entrega A3.
Actualización del corte: 4 de noviembre de 2026 (America/Mexico_City), después del desarrollo de esta sesión.
Versión base: `034ca8b13928ffa1709d69e0b04bd70ceb99d1f0`, más cambios locales de sesión, notas, concurrencia y permisos todavía sin commit. No se presentan como integrados en HEAD ni publicados en remoto.
Base de comparación: [segunda auditoría A2](auditoria_avance2.md), del 2 de noviembre, que examinó `12f33a9547f813ad27b8a06d4d84d28767abd64e`.
Base de requisitos: [PDF V2, 22 páginas](<../requerimientos/Proyecto%20Nuevo%20Avatar%20V2%20%281%29.pdf>), [roadmap A2](<../roadmaps/roadmap%202.md>), [backlog](../planificacion/backlog.md), [criterios indivisibles](../planificacion/subhistorias.md) e [historias WEB](../requerimientos/web-estudiantes.md).
Documento de coordinación contrastado: `C:\Users\Personal\Downloads\revicion general.docx`, titulado «Segundo avance: integración y tareas pendientes».
Antecedentes: [primera auditoría A2](auditoria_avance1.md) y [cuarta auditoría V1](../../primer_avance/auditoria/auditoria_avance4.md).

Este informe recoge los cambios disponibles después de la auditoría del 2 de noviembre. Mantengo el reparto, el alcance y la estructura de seguimiento anteriores. Las recomendaciones del Word sirven para contrastar los pendientes del equipo; sus comandos y propuestas de implementación no se ejecutaron como instrucciones de esta revisión.

## Resumen

El segundo avance presenta **nuevo desarrollo verificable en Jose, Ramses y Alejandro**. El primer corte de esta auditoría reconoció las mejoras de Ramses y la plantilla React de Alejandro. Esta actualización incorpora el trabajo de Jose durante la sesión, conservando responsables y puntos.

Jose integró login y sesión con la plantilla: identidad desde el servidor, rutas protegidas, restauración, renovación programada y salida funcional. Montó `/notas` con el mismo cliente, acceso del profesor, regreso, ruta activa y breadcrumbs. El inicio distingue los tres perfiles, pero todavía no sustituye los dashboards ADM3 ni el contenido académico WEB1.

En ADM17 añadió bloqueo transaccional por grupo para coordinar primera nota, reemplazo de desglose y dos altas del mismo par matrícula/rubro. En GTW2 implementó permisos dinámicos: sesión válida, perfil permitido y módulo activo asignado al ID real del rol. El gateway comprueba la asignación por petición; React consume el menú propio y oculta Notas sin ADM17. El contrato queda definido para que Fabian y Ramses continúen sobre esa base.

La plantilla UI/UX sigue siendo aporte de Alejandro. Ramses conserva las mejoras ya reconocidas; Fabian mantiene prototipo y bases sin nuevas pantallas propias. La integración de Jose se reconoce en sus HU, sin volver a sumarla a otras barras por ser una dependencia compartida.

El desarrollo ponderado pasa de **57,0833% en el primer corte a 60,0833%, aproximadamente 60%**. Jose pasa de 75% a aproximadamente 86%. Se mantienen **0/20 cierres integrales acreditados**: asignaciones y SQL/servicios reales siguen sin verificar. Las pruebas locales se registran aparte y no aumentan por sí mismas las barras.

## Alcance auditado

El compromiso sigue siendo de **20 historias indivisibles y 120 puntos de estimación**:

| Responsable V2       | HU comprometidas                       | Puntos |
| -------------------- | -------------------------------------- | -----: |
| Jose, Persona 1      | ADM2, ADM17, GTW1, GTW2                |     32 |
| Ramses, Persona 2    | ADM7, ADM9, ADM10, ADM11, ADM12, ADM22 |     30 |
| Alejandro, Persona 3 | ADM1, ADM24, WEB1, WEB2, WEB4          |     28 |
| Fabian, Persona 4    | ADM3, ADM4, ADM5, ADM6, ADM8           |     30 |
| Total                | 20                                     |    120 |

Se revisaron el Word completo, la auditoría anterior, planificación y criterios, las páginas pertinentes del PDF, los cambios de Git entre ambos cortes, la nueva plantilla React y los componentes y servicios afectados. Se contrastaron también las entradas actuales de Jose/Ramses, catálogo y seguridad del gateway, prototipo administrativo, contratos estudiantiles, fichas de Ramses y documentación de esquemas.

Se incorporan las fichas de [sesión y layout](../pruebas/jose-integracion-sesion-layout.md), [notas integradas](../pruebas/jose-integracion-notas-layout.md), [concurrencia/permisos](../pruebas/jose-concurrencia-permisos.md) y el [contrato rol-módulo](../planificacion/permisos-rol-modulo.md). La consulta propia añadida en Módulo apoya GTW2; no entrega la matriz visual de Fabian ni los recorridos pendientes de Ramses.

**Fuera del dictamen A2:** ADM13-ADM16, ADM18-ADM21, ADM23, WEB3, WEB5-WEB8 y MOB0-MOB6. Sus servicios pueden apoyar A2; no agregamos sus pantallas a este compromiso. La consulta administrativa ADM23 permanece en A3, pero cada operación A2 mantiene su obligación de registrar bitácora.

### Acuerdos del equipo que se mantienen

Cada HU conserva un único responsable y todos sus criterios. ADM1, ADM2 y ADM24 se reutilizan entre las dos experiencias web sin volver a sumar sus puntos. Alejandro conserva el reconocimiento de haber creado la plantilla UI/UX. El apoyo de Jose en el resumen del profesor y de Ramses en módulos/correo mantiene su autoría, sin reasignar ADM3 o ADM5.

Las barras representan desarrollo disponible y se ponderan con los puntos del backlog. Ejecutar más pruebas, ampliar fichas o revisar código no aumenta esas barras por sí solo. Los cierres y la aceptación se registran por separado.

### Mi criterio de verificación

Distingo implementación, diferencias, ejecuciones locales de esta sesión, resultados históricos y declaraciones del equipo. Las comprobaciones de Jose sí se ejecutaron durante la sesión; las fichas de Ramses conservan su procedencia. No se repiten suites por el solo hecho de actualizar el informe.

Los estados de este corte son:

- **Parcial con incremento inspeccionado o verificación local:** existe desarrollo concreto; se identifica qué se comprobó y qué falta para el cierre.
- **Parcial conservado:** se mantiene la implementación anterior sin incremento funcional localizado en este corte.
- **Prototipo / base reutilizable:** existe estructura visual o servicio de apoyo, sin el flujo A2 completo.
- **Pendiente:** no se localiza el flujo específico requerido.
- **No verificado en ejecución:** el código o la documentación no bastan para acreditar su comportamiento real.

No se consultaron SQL compartido, SMTP, Teams, tablero ni aprobación remota de PR. Los 404 Razor y builds anteriores conservan su fecha. Los arranques para Playwright, builds y suites de esta sesión se registran como actuales, con API/propietarios simulados.

### Lectura del documento de recomendaciones

El Word organiza el trabajo en ocho apartados. Lo tomo como guía de coordinación sobre capacidades y pendientes, subordinada al PDF y al reparto vigente.

| Apartado del Word               | Contraste con el estado actual                                                                                                                                                                                                |
| ------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1. Componentes de Jose | Sesión y notas ya están en la plantilla; se añaden bloqueo por grupo y autorización dinámica. Pendientes: destinos finales, operaciones propias y verificación real. |
| 2. Componentes de Ramses        | Se conservan las seis pantallas y APIs. Ahora hay mejoras adicionales de paginación, bitácoras y tratamiento de resultados.                                                                                                 |
| 3. Pendientes de Alejandro | La integración de Jose conecta sesión, salida y menú. Siguen pendientes contenido académico, perfil, formularios propios y criterios restantes de ADM1. |
| 4. Pendientes de Fabian         | No localicé incremento funcional en sus cinco HU. Las páginas de demostración de la nueva plantilla no sustituyen sus dashboards ni mantenimientos.                                                                        |
| 5. Pendientes de Jose | Se avanzó en sesión/notas/permisos; continúan entorno real, operaciones propias y SRV7. Las fichas nuevas documentan el trabajo sin restaurar la guía eliminada. |
| 6. Pendientes de Ramses         | Paginación, snapshots, teléfonos y resultados de correo tienen cambios concretos. La regla temporal se documentó, pero mantiene una diferencia respecto al texto del PDF. Migraciones y recorridos reales siguen abiertos. |
| 7. Apertura de las aplicaciones | Ramses conserva proxy a 5080; plantilla integrada en 4175. Se arrancó para Playwright. Antes de usar el gateway real deben prepararse las asignaciones. |
| 8. Continuidad propuesta | La base común ya permite sesión y notas. Corresponde completar pantallas propias y acreditar los seis recorridos con servicios reales. |

Actualizamos el menú: **`GET /gateway/menu`** devuelve los módulos propios sin aceptar un rol del navegador. La variante **`GET /gateway/menu/{idRol}`** queda para administrador con ADM7. Se resuelve el ID persistido del rol, distinto de su nombre canónico cuando corresponda. La ruta antigua era antecedente; el endpoint propio, sus políticas y consumo sí son incremento de esta sesión.

## Criterios generales del PDF y roadmap

| Criterio / fuente                          | Estado actual                                | Observación                                                                                                                                                                     |
| ------------------------------------------ | -------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Reparto y alcance A2                       | Conservado                                   | Veinte HU, 120 puntos y responsables sin cambios.                                                                                                                                |
| Tecnología web, PDF p. 21 | Integración parcial en React | Login, sesión y notas montados en la plantilla. Persisten las bases Razor y demás experiencias A2 por integrar. |
| Gateway YARP, pp. 15-16 | Routing ampliado | Menú propio, destino Módulo y metadatos de módulo por familia; conserva HTTP/YARP. Faltan destinos reales y operaciones propias estudiantiles. |
| Login, refresh y logout, p. 5 | Integrado con verificación local | Identidad, renovación programada, recarga, rutas y salida. Los inicios por perfil son contenedores; dashboards finales pendientes. |
| Layout y navegación por rol, pp. 4-5 | Integración común disponible | Se retiran demoUser y salida deshabilitada. Notas incluye regreso, ruta activa y breadcrumbs. Responsive integral y auditoría UI pendientes. |
| Permisos efectivos | Implementados en gateway y menú | Perfil permitido y módulo activo asignado por petición. Sin bypass de administrador; fallo de consulta devuelve 503. Asignaciones reales sin aplicar. |
| Mantenimientos de Ramses                   | Incremento inspeccionado                     | Paginación, resultados, snapshots y validaciones añadidos. Falta unión con la plantilla y recorrido con propietarios reales.                                                  |
| Dashboard y mantenimientos de Fabian       | Base/prototipo conservado                    | Sin nuevas pantallas funcionales localizadas para sus cinco HU.                                                                                                                  |
| Inicio, expediente y prematrícula propios | Sesión conectada; trámites pendientes | Estudiante entra a /inicio con identidad y salida, sin vínculo/conteos académicos ni formularios propios. URLs pendientes informan que no están integradas. |
| Perfil y contraseña, p. 13                | Pendiente                                    | Sin pantalla propia ni flujo parametrizado integrado localizado.                                                                                                                 |
| Paginación, pp. 17-19                     | Implementada en las seis pantallas de Ramses | Lectura de `TAMPAG`, tope visual de 15 y controles. Módulos/carreras/cursos/periodos paginan en cliente; profesor e historial solicitan páginas al servidor.                 |
| Bitácoras, pp. 16-17                      | Mejora concreta, cierre parcial              | Nuevas consultas auditadas de módulos y snapshots de carrera, profesor, curso y periodo. Persistencia y recuperación real pendientes; usuarios mantiene su pendiente anterior. |
| Correo ADM22, p. 12                        | Manejo de resultados ampliado                | Intenta guardar errores de configuración/SMTP y diferencia `ENVIADA_SIN_HISTORIAL` cuando falla SQL después de enviar. La UI advierte que no se reenvíe.                    |
| Periodos ADM12, p. 9                       | Protección ampliada                         | Transacciones serializables y bloqueos SQL en altas/cambios. Sigue la restricción por año, más amplia que la literalidad año/número del PDF.                                |
| BD, pp. 20-21 | Preparación pendiente | Migraciones 002/003 conservadas; script manual de asignaciones sin ejecutar. Bloqueo de Notas sin cambios de esquema. |
| Evidencia, p. 22 | Documentación y comprobaciones locales ampliadas | Tres fichas de Jose y contrato añadidos. Faltan versión comprometida, capturas reales y revisión por criterio de las veinte HU. |
| Paquete final, pp. 21-22 | Parcial | Guías y contratos disponibles; faltan consolidación A2, diagramas y entrega remota comprobable. |

## Comparación contra auditorías anteriores

| Área | Segunda auditoría A2 | Primer corte auditoría 3 | Después de esta sesión |
| --- | --- | --- | --- |
| Jose | Implementación principal; 75% | Sin incremento nuevo; 75% | Sesión/notas integradas, bloqueo por grupo y permisos dinámicos; aproximadamente 86%. |
| Ramses | Seis pantallas; 75% | Correcciones reconocidas; 84% | Conserva 84%; contrato disponible para su consumo. |
| Alejandro | Plantillas/base visual; 28% | Plantilla React; 35% | Conserva 35%; Jose integra su plantilla. Flujos propios pendientes. |
| Fabian | Prototipo/bases; 32% | Sin pantallas nuevas; 32% | Conserva 32%; dispone de contrato y destino de notas, sin matriz/dashboard completos. |
| Menú/permisos | Relación persistible sin efecto integrado | Contrato/consumo abiertos | Menú propio y autorización por petición implementados; datos y revocación reales pendientes. |
| Notas/desglose | Componente aislado y servicios | Conservados | Montaje en layout y bloqueo transaccional por grupo; ejecución SQL real pendiente. |
| Paginación/correo/bitácoras de Ramses | Pendientes transversales | Correcciones inspeccionadas | Se conservan sin presentar sus fichas como ejecuciones nuevas. |
| Desarrollo / cierres | 53,125%; 0/20 | 57,0833%; 0/20 | 60,0833%; 0/20. |

HEAD sigue en `034ca8b`, con la plantilla `4db2de8`, proxy `79b9efc` e integraciones de Ramses ya examinadas. El incremento de esta sesión está en el árbol local; no se presenta como otro commit, merge o PR remoto.

Las HU y pruebas V1 son antecedentes. Las 136 pruebas de la segunda auditoría no se suman a las actuales: varias son regresiones de los mismos conjuntos.

## Auditoría por persona y trazabilidad por HU

Las tablas cubren las veinte historias. En Jose se incorporan desarrollo y ejecuciones locales de la sesión. Las referencias de Ramses y los informes anteriores conservan su procedencia.

### Persona 1 - Jose

Responsabilidad: **4 HU, 32 puntos estimados**. Fuentes: [sesión y layout](../pruebas/jose-integracion-sesion-layout.md), [notas integradas](../pruebas/jose-integracion-notas-layout.md), [concurrencia/permisos](../pruebas/jose-concurrencia-permisos.md) y [contrato rol-módulo](../planificacion/permisos-rol-modulo.md).

| HU / fuente | Desarrollo e incremento de esta sesión | Verificación local | Estado y cierre pendiente |
| --- | --- | --- | --- |
| ADM2 / PDF p. 5 | Login y ClienteSesion compartidos en la plantilla mediante SesionProvider. Identidad desde /sesion, restauración, renovación programada, rutas protegidas y salida inmediata. Limpieza de ingresos incompletos y rechazo de respuestas tardías de cuentas cerradas. | Node y navegador: tres perfiles, recarga, refresh, expiración, dependencia caída, salida y respuestas en vuelo. API interceptada. | Parcial con incremento. **75% → 85%**. Integrar dashboards finales ADM3/WEB1 y comprobar cuentas/bitácoras reales. El contenedor /inicio no entrega esas historias. |
| ADM17 / PDF p. 11 | /notas monta el componente existente con cliente compartido, menú/acceso rápido, breadcrumbs y regreso. Exige profesor y ADM17. BloqueoGrupoNotas coordina nota y reemplazo con bloqueo SQL exclusivo por grupo hasta commit/rollback, incluyendo dos POST del mismo par matrícula/rubro. | Navegador integrado y 45 casos Java de Notas, incluidos tres concurrentes con dos instancias/transacciones y ocho del adaptador JDBC. Repositorios/bloqueo/conexiones simulados. | Parcial con incremento. **75% → 90%**. Acreditar bloqueo SQL real, rollback/timeout, grupos/matrículas y snapshots persistidos. Integrar el destino desde el dashboard completo de Fabian. |
| GTW1 / PDF pp. 15-16 | YARP conserva familias y contratos HTTP. Nuevo /gateway/menu propio, destino Módulo y metadatos de módulo; variante con rol administrativa. Un servicio sin mapeo no queda exento por defecto. | HTTP con Kestrel/YARP y destinos simulados; verbos/cuerpo/query/estado/Location y routing conservados. Compilación aprobada. | Parcial con incremento. **75% → 80%**. Acreditar destinos/configuración reales y publicar operaciones propias estudiantiles cuando existan. Aquí se reconoce routing/exposición; la decisión de autorización se cuenta en GTW2. |
| GTW2 / PDF p. 16 | Sesión y perfil permitido más módulo activo asignado al ID real del rol, por petición. Menú propio sin rol del navegador, sin bypass de administrador, sin caché de permisos entre peticiones y 503 ante fallo de consulta. Contrato y preparación manual documentados. | Retiro/inactividad, techo de perfil, respuesta inválida/caída y controlador de menú propio; navegador oculta Notas sin ADM17. Las 27 pruebas de gateway compartidas con GTW1 se cuentan una vez. | Parcial con incremento. **75% → 90%**. Preparar asignaciones y demostrar efectos reales UI/API; siguen propiedad estudiantil y SRV7 pendientes. El contrato no entrega la matriz visual ADM5. |

**Balance del avance:** Jose pasa de **75% a 86,25%, aproximadamente 86%**. El aumento reconoce montaje funcional de sesión/notas, coordinación transaccional por grupo y autorización dinámica. No procede del número de pruebas ni de las fichas. Las cuatro HU siguen abiertas para cierre integral.

Se reutilizan la plantilla de Alejandro y la relación rol-módulo de Ramses, conservando su autoría y el reparto. El script de asignaciones está preparado, **sin ejecutar**. Antes de usar el gateway con datos reales deben existir módulos/asignaciones, porque el perfil administrador por sí solo ya no concede acceso.

### Persona 2 - Ramses

Responsabilidad: **6 HU, 30 puntos estimados**. Fuentes: [fichas de sus HU](../pruebas/ramses/ADM7.md), cambios de frontend y servicios.

| HU / fuente       | Desarrollo nuevo inspeccionado                                                                                                                                                                                                                                                                                                                                          | Verificación actual                                                                                                                                                                                    | Estado y cierre pendiente                                                                                                                                                                                                          |
| ----------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| ADM7 / PDF p. 8 | Módulos conserva paginación, mensajes y asignación de roles. Jose añadió GET /modulo/menu propio como soporte GTW2 sobre la relación de Ramses. | Se conserva la inspección de su pantalla. La consulta propia prueba ID real de rol y auditoría; no acredita persistencia SQL de la matriz. | Parcial. Verificar migración/asignaciones y adaptar consumo al contrato. El apoyo de Jose no vuelve a sumar la misma integración a esta barra. |
| ADM9 / PDF p. 8   | [Carreras](../../../web/ramses/src/Carreras.jsx) añade paginación, resultado y confirmación. `CarreraService` conserva estados anterior/actual y eliminado para la bitácora.                                                                                                                                                                                       | Se inspeccionaron servicio y filtro que utiliza el detalle de la operación.                                                                                                                            | Parcial con incremento. Demostrar persistencia y bloqueo por cursos, selección de director e integración con propietarios reales.                                                                                                |
| ADM10 / PDF p. 8  | [Cursos](../../../web/ramses/src/Cursos.jsx) añade páginas y mensajes. `CursoEndpoint` prepara JSON nuevo/anterior/actual/eliminado; el filtro lo utiliza.                                                                                                                                                                                                           | Cambios inspeccionados. El fallo al registrar bitácora ahora se escribe en log en lugar de quedar en un `catch` vacío.                                                                              | Parcial con incremento. Comprobar registros persistidos, nivel/carrera y borrado con grupos reales. El log del fallo no recupera por sí solo la bitácora perdida.                                                                |
| ADM11 / PDF p. 9  | [Profesores](../../../web/ramses/src/Profesores.jsx) usa tamaño parametrizado y mensajes. Request/servicio validan formato local de ocho dígitos, con prefijo 506 opcional; se añaden snapshots.                                                                                                                                                                      | Validación presente en cliente y servidor. Sigue la comprobación de teléfonos repetidos sobre cadenas recortadas.                                                                                    | Parcial con incremento. Acreditar edad/dominio, páginas/orden y bloqueo por grupos activos. Revisar números equivalentes escritos con guion o prefijo distinto, que no quedan unificados por el recorte actual.                  |
| ADM12 / PDF p. 9  | [Periodos](../../../web/ramses/src/Periodos.jsx) añade páginas, estados y resultados. Endpoint incorpora JSON. [Repositorio](../../../NuevoAvatar.Periodo/Repository/PeriodoRepository.cs) verifica solapamientos dentro de transacciones serializables con `UPDLOCK, HOLDLOCK`.                                                                                      | Protección SQL presente por inspección. La prueba concurrente usa repositorio en memoria y una misma instancia del servicio; no demuestra concurrencia entre peticiones HTTP o instancias SQL reales. | Parcial con incremento. Validar concurrencia real y resolver formalmente la regla por año frente a «mismo año/número». El semáforo pertenece al servicio registrado como scoped y no es un bloqueo global entre solicitudes. |
| ADM22 / PDF p. 12 | [Correo](../../../web/ramses/src/Notificaciones.jsx) añade historial navegable, resultados y recarga ante fallo. El [servicio](../../../NuevoAvatar.Notificacion/src/main/java/cr/ac/cuc/nuevoavatar/persona5/notificacion/NotificacionService.java) intenta registrar ERROR por configuración/SMTP y retorna `ENVIADA_SIN_HISTORIAL` si falla el guardado posterior. | Se inspeccionaron las ramas y el mensaje «No reenvíes el mensaje» en UI. Las fichas declaran pruebas locales, no ejecución SMTP/SQL real.                                                           | Parcial con incremento. Aplicación de migración, envío/historial reales y consumo en ADM3 pendientes. Si falla también el guardado de ERROR, el código conserva el error principal sin garantizar persistencia del intento.   |

**Balance del avance:** se conserva aproximadamente **84%**, reconocido en el primer corte por las correcciones de sus seis HU. Esta sesión no añade otro incremento a su barra. Ya no corresponde repetir como ausentes paginación, consultas auditadas, snapshots o resultado explícito de correo. Las seis historias siguen abiertas por integración y evidencia.

Las fichas ahora incluyen matrices y explicaciones de reglas. Eso mejora la trazabilidad, sin sumar desarrollo a la barra. El selector de director sigue consultando hasta cien resultados; se distingue de la página del mantenimiento y conviene verificar que permita encontrar todos los profesores necesarios.

### Persona 3 - Alejandro

Responsabilidad: **5 HU, 28 puntos estimados**. Se mantiene el reconocimiento expreso de que **Alejandro creó la plantilla UI/UX**.

| HU / fuente         | Desarrollo actual inspeccionado                                                                                                                                                                                                                                                        | Verificación actual                                                                                                                                                                                         | Estado y cierre pendiente                                                                                                                                                                                                          |
| ------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| ADM1 / PDF pp. 4-5 | Plantilla y componentes de Alejandro conservados. La integración de Jose proporciona identidad, sesión/salida, menú propio y Notas. | Build aprobado y navegación cubierta localmente. Se retiró demoUser. Login móvil comprobado; no es una batería responsive completa ni de auditoría UI. | Parcial. Completar experiencias, responsive integral y registro de accesos/rutas/errores. Se conserva 75% de ADM1; sesión/notas/gateway se reconocen en las HU de Jose sin duplicación. |
| ADM24 / PDF p. 13   | No se localiza pantalla/formulario propio nuevo de perfil o cambio de contraseña.                                                                                                                                                                                                     | Sin incremento específico; Header muestra identidad recibida por props, no un flujo de perfil.                                                                                                              | Pendiente. Perfil propio, campos protegidos, política parametrizada, cambio real, limpieza de sesiones cliente y bitácora.                                                                                                       |
| WEB1 / equipo C1-C6 | Base visual conservada. El estudiante llega al contenedor /inicio con identidad y salida mediante ADM2, sin información académica propia. | Navegador cubre la sesión estudiantil; no vínculo/disponibilidad de expediente ni conteo de prematrículas. La página visual anterior sigue sin montarse. | Prototipo/base conservados. Completar contenido académico, caso sin vínculo y accesos propios. La sesión compartida no vuelve a contarse como WEB1. |
| WEB2 / equipo C1-C6 | Servicios de Expediente/Dirección reutilizables; no se localiza formulario propio nuevo ni ruta React de expediente.                                                                                                                                                                  | Los archivos Razor antes observados no cambiaron. El resultado HTTP anterior no se reejecuta.                                                                                                                | Base reutilizable. Consulta/edición exclusiva de contacto, cascadas, confirmación, persistencia y rechazos de campos protegidos/datos ajenos.                                                                                    |
| WEB4 / equipo C1-C6 | Servicio V1 reutilizable; no se localiza flujo React de consulta/alta/cambio/cancelación propia.                                                                                                                                                                                      | Contratos y validaciones conservados: primer nivel y periodo futuro no completan pertenencia curso-carrera ni duplicados entre solicitudes.                                                                  | Base reutilizable. Operaciones propias, reglas en servidor, protección concurrente, confirmaciones y snapshots según operación.                                                                                                 |

**Balance del avance:** se conserva la estimación del primer corte: ADM1 **75%** y Alejandro aproximadamente **35%**. El aumento anterior desde ADM1 50% y Alejandro 28% correspondió a la estructura React y componentes comunes. Esta sesión integra las dependencias de Jose sin volver a sumar ese trabajo. WEB1/WEB2/WEB4 y ADM24 mantienen su estimación hasta completar sus flujos propios.

La carpeta NuevoAvatar.Estudiante.React reúne ahora sesión y notas. Su nombre no acredita un portal académico completo. Jose resuelve dependencias compartidas; los formularios propios y criterios restantes siguen bajo las HU de Alejandro.

### Persona 4 - Fabian

Responsabilidad: **5 HU, 30 puntos estimados**. Se conserva la distinción entre responsabilidad evaluable y autoría de las bases compartidas.

| HU / fuente        | Desarrollo actual inspeccionado                                                                                                                   | Verificación actual                                                                                                 | Estado y cierre pendiente                                                                                                                                    |
| ------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| ADM3 / PDF pp. 5-6 | Resumen de grupos y servicios de métricas/correo conservados; /notas ya integrado como apoyo de Jose. | El inicio identifica sesión por perfil, pero no tiene las cinco métricas ni el dashboard completo. Notas ya tiene destino para enlazar desde ADM3. | Base conservada. Construir dashboards, composición/estados, ayuda/perfil y auditoría de visualización. Consumir resúmenes y otros recursos con sus permisos. |
| ADM4 / PDF pp. 6-7 | Prototipo anterior de tres usuarios en memoria; Usuario aporta backend. | /usuarios en la nueva plantilla informa que la función no está integrada, sin montar el mantenimiento. | Prototipo/base conservados. CRUD real, filtros/orden/páginas, detalle/última modificación, roles/dominios/contraseña, dependencias y snapshots. |
| ADM5 / PDF p. 7 | Rol y relación módulo-rol conservados. Jose entrega contrato y autorización dinámica de gateway como dependencia GTW2. | Sin nueva matriz visual de Fabian. Revocación probada localmente con respuestas controladas; no desde UI persistida de ADM5. | Base conservada. Implementar matriz sobre endpoints existentes, validaciones/guardado y demostrar cambios reales en menú/API. |
| ADM6 / PDF pp. 7-8 | Servicio Parámetro conservado. Ramses ya consume `TAMPAG` y parámetros SMTP.                                                                  | No se localiza mantenimiento visual integrado nuevo. El consumo de parámetros por otra HU no entrega esta pantalla. | Base conservada. CRUD por gateway, validaciones, páginas, mensajes y persistencia; coordinar claves y valores con consumidores.                             |
| ADM8 / PDF p. 8    | Servicio Institución y validación de referencias reutilizables.                                                                                 | No se localiza interfaz nueva de búsqueda/mantenimiento.                                                            | Base conservada. Búsqueda por nombre, CRUD, paginación, confirmación, bloqueo con carreras y bitácora.                                                   |

**Balance del avance:** mantengo aproximadamente **32%**. Las nuevas bases de integración están disponibles para su trabajo, pero no se encontró implementación nueva que justifique subir sus cinco historias. El contrato de permisos y el destino de notas ya están disponibles para integrar. No se vuelve a atribuir a sus HU el incremento reconocido en Jose; faltan sus pantallas completas.

## Hallazgos principales

Conservo A2-01 a A2-19 y actualizo su evolución. A2-20/A2-21 añaden condiciones de puesta en uso y verificación. Distingo inspección, ejecución local simulada y comprobación real pendiente.

| ID    | Prioridad / evolución                                | Hallazgo y evidencia actual                                                                                                                                           | HU / responsables                                   | Condición de cierre                                                                                      |
| ----- | ----------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------- | --------------------------------------------------------------------------------------------------------- |
| A2-01 | Alta; avance parcial                                  | Nueva plantilla React supera la ausencia de una base general en tecnología admitida. Todavía convive con las webs Razor sin integrar los flujos finales.            | ADM1 Alejandro; equipo integra                      | Experiencias A2 completas sobre React/Blazor WebAssembly.                                                 |
| A2-02 | Implementación ampliada | Menú propio y políticas de módulo por familia; YARP conserva HTTP. | GTW1/GTW2 Jose | Destinos/configuración y propietarios reales. |
| A2-03 | Alta; alcance reducido | Usuarios sigue en memoria. Identidad demoUser retirada; sesión conectada a la plantilla. | ADM4 Fabian; ADM2 Jose; ADM1/WEB1 Alejandro | Mantenimiento persistido y contenido académico propio. |
| A2-04 | Alta; resuelto localmente | Gateway comprueba asignación por petición y React consume menú. Contrato definido; datos y matriz visual pendientes. | GTW2 Jose; ADM5 Fabian; ADM7 Ramses; ADM1 Alejandro | Preparar asignaciones, integrar matriz y demostrar revocación/inactividad reales. |
| A2-05 | Alta; vigente                                         | Faltan contratos públicos propios del estudiante. Las rutas globales de expediente/prematrícula siguen restringidas al administrador.                               | WEB2/WEB4 Alejandro; GTW2 Jose                      | Identidad desde sesión y rechazo de campos/datos ajenos sin abrir el CRUD global.                        |
| A2-06 | Alta; vigente                                         | Sin cambio localizado en comparación curso-carrera y duplicados de prematrícula al crear/editar.                                                                    | WEB4 Alejandro                                      | Validaciones y unicidad concurrente en servidor.                                                          |
| A2-07 | Alta; implementación ampliada                        | Sanitización, parámetros e historial conservados; se añaden ramas para fallos y resultado diferenciado.                                                            | ADM22 Ramses; ADM3 Fabian consume                   | SMTP/SQL reales, migración y consumo en dashboard.                                                       |
| A2-08 | Protección ampliada | Snapshots/eventos después de commit conservados; bloqueo transaccional por grupo añadido. | ADM17 Jose | SQL real, rollback/timeout y bitácora persistida. |
| A2-09 | Alta; integración común disponible | Login, identidad, refresh, rutas y salida conectados. Inicios por perfil disponibles; dashboards finales/perfil pendientes. | ADM2 Jose; ADM24/WEB1 Alejandro; ADM3 Fabian | Contenido final y recorrido con cuentas/servicios reales. |
| A2-10 | Alta; alcance reducido | Notas ya está en el layout; faltan experiencias de Fabian, pantallas de Ramses sin integrar y trámites estudiantiles. | Cada responsable | Completar los flujos restantes; no repetir ausencia de montaje ADM17. |
| A2-11 | Alta; evidencia ampliada | Fichas y contrato añadidos. Cambios locales sin commit; evidencia/revisor finales pendientes. | Equipo | Versión comprometida, capturas reales y paquete A2 coherente. |
| A2-12 | Alta; correcciones en Ramses                          | Ya hay snapshots de carrera/profesor/curso/periodo y registro de consultas menú/roles. Usuario mantiene el pendiente anterior.                                       | Ramses por HU; ADM4 Fabian                          | Confirmar contenido/usuario/fecha persistidos y completar el resto de operaciones y errores.              |
| A2-13 | Media; avance limitado                                | Curso registra en log el fallo de Bitácora. Eso no garantiza entrega/recuperación del registro; otros clientes siguen tolerando fallos.                             | Cada responsable de HU                              | Política verificable y comportamiento demostrado ante caída/recuperación.                              |
| A2-14 | Alta; corregido por inspección en las seis pantallas | `TAMPAG`, máximo visual 15 y navegación presentes. Cuatro listados recortan en cliente; profesor e historial paginan por API.                                     | Ramses; regla común                                | Demostrar parámetro y recorrido real; aplicar la regla también a mantenimientos todavía pendientes.    |
| A2-15 | Alta; trámites pendientes | Los 404 Razor son históricos. Estudiante entra con sesión en React, sin expediente/prematrícula propios. | ADM1/WEB1/WEB2/WEB4 Alejandro; ADM2 Jose | Contenido y operaciones propias protegidas/persistidas. |
| A2-16 | Media; formato corregido por inspección              | Validación de teléfono local añadida en cliente/request/servicio. La comparación de duplicados no normaliza guiones/prefijo.                                      | ADM11 Ramses                                        | Demostrar formatos válidos/incorrectos y resolver duplicados equivalentes.                               |
| A2-17 | Media; protección ampliada, criterio abierto         | SQL serializable con bloqueos presente. La ficha documenta exclusión por año, pero el PDF dice mismo año/número.                                                  | ADM12 Ramses                                        | Dejar validada la interpretación y demostrar concurrencia real, incluidos números distintos y límites. |
| A2-18 | Media; corregido parcialmente                         | Se intenta guardar ERROR ante configuración/SMTP y la UI distingue envío sin historial. Si SQL falla también al registrar ERROR, ese intento no queda garantizado. | ADM22 Ramses                                        | Demostrar cada resultado con SMTP/SQL y comportamiento visible ante fallos simultáneos.                  |
| A2-19 | Parcialmente resuelto | Páginas/identidad demostrativas retiradas. URLs pendientes informan y permiten regresar; Notas sí se monta con sesión/permisos. | ADM2/ADM17 Jose; ADM1/WEB1 Alejandro; consumidores | Incorporar destinos finales. No sigue vigente la redirección indiscriminada. |
| A2-20 | Alta; nueva condición de puesta en uso | Gateway deniega sin asignaciones activas, también a administrador. Script inicial sin ejecutar. | GTW2 Jose; coordinación ADM5/ADM7 | Verificar migración 002, IDs y asignaciones antes del uso real. |
| A2-21 | Alta; comprobación real pendiente | Bloqueo SQL ADM17 implementado y probado con dobles; no ejecutado con dos conexiones reales. | ADM17 Jose | Probar cuenta del servicio, misma BD/DataSource, todas las instancias actualizadas, ambos órdenes, timeout y rollback. |

La deduplicación de teléfonos merece una comprobación concreta: `8888-1111` y `88881111` cumplen el patrón, pero son cadenas distintas para el `HashSet` actual. Es un caso deducido del código; no se ejecutó una escritura real para reproducirlo.

### Dependencias que afectan al cierre

La plantilla ya consume sesión y menú; GTW2 aplica el contrato. Fabian debe integrar la matriz y Ramses adaptar sus pantallas a las asignaciones definidas. ADM3 dispone de /notas, pero aún debe componer dashboard, correo y perfil. WEB2/WEB4 siguen necesitando operaciones propias autorizadas.

Los permisos no crean pantallas ni conceden catálogos auxiliares automáticamente: el contrato identifica los recursos consumidos por cada mantenimiento. No hay bypass de administrador ni ejecución automática del script inicial. El entorno debe prepararse antes del recorrido real.

En ADM12 la nueva ficha llama «acordada» a la regla por año. Eso documenta la interpretación de implementación; no localicé en este corte la validación del cambio respecto al criterio literal del PDF. Mantengo abierta esa diferencia, sin descartar la protección concurrente añadida.

## Verificación ejecutada

El primer corte fue documental/estático. Esta actualización incorpora las comprobaciones ejecutadas durante el desarrollo de la sesión, sin repetirlas por el solo hecho de actualizar el informe. Se contrastaron fichas, código y reportes locales disponibles. No se levantó ahora el entorno real ni se aplicaron datos compartidos.

| Verificación de la sesión | Resultado | Alcance y límites |
| --- | ---: | --- |
| Gateway .NET | **27/27 aprobadas** | HTTP con Kestrel/YARP y propietarios simulados; incluye menú propio, revocación, inactividad, techo de perfil y fallos. |
| Autenticación .NET | **29/29 aprobadas** | Regresión local; sin cuentas/BD remotas. |
| Académico .NET | **17/17 aprobadas** | Regresión local; no acredita SQL de periodos ni todas las HU de Ramses. |
| Notas Java | **45/45 aprobadas** | Tres escenarios concurrentes y ocho del adaptador de bloqueo incluidos. Repositorios/bloqueo/JDBC simulados. |
| Integración Java | **3/3 aprobadas** | Configuración local del reactor seleccionado. |
| Cliente de sesión Node | **10/10 aprobadas** | Fetch/almacenamiento controlados y respuestas en vuelo. |
| Navegador de plantilla React | **27/27 aprobadas** | Login/layout/notas/permisos con API interceptada. |
| Navegador de cliente aislado Jose | **6/6 aprobadas** | Regresión de login/notas con API interceptada. |
| Build gateway/Módulo/plantilla | Aprobados | Compilación local, sin despliegue ni destinos reales. |
| Lint de plantilla | Sin errores | Advertencia previa de Fast Refresh en NotificationContext.jsx. |
| Migraciones/preparación de asignaciones | No ejecutadas | Scripts disponibles; esquema/datos compartidos no comprobados. |
| Recorridos SQL/SMTP reales | Sin nueva acreditación | Sin escrituras compartidas ni envíos de correo. |

**164 casos distintos aprobados** en estos conjuntos: `27 + 29 + 17 + 45 + 3 + 10 + 27 + 6`. Se cuenta una ejecución final por caso; builds, lint y repeticiones no se suman. No se agregan las 136 pruebas del informe anterior ni declaraciones de Ramses. No es un total de todas las suites del repositorio ni de criterios aceptados.

Los reportes Maven confirman 45 casos de Notas y tres de Integración, sin fallos/errores/omisiones; los últimos estados Playwright aparecen aprobados. La [ficha del incremento](../pruebas/jose-concurrencia-permisos.md) detalla comandos y límites.

La concurrencia de Notas usa un bloqueo simulado ligado a transacciones de prueba y el adaptador comprueba el contrato JDBC. No certifica sp_getapplock en SQL Server real. En Periodo se mantiene la prueba en memoria con una instancia; en correo se conservan las declaraciones de fichas sin presentar SMTP/SQL real como verificado.

### Evidencia documental y entregables

| Entregable / fuente PDF      | Evidencia localizada                                                                                                | Estado para A2                                                                                                                                    |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------- |
| Análisis/diseño, pp. 21-22 | Planificación, contratos, antecedentes V1 y diagrama existente                                                     | No se localiza paquete A2 consolidado con todos los diagramas, conclusiones y bibliografía exigidos.                                             |
| Scripts | Migraciones 002/003, DDL y preparación manual de permisos | IDs/preparación/aplicación sin ejecutar. Bloqueo de Notas sin DDL; el script inicial configura datos y no se aplica automáticamente. |
| Evidencia por criterio | Matrices de Ramses y fichas de Jose con resultados locales/contrato | Parcial: faltan versión comprometida y capturas/revisión reales por criterio de todas las HU. |
| Esquema de Notificación     | `integracion/contratos.json` ahora describe `general.Notificacion`; README de BD distingue V1 de la ampliación | Corrección documental reconocida. No demuestra que la tabla ya exista en el entorno compartido.                                                  |
| Guía de ejecución | README y fichas de sesión/notas/permisos; lanzadores y proxy | Puerto 4175, dependencias y asignaciones documentados. Falta demostrar paquete con destinos reales; guía antigua eliminada sin restaurar. |
| Consistencia de enlaces | Índice/contratos y guías enlazan fichas nuevas | Mejora del acceso documental actual; quedan referencias históricas ausentes. No se reescriben auditorías anteriores. |
| Tablero y PR final           | Backlog y merges locales                                                                                            | Estado y aprobación remotos no verificados.                                                                                                      |

La documentación corregida sobre el historial de correo resuelve parte de la inconsistencia señalada anteriormente. El inventario histórico de 22 bases/26 tablas del 14 de octubre no certifica por sí solo los objetos añadidos en noviembre.

## Barras de progreso por persona

Las barras conservan la dimensión anterior: **desarrollo disponible por HU, ponderado por complejidad**. Pruebas, fichas y actividad de auditoría no aumentan el porcentaje. Esta actualización solo cambia la estimación de Jose por la implementación nueva. Se conserva la autoría de Alejandro/Ramses sin sumar dos veces componentes compartidos.

Referencias: 0% sin desarrollo localizado; 25% base/estructura inicial; 50% prototipo o componentes parciales; 75% implementación principal con pendientes; 100% desarrollo completo e integrado. Los valores intermedios 80%, 85% y 90% reconocen ampliaciones concretas, sin declarar resueltas las dependencias finales. Son estimaciones de seguimiento, no porcentajes exactos de criterios aprobados ni notas.

### Base del cálculo por historia

La columna anterior corresponde al **primer corte de esta tercera auditoría**, antes del desarrollo de la sesión.

| Responsable | HU: primer corte → actualizado | Fundamento del ajuste |
| --- | --- | --- |
| Jose | ADM2 75% → 85%; ADM17 75% → 90%; GTW1 75% → 80%; GTW2 75% → 90% | Sesión/notas montadas; bloqueo por grupo; routing del menú propio y autorización dinámica consumida por la plantilla. Destinos finales, operaciones propias, configuración real y SRV7 pendientes. |
| Ramses | ADM7 85% → 85%; ADM9 85% → 85%; ADM10 85% → 85%; ADM11 85% → 85%; ADM12 80% → 80%; ADM22 85% → 85% | Se conservan las mejoras reconocidas. La consulta propia añadida por Jose apoya ADM7, sin duplicar la integración ni completar sus recorridos reales. |
| Alejandro | ADM1 75% → 75%; ADM24 0% → 0%; WEB1 25% → 25%; WEB2 25% → 25%; WEB4 25% → 25% | Plantilla/componentes conservados. Se resuelven dependencias de sesión/menú, pero sus flujos propios y criterios restantes no reciben otro incremento independiente en este corte. |
| Fabian | ADM3 25% → 25%; ADM4 50% → 50%; ADM5 25% → 25%; ADM6 25% → 25%; ADM8 25% → 25% | Contrato y destino de notas disponibles como dependencias; sin nuevas pantallas propias. |

Fórmula: **progreso de la persona = suma(puntos de cada HU × desarrollo estimado) / puntos A2 asignados**. Las cuatro HU de Jose tienen ocho puntos cada una: `8 × (0,85 + 0,90 + 0,80 + 0,90) = 27,6` equivalentes de desarrollo, frente a 24 en el primer corte.

| Persona | Barra de desarrollo aproximada | Auditoría 2 | Primer corte auditoría 3 | Actualizado | Justificación |
| --- | --- | ---: | ---: | ---: | --- |
| Jose | `[#################---]` | 75% | 75% | **86%** | Integración de sesión/notas, bloqueo transaccional y permisos dinámicos. |
| Ramses | `[#################---]` | 75% | 84% | **84%** | Correcciones previas de sus seis historias conservadas. |
| Alejandro | `[#######-------------]` | 28% | 35% | **35%** | Estimación de plantilla y bases propias conservada. |
| Fabian | `[######--------------]` | 32% | 32% | **32%** | Sin nuevo desarrollo propio localizado de sus pantallas. |

Cuentas sin redondear: Jose **27,6/32 = 86,25%**; Ramses **25,25/30 = 84,1667%**; Alejandro **9,75/28 = 34,8214%**; Fabian **9,5/30 = 31,6667%**. No son puntos académicos obtenidos. Las barras usan veinte segmentos y se muestran porcentajes redondeados.

**Progreso estimado A2: `[############--------]` ≈ 60%.** Cálculo: `(27,6 + 25,25 + 9,75 + 9,5) / 120 = 60,0833%`. El primer corte era `68,5/120 = 57,0833%`: **esta sesión añade tres puntos porcentuales** por 3,6 equivalentes de desarrollo en Jose. Frente a auditoría 2 (`63,75/120 = 53,125%`), el aumento acumulado es de aproximadamente 6,96 puntos porcentuales.

Las pruebas no forman parte de estas cuentas. El aumento corresponde a implementación que faltaba; las dependencias de desarrollo y las comprobaciones reales pendientes mantienen las historias abiertas.

## Avance del proyecto

| Indicador | Resultado actualizado | Interpretación |
| --- | ---: | --- |
| Historias del corte revisadas | **20/20** | Compromiso A2 conservado. |
| Desarrollo estimado ponderado | **≈ 60%** | Tres puntos porcentuales más que el primer corte por implementación de Jose. |
| Cierre integral acreditado | **0/20** | Criterios y evidencia real de aceptación incompletos. |
| Sesión y notas en plantilla React | **Integradas localmente** | Sesión/identidad/navegación/notas; dashboards y módulos restantes pendientes. |
| Permisos dinámicos | **Implementados localmente** | Gateway y menú consumen asignaciones; datos iniciales y revocación real pendientes. |
| Protección concurrente de Notas | **Implementada y verificada con dobles** | Bloqueo transaccional por grupo; sin certificación SQL real. |
| Pantallas Ramses con incremento previo | **6/6** | Desarrollo y pendientes del primer corte conservados. |
| Casos de los conjuntos de esta sesión | **164 aprobados** | Verificación local separada de progreso/aceptación de HU. |
| Builds gateway/Módulo/plantilla | **Aprobados** | Sin despliegue ni garantía de infraestructura real. |
| Recorridos finales acreditados | **0 de los 6 del roadmap** | Falta demostración con propietarios y persistencia reales. |

Los 120 puntos mantienen su función de compromiso/complejidad. Este informe no fija una calificación ni entrega historias por haber pasado pruebas locales.

## Prioridades para continuar

| Orden | Acción concreta | Responsable según HU | Resultado necesario |
| ---: | --- | --- | --- |
| 1 | Revisar entorno, migración 002, IDs y asignaciones antes del gateway real | Jose GTW2; coordinación Fabian ADM5/Ramses ADM7 | Configuración y preparación de datos conforme al proceso del equipo. Script aún sin ejecutar. |
| 2 | Demostrar login → layout → notas → salida y revocación con servicios reales | Jose ADM2/ADM17/GTW1/GTW2 | Identidad/grupos/matrículas, notas y bitácoras persistidas; permisos, expiración y fallos. |
| 3 | Comprobar concurrencia con dos conexiones SQL reales | Jose ADM17 | Ambos órdenes de nota/reemplazo, dos POST, timeout/rollback y todas las instancias con bloqueo. |
| 4 | Construir matriz/dashboards/mantenimientos sobre el contrato e integrar pantallas | Fabian ADM3/4/5/6/8; Ramses por HU; Alejandro ADM1 | Datos reales, permisos y navegación completa. Notas ya tiene destino; falta dashboard y demás módulos. |
| 5 | Completar inicio académico y operaciones propias estudiantiles | Alejandro WEB1/WEB2/WEB4; Jose integra gateway | Vínculo/conteos reales, expediente de contacto y prematrícula con reglas/propiedad/concurrencia. |
| 6 | Implementar perfil/cambio de contraseña | Alejandro ADM24 | Política parametrizada, campos protegidos, cambio real, limpieza de sesiones y bitácora. |
| 7 | Resolver pendientes específicos de Ramses y completar bitácoras | Ramses por HU; cada responsable registra operaciones | Regla temporal, teléfonos equivalentes, TAMPAG/SMTP, migraciones y registros reales. |
| 8 | Completar responsive y auditoría UI | Alejandro ADM1; consumidores integran | Desktop/tablet/móvil y registro de acceso/rutas/errores en flujos completos. |
| 9 | Acreditar seis recorridos y aclarar SRV7 | Equipo; Jose GTW2 para SRV7 | Servicios reales, expiración, dependencia caída y rechazo de datos ajenos. |
| 10 | Comprometer versión y consolidar entrega | Equipo | Captura por criterio, revisión, guías/diagramas/scripts, conclusiones/bibliografía, tablero y PR final comprobables. |

Se retiran como trabajo ausente el montaje de login/sesión y notas, salida/identidad, definición del contrato rol-módulo, aplicación en gateway/menú y bloqueo por grupo en Notas. Permanecen comprobación real y dependencias finales. Las correcciones previas de Ramses tampoco se vuelven a presentar como implementación pendiente.

La revisión del entorno y las pruebas reales quedaron para una etapa posterior por decisión de esta sesión. No se ejecutan al actualizar este informe.

## Estado al cierre de la revisión

El segundo avance sigue **en desarrollo, con incremento funcional de Jose y sin cierre integral acreditado**. La plantilla reúne sesión y notas; el gateway consulta permisos activos por cuenta en cada petición de negocio. Notas incorpora coordinación transaccional por grupo. Ramses y Alejandro conservan los avances del primer corte; Fabian dispone de contrato y destino de notas para completar sus experiencias.

La estimación actual es **60%**, con Jose **86%**, Ramses **84%**, Alejandro **35%** y Fabian **32%**. La variación de esta sesión corresponde al desarrollo de las cuatro HU de Jose. La plantilla de Alejandro y la base rol-módulo de Ramses conservan su autoría; no se duplican historias ni puntos por reutilización.

Las comprobaciones locales respaldan el alcance probado con propietarios/API/bloqueo simulados. No se aplicaron asignaciones ni migraciones, no se verificó SQL Server real y faltan dashboards, flujos propios y evidencia final. Mantengo **0/20 cierres integrales acreditados**. Antes del recorrido real debemos preparar el entorno y las asignaciones que ahora exige el gateway.
