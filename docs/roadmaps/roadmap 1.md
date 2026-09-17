# Roadmap 1 - Proyecto Nuevo Avatar V1

Documento basado en el PDF oficial `docs/requerimientos/Proyecto Nuevo Avatar V1.pdf`.

## Resumen del proyecto

El sistema corresponde a una administracion de academia compuesta por servicios REST para gestionar usuarios, roles, oferta academica, matricula, expediente estudiantil, notas, historial academico, facturacion, pagos, notificaciones y bitacoras.

Todas las historias, excepto el login inicial, dependen de autenticacion por token mediante `USR5` y deben registrar acciones importantes y errores tecnicos mediante el servicio de bitacora `GEN1`.

Fecha de entrega indicada en el documento oficial: **8 de octubre de 2026**.

## Stack recomendado

Para mantener consistencia entre las 4 personas, se recomienda usar un stack unico:

- Backend: Java 21 + Spring Boot 3.
- API REST: Spring Web, DTOs, controladores por modulo y documentacion con OpenAPI/Swagger.
- Seguridad: Spring Security + JWT + refresh token.
- Persistencia: PostgreSQL + Spring Data JPA/Hibernate.
- Migraciones BD: Flyway.
- Validaciones: Bean Validation/Jakarta Validation.
- Pruebas: JUnit 5, Mockito, Spring Boot Test y Postman/Insomnia para evidencia.
- DevOps local: Docker Compose para PostgreSQL y servicios.
- Control de codigo: Git con ramas por historia o feature, pull request hacia rama principal.

Alternativa aceptable: .NET 8 Web API + Entity Framework Core + PostgreSQL. Sin embargo, para el equipo se recomienda escoger una sola tecnologia para evitar diferencias en estructura, seguridad, pruebas y despliegue.

## Acuerdos tecnicos comunes

- Cada servicio debe cumplir estilo REST, usar JSON y responder con codigos HTTP correctos.
- Todos los endpoints protegidos deben validar token contra `/validate`.
- Las contrasenas se deben almacenar cifradas o hasheadas, nunca en texto plano.
- Los errores tecnicos y operaciones CRUD deben registrarse en bitacora.
- Los nombres que indiquen "solo letras y espacios" deben validarse desde DTO y servicio.
- Los campos requeridos no pueden aceptar `null`, vacio ni solo espacios.
- Cada historia debe incluir pruebas por criterio de aceptacion.
- Antes de iniciar implementacion completa, el equipo debe acordar el modelo de base de datos completo.

## Dependencias principales

| Historia | Depende de | Motivo |
|---|---|---|
| `GEN1` | `USR5` parcialmente | La bitacora requiere token, pero debe quedar disponible temprano para el resto. |
| `USR1` | `USR2`, `USR5`, `GEN1` | Usuario necesita rol, autenticacion y bitacora. |
| `USR2` | `USR5`, `GEN1` | Roles requiere autorizacion y bitacora. |
| `USR3` | `USR5`, `GEN1` | Parametros alimentan configuraciones como expiracion y dominios. |
| `USR4` | `USR5`, `GEN1` | Modulos requiere autorizacion y bitacora. |
| `ACD1` | `USR5`, `GEN1` | Instituciones requiere autorizacion y bitacora. |
| `ACD6` | `USR5`, `GEN1`, `USR3` | Profesores usa dominio de correo parametrizable. |
| `ACD2` | `ACD1`, `ACD6`, `USR5`, `GEN1` | Carrera pertenece a institucion y director debe ser profesor. |
| `ACD3` | `ACD2`, `USR5`, `GEN1` | Curso pertenece a carrera. |
| `ACD5` | `USR5`, `GEN1` | Periodos se usan en grupos, prematricula y matricula. |
| `ACD4` | `ACD3`, `ACD5`, `ACD6`, `USR5`, `GEN1` | Grupo requiere curso, periodo y profesor. |
| `MAT4` | `USR5`, `GEN1` | Direcciones se consumen desde expediente. |
| `MAT3` | `MAT4`, `USR3`, `USR5`, `GEN1` | Expediente necesita direcciones y dominio de correo estudiantil. |
| `MAT1` | `MAT3`, `ACD2`, `ACD3`, `ACD5`, `USR5`, `GEN1` | Prematricula requiere estudiante, carrera, cursos de primer nivel y periodo futuro. |
| `MAT2` | `MAT3`, `ACD3`, `ACD4`, `ACD5`, `USR5`, `GEN1` | Matricula requiere estudiante, curso, grupo y periodo activo. |
| `MAT5` | `MAT2`, `ACD4`, `USR5`, `GEN1` | Notas se cargan para estudiantes matriculados en grupos. |
| `ACA1` | `MAT5`, `MAT3`, `ACD3`, `USR5`, `GEN1` | Historial academico usa notas, estudiante y cursos. |
| `ACA2` | `MAT2`, `MAT3`, `ACD2`, `ACD3`, `ACD4`, `USR5`, `GEN1` | Listado depende de matricula y datos academicos. |
| `IPN1` | `MAT2`, `USR5`, `GEN1` | Factura nace a partir de la matricula. |
| `IPN2` | `IPN1`, `USR5`, `GEN1` | Pago cancela o revierte facturas. |
| `IPN3` | `USR3`, `USR5`, `GEN1` | Notificaciones usan parametros del servidor/cuenta de correo. |

## Distribucion por participante

### Persona 1 - Fundacion, seguridad, usuarios y bitacoras

Carga estimada: **alta**. Esta persona construye la base transversal del sistema, por lo que debe iniciar primero.

Historias asignadas:

| HU | Endpoint principal | Trabajo esperado |
|---|---|---|
| `USR5` | `/login`, `/refresh`, `/validate` | Login, JWT, refresh token, expiraciones parametrizables, respuestas `201`, `200` y `401`. |
| `GEN1` | `/bitacora` | Registro y consulta de bitacoras con fecha/hora actual, usuario y descripcion. |
| `USR2` | `/rol` | CRUD de roles, validacion de nombre con letras y espacios. |
| `USR1` | `/usuario` | CRUD y filtros por identificacion, nombre y tipo; validacion de email, dominio, rol y contrasena cifrada. |
| `USR3` | `/parametro` | CRUD de parametros, identificador maximo 10 caracteres en mayusculas y valor maximo 500 caracteres. |
| `USR4` | `/modulo` | CRUD de modulos, validacion de nombre. |

Requisitos clave:

- Definir estructura comun de respuesta y manejo de errores.
- Crear middleware/filtro de autenticacion reutilizable para endpoints protegidos.
- Publicar documentacion Swagger inicial.
- Dejar ejemplos de pruebas para que las otras personas copien el patron.
- Coordinar con todo el equipo los parametros necesarios: dominios de correo, expiracion JWT, expiracion refresh token y datos SMTP.

Entregables:

- Modelo BD inicial de usuarios, roles, parametros, modulos y bitacoras.
- Servicios REST protegidos.
- Pruebas unitarias e integracion basicas.
- Documentacion tecnica de seguridad y bitacora.

### Persona 2 - Oferta academica

Carga estimada: **media-alta**. Tiene muchas historias CRUD, pero con reglas relativamente claras.

Historias asignadas:

| HU | Endpoint principal | Trabajo esperado |
|---|---|---|
| `ACD1` | `/institucion` | CRUD de instituciones, validacion de nombre. |
| `ACD6` | `/profesor` | CRUD de profesores, mayoria de edad, email `cuc.ac.cr` parametrizable, telefonos. |
| `ACD2` | `/carrera` | CRUD de carreras, filtro por institucion, director registrado como profesor. |
| `ACD3` | `/curso` | CRUD de cursos, filtro por carrera, nivel entre 1 y 12. |
| `ACD5` | `/periodo` | CRUD de periodos con ano, numero, fecha inicio y fecha fin. |
| `ACD4` | `/grupo` | CRUD de grupos con numero, curso, profesor, horario, cupo y periodo. |

Requisitos clave:

- Implementar relaciones consistentes entre institucion, carrera, curso, profesor, periodo y grupo.
- Validar integridad referencial desde servicio y base de datos.
- Usar `GEN1` para registrar CRUD y errores tecnicos.
- Consumir/validar token con `USR5`.

Dependencias internas:

- `ACD1` y `ACD6` deben ir antes que `ACD2`.
- `ACD2` debe ir antes que `ACD3`.
- `ACD3`, `ACD5` y `ACD6` deben estar listos antes que `ACD4`.

Entregables:

- Modelo BD de oferta academica.
- Endpoints CRUD y consultas adicionales.
- Pruebas por validaciones de nombres, nivel, relaciones y autorizacion.
- Datos semilla recomendados para instituciones, carreras, profesores, cursos, periodos y grupos.

### Persona 3 - Matricula, expediente, direcciones y notas

Carga estimada: **alta**. Esta persona maneja procesos con mas reglas de negocio y dependencias.

Historias asignadas:

| HU | Endpoint principal | Trabajo esperado |
|---|---|---|
| `MAT4` | `/provincias`, `/cantones`, `/distritos` | Consulta de division territorial y validacion provincia-canton-distrito. |
| `MAT3` | `/expediente` | CRUD de estudiantes, direccion, telefonos, email `cuc.cr` parametrizable. |
| `MAT1` | `/prematricula` | Prematricular, modificar, eliminar y consultar; cursos de primer nivel y periodos futuros. |
| `MAT2` | `/matricula` | Matricular, modificar, eliminar y consultar estudiantes por curso/grupo. |
| `MAT5` | `/cargardesglose`, `/asignarnotarubro`, `/obtenerdesglose`, `/obtenernotas` | Gestion de rubros y notas; rubros suman 100, notas entre 1 y 100, bloqueo de rubros si ya hay notas. |

Requisitos clave:

- Definir bien entidades de estudiante, direccion, telefono, prematricula, matricula, desglose, rubro y nota.
- Validar fechas de periodos segun reglas del documento.
- Coordinar con Persona 2 para consumir cursos, grupos y periodos.
- Coordinar con Persona 4 porque facturacion, reportes e historial dependen de matricula/notas.

Dependencias internas:

- `MAT4` debe completarse antes de `MAT3`.
- `MAT3` y datos de Persona 2 deben existir antes de `MAT1` y `MAT2`.
- `MAT2` debe estar listo antes de `MAT5`.

Entregables:

- Modelo BD de estudiantes, direcciones, matriculas y notas.
- Endpoints de consulta territorial.
- Flujo probado de expediente -> prematricula -> matricula -> notas.
- Pruebas de reglas de negocio: periodo futuro/activo, curso primer nivel, sumatoria 100, bloqueo de rubros y rangos de nota.

### Persona 4 - Facturacion, pagos, notificaciones y consultas academicas

Carga estimada: **media-alta**. Trabaja integraciones funcionales y consultas transversales.

Historias asignadas:

| HU | Endpoint principal | Trabajo esperado |
|---|---|---|
| `IPN1` | `/factura` | Crear, reversar, consultar factura y listar facturacion por periodo; encabezado-detalle, impuesto 2%, estado pendiente. |
| `IPN2` | `/pago` | Crear pago, reversar pago, consultar pago y listar pagos por periodo; actualiza estado de factura. |
| `IPN3` | `/notificar` | Envio de correo con email, asunto y cuerpo HTML; configuracion SMTP parametrizable. |
| `ACA1` | `/historialacademico` | Consulta de cursos y promedios obtenidos por estudiante. |
| `ACA2` | `/listadoestudiantes` | Consulta de estudiantes matriculados por periodo con carrera, curso y grupo. |

Requisitos clave:

- Implementar patron encabezado-detalle en facturas.
- Mantener estados de factura: pendiente, pagada y anulada.
- Reversar pagos debe devolver la factura a pendiente.
- Para notificaciones, usar parametros configurables y permitir pruebas con proveedor SMTP de desarrollo.
- Las consultas academicas deben ser eficientes y no duplicar logica de matricula/notas.

Dependencias internas:

- `IPN1` debe completarse antes de `IPN2`.
- `ACA1` depende de notas cargadas por `MAT5`.
- `ACA2` depende de matriculas creadas por `MAT2`.
- `IPN3` puede desarrollarse en paralelo cuando `USR3` este listo.

Entregables:

- Modelo BD de facturas, detalle de factura y pagos.
- Servicios de consulta academica.
- Servicio de notificaciones configurable.
- Pruebas de estados de factura, pagos, reversos, impuesto y consultas por periodo.

## Orden sugerido de desarrollo

### Fase 1 - Base comun

Responsable principal: Persona 1.

- Crear estructura del proyecto, conexion BD, migraciones, manejo de errores, Swagger y pruebas base.
- Implementar `USR5`, `GEN1`, `USR2` y `USR3`.
- Acordar modelo de base de datos completo con el equipo.

### Fase 2 - Catalogos base

Responsables: Personas 1 y 2.

- Persona 1 completa `USR1` y `USR4`.
- Persona 2 implementa `ACD1`, `ACD6`, `ACD2`, `ACD3` y `ACD5`.
- Persona 3 puede iniciar `MAT4`.
- Persona 4 puede preparar estructura de facturas, pagos y notificaciones.

### Fase 3 - Procesos academicos y matricula

Responsables: Personas 2 y 3.

- Persona 2 completa `ACD4`.
- Persona 3 implementa `MAT3`, `MAT1`, `MAT2` y luego `MAT5`.
- Persona 4 inicia `IPN3` y prepara consultas `ACA1`/`ACA2`.

### Fase 4 - Integracion final

Responsables: Personas 3 y 4, con soporte de Persona 1.

- Persona 4 implementa `IPN1`, `IPN2`, `ACA1` y `ACA2`.
- Todo el equipo verifica autorizacion, bitacoras y pruebas tecnicas por criterio de aceptacion.
- Integrar documentacion final, scripts BD y evidencias.

## Checklist de calidad por historia

Cada persona debe entregar por historia:

- Endpoint documentado en Swagger.
- DTOs de entrada/salida.
- Validaciones de campos requeridos.
- Validaciones de reglas de negocio.
- Validacion de token.
- Registro de bitacora para crear, modificar, eliminar, consultar y errores tecnicos.
- Pruebas unitarias o de integracion.
- Evidencia de prueba tecnica por criterio de aceptacion.
- Script Flyway o SQL correspondiente.

## Riesgos y recomendaciones

- `USR5` y `GEN1` son bloqueantes para casi todo el proyecto; deben priorizarse.
- El modelo de base de datos debe acordarse antes de programar relaciones complejas.
- `MAT5`, `ACA1`, `ACA2`, `IPN1` e `IPN2` dependen de datos reales de matricula, grupos y periodos.
- Los dominios de correo y expiraciones no deben quedar quemados en codigo; deben salir de parametros o configuracion.
- La evidencia de pruebas debe mapearse contra cada criterio de aceptacion del PDF, no solo contra cada endpoint.
