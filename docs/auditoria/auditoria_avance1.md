# Auditoria de avance 1 - Proyecto Nuevo Avatar V1

Fecha de auditoria: 1 de octubre de 2026  
Base principal: `docs/roadmaps/roadmap 1.md`  
Mi objetivo es revisar el avance real del proyecto contra las historias de usuario, entregables y responsabilidades definidas para el primer avance.

## Resumen 

Considero que el proyecto va por buen camino en la base de servicios REST y scripts SQL. Ya existe desarrollo verificable para seguridad, usuarios/oferta academica y Persona 5. Tambien existe evidencia tecnica para Persona 1 y Persona 2, y pruebas automatizadas para Persona 5.

Considero alta la fidelidad funcional al roadmap en las partes implementadas, pero el avance global todavia no puede considerarse 100% porque faltan evidencias y/o implementaciones visibles para Persona 4, falta separar con claridad algunas responsabilidades de Persona 3, y aun no se observa una coleccion Postman integrada ni evidencia por cada criterio de aceptacion de las 24 HU.

## Alcance auditado

En esta auditoria reviso los siguientes elementos del repositorio:

- `docs/roadmaps/roadmap 1.md`
- `docs/pruebas/persona1-persona2.md`
- `services/seguridad`
- `services/persona2`
- `services/persona5`
- `db/persona2/sql`
- `db/persona5/migraciones`

Estado Git que documento durante la auditoria:

- `db/` contiene la nueva organizacion de scripts por persona.
- `services/persona5/` contiene el desarrollo nuevo de Persona 5.

## Criterios generales del roadmap

| Criterio | Estado | Observacion |
|---|---|---|
| Base de datos versionada | En progreso | Hay scripts base en `db/persona2/sql` y migraciones de Persona 5 en `db/persona5/migraciones`. |
| Servicios REST por HU | En progreso | Existen servicios para seguridad, persona2 y persona5. |
| Validacion por token `/validate` | Parcialmente cubierto | Persona 2 y Persona 5 consumen seguridad. Falta validar contra Postman integrado de todo el equipo. |
| Registro de bitacora `/bitacora` | Parcialmente cubierto | Hay componentes transversales y evidencia para Persona 1/2. Falta evidencia completa por cada HU. |
| Contratos JSON | Parcialmente cubierto | Los DTO existen en servicios, pero falta documentacion consolidada por endpoint. |
| Pruebas automatizadas | Parcialmente cubierto | Persona 2 y Persona 5 tienen pruebas. Seguridad tiene pruebas de autenticacion. |
| Evidencia Postman | Parcialmente cubierto | Existe evidencia para Persona 1/2. Falta coleccion/evidencia consolidada de todas las HU. |
| Diagrama completo BD | No verificado | No encuentro evidencia visible en archivos revisados. |

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
- Script SQL de seguridad y bitacora dentro de `db/persona2/sql/separado/01_seguridad_general.sql`.
- Evidencia tecnica en `docs/pruebas/persona1-persona2.md`.

Riesgos y pendientes que identifico:

- La ubicacion SQL de Persona 1 esta actualmente dentro de `db/persona2/sql`; se acepto dejarlo asi por ahora.
- Tengo pendiente confirmar evidencia Postman completa por cada criterio de aceptacion.
- Tengo pendiente verificar si todos los errores tecnicos se registran en bitacora.

Mi dictamen: avance alto y alineado al roadmap.

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
- Controladores para `/usuario`, `/institucion`, `/profesor` y `/carrera`.
- DTOs, servicios, repositorios y validaciones por modulo.
- Integracion con seguridad para `/validate` y `/bitacora`.
- Pruebas automatizadas documentadas: 9 pruebas sin fallos segun `docs/pruebas/persona1-persona2.md`.
- Scripts SQL academicos en `db/persona2/sql`.

Riesgos y pendientes que identifico:

- El servicio de Persona 2 tambien contiene `/curso`, `/periodo` y `/grupo`, que en el roadmap pertenecen a Persona 3. Funcionalmente ayuda al proyecto, pero la trazabilidad por persona queda mezclada.
- Identifico falta de evidencia Postman detallada por cada criterio de aceptacion.
- Tengo pendiente confirmar datos semilla especificos de instituciones, profesores y carreras.

Mi dictamen: avance alto; fiel en sus HU asignadas, con trabajo adicional que se cruza con Persona 3.

### Persona 3 - Alejandro

Responsabilidades que reviso segun el roadmap:

| HU | Endpoint | Alcance esperado | Estado |
|---|---|---|---|
| `ACD3` | `/curso` | CRUD de cursos, filtro por carrera, nivel 1 a 12 | Implementado en `services/persona2` |
| `ACD5` | `/periodo` | CRUD de periodos con anio, numero y fechas | Implementado en `services/persona2` |
| `ACD4` | `/grupo` | CRUD de grupos con curso, profesor, horario, cupo y periodo | Implementado en `services/persona2` |
| `MAT4` | `/provincias`, `/cantones`, `/distritos` | Consultas territoriales y validacion jerarquica | Parcial en BD, no se encontro API |

Evidencia que identifico:

- Endpoints `/curso`, `/periodo` y `/grupo` existen en `services/persona2`.
- Scripts base contienen tablas territoriales en `db/persona2/sql/separado/03_matricula_catalogos.sql`.
- No encuentro servicio/controlador visible para `/provincias`, `/cantones` o `/distritos`.

Riesgos y pendientes que identifico:

- La responsabilidad de Persona 3 no esta separada como servicio propio.
- `MAT4` requiere endpoints de consulta territorial; solo se observa estructura SQL.
- Identifico falta de evidencia Postman o pruebas especificas para Persona 3.

Mi dictamen: avance medio-alto en oferta academica, pendiente importante en direcciones territoriales.

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
- Persona 5 deja `IdentificacionEstudiante` como texto precisamente hasta que exista la integracion de expedientes.

Riesgos y pendientes que identifico:

- Es el mayor pendiente del avance global.
- Bloquea la validacion completa de flujos de expediente -> prematricula -> matricula -> notas.
- Bloquea la fidelidad completa de `ACA1` y `ACA2`.

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
- Pruebas automatizadas ejecutadas: 14 pruebas, 0 fallos, 0 errores.
- `git diff --check` ejecutado sin problemas.

Riesgos y pendientes que identifico:

- Identifico falta de evidencia Postman por cada criterio de aceptacion.
- `MAT2` todavia no valida estudiante contra `MAT3` porque Persona 4 no esta disponible.
- `IPN3` usa variables de ambiente para SMTP; el roadmap idealmente indica parametros de `USR3`, aunque variables de ambiente son aceptadas como configuracion.

Mi dictamen: avance muy alto y alineado al roadmap; pendiente solo integracion/evidencia completa.

## Trazabilidad global por HU

| Area | HU | Estado |
|---|---|---|
| Seguridad | `USR5` | Implementado |
| Seguridad | `GEN1` | Implementado |
| Seguridad | `USR2` | Implementado |
| Seguridad | `USR3` | Implementado |
| Seguridad | `USR4` | Implementado |
| Usuarios | `USR1` | Implementado |
| Oferta academica | `ACD1` | Implementado |
| Oferta academica | `ACD2` | Implementado |
| Oferta academica | `ACD3` | Implementado, pero bajo `services/persona2` |
| Oferta academica | `ACD4` | Implementado, pero bajo `services/persona2` |
| Oferta academica | `ACD5` | Implementado, pero bajo `services/persona2` |
| Oferta academica | `ACD6` | Implementado |
| Matricula | `MAT4` | Parcial: SQL territorial, falta API visible |
| Matricula | `MAT3` | No encontrado |
| Matricula | `MAT1` | No encontrado |
| Matricula | `MAT2` | Implementado |
| Matricula | `MAT5` | Implementado |
| Academico | `ACA1` | No encontrado |
| Academico | `ACA2` | No encontrado segun roadmap actual |
| Finanzas/notificaciones | `IPN1` | Implementado |
| Finanzas/notificaciones | `IPN2` | Implementado |
| Finanzas/notificaciones | `IPN3` | Implementado |

En mi revision observo que el roadmap indica 24 historias, pero la lista visible suma 22 codigos unicos. Tengo pendiente contrastar este conteo con el PDF original para confirmar si faltan dos HU o si el total del roadmap esta desactualizado.

## Barras de progreso por persona

Para mis estimaciones combino implementacion visible, SQL, pruebas y evidencia encontrada. No representan aprobacion final del profesor; expresan mi lectura tecnica del repositorio contra el roadmap.

| Persona | Responsable | Progreso | Barra |
|---|---|---:|---|
| Persona 1 | Hector | 90% | `[##################--]` |
| Persona 2 | Ramses | 90% | `[##################--]` |
| Persona 3 | Alejandro | 65% | `[#############-------]` |
| Persona 4 | Fabian | 0% | `[--------------------]` |
| Persona 5 | Jose | 90% | `[##################--]` |

## Avance estimado del proyecto

Estimo el progreso global en 67%

Represento mi estimacion global con esta barra:

`[#############-------]`

Estimo este porcentaje principalmente por la ausencia visible de Persona 4, la falta de endpoints territoriales de `MAT4`, y la falta de evidencia integrada/Postman para todas las HU.

## Mis hallazgos principales

1. Observo que Persona 1 esta funcionalmente avanzada: seguridad, roles, parametros, modulos, login, refresh, validate y bitacora existen.
2. Documento que Persona 2 esta funcionalmente avanzada y ademas contiene trabajo que el roadmap asigna a Persona 3.
3. Identifico que Persona 3 tiene `ACD3`, `ACD4` y `ACD5` cubiertos en codigo, pero `MAT4` no esta completo como API.
4. Observo que Persona 4 no tiene implementacion verificable en el repositorio actual.
5. Documento que Persona 5 tiene sus cinco HU implementadas, con migraciones y pruebas automatizadas.
6. Identifico como pendiente la evidencia consolidada de Postman por cada criterio de aceptacion.
7. Identifico como pendiente verificar el diagrama completo de base de datos.
8. Identifico que la estructura SQL quedo aceptada temporalmente con parte de Persona 1 dentro de `db/persona2/sql`.

## Acciones que propongo

1. Me propongo priorizar Persona 4: `MAT3`, `MAT1`, `ACA1` y `ACA2`.
2. Me propongo completar `MAT4` con endpoints `/provincias`, `/cantones` y `/distritos`.
3. Me propongo crear o actualizar evidencia Postman por persona.
4. Me propongo preparar una coleccion Postman integrada por flujo:
   - Login -> usuario/rol -> bitacora.
   - Institucion -> profesor -> carrera -> curso -> periodo -> grupo.
   - Direcciones -> expediente -> prematricula -> matricula -> notas.
   - Matricula -> factura -> pago/reverso.
5. Me propongo documentar contratos JSON de request/response por endpoint.
6. Me propongo confirmar si el conteo de 24 HU del roadmap coincide con el PDF.
7. Me propongo mantener todos los cambios SQL en `db/` por persona o en una carpeta comun acordada.

## Mi dictamen final

Considero que el avance es consistente con la arquitectura esperada y las partes implementadas respetan en buena medida el roadmap. Sin embargo, el proyecto aun no esta al 100% contra el primer avance porque faltan historias completas de Persona 4, falta terminar `MAT4` como API, y falta evidencia integrada para todos los criterios de aceptacion.

Considero que el proyecto va por buen camino: la base de seguridad, oferta academica y Persona 5 ya permite avanzar hacia pruebas integradas reales cuando se complete expediente/prematricula y las consultas academicas.
