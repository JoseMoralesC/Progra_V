# Contratos y dependencias del plan

Revision estatica de las fuentes activas del 23 de octubre de 2026. No es una
prueba de conectividad ni una implementacion de las ampliaciones descritas.
Se conserva la propiedad de datos de los 22 servicios y las tecnologias V1.

## Lo reutilizable no equivale a un portal listo

| Evidencia actual | Reutilizacion | Trabajo que debe incluir la HU V2 |
|---|---|---|
| [AuthController](../../../NuevoAvatar.Autenticacion/Controllers/AuthController.cs): login por headers, refresh por body y validate. [AuthService](../../../NuevoAvatar.Seguridad.DataAccess/AuthService.cs) emite JWT con sub. | Sesion y renovacion. | ADM2/GTW2: resolver perfil/rol e identidad de estudiante de forma confiable; /validate por si solo no prueba propiedad de un expediente. |
| [UsuarioController](../../../NuevoAvatar.Usuario/src/main/java/cr/ac/cuc/nuevoavatar/persona2/usuario/UsuarioController.java): consulta por email y CRUD/filtros generales. | Datos de cuenta y relacion con identificacion. | ADM4/ADM24/WEB2: contrato de cuenta propia y cambios limitados; no publicar el CRUD global al estudiante. |
| [AutorizacionInterceptor](../../../NuevoAvatar.Usuario/src/main/java/cr/ac/cuc/nuevoavatar/persona2/seguridad/AutorizacionInterceptor.java): valida token y extrae usuario. | Autenticacion. | GTW2 y cada dominio: autorizacion por rol/propiedad. El interceptor revisado no demuestra una matriz de permisos. |
| [PrematriculaController](../../../NuevoAvatar.Prematricula/src/main/java/cr/ac/cuc/nuevoavatar/persona4/prematricula/PrematriculaController.java): CRUD y listado general. | Reglas de MAT1 y persistencia. | WEB4: consulta y escritura propias con identidad verificada, filtros y rechazo de IDs ajenos. |
| [MatriculaController](../../../NuevoAvatar.Matricula/src/main/java/cr/ac/cuc/nuevoavatar/persona5/matricula/MatriculaController.java): listado por curso/grupo. | Inscripcion y consultas existentes. | WEB3/WEB5/MOB6: contrato para listar matriculas del estudiante por periodo; no descargar todos los estudiantes para filtrarlos en cliente. |
| [NotasController](../../../NuevoAvatar.Notas/src/main/java/cr/ac/cuc/nuevoavatar/persona5/notas/NotasController.java): consulta por estudiante/curso/grupo y operaciones de escritura. | MAT5 y rubros/notas. | ADM17/WEB6: separar permiso de profesor y lectura propia del estudiante, incluyendo comprobacion de grupo autorizado. |
| [HistorialAcademicoController](../../../NuevoAvatar.HistorialAcademico/src/main/java/cr/ac/cuc/nuevoavatar/persona4/academico/HistorialAcademicoController.java): consulta por tipo/identificacion. | Calculo de ACA1. | ADM18/WEB7: contrato de filtros por periodo/anio e intentos; validar identidad antes de consultar. |
| [Contratos internos](../../../integracion/contratos.json): recursos y datos de los propietarios. | Composicion entre servicios. | Endpoints publicos acotados a la necesidad del cliente. Las APIs internas y su clave no se entregan al navegador. |
| [NotificacionService](../../../NuevoAvatar.Notificacion/src/main/java/cr/ac/cuc/nuevoavatar/persona5/notificacion/NotificacionService.java): envio de HTML por SMTP. | Transporte de correo V1. | ADM22: sanitizacion, historial y configuracion requerida por V2; no asumir que el envio existente ya satisface esos criterios. |

## Entrega y responsable de los acuerdos

| Acuerdo | Cuando debe estar resuelto | Responsable principal | Consumidores y condicion |
|---|---|---|---|
| Rutas externas, verbos y errores | Inicio del avance 2 | Alejandro, GTW1 | Jose valida proteccion con GTW2; un solo gateway. |
| Sesion, rol e identidad Usuario-Expediente | Antes del primer flujo estudiantil A2 | Jose, ADM2/GTW2; Alejandro, ADM4/ADM5 | Fabian verifica WEB2/WEB4/ADM24; sin vinculo valido no se permite elegir otro expediente. |
| Permisos y menu persistidos | Antes de cerrar ADM1/ADM5/ADM7 | Alejandro (permisos), Ramses (modulos) | Jose integra el layout y la politica del gateway; Fabian verifica perfiles. |
| Campos editables del estudiante | A2, antes de WEB2/ADM24 | Fabian | Jose reutiliza el contrato en MOB3, sin otro desarrollo backend equivalente. |
| Prematricula propia y duplicados | A2, WEB4 | Fabian | Se reutiliza en ADM14/MOB4 del mismo responsable en A3; las interfaces y evidencia son nuevas. |
| Historial de correo y parametros | A2, ADM22 | Ramses; Alejandro provee contrato ADM6 | Fabian lo consume en ADM3-A y Jose en ADM3-P con permisos por rol. |
| Dashboards y notas | A2, ADM3/ADM17 | Fabian (ADM3-A/ADM17-R), Jose (ADM3-P/ADM17-N) | Contratos comunes y prueba conjunta de cada padre; fixtures V1 para notas/metricas. |
| Mis matriculas/cursos y filtros propios | Inicio de A3 | Ramses (WEB3), Alejandro (WEB5/MOB5-C), Fabian (MOB5-S) | Un contrato de consulta y otro de confirmacion; Alejandro gobierna escritura propia y validacion final. |
| Historial y notas propias | A3 antes de WEB6/WEB7/MOB6 | Ramses | Jose entrega las restricciones de escritura de notas en ADM17-N desde A2, coordinado con los rubros de Fabian. |
| Finanzas por estudiante | A3 antes de WEB8/MOB2 | Fabian | Jose consume el contrato para el resumen movil. |
| Sesion movil segura | A3, antes de cerrar MOB1 | Jose (MOB1-A), Alejandro (MOB1-S) | Contrato de entrega de tokens y eventos; probar login, restauracion y salida juntos. |
| Navegacion movil y destinos implicitos | Inicio de A3, antes de cerrar MOB0/MOB6 | Jose y Ramses | Resolver Pagos/Perfil/Notas/Historial; un tab sin destino no acredita cumplimiento. |

El responsable de cada unidad evaluable V2 coordina los cambios del servicio que necesita;
no depende de que vuelva una quinta persona de V1. Las cuatro HU descompuestas
conservan coordinador y contratos de integracion en [subhistorias](subhistorias.md). La autoria historica del
backend no transfiere automaticamente la responsabilidad evaluable.

## Decisiones que no deben ocultarse

- GTW2 menciona SRV1-SRV19 y SRV7 sin definicion equivalente en V2. Proponer
  login/refresh publicos y negocio protegido; consultar esa excepcion antes del
  cierre. No adoptar una ruta de negocio publica por adivinar que es SRV7.
- Identificacion y rol en el frontend no son controles suficientes. Probar
  acceso directo a API con otro rol, otro estudiante y otro grupo, ademas de 401.
  Usar 403/404 segun el contrato de no exposicion que acuerde el equipo.
- ADM13 no especifica limites detallados del CRUD de un profesor. Conservar
  su actor en el alcance y acordar permisos sobre grupos propios; no crear un
  portal nuevo de profesores ni conceder mantenimiento global por defecto.
- MAT2 usa periodo activo como fechaInicio <= hoy <= fechaFin en la
  implementacion; documentar esta interpretacion ante la redaccion ambigua de V1.
- La base de datos actual no debe darse por suficiente para permisos, orden de
  modulos, historial de envios, politica de contrasena o motivos de reverso.
  Toda ampliacion se vincula al criterio y sigue el proceso de cambio de BD del PDF.
- El PDF movil nombra Pagos/Perfil/Notas/Historial sin desglosar todas sus pantallas.
  El equipo debe especificar destinos y esfuerzo antes de comprometer A3. No se
  agregan pagos en linea ni pantallas completas sin estimarlas. Si cambia la carga,
  se revisan MOB0/MOB2/MOB6 y el balance del backlog.

## Registro de cambios de contrato

Para cada acuerdo se registra: HU, recurso propietario, operacion actual,
ampliacion necesaria, request/response, permisos, errores, migracion si aplica,
responsable, consumidores, version y pruebas. Las rutas propuestas se rotulan
como propuestas hasta su implementacion; no se inventa documentacion de APIs
ya disponibles. Las pruebas con dos estudiantes y un rol incorrecto son parte
del cierre de cada operacion propia, en web y movil.
