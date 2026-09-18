# Roadmap 1 - Proyecto Nuevo Avatar V1

Documento actualizado a partir del PDF oficial `docs/requerimientos/Proyecto Nuevo Avatar V1.pdf`.

Este roadmap reorganiza el trabajo para **5 personas** y **3 fases**, tomando como base exclusiva las historias de usuario del PDF. No se agregan historias nuevas ni se eliminan historias existentes.

## Principio de alcance

Las historias de usuario son la base del proyecto. Cada tarea tecnica debe poder rastrearse a una HU del PDF:

- Usuarios y roles: `USR1`, `USR2`, `USR3`, `USR4`, `USR5`.
- Oferta academica: `ACD1`, `ACD2`, `ACD3`, `ACD4`, `ACD5`, `ACD6`.
- Matricula: `MAT1`, `MAT2`, `MAT3`, `MAT4`, `MAT5`.
- Academico: `ACA1`, `ACA2`.
- Integracion de pagos y notificaciones: `IPN1`, `IPN2`, `IPN3`.
- General: `GEN1`.

Total: **24 historias de usuario**.

## Enfoque de la Fase 1

La primera fase corresponde al primer alcance indicado por el profesor. Se entrega y se prueba como backend/API, sin interfaz grafica.

El entregable de fase 1 debe incluir:

- Base de datos completa del sistema, modelada desde las HU.
- Script de base de datos versionado y ejecutable.
- Microservicios REST para las HU asignadas.
- Endpoints protegidos con token cuando la HU lo pide.
- Contratos JSON de request/response.
- Validaciones de criterios de aceptacion.
- Bitacoras por acciones CRUD, consultas relevantes y errores tecnicos, usando `GEN1`.
- Coleccion Postman o evidencia equivalente para probar cada endpoint.
- Documentacion de pruebas tecnicas: cada criterio de aceptacion debe tener evidencia.

No se debe entregar frontend, UI, pantallas web ni interfaz grafica en fase 1. Las pruebas se hacen desde Postman.

## Base de datos y ambiente remoto

La base de datos ya existe como infraestructura remota y se accede mediante Tailscale. Sobre esa infraestructura se debe crear la BD completa del sistema o, si el equipo lo justifica, varias BD separadas por dominio.

Recomendacion principal: usar una sola base de datos de integracion, por ejemplo `NuevoAvatar_Integracion`, con esquemas por dominio:

- `seguridad`: usuarios, roles, parametros, modulos, tokens.
- `general`: bitacoras.
- `academico`: instituciones, carreras, cursos, grupos, periodos, profesores.
- `matricula`: estudiantes, direcciones, prematriculas, matriculas, notas.
- `finanzas`: facturas, detalles y pagos.
- `notificaciones`: configuracion/logs de notificacion si aplica.

Si se usan varias bases de datos, debe quedar claro como se mantienen las relaciones entre dominios y como se prueban los flujos desde Postman. No se deben partir datos que necesitan integridad referencial fuerte sin una razon tecnica.

### Reglas de BD

- Todo cambio estructural debe quedar en scripts SQL versionados.
- No hacer cambios manuales en SSMS que no queden documentados en script.
- Mantener llaves primarias, llaves foraneas, restricciones `NOT NULL`, unicidad e indices necesarios.
- Los datos requeridos por el PDF no deben permitir `NULL`, vacio ni solo espacios.
- Las contrasenas deben almacenarse encriptadas o hasheadas, nunca en texto plano.
- Los dominios de correo, expiracion de JWT, expiracion de refresh token y datos de correo deben ser parametrizables.
- La BD de integracion no debe depender de creacion automatica por ORM; el backend debe validar contra el modelo aprobado.

## Stack recomendado

El PDF permite escoger tecnologia siempre que se respeten principios de microservicios y se mantenga la integridad de la informacion.

Para mantener consistencia, se recomienda:

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

- Todos los servicios deben exponer REST y consumir/producir JSON.
- Los endpoints deben usar codigos HTTP correctos.
- Todas las operaciones protegidas deben validar el token contra `/validate`, segun `USR5`.
- Cada historia debe documentar request, response, errores esperados y casos de prueba Postman.
- Cada accion importante debe registrar bitacora usando `GEN1`.
- Las consultas tambien registran bitacora segun el PDF: "El usuario consulta <elemento>".
- En creaciones, la descripcion de bitacora incluye JSON del nuevo registro.
- En actualizaciones, la descripcion de bitacora incluye JSON anterior y JSON actual.
- En eliminaciones, la descripcion de bitacora incluye JSON eliminado.
- Los errores tecnicos tambien deben registrarse.

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

La division busca balancear carga, dependencias y responsabilidad individual. Cada persona es responsable de sus HU completas: tablas, scripts, contratos JSON, endpoints, validaciones, bitacoras y pruebas Postman.

### Persona 1 - Seguridad, parametros, roles y bitacora

Carga estimada: alta, porque desbloquea al resto del equipo.

| HU | Endpoint principal | Alcance |
|---|---|---|
| `USR5` | `/login`, `/refresh`, `/validate` | Login, JWT, refresh token, expiraciones parametrizables, respuestas `201`, `200` y `401`. |
| `GEN1` | `/bitacora` | Registro y consulta de bitacoras con usuario, descripcion y fecha/hora actual. |
| `USR2` | `/rol` | CRUD de roles, datos requeridos y nombre solo con letras y espacios. |
| `USR3` | `/parametro` | CRUD de parametros, identificador maximo 10 caracteres en mayusculas y valor maximo 500 caracteres. |
| `USR4` | `/modulo` | CRUD de modulos, datos requeridos y nombre solo con letras y espacios. |

Responsabilidades adicionales:

- Definir el contrato comun de autenticacion.
- Entregar datos semilla minimos para roles, parametros y usuario administrador.
- Definir formato comun de errores y estructura base de respuestas.
- Coordinar que los demas servicios puedan validar token y registrar bitacoras.

### Persona 2 - Usuarios, profesores, instituciones y carreras

Carga estimada: media-alta, con relaciones importantes entre usuarios y oferta academica.

| HU | Endpoint principal | Alcance |
|---|---|---|
| `USR1` | `/usuario` | CRUD de usuarios, filtros por identificacion/nombre/tipo, email, rol y contrasena encriptada. |
| `ACD1` | `/institucion` | CRUD de instituciones, nombre requerido y solo letras/espacios. |
| `ACD6` | `/profesor` | CRUD de profesores, mayoria de edad, telefonos y email `cuc.ac.cr` parametrizable. |
| `ACD2` | `/carrera` | CRUD de carreras, consulta por institucion y director registrado como profesor. |

Responsabilidades adicionales:

- Alinear roles de usuario con dominios `cuc.cr` y `cuc.ac.cr`.
- Coordinar con Persona 3 para que cursos tengan carreras disponibles.
- Entregar datos semilla de instituciones, profesores y carreras.

### Persona 3 - Cursos, periodos, grupos y direcciones

Carga estimada: media-alta, porque cierra la oferta academica y deja bases para matricula.

| HU | Endpoint principal | Alcance |
|---|---|---|
| `ACD3` | `/curso` | CRUD de cursos, consulta por carrera, nivel entre 1 y 12. |
| `ACD5` | `/periodo` | CRUD de periodos con anio, numero, fecha inicio y fecha fin. |
| `ACD4` | `/grupo` | CRUD de grupos con curso, profesor, horario, cupo y periodo. |
| `MAT4` | `/provincias`, `/cantones`, `/distritos` | Consultas territoriales y validacion provincia-canton-distrito. |

Responsabilidades adicionales:

- Entregar datos semilla de cursos, periodos, grupos y division territorial.
- Coordinar con Persona 4 para que expediente use direcciones.
- Coordinar con Persona 5 para que matricula tenga cursos, grupos y periodos listos.

### Persona 4 - Expedientes, prematricula y consultas academicas

Carga estimada: alta, porque toca datos de estudiantes y consultas academicas.

| HU | Endpoint principal | Alcance |
|---|---|---|
| `MAT3` | `/expediente` | CRUD de estudiantes, direccion, telefonos y email `cuc.cr` parametrizable. |
| `MAT1` | `/prematricula` | Prematricular, modificar, eliminar y consultar; cursos de primer nivel y periodos futuros. |
| `ACA1` | `/historialacademico` | Promedios de notas obtenidos por estudiante. |
| `ACA2` | `/listadoestudiantes` | Estudiantes matriculados en un periodo con carrera, curso y grupo. |

Responsabilidades adicionales:

- Coordinar con Persona 3 para validar provincia/canton/distrito.
- Coordinar con Persona 5 para que `ACA1` consuma notas y `ACA2` consuma matricula.
- Entregar datos semilla de estudiantes y prematriculas.

### Persona 5 - Matricula, notas, facturacion, pagos y notificaciones

Carga estimada: alta, porque contiene los procesos mas transaccionales.

| HU | Endpoint principal | Alcance |
|---|---|---|
| `MAT2` | `/matricula` | Matricular, modificar, eliminar y consultar estudiantes por curso/grupo. |
| `MAT5` | `/cargardesglose`, `/asignarnotarubro`, `/obtenerdesglose`, `/obtenernotas` | Rubros, notas, sumatoria 100, notas entre 1 y 100 y bloqueo si ya hay notas. |
| `IPN1` | `/factura` | Crear, reversar, consultar factura y listar facturacion por periodo; encabezado-detalle, impuesto 2%, estado pendiente. |
| `IPN2` | `/pago` | Crear pago, reversar pago, consultar pago y listar pagos por periodo; actualiza estado de factura. |
| `IPN3` | `/notificar` | Envio de correo con email, asunto y cuerpo HTML; datos SMTP parametrizables. |

Responsabilidades adicionales:

- Coordinar con Persona 4 para estudiantes y prematricula.
- Coordinar con Persona 3 para grupos, cursos y periodos.
- Entregar datos semilla para matriculas, desglose de rubros, facturas y pagos.

## Fases del proyecto

### Fase 1 - Base de datos, contratos y microservicios REST

Objetivo: entregar al profesor la base de datos completa y los servicios REST de las HU, probados desde Postman, sin frontend.

Trabajo comun:

- Confirmar conexion remota por Tailscale a SQL Server.
- Definir si se usara una BD o varias BD.
- Crear modelo completo de datos basado en las 24 HU.
- Crear scripts SQL de estructura, restricciones, indices y datos semilla.
- Definir contratos JSON por endpoint.
- Implementar servicios REST por HU asignada.
- Validar token con `USR5` en todas las operaciones protegidas.
- Registrar bitacoras con `GEN1`.
- Crear coleccion Postman por persona y una coleccion integrada del equipo.
- Documentar evidencia por criterio de aceptacion.

Orden sugerido dentro de la fase:

1. Persona 1 implementa `USR5`, `USR2`, `USR3` y base de `GEN1`.
2. Persona 2 implementa `USR1`, `ACD1` y `ACD6`.
3. Persona 3 implementa `ACD5`, `MAT4`, `ACD3` y luego `ACD4`.
4. Persona 2 completa `ACD2` cuando existan instituciones y profesores.
5. Persona 4 implementa `MAT3` y `MAT1`.
6. Persona 5 implementa `MAT2`, `MAT5`, `IPN1`, `IPN2` e `IPN3`.
7. Persona 4 completa `ACA1` y `ACA2` cuando existan matriculas y notas.
8. Todo el equipo ejecuta pruebas integradas desde Postman.

Entregables de fase 1:

- Diagrama de base de datos completo.
- Script de base de datos completo.
- Codigo fuente de servicios REST.
- Colecciones Postman.
- Evidencia de pruebas tecnicas por criterio de aceptacion.
- Historias de usuario actualizadas en la herramienta elegida.
- Pull request hacia la rama principal.

Fecha indicada por el PDF para el primer alcance: **8 de octubre de 2026**.

### Fase 2 - Integracion, consistencia y endurecimiento tecnico

Objetivo: estabilizar lo construido en fase 1 sin agregar historias nuevas.

Trabajo comun:

- Ejecutar flujos completos entre HU:
  - Login -> rol/usuario -> bitacora.
  - Institucion -> profesor -> carrera -> curso -> periodo -> grupo.
  - Direcciones -> expediente -> prematricula -> matricula -> notas.
  - Matricula -> factura -> pago/reverso.
  - Matricula/notas -> historial academico/listado de estudiantes.
- Revisar que todas las operaciones protegidas validen token.
- Revisar que todas las acciones importantes y errores tecnicos registren bitacora.
- Validar integridad referencial en BD y servicios.
- Normalizar respuestas de error y codigos HTTP.
- Probar casos negativos: datos vacios, dominios invalidos, token invalido, relaciones inexistentes, rangos invalidos.
- Ajustar indices o consultas cuando una HU lo necesite.

Entregables de fase 2:

- Coleccion Postman integrada y ordenada por flujos.
- Matriz de dependencias HU vs endpoints.
- Evidencia de pruebas positivas y negativas.
- Scripts correctivos de BD si fueron necesarios.
- Version estabilizada de servicios.

### Fase 3 - Cierre documental y entrega final

Objetivo: cerrar la entrega con trazabilidad entre PDF, HU, BD, servicios y pruebas.

Trabajo comun:

- Completar documentacion de analisis y diseno enfocada en las HU.
- Incluir portada, introduccion, diagrama de BD, casos de uso, clases, pruebas tecnicas, conclusiones, recomendaciones y bibliografia.
- Revisar que cada HU tenga endpoints, contratos JSON, pruebas y evidencia.
- Revisar que no existan historias inventadas ni historias omitidas.
- Preparar scripts finales para recrear la BD.
- Preparar guia de ejecucion local/remota y variables de ambiente.
- Validar que el profesor pueda probar todo desde Postman contra los servicios.

Entregables de fase 3:

- Documento final.
- Script final de BD.
- Codigo fuente final.
- Coleccion Postman final.
- Evidencias finales por criterio de aceptacion.
- Pull request final o merge aprobado hacia rama principal.

## Checklist por historia

Cada HU debe quedar cerrada solo cuando tenga:

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
| Persona 1 | `USR5`, `GEN1`, `USR2`, `USR3`, `USR4` | 5 |
| Persona 2 | `USR1`, `ACD1`, `ACD6`, `ACD2` | 4 |
| Persona 3 | `ACD3`, `ACD5`, `ACD4`, `MAT4` | 4 |
| Persona 4 | `MAT3`, `MAT1`, `ACA1`, `ACA2` | 4 |
| Persona 5 | `MAT2`, `MAT5`, `IPN1`, `IPN2`, `IPN3` | 5 |

## Riesgos y controles

| Riesgo | Control |
|---|---|
| `USR5` y `GEN1` bloquean al resto | Persona 1 debe priorizarlas al inicio de fase 1. |
| Modelo de BD incompleto | No iniciar implementacion profunda sin diagrama y script base acordados. |
| Cambios manuales en BD remota | Todo cambio debe pasar por script versionado. |
| Historias implementadas sin evidencia | Cada criterio del PDF debe tener prueba Postman documentada. |
| Diferencias de formato entre servicios | Usar contratos JSON comunes y ejemplos compartidos. |
| Dependencias cruzadas entre personas | Trabajar con datos semilla tempranos y endpoints mockeados solo de forma temporal. |
| Errores no auditados | Centralizar manejo de excepciones y registrar errores tecnicos en `GEN1`. |
| Parametros quemados en codigo | Usar `USR3` o variables de ambiente para dominios, expiraciones y correo. |

## Nota final

Este roadmap no cambia el alcance funcional del PDF. Solo reorganiza la ejecucion para 5 personas, 3 fases y una primera entrega orientada a base de datos, microservicios, contratos JSON, endpoints y pruebas desde Postman.
