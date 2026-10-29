# Auditoria de avance 2 - Proyecto Nuevo Avatar V2

Fecha de auditoria: 28 de octubre de 2026 (America/Mexico_City).  
Revision: primera auditoria del segundo avance; el sufijo `avance1` identifica la revision del informe, siguiendo el archivo historico.  
Version examinada: `d5c9769dce2539aaca80525873a98450c21a2d33`; arbol de trabajo inicialmente sin cambios.  
Base principal: [roadmap del avance 2](../roadmaps/roadmap%202.md), [backlog vigente](../planificacion/backlog.md) y [PDF V2](../requerimientos/Proyecto%20Nuevo%20Avatar%20V2%20%281%29.pdf).

Mi objetivo es revisar el estado real del proyecto exclusivamente contra el compromiso del **segundo avance**. Conservo el formato de resumen, alcance, criterios, revision por persona, hallazgos, evidencia, progreso, acciones y dictamen utilizado en la [auditoria anterior](../../primer_avance/auditoria/auditoria_avance4.md). No copio sus resultados como verificaciones actuales.

## Resumen

Considero que el segundo avance tiene planificacion definida, servicios V1 reutilizables, una plantilla visual y ampliaciones de backend verificables para sesion, identidad, proteccion del gateway y notas. Reejecuto **58 pruebas aprobadas**: 25 .NET, 30 de Notas y 3 de la biblioteca Java de integracion. Compilo la plantilla web y verifico el JAR de Notas.

No considero cerrado el segundo avance: **0 de las 20 HU tienen cierre integral acreditado**. Este indicador mide historias completas con interfaz o gateway ejecutable, integracion real, permisos, bitacora y evidencia; no significa ausencia de trabajo. No encuentro un proxy YARP/Ocelot ejecutable ni las dos experiencias web integradas. La interfaz disponible es un prototipo Razor Pages con datos en memoria; el PDF exige React o Blazor WebAssembly. Las pruebas aprobadas usan dobles y no certifican los recorridos finales.

Mi prioridad es resolver la tecnologia web y el gateway, acordar permisos/identidad y completar los contratos nuevos antes de acreditar pantallas. Identifico tambien faltantes concretos en prematricula propia, correo y auditoria de rubros.

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

No modifico codigo funcional, no aplico migraciones, no consulto ni escribo SQL Server y no envio correos. No ejecuto la suite que crea bases temporales. La auditoria combina inspeccion estatica y pruebas locales; no constituye certificacion de la infraestructura remota ni revision visual en navegador.

## Criterios generales del roadmap

| Criterio / fuente | Estado | Observacion |
|---|---|---|
| Alcance y responsable unico | Documentado | Backlog y roadmap coinciden en 20 HU y 120 puntos. Las historias WEB son propuestas del equipo, no IDs del PDF. |
| Tecnologia web, PDF p.21 | Incumplimiento visible | La unica web localizada usa Razor Pages. No encuentro proyecto React ni Blazor WebAssembly. |
| Gateway YARP/Ocelot, PDF pp.15-16 y 21 | Pendiente | Hay biblioteca de seguridad; falta host, proxy, destinos y rutas publicadas. |
| Layout y navegacion por perfil | Parcial: prototipo | Hay menu colapsable, titulos, tema y CSS responsive; nombre/rol y opciones son estaticos. |
| Sesion, JWT y refresh | Parcial: backend probado | Login/refresh/validate/sesion existen. Falta ciclo web, salida, renovacion automatica y redireccion por perfil. |
| Roles, permisos y pertenencia | Parcial | Notas comprueba profesor/grupo; los CRUD de estudiante aun reciben IDs libres y no acreditan propiedad. Falta matriz rol-modulo persistida. |
| Flujos administrativos y estudiantiles | Sin cierre acreditado | El prototipo solo mantiene un arreglo JavaScript; las otras secciones muestran demostraciones. |
| Mantenimientos paginados, PDF pp.17-19 | Pendiente | Botones anterior/siguiente muestran mensajes; no hay paginacion real ni tamano parametrizado hasta 15. |
| Bitacoras, PDF pp.16-17 | Parcial | Notas tiene snapshots para asignacion/modificacion; carga/reemplazo de rubros y varios filtros siguen incompletos. Persistencia real no verificada. |
| BD y contratos necesarios para A2 | Parcial | Scripts de bases propias y contratos internos presentes; no encuentro ampliaciones de permisos/menu ni historial de correo. Aprobacion de cambios no verificable localmente. |
| Pruebas automatizadas | Aprobadas dentro de su alcance | 58 casos locales; 0 recorridos finales interfaz -> gateway -> servicios reales ejecutados en esta auditoria. |
| Evidencia por criterio y revision | Pendiente | Solo encuentro ficha tecnica de Jose y guia; no capturas A2 ni matrices finales completas de las 20 HU. |
| Entregables y PR final, PDF pp.21-22 | Parcial / no verificado | Planificacion y scripts existen; paquete final de analisis/diseno y evidencia A2 no localizado. Tablero remoto y PR no comprobados. |

Estados: **parcial** significa que encuentro trabajo relacionado, pero faltan criterios; **pendiente** indica una capacidad requerida no localizada; **no verificado** indica que no tengo evidencia suficiente para resolverla. Ninguno equivale a una calificacion del profesor.

## Comparacion contra auditorias anteriores

La auditoria del primer avance evaluaba 22 HU de servicios V1. Aqui evalúo las 20 HU del corte V2, con nuevas interfaces, permisos y entregables. Sus porcentajes y conteos no son comparables. Las capturas V1 pueden orientar fixtures; no prueban estas pantallas.

La ficha tecnica previa de Jose declara 25 pruebas .NET y 30+3 Java aprobadas. Reproduzco esos totales en esta revision. Esa ficha sigue siendo evidencia de backend con dobles, sin cierre de ADM2/ADM17/GTW2. El aporte previo al resumen del profesor conserva su autoria tecnica y se relaciona con ADM3, cuyo responsable completo es Fabian.

No traslado bloqueos antiguos sin comprobarlos. Por ejemplo, el DTO actual de Periodo ya contiene `PeriodoId`; no repito como vigente la observacion historica sobre su ausencia. No he reejecutado los arranques de Periodo/Direccion y no afirmo que fallen actualmente.

## Auditoria por persona y trazabilidad por HU

En las tablas enumero las familias de criterios que contrasto y lo que falta. Los pendientes requieren pruebas por criterio; no presento estas filas como casos finales aprobados.

### Persona 1 - Jose

| HU / fuente | Criterios revisados | Evidencia actual | Estado y cierre pendiente |
|---|---|---|---|
| ADM2 / PDF p.5 | Formulario email/dominio; login y tokens; error de credenciales; dashboard por perfil; salida; refresh automatico; bitacora de login/logout. | AuthController, SesionController, IdentidadSesionService y AuditoriaSesionMiddleware; suite .NET aprobada. | Parcial backend. No hay formulario ni ciclo de sesion web integrado; middleware audita login/refresh/sesion, no demuestra logout. Probar expiracion y limpieza de cliente. |
| ADM17 / PDF p.11 y subhistorias | Suma 100; reemplazo sin notas; bloqueo con notas; POST/PUT 1-100; vistas grupo/estudiante; estados vacios; permisos y bitacora. | NotasController/NotasService/AccesoNotas/AuditoriaNotas; 30 pruebas aprobadas. | Parcial backend. Falta UI completa, ejecucion real, persistencia de bitacora y snapshots del reemplazo de rubros. No dividir rubros/notas para declarar cierre parcial. |
| GTW1 / PDF pp.15-16 | Routing de APIs V1, prefijos externos, destinos, verbos, respuestas y errores. | Biblioteca Gateway.Seguridad; no encuentro host YARP/Ocelot ni tabla de rutas/destinos. | Pendiente de proxy ejecutable. El middleware no reenvia solicitudes por si mismo. GTW1 conserva el alcance completo de routing aunque algunas pantallas sean A3. |
| GTW2 / PDF p.16 y contratos | Interceptar rutas protegidas, /validate, 401 invalido/vencido; politica por rol/identidad; excepciones e internos. | SeguridadGatewayMiddleware; pruebas con HttpContext y clientes HTTP simulados. | Parcial, sin integracion a GTW1. Adjuntar politica explicita a cada ruta y ejecutar 401/403/503 y bloqueo de internos con el gateway real. Resolver referencia ambigua SRV7. |

Mi dictamen: hay desarrollo A2 comprobable y pruebas utiles, pero **0/4 HU cerradas acreditadas**. El bloqueo comun es la integracion del gateway y la interfaz. La autorizacion del dominio de notas es un avance concreto que no extrapolo a todos los servicios.

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

Las prioridades expresan impacto sobre el cierre A2, no una evaluacion de seguridad certificada. Distingo defectos observados por lectura de fallos reproducidos por ejecucion.

| ID | Prioridad | Hallazgo y fundamento | HU / responsable | Accion y evidencia de cierre |
|---|---|---|---|---|
| A2-01 | Critica | La web usa `Microsoft.NET.Sdk.Web`, `AddRazorPages` y `MapRazorPages`; PDF p.21 exige React o Blazor WebAssembly. [Proyecto web](../../../Usuario.ConsumoWeb/NuevoAvatar.Web/Usuario.ConsumoWeb.csproj), [Program](../../../Usuario.ConsumoWeb/NuevoAvatar.Web/Program.cs). | ADM1 / Alejandro; todas las pantallas A2 | Integrar la plantilla en tecnologia permitida o documentar excepcion expresa del profesor. Compilacion y recorridos en la tecnologia aprobada. |
| A2-02 | Critica | [Gateway.Seguridad](../../../NuevoAvatar.Gateway.Seguridad/NuevoAvatar.Gateway.Seguridad.csproj) es biblioteca; [middleware](../../../NuevoAvatar.Gateway.Seguridad/SeguridadGateway.cs) llama a next, no implementa proxy. No encuentro host YARP/Ocelot/rutas ni registro en lanzadores. | GTW1/GTW2 / Jose | Host unico, mapa de rutas/destinos/verbo/rol, ejecucion reproducible y respuestas reales por cada familia de APIs. |
| A2-03 | Alta | [site.js](../../../Usuario.ConsumoWeb/NuevoAvatar.Web/wwwroot/js/site.js) mantiene tres usuarios de ejemplo, modifica un arreglo, usa perfil fijo y muestra toast en paginacion y secciones de demostracion. No hay llamadas HTTP al backend en ese flujo. | ADM1/ADM4; resto de interfaces / responsables del backlog | Sustituir datos de demostracion por integracion real, listado persistido y estados de carga/vacio/error. Verificar recarga tras guardar y permisos. |
| A2-04 | Alta | [Modelo Modulo](../../../NuevoAvatar.Seguridad.Models/Modulo.cs), [Rol](../../../NuevoAvatar.Seguridad.Models/Rol.cs) y scripts solo contienen ID/nombre; no encuentro relacion de permisos ni estado/orden de modulo. | ADM5 Fabian, ADM7 Ramses, ADM1 Alejandro; GTW2 Jose | Definir contrato comun, cambio de datos aprobado, persistencia y autorizacion efectiva; demostrar menu diferente y rechazo de API con otro rol. |
| A2-05 | Alta | [ExpedienteController](../../../NuevoAvatar.Expediente/src/main/java/cr/ac/cuc/nuevoavatar/persona4/expediente/ExpedienteController.java) y [PrematriculaService](../../../NuevoAvatar.Prematricula/src/main/java/cr/ac/cuc/nuevoavatar/persona4/prematricula/PrematriculaService.java) usan IDs/body suministrados; sus interceptores validan token/sub, sin politica de pertenencia. /sesion por si solo no la aplica a esos CRUD. | WEB2/WEB4 / Alejandro; politica GTW2 / Jose | Contratos propios que deriven identidad de sesion, restrinjan contacto y rechacen IDs ajenos/campos protegidos. Probar dos estudiantes y estado sin cambios. Hallazgo estatico; no ataque HTTP ejecutado. |
| A2-06 | Alta | [PrematriculaRepository](../../../NuevoAvatar.Prematricula/src/main/java/cr/ac/cuc/nuevoavatar/persona4/prematricula/PrematriculaRepository.java) obtiene curso por ID/nivel, no carrera; servicio no compara curso-carrera ni duplicados entre solicitudes. [SQL](../../../db/por_servicio/Prematricula.sql) tampoco declara unicidad estudiante/curso/periodo. | WEB4 / Alejandro | Verificar relacion y duplicados en alta/edicion con proteccion transaccional apropiada. Rechazar curso de otra carrera, repeticion y colisiones sin cambios parciales. |
| A2-07 | Alta | [NotificacionService](../../../NuevoAvatar.Notificacion/src/main/java/cr/ac/cuc/nuevoavatar/persona5/notificacion/NotificacionService.java) envia HTML directamente; propiedades MAIL_*; [SQL](../../../db/por_servicio/Notificacion.sql) sin tablas. No encuentro sanitizacion, historial ni configuracion desde Parametro. | ADM22 / Ramses; dependencia ADM3 / Fabian | Implementar criterios V2 completos; probar HTML peligroso sanitizado, envio fallido/exitoso e historial real consumible por dashboard. |
| A2-08 | Alta | [NotasService.cargarDesglose](../../../NuevoAvatar.Notas/src/main/java/cr/ac/cuc/nuevoavatar/persona5/notas/NotasService.java) reemplaza rubros sin snapshots de auditoria. [BitacoraFilter](../../../NuevoAvatar.Notas/src/main/java/cr/ac/cuc/nuevoavatar/persona5/seguridad/BitacoraFilter.java) registra el request. AuditoriaNotas cubre escrituras de notas, no el anterior/actual del desglose. | ADM17 / Jose | Auditar resultado persistido y anterior/actual del reemplazo confirmado; verificar Bitacora real y rollback. |
| A2-09 | Alta | No localizo login web, perfil/cambio de contrasena ni portal de estudiante; sesion backend no implementa renovacion/redireccion/salida en cliente. | ADM2 Jose; ADM24/WEB1 Alejandro | Recorridos por perfil, tokens expirados, refresh fallido, logout y politica de contrasena parametrizada; sin credenciales en evidencia. |
| A2-10 | Alta | Faltan dashboard administrativo completo y pantallas de oferta/parametros/roles/modulos/correo/notas; el resumen de profesor solo cubre parte del backend. | ADM3 y mantenimientos / responsables del backlog | Criterios completos desde las interfaces; metricas con fixtures reales identificables, sin inventar valores. |
| A2-11 | Alta | Guia de pruebas y ficha tecnica no equivalen a matriz final ni capturas A2. README A2 enlaza `contexto-desarrollo.md` y `desarrollo-jose.md`, que no existen en el corte examinado. | Entrega / equipo | Publicar fichas por las 20 HU, evidencia por criterio, revision y version; corregir referencias. |
| A2-12 | Media | [Filtro Usuario](../../../NuevoAvatar.Usuario/src/main/java/cr/ac/cuc/nuevoavatar/persona2/seguridad/BitacoraFilter.java) registra request en PUT y solicitud en DELETE; no conserva estado anterior/eliminado. Oculta contrasenas, pero eso no completa el JSON requerido. | ADM4 / Fabian y otros consumidores del patron | Revisar filtros por dominio; persistir snapshots sin secretos y comprobar alta/cambio/baja/consulta/error. |
| A2-13 | Media | El cliente de Notas captura fallos de Bitacora y solo los registra en log; un negocio 2xx no demuestra auditoria remota persistida. | ADM17 / Jose; transversal | Acordar tratamiento de indisponibilidad y recuperacion de auditoria; demostrar persistencia y comportamiento ante caida. |
| A2-14 | Media | No localizo paquete A2 de analisis/diseno con diagramas, conclusiones/recomendaciones/bibliografia y pruebas completas. Tablero, aprobaciones BD y PR no comprobados. | Entrega / equipo | Reunir entregables aplicables a A2, conservar dependencias y pendientes A3; adjuntar enlaces verificables de revision/aprobacion. |

No elevo a defectos actuales los errores preliminares de compilacion por falta de restore o de JAR sin empaquetar: los resolvi en esta misma verificacion y documento el resultado final abajo. No convierto los nombres de roles normalizados de sesion en prueba de compatibilidad con todos los selectores: el backend Usuario usa `ADMIN`, mientras la sesion normaliza a `ADMINISTRADOR`; deben acordar ID y etiqueta sin enviar la etiqueta visual como ID arbitrario.

## Evidencia y limites de cobertura

Registro estructurado: [verificacion-avance1.json](verificacion-avance1.json). Comandos ejecutados desde la raiz del repositorio:

| Verificacion actual | Resultado | Alcance real |
|---|---|---|
| `dotnet test NuevoAvatar.Autenticacion.Tests/NuevoAvatar.Autenticacion.Tests.csproj --no-restore` | 25 aprobadas; 0 fallos/omitidas | Autenticacion, JWT, identidad, middleware y auditoria; dobles de dependencias. |
| `.\NuevoAvatar.Usuario\mvnw.cmd -B -q -o -f pom.xml -pl NuevoAvatar.Notas -am test` | 30 Notas + 3 integracion; 0 fallos/errores/omitidas | Mockito/MockMvc y configuracion local; no servicios reales ni SQL. |
| `dotnet restore Usuario.ConsumoWeb/NuevoAvatar.Web/Usuario.ConsumoWeb.csproj --ignore-failed-sources` y `dotnet build Usuario.ConsumoWeb/NuevoAvatar.Web/Usuario.ConsumoWeb.csproj --no-restore` | Build aprobado, 0 errores/advertencias | Compilacion del prototipo Razor; no valida tecnologia admitida ni comportamiento visual. Primer intento sin restore dio NETSDK1004 por assets ausentes. |
| `.\NuevoAvatar.Usuario\mvnw.cmd -B -q -o -f pom.xml -pl NuevoAvatar.Notas -am package -DskipTests` | Empaquetado aprobado | No vuelve a contar ni ejecutar pruebas; completa artefacto tras el comando test. |
| `python tools/validar-artefactos.py --servicio Notas` | Artefacto verificado | Antes de package indico falta de JAR unico; comprobacion final aprobada. |
| `python tools/auditar-limites.py` | 22 servicios, 290 fuentes, 0 infracciones detectadas | Busqueda estatica de SQL explicito/JPA; no valida roles, pertenencia ni integracion. Cubre infraestructura compartida; no suma HU V1/A3 al dictamen A2. |

El verificador de limites escribe por defecto el reporte historico V1. Conservo aqui el resultado actual y restauro ese archivo a su contenido previo para no modificar la evidencia del primer avance.

Desglose de las 30 pruebas Java de Notas: EscrituraNotasTest 9, NotasServiceTest 2, NotasHttpTest 6, ResumenProfesorTest 3, SeguridadNotasTest 8 y AuditoriaNotasTest 2. ConfiguracionLocalTest aporta otras 3. Los XML de Surefire quedan en `NuevoAvatar.Notas/target/surefire-reports` e `integracion/target/surefire-reports`; son salidas locales regenerables.

No sumo pruebas historicas de otros servicios, capturas V1 ni colecciones preparadas como ejecuciones A2. No he comprobado concurrencia SQL, persistencia remota de bitacoras, entrega SMTP, responsive en dispositivos, accesibilidad completa, aislamiento real entre estudiantes ni recuperacion de dependencias desde una web integrada. Esas limitaciones quedan abiertas aunque las 58 pruebas locales aprueben.

## Barras de progreso por persona

Conservo la seccion de progreso del formato original y uso exclusivamente **cierre integral acreditado**. No asigno porcentajes subjetivos de desarrollo ni notas academicas.

| Persona | HU cerradas acreditadas / comprometidas | Barra de cierre | Trabajo visible que reconozco |
|---|---:|---|---|
| Jose | 0/4 | `[....................]` | Backend A2 de sesion/seguridad/notas y pruebas locales. |
| Ramses | 0/6 | `[....................]` | Bases V1 de oferta/modulos/correo reutilizables; criterios V2 pendientes. |
| Alejandro | 0/5 | `[....................]` | Autoria de plantilla UI/UX; prototipo compilable y servicios V1 de apoyo. |
| Fabian | 0/5 | `[....................]` | Bases V1 de usuarios/roles/parametros/instituciones y dependencia de resumen profesor. |

Estas barras expresan ausencia de cierre demostrado, no cero esfuerzo ni autoria individual del backend historico. Los puntos estiman complejidad y no se usan para calcular calificacion.

## Avance del proyecto

Documento **20/20 HU revisadas en alcance**, **0/20 cierres integrales acreditados**, **58/58 pruebas locales aprobadas**, **1/1 build web aprobado** y **1/1 artefacto Notas finalmente verificado**. El alcance contiene **15 ADM, 2 GTW y 3 WEB**. Login y notas forman parte de esas 15 ADM; no los cuento de nuevo como componentes independientes.

No calculo porcentaje de implementacion global: los criterios no tienen ponderacion aprobada y la presencia de un servicio V1 o un prototipo no equivale a completar su historia V2. El objetivo de 120 puntos es compromiso de planificacion, no 120 puntos entregados. El PDF ubica A2 en semana 10 con valor academico 15%; no deduzco una nota de estos indicadores.

## Acciones que propongo

| Orden | Accion | Responsable segun backlog | Evidencia necesaria para cerrar |
|---:|---|---|---|
| 1 | Resolver tecnologia web y adaptar plantilla conservando su autoria | Alejandro / ADM1 | Proyecto React o Blazor WebAssembly ejecutable, o excepcion documentada; layout responsive probado. |
| 2 | Entregar gateway unico y mapa de rutas/politicas | Jose / GTW1-GTW2 | YARP/Ocelot en lanzador, routing de APIs y pruebas reales 401/403/503 e internos bloqueados. |
| 3 | Acordar permisos/menu/identidad y cambios de datos | Fabian ADM5, Ramses ADM7, Jose GTW2; Alejandro consume | Contrato y scripts aprobados, persistencia, menu por rol y API protegida. |
| 4 | Completar sesion y perfil en las webs | Jose ADM2, Alejandro ADM24 | Login por perfil, refresh, salida, cambio de contrasena parametrizado y bitacoras. |
| 5 | Proteger y completar contratos propios de expediente/prematricula | Alejandro WEB1/WEB2/WEB4 | Dos estudiantes, campos protegidos, curso-carrera, duplicados, CRUD propio, cascada territorial y fallos controlados. |
| 6 | Integrar mantenimientos administrativos y ampliar correo/modulos | Fabian y Ramses segun tablas | Persistencia real, filtros/orden/paginacion parametrizada, referencias, sanitizacion e historial de correo. |
| 7 | Completar notas, dashboards y auditoria de desglose | Jose ADM17, Fabian ADM3, Ramses ADM22 | Grupo autorizado, notas/rubros completos, antes/despues, metricas y notificaciones reales; rollback y dependencia caida. |
| 8 | Demostrar los seis recorridos del roadmap | Cada responsable y revisor | Interfaz -> gateway -> servicios reales, fixtures identificables, capturas por criterio y version. |
| 9 | Cerrar documentacion y trazabilidad | Equipo | Diagramas/analisis/diseno, scripts, guia A2, conclusiones/bibliografia, matrices, tablero y PR a rama final. |

No propongo agregar pantallas A3 ni repartir una historia entre personas para aparentar avance. Las correcciones de servicios V1 necesarias para un criterio A2 pertenecen a la HU V2 que las necesita.

## Mi dictamen final

Considero el segundo avance **en desarrollo y sin cierre integral demostrable**. Hay una base reutilizable importante, una plantilla de UI/UX reconocida y ampliaciones de backend respaldadas por 58 pruebas actuales aprobadas. La compilacion del prototipo y el empaquetado de Notas son reproducibles en este entorno.

Los bloqueos principales son la tecnologia web exigida, el gateway ejecutable, las interfaces integradas y la autorizacion propia del estudiante. Deben completarse tambien permisos persistidos, criterios V2 de correo, auditoria de rubros y evidencia por criterio. Estos resultados se limitan a las 20 HU acordadas para A2; la ausencia de funcionalidades reservadas a A3 no forma parte del incumplimiento que documento.
