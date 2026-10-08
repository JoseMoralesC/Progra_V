# Auditoria de avance 4 - Proyecto Nuevo Avatar V1

Fecha de auditoria: 8 de octubre de 2026  
Base principal: `docs/requerimientos/Proyecto Nuevo Avatar V1.pdf`  
Version revisada: `143f92fbefed2d0fd228d9c4a6ec07d2cc78aa58`  
Bases complementarias: Doc Master, roadmaps, auditorias 1 a 3, codigo, scripts, colecciones y evidencias del repositorio.  
Resultados consolidados: [verificacion-avance4.json](verificacion-avance4.json).

## Resumen

Identifico implementacion visible para las **22 historias de usuario del PDF** y un aumento importante de evidencia respecto a la auditoria 3. Ya no considero vigente el fallo de conexion de Persona 2 cuando configuro correctamente PrograV: ejecuto sus 12 pruebas y todas pasan. Tambien ejecuto 5 pruebas de Seguridad, 251 de Persona 4 y 16 de Persona 5. En total documento **284 pruebas aprobadas, sin fallos, errores ni omisiones**.

Compruebo que los cuatro proyectos .NET de Persona 3 compilan al restaurar sus dependencias desde NuGet oficial. Sin embargo, reproduzco un bloqueo real de arranque en Periodo y Direccion: ambos registran una clase de middleware sin `Invoke` ni `InvokeAsync`. Distingo este defecto del problema de restauracion del ambiente.

Verifico PrograV mediante consultas de solo lectura: encuentro 26 tablas de dominio, 31 relaciones habilitadas y verificadas, la relacion matricula-estudiante y los parametros `DOMESTUD` y `DOMPROF`. No encuentro diagramas guardados en SQL Server ni diagramas incorporados al Doc Master revisado.

Mi dictamen es **implementacion amplia, con pruebas locales aprobadas, pero entrega integral todavia no cerrada**. Mantengo como prioridades el arranque reproducible de Persona 3, el formato obligatorio de bitacoras, la evidencia integrada con datos reales de prueba y los entregables de analisis/diseno.

## Alcance auditado

En esta revision comparo los siguientes elementos:

- PDF oficial completo: 30 paginas; criterios funcionales y entregables.
- `docs/doc_master/Doc Master.docx`: contenido, tablas y presencia de diagramas.
- `docs/roadmaps/propuesta.md` y `docs/roadmaps/roadmap 1.md`.
- `docs/auditoria/auditoria_avance1.md`, `auditoria_avance2.md` y `auditoria_avance3.md`.
- README de servicios y SQL; resumenes de pruebas de Personas 1, 2 y 4; recorrido de Persona 5.
- Seguridad .NET; servicios Java de Personas 2, 4 y 5; proyectos .NET Curso, Grupo, Periodo y Direccion.
- Scripts en `db/persona2`, `db/persona3`, `db/persona4`, `db/persona5` y preparaciones de pruebas.
- Ocho colecciones principales en `docs/pruebas/postman`, resultados HTTP historicos y carpetas de capturas.
- Metadatos de tablas, relaciones, parametros seleccionados y cantidades de catalogos en PrograV.

Documento que el arbol Git estaba limpio al iniciar. Las pruebas generan archivos en `services/persona4/target/`; no los considero parte de la entrega documental. No modifico codigo funcional ni ejecuto creaciones, pagos, anulaciones o envios SMTP durante esta auditoria. En la carpeta Doc Master encuentro el archivo `.docx`; no encuentro `Doc_Master.doc`, aunque figure entre las pestanas del editor.

### Mi criterio de verificacion

Distingo **codigo revisado**, **ejecucion actual**, **evidencia historica** y **pendiente de integracion**. No equiparo una coleccion importable con una ejecucion aprobada, ni un codigo HTTP correcto con cumplimiento de todos los criterios de una HU.

Reviso visualmente una muestra de cinco capturas: `P3-DIR-01.png`, `P3-PER-01.png`, `GEN1_01_BitacoraMatricula_200.PNG`, `IPN1_01_CrearFactura_201.PNG` e `IPN3_04_CorreoRecibido.PNG`. Inventario las demas capturas, pero no certifico individualmente su contenido ni su correspondencia con cada criterio del PDF.

## Verificacion ejecutada

| Verificacion | Resultado actual | Alcance que documento |
|---|---|---|
| Extraccion del PDF y del XML del Doc Master | Exitosa | Requerimientos, distribucion, entregables y discrepancias documentales. |
| Seguridad: `dotnet test services/seguridad/NuevoAvatarSeguridad.Tests/NuevoAvatarSeguridad.Tests.csproj --no-restore` | 5/5 aprobadas | Pruebas del controlador de autenticacion; no certifican todos los CRUD ni SQL real. |
| Persona 2: `.\mvnw.cmd test` con propiedades locales de datasource | 12/12 aprobadas | Incluye `contextLoads` contra PrograV con `ddl-auto=validate`; no reejecuto las colecciones HTTP. |
| Persona 4: `.\mvnw.cmd test` | 251/251 aprobadas | H2, MockMvc y dependencias HTTP simuladas. |
| Persona 5: `..\persona2\mvnw.cmd -f pom.xml test` | 16/16 aprobadas | Servicios con repositorios/correo simulados y pruebas de carga de `.env`; no es una prueba completa de persistencia. |
| Curso, Grupo, Periodo y Direccion: restauracion oficial y `dotnet build --no-restore` | Cuatro compilaciones aprobadas | Cero advertencias y errores en los builds finales. |
| Arranque aislado de Periodo en puerto 18083 | Fallido, salida 1 | Middleware sin metodo publico `Invoke`/`InvokeAsync`. |
| Arranque aislado de Direccion en puerto 18084 | Fallido, salida 1 | Mismo defecto; ocurre antes de atender solicitudes. |
| Consulta de metadatos PrograV | Exitosa | 26 tablas de dominio, 31 FK, ninguna deshabilitada o no verificada. |
| Catalogos territoriales PrograV | Un registro en cada tabla | Provincia, canton y distrito disponibles para el fixture; no prueban un catalogo territorial completo. |
| Parametros seleccionados PrograV | `DOMESTUD=cuc.cr`, `DOMPROF=cuc.ac.cr` | `EXPJWT` y `EXPREFRESH` no aparecen en la consulta; el codigo dispone de valores por defecto 5 y 60 minutos. |
| Diagramas PrograV / Doc Master | Cero guardados / cero dibujos | No encuentro una representacion completa de BD, casos de uso o clases en los archivos revisados. |

Documento que los primeros intentos Maven restringidos no pudieron resolver dependencias. Repito las pruebas con acceso autorizado y utilizo los resultados finales anteriores. Los builds .NET iniciales fallaron por la fuente local inexistente `Microsoft SDKs/NuGetPackages`; la restauracion explicita desde `https://api.nuget.org/v3/index.json` permitio compilar, sin modificar archivos de codigo o configuracion versionados.

Para reproducir Persona 2 uso `spring.datasource.url`, `spring.datasource.username` y `spring.datasource.password` con valores locales. No incorporo las credenciales al informe. No considero suficiente ejecutar Maven sin preparar esas propiedades o sus variables equivalentes.

## Criterios generales del PDF y roadmap

| Criterio | Estado actualizado | Observacion que documento |
|---|---|---|
| Codigo REST por HU | Presente para 22/22 HU | Presencia no equivale a todos los criterios aprobados. |
| Base de datos versionada | Avance alto | 23 scripts bajo `db/`, mas fixtures; no recreo una BD vacia para certificar el orden completo. |
| Integridad referencial actual | Verificada en metadatos | 31 FK habilitadas/verificadas; no pruebo todas las restricciones mediante escrituras. |
| Autorizacion `/validate` | Parcial | Clientes en Java, filtro Curso y middleware Grupo; Periodo/Direccion no arrancan. |
| Bitacora `GEN1` y detalle exigido | Parcial, con incumplimientos concretos | Falta JSON anterior/actual/eliminado en varios servicios; no basta consultar `/bitacora` con 200. |
| Contratos JSON | Parcial | DTO y colecciones disponibles; contratos distintos entre variantes Java/.NET y adaptacion IPN1. |
| Pruebas automatizadas | 284 aprobadas en cuatro suites | No encuentro proyectos de pruebas para los cuatro servicios .NET de Persona 3. |
| Evidencia Postman | Mejora considerable, parcial por criterio | Resultados historicos P1/P2, capturas P3/P4/P5; falta matriz completa criterio-evidencia. |
| Integracion de extremo a extremo | No certificada integralmente | No ejecuto en esta revision un flujo completo con todos los servicios reales. |
| Analisis y diseno final | Incompleto | Doc Master de propuesta sin diagramas ni resultados finales insertados. |
| HU actualizadas / PR final | No verificado | Git local no demuestra estado del tablero ni aprobacion del PR final. |

## Comparacion contra auditorias anteriores

| Area | Avance 1 y 2 | Avance 3 | Estado actual |
|---|---|---|---|
| Persona 4 | Ausente | Implementada, 251 pruebas | Reejecuto 251 aprobadas; capturas reales siguen siendo parciales. |
| Persona 2 | Fallo de contexto por SQL en avance 2 | 8/9, contexto fallido | Reejecuto 12/12 con PrograV; existe evidencia posterior de 56 peticiones HTTP. |
| Persona 5 | 14 pruebas, poca evidencia | 14 pruebas | Reejecuto 16 pruebas; encuentro 61 capturas, incluida recepcion de correo. |
| Seguridad | Servicio y pruebas existentes | Comando exitoso sin total preciso | Confirmo 5 pruebas; evidencia historica adicional de 43 solicitudes. |
| Compilacion P3 .NET | Bloqueada por NuGet | Sigue bloqueada | Cuatro builds aprobados usando fuente oficial explicita. |
| Arranque Periodo/Direccion | Middleware vacio observado | Defecto pendiente | Reproduzco excepcion de arranque en ambos proyectos actuales. |
| Evidencia P3 | Insuficiente | Faltante | 20 capturas; muestra de consultas 200 no coincide con arranque reproducible actual. |
| Diagrama BD | No localizado | No localizado | Doc Master sin dibujos y SQL Server sin diagramas guardados. |
| Conteo HU | Roadmap declara 24 | PDF identificado con 22 | Confirmo 22; la inconsistencia persiste en roadmap y Doc Master. |

Mantengo las auditorias anteriores como documentos historicos. No traslado sus errores o porcentajes al estado actual sin volver a contrastarlos.

## Auditoria por persona y trazabilidad por HU

Uso la distribucion del roadmap y Doc Master como referencia de responsabilidades actuales. Documento mas adelante las diferencias con la propuesta inicial.

### Persona 1 - Hector

| HU / paginas PDF | Endpoint | Criterios que reviso | Estado y pendiente |
|---|---|---|---|
| USR5 / 8-10 | `/login`, `/refresh`, `/validate` | Headers, respuestas 201/401/200, BCrypt, JWT y refresh configurables | Implementado; 5 pruebas actuales y evidencia HTTP historica. Falta garantizar que la expiracion refresh configurada sea mayor que JWT. |
| USR2 / 5-6 | `/rol` | Cinco operaciones, requeridos, nombre letras/espacios, autorizacion | Implementado; controladores registran JSON nuevo/anterior/actual/eliminado. |
| USR3 / 6-7 | `/parametro` | CRUD, identificador mayusculas hasta 10, valor hasta 500 | Implementado y presente en coleccion; parametros de dominio comprobados en PrograV. |
| USR4 / 7-8 | `/modulo` | CRUD, requeridos, nombre letras/espacios | Implementado; evidencia de ejecucion historica P1. |
| GEN1 / 26-27 | `/bitacora` | Usuario/descripcion requeridos, hora del servidor, token y consulta | Servicio implementado; el cumplimiento de sus consumidores es parcial. |

Mi dictamen: avance alto. Distingo las pruebas actuales de AuthController del conjunto de CRUD historicamente ejecutado. Mantengo pendiente la cobertura de errores tecnicos de todos los controladores y la validacion del orden de expiraciones.

### Persona 2 - Ramses

| HU / paginas PDF | Endpoint | Criterios que reviso | Estado y pendiente |
|---|---|---|---|
| USR1 / 4-5 | `/usuario` | Seis operaciones, filtros, BCrypt, rol/dominio y requeridos | Implementado; evidencia historica de casos positivos/negativos. |
| ACD1 / 10-11 | `/institucion` | CRUD, requeridos y nombre letras/espacios | Implementado; consultas y referencias cubiertas por coleccion P2. |
| ACD6 / 15-16 | `/profesor` | CRUD, mayoria de edad, telefonos, nombre y dominio parametrizable | Implementado; `DOMPROF` disponible y pruebas HTTP historicas. |
| ACD2 / 11-12 | `/carrera` | CRUD, filtro por institucion, director profesor | Implementado; relaciones y errores representados en coleccion. |

Mi dictamen: avance funcional alto y suite actual recuperada. Documento 12 pruebas aprobadas, incluida conexion/esquema real. El filtro de bitacora sigue sin capturar registros anteriores ni eliminados. Tambien encuentro implementaciones Java de curso, grupo y periodo, cuya coexistencia con Persona 3 requiere definir el servicio oficial por URL.

### Persona 3 - Alejandro

| HU / paginas PDF | Endpoint | Criterios que reviso | Estado y pendiente |
|---|---|---|---|
| ACD3 / 12-13 | `/curso`, `/curso/{id}`, `/curso/carrera/{carreraId}` | CRUD, carrera, nivel 1-12 y nombre letras/espacios | Compilacion aprobada; falta bitacora con contenido obligatorio y suite automatizada propia. |
| ACD4 / 13-14 | `/grupo` | CRUD, curso/profesor/horario/cupo/periodo | Compilacion aprobada y middleware real; bitacoras describen accion/ID sin JSON. |
| ACD5 / 14-15 | `/periodo` | CRUD, anio/numero/fechas | Compila, pero arranque actual bloqueado; consultas tampoco devuelven `PeriodoId`. |
| MAT4 / 19-20 | `/provincias`, `/cantones/{provinciaId}`, `/distritos/{provinciaId}/{cantonId}` | Parametros y correspondencia territorial | Consulta SQL jerarquica implementada; arranque actual bloqueado. |

Mi dictamen: codigo presente y compilacion resuelta en esta revision, pero dos servicios no son ejecutables en la version auditada. Las capturas P3 son evidencia de otra ejecucion; no las uso para negar el fallo actual. Necesito identificar el commit y la configuracion que produjeron esas respuestas y repetir los casos sin token.

### Persona 4 - Fabian

| HU / paginas PDF | Endpoint | Criterios que reviso | Estado y pendiente |
|---|---|---|---|
| MAT3 / 18-19 | `/expediente` | CRUD, datos/direccion/telefonos, nombre, email y dominio | Implementado con pruebas H2; integracion real de creacion depende de MAT4 disponible. |
| MAT1 / 16-17 | `/prematricula` | CRUD, observaciones opcionales, primer nivel y periodo futuro | Implementado y probado localmente; falta recorrido CRUD real completo documentado. |
| ACA1 / 21-22 | `/historialacademico` | Tipo/identificacion, codigo/nombre/promedio por curso | Implementado y probado con MAT5 simulado; solo contempla matriculas ACTIVA y detecta intentos ambiguos. |
| ACA2 / 22-23 | `/listadoestudiantes` | Periodo requerido y campos del estudiante/carrera/curso/grupo | Implementado con consumo MAT2; captura de lista vacia no acredita listado con estudiantes. |

Mi dictamen: cobertura automatizada fuerte con 251 pruebas aprobadas, sin certificacion integral real. El archivo `docs/evidencia/fabian/alcance-capturas.json` limita expresamente sus diez capturas a seguridad y consultas, sin acreditar CRUD completo, historial con notas ni listado con matriculas. No actualizo ese alcance solo porque la coleccion tenga 85 solicitudes.

### Persona 5 - Jose

| HU / paginas PDF | Endpoint | Criterios que reviso | Estado y pendiente |
|---|---|---|---|
| MAT2 / 17-18 | `/matricula` | Crear/modificar/anular/listar, requeridos, curso/grupo/periodo | Implementado y con capturas; falta validar estudiante antes de SQL y resolver ambiguedad de periodo del PDF. |
| MAT5 / 20-21 | `/cargardesglose`, `/asignarnotarubro`, `/obtenerdesglose`, `/obtenernotas` | Suma 100, reemplazo, bloqueo con notas, rango 1-100 | Implementado, pruebas y capturas; casos de datos adicionales no acreditados integralmente. |
| IPN1 / 23-24 | `/factura` | Encabezado-detalle, costo 30000, impuesto 2%, pendiente y reverso | Implementado; captura revisada muestra 30000+600=30600. Request adaptado a `matriculaId`. |
| IPN2 / 24-25 | `/pago` | Pago/reverso, consulta/listado y estados de factura | Implementado con transacciones, pruebas y capturas; concurrencia no verificada. |
| IPN3 / 25 | `/notificar` | Email/asunto/HTML, SMTP configurable y token | Implementado; captura revisada acredita recepcion historica por correo, no envio nuevo en esta auditoria. |

Mi dictamen: avance funcional y de evidencia alto, con 16 pruebas aprobadas. Las 61 capturas no equivalen a las 79 solicitudes de la coleccion ni a todos los criterios aprobados. Documento un incumplimiento transversal de bitacoras, aunque haya evidencia de persistencia en GEN1.

Considero que SMTP por variables de ambiente satisface la capacidad de parametrizacion descrita en IPN3: el PDF no exige expresamente almacenar la configuracion SMTP mediante USR3. Distingo esa exigencia del diseno propuesto en el roadmap.

## Mis hallazgos principales

| ID | Prioridad | Hallazgo y fundamento | Accion que propongo |
|---|---|---|---|
| A4-01 | Critica | Periodo y Direccion terminan con `InvalidOperationException: No public 'Invoke' or 'InvokeAsync'`. Registros en [Periodo/Program.cs](../../NuevoAvatar.Periodo/Program.cs#L51) y [Direccion/Program.cs](../../NuevoAvatar.Direccion/Program.cs#L49); clases vacias en ambos proyectos. | Implementar middleware real; repetir arranque y 401/200. No retirar la proteccion como solucion. |
| A4-02 | Alta | Bitacoras P2/P5 almacenan request, pero PUT no incluye estado anterior y DELETE solo registra solicitud. [P2 BitacoraFilter](../../services/persona2/src/main/java/cr/ac/cuc/nuevoavatar/persona2/seguridad/BitacoraFilter.java#L75), [P5 BitacoraFilter](../../services/persona5/src/main/java/cr/ac/cuc/nuevoavatar/persona5/seguridad/BitacoraFilter.java#L70). Curso solo registra metodo/ruta; Grupo/Periodo accion e ID. La captura GEN1 de P5 confirma una eliminacion sin JSON. | Capturar estado previo y resultado persistido; comprobar JSON nuevo, anterior/actual y eliminado segun PDF pp.26-27. |
| A4-03 | Alta | Doc Master declara ser una propuesta, tiene cero dibujos y no contiene la matriz final de pruebas con capturas. No encuentro diagramas completos BD/casos de uso/clases ni bibliografia final desarrollada. | Completar entregables PDF p.29, no solo reunir colecciones. |
| A4-04 | Alta | Evidencia P3 muestra respuestas 200, pero la version actual no arranca Periodo/Direccion. Evidencia P4 declara alcance parcial; no hay certificacion integral de todos los servicios reales. | Identificar version de capturas y generar evidencia nueva de un recorrido integrado reproducible. |
| A4-05 | Media | MAT2 valida oferta y duplicados, no existencia del estudiante. La FK existe en PrograV, pero `saveAndFlush` puede producir excepcion de integridad que el handler general convierte en 500. [MatriculaService](../../services/persona5/src/main/java/cr/ac/cuc/nuevoavatar/persona5/matricula/MatriculaService.java#L80). | Validar expediente o traducir la infraccion de FK a un error funcional; agregar caso de estudiante inexistente. |
| A4-06 | Media | PDF p.18 llama activo al periodo y agrega fecha de inicio posterior a la actual. El codigo exige inicio <= hoy <= fin. [MatriculaService](../../services/persona5/src/main/java/cr/ac/cuc/nuevoavatar/persona5/matricula/MatriculaService.java#L120). | Acordar interpretacion con el profesor y documentar casos de frontera; no marcar conformidad literal sin aclaracion. |
| A4-07 | Media | IPN1 recibe solo `matriculaId`; el PDF describe identificacion del estudiante y monto. [CrearFacturaRequest](../../services/persona5/src/main/java/cr/ac/cuc/nuevoavatar/persona5/factura/CrearFacturaRequest.java). El calculo coincide, pero no el contrato de entrada literal. | Documentar/validar la adaptacion o ofrecer el contrato esperado, evitando aceptar montos inconsistentes. |
| A4-08 | Media | No hay matriz unica con HU, cada criterio, request, resultado y captura. P5 conserva un enlace a `POST_Login_Persona5.PNG` que no existe; la captura disponible es `USR5_01_Login_201.PNG`. | Indexar la evidencia real y corregir enlaces; separar casos preparados de casos ejecutados. |
| A4-09 | Media | Configuracion no uniforme: P2/P4 requieren variables del proceso, P5 y .NET cargan `.env`; P4 usa `DIRECCION_BASE_URL` por defecto puerto 5227, mientras la captura P3 usa HTTPS 7264. | Publicar tabla real de URLs/puertos y carga de variables por servicio; no asumir que importar Postman configura el backend. |
| A4-10 | Media | Propuesta asigna ACA2 a Ramses, ACD6 a Fabian e IPN3 a Alejandro; roadmap/Doc Master/codigo los asignan a Fabian, Ramses y Jose. Ademas roadmap y Doc Master declaran 24 HU aunque enumeran 22. | Acordar y actualizar la distribucion vigente y el total, manteniendo trazabilidad de la propuesta original. |
| A4-11 | Media | Periodo retorna `PeriodoRequest` sin `PeriodoId`; obliga a conocer IDs por otra via para grupos/matriculas. [PeriodoRepository](../../NuevoAvatar.Periodo/Repository/PeriodoRepository.cs). | Separar DTO de entrada/salida e incluir identificador en consultas. |
| A4-12 | Media | `EXPJWT` y `EXPREFRESH` se validan como positivos independientemente; puede configurarse refresh <= JWT contra USR5. [AuthService](../../services/seguridad/NuevoAvatar.Seguridad.DataAccess/AuthService.cs#L85). | Validar relacion de duraciones y probar valores incompatibles. |

Documento A4-05 y A4-12 por inspeccion de codigo, no como solicitudes HTTP nuevas ejecutadas. Distingo esas rutas de error de A4-01, que reproduzco directamente.

### Riesgos adicionales que mantengo abiertos

- No encuentro pruebas de concurrencia para pagos, facturas o reemplazo de rubros. Las comprobaciones previas a guardar no demuestran por si solas ausencia de carreras concurrentes.
- Los clientes P2/P5 y el filtro Curso permiten continuar aunque falle el registro remoto de bitacora; un 2xx de negocio no garantiza auditoria persistida.
- ACA1 y MAT5 consultan solo matriculas activas. Una anulacion deja notas persistidas pero las excluye de esos consumos; tengo pendiente acordar la politica del historial y probar rematriculas/intentos repetidos con servicios reales.
- El seed de Jose crea un usuario ADMIN con dominio `cuc.cr`, una excepcion al criterio USR1 para estudiantes. No lo considero evidencia positiva de cumplimiento de rol/dominio; propongo usar un administrador `cuc.ac.cr` para esa demostracion.
- No recreo todo el esquema desde cero ni aplico migraciones; la presencia de scripts y FK actuales no certifica el procedimiento final de instalacion.

## Evidencia y limites de cobertura

| Fuente | Cantidad / resultado | Interpretacion que aplico |
|---|---|---|
| P1/P2: JSON HTTP del 5 de octubre | 43 + 56 solicitudes; 56 + 72 aserciones; cero fallos registrados | Evidencia historica de ejecutor HTTP Node, complementada por capturas de Runner; no ejecucion nueva de esta auditoria. |
| `docs/evidencia/alejandro` | 20 PNG | Inventario; reviso visualmente dos consultas 200. No certifica seguridad ni version actual completa. |
| `docs/evidencia/fabian` | 10 PNG y manifiesto de alcance | Cuatro rechazos sin token y seis consultas autenticadas; tres listas vacias. |
| `docs/evidencia/persona5` | 61 PNG | Evidencia ampliada de MAT2/MAT5/IPN1/IPN2/IPN3/GEN1/USR5; muestra visual de tres capturas. |
| Colecciones P1/P2/P4/P5 | 43 / 56 / 85 / 79 solicitudes preparadas | No sumo estos numeros como solicitudes ejecutadas. |
| Colecciones separadas P4 | MAT3 43, MAT1 30, ACA1 6, ACA2 4 | Existen recorridos solapados; no los sumo como cobertura independiente. |

Para cerrar la evidencia propongo una fila por criterio del PDF, con HU, pagina, descripcion, solicitud, resultado esperado, observado, archivo de captura y version del codigo. Marco expresamente cualquier criterio sin ejecucion real.

## Barras de progreso por persona

Conservo la seccion de avance, pero sustituyo los porcentajes subjetivos anteriores por **cobertura de presencia de implementacion**. No los presento como porcentaje de aprobacion ni de entrega terminada.

| Persona | HU con implementacion / asignadas | Barra de presencia | Cierre que documento |
|---|---:|---|---|
| Hector | 5/5 | `[####################]` | Pruebas actuales e historial HTTP; pendientes transversales. |
| Ramses | 4/4 | `[####################]` | Suite recuperada; bitacoras y evidencia final pendientes. |
| Alejandro | 4/4 | `[####################]` | Dos arranques bloqueados; pruebas propias pendientes. |
| Fabian | 4/4 | `[####################]` | Suite local amplia; integracion real y capturas completas pendientes. |
| Jose | 5/5 | `[####################]` | Evidencia ampliada; bitacora y contratos/limites pendientes. |

## Avance del proyecto

Documento **22/22 HU con implementacion visible**, **284/284 pruebas ejecutadas aprobadas** y **4/4 builds .NET de Persona 3 aprobados**. Documento tambien **2/2 intentos de arranque auditados de Periodo/Direccion fallidos**.

No calculo un porcentaje de cumplimiento global porque falta una matriz completa por criterio y entregable. El 84% estimado de la auditoria 3 era una valoracion subjetiva; no lo convierto en una medicion comparable a los indicadores actuales. Considero que la evidencia y la verificacion mejoraron, pero los bloqueos confirmados impiden declarar el proyecto terminado.

## Acciones que propongo

| Orden | Accion | Responsable segun roadmap | Evidencia necesaria para cerrar |
|---:|---|---|---|
| 1 | Corregir middleware de Periodo/Direccion y validar version compartida | Alejandro | Arranque desde checkout, 401 sin token, 200 con token y bitacora. |
| 2 | Completar JSON requerido en bitacoras y registro de errores tecnicos | Equipo; contrato GEN1 con Hector | Capturas/consultas de creacion, anterior/actual, eliminado y errores por servicio. |
| 3 | Alinear URLs, variables, implementacion oficial y puertos | Equipo | Guia de ejecucion por servicio probada en la computadora compartida. |
| 4 | Ejecutar direcciones -> expediente -> prematricula -> matricula -> notas | Alejandro, Fabian, Jose | Fixture dedicado y capturas de operaciones positivas/negativas con servicios reales. |
| 5 | Ejecutar historial con notas y listado con estudiantes | Fabian y Jose | Promedio comprobable, campos completos, aislamiento de grupos/periodos e intentos. |
| 6 | Acordar periodo MAT2, contrato IPN1 y estudiante inexistente | Jose con equipo/profesor | Decision documentada y pruebas del contrato acordado. |
| 7 | Completar Doc Master y matriz criterio-evidencia | Equipo | Diagramas BD/casos de uso/clases, pruebas finales, conclusiones y bibliografia. |
| 8 | Alinear asignaciones, 22 HU y referencias a archivos | Equipo | Documentacion consistente sin enlaces a capturas inexistentes. |
| 9 | Comprobar instalacion desde BD vacia en ambiente aislado | Equipo | Orden de scripts y resultados sin alterar PrograV compartida. |
| 10 | Verificar tablero de HU y PR hacia rama final | Equipo | Enlaces y estados reales de la herramienta elegida. |

No propongo repetir indiscriminadamente toda la coleccion P5 sobre el grupo ya evaluado: sus notas bloquean el reemplazo de rubros y las anulaciones cambian los resultados posteriores. Para una nueva demostracion completa propongo preparar datos exclusivos y registrar sus IDs.

## Mi dictamen final

Considero superadas dos observaciones relevantes del avance 3: recupero la suite de Persona 2 y verifico la compilacion de los cuatro proyectos .NET. Tambien documento una mejora importante de evidencia de Persona 5, incluida recepcion de correo, y mantengo las 251 pruebas aprobadas de Persona 4.

No considero cerrado el proyecto contra el PDF. Reproduzco dos bloqueos de arranque, identifico incumplimientos concretos del formato de bitacora y mantengo pendientes los diagramas y la trazabilidad final por criterio. Las capturas existentes y las pruebas simuladas no sustituyen una demostracion integrada reproducible de todos los servicios reales.

Mi conclusion es que el trabajo debe concentrarse en estabilizacion, integracion y documentacion de entrega, con prioridad sobre nuevas funcionalidades. Mantengo este informe en primera persona y conservo los resultados historicos separados de lo que verifico actualmente.
