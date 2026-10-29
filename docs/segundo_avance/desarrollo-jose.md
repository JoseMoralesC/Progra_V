# Desarrollo de Jose - segundo avance

Fecha: 28 de octubre de 2026. Alcance: **ADM2, ADM17, GTW1 y GTW2**, segun el
[backlog](planificacion/backlog.md). Este documento describe implementacion;
no declara cerradas las cuatro historias ni sustituye sus criterios del PDF.

## Componentes implementados

| HU | Desarrollo de este incremento | Ubicacion |
|---|---|---|
| GTW1 | Host ASP.NET Core + YARP, puerto 5080, routing de las 22 familias de servicios, verbos/cuerpo/query/respuesta, reescritura de Location y error 502 de destino. | [Gateway](../../NuevoAvatar.Gateway/GatewayApplication.cs), [catalogo](../../NuevoAvatar.Gateway/CatalogoRutas.cs) |
| GTW2 | Integracion del middleware existente: /validate y /sesion antes del negocio, roles por ruta, rechazo de internos y limpieza de headers de identidad. | [Seguridad](../../NuevoAvatar.Gateway.Seguridad/SeguridadGateway.cs) |
| ADM2 | Componente React de login, contrato de headers del backend, almacenamiento de sesion de pestana, refresh compartido entre solicitudes, limpieza de cliente y registro de logout en Autenticacion. | [Cliente](../../web/jose/src/sesion.js), [Login](../../web/jose/src/Login.jsx), [SesionController](../../NuevoAvatar.Autenticacion/Controllers/SesionController.cs) |
| ADM17 | Componente React de desglose/notas por grupo y estudiante, validaciones, confirmaciones, POST/PUT y bloqueo del desglose; backend con snapshots de rubros y envio a bitacora despues de commit. | [Notas UI](../../web/jose/src/Notas.jsx), [AuditoriaNotas](../../NuevoAvatar.Notas/src/main/java/cr/ac/cuc/nuevoavatar/persona5/notas/AuditoriaNotas.java) |

La plantilla visual fue creada por **Alejandro**. Los componentes de Jose
importan su CSS existente sin modificar la fuente ni reemplazar el layout ADM1.
`web/jose` usa React, tecnologia admitida por V2. Su envoltorio de ejecucion
permite probar las dos funciones propias; no implementa los dashboards de
Fabian, el portal de Alejandro ni el mantenimiento de otros responsables.

## Ejecutar en desarrollo

Desde la raiz, iniciar primero las dependencias necesarias con las herramientas
existentes. Para login/identidad se necesitan Autenticacion y sus propietarios
Usuario/Rol, y Profesor o Expediente segun el perfil. Bitacora recibe los
registros. Para notas se necesitan Notas y los servicios de Grupo, Curso,
Periodo y Matricula; estos pueden tener dependencias propias. Configuracion
de los servicios V1: [manifiesto](../../tools/servicios.json) y `.env` local.

No se crean usuarios ni datos de negocio automaticamente al ejecutar la web.
Un profesor necesita cuenta vinculada por email/tipo/identificacion y grupos
reales del periodo activo. Las credenciales se ingresan en el formulario.

En una terminal:

```powershell
powershell -NoProfile -File tools/ejecutar-gateway.ps1
```

El gateway escucha en `http://localhost:5080`. No usa BD ni carga `.env`; sus
destinos por defecto coinciden con los puertos V1. Para otro entorno, definir
variables como `Destinos__Autenticacion`, `Destinos__Usuario`, `Destinos__Notas`,
etc., con un origen HTTP(S) sin ruta. `AllowedHosts` y la URL de escucha deben
configurarse expresamente si se expone fuera de localhost. El lanzamiento
local HTTP es para desarrollo; publicar credenciales requiere HTTPS.

En otra terminal:

```powershell
cd web/jose
npm ci
npm run dev
```

Abrir `http://127.0.0.1:5173`. Vite envia `/gateway/**` al puerto 5080; el
navegador no usa puertos individuales de los servicios ni claves internas.
El bundle de produccion se obtiene con `npm run build`; su publicacion dentro
de la web comun queda pendiente de integracion con ADM1.

El gateway tiene `GET /health`, que acredita solo que el proceso esta vivo;
no certifica disponibilidad de los 22 propietarios. El script
`tools/ejecutar-gateway.ps1 -Verificar` comprueba el lanzador sin iniciar procesos.
`tools/detener-servicios.ps1` incluye el puerto 5080 y conserva su comprobacion
de pertenencia del proceso al repositorio antes de detenerlo.

## Rutas externas y politica actual

La fuente ejecutable completa es `CatalogoRutas.Todas`. Los sufijos y query se
conservan; por ejemplo, `/gateway/admin/usuario/a@cuc.cr` llega a `/usuario/a@cuc.cr`.

| Prefijo / operaciones | Destino | Acceso actual |
|---|---|---|
| `/gateway/auth/login`, `/refresh` | Autenticacion /login y /refresh | POST publico; las otras operaciones no son excepciones publicas. |
| `/gateway/auth/validate`, `/sesion`, `/logout` | Autenticacion | Sesion valida; validate/sesion GET, logout POST. |
| `/gateway/admin/usuario`, `/user`, `/rol`, `/parametro`, `/modulo` | Propietario correspondiente; user es alias de usuario | Administrador. |
| `/gateway/admin/institucion`, `/carrera`, `/curso`, `/profesor`, `/periodo`, `/grupo`, `/expediente` | Propietario correspondiente | Administrador para estos CRUD globales. |
| `/gateway/admin/bitacora` | Bitacora | GET/POST administrador; no consulta global desde el estudiante. |
| `/gateway/mat/prematricula`, `/matricula`, `/historialacademico`, `/listadoestudiantes` | Propietario correspondiente | Administrador para los contratos globales V1. |
| `/gateway/mat/provincias`, `/cantones`, `/distritos` | Direccion | GET de los tres perfiles; conservar la forma de parametros/rutas del servicio actual. |
| `/gateway/mat/cargardesglose`, `/asignarnotarubro`, `/obtenerdesglose`, `/obtenernotas`, `/obtenernotasgrupo`, `/profesor/grupos` | Notas | Profesor; Notas comprueba pertenencia al grupo, matricula, curso y periodo. |
| `/gateway/fin/factura`, `/pago`, `/notificar` | Finanzas/correo | Administrador. |

Las rutas `interno` quedan fuera de la interfaz publica, incluso con codificacion
de caracteres. No hay reenvio arbitrario de un destino elegido por el navegador.
Una ruta sin politica no obtiene acceso. Autenticacion caida devuelve 503; un
destino de negocio caido devuelve 502; los errores funcionales del propietario
conservan su respuesta. No se registran cuerpos/tokens en el gateway.

Estas politicas iniciales no implementan la matriz dinamica ADM5/ADM7 de otros
responsables. Tampoco permiten al profesor usar CRUD global de grupos ni al
estudiante usar CRUD global de expedientes/prematriculas: falta integrar los
contratos acotados a propietario antes de habilitarlos. Esa dependencia no se
resuelve publicando todos los registros al navegador.

La referencia SRV7 del PDF sigue pendiente de aclaracion. Las unicas excepciones
publicas de negocio implementadas son POST login y refresh; no se adivina otra.

## Integracion con el trabajo de Alejandro y Fabian

- `Login` recibe `cliente` y `onAuthenticated(sesion)`. El layout anfitrion usa
  ese callback para navegar al dashboard correspondiente de ADM3 o WEB1.
- `Notas` recibe el mismo cliente autenticado; debe montarse para el profesor.
  El control efectivo de pertenencia permanece en servidor.
- El envoltorio local muestra notas al profesor y una confirmacion de sesion
  para los otros perfiles. No presenta esa confirmacion como dashboard terminado.
  Emite `avatar:sesion-iniciada` con el rol como punto adicional de integracion.
- La sesion guarda los cuatro campos del contrato en `sessionStorage`, sin
  contrasena. No incluye Recordarme persistente, opcion que ADM2 deja opcional.
  Un refresh en vuelo no puede restaurar una sesion cerrada.
- Logout limpia credenciales del cliente aunque falle la red. `/logout` registra
  el evento desde Autenticacion; no promete revocacion de JWT ya emitidos.
- El componente de notas usa grupos del periodo activo y permite vista de todo
  el grupo o filtro de estudiante. Sin desglose, sin notas y dependencia caida
  tienen estados diferentes. No contiene datos de ejemplo en su codigo de producto.

## Pruebas y pendientes de cierre

Resultados actuales y limites: [verificacion de este incremento](pruebas/jose-incremento-a2.md).
Se comprobaron 96 casos locales distintos; no equivalen a 96 criterios finales
del PDF ni a historias cerradas.

Pendiente: recorrido de las cuatro HU con propietarios/SQL reales, persistencia
de bitacoras, fixtures exclusivos, permisos/menu acordados con ADM5/ADM7,
integracion al layout y dashboards finales, y capturas por criterio con version
y revision de apoyo. Tambien queda verificar concurrencia SQL en reemplazo de
rubros/asignacion de notas y recuperacion de fallos de Bitacora: el cliente actual
de Notas registra esos fallos en log sin garantizar reintento persistente.

No se modificaron migraciones ni bases compartidas en este incremento.

Referencia tecnica de GTW1: [YARP: reenvio directo](https://learn.microsoft.com/en-us/aspnet/core/fundamentals/servers/yarp/direct-forwarding).
El host usa `IHttpForwarder` con routing y transformaciones explicitos.
