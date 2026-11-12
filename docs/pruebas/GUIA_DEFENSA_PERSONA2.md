# Guía de defensa: Persona 2 (Ramses)

Adaptación de las 75 preguntas del documento del profesor a Nuevo Avatar. Respuestas modelo basadas en el código revisado el 8 de octubre de 2026. Uso la primera persona para ensayar la explicación; no sustituyo la comprensión del código ni afirmo autoría individual de archivos sin revisar el historial.

## Antes de estudiar

El ejemplo del profesor habla de Carnet Digital. Mi proyecto es Nuevo Avatar y mis historias del roadmap son USR1 (usuarios), ACD1 (instituciones), ACD6 (profesores) y ACD2 (carreras). No presento QR, fotografía, autorregistro, áreas o pantallas por rol como funciones implementadas en mi parte.

Mi servicio está en `services/persona2`, usa Java 21 y Spring Boot y escucha en el puerto 8082. Seguridad es de Persona 1 (Héctor), usa .NET y escucha en el puerto 5158. Utilizo SQL Server y la base compartida PrograV por Tailscale.

## A. Proyecto y servicios (1-8)

**1. ¿Cuál es el propósito general de Nuevo Avatar?**
El sistema integra la gestión académica y administrativa: usuarios, seguridad, oferta académica, matrícula, expedientes, notas, facturación, pagos y notificaciones. Mi parte administra usuarios, instituciones, profesores y carreras.

**2. ¿Qué se desarrolla en este alcance?**
Trabajamos los servicios REST, persistencia, reglas de negocio e integración entre servicios. En mi entrega demuestro los cuatro CRUD asignados, seguridad y pruebas; no una interfaz gráfica completa.

**3. ¿Por qué construir primero servicios REST?**
Defino y pruebo las operaciones y sus contratos antes de depender de una pantalla. Después un gateway o una interfaz pueden consumir esas mismas APIs.

**4. ¿Qué servicio trabajás y qué responsabilidad tiene?**
Trabajo Persona 2, en Java Spring Boot: USR1, ACD1, ACD6 y ACD2. Mantengo las relaciones de carreras con instituciones y profesores directores.

**5. ¿Monolito y microservicios son lo mismo?**
No. Un monolito se despliega como una aplicación; una arquitectura de microservicios divide responsabilidades en procesos y contratos independientes. Nuestro proyecto separa servicios, pero comparte base de datos, por lo que no tiene aislamiento completo de datos por servicio.

**6. ¿Qué ventaja tiene la división?**
Puedo trabajar y probar mi servicio por separado y coordinarme mediante HTTP. Seguridad centraliza autenticación y bitácora; los servicios académicos se concentran en sus reglas.

**7. ¿Qué sucede sin coordinación?**
Pueden no coincidir rutas, campos, puertos, códigos HTTP o nombres de tablas. Una API puede arrancar correctamente y aun así romper la integración.

**8. ¿Cómo preparo mi API para futuras interfaces?**
Uso recursos identificables, JSON, códigos HTTP, DTO de entrada/salida y documentación OpenAPI/Swagger. Evito devolver entidades con hashes de contraseña.

## B. REST y HTTP (9-18)

**9. ¿Qué significa respetar REST?**
Expongo recursos mediante URI y empleo la semántica de HTTP. Cada petición protegida incluye su token; no depende de una pantalla ni de una sesión gráfica previa.

**10. Mostrá una ruta y explicá el recurso.**
`GET /institucion/7` consulta la institución identificada por 7. La ruta representa el recurso y GET expresa la operación.

**11. ¿Por qué /institucion y no /crearInstitucion?**
Porque el verbo HTTP ya indica la acción: POST crea y GET consulta sobre la misma colección. En mi código las rutas están en singular: `/usuario`, `/institucion`, `/profesor` y `/carrera`.

**12. ¿Qué verbos utilizás?**
POST para crear, GET para consultar, PUT para modificar y DELETE para eliminar. No implemento PATCH en los cuatro controladores de mi parte.

**13. ¿Qué diferencia hay entre esos verbos y PATCH?**
GET consulta; POST crea; PUT envía los datos de actualización definidos por el contrato; PATCH aplica cambios parciales; DELETE elimina. Mi PUT de usuario conserva la contraseña si no la envío, por lo que explico el contrato real y no lo llamo un reemplazo absoluto de todos los campos.

**14. ¿Implementaste PATCH para cambiar el estado del usuario?**
No: ese ejemplo del PDF no corresponde a mi contrato actual. Conceptualmente PATCH sería adecuado para cambiar solo el estado, pero no muestro una ruta inexistente ni digo que esté implementada.

**15. ¿Qué responde una creación exitosa?**
`201 Created`, el objeto creado y, en los controladores correspondientes, el encabezado Location con la ruta del recurso.

**16. ¿Qué responde una solicitud sin autorización válida?**
Mi interceptor responde `401` cuando falta el Bearer o Seguridad no lo valida. `403` sería para una identidad válida sin permiso, pero mi interceptor actual no implementa una matriz de permisos por rol.

**17. ¿Qué responde si los datos son inválidos?**
`400` para validaciones del DTO y `422` para reglas como rol inexistente, dominio incompatible o referencias inexistentes. No todos los errores de validación tienen el mismo código.

**18. ¿Por qué no devolver siempre 200?**
Ocultaría errores y obligaría al cliente a adivinar el resultado. Uso también 404 para ausencia, 409 para conflictos y 500 para errores internos.

## C. Login y JWT (19-29)

**19. ¿Cómo funciona /login?**
Seguridad busca la cuenta en `seguridad.usuario`, compara la contraseña con el hash BCrypt y, si coincide, genera un JWT y un refresh token. Yo consumo ese servicio; no emito el JWT desde Persona 2.

**20. ¿Qué recibe el login?**
En esta implementación recibe los headers `usuario` y `contrasena`, no un body JSON. Si faltan o están en blanco devuelve 400. Si la cuenta o la clave no coinciden devuelve 401.

**21. ¿Qué devuelve un login exitoso?**
`201` y la respuesta con access token, refresh token, vencimiento e identificador de usuario. Reviso los nombres JSON del contrato antes de escribir un script cliente.

**22. ¿Qué devuelve con credenciales incorrectas?**
`401`. La contraseña de la cuenta no es la clave SQL, la SMTP ni la clave de firma JWT.

**23. ¿Access token y refresh token?**
El JWT autoriza las peticiones protegidas. El refresh permite obtener nuevos tokens sin reenviar la contraseña y se persiste en Seguridad con vencimiento y estado de revocación.

**24. ¿Por qué vence el JWT?**
Limito el tiempo en que un token robado puede utilizarse. Tener vencimiento no reemplaza el cuidado de las credenciales ni HTTPS.

**25. ¿Cómo se parametrizan los cinco minutos?**
Seguridad consulta `EXPJWT` en seguridad.parametro`, usando cinco minutos si el parámetro no contiene un entero positivo. Para refresh consulta `EXPREFRESH`, con 60 minutos de respaldo.

**26. ¿Qué hace /refresh?**
Recibe el refresh token en el body. Rechaza uno inexistente, vencido o revocado; si es válido revoca el anterior y genera una nueva pareja. Responde 201 o 401.

**27. ¿Qué hace /validate?**
Seguridad valida la firma y el vencimiento del JWT. Devuelve `200` con `true` si es válido y 401 si no. Persona 2 lo llama mediante `SeguridadClient`.

**28. ¿Qué pasa sin token?**
`AutorizacionInterceptor.preHandle` bloquea la petición antes del controlador y responde 401. Si Seguridad está inaccesible, mi cliente devuelve false y actualmente también termina en 401; no implica necesariamente contraseña incorrecta.

**29. ¿Por qué no confiar solo en la interfaz?**
Cualquier cliente puede llamar directamente la API. Valido cada petición en el servidor. Decodificar el `sub` en `JwtUsuario` solo obtiene la identidad después de validar; decodificar Base64 no verifica una firma.

## D. Validaciones (30-38)

**30. Explicá una regla importante.**
Al crear una carrera verifico que exista la institución y que el director esté registrado como profesor. Si una referencia no existe respondo 422 antes de guardar.

**31. ¿Cómo rechazo nulos, vacíos o blancos?**
Uso `@NotBlank` en cadenas, `@NotNull` para valores requeridos y `@NotEmpty` para la lista de teléfonos. `@Valid` en el controlador activa estas validaciones. Recorto y normalizo datos cuando corresponde.

**32. ¿Cómo valido email?**
Uso `@Email` junto a `@NotBlank` para formato y obligatoriedad. Después aplico las reglas de dominio en el servicio; formato válido no significa dominio autorizado.

**33. ¿Cómo valido teléfonos?**
En el código actual exijo una lista no vacía, elementos no blancos y teléfonos no repetidos tras recortar espacios. No encontré una validación que exija solo números en `ProfesorRequest` ni en `normalizarTelefonos`. Si el criterio la exige, debo implementarla y probarla; no afirmo que ya esté.

**34. ¿Cómo relaciono dominio y rol?**
En `UsuarioService.validarRolYDominio`, `@cuc.cr` requiere ESTUDIANTE y `@cuc.ac.cr` requiere PROFESOR o ADMIN. Primero compruebo que exista el rol. Esas reglas están fijas en este método; el dominio de profesores, en cambio, se obtiene de DOMPROF.

**35. ¿Servicio y base de datos validan lo mismo?**
El servicio ofrece reglas y mensajes adecuados al cliente. SQL protege integridad con claves, restricciones y relaciones aunque haya accesos por otras vías. Uso ambas capas.

**36. ¿Qué pasa cuando falla una validación?**
No guardo el recurso y devuelvo 400 o 422 según el caso. `ApiExceptionHandler` arma el error con status, message, path y errores por campo cuando existen.

**37. ¿Qué hago con un duplicado?**
Devuelvo 409 para email de usuario duplicado y restricciones únicas de profesor. No digo que todos los nombres sean únicos: depende de la entidad y del esquema.

**38. ¿Y si modifico o elimino algo inexistente?**
Busco el recurso y respondo 404 si no existe. No simulo que la operación tuvo éxito.

## E. Base de datos y relaciones (39-47)

**39. Explicá una tabla de tu parte.**
`academico.Carrera` tiene CarreraId, Nombre, InstitucionId y DirectorProfesorId. La identifico por una PK y relaciono institución y director mediante FK. En Java la represento con `Carrera` y la devuelvo con `CarreraResponse`.

**40. ¿Qué motor utilizás y por qué?**
SQL Server: es el motor acordado por el equipo y permite integridad referencial, transacciones y conexión desde Java y .NET. No invento una comparación de rendimiento que no medimos.

**41. ¿Una base o una por servicio?**
Actualmente utilizamos PrograV compartida, organizada por esquemas. Facilita la integración académica, pero introduce acoplamiento y obliga a coordinar cambios. Los servicios corren localmente; SQL se alcanza por Tailscale.

**42. ¿Usuario tiene una institución asociada directamente?**
En mi entidad actual no existe esa relación directa. La relación con institución aparece en Carrera. Adapto el ejemplo del PDF sin inventar un campo InstitucionId en Usuario.

**43. ¿Qué relación tiene el usuario con el rol?**
`seguridad.usuario.id_rol` referencia el rol. No confundo esta asociación con permisos por pantalla: mi parte no implementa tablas rol-pantalla.

**44. ¿Cómo se asocia un estudiante con una carrera?**
Es una integración con expediente y matrícula de otros servicios. Mi CRUD Usuario no guarda una colección de carreras; Carrera aporta el catálogo que esos módulos consumen.

**45. ¿Cómo se relaciona el director de una carrera?**
Uso DirectorProfesorId y exijo que el profesor exista. No utilizo un catálogo de áreas de funcionarios en mi parte.

**46. ¿Qué tabla adicional necesitás para teléfonos?**
`academico.ProfesorTelefono`: un profesor puede tener varios teléfonos. Tiene su identificador, ProfesorId, Telefono y unicidad por profesor y teléfono. Es una relación uno-a-muchos, no una tabla de muchos-a-muchos.

**47. ¿Cómo evito inconsistencias al eliminar?**
Uso FK y transacciones. Si hay dependencias bloqueadas respondo 409. Solo los teléfonos se eliminan en cascada con su profesor. En pruebas limpio carrera, profesor, institución y usuario ficticio en ese orden.

## F. Funciones de mi alcance (48-58)

**48. ¿Qué requiere una institución?**
Nombre obligatorio y solo letras y espacios, incluyendo letras acentuadas. `InstitucionRequest` usa `@NotBlank` y `@Pattern`; SQL también tiene restricciones.

**49. ¿Cómo relacionás carrera e institución?**
Con InstitucionId; lo valido y guardo como referencia. Puedo consultar `GET /carrera?institucionId=7` para filtrar.

**50. ¿Tu parte administra roles o permisos por pantalla?**
No administro el CRUD de roles: corresponde a Héctor. Yo consulto que el rol exista y verifico su compatibilidad con el dominio. No afirmo tener autorización granular por pantalla.

**51. ¿Por qué identificás usuario por email?**
En el contrato actual el email es la PK del usuario y la ruta de consulta usa ese identificador. Lo normalizo a minúsculas y evito duplicados. El documento de identificación también es un campo, pero no reemplaza el email en la ruta.

**52. ¿Tu alta de usuario es autorregistro público?**
No. `POST /usuario` está protegido por token. Un autorregistro público requeriría otro contrato y controles adicionales; no confundo ambas funciones.

**53. ¿Confirmás la cuenta por correo al crear usuario?**
Mi código actual no implementa ese flujo. El envío de notificaciones corresponde a Persona 5, pero tener SMTP no implica que el alta de mi usuario incluya confirmación de cuenta.

**54. ¿Cómo recibís los teléfonos de profesor?**
Como un arreglo JSON de cadenas en `telefonos`. No uso fotografía Base64 porque ese recurso no pertenece a mi alcance.

**55. ¿Qué devolvés al crear una carrera?**
JSON con id, nombre, institucionId y directorProfesorId. No genero QR en mi parte.

**56. ¿Cómo comprobás que el profesor sea adulto?**
Comparo fechaNacimiento con `LocalDate.now().minusYears(18)`. Si nació después de esa fecha respondo 422; exactamente 18 años pasa esta regla.

**57. ¿Qué conviene parametrizar?**
Dominios, vencimientos de tokens y configuración SMTP. Mi ProfesorService lee DOMPROF; si falta o está vacío responde 503. La conexión SQL y las claves permanecen en configuración local, no en Git.

**58. ¿Cómo registrás la bitácora?**
Mi filtro envía usuario y descripción a Seguridad. La fecha la asigna el lado servidor/persistencia, no el cliente. Oculto campos de contraseña/hash. Actualmente el filtro registra solicitudes; no conserva automáticamente una instantánea anterior y posterior para PUT ni el objeto eliminado para DELETE. Esa limitación de GEN1 debe explicarse.

## G. Pruebas y demostración (59-67)

**59. Mostrá una prueba de un endpoint propio.**
Demuestro `POST /institucion` con nombre válido y token. Después consulto el id devuelto, actualizo y elimino el registro ficticio. Finalmente consulto de nuevo y espero 404.

**60. Explicá el request completo.**
URL `http://localhost:8082/institucion`, método POST, headers `Authorization: Bearer <token>` y `Content-Type: application/json`, body `{"nombre":"Institucion Prueba"}`. Este POST no necesita query params. Un filtro por institución se usa en GET /carrera.

**61. Explicá el response.**
Espero 201, Location `/institucion/<id>` y un JSON como `{"id":7,"nombre":"Institucion Prueba"}`. El id 7 es ilustrativo: uso el generado por la base. No necesito un campo message en cada respuesta exitosa.

**62. ¿Cómo probás un caso exitoso?**
Compruebo código, campos y persistencia consultando el recurso creado. En el Runner guardo los identificadores de las altas para los siguientes pasos.

**63. ¿Cómo probás un caso fallido?**
Envío un nombre de institución inválido para obtener 400 o un usuario con rol inexistente para obtener 422. Un rechazo esperado cuenta como prueba aprobada si coincide con el contrato.

**64. ¿Cómo probás la protección?**
Repito una solicitud sin Authorization y espero 401; también pruebo un token inválido. Con un token válido, confirmar ESTUDIANTE debe devolver 200, no 401.

**65. ¿Cómo probás reglas de negocio?**
Uso duplicados, dominio ajeno, profesor menor de edad, teléfonos repetidos, referencias inexistentes y eliminación de registros relacionados. Los casos ejecutados no demuestran una regla de solo dígitos en teléfono que aún no está implementada.

**66. ¿Qué herramientas utilizaste?**
Las colecciones se ejecutaron primero con un ejecutor HTTP local y después en Postman Runner. También hay pruebas Java y Swagger para consultar el contrato. No llamo Newman al ejecutor local.

**67. ¿Qué evidencia existe?**
El 5 de octubre de 2026 se ejecutaron 99 solicitudes entre Persona 1 y Persona 2, con 128 comprobaciones aprobadas: 56 de Persona 1 y 72 de Persona 2. Mi colección tuvo 56 solicitudes; no confundo solicitudes con comprobaciones. Las capturas están en `docs/pruebas/evidencias/` y el resumen en `RESUMEN_PERSONA2.md`. Son resultados de esa fecha, no una nueva ejecución de hoy.

## H. Código y participación (68-75)

**68. ¿Qué historias te asignaron?**
USR1, ACD1, ACD6 y ACD2. El servicio también contiene clases de curso, grupo y periodo, pero su presencia en esta carpeta no cambia la asignación del roadmap a Persona 3. Distingo mi responsabilidad de la estructura del repositorio.

**69. ¿Qué clases debés dominar?**
UsuarioController/Service/Repository, InstitucionController/Service/Repository, ProfesorController/Service/Repository y CarreraController/Service/Repository; sus entidades y DTO. También explico AutorizacionInterceptor, SeguridadClient, BitacoraFilter, ApiExceptionHandler y application.properties. Para afirmar quién escribió cada cambio reviso Git, no solo el nombre de la carpeta.

**70. Mostrá código y explicalo.**
En `UsuarioService.crear`, normalizo email y rol, rechazo duplicados, valido rol y dominio, codifico la contraseña con BCrypt, guardo y convierto a UsuarioResponse. BCrypt es hash de una vía, no cifrado reversible. La respuesta no devuelve contraseña ni hash.

**71. ¿Cómo separás responsabilidades?**
El Controller maneja HTTP y valida el DTO; el Service aplica reglas y transacciones; el Repository usa JPA para persistir; la Entity mapea tablas; los DTO definen el contrato. Uso JdbcTemplate para consultar rol, parámetros y algunas referencias con argumentos, sin concatenar entrada del usuario en SQL.

**72. ¿Qué problema técnico enfrentaste?**
Hibernate reportó una tabla faltante y hubo un bloqueo por una transacción SQL abierta. Revisamos destino, esquema y nombres; la configuración actual usa PrograV, naming estándar y `ddl-auto=validate`. Acotamos transacciones, desactivamos open-in-view y verificamos DBCC OPENTRAN. No atribuyo toda la causa inicial solo a mayúsculas/minúsculas sin evidencia.

**73. ¿Qué aprendizaje podés explicar?**
Debo coordinar el contrato HTTP, la integridad SQL y la autenticación; una respuesta HTTP no sustituye una verificación de persistencia. Ensayo esta respuesta con mis propias palabras y ejemplos del código que comprendo.

**74. ¿Qué mejorarías?**
Añadiría la validación de teléfonos solo numéricos si el criterio lo exige, auditoría anterior/actual/eliminado para GEN1, pruebas automatizadas de mis reglas y manejo diferenciado de caída de Seguridad frente a JWT inválido. Evaluaría permisos por rol y parametrizaría dominios de usuario hoy fijos. No presento estas mejoras como terminadas.

**75. ¿Cómo coordinás los cambios?**
Uso la rama ramses, commits específicos e integración con dev. Coordino roles, dominios y bitácora con Héctor; carreras y profesores con Alejandro; estudiantes y oferta con los módulos de matrícula/expediente. Mantengo credenciales y compilados fuera de Git y reviso contratos antes de integrar.

## Orden de demostración para la defensa

1. Explico mis cuatro historias y cómo se relacionan con el equipo.
2. Muestro Seguridad en 5158 y Persona 2 en 8082; diferencio APIs locales de SQL por Tailscale.
3. Hago login, valido el token y consulto una ruta protegida.
4. Creo institución y profesor, luego carrera usando sus identificadores reales.
5. Muestro filtro por institución y una actualización.
6. Demuestro un rechazo 400, uno 422, uno 409 y uno 401 explicando cada causa.
7. Limpio solo registros de demostración y verifico 404.
8. Muestro Controller, Service, Repository, entidad, validaciones y captura del Runner.

## Alertas que no debo memorizar incorrectamente

- Mi alcance no es Carnet Digital: no tengo QR, fotografía ni autorregistro público.
- Hash BCrypt no es cifrado reversible. JWT firmado tampoco es información necesariamente cifrada.
- Mi interceptor valida identidad, no permisos granulares por rol; rol existente no significa permiso comprobado.
- El filtro de auditoría no prueba por sí solo todos los requisitos de GEN1.
- Mis teléfonos actualmente no tienen comprobación de solo dígitos.
- Haber aprobado las colecciones no certifica todas las historias ni todos los servicios del equipo.
- Nunca muestro claves SQL, SMTP, JWT ni tokens reales en una captura o exposición pública.

## Archivos del repositorio para practicar

- `docs/roadmaps/roadmap 1.md`: asignación de historias y dependencias.
- `services/persona2/src/main/java/cr/ac/cuc/nuevoavatar/persona2/`: código de los cuatro recursos, DTO, seguridad y errores.
- `services/seguridad/NuevoAvatarSeguridad/Controllers/AuthController.cs`: contrato del login, refresh y validate.
- `services/seguridad/NuevoAvatar.Seguridad.DataAccess/AuthService.cs`: BCrypt, emisión, expiración y validación de JWT.
- `docs/pruebas/RESUMEN_PERSONA2.md`: resultados históricos y enlaces a las evidencias.

No ejecuto nuevas pruebas ni modifico los servicios al preparar esta guía. La revisión identifica límites del código actual; no cambia sus funcionalidades.
