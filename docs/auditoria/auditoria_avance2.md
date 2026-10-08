# Auditoria de avance 2 - Proyecto Nuevo Avatar V1

Fecha de auditoria: 3 de octubre de 2026  
Base principal: `docs/requerimientos/Proyecto Nuevo Avatar V1.pdf`  
Bases complementarias: `docs/roadmaps/roadmap 1.md`, `docs/roadmaps/propuesta.md`, `docs/auditoria/auditoria_avance1.md`  
Mi objetivo es revisar el avance real mas reciente del proyecto contra el PDF oficial de requerimientos, el roadmap interno y la auditoria de avance 1.

## Resumen

Identifico avances claros en el proyecto respecto a la auditoria de avance 1. La mejora principal esta en Persona 3: ahora existen servicios separados para `Curso`, `Grupo`, `Periodo` y `Direccion`, y ya se observan endpoints para `/provincias`, `/cantones` y `/distritos`, que antes eran el faltante mas visible de `MAT4`.

Tambien identifico un avance alto en Seguridad, Persona 2 y Persona 5. Persona 5 fue verificada nuevamente con pruebas automatizadas exitosas: 14 pruebas ejecutadas, 0 fallos y 0 errores. Sin embargo, Persona 2 ya no quedo verde en esta auditoria porque la prueba de contexto fallo al intentar conectarse a SQL Server con el usuario `db70273`.

Identifico como mayor pendiente funcional Persona 4. No encuentro implementacion visible para `/expediente`, `/prematricula`, `/historialacademico` ni `/listadoestudiantes`. Esto mantiene incompleto el flujo expediente -> prematricula -> matricula -> notas -> consultas academicas.

Tambien identifico un riesgo tecnico nuevo: los servicios .NET nuevos no pudieron compilarse en esta revision por un problema de restauracion NuGet del ambiente, y `Periodo`/`Direccion` contienen un `GrupoAuthorizationMiddleware` vacio aunque se registra con `UseMiddleware`. Mantengo pendiente corregirlo o verificarlo antes de considerar esos servicios listos para entrega.

## Alcance auditado

En esta auditoria reviso los siguientes elementos del repositorio:

- `docs/requerimientos/Proyecto Nuevo Avatar V1.pdf`
- `docs/roadmaps/roadmap 1.md`
- `docs/roadmaps/propuesta.md`
- `docs/auditoria/auditoria_avance1.md`
- `docs/pruebas/persona1-persona2.md`
- `services/seguridad`
- `services/persona2`
- `services/persona5`
- `NuevoAvatar.Curso`
- `NuevoAvatar.Grupo`
- `NuevoAvatar.Periodo`
- `NuevoAvatar.Direccion`
- `db/persona2/sql`
- `db/persona5/migraciones`

Estado Git que documento durante la auditoria:

- No habia cambios pendientes al iniciar la revision.
- Presento este documento como nueva evidencia de auditoria.

## Verificacion ejecutada

| Verificacion | Resultado | Observacion |
|---|---|---|
| Extraccion del PDF oficial con `pypdf` | Exitosa | Se confirmaron los criterios de aceptacion de las HU visibles. |
| Busqueda de endpoints REST | Exitosa | Se ubicaron endpoints en Seguridad, Persona 2, Persona 5 y servicios .NET nuevos. |
| `dotnet test services/seguridad/NuevoAvatarSeguridad.sln --no-restore` | Sin errores visibles | El comando termino con codigo 0. |
| `services/persona5`: `..\persona2\mvnw.cmd -f pom.xml test` | Exitoso | 14 pruebas, 0 fallos, 0 errores, 0 omitidas. |
| `services/persona2`: `.\mvnw.cmd test` | Fallido | 9 pruebas ejecutadas, 1 error. Falla por login a SQL Server con usuario `db70273`. |
| `dotnet build` de `NuevoAvatar.Curso`, `Grupo`, `Periodo`, `Direccion` | Fallido por ambiente | NuGet intenta usar `C:\Program Files (x86)\Microsoft SDKs\NuGetPackages\`, ruta local inexistente. |

Considero que las fallas de build .NET no prueban por si solas que el codigo funcional este mal, pero impiden validar compilacion en este ambiente. En `Periodo` y `Direccion` si existe ademas una observacion de codigo: el middleware registrado esta vacio.

## Criterios generales del PDF y roadmap

| Criterio | Estado | Observacion |
|---|---|---|
| Base de datos versionada | En progreso | Hay scripts base en `db/persona2/sql` y migraciones de Persona 5 en `db/persona5/migraciones`. |
| Diagrama completo de BD | No verificado | No encuentro archivo de diagrama en el repositorio auditado. |
| Servicios REST por HU | En progreso | Hay servicios para Seguridad, Persona 2, Persona 3 parcial/completo en .NET y Persona 5. Persona 4 sigue ausente. |
| Validacion por token `/validate` | Parcialmente cubierto | Seguridad existe; Persona 2/5 la consumen. Curso usa filtro. Grupo usa middleware. Periodo/Direccion registran middleware vacio. |
| Registro de bitacora `/bitacora` | Parcialmente cubierto | Hay integracion transversal, pero falta evidencia completa por criterio de aceptacion y JSON anterior/actual/eliminado en todos los CRUD. |
| Contratos JSON | Parcialmente cubierto | DTOs existen, pero falta documentacion consolidada de request/response por endpoint. |
| Pruebas automatizadas | Parcialmente cubierto | Persona 5 pasa. Seguridad pasa sin errores visibles. Persona 2 falla por conexion a BD. No se observan pruebas para los servicios .NET nuevos. |
| Evidencia Postman | Parcialmente cubierto | Existe evidencia tecnica para Persona 1/2, pero falta coleccion integrada de todas las HU. |
| Historias de usuario actualizadas | No verificado | No encuentro evidencia de herramienta Scrum/GitLab/Azure DevOps en el repositorio. |

## Comparacion contra auditoria de avance 1

| Area | Estado en avance 1 | Estado en avance 2 | Cambio |
|---|---|---|---|
| `MAT4` direcciones | Parcial: SQL territorial, faltaba API visible | Existen endpoints `/provincias`, `/cantones/{provinciaId}` y `/distritos/{provinciaId}/{cantonId}` | Mejora importante |
| Persona 3 separada | Curso/Periodo/Grupo estaban en `services/persona2` | Ahora existen servicios .NET separados para Curso, Grupo, Periodo y Direccion | Mejora de trazabilidad |
| Persona 4 | No encontrada | Sigue sin encontrarse | Sin avance visible |
| Persona 5 | Implementada y probada | Sigue implementada y pruebas pasan | Se mantiene fuerte |
| Persona 2 | Pruebas documentadas como exitosas | Pruebas actuales fallan por conexion SQL Server | Riesgo nuevo de ambiente/integracion |
| Evidencia Postman integrada | Pendiente | Sigue pendiente | Sin avance visible |
| Diagrama de BD | No verificado | Sigue no verificado | Sin avance visible |

## Auditoria por persona

### Persona 1 - Hector

Responsabilidades que reviso segun el roadmap:

| HU | Endpoint | Alcance esperado | Estado |
|---|---|---|---|
| `USR5` | `/login`, `/refresh`, `/validate` | Login, JWT, refresh token, expiraciones parametrizables | Implementado |
| `GEN1` | `/bitacora` | Registro y consulta de bitacoras | Implementado |
| `USR2` | `/rol` | CRUD de roles | Implementado |
| `USR3` | `/parametro` | CRUD de parametros | Implementado |
| `USR4` | `/modulo` | CRUD de modulos | Implementado |

Evidencia que identifico:

- Servicio .NET en `services/seguridad`.
- Controladores para `/login`, `/refresh`, `/validate`, `/rol`, `/parametro`, `/modulo` y `/bitacora`.
- Pruebas de autenticacion en `services/seguridad/NuevoAvatarSeguridad.Tests`.
- `dotnet test services/seguridad/NuevoAvatarSeguridad.sln --no-restore` termino con codigo 0.
- Script SQL de seguridad y bitacora en `db/persona2/sql/separado/01_seguridad_general.sql`.

Riesgos y pendientes que identifico:

- Tengo pendiente confirmar evidencia Postman completa por cada criterio de aceptacion del PDF.
- Tengo pendiente verificar si bitacora registra exactamente JSON nuevo, anterior/actual y eliminado en todos los CRUD.
- La ubicacion SQL de seguridad sigue dentro de `db/persona2/sql`.

Mi dictamen: avance alto y estable.

### Persona 2 - Ramses

Responsabilidades que reviso segun el roadmap:

| HU | Endpoint | Alcance esperado | Estado |
|---|---|---|---|
| `USR1` | `/usuario` | CRUD de usuarios, filtros, BCrypt, rol y dominio | Implementado |
| `ACD1` | `/institucion` | CRUD de instituciones | Implementado |
| `ACD6` | `/profesor` | CRUD de profesores, mayoria de edad, telefonos y dominio parametrizable | Implementado |
| `ACD2` | `/carrera` | CRUD de carreras, filtro por institucion y director profesor | Implementado |

Evidencia que identifico:

- Servicio Java/Spring Boot en `services/persona2`.
- Controladores para `/usuario`, `/institucion`, `/profesor`, `/carrera`, `/curso`, `/periodo` y `/grupo`.
- DTOs, servicios, repositorios, validaciones y manejo global de errores.
- Integracion con seguridad para `/validate` y `/bitacora`.
- Documentacion tecnica en `services/persona2/README.md`.

Riesgos y pendientes que identifico:

- La prueba actual de Persona 2 fallo: 9 pruebas ejecutadas, 1 error, causado por login fallido a SQL Server para el usuario `db70273`.
- `services/persona2` sigue mezclando HU de Persona 3 (`ACD3`, `ACD4`, `ACD5`) aunque ahora tambien existen servicios .NET separados.
- Identifico falta de evidencia Postman detallada por cada criterio de aceptacion.
- Tengo pendiente confirmar datos semilla completos de instituciones, profesores y carreras.

Mi dictamen: avance funcional alto, pero con riesgo actual de verificacion por conexion de base de datos.

### Persona 3 - Alejandro

Responsabilidades que reviso segun el roadmap:

| HU | Endpoint | Alcance esperado | Estado |
|---|---|---|---|
| `ACD3` | `/curso` | CRUD de cursos, filtro por carrera, nivel 1 a 12 | Implementado en `services/persona2` y tambien en `NuevoAvatar.Curso` |
| `ACD5` | `/periodo` | CRUD de periodos con anio, numero y fechas | Implementado en `services/persona2` y tambien en `NuevoAvatar.Periodo` |
| `ACD4` | `/grupo` | CRUD de grupos con curso, profesor, horario, cupo y periodo | Implementado en `services/persona2` y tambien en `NuevoAvatar.Grupo` |
| `MAT4` | `/provincias`, `/cantones`, `/distritos` | Consultas territoriales y validacion jerarquica | Implementado en `NuevoAvatar.Direccion`, con riesgo de middleware |

Evidencia que identifico:

- `NuevoAvatar.Curso` expone `/curso`, `/curso/{id}` y `/curso/carrera/{carreraId}`.
- `NuevoAvatar.Curso` valida carrera, nivel 1 a 12 y nombre solo con letras/espacios.
- `NuevoAvatar.Grupo` expone CRUD de `/grupo` y valida curso, profesor, horario, cupo y periodo.
- `NuevoAvatar.Periodo` expone CRUD de `/periodo` y valida anio, numero de periodo, fecha inicio y fecha fin.
- `NuevoAvatar.Direccion` expone `/provincias`, `/cantones/{provinciaId}` y `/distritos/{provinciaId}/{cantonId}`.
- `DireccionRepository` valida jerarquia provincia-canton para distritos mediante join contra `matricula.Canton`.
- Scripts territoriales existen en `db/persona2/sql/separado/03_matricula_catalogos.sql`.

Riesgos y pendientes que identifico:

- `NuevoAvatar.Periodo` y `NuevoAvatar.Direccion` registran `UseMiddleware<GrupoAuthorizationMiddleware>()`, pero la clase `GrupoAuthorizationMiddleware` esta vacia en esos proyectos.
- No logro verificar la compilacion de ninguno de los servicios .NET nuevos por problema de fuente NuGet local inexistente.
- No encuentro pruebas automatizadas para `NuevoAvatar.Curso`, `Grupo`, `Periodo` o `Direccion`.
- Identifico falta de evidencia Postman de `MAT4` y de los CRUD separados.
- La coexistencia de implementaciones Java y .NET para `curso`, `periodo` y `grupo` puede causar confusion de fuente oficial si no se define cual se entrega.

Mi dictamen: avance medio-alto con mejora fuerte respecto a avance 1, pero requiere limpieza y verificacion de ejecucion antes del cierre.

### Persona 4 - Fabian

Responsabilidades que reviso segun el roadmap:

| HU | Endpoint | Alcance esperado | Estado |
|---|---|---|---|
| `MAT3` | `/expediente` | CRUD de estudiantes, direccion, telefonos y email `cuc.cr` parametrizable | No encontrado |
| `MAT1` | `/prematricula` | Prematricular, modificar, eliminar y consultar | No encontrado |
| `ACA1` | `/historialacademico` | Promedios de notas por estudiante | No encontrado |
| `ACA2` | `/listadoestudiantes` | Estudiantes matriculados por periodo con carrera, curso y grupo | No encontrado |

Evidencia que identifico:

- No encuentro carpeta `services/persona4`.
- No encuentro controladores para `/expediente`, `/prematricula`, `/historialacademico` o `/listadoestudiantes`.
- Persona 5 mantiene `IdentificacionEstudiante` como texto en matricula/factura, lo que confirma que la integracion con expediente aun no existe.

Riesgos y pendientes que identifico:

- Sigue siendo el mayor pendiente del proyecto.
- Bloquea la validacion completa de estudiante real, direccion, telefonos y dominio `cuc.cr`.
- Bloquea prematricula.
- Bloquea las consultas academicas `ACA1` y `ACA2`.
- Impide cerrar los flujos integrados exigidos por el roadmap.

Mi dictamen: sin avance verificable en el repositorio actual.

### Persona 5 - Jose

Responsabilidades que reviso segun el roadmap:

| HU | Endpoint | Alcance esperado | Estado |
|---|---|---|---|
| `MAT2` | `/matricula` | Matricular, modificar, eliminar y consultar por curso/grupo | Implementado |
| `MAT5` | `/cargardesglose`, `/asignarnotarubro`, `/obtenerdesglose`, `/obtenernotas` | Rubros, suma 100, notas 1-100 y bloqueo si ya hay notas | Implementado |
| `IPN1` | `/factura` | Crear, reversar, consultar y listar facturas por periodo | Implementado |
| `IPN2` | `/pago` | Crear pago, reversar pago, consultar y listar pagos por periodo | Implementado |
| `IPN3` | `/notificar` | Envio de correo con email, asunto, cuerpo HTML y SMTP parametrizable | Implementado |

Evidencia que identifico:

- Servicio Java/Spring Boot en `services/persona5`.
- Migraciones separadas en `db/persona5/migraciones`.
- Controladores, DTOs, servicios, repositorios y entidades por modulo.
- Seguridad transversal con validacion de token y registro de bitacora.
- Configuracion SMTP por variables de ambiente.
- Pruebas automatizadas ejecutadas en esta auditoria: 14 pruebas, 0 fallos, 0 errores, 0 omitidas.

Riesgos y pendientes que identifico:

- `MAT2` todavia no valida estudiante contra `MAT3` porque Persona 4 no esta disponible.
- Identifico falta de evidencia Postman por cada criterio de aceptacion.
- `IPN3` usa variables de ambiente para SMTP; el PDF pide datos parametrizables. Tengo pendiente aclarar si se acepta variable de ambiente o si se requiere integracion con `USR3`.

Mi dictamen: avance muy alto y verificado con pruebas automatizadas.

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
| Matricula | `MAT3` | No encontrado |
| Matricula | `MAT1` | No encontrado |
| Matricula | `MAT2` | Implementado |
| Matricula | `MAT5` | Implementado |
| Academico | `ACA1` | No encontrado |
| Academico | `ACA2` | No encontrado |
| Finanzas/notificaciones | `IPN1` | Implementado |
| Finanzas/notificaciones | `IPN2` | Implementado |
| Finanzas/notificaciones | `IPN3` | Implementado |

En mi revision observo que el PDF oficial contiene 22 codigos de HU visibles en la extraccion auditada, mientras el roadmap interno indica 24 historias. Mantengo este desfase como pendiente de aclaracion documental.

## Barras de progreso por persona

Para mis estimaciones combino implementacion visible, scripts SQL, pruebas, evidencia y capacidad de verificacion. No representan aprobacion final del profesor; expresan mi lectura tecnica del repositorio contra el PDF y la documentacion interna.

| Persona | Responsable | Progreso | Barra |
|---|---|---:|---|
| Persona 1 | Hector | 90% | `[##################--]` |
| Persona 2 | Ramses | 80% | `[################----]` |
| Persona 3 | Alejandro | 75% | `[###############-----]` |
| Persona 4 | Fabian | 0% | `[--------------------]` |
| Persona 5 | Jose | 92% | `[##################--]` |

## Avance estimado del proyecto

Estimo el progreso global en 70%

Represento mi estimacion global con esta barra:

`[##############------]`

Estimo un aumento respecto a avance 1 por la aparicion de servicios para Persona 3 y por la cobertura visible de `MAT4`. No sube mas porque Persona 4 sigue ausente, no hay evidencia Postman consolidada, los servicios .NET nuevos no quedaron compilados en esta auditoria y Persona 2 presenta una falla actual de prueba por conexion a base de datos.

## Mis hallazgos principales

1. Observo que el PDF oficial confirma las HU y criterios principales usados por el roadmap.
2. Documento que la mejora mas clara frente a avance 1 es `MAT4`: ahora existen endpoints de provincias, cantones y distritos.
3. Identifico que Persona 3 ya tiene estructura propia en .NET para Curso, Grupo, Periodo y Direccion.
4. Observo que `NuevoAvatar.Periodo` y `NuevoAvatar.Direccion` tienen un middleware vacio registrado, lo cual pone en riesgo la ejecucion y la proteccion por token.
5. Documento que Persona 4 sigue sin implementacion verificable.
6. Identifico que Persona 5 mantiene un avance muy alto y sus pruebas pasan.
7. Observo que Persona 2 conserva implementacion amplia, pero la verificacion actual falla por credenciales/conexion SQL Server.
8. Documento que los builds .NET nuevos estan bloqueados por una configuracion NuGet local inexistente.
9. Identifico como pendiente la evidencia Postman integrada por criterio de aceptacion.
10. Identifico como pendiente el diagrama completo de base de datos.

## Acciones que propongo

1. Me propongo priorizar Persona 4: implementar `MAT3`, `MAT1`, `ACA1` y `ACA2`.
2. Me propongo corregir `GrupoAuthorizationMiddleware` vacio en `NuevoAvatar.Periodo` y `NuevoAvatar.Direccion`.
3. Me propongo definir si la entrega oficial de `ACD3`, `ACD4`, `ACD5` sera la version Java en `services/persona2`, la version .NET separada, o ambas.
4. Me propongo arreglar la restauracion NuGet de los proyectos .NET nuevos para poder compilar y probar.
5. Me propongo corregir credenciales/variables de ambiente de Persona 2 para recuperar pruebas verdes.
6. Me propongo agregar pruebas automatizadas para Curso, Grupo, Periodo y Direccion.
7. Me propongo crear evidencia Postman por cada criterio de aceptacion del PDF.
8. Me propongo preparar una coleccion Postman integrada por flujo:
   - Login -> usuario/rol -> bitacora.
   - Institucion -> profesor -> carrera -> curso -> periodo -> grupo.
   - Direcciones -> expediente -> prematricula -> matricula -> notas.
   - Matricula -> factura -> pago/reverso.
   - Matricula/notas -> historial academico/listado de estudiantes.
9. Me propongo documentar contratos JSON de request/response por endpoint.
10. Me propongo agregar el diagrama completo de base de datos al repositorio.
11. Me propongo aclarar documentalmente la diferencia entre 22 HU visibles en el PDF y 24 HU indicadas en el roadmap.

## Mi dictamen final

Observo un avance del proyecto desde la auditoria de avance 1. Ya no es correcto decir que `MAT4` solo existe en SQL: ahora existe implementacion visible para direcciones, y Persona 3 tiene servicios propios para sus responsabilidades principales.

Sin embargo, no considero que el proyecto este listo como cierre completo del primer alcance. La ausencia de Persona 4 sigue siendo el mayor bloqueo funcional, la evidencia Postman integrada aun no aparece, y los servicios nuevos requieren una ronda de estabilizacion tecnica para compilar, proteger endpoints y demostrar ejecucion.

Considero que el estado actual es prometedor y mas completo que el avance anterior, pero el cierre debe concentrarse en tres frentes: completar Persona 4, validar la ejecucion real de los servicios .NET nuevos y documentar pruebas por cada criterio de aceptacion del PDF.
