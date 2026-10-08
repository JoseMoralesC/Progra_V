# Persona 5 (Jose): recorrido Postman y evidencia

## Estado de este material

Prepare esta coleccion y la contraste con el codigo antes de las ejecuciones
contra la base real. Documento la evidencia inicial del login en
`docs/evidencia/persona5/POST_Login_Persona5.PNG`.
En esta guia presento los estados HTTP esperados y los distingo de los
resultados observados en mis capturas. Organizo 79 solicitudes, incluidas
pruebas de seguridad y casos opcionales. Considero exitoso un rechazo
cuando coincide con el resultado esperado.

## Fuentes y alcance

- [PDF del proyecto](../requerimientos/Proyecto%20Nuevo%20Avatar%20V1.pdf):
  MAT2 pp. 17-18, MAT5 pp. 20-21, IPN1 pp. 23-24,
  IPN2 pp. 24-25, IPN3 p. 25.
- [Asignacion persona 5](../roadmaps/roadmap%201.md), seccion Persona 5.
- [Servicio implementado](../../services/persona5/README.md).
- [Auditoria anterior](../auditoria/auditoria_avance3.md), seccion Persona 5.
- Controladores, DTOs, servicios y filtros de `services/persona5/src/main/java`.

Como persona 5, tengo asignadas MAT2, MAT5, IPN1, IPN2 e IPN3.
Utilizo cursos, grupos, periodos, expedientes y prematricula como
dependencias de otros integrantes, sin incorporarlos a mis historias.
Distingo el servicio `services/persona5` del proyecto `NuevoAvatar.Curso`.
Utilizo el login como preparacion, no como cobertura de mis cinco historias.

## Como preparo mis pruebas

1. Mantengo Tailscale conectado y SQL Server disponible.
2. Mantengo Seguridad en `http://localhost:5158`.
3. Compruebo las tablas con
   [consulta de preparacion](persona5-preparacion.sql) sobre la base elegida.
   El archivo solo consulta: no crea tablas ni modifica registros.
4. Si faltan tablas de persona 5, reviso las seis
   [migraciones](../../db/persona5/migraciones/README.md).
   Compruebo primero su estado: las migraciones fallan si las tablas ya existen.
5. Obtengo un curso, un grupo de ese curso y su periodo. Compruebo que
   la fecha actual este entre FechaInicio y FechaFin, inclusive.
   Utilizo un grupo exclusivo sin notas: al cargar desglose reemplazo
   sus rubros. Tengo en cuenta que las notas del recorrido quedan almacenadas.
6. Para los casos opcionales preparo otro curso, un grupo de periodo no
   vigente y un rubro de otro grupo. Utilizo IDs que existen en la base.
7. Configuro SMTP y un destinatario propio o buzon de prueba para el
   envio de correo. Distingo esta configuracion de la autenticacion JWT.

### Como inicio persona 5

Abro una segunda terminal PowerShell y conservo la de Seguridad.
Cargo automaticamente el `.env` del proyecto al iniciar el servicio Java.
Completo `MAIL_PASSWORD` con la contrasena de aplicacion de Gmail.
Como alternativa manual utilizo los comandos siguientes, considerando que
las variables de la terminal tienen prioridad sobre el `.env`.

```powershell
cd C:\Users\Personal\Desktop\PrograV\services\persona5
$env:SPRING_DATASOURCE_URL = 'jdbc:sqlserver://100.124.109.126:1433;databaseName=PrograV;encrypt=true;trustServerCertificate=true'
$env:SPRING_DATASOURCE_USERNAME = 'jose'
$env:SPRING_DATASOURCE_PASSWORD = '<clave de SQL Server, incluida la almohadilla final>'
$env:SEGURIDAD_URL = 'http://localhost:5158'
..\persona2\mvnw.cmd -f pom.xml spring-boot:run
```

Utilizo Java 21 y espero el mensaje de inicio en el puerto 8085.
Si Hibernate informa una tabla o columna faltante, reviso la estructura
antes de probar endpoints. Configuro `ddl-auto=validate` y administro las
tablas mediante SQL. Consulto Swagger en `http://localhost:8085/swagger-ui.html`.

Para SMTP, configuro antes de iniciar el servicio:
`MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`,
`MAIL_SMTP_AUTH`, `MAIL_SMTP_STARTTLS` y `MAIL_FROM`,
segun mi servidor de correo. Considero que, sin configuracion local,
el servicio intenta conectarse a localhost:25.

## Como importo y configuro Postman

1. En Postman, utilizo el menu de importacion (Import) y selecciono
   [Persona5_Jose.postman_collection.json](postman/Persona5_Jose.postman_collection.json).
2. En Collections, selecciono la coleccion
   `Jose - Persona 5 - Recorrido y evidencia`.
3. Abro Variables y completo los valores de ejecucion. Mantengo
   No Environment seleccionado para evitar variables de otro entorno
   con el mismo nombre y retiro valores de mayor prioridad si interfieren.
4. Abro la carpeta 00 y envio `00.1 Login`. Esta peticion guarda el token.
5. Envio `00.2 Validar token` y compruebo 200 y `true`.
6. Abro las carpetas y envio las solicitudes individualmente en el orden
   indicado. Los scripts guardan IDs y comprueban respuestas en Test Results.
   Evito ejecutar toda la coleccion con Runner: las carpetas 06 y 09 necesitan
   preparacion adicional.

| Variable | Valor/configuracion |
|---|---|
| baseUrl | http://localhost:8085 |
| seguridadUrl | http://localhost:5158 |
| usuario | jose |
| contrasena | La clave que utilizo en mi login |
| cursoId, grupoId, periodoId | IDs reales compatibles, obtenidos del SQL |
| grupoPruebasConfirmado | true, solo tras elegir grupo exclusivo de pruebas |
| estudiante | P5-JOSE-ESTUDIANTE; requiere expediente, igual que P5-JOSE-ESTUDIANTE-M |
| idInexistente | 2147483647; compruebo que no existe en las tablas consultadas |
| emailDestino | Mi correo/buzon de pruebas; necesario solo para 06.3 |
| token y los IDs creados | Los guarda automaticamente la coleccion |
| otroCursoId | Otro curso real, diferente al curso del grupo principal |
| periodoInactivoId, grupoInactivoId | Periodo fuera de vigencia y grupo del mismo curso principal |
| rubroOtroGrupoId | Rubro real de un grupo distinto al principal |

Utilizo la autenticacion heredada de la coleccion:
`Authorization: Bearer {{token}}`. Reservo los headers usuario y
contrasena para el login.
Para POST/PUT, Body ya esta configurado como raw / JSON.
Los GET y los reversos no requieren cuerpo.
Si vence el token, repito solo 00.1 y 00.2, sin repetir las creaciones.

## Matriz de recorrido principal

Utilizo como prefijo de todas las rutas de negocio `{{baseUrl}}`.

| HU / caso | Metodo y ruta | Solicitud / verificacion | HTTP |
|---|---|---|---|
| MAT2 crear | POST /matricula | Identificacion, cursoId, grupoId, periodoId; guardar matriculaId; ACTIVA | 201 |
| MAT2 consultar | GET /matricula?cursoId=...&grupoId=... | La lista contiene la matricula creada | 200 |
| MAT2 modificar | PUT /matricula/{{matriculaId}} | Cambiar identificacion y comprobarla en una consulta posterior | 200 |
| MAT2 duplicada | POST /matricula | Repetir los mismos datos mientras esta activa | 409 |
| MAT2 requeridos | POST /matricula | Enviar {} | 400 |
| MAT2 referencias | POST /matricula | Curso o grupo inexistentes | 422 |
| MAT2 no encontrada | PUT /matricula/{{idInexistente}} | No existe la matricula | 404 |
| MAT5 cargar | POST /cargardesglose | Examen 40 y Proyecto 60; guardar IDs | 200 |
| MAT5 consultar | GET /obtenerdesglose?grupoId=... | Suma 100 | 200 |
| MAT5 reemplazar | POST /cargardesglose | Cambiar a 50 y 50 antes de asignar notas; guardar nuevos IDs | 200 |
| MAT5 suma incorrecta | POST /cargardesglose | 40 y 50; confirmar que el desglose anterior se conserva | 422 |
| MAT5 asignar | POST /asignarnotarubro | matriculaId, rubroId, nota 85 | 200 |
| MAT5 modificar/limites | PUT /asignarnotarubro | Cambiar a 1, 100 y 90; mismo ID de nota | 200 |
| MAT5 segundo rubro | POST /asignarnotarubro | Proyecto con nota 80 | 200 |
| MAT5 obtener notas | GET /obtenernotas?identificacionEstudiante=...&cursoId=...&grupoId=... | Dos rubros, notas 90 y 80, pesos 50 y 50 | 200 |
| MAT5 rango invalido | PUT /asignarnotarubro | 0 y 101 | 400 |
| MAT5 bloqueo | POST /cargardesglose | Intentar reemplazar rubros despues de asignar notas | 409 |
| MAT5 referencias | POST /asignarnotarubro | Rubro inexistente | 422 |
| MAT5 sin matricula | GET /obtenernotas?... | Identificacion sin matricula activa | 404 |
| IPN1 crear | POST /factura | matriculaId; subtotal 30000, impuesto 600, total 30600, PENDIENTE | 201 |
| IPN1 encabezado-detalle | GET /factura/{{facturaId}} | Datos estudiante/periodo y detalle Servicios estudiantiles por 30000 | 200 |
| IPN1 listar | GET /factura?periodoId=... | Incluye factura creada | 200 |
| IPN1 duplicada | POST /factura | Misma matricula con factura activa | 409 |
| IPN1 referencias | POST /factura | Matricula inexistente | 422 |
| IPN1 no encontrada | GET /factura/{{idInexistente}} | Factura inexistente | 404 |
| IPN2 crear | POST /pago | facturaId; monto 30600, APLICADO | 201 |
| IPN2 comprobar efecto | GET /factura/{{facturaId}} | PAGADA | 200 |
| IPN2 consultar/listar | GET /pago/{{pagoId}}; GET /pago?periodoId=... | Pago creado incluido en el periodo | 200 |
| IPN2 duplicado | POST /pago | Intentar pagar factura pagada | 409 |
| IPN1 bloqueo | PUT /factura/{{facturaId}}/reversar | No se anula factura pagada | 409 |
| IPN2 reversar | PUT /pago/{{pagoId}}/reversar | Pago ANULADO | 200 |
| IPN2 comprobar efecto | GET /factura/{{facturaId}} | PENDIENTE nuevamente | 200 |
| IPN2 repetir reverso | PUT /pago/{{pagoId}}/reversar | Pago ya anulado | 409 |
| IPN2 referencias | GET /pago/{{idInexistente}}; POST /pago con factura inexistente | No encontrado / referencia invalida | 404 / 422 |
| IPN1 reversar | PUT /factura/{{facturaId}}/reversar | ANULADA, confirmar con GET | 200 |
| IPN2 factura anulada | POST /pago | No pagar factura anulada | 409 |
| IPN3 validacion | POST /notificar | Email invalido o {} | 400 |
| IPN3 enviar | POST /notificar | email, asunto, cuerpoHtml; ENVIADA; comprobar recepcion | 200 |
| MAT2 eliminar, al final | DELETE /matricula/{{matriculaId}} | Anulacion logica; deja de aparecer en activos | 204 |
| MAT2 eliminar inexistente | DELETE /matricula/{{idInexistente}} | No encontrada | 404 |
| MAT5/IPN1 anulada | POST /asignarnotarubro; POST /factura | Matricula anulada no admite nota ni factura | 422 |

### Casos opcionales de integridad

Ejecuto la carpeta 09 despues de 03 y antes de 07, mientras la matricula
esta ACTIVA. Agrupo bajo 09 los casos que requieren datos adicionales;
no interpreto su numero como indicacion de ejecutarlos despues de anularla.

- Curso existente distinto al del grupo: 422.
- Periodo distinto al del grupo: 422.
- Grupo y periodo compatibles, pero periodo no vigente: 422.
- Rubro existente de otro grupo: 422. Con matricula anulada se obtiene otro
  rechazo y no se demuestra la regla de pertenencia del rubro.

## Seguridad y bitacoras

Con la carpeta 01 compruebo 401 sin token en las 18 combinaciones
metodo/ruta de negocio y con un token invalido. Utilizo IDs de ejemplo y
compruebo que el rechazo ocurra antes de validar datos. Para probar un
token vencido, conservo el anterior, lo uso despues de su vencimiento y
espero 401.
Un 401 con token recien generado puede indicar que Seguridad no esta
disponible para el servicio Java.

Con la carpeta 08 consulto `GET {{seguridadUrl}}/bitacora`.
Busco entradas recientes de mi usuario con las rutas /matricula,
/cargardesglose, /asignarnotarubro, /factura, /pago y /notificar.
Las creaciones/modificaciones incluyen JSON; consultas y eliminaciones
tienen descripciones especificas.
Guardo registros con fecha, usuario, descripcion e ID.
Un 200 de negocio no demuestra por si solo la bitacora: el cliente captura
el fallo al registrarla y permite que la operacion termine.
Las solicitudes sin token no llegan al registro de este filtro.
Consulto la bitacora como dependencia GEN1, sin agregarla a mis HU.

## Evidencia que guardo

Para cada caso ejecutado documento lo siguiente:

1. Tomo una captura de Postman con nombre de solicitud, metodo, URL, estado HTTP y
   respuesta. Para POST/PUT tomo otra captura del Body cuando no quepa
   junto a la respuesta. Incluyo Test Results en las comprobaciones.
2. Guardo las capturas en `docs/evidencia/persona5`, usando por ejemplo
   `MAT2_01_Crear_201.PNG`, `MAT5_14_Bloqueo_409.PNG`,
   `IPN2_08_FacturaPendiente_200.PNG`.
3. Para el correo guardo la respuesta ENVIADA y una captura del mensaje recibido con
   asunto y HTML. SMTP aceptando un mensaje no garantiza entrega al buzon.
4. Para encabezado-detalle guardo el JSON con detalles y, si necesito
   evidenciar el modelo, consulto las filas Factura/FacturaDetalle.
5. Llevo un registro con las columnas: HU, solicitud, esperado, observado,
   fecha, resultado (aprobado/fallido/pendiente), archivo de evidencia.
   Marco como aprobado unicamente un caso que envie y cuyo resultado verifique.

Exporto la coleccion al terminar para conservar las solicitudes y la
complemento con capturas, sin asumir que la exportacion incluye cada respuesta.
Si un caso devuelve 500, guardo el error y reviso el log; no lo considero
un rechazo funcional correcto.

## Diferencias y limites que documento

- MAT2, PDF p.18: dice periodo activo, pero entre parentesis indica inicio
  posterior a hoy. El codigo usa FechaInicio <= hoy <= FechaFin.
  Pruebo el comportamiento actual y mantengo pendiente aclarar esta
  contradiccion con el profesor antes de afirmar cumplimiento de ambas reglas.
- IPN1 pide identificacion y monto. El contrato actual recibe matriculaId,
  deriva identificacion/periodo y fija el precio en 30000.
  Describo este contrato en mi evidencia y mantengo pendiente confirmar su aceptacion.
- La base PrograV tiene FK_Matricula_Estudiante: el expediente debe existir.
  Preparo ambos expedientes con persona5-estudiantes-prueba.sql antes del
  recorrido; el segundo se usa en 02.4. Este script incluye catalogos
  territoriales ficticios etiquetados como pruebas, no datos oficiales.
  MAT2 no consulta el servicio de expediente ni prematricula. La relacion
  SQL no acredita la integracion REST con esos servicios.
- La modificacion y eliminacion de matricula no sincronizan automaticamente
  factura o notas. Por eso anulo primero pago y factura.
- SMTP es configurable con variables de ambiente; no se lee desde /parametro.
  Mantengo pendiente confirmar si esta forma satisface el criterio de parametrizacion.
- POST y PUT de notas llaman al mismo servicio: ambos actualizan una nota
  existente. Documento la modificacion con PUT, sin afirmar que POST
  rechace una nota ya creada.
- No hay endpoint de borrado de notas/rubros. Al terminar se conservan notas,
  rubros y registros anulados. Para repetir el recorrido utilizo otro
  grupo exclusivo sin notas, restablezco estudiante a P5-JOSE-ESTUDIANTE,
  vacio los IDs generados y obtengo un nuevo login; no cambio unicamente
  la identificacion.
