# Auditoria de avance 2 - Proyecto Nuevo Avatar V2

Fecha de auditoria: 28 de octubre de 2026 (America/Mexico_City).  
Revision: primera auditoria del segundo avance; el sufijo `avance1` identifica la revision del informe, siguiendo el archivo historico.  
Version inicial examinada: `d5c9769dce2539aaca80525873a98450c21a2d33`. Actualizacion de Jose: incremento posterior en el arbol de trabajo, aun sin commit nuevo.
Base principal: [roadmap del avance 2](../roadmaps/roadmap%202.md), [backlog vigente](../planificacion/backlog.md) y [PDF V2](../requerimientos/Proyecto%20Nuevo%20Avatar%20V2%20%281%29.pdf).

Mi objetivo es revisar el estado real del proyecto exclusivamente contra el compromiso del **segundo avance**. Conservo el formato de resumen, alcance, criterios, revision por persona, hallazgos, evidencia, progreso, acciones y dictamen utilizado en la [auditoria anterior](../../primer_avance/auditoria/auditoria_avance4.md). No copio sus resultados como verificaciones actuales.

Actualizacion de progreso: 28 de octubre de 2026, limitada a Jose (ADM2, ADM17, GTW1, GTW2). Incorporo los resultados ya ejecutados y documentados en [su incremento](../pruebas/jose-incremento-a2.md); no los presento como pruebas reejecutadas durante esta edicion documental. Los dictamenes individuales de los demas integrantes conservan el alcance de la revision inicial.

## Resumen

El incremento de Jose incorpora **gateway YARP ejecutable, seguridad integrada al proxy, componentes React de login y notas, logout auditado y snapshots del reemplazo de rubros tras commit**. Las cuatro historias tienen implementacion visible y verificacion local vinculada. Se reutiliza el CSS de la plantilla creada por Alejandro sin modificar su fuente.

El conjunto de verificacion asociado al incremento registra **96 pruebas locales aprobadas**: 29 de Autenticacion, 16 del gateway, 34 de Notas, 3 de la biblioteca Java, 8 del cliente de sesion y 6 de navegador. Sustituye el total anterior de 58; no se suman ambos. Incluye pruebas existentes y componentes de apoyo, no 96 criterios exclusivos de Jose.

Mantengo **0/4 HU de Jose con cierre integral acreditado** y **0/20 cierres acreditados en el corte auditado**: falta demostracion con propietarios/SQL reales, bitacora persistida, integracion al layout y dashboards finales, evidencia por criterio y revision de apoyo. El bloqueo original de ausencia de gateway ya esta resuelto en implementacion local. Los modulos propios de Jose ya usan React; la integracion general de ADM1 y el resto de interfaces conserva sus pendientes.

## Alcance auditado

El corte vigente contiene **20 historias completas y 120 puntos de estimacion**:

| Responsable V2 | HU comprometidas | Puntos |
|---|---|---:|
| Jose | ADM2, ADM17, GTW1, GTW2 | 32 |
| Ramses | ADM7, ADM9, ADM10, ADM11, ADM12, ADM22 | 30 |
| Alejandro | ADM1, ADM24, WEB1, WEB2, WEB4 | 28 |
| Fabian | ADM3, ADM4, ADM5, ADM6, ADM8 | 30 |
| Total | 20 | 120 |

Uso las asignaciones V2: Persona 1 Jose, Persona 2 Ramses, Persona 3 Alejandro y Persona 4 Fabian. Los numeros de los lanzadores y de `tools/servicios.json` conservan el reparto historico V1; no los uso para atribuir responsabilidades evaluables V2. Reconozco que **Alejandro creo la plantilla de UI/UX**, como establece el backlog; ese aporte no cierra por si solo ADM1.

Fuentes y elementos revisados:

- PDF V2, especialmente pp. 4-13, 15-22; criterios WEB1/WEB2/WEB4 en [web-estudiantes.md](../requerimientos/web-estudiantes.md).
- [Contratos](../planificacion/contratos.md), [historias indivisibles](../planificacion/subhistorias.md), [guia de evidencia](../pruebas/criterio-evidencia.md) y [verificacion previa de Jose](../pruebas/persona1.md).
- `Usuario.ConsumoWeb/NuevoAvatar.Web`, proyectos de Autenticacion, Seguridad, Gateway.Seguridad y Notas; servicios reutilizados de Usuario, Rol, Parametro, Modulo, Institucion, Carrera, Curso, Profesor, Periodo, Expediente, Prematricula y Notificacion.
- Scripts `db/por_servicio`, biblioteca `integracion`, manifiesto de servicios y herramientas de compilacion/verificacion.
- Inventario documental A2 y configuracion de proyectos, rutas, modelos, validadores, autorizacion y bitacoras relevantes.

**Excluyo del dictamen de cumplimiento** ADM13-ADM16, ADM18-ADM21, ADM23, WEB3, WEB5-WEB8 y MOB0-MOB6. No penalizo la ausencia de esas pantallas ni de la app movil. Si ADM3 necesita contar facturas pendientes o ADM17 necesita grupos y matriculas, reviso esas capacidades como dependencias existentes, sin adelantar sus mantenimientos visuales de A3.

Esta actualizacion es documental: no modifica codigo funcional ni reejecuta pruebas. El incremento previo incluyo pruebas de navegador y revision visual de login/notas con fixtures, ademas de gateway HTTP real con propietarios simulados. No se aplicaron migraciones, no se consulto ni escribio SQL Server, no se enviaron correos ni se ejecuto la suite de bases temporales. No constituye certificacion de la infraestructura remota.

## Criterios generales del roadmap

| Criterio / fuente | Estado | Observacion |
|---|---|---|
| Alcance y responsable unico | Documentado | Backlog y roadmap coinciden en 20 HU y 120 puntos. Las historias WEB son propuestas del equipo, no IDs del PDF. |
| Tecnologia web, PDF p.21 | Parcial; Jose usa React | Login y notas estan en `web/jose` con build aprobado. La plantilla general Razor de ADM1 sigue pendiente de integracion en tecnologia permitida. |
| Gateway YARP/Ocelot, PDF pp.15-16 y 21 | Implementado y verificado localmente | Host YARP en puerto 5080, catalogo de rutas y lanzador; 16 pruebas HTTP. Falta comprobar todos los propietarios reales. |
| Layout y navegacion por perfil | Parcial: prototipo | Hay menu colapsable, titulos, tema y CSS responsive; nombre/rol y opciones son estaticos. |
| Sesion, JWT y refresh | Parcial: backend y componente web verificados | Login React, refresh compartido, salida y limpieza del cliente implementados. Falta integrar redireccion a dashboards ADM3/WEB1 y persistencia real de auditoria. |
| Roles, permisos y pertenencia | Parcial | Notas comprueba profesor/grupo; los CRUD de estudiante aun reciben IDs libres y no acreditan propiedad. Falta matriz rol-modulo persistida. |
| Flujos administrativos y estudiantiles | Sin cierre acreditado | Jose ya tiene login y desglose/notas React conectados al contrato del gateway. Las demas pantallas conservan la revision inicial; falta recorrido final con servicios reales. |
| Mantenimientos paginados, PDF pp.17-19 | Pendiente | Botones anterior/siguiente muestran mensajes; no hay paginacion real ni tamano parametrizado hasta 15. |
| Bitacoras, PDF pp.16-17 | Parcial; snapshots de Jose ampliados | Notas y rubros conservan JSON nuevo/anterior/actual tras commit; logout se audita desde Autenticacion. Persistencia/remediacion de caidas y filtros de otros dominios pendientes. |
| BD y contratos necesarios para A2 | Parcial | Scripts de bases propias y contratos internos presentes; no encuentro ampliaciones de permisos/menu ni historial de correo. Aprobacion de cambios no verificable localmente. |
| Pruebas automatizadas | Aprobadas dentro de su alcance | 96 casos locales del conjunto del incremento; gateway real con propietarios simulados y navegador con API interceptada. Sin recorrido final con SQL/propietarios reales. |
| Evidencia por criterio y revision | Pendiente | Solo encuentro ficha tecnica de Jose y guia; no capturas A2 ni matrices finales completas de las 20 HU. |
| Entregables y PR final, PDF pp.21-22 | Parcial / no verificado | Planificacion y scripts existen; paquete final de analisis/diseno y evidencia A2 no localizado. Tablero remoto y PR no comprobados. |

Estados: **parcial** significa que encuentro trabajo relacionado, pero faltan criterios; **pendiente** indica una capacidad requerida no localizada; **no verificado** indica que no tengo evidencia suficiente para resolverla. Ninguno equivale a una calificacion del profesor.

## Comparacion contra auditorias anteriores

La auditoria del primer avance evaluaba 22 HU de servicios V1. Aqui evalúo las 20 HU del corte V2, con nuevas interfaces, permisos y entregables. Sus porcentajes y conteos no son comparables. Las capturas V1 pueden orientar fixtures; no prueban estas pantallas.

La etapa inicial de Jose registraba 25 pruebas .NET y 30+3 Java. El incremento posterior amplia ese conjunto a 96 pruebas, agregando proxy ejecutable, componentes React y verificaciones de salida/rubros. La evidencia local aun no cierra integralmente sus historias. El aporte previo al resumen del profesor conserva su autoria tecnica y se relaciona con ADM3, cuyo responsable completo es Fabian.

No traslado bloqueos antiguos sin comprobarlos. Por ejemplo, el DTO actual de Periodo ya contiene `PeriodoId`; no repito como vigente la observacion historica sobre su ausencia. No he reejecutado los arranques de Periodo/Direccion y no afirmo que fallen actualmente.

## Auditoria por persona y trazabilidad por HU

En las tablas enumero las familias de criterios que contrasto y lo que falta. Los pendientes requieren pruebas por criterio; no presento estas filas como casos finales aprobados.

### Persona 1 - Jose

Actualizo exclusivamente sus cuatro HU de A2. Fuentes: [desarrollo de Jose](../desarrollo-jose.md) y [verificacion del incremento](../pruebas/jose-incremento-a2.md). Alejandro conserva la autoria de la plantilla de UI/UX; Jose reutiliza su CSS para los componentes de sus historias.

| HU / fuente | Desarrollo actual verificado | Evidencia | Estado y cierre pendiente |
|---|---|---|---|
| ADM2 / PDF p.5 | Login React, validacion de correo/credenciales, sesion obtenida del servidor, refresh compartido entre solicitudes, limpieza de cliente, proteccion frente a refresh pendiente y endpoint logout auditado. | [Login](../../../web/jose/src/Login.jsx), [cliente de sesion](../../../web/jose/src/sesion.js), pruebas .NET/Node/Playwright. | En verificacion local. Integrar con layout ADM1 y redireccion al dashboard ADM3/WEB1 de cada perfil; demostrar login/refresh/logout y bitacoras persistidas con servicios reales. |
| ADM17 / PDF p.11 y subhistorias | Pantalla React de rubros/notas, vista por grupo/estudiante, estados vacios/error, suma 100, POST/PUT, confirmaciones y bloqueo con notas. Backend captura desglose anterior/actual y publica auditoria tras commit; rollback no publica exito. | [Notas UI](../../../web/jose/src/Notas.jsx), NotasService/AuditoriaNotas, 34 pruebas Notas y casos de navegador compartidos. | En verificacion local. Integrar al layout, ejecutar fixtures reales de profesor/grupo/matricula y comprobar bitacora SQL, concurrencia y caidas. |
| GTW1 / PDF pp.15-16 | Host YARP ejecutable en puerto 5080, rutas hacia las 22 familias de servicios, verbos/cuerpo/query/status, Location publico, error 502 de destino y lanzador propio. | [GatewayApplication](../../../NuevoAvatar.Gateway/GatewayApplication.cs), [catalogo](../../../NuevoAvatar.Gateway/CatalogoRutas.cs), [ProxyHttpTests](../../../NuevoAvatar.Gateway.Tests/ProxyHttpTests.cs). | En verificacion local; superada la ausencia de proxy. Falta recorrido contra cada propietario real y configuracion de entrega. |
| GTW2 / PDF p.16 y contratos | Middleware integrado al host: /validate y /sesion, politica por ruta/rol, rechazo 401/403, internos codificados bloqueados, headers de identidad eliminados y error 503 de Auth. | SeguridadGatewayMiddleware y suite HTTP del gateway (16 casos compartidos con GTW1). | En verificacion local. Acordar matriz dinamica ADM5/ADM7 y contratos propios antes de habilitar CRUD al estudiante/profesor; aclarar SRV7 y documentar rechazos reales. |

Mi dictamen: **4/4 HU con implementacion visible y verificacion local vinculada; 0/4 con cierre integral acreditado**. Es un avance concreto frente a la base anterior: gateway y componentes propios ya existen y funcionan dentro del alcance probado. No equivale a 100% de criterios aprobados ni a entrega terminada. La prioridad pasa a integracion real y evidencia, no a volver a crear el gateway o el formulario de login.

### Persona 2 - Ramses

| HU / fuente | Criterios revisados | Evidencia actual | Estado y cierre pendiente |
|---|---|---|---|
| ADM7 / PDF p.8 | Modulos: ID/nombre, estado/orden, asociacion a rol y bitacora. | ModuloService y modelo Modulo con ID/nombre; SQL equivalente. | Base V1 reutilizable. Faltan estado/orden, relacion persistida rol-modulo y mantenimiento integrado. |
| ADM9 / PDF p.8 | Filtro institucion; CRUD/nombre; director existente con selector buscable; bloqueo por cursos; bitacora. | CarreraService valida institucion/director; biblioteca comun comprueba referencias antes de DELETE. | Base V1. Falta pantalla, selector, flujo de errores y evidencia de bloqueo entre servicios reales. |
| ADM10 / PDF p.8 | Filtros carrera/nivel; nivel 1-12; nombre; bloqueo por grupos; bitacora. | Proyecto Curso con servicios/validadores/repositorios. | Base V1. Falta mantenimiento integrado, filtros visuales y pruebas criterio por criterio por gateway. |
| ADM11 / PDF p.9 | Edad; dominio parametrizado; nombre/telefonos; busqueda; paginacion/orden; bloqueo por grupos activos; bitacora. | ProfesorService valida edad, DOMPROF y telefonos; listar devuelve lista completa. | Base V1. Falta pantalla con busqueda/paginacion/orden y demostrar la politica exacta de grupos activos; existencia de referencias no acredita esa distincion. |
| ADM12 / PDF p.9 | CRUD/fechas; estados futuro/activo/cerrado; no solapamiento mismo anio/numero; bitacora. | PeriodoValidator comprueba fin >= inicio; SQL declara UNIQUE(Anio,NumeroPeriodo); DTO incluye ID. | Base V1. Falta interfaz y estados derivados visibles. Comprobar restriccion SQL aplicada y respuesta funcional de duplicado, sin afirmar que falte toda proteccion de solapamiento. |
| ADM22 / PDF p.12 | Destinatarios/asunto/HTML sanitizado; confirmacion; resultado; historial; servidor/cuenta desde parametros. | NotificacionService envia HTML por SMTP; propiedades MAIL_*; SQL sin tablas propias. | Parcial transporte V1. No encuentro sanitizacion, historial ni lectura de SMTP desde Parametro; falta pantalla y alimentacion real de ADM3. |

Mi dictamen: hay servicios de apoyo, pero **0/6 HU cerradas acreditadas**. ADM7 y ADM22 requieren ampliaciones de API/datos, ademas de las interfaces. No considero el correo recibido en V1 como evidencia del historial o sanitizacion V2.

### Persona 3 - Alejandro

| HU / fuente | Criterios revisados | Evidencia actual | Estado y cierre pendiente |
|---|---|---|---|
| ADM1 / PDF pp.4-5 | Layout maestro; menu backend por rol; responsive; cuatro tipos de notificacion; ruta/breadcrumb/titulo; auditoria acceso/rutas/errores UI. | Plantilla UI/UX reconocida a Alejandro; Index, layout, CSS con media queries y site.js. Compilacion aprobada. | Parcial prototipo. Resolver tecnologia exigida, identidad/menu reales, salida, mensajes y auditoria. CSS responsive no sustituye pruebas desktop/tablet/movil. |
| ADM24 / PDF p.13 | Perfil con campos protegidos; cambio de contrasena con longitud/complejidad parametrizadas; limpiar sesiones del cliente; bitacoras. | Datos de sesion y CRUD Usuario reutilizables; no encuentro pantalla/contrato propio completo. | Pendiente de flujo A2. Cambiar contrasena en CRUD global no demuestra perfil propio ni politica parametrizada. |
| WEB1 / equipo C1-C6 y comunes | Nombre/rol/salida; estado expediente y conteo propio; accesos funcionales; sin vinculo; rol ajeno; expiracion/dependencia/auditoria. | /sesion resuelve identidad desde propietarios; prototipo muestra administradora fija. | Pendiente de inicio estudiantil. No hay conteo real ni manejo integrado del vinculo ausente y estados de error. |
| WEB2 / equipo C1-C6 y comunes | Expediente propio; solo contacto editable; cascada territorial; confirmacion/persistencia; rechazo de IDs/campos protegidos; auditoria antes/despues. | CRUD Expediente/Direccion V1; servicio de identidad de sesion. | Pendiente de interfaz y contrato propio acotado. El CRUD actual permite cuerpo completo e identificacion en URL, sin comparacion de propietario visible. |
| WEB4 / equipo C1-C6 y comunes | Carrera/curso relacionado; primer nivel y futuro; duplicados crear/editar; consulta/cambio/cancelacion propios; confirmaciones; aislamiento y JSON por operacion. | Prematricula valida existencia, primer nivel y periodo futuro; tiene CRUD y bitacoras. | Parcial en reglas V1; sin UI propia. No encuentro control de propietario, correspondencia curso-carrera ni rechazo de duplicados entre solicitudes. |

Mi dictamen: reconozco la plantilla como trabajo realizado; **0/5 HU cerradas acreditadas**. El portal exige adaptar los servicios para datos propios antes de exponer sus CRUD al estudiante. No se necesita adelantar la pantalla administrativa ADM16 para entregar WEB2.

### Persona 4 - Fabian

| HU / fuente | Criterios revisados | Evidencia actual | Estado y cierre pendiente |
|---|---|---|---|
| ADM3 / PDF pp.5-6 y subhistorias | Saludo/atajos; cinco metricas admin; grupos/cursos/notas del profesor; notificaciones/bitacoras recientes; ayuda/perfil; auditoria. | ResumenProfesorService y 3 pruebas dentro de las 30 de Notas; servicios V1 pueden aportar datos administrativos. | Parcial backend de profesor con aporte previo de Jose. Falta dashboard completo de ambos perfiles, composicion real y accesos autorizados; no contar esos 3 casos de nuevo. |
| ADM4 / PDF pp.6-7 | Columnas/filtros; orden/paginacion; roles y dominio; contrasena requerida/opcional; borrar; detalle/ultima modificacion; token y snapshots. | Prototipo de usuarios en JavaScript y backend Usuario con BCrypt/validaciones. | Parcial prototipo + base V1. No existe enlace UI/API, detalle de auditoria ni lista integrada paginada/ordenable. |
| ADM5 / PDF p.7 | CRUD/duplicado/nombre; matriz rol-opcion; persistencia/lectura al ingresar; autorizacion efectiva; bitacora de permisos. | RolService y SQL de ID/nombre; no encuentro matriz de permisos. | Base V1. Faltan contrato/persistencia de permisos y mantenimiento; ocultar botones no cumple autorizacion. |
| ADM6 / PDF pp.7-8 | CRUD ID mayusculas <=10/valor <=500; paginacion; bitacora y cambios JSON. | Parametro y controladores/servicios V1. | Base V1. Falta mantenimiento integrado paginado y evidencia de validaciones/estados/auditoria por gateway. |
| ADM8 / PDF p.8 | Busqueda nombre; CRUD/nombre; bloqueo por carreras con mensaje; bitacora. | InstitucionService y comprobacion comun de referencias en integracion. | Base V1. Falta pantalla, busqueda, mensajes y ejecucion real con carrera dependiente. |

Mi dictamen: **0/5 HU cerradas acreditadas**. La prioridad es el contrato ADM5 con ADM7 y las necesidades de ADM3 con ADM17/ADM22/ADM24. Las historias conservan un solo responsable aunque exista apoyo tecnico previo.

## Mis hallazgos principales

Las prioridades expresan impacto sobre el cierre A2, no una evaluacion de seguridad certificada. Actualizo los hallazgos afectados por el incremento de Jose; los demas conservan su inspeccion inicial. A2-02 y A2-08 tienen correccion local verificada, con integracion final aun pendiente. Distingo defectos observados por lectura de fallos reproducidos por ejecucion.

| ID | Prioridad | Hallazgo y fundamento | HU / responsable | Accion y evidencia de cierre |
|---|---|---|---|---|
| A2-01 | Critica | La plantilla general ADM1 sigue en Razor Pages. **Actualizacion Jose:** sus componentes de login/notas ya usan React y compilan; no se les aplica la observacion de ausencia de tecnologia permitida. | ADM1 / Alejandro; integracion de interfaces | Integrar los componentes al layout final en tecnologia admitida, conservando la autoria de Alejandro. |
| A2-02 | Implementacion local resuelta | Ya existe [host YARP](../../../NuevoAvatar.Gateway/GatewayApplication.cs), catalogo, politicas y [lanzador](../../../tools/ejecutar-gateway.ps1). Las 16 pruebas HTTP y el arranque local superan la ausencia de proxy original. | GTW1/GTW2 / Jose | Pendiente certificacion con los 22 propietarios reales; no se marca cierre integral de las HU. |
| A2-03 | Alta | [site.js](../../../Usuario.ConsumoWeb/NuevoAvatar.Web/wwwroot/js/site.js) mantiene tres usuarios de ejemplo, modifica un arreglo, usa perfil fijo y muestra toast en paginacion y secciones de demostracion. No hay llamadas HTTP al backend en ese flujo. | ADM1/ADM4; resto de interfaces / responsables del backlog | Sustituir datos de demostracion por integracion real, listado persistido y estados de carga/vacio/error. Verificar recarga tras guardar y permisos. |
| A2-04 | Alta | [Modelo Modulo](../../../NuevoAvatar.Seguridad.Models/Modulo.cs), [Rol](../../../NuevoAvatar.Seguridad.Models/Rol.cs) y scripts solo contienen ID/nombre; no encuentro relacion de permisos ni estado/orden de modulo. | ADM5 Fabian, ADM7 Ramses, ADM1 Alejandro; GTW2 Jose | Definir contrato comun, cambio de datos aprobado, persistencia y autorizacion efectiva; demostrar menu diferente y rechazo de API con otro rol. |
| A2-05 | Alta | [ExpedienteController](../../../NuevoAvatar.Expediente/src/main/java/cr/ac/cuc/nuevoavatar/persona4/expediente/ExpedienteController.java) y [PrematriculaService](../../../NuevoAvatar.Prematricula/src/main/java/cr/ac/cuc/nuevoavatar/persona4/prematricula/PrematriculaService.java) usan IDs/body suministrados; sus interceptores validan token/sub, sin politica de pertenencia. /sesion por si solo no la aplica a esos CRUD. | WEB2/WEB4 / Alejandro; politica GTW2 / Jose | Contratos propios que deriven identidad de sesion, restrinjan contacto y rechacen IDs ajenos/campos protegidos. Probar dos estudiantes y estado sin cambios. Hallazgo estatico; no ataque HTTP ejecutado. |
| A2-06 | Alta | [PrematriculaRepository](../../../NuevoAvatar.Prematricula/src/main/java/cr/ac/cuc/nuevoavatar/persona4/prematricula/PrematriculaRepository.java) obtiene curso por ID/nivel, no carrera; servicio no compara curso-carrera ni duplicados entre solicitudes. [SQL](../../../db/por_servicio/Prematricula.sql) tampoco declara unicidad estudiante/curso/periodo. | WEB4 / Alejandro | Verificar relacion y duplicados en alta/edicion con proteccion transaccional apropiada. Rechazar curso de otra carrera, repeticion y colisiones sin cambios parciales. |
| A2-07 | Alta | [NotificacionService](../../../NuevoAvatar.Notificacion/src/main/java/cr/ac/cuc/nuevoavatar/persona5/notificacion/NotificacionService.java) envia HTML directamente; propiedades MAIL_*; [SQL](../../../db/por_servicio/Notificacion.sql) sin tablas. No encuentro sanitizacion, historial ni configuracion desde Parametro. | ADM22 / Ramses; dependencia ADM3 / Fabian | Implementar criterios V2 completos; probar HTML peligroso sanitizado, envio fallido/exitoso e historial real consumible por dashboard. |
| A2-08 | Correccion local verificada | NotasService captura rubros anteriores y persistidos; AuditoriaNotas publica JSON nuevo/anterior/actual tras commit. Pruebas comprueban alta, reemplazo y rollback. El filtro evita duplicar registros de exito basados solo en request. | ADM17 / Jose | Verificar persistencia en Bitacora real y recuperacion ante caida; concurrencia SQL pendiente. |
| A2-09 | Alta; alcance actualizado | Jose ya tiene login React, refresh, limpieza de cliente y logout auditado. Queda pendiente redireccion a dashboards finales; perfil/cambio de contrasena y portal estudiantil conservan sus responsables. | ADM2 Jose; ADM24/WEB1 Alejandro | Integracion final y recorrido con servicios reales por perfil; politica de contrasena pertenece a ADM24. |
| A2-10 | Alta; alcance actualizado | La pantalla propia de desglose/notas ya existe y tiene pruebas de navegador. Dashboards y mantenimientos de otros responsables conservan los pendientes de la revision inicial. | ADM17 Jose; ADM3 y mantenimientos segun backlog | Integrar notas al layout/dashboard y demostrar datos reales; completar las demas interfaces segun sus responsables. |
| A2-11 | Alta | Jose ya tiene guia de desarrollo y ficha del incremento con 96 pruebas. Falta matriz final por criterio/capturas reales; `contexto-desarrollo.md` sigue sin localizarse. La ausencia anterior de `desarrollo-jose.md` queda superada. | Entrega / equipo | Completar evidencia final y revision individual; no presentar capturas de fixtures como integracion real. |
| A2-12 | Media | [Filtro Usuario](../../../NuevoAvatar.Usuario/src/main/java/cr/ac/cuc/nuevoavatar/persona2/seguridad/BitacoraFilter.java) registra request en PUT y solicitud en DELETE; no conserva estado anterior/eliminado. Oculta contrasenas, pero eso no completa el JSON requerido. | ADM4 / Fabian y otros consumidores del patron | Revisar filtros por dominio; persistir snapshots sin secretos y comprobar alta/cambio/baja/consulta/error. |
| A2-13 | Media | El cliente de Notas captura fallos de Bitacora y solo los registra en log; un negocio 2xx no demuestra auditoria remota persistida. | ADM17 / Jose; transversal | Acordar tratamiento de indisponibilidad y recuperacion de auditoria; demostrar persistencia y comportamiento ante caida. |
| A2-14 | Media | No localizo paquete A2 de analisis/diseno con diagramas, conclusiones/recomendaciones/bibliografia y pruebas completas. Tablero, aprobaciones BD y PR no comprobados. | Entrega / equipo | Reunir entregables aplicables a A2, conservar dependencias y pendientes A3; adjuntar enlaces verificables de revision/aprobacion. |

No elevo a defectos actuales los errores preliminares de compilacion por falta de restore o de JAR sin empaquetar: los resolvi en esta misma verificacion y documento el resultado final abajo. No convierto los nombres de roles normalizados de sesion en prueba de compatibilidad con todos los selectores: el backend Usuario usa `ADMIN`, mientras la sesion normaliza a `ADMINISTRADOR`; deben acordar ID y etiqueta sin enviar la etiqueta visual como ID arbitrario.

## Evidencia y limites de cobertura

Registro estructurado: [verificacion-avance1.json](verificacion-avance1.json). Fuente del incremento: [96 verificaciones de Jose](../pruebas/jose-incremento-a2.md). Los comandos siguientes fueron ejecutados durante el desarrollo previo; esta actualizacion documental incorpora sus resultados sin repetirlos. Los controles no afectados conservan expresamente su resultado inicial.

| Verificacion actual | Resultado | Alcance real |
|---|---|---|
| `dotnet test NuevoAvatar.Autenticacion.Tests/NuevoAvatar.Autenticacion.Tests.csproj --no-restore` | 29 aprobadas; 0 fallos/omitidas | Incluye 25 anteriores y 4 de logout/auditoria; dependencias con dobles. |
| `.\NuevoAvatar.Usuario\mvnw.cmd -B -q -o -f pom.xml -pl NuevoAvatar.Notas -am test` | 34 Notas + 3 integracion; 0 fallos/errores/omitidas | Mockito/MockMvc, snapshots y ciclo commit/rollback; sin SQL remoto. |
| `dotnet restore Usuario.ConsumoWeb/NuevoAvatar.Web/Usuario.ConsumoWeb.csproj --ignore-failed-sources` y `dotnet build Usuario.ConsumoWeb/NuevoAvatar.Web/Usuario.ConsumoWeb.csproj --no-restore` | Build aprobado, 0 errores/advertencias | Compilacion del prototipo Razor; no valida tecnologia admitida ni comportamiento visual. Primer intento sin restore dio NETSDK1004 por assets ausentes. |
| `.\NuevoAvatar.Usuario\mvnw.cmd -B -q -o -f pom.xml -pl NuevoAvatar.Notas -am package -DskipTests` | Empaquetado aprobado | No vuelve a contar ni ejecutar pruebas; completa artefacto tras el comando test. |
| `python tools/validar-artefactos.py --servicio Notas` | Artefacto verificado | Antes de package indico falta de JAR unico; comprobacion final aprobada. |
| `python tools/auditar-limites.py` (revision inicial; no reejecutado en el incremento) | 22 servicios, 290 fuentes, 0 infracciones detectadas en aquella revision | Busqueda estatica de SQL explicito/JPA; no valida roles, pertenencia ni integracion. Cubre infraestructura compartida; no suma HU V1/A3 al dictamen A2. |

Verificaciones adicionales de Jose:

| Verificacion del incremento | Resultado | Limite |
|---|---|---|
| `dotnet test NuevoAvatar.Gateway.Tests/NuevoAvatar.Gateway.Tests.csproj --no-restore` | 16 aprobadas | YARP/Kestrel reales, auth/propietarios simulados por HTTP. |
| `npm test` en `web/jose` | 8 aprobadas | Cliente de sesion con fetch/almacenamiento controlados. |
| `npm run test:ui` en `web/jose` | 6 aprobadas | Chromium con API interceptada; incluye login movil y notas. |
| `npm run build` en `web/jose` | Aprobado | Bundle React; reutiliza CSS de Alejandro. |
| Arranque del gateway y consultas locales | /health 200, sin token 401, interno codificado 404 | Sin acceso a SQL ni operaciones de negocio. |

El verificador de limites escribe por defecto el reporte historico V1. Conservo aqui el resultado actual y restauro ese archivo a su contenido previo para no modificar la evidencia del primer avance.

Desglose actual de las 34 pruebas Java de Notas: EscrituraNotasTest 9, NotasServiceTest 3, NotasHttpTest 6, ResumenProfesorTest 3, SeguridadNotasTest 8 y AuditoriaNotasTest 5. ConfiguracionLocalTest aporta otras 3. Los XML de Surefire quedan en `NuevoAvatar.Notas/target/surefire-reports` e `integracion/target/surefire-reports`; son salidas locales regenerables.

No sumo pruebas historicas de otros servicios, capturas V1 ni colecciones preparadas como ejecuciones A2. No he comprobado concurrencia SQL, persistencia remota de bitacoras, entrega SMTP, responsive en dispositivos fisicos (si hay login probado en viewport movil de Chromium), accesibilidad completa, aislamiento real entre estudiantes ni recuperacion de dependencias desde una web integrada. Esas limitaciones quedan abiertas aunque las 96 pruebas locales aprueben.

## Barras de progreso por persona

Integro en una sola tabla la presencia de implementacion, la verificacion local
y el cierre integral. Las barras miden **HU con implementacion visible**, no
porcentaje de criterios cumplidos ni notas academicas. Los conteos tecnicos de
los otros integrantes no se actualizan sin una verificacion equivalente.

| Persona | HU con implementacion / asignadas | HU con verificacion local / asignadas | HU cerradas / asignadas | Barra de implementacion | Evidencia y pendientes |
|---|---:|---:|---:|---|---|
| Jose | **4/4** | **4/4** | **0/4** | `[####################]` | Gateway YARP/seguridad, login React/logout y pantalla/auditoria de notas. 96 pruebas del conjunto aprobadas; pendientes layout/dashboards finales, servicios/SQL reales y evidencia por criterio. |
| Ramses | No cuantificado | No cuantificado | 0/6 | Sin medicion actualizada | Bases V1 de oferta/modulos/correo reutilizables; criterios V2 pendientes. |
| Alejandro | No cuantificado | No cuantificado | 0/5 | Sin medicion actualizada | Autoria de plantilla UI/UX; prototipo compilable y servicios V1 de apoyo. |
| Fabian | No cuantificado | No cuantificado | 0/5 | Sin medicion actualizada | Bases V1 de usuarios/roles/parametros/instituciones y dependencia de resumen profesor. |

Una barra completa de implementacion no acredita cierre integral. Las 16 pruebas
del gateway apoyan GTW1 y GTW2 sin duplicarse en el total. El conjunto de 96
incluye regresion y componentes compartidos, sin transferir autoria de otras
historias a Jose. Los puntos estiman complejidad y no se usan para calificar.

## Avance del proyecto

Documento **20/20 HU revisadas en alcance**, **0/20 cierres integrales acreditados**, **96/96 pruebas locales aprobadas en el conjunto asociado al incremento de Jose**, **build React aprobado**, arranque local del gateway y **artefacto Notas verificado**. La compilacion Razor de la revision inicial permanece como antecedente. El alcance contiene **15 ADM, 2 GTW y 3 WEB**. Login y notas forman parte de esas 15 ADM; no los cuento de nuevo como componentes independientes.

No calculo porcentaje de implementacion global: los criterios no tienen ponderacion aprobada y la presencia de un servicio V1 o un prototipo no equivale a completar su historia V2. El objetivo de 120 puntos es compromiso de planificacion, no 120 puntos entregados. El PDF ubica A2 en semana 10 con valor academico 15%; no deduzco una nota de estos indicadores.

## Acciones que propongo

| Orden | Accion | Responsable segun backlog | Evidencia necesaria para cerrar |
|---:|---|---|---|
| 1 | Resolver tecnologia web y adaptar plantilla conservando su autoria | Alejandro / ADM1 | Proyecto React o Blazor WebAssembly ejecutable, o excepcion documentada; layout responsive probado. |
| 2 | Integrar y certificar el gateway ya implementado con propietarios reales | Jose / GTW1-GTW2 | Routing contra cada destino, politicas finales y evidencia real; host/lanzador y 16 pruebas HTTP ya disponibles. |
| 3 | Acordar permisos/menu/identidad y cambios de datos | Fabian ADM5, Ramses ADM7, Jose GTW2; Alejandro consume | Contrato y scripts aprobados, persistencia, menu por rol y API protegida. |
| 4 | Integrar login/refresh/logout ya implementados al layout y dashboards; completar perfil | Jose ADM2, Alejandro ADM24 | Redireccion por perfil y bitacora real; politica de contrasena corresponde a ADM24. |
| 5 | Proteger y completar contratos propios de expediente/prematricula | Alejandro WEB1/WEB2/WEB4 | Dos estudiantes, campos protegidos, curso-carrera, duplicados, CRUD propio, cascada territorial y fallos controlados. |
| 6 | Integrar mantenimientos administrativos y ampliar correo/modulos | Fabian y Ramses segun tablas | Persistencia real, filtros/orden/paginacion parametrizada, referencias, sanitizacion e historial de correo. |
| 7 | Integrar notas y snapshots ya implementados; completar dashboards | Jose ADM17, Fabian ADM3, Ramses ADM22 | Grupo/fixtures reales, bitacora persistida, metricas y notificaciones reales; concurrencia y dependencia caida. |
| 8 | Demostrar los seis recorridos del roadmap | Cada responsable y revisor | Interfaz -> gateway -> servicios reales, fixtures identificables, capturas por criterio y version. |
| 9 | Cerrar documentacion y trazabilidad | Equipo | Diagramas/analisis/diseno, scripts, guia A2, conclusiones/bibliografia, matrices, tablero y PR a rama final. |

No propongo agregar pantallas A3 ni repartir una historia entre personas para aparentar avance. Las correcciones de servicios V1 necesarias para un criterio A2 pertenecen a la HU V2 que las necesita.

## Mi dictamen final

Considero el segundo avance **en desarrollo y sin cierre integral demostrable**. Actualizo a Jose a **4/4 historias con implementacion visible y verificacion local**, conservando **0/4 cierres integrales acreditados**. El gateway YARP ya es ejecutable y la seguridad esta integrada al proxy. Los componentes propios de login/notas ya usan React; el reemplazo de rubros y logout tienen auditoria implementada. El conjunto del incremento registra **96 pruebas locales aprobadas**, sin duplicar las 58 de la etapa anterior.


Los pendientes de Jose se concentran ahora en integracion al layout/dashboards, recorrido con propietarios y SQL reales, persistencia/recuperacion de bitacoras, concurrencia y evidencia por criterio con revision. La ausencia de gateway y de componentes React propios ya no se mantiene como bloqueo suyo. El resto del equipo conserva sus pendientes iniciales de layout, permisos persistidos, operaciones propias del estudiante y correo V2. Estos resultados se limitan a las 20 HU acordadas para A2; la ausencia de funcionalidades reservadas a A3 no forma parte del incumplimiento que documento.
