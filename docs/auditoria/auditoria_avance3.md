# Auditoria de avance 3 - Proyecto Nuevo Avatar V1

Fecha de auditoria: 4 de octubre de 2026  
Base principal: `docs/requerimientos/Proyecto Nuevo Avatar V1.pdf`  
Bases complementarias: `docs/roadmaps/roadmap 1.md`, `docs/roadmaps/propuesta.md`, `docs/auditoria/auditoria_avance2.md`, `docs/pruebas/RESUMEN_PERSONA4.md`  

## Resumen

El proyecto presenta un avance importante respecto a la auditoria de avance 2. El cambio mas relevante es que ahora existe `services/persona4` con implementacion visible para `MAT3`, `MAT1`, `ACA1` y `ACA2`, ademas de colecciones Postman y evidencia tecnica asociada. En esta auditoria se ejecuto la suite de Persona 4 y paso: 251 pruebas, 0 fallos, 0 errores y 0 omitidas.

Persona 5 se mantiene estable y verificada: sus 14 pruebas automatizadas siguen pasando. Seguridad tambien se verifico con `dotnet test --no-restore` y termino con codigo 0.

Persona 2 conserva un avance funcional amplio, pero sigue sin quedar verde en pruebas completas: la suite ejecuta 9 pruebas, con 8 exitosas y 1 error en `contextLoads` por conexion/metadata de SQL Server usando el usuario `db70273`. Este riesgo ya venia de la auditoria 2.

Persona 3 mantiene los avances de servicios separados en .NET para Curso, Grupo, Periodo y Direccion, pero los cuatro builds siguen bloqueados por una fuente NuGet local inexistente. Ademas, `NuevoAvatar.Periodo` y `NuevoAvatar.Direccion` continuan con `GrupoAuthorizationMiddleware` vacio aunque se registra en `Program.cs`, por lo que no deben considerarse listos sin correccion o verificacion adicional.

El avance global sube de forma clara por Persona 4, pero el proyecto aun no esta cerrado al 100% contra el PDF: falta evidencia de pruebas tecnicas por cada criterio de aceptacion en formato final con pantallazos, falta diagrama completo de base de datos, falta resolver la verificacion de Persona 2, falta compilar/verificar los servicios .NET nuevos y falta una validacion integrada real entre todos los servicios.

## Alcance auditado

Se revisaron los siguientes elementos del repositorio:

- `docs/requerimientos/Proyecto Nuevo Avatar V1.pdf`
- `docs/roadmaps/roadmap 1.md`
- `docs/roadmaps/propuesta.md`
- `docs/auditoria/auditoria_avance1.md`
- `docs/auditoria/auditoria_avance2.md`
- `docs/pruebas/persona1-persona2.md`
- `docs/pruebas/RESUMEN_PERSONA4.md`
- `docs/pruebas/postman`
- `docs/pruebas/evidencias/2026-10-04`
- `services/seguridad`
- `services/persona2`
- `services/persona4`
- `services/persona5`
- `NuevoAvatar.Curso`
- `NuevoAvatar.Grupo`
- `NuevoAvatar.Periodo`
- `NuevoAvatar.Direccion`
- `db/persona2/sql`
- `db/persona4`
- `db/persona5/migraciones`

Estado Git observado durante la auditoria:

- No habia cambios pendientes al iniciar la revision.
- Se creo este documento como nueva evidencia de auditoria.

## Verificacion ejecutada

| Verificacion | Resultado | Observacion |
|---|---|---|
| Extraccion del PDF oficial con `pypdf` | Exitosa | Se confirmaron los criterios de aceptacion y el total visible de HU del PDF. |
| Busqueda de endpoints REST | Exitosa | Se ubicaron endpoints en Seguridad, Persona 2, Persona 4, Persona 5 y servicios .NET nuevos. |
| `dotnet test services/seguridad/NuevoAvatarSeguridad.sln --no-restore` | Exitoso | El comando termino con codigo 0. |
| `services/persona4`: `.\mvnw.cmd test` | Exitoso | 251 pruebas, 0 fallos, 0 errores, 0 omitidas. Usa H2 y dependencias externas simuladas. |
| `services/persona5`: `..\persona2\mvnw.cmd -f pom.xml test` | Exitoso | 14 pruebas, 0 fallos, 0 errores, 0 omitidas. |
| `services/persona2`: `.\mvnw.cmd test` | Fallido | 9 pruebas ejecutadas, 1 error. Falla `contextLoads` por conexion/metadata SQL Server con usuario `db70273`. |
| `dotnet build` de `NuevoAvatar.Curso`, `Grupo`, `Periodo`, `Direccion` | Fallido por ambiente | NuGet intenta usar `C:\Program Files (x86)\Microsoft SDKs\NuGetPackages\`, ruta local inexistente. |
| Revision de `RESUMEN_PERSONA4.md` y evidencias | Exitosa | Documenta 251 pruebas, 37 peticiones HTTP y migraciones aplicadas en PrograV. |
| Revision de colecciones Postman nuevas | Exitosa | Existen colecciones para `MAT3`, `MAT1`, `ACA1` y `ACA2`. No sustituyen pantallazos finales por criterio del PDF. |

Nota: la suite de Persona 4 si fue ejecutada en esta auditoria. Las pruebas usan H2 y servicios externos simulados, por lo que demuestran reglas y contratos locales, pero no certifican por si solas la integracion real con `USR5`, `USR3`, `GEN1`, `MAT4`, `MAT2` y `MAT5` en ambiente compartido.

## Criterios generales del PDF y roadmap

| Criterio | Estado | Observacion |
|---|---|---|
| Base de datos versionada | En progreso | Hay scripts base en `db/persona2/sql`, migraciones de Persona 4 en `db/persona4` y migraciones de Persona 5 en `db/persona5/migraciones`. |
| Diagrama completo de BD | No verificado | No se encontro archivo de diagrama completo en el repositorio auditado. |
| Servicios REST por HU | En progreso alto | Hay servicios para Seguridad, Persona 2, Persona 4, Persona 5 y Persona 3 en .NET. |
| Validacion por token `/validate` | Parcialmente cubierto | Seguridad existe; Persona 2/4/5 la consumen. Curso usa filtro y Grupo usa middleware. Periodo/Direccion tienen middleware vacio. |
| Registro de bitacora `/bitacora` | Parcialmente cubierto | Hay integracion transversal, pero falta evidencia completa con JSON nuevo/anterior/actual/eliminado en todos los CRUD. |
| Contratos JSON | Parcialmente cubierto | DTOs y colecciones Postman existen para varias HU, pero falta documentacion consolidada de request/response por endpoint. |
| Pruebas automatizadas | Parcialmente cubierto | Persona 4 y Persona 5 pasan. Seguridad termina con codigo 0. Persona 2 falla por BD. No hay pruebas automatizadas visibles para servicios .NET nuevos de Persona 3. |
| Evidencia Postman | En progreso | Hay colecciones nuevas para Persona 4 y evidencia para Persona 1/2. Falta coleccion integrada final y pantallazos por criterio de aceptacion. |
| Historias de usuario actualizadas | No verificado | No se encontro evidencia de herramienta Scrum/GitLab/Azure DevOps en el repositorio. |

## Comparacion contra auditoria de avance 2

| Area | Estado en avance 2 | Estado en avance 3 | Cambio |
|---|---|---|---|
| Persona 4 | No encontrada | Implementada en `services/persona4` con endpoints, migraciones, pruebas, Postman y evidencia | Mejora critica |
| `MAT3` expediente | No encontrado | Existe `/expediente` con CRUD, validaciones, direcciones, telefonos, dominio y pruebas | Mejora critica |
| `MAT1` prematricula | No encontrado | Existe `/prematricula` con CRUD, cursos de primer nivel, periodo futuro y pruebas | Mejora critica |
| `ACA1` historial academico | No encontrado | Existe `/historialacademico` y consume notas de `MAT5` | Mejora importante |
| `ACA2` listado estudiantes | No encontrado | Existe `/listadoestudiantes` y consume matriculas de `MAT2` | Mejora importante |
| Persona 5 | Implementada y pruebas pasan | Sigue implementada y pruebas pasan | Se mantiene fuerte |
| Persona 2 | Pruebas fallan por conexion SQL Server | Sigue fallando por conexion/metadata SQL Server | Riesgo persiste |
| Persona 3 .NET | Servicios existentes, builds bloqueados | Servicios siguen existentes, builds siguen bloqueados | Sin cierre tecnico |
| Middleware Periodo/Direccion | Vacio y registrado | Sigue vacio y registrado | Riesgo persiste |
| Evidencia Postman | Parcial | Hay nuevas colecciones de Persona 4, pero falta integrada final | Mejora parcial |
| Diagrama de BD | No verificado | Sigue no verificado | Sin avance visible |

## Auditoria por persona

### Persona 1 - Hector

Responsabilidad segun roadmap:

| HU | Endpoint | Alcance esperado | Estado |
|---|---|---|---|
| `USR5` | `/login`, `/refresh`, `/validate` | Login, JWT, refresh token, expiraciones parametrizables | Implementado |
| `GEN1` | `/bitacora` | Registro y consulta de bitacoras | Implementado |
| `USR2` | `/rol` | CRUD de roles | Implementado |
| `USR3` | `/parametro` | CRUD de parametros | Implementado |
| `USR4` | `/modulo` | CRUD de modulos | Implementado |

Evidencia encontrada:

- Servicio .NET en `services/seguridad`.
- Controladores para `/login`, `/refresh`, `/validate`, `/rol`, `/parametro`, `/modulo` y `/bitacora`.
- Pruebas de autenticacion en `services/seguridad/NuevoAvatarSeguridad.Tests`.
- `dotnet test services/seguridad/NuevoAvatarSeguridad.sln --no-restore` termino con codigo 0.
- Script SQL de seguridad y bitacora en `db/persona2/sql/separado/01_seguridad_general.sql`.

Riesgos o pendientes:

- Falta confirmar evidencia Postman completa por cada criterio de aceptacion del PDF.
- Falta verificar si bitacora registra exactamente JSON nuevo, anterior/actual y eliminado en todos los CRUD.
- La ubicacion SQL de seguridad sigue dentro de `db/persona2/sql`.

Dictamen: avance alto y estable.

### Persona 2 - Ramses

Responsabilidad segun roadmap:

| HU | Endpoint | Alcance esperado | Estado |
|---|---|---|---|
| `USR1` | `/usuario` | CRUD de usuarios, filtros, BCrypt, rol y dominio | Implementado |
| `ACD1` | `/institucion` | CRUD de instituciones | Implementado |
| `ACD6` | `/profesor` | CRUD de profesores, mayoria de edad, telefonos y dominio parametrizable | Implementado |
| `ACD2` | `/carrera` | CRUD de carreras, filtro por institucion y director profesor | Implementado |

Evidencia encontrada:

- Servicio Java/Spring Boot en `services/persona2`.
- Controladores para `/usuario`, `/institucion`, `/profesor`, `/carrera`, `/curso`, `/periodo` y `/grupo`.
- DTOs, servicios, repositorios, validaciones y manejo global de errores.
- Integracion con seguridad para `/validate` y `/bitacora`.
- Documentacion tecnica en `services/persona2/README.md`.
- Al ejecutar pruebas, 8 de 9 pruebas pasan.

Riesgos o pendientes:

- La prueba completa de Persona 2 sigue fallando: 9 pruebas ejecutadas, 1 error, causado por login/conexion/metadata de SQL Server con usuario `db70273`.
- `services/persona2` sigue mezclando HU de Persona 3 (`ACD3`, `ACD4`, `ACD5`) aunque tambien existen servicios .NET separados.
- Falta evidencia Postman detallada por cada criterio de aceptacion.
- Falta confirmar datos semilla completos de instituciones, profesores y carreras.

Dictamen: avance funcional alto, pero con riesgo actual de verificacion por base de datos.

### Persona 3 - Alejandro

Responsabilidad segun roadmap:

| HU | Endpoint | Alcance esperado | Estado |
|---|---|---|---|
| `ACD3` | `/curso` | CRUD de cursos, filtro por carrera, nivel 1 a 12 | Implementado en `services/persona2` y tambien en `NuevoAvatar.Curso` |
| `ACD5` | `/periodo` | CRUD de periodos con anio, numero y fechas | Implementado en `services/persona2` y tambien en `NuevoAvatar.Periodo` |
| `ACD4` | `/grupo` | CRUD de grupos con curso, profesor, horario, cupo y periodo | Implementado en `services/persona2` y tambien en `NuevoAvatar.Grupo` |
| `MAT4` | `/provincias`, `/cantones`, `/distritos` | Consultas territoriales y validacion jerarquica | Implementado en `NuevoAvatar.Direccion`, con riesgo de middleware/build |

Evidencia encontrada:

- `NuevoAvatar.Curso` expone endpoints de `/curso`, `/curso/{id}` y `/curso/carrera/{carreraId}`.
- `NuevoAvatar.Curso` valida carrera, nivel 1 a 12 y nombre solo con letras/espacios.
- `NuevoAvatar.Grupo` expone CRUD de `/grupo` y contiene middleware real de autorizacion.
- `NuevoAvatar.Periodo` expone CRUD de `/periodo`.
- `NuevoAvatar.Direccion` expone `/provincias`, `/cantones/{provinciaId}` y `/distritos/{provinciaId}/{cantonId}`.
- `DireccionRepository` valida jerarquia provincia-canton para distritos mediante join contra `matricula.Canton`.
- Scripts territoriales existen en `db/persona2/sql/separado/03_matricula_catalogos.sql`.

Riesgos o pendientes:

- `NuevoAvatar.Periodo` y `NuevoAvatar.Direccion` registran `UseMiddleware<GrupoAuthorizationMiddleware>()`, pero la clase `GrupoAuthorizationMiddleware` esta vacia en esos proyectos.
- No se pudo compilar ninguno de los servicios .NET nuevos por problema de fuente NuGet local inexistente.
- No se encontraron pruebas automatizadas para `NuevoAvatar.Curso`, `Grupo`, `Periodo` o `Direccion`.
- Falta evidencia Postman de `MAT4` y de los CRUD separados.
- La coexistencia de implementaciones Java y .NET para `curso`, `periodo` y `grupo` puede causar confusion de fuente oficial si no se define cual se entrega.

Dictamen: avance medio-alto en codigo, pero aun no verificable como entrega cerrada por build, middleware y falta de pruebas.

### Persona 4 - Fabian

Responsabilidad segun roadmap:

| HU | Endpoint | Alcance esperado | Estado |
|---|---|---|---|
| `MAT3` | `/expediente` | CRUD de estudiantes, direccion, telefonos y email `cuc.cr` parametrizable | Implementado |
| `MAT1` | `/prematricula` | Prematricular, modificar, eliminar y consultar | Implementado |
| `ACA1` | `/historialacademico` | Promedios de notas por estudiante | Implementado |
| `ACA2` | `/listadoestudiantes` | Estudiantes matriculados por periodo con carrera, curso y grupo | Implementado |

Evidencia encontrada:

- Servicio Java/Spring Boot en `services/persona4`.
- Controladores para `/expediente`, `/prematricula`, `/historialacademico` y `/listadoestudiantes`.
- `ExpedienteValidador` valida datos requeridos, formato por Bean Validation y dominio estudiantil obtenido desde `USR3` mediante `SeguridadClient`.
- `PrematriculaValidador` valida cursos de primer nivel y periodo con fecha de inicio posterior a la fecha actual.
- `HistorialAcademicoService` consulta intentos del estudiante, consume notas de `MAT5` y calcula acumulado ponderado.
- `ListadoEstudiantesService` cruza grupos del periodo con matriculas activas de `MAT2` y expedientes locales.
- Suite ejecutada: 251 pruebas, 0 fallos, 0 errores, 0 omitidas.
- Evidencia documental en `docs/pruebas/RESUMEN_PERSONA4.md`.
- Evidencia JSON indica 37 peticiones HTTP correctas para MAT3, MAT1 y ACA2 con SQL real local antes de incorporar ACA1.
- Migraciones en `db/persona4` y resumen PrograV: 4 tablas nuevas, 9 relaciones nuevas, 16 relaciones anteriores verificadas y 0 errores de ejecucion.
- Colecciones Postman para `MAT3`, `MAT1`, `ACA1` y `ACA2`.

Riesgos o pendientes:

- Las 251 pruebas usan H2 y servicios externos simulados; aun falta validar integracion real con los servicios del equipo.
- La evidencia HTTP con SQL real documenta MAT3, MAT1 y ACA2; el propio resumen indica que fue ejecutada antes de incorporar ACA1.
- Falta ejecutar las colecciones Postman contra ambiente integrado real y guardar pantallazos por cada criterio de aceptacion.
- `ACA1` depende de que `MAT5` devuelva datos consistentes por estudiante/curso/grupo; si el contrato real no distingue intentos ambiguos, el servicio responde 503.
- Falta confirmar datos semilla compartidos para direcciones, oferta academica, estudiantes, prematriculas, matriculas y notas.

Dictamen: avance alto y principal mejora frente a la auditoria 2; requiere integracion real y evidencia final de entrega.

### Persona 5 - Jose

Responsabilidad segun roadmap:

| HU | Endpoint | Alcance esperado | Estado |
|---|---|---|---|
| `MAT2` | `/matricula` | Matricular, modificar, eliminar y consultar por curso/grupo | Implementado |
| `MAT5` | `/cargardesglose`, `/asignarnotarubro`, `/obtenerdesglose`, `/obtenernotas` | Rubros, suma 100, notas 1-100 y bloqueo si ya hay notas | Implementado |
| `IPN1` | `/factura` | Crear, reversar, consultar y listar facturas por periodo | Implementado |
| `IPN2` | `/pago` | Crear pago, reversar pago, consultar y listar pagos por periodo | Implementado |
| `IPN3` | `/notificar` | Envio de correo con email, asunto, cuerpo HTML y SMTP parametrizable | Implementado |

Evidencia encontrada:

- Servicio Java/Spring Boot en `services/persona5`.
- Migraciones separadas en `db/persona5/migraciones`.
- Controladores, DTOs, servicios, repositorios y entidades por modulo.
- Seguridad transversal con validacion de token y registro de bitacora.
- Configuracion SMTP por variables de ambiente.
- Pruebas automatizadas ejecutadas en esta auditoria: 14 pruebas, 0 fallos, 0 errores, 0 omitidas.

Riesgos o pendientes:

- `MAT2` ya puede integrarse con Persona 4, pero falta evidencia de validacion integrada real expediente -> prematricula -> matricula.
- Falta evidencia Postman por cada criterio de aceptacion.
- `IPN3` usa variables de ambiente para SMTP; el PDF pide datos parametrizables. Se debe aclarar si se aceptara variable de ambiente o si debe integrarse con `USR3`.

Dictamen: avance muy alto y verificado con pruebas automatizadas.

## Trazabilidad global por HU

| Area | HU | Estado |
|---|---|---|
| Seguridad | `USR5` | Implementado |
| Seguridad | `GEN1` | Implementado |
| Seguridad | `USR2` | Implementado |
| Seguridad | `USR3` | Implementado |
| Seguridad | `USR4` | Implementado |
| Usuarios | `USR1` | Implementado, prueba actual afectada por BD |
| Oferta academica | `ACD1` | Implementado |
| Oferta academica | `ACD2` | Implementado |
| Oferta academica | `ACD3` | Implementado en Java y .NET |
| Oferta academica | `ACD4` | Implementado en Java y .NET |
| Oferta academica | `ACD5` | Implementado en Java y .NET, middleware .NET pendiente |
| Oferta academica | `ACD6` | Implementado |
| Matricula | `MAT4` | Implementado en codigo, pendiente validar ejecucion por middleware/build |
| Matricula | `MAT3` | Implementado y probado localmente |
| Matricula | `MAT1` | Implementado y probado localmente |
| Matricula | `MAT2` | Implementado |
| Matricula | `MAT5` | Implementado |
| Academico | `ACA1` | Implementado y probado localmente |
| Academico | `ACA2` | Implementado y probado localmente |
| Finanzas/notificaciones | `IPN1` | Implementado |
| Finanzas/notificaciones | `IPN2` | Implementado |
| Finanzas/notificaciones | `IPN3` | Implementado |

Nota: el PDF oficial contiene 22 codigos de HU visibles: 5 USR, 6 ACD, 5 MAT, 2 ACA, 3 IPN y 1 GEN. El roadmap interno indica 24 historias; este desfase documental sigue pendiente de aclaracion.

## Barras de progreso por persona

Las barras mezclan implementacion visible, scripts SQL, pruebas, evidencia y capacidad de verificacion. No representan aprobacion final del profesor; son una lectura tecnica del repositorio contra el PDF y la documentacion interna.

| Persona | Responsable | Progreso | Barra |
|---|---|---:|---|
| Persona 1 | Hector | 90% | `[##################--]` |
| Persona 2 | Ramses | 80% | `[################----]` |
| Persona 3 | Alejandro | 75% | `[###############-----]` |
| Persona 4 | Fabian | 85% | `[#################---]` |
| Persona 5 | Jose | 92% | `[##################--]` |

## Avance estimado del proyecto

Progreso global estimado: 84%

Barra global:

`[#################---]`

El porcentaje sube respecto a avance 2 principalmente porque Persona 4 ya existe, tiene endpoints, migraciones, colecciones Postman y una suite automatizada amplia que pasa. No sube mas porque falta evidencia final con pantallazos por criterio del PDF, falta integracion real completa, Persona 2 sigue con pruebas rojas por base de datos, los servicios .NET nuevos no compilan en este ambiente y Periodo/Direccion tienen middleware vacio.

## Hallazgos principales

1. Persona 4 ya no esta ausente: `MAT3`, `MAT1`, `ACA1` y `ACA2` tienen implementacion visible.
2. La suite de Persona 4 pasa con 251 pruebas automatizadas.
3. La evidencia de Persona 4 documenta 37 peticiones HTTP correctas y migraciones aplicadas en PrograV.
4. Persona 5 mantiene pruebas verdes con 14 pruebas exitosas.
5. Seguridad mantiene verificacion exitosa con `dotnet test --no-restore`.
6. Persona 2 sigue fallando por conexion/metadata de SQL Server con el usuario `db70273`.
7. Los servicios .NET de Persona 3 siguen bloqueados por configuracion NuGet local inexistente.
8. `NuevoAvatar.Periodo` y `NuevoAvatar.Direccion` tienen middleware vacio registrado, lo cual compromete la proteccion por token y posiblemente la ejecucion.
9. Ya existen colecciones Postman de Persona 4, pero falta coleccion integrada final del equipo.
10. Falta diagrama completo de base de datos.
11. Falta evidencia final de pruebas tecnicas con pantallazos por cada criterio de aceptacion del PDF.
12. El PDF visible suma 22 HU, mientras el roadmap habla de 24.

## Recomendaciones

1. Ejecutar pruebas integradas reales de extremo a extremo:
   - Login -> usuario/rol -> bitacora.
   - Institucion -> profesor -> carrera -> curso -> periodo -> grupo.
   - Direcciones -> expediente -> prematricula -> matricula -> notas.
   - Matricula -> factura -> pago/reverso.
   - Matricula/notas -> historial academico/listado de estudiantes.
2. Corregir `GrupoAuthorizationMiddleware` vacio en `NuevoAvatar.Periodo` y `NuevoAvatar.Direccion`.
3. Arreglar la configuracion NuGet de los servicios .NET nuevos para poder compilar y probar.
4. Definir si la entrega oficial de `ACD3`, `ACD4`, `ACD5` sera la version Java en `services/persona2`, la version .NET separada, o ambas.
5. Corregir credenciales/variables de ambiente de Persona 2 para recuperar pruebas verdes.
6. Agregar pruebas automatizadas para Curso, Grupo, Periodo y Direccion.
7. Ejecutar las colecciones Postman de Persona 4 contra el ambiente integrado real.
8. Crear una coleccion Postman integrada final del equipo.
9. Preparar pantallazos por cada criterio de aceptacion del PDF, como pide la seccion de pruebas tecnicas.
10. Documentar contratos JSON de request/response por endpoint.
11. Agregar el diagrama completo de base de datos al repositorio.
12. Aclarar documentalmente la diferencia entre 22 HU visibles en el PDF y 24 HU indicadas en el roadmap.

## Dictamen final

El proyecto mejoro de forma sustancial desde la auditoria de avance 2. La ausencia de Persona 4, que era el bloqueo mas grande, ya fue atendida con codigo, pruebas, migraciones y evidencia. Esto cambia el estado del proyecto de "incompleto por ausencia critica" a "mayoritariamente implementado, pendiente de estabilizacion e integracion final".

No obstante, el proyecto todavia no debe considerarse cerrado contra el PDF. Los principales pendientes son integracion real entre servicios, evidencia tecnica final por criterio de aceptacion, correccion de Persona 2, compilacion de los servicios .NET nuevos, middleware real para Periodo/Direccion y diagrama completo de base de datos.

El estado actual es alto y mucho mas cercano al cierre, pero el ultimo tramo debe enfocarse menos en agregar codigo nuevo y mas en demostrar funcionamiento integrado, corregir riesgos tecnicos conocidos y preparar evidencia formal de entrega.
