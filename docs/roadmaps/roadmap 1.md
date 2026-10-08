# Roadmap 1 - Proyecto Nuevo Avatar V1

En este roadmap organizo el trabajo de **5 personas** para completar el **primer avance**. El avance se divide en tres etapas internas de trabajo, tomando como base exclusiva las historias de usuario del PDF. No se agregan historias nuevas ni se eliminan historias existentes.

## Principio de alcance

Tomo las historias de usuario como base del proyecto y relaciono cada tarea tecnica con una HU del PDF:

- Usuarios y roles: `USR1`, `USR2`, `USR3`, `USR4`, `USR5`.
- Oferta academica: `ACD1`, `ACD2`, `ACD3`, `ACD4`, `ACD5`, `ACD6`.
- Matricula: `MAT1`, `MAT2`, `MAT3`, `MAT4`, `MAT5`.
- Academico: `ACA1`, `ACA2`.
- Integracion de pagos y notificaciones: `IPN1`, `IPN2`, `IPN3`.
- General: `GEN1`.

Total: **24 historias de usuario**.

## Alcance del primer avance

Organizo el primer avance segun el alcance indicado por el profesor: planteo la entrega y las pruebas de base de datos y backend/API, sin interfaz grafica ni aplicacion cliente.

Incluyo estos elementos en la planificacion del primer avance:

- Base de datos completa del sistema, modelada desde las HU.
- Diagrama completo del modelo de base de datos y sus relaciones.
- Script de base de datos versionado y ejecutable.
- Conexion documentada entre los servicios y SQL Server por medio de Tailscale.
- Microservicios REST para las HU asignadas.
- Consumos entre servicios requeridos por las HU, especialmente `/validate` y `/bitacora`.
- Endpoints protegidos con token cuando la HU lo pide.
- Contratos JSON de request/response.
- Validaciones de criterios de aceptacion.
- Bitacoras por acciones CRUD, consultas relevantes y errores tecnicos, usando `GEN1`.
- Coleccion Postman o evidencia equivalente para probar cada endpoint.
- Documentacion de pruebas tecnicas: cada criterio de aceptacion debe tener evidencia.

### Fuera del alcance

Dejo fuera del alcance de este avance:

- Frontend o interfaz grafica.
- Aplicacion web, movil o de escritorio para consumir los servicios.
- Pantallas, formularios o navegacion visual.
- Pruebas ejecutadas desde una UI.

Planifico las pruebas funcionales directamente contra los endpoints REST mediante Postman.

## Base de datos y ambiente remoto

Considero la infraestructura remota de SQL Server accesible mediante Tailscale como punto de partida. Sobre ella planifico la BD completa del sistema o varias BD por dominio si el equipo justifica esa decision.

Propongo usar una sola base de datos de integracion, por ejemplo `NuevoAvatar_Integracion`, con esquemas por dominio:

- `seguridad`: usuarios, roles, parametros, modulos, tokens.
- `general`: bitacoras.
- `academico`: instituciones, carreras, cursos, grupos, periodos, profesores.
- `matricula`: estudiantes, direcciones, prematriculas, matriculas, notas.
- `finanzas`: facturas, detalles y pagos.
- `notificaciones`: configuracion/logs de notificacion si aplica.

Si optamos por varias bases, documento como mantendremos las relaciones entre dominios y probaremos los flujos desde Postman. Evito separar datos que necesitan integridad referencial fuerte sin una razon tecnica.

### Reglas de BD

- Documento cada cambio estructural en scripts SQL versionados.
- Evito cambios manuales en SSMS que no queden documentados en un script.
- Mantengo llaves primarias, llaves foraneas, restricciones `NOT NULL`, unicidad e indices necesarios.
- Valido que los datos requeridos por el PDF no permitan `NULL`, vacio ni solo espacios.
- Planteo almacenar las contrasenas encriptadas o hasheadas, nunca en texto plano.
- Planteo parametrizar los dominios de correo, las expiraciones de JWT y refresh token, y los datos de correo.
- Administro la BD de integracion con scripts y planteo que el backend valide el modelo aprobado, sin creacion automatica por ORM.

## Stack que propongo

Tomo como condicion del PDF respetar los principios de microservicios y mantener la integridad de la informacion al escoger la tecnologia.

Para mantener consistencia, propongo:

- Backend: Java 21 + Spring Boot 3.
- API REST: Spring Web, DTOs, controladores por modulo y OpenAPI/Swagger.
- Seguridad: Spring Security + JWT + refresh token.
- Persistencia: SQL Server + Spring Data JPA/Hibernate + `mssql-jdbc`.
- BD remota: SQL Server accesible por Tailscale.
- Administracion BD: SSMS.
- Migraciones: scripts SQL versionados; Flyway solo si el equipo lo confirma.
- Pruebas tecnicas: Postman.
- Pruebas automatizadas: JUnit 5, Mockito y Spring Boot Test cuando aplique.
- Control de codigo: Git con ramas por HU o feature y pull request hacia la rama principal.

## Acuerdos comunes de servicios

- Planteo que todos los servicios expongan REST y consuman/produzcan JSON.
- Configuro la conexion a SQL Server mediante variables de ambiente o perfiles locales y mantengo usuarios, contrasenas y secretos fuera del control de versiones.
- Defino codigos HTTP correctos para los endpoints.
- Planteo validar el token contra `/validate` en todas las operaciones protegidas, segun `USR5`.
- Planteo consumos entre servicios con URLs configurables y documento sus dependencias, sin fijar direcciones IP o puertos en el codigo.
- Documento para cada historia request, response, errores esperados y casos de prueba Postman.
- Planteo registrar cada accion importante en bitacora mediante `GEN1`.
- Incluyo el registro de consultas en bitacora segun el PDF: "El usuario consulta <elemento>".
- Incluyo el JSON nuevo en la descripcion de bitacora de las creaciones.
- Incluyo el JSON anterior y actual en la descripcion de bitacora de las actualizaciones.
- Incluyo el JSON eliminado en la descripcion de bitacora de las eliminaciones.
- Incluyo el registro de errores tecnicos.

## Dependencias principales entre HU

| HU | Depende de | Motivo |
|---|---|---|
| `USR5` | `USR1` para usuarios reales | Autentica usuarios y emite/valida tokens. |
| `GEN1` | `USR5` parcialmente | La HU pide token, pero el resto necesita bitacora desde temprano. |
| `USR1` | `USR2`, `USR5`, `GEN1` | Usuario necesita rol, autenticacion y bitacora. |
| `USR2` | `USR5`, `GEN1` | Roles requiere autorizacion y bitacora. |
| `USR3` | `USR5`, `GEN1` | Parametros alimentan expiraciones, dominios y correo. |
| `USR4` | `USR5`, `GEN1` | Modulos requiere autorizacion y bitacora. |
| `ACD1` | `USR5`, `GEN1` | Instituciones requiere autorizacion y bitacora. |
| `ACD6` | `USR3`, `USR5`, `GEN1` | Profesores usa dominio `cuc.ac.cr` parametrizable. |
| `ACD2` | `ACD1`, `ACD6`, `USR5`, `GEN1` | Carrera pertenece a institucion y director debe ser profesor. |
| `ACD3` | `ACD2`, `USR5`, `GEN1` | Curso pertenece a carrera. |
| `ACD5` | `USR5`, `GEN1` | Periodos se usan en grupos, prematricula y matricula. |
| `ACD4` | `ACD3`, `ACD5`, `ACD6`, `USR5`, `GEN1` | Grupo requiere curso, periodo y profesor. |
| `MAT4` | `USR5`, `GEN1` | Direcciones se consumen desde expediente. |
| `MAT3` | `MAT4`, `USR3`, `USR5`, `GEN1` | Expediente necesita direcciones y dominio `cuc.cr` parametrizable. |
| `MAT1` | `MAT3`, `ACD2`, `ACD3`, `ACD5`, `USR5`, `GEN1` | Prematricula requiere estudiante, carrera, cursos de primer nivel y periodo futuro. |
| `MAT2` | `MAT3`, `ACD3`, `ACD4`, `ACD5`, `USR5`, `GEN1` | Matricula requiere estudiante, curso, grupo y periodo activo. |
| `MAT5` | `MAT2`, `ACD4`, `USR5`, `GEN1` | Notas se cargan para estudiantes matriculados en grupos. |
| `ACA1` | `MAT5`, `MAT3`, `ACD3`, `USR5`, `GEN1` | Historial academico usa notas, estudiante y cursos. |
| `ACA2` | `MAT2`, `MAT3`, `ACD2`, `ACD3`, `ACD4`, `USR5`, `GEN1` | Listado depende de matricula y datos academicos. |
| `IPN1` | `MAT2`, `USR5`, `GEN1` | Factura nace a partir de matricula. |
| `IPN2` | `IPN1`, `USR5`, `GEN1` | Pago cancela o revierte facturas. |
| `IPN3` | `USR3`, `USR5`, `GEN1` | Notificaciones usan parametros SMTP/correo. |

## Distribucion por persona

Con esta division busco balancear carga, dependencias y responsabilidad individual. Asigno a cada persona sus HU completas: tablas, scripts, contratos JSON, endpoints, validaciones, bitacoras y pruebas Postman.

### Persona 1 (Hector) - Seguridad, parametros, roles y bitacora

Carga estimada: alta, porque desbloquea al resto del equipo.

| HU | Endpoint principal | Alcance |
|---|---|---|
| `USR5` | `/login`, `/refresh`, `/validate` | Login, JWT, refresh token, expiraciones parametrizables, respuestas `201`, `200` y `401`. |
| `GEN1` | `/bitacora` | Registro y consulta de bitacoras con usuario, descripcion y fecha/hora actual. |
| `USR2` | `/rol` | CRUD de roles, datos requeridos y nombre solo con letras y espacios. |
| `USR3` | `/parametro` | CRUD de parametros, identificador maximo 10 caracteres en mayusculas y valor maximo 500 caracteres. |
| `USR4` | `/modulo` | CRUD de modulos, datos requeridos y nombre solo con letras y espacios. |

Responsabilidades adicionales que incluyo en la planificacion:

- Me propongo definir el contrato comun de autenticacion.
- Me propongo entregar datos semilla minimos para roles, parametros y usuario administrador.
- Me propongo definir formato comun de errores y estructura base de respuestas.
- Me propongo coordinar que los demas servicios puedan validar token y registrar bitacoras.

### Persona 2 (Ramses) - Usuarios, profesores, instituciones y carreras

Carga estimada: media-alta, con relaciones importantes entre usuarios y oferta academica.

| HU | Endpoint principal | Alcance |
|---|---|---|
| `USR1` | `/usuario` | CRUD de usuarios, filtros por identificacion/nombre/tipo, email, rol y contrasena encriptada. |
| `ACD1` | `/institucion` | CRUD de instituciones, nombre requerido y solo letras/espacios. |
| `ACD6` | `/profesor` | CRUD de profesores, mayoria de edad, telefonos y email `cuc.ac.cr` parametrizable. |
| `ACD2` | `/carrera` | CRUD de carreras, consulta por institucion y director registrado como profesor. |

Responsabilidades adicionales que incluyo en la planificacion:

- Me propongo alinear roles de usuario con dominios `cuc.cr` y `cuc.ac.cr`.
- Me propongo coordinar con Persona 3 (Alejandro) para que cursos tengan carreras disponibles.
- Me propongo entregar datos semilla de instituciones, profesores y carreras.

### Persona 3 (Alejandro) - Cursos, periodos, grupos y direcciones

Carga estimada: media-alta, porque cierra la oferta academica y deja bases para matricula.

| HU | Endpoint principal | Alcance |
|---|---|---|
| `ACD3` | `/curso` | CRUD de cursos, consulta por carrera, nivel entre 1 y 12. |
| `ACD5` | `/periodo` | CRUD de periodos con anio, numero, fecha inicio y fecha fin. |
| `ACD4` | `/grupo` | CRUD de grupos con curso, profesor, horario, cupo y periodo. |
| `MAT4` | `/provincias`, `/cantones`, `/distritos` | Consultas territoriales y validacion provincia-canton-distrito. |

Responsabilidades adicionales que incluyo en la planificacion:

- Me propongo entregar datos semilla de cursos, periodos, grupos y division territorial.
- Me propongo coordinar con Persona 4 (Fabian) para que expediente use direcciones.
- Me propongo coordinar con Persona 5 (Jose) para que matricula tenga cursos, grupos y periodos listos.

### Persona 4 (Fabian) - Expedientes, prematricula y consultas academicas

Carga estimada: alta, porque toca datos de estudiantes y consultas academicas.

| HU | Endpoint principal | Alcance |
|---|---|---|
| `MAT3` | `/expediente` | CRUD de estudiantes, direccion, telefonos y email `cuc.cr` parametrizable. |
| `MAT1` | `/prematricula` | Prematricular, modificar, eliminar y consultar; cursos de primer nivel y periodos futuros. |
| `ACA1` | `/historialacademico` | Promedios de notas obtenidos por estudiante. |
| `ACA2` | `/listadoestudiantes` | Estudiantes matriculados en un periodo con carrera, curso y grupo. |

Responsabilidades adicionales que incluyo en la planificacion:

- Me propongo coordinar con Persona 3 (Alejandro) para validar provincia/canton/distrito.
- Me propongo coordinar con Persona 5 (Jose) para que `ACA1` consuma notas y `ACA2` consuma matricula.
- Me propongo entregar datos semilla de estudiantes y prematriculas.

### Persona 5 (Jose) - Matricula, notas, facturacion, pagos y notificaciones

Carga estimada: alta, porque contiene los procesos mas transaccionales.

| HU | Endpoint principal | Alcance |
|---|---|---|
| `MAT2` | `/matricula` | Matricular, modificar, eliminar y consultar estudiantes por curso/grupo. |
| `MAT5` | `/cargardesglose`, `/asignarnotarubro`, `/obtenerdesglose`, `/obtenernotas` | Rubros, notas, sumatoria 100, notas entre 1 y 100 y bloqueo si ya hay notas. |
| `IPN1` | `/factura` | Crear, reversar, consultar factura y listar facturacion por periodo; encabezado-detalle, impuesto 2%, estado pendiente. |
| `IPN2` | `/pago` | Crear pago, reversar pago, consultar pago y listar pagos por periodo; actualiza estado de factura. |
| `IPN3` | `/notificar` | Envio de correo con email, asunto y cuerpo HTML; datos SMTP parametrizables. |

Responsabilidades adicionales que incluyo en la planificacion:

- Me propongo coordinar con Persona 4 (Fabian) para estudiantes y prematricula.
- Me propongo coordinar con Persona 3 (Alejandro) para grupos, cursos y periodos.
- Me propongo entregar datos semilla para matriculas, desglose de rubros, facturas y pagos.

## Etapas del primer avance

Organizo estas etapas como partes del mismo primer entregable, sin plantear una aplicacion con UI ni entregas funcionales independientes.

### Etapa 1 - Base de datos, conexiones y servicios REST

Mi objetivo es entregar al profesor la base de datos completa y los servicios REST de las HU, probados desde Postman, sin frontend.

Trabajo que planifico con el equipo:

- Me propongo confirmar conexion remota por Tailscale a SQL Server.
- Me propongo definir si se usara una BD o varias BD.
- Me propongo crear modelo completo de datos basado en las 24 HU.
- Me propongo crear scripts SQL de estructura, restricciones, indices y datos semilla.
- Me propongo configurar y comprobar la conexion de cada servicio con la BD remota sin publicar credenciales en Git.
- Me propongo definir contratos JSON por endpoint.
- Me propongo implementar servicios REST por HU asignada.
- Me propongo validar token con `USR5` en todas las operaciones protegidas.
- Me propongo registrar bitacoras con `GEN1`.
- Me propongo probar los consumos servicio-a-servicio requeridos por las HU.
- Me propongo crear coleccion Postman por persona y una coleccion integrada del equipo.
- Me propongo documentar evidencia por criterio de aceptacion.

Orden que propongo dentro de la etapa:

1. Persona 1 (Hector) implementa `USR5`, `USR2`, `USR3` y base de `GEN1`.
2. Persona 2 (Ramses) implementa `USR1`, `ACD1` y `ACD6`.
3. Persona 3 (Alejandro) implementa `ACD5`, `MAT4`, `ACD3` y luego `ACD4`.
4. Persona 2 (Ramses) completa `ACD2` cuando existan instituciones y profesores.
5. Persona 4 (Fabian) implementa `MAT3` y `MAT1`.
6. Persona 5 (Jose) implementa `MAT2`, `MAT5`, `IPN1`, `IPN2` e `IPN3`.
7. Persona 4 (Fabian) completa `ACA1` y `ACA2` cuando existan matriculas y notas.
8. Todo el equipo ejecuta pruebas integradas desde Postman.

Resultados que espero de la etapa 1:

- Diagrama de base de datos completo.
- Script de base de datos completo.
- Codigo fuente de servicios REST.
- Colecciones Postman.
- Conexion funcional de los servicios con SQL Server.
- Consumos funcionales de autenticacion y bitacora.

### Etapa 2 - Integracion, consistencia y pruebas tecnicas

Mi objetivo es integrar y estabilizar lo construido en la etapa 1, sin agregar historias nuevas ni interfaces graficas.

Trabajo que planifico con el equipo:

- Me propongo ejecutar flujos completos entre HU:
  - Login -> rol/usuario -> bitacora.
  - Institucion -> profesor -> carrera -> curso -> periodo -> grupo.
  - Direcciones -> expediente -> prematricula -> matricula -> notas.
  - Matricula -> factura -> pago/reverso.
  - Matricula/notas -> historial academico/listado de estudiantes.
- Me propongo revisar que todas las operaciones protegidas validen token.
- Me propongo revisar que todas las acciones importantes y errores tecnicos registren bitacora.
- Me propongo validar integridad referencial en BD y servicios.
- Me propongo normalizar respuestas de error y codigos HTTP.
- Me propongo probar casos negativos: datos vacios, dominios invalidos, token invalido, relaciones inexistentes, rangos invalidos.
- Me propongo ajustar indices o consultas cuando una HU lo necesite.

Resultados que espero de la etapa 2:

- Coleccion Postman integrada y ordenada por flujos.
- Matriz de dependencias HU vs endpoints.
- Evidencia de pruebas positivas y negativas.
- Scripts correctivos de BD si fueron necesarios.
- Version estabilizada de servicios.

### Etapa 3 - Documentacion y cierre del primer avance

Mi objetivo es cerrar la entrega con trazabilidad entre PDF, HU, BD, servicios y pruebas.

Trabajo que planifico con el equipo:

- Me propongo completar documentacion de analisis y diseno enfocada en las HU.
- Me propongo incluir portada, introduccion, diagrama de BD, casos de uso, clases, pruebas tecnicas, conclusiones, recomendaciones y bibliografia.
- Me propongo revisar que cada HU tenga endpoints, contratos JSON, pruebas y evidencia.
- Me propongo revisar que no existan historias inventadas ni historias omitidas.
- Me propongo preparar scripts finales para recrear la BD.
- Me propongo preparar guia de ejecucion local/remota y variables de ambiente.
- Me propongo validar que el profesor pueda probar todo desde Postman contra los servicios.

Entregables que planifico para el primer avance:

- Me propongo documento de analisis y diseno con portada, introduccion, diagrama completo de BD, casos de uso, clases, pruebas tecnicas, conclusiones, recomendaciones y bibliografia.
- Me propongo script versionado para crear la base de datos, sus restricciones y datos semilla.
- Me propongo codigo fuente de los servicios REST y configuracion documentada de conexion.
- Me propongo coleccion Postman integrada, con variables de ambiente y orden de ejecucion.
- Me propongo evidencia de una prueba exitosa por cada criterio de aceptacion del PDF.
- Me propongo historias de usuario actualizadas en la herramienta elegida.
- Me propongo pull request hacia la rama principal o rama final acordada.

Fecha indicada por el PDF para el primer alcance: **8 de octubre de 2026**.

## Checklist por historia

Considero una HU cerrada solo cuando cuenta con:

- Tabla(s) o estructura de BD correspondiente.
- Script SQL versionado.
- Endpoint(s) REST segun el PDF.
- Request JSON documentado, cuando aplique.
- Response JSON documentado.
- Validaciones de campos requeridos.
- Validaciones de reglas de negocio.
- Validacion de token contra `/validate`, cuando aplique.
- Registro de bitacora por CRUD, consultas y errores tecnicos.
- Prueba Postman por cada criterio de aceptacion.
- Evidencia documentada de ejecucion exitosa.

## Historias por persona

| Persona | Historias | Total |
|---|---|---:|
| Persona 1 (Hector) | `USR5`, `GEN1`, `USR2`, `USR3`, `USR4` | 5 |
| Persona 2 (Ramses) | `USR1`, `ACD1`, `ACD6`, `ACD2` | 4 |
| Persona 3 (Alejandro) | `ACD3`, `ACD5`, `ACD4`, `MAT4` | 4 |
| Persona 4 (Fabian) | `MAT3`, `MAT1`, `ACA1`, `ACA2` | 4 |
| Persona 5 (Jose) | `MAT2`, `MAT5`, `IPN1`, `IPN2`, `IPN3` | 5 |

## Riesgos y controles

| Riesgo | Control |
|---|---|
| `USR5` y `GEN1` bloquean al resto | Persona 1 (Hector) debe priorizarlas al inicio de la etapa 1. |
| Modelo de BD incompleto | No iniciar implementacion profunda sin diagrama y script base acordados. |
| Cambios manuales en BD remota | Todo cambio debe pasar por script versionado. |
| Historias implementadas sin evidencia | Cada criterio del PDF debe tener prueba Postman documentada. |
| Diferencias de formato entre servicios | Usar contratos JSON comunes y ejemplos compartidos. |
| Dependencias cruzadas entre personas | Trabajar con datos semilla tempranos y endpoints mockeados solo de forma temporal. |
| Errores no auditados | Centralizar manejo de excepciones y registrar errores tecnicos en `GEN1`. |
| Parametros quemados en codigo | Usar `USR3` o variables de ambiente para dominios, expiraciones y correo. |

## Nota final

En este roadmap mantengo el alcance funcional del PDF y organizo el primer avance para cinco personas y tres etapas internas. Centro la entrega en base de datos, conexiones, servicios REST, consumos entre servicios y pruebas directas desde Postman, sin incluir UI ni aplicacion cliente.
