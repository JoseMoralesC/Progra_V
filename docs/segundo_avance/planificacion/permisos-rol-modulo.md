# Contrato de permisos rol-módulo — GTW2 / ADM1 / ADM5 / ADM7

Definido e implementado el 4 de noviembre de 2026 por Jose, a solicitud del equipo.
Consumidores: Fabian (roles/matriz, ADM5), Ramses (módulos, ADM7) y la plantilla
compartida de Alejandro. Se conserva el responsable único y los puntos de cada HU.

## Decisión

Una petición de negocio requiere **sesión válida, perfil permitido por la ruta
y módulo activo asignado al ID real del rol de la cuenta**. La validación de
propiedad sigue en cada dominio: ADM17 exige profesor/grupo/matrícula autorizados.

No hay excepción permanente de permisos para administrador. Retirar un permiso
o desactivar su módulo bloquea la siguiente petición al gateway. No se almacenan
asignaciones dentro del JWT ni se mantienen cachés de permisos entre peticiones.
Un fallo al consultar permisos responde 503 sin ejecutar la operación de negocio.

El permiso es binario por módulo y cubre los verbos ya publicados por esa ruta.
La matriz actual no distingue lectura, alta, edición y baja. No se introducen
columnas nuevas ni se infieren permisos a partir del nombre visible del módulo.

## Fuente de datos e identidad

- Módulo es dueño de `seguridad.modulo` y `seguridad.rol_modulo`, en `PrograV_Modulo`.
- `id_modulo` identifica la capacidad; `activo` deshabilita su uso global y `orden`
  determina la presentación. `nombre_modulo` es solo la etiqueta visible.
- `id_rol` de la asignación es el ID persistido en Rol/Usuario. Por ejemplo,
  `ROL_DOCENTE_02` puede representar el perfil canónico `PROFESOR`.
- `ADMINISTRADOR`, `PROFESOR` y `ESTUDIANTE` son perfiles de seguridad derivados
  por Autenticación; no se supone que sean los IDs de la tabla de roles.
- El gateway valida la sesión y envía su Bearer a `GET /modulo/menu`. Módulo
  obtiene el email validado, consulta Usuario y resuelve el ID real de rol.
  No se acepta un ID de rol elegido por navegador, query o header de identidad.

La migración 002 ya define las tablas/columnas necesarias. Su aplicación real
continúa sujeta al proceso de datos del proyecto; este incremento no la ejecutó.

## Lectura del menú y permisos propios

```http
GET /gateway/menu
Authorization: Bearer <access_token>
```

Ejemplo de respuesta 200:

```json
[
  { "idModulo": "ADM17", "nombreModulo": "Notas", "activo": true, "orden": 17 }
]
```

Es una lista de módulos activos asignados a la cuenta autenticada, ordenada por
orden y etiqueta. `[]` es una respuesta válida: no hay permisos otorgados.
El endpoint es accesible para los tres perfiles autenticados aunque no tengan
permiso de administrar módulos. No devuelve las asignaciones de otra cuenta.
`?idRol=...` no cambia la identidad consultada. La respuesta usa `Cache-Control: no-store`.

El cuerpo público no contiene claves internas, tokens ni credenciales SQL.
El endpoint del servicio, `/modulo/menu`, también registra la consulta propia
en Bitácora. Un fallo de esa dependencia puede impedir confirmar la consulta.

La ruta anterior **`GET /gateway/menu/{idRol}` queda solo para administrador con
ADM7**, como consulta administrativa de un rol. Los clientes por perfil deben
migrar a la variante sin ID; no construir el menú enviando un rol del navegador.

## IDs estables y alcance de API

Los IDs se reservan con la misma referencia de historia para facilitar trazabilidad.
No es necesario cambiar el nombre visible existente; sí debe coincidir el ID.

| ID | Recurso publicado por el gateway | Perfil máximo actual |
| --- | --- | --- |
| ADM4 | `/gateway/admin/usuario`, alias `/gateway/admin/user` | Administrador |
| ADM5 | `/gateway/admin/rol` | Administrador |
| ADM6 | `/gateway/admin/parametro` | Administrador |
| ADM7 | `/gateway/admin/modulo`, `/gateway/menu/{idRol}` | Administrador |
| ADM8 | `/gateway/admin/institucion` | Administrador |
| ADM9 | `/gateway/admin/carrera` | Administrador |
| ADM10 | `/gateway/admin/curso` | Administrador |
| ADM11 | `/gateway/admin/profesor` | Administrador |
| ADM12 | `/gateway/admin/periodo` | Administrador |
| ADM13 | `/gateway/admin/grupo` | Administrador |
| ADM14 | `/gateway/mat/prematricula` | Administrador |
| ADM15 | `/gateway/mat/matricula` | Administrador |
| ADM16 | `/gateway/admin/expediente` | Administrador |
| ADM17 | `/gateway/mat/cargardesglose`, `asignarnotarubro`, `obtenerdesglose`, `obtenernotas`, `obtenernotasgrupo`, `profesor/grupos` | Profesor |
| ADM18 | `/gateway/mat/historialacademico` | Administrador |
| ADM19 | `/gateway/mat/listadoestudiantes` | Administrador |
| ADM20 | `/gateway/fin/factura` | Administrador |
| ADM21 | `/gateway/fin/pago` | Administrador |
| ADM22 | `/gateway/fin/notificar`, incluido historial | Administrador |
| ADM23 | `/gateway/admin/bitacora` | Administrador |

La asignación no amplía el perfil máximo: otorgar ADM17 a estudiante no permite
cargar notas y otorgar ADM16 a estudiante no abre el expediente global. Los
endpoints propios WEB siguen pendientes y necesitarán políticas específicas.
Los recursos A3 publicados también reciben protección, sin declarar sus interfaces
entregadas ni cambiar la frontera de alcance del backlog.

Excepciones explícitas: login/refresh públicos; validate/sesión/logout autenticados;
menú propio autenticado; catálogos territoriales autenticados para los tres perfiles.
`/health` sigue siendo diagnóstico público. No se asigna otro módulo para poder
iniciar/cerrar sesión o recuperar el propio menú. Un servicio nuevo sin mapeo
de módulo impide construir su política en el catálogo; no queda exento por defecto.

ADM3 y ADM24 se reservan para dashboard/perfil. Sus pantallas/API finales no se
implementan en este incremento. El Inicio actual es el contenedor de sesión;
no equivale al dashboard ADM3 ni al inicio académico WEB1.

## Uso por Fabian — matriz de roles

1. Consultar roles con `GET /gateway/admin/rol` y módulos con
   `GET /gateway/admin/modulo` usando una cuenta con ADM5/ADM7.
2. Construir la matriz usando los IDs reales de rol y los IDs estables de módulo.
3. Leer asignaciones mediante `GET /gateway/admin/modulo/{idModulo}/roles`.
4. Guardar la lista completa de roles autorizados para ese módulo:

```http
PUT /gateway/admin/modulo/ADM17/roles
Content-Type: application/json
Authorization: Bearer <access_token>

["ROL_DOCENTE_02"]
```

`[]` retira todas las asignaciones del módulo. El endpoint existente normaliza
IDs, valida que los roles existan, reemplaza la lista y registra anterior/actual.
Cada PUT es una operación por módulo; no hay un guardado transaccional de toda
la matriz a través de múltiples peticiones. Su UI debe reflejar los errores de
cada operación y no anunciar un guardado global si alguna falló.

No implementar otra fuente de permisos en localStorage ni modificar el JWT para
representar los toggles. Tras guardar, volver a leer la matriz y el menú propio.
El resumen de grupos usado por notas exige ADM17; si ADM3 lo consume directamente,
la cuenta necesita también ese permiso. Las métricas que consuman otros CRUD
requieren sus permisos correspondientes o futuros endpoints agregados acotados.

## Uso por Ramses — catálogo y consumidores

Conservar los IDs anteriores al crear/editar módulos. Cambiar nombre/orden no
cambia la política de API. Desactivar el módulo retira su acceso para todos sus
roles sin borrar la relación; reactivarlo conserva sus asignaciones.

Los selectores y consultas auxiliares no conceden permisos implícitos. Por ejemplo,
la pantalla de carreras consume instituciones/profesores, la asignación de módulos
consume roles y `TAMPAG` se obtiene de Parámetro. Las cuentas que ejecuten esas
pantallas necesitan los permisos de las consultas que consumen. Un 403 debe
mostrarse/controlarse; no resolverlo concediendo permisos desde el navegador.
La preparación A2 propuesta incluye los mantenimientos administrativos completos.

No hay rutas de frontend persistidas en la tabla actual. Cada cliente mantiene
un registro local de pantallas implementadas por ID. En la plantilla actual
ADM17 corresponde a `/notas`; los demás módulos no crean enlaces a páginas vacías.
Ramses puede seguir usando su cliente compartido y migrar su navegación al mismo
contrato al integrar sus pantallas. Este incremento no modifica su aplicación aislada.

## Consumo ya implementado en React

`SesionProvider` carga permisos al ingresar, restaurar sesión, navegar a otra ruta
y renovar. Mientras verifica una navegación no monta el contenido de destino.
En Inicio, «Actualizar permisos» fuerza una nueva consulta. Una revocación en una
página ya abierta puede permanecer visualmente hasta actualizar/navegar/renovar,
pero la siguiente llamada de negocio al gateway vuelve a comprobarla.

El menú usa la etiqueta devuelta por el backend para ADM17 y no muestra su opción
si falta la asignación activa. `/notas` exige simultáneamente profesor y ADM17.
La pantalla no se monta cuando falta cualquiera de los dos. Si falla la consulta
de permisos no se conserva un menú antiguo como si estuviera autorizado.

## Preparación y puesta en uso

Antes de usar el nuevo gateway con datos reales, comprobar la migración 002 y
preparar las asignaciones iniciales. Se entrega
[script manual de preparación](../../../db/por_servicio/configuracion/permisos_a2_inicial.sql):

1. Consultar los IDs reales en Rol/Usuario por los medios administrativos existentes.
2. Revisar el script y completar esos IDs; activar explícitamente
   `@ConfirmarInicializacion = 1` solo para la primera preparación autorizada.
3. El script crea módulos ausentes sin sobrescribir etiquetas/estados existentes.
   Otorga al administrador los mantenimientos A2, al profesor ADM3/ADM17/ADM24
   y al estudiante ADM24 si se indicaron sus IDs. Los módulos A3 se catalogan sin otorgarlos.
4. Conservar ADM5/ADM7 en una cuenta administrativa para poder mantener la matriz.
5. Después gestionar permisos mediante las API. No reutilizar la preparación
   para un despliegue ordinario: repondría asignaciones iniciales que se hayan retirado.

El script no se ejecutó. Tampoco se agregan asignaciones automáticamente al iniciar
los servicios. La ausencia de módulo/asignación produce denegación, no un permiso
de compatibilidad. Destino adicional del gateway: `Destinos__Modulo` (por defecto
`http://localhost:5162`). Módulo depende de Usuario y Bitácora además de su BD.

## Verificación y límites

Las pruebas HTTP de gateway usan Kestrel/YARP reales y propietarios simulados.
Comprueban retiro de permiso en la siguiente petición, módulo inactivo, ausencia
de excepción para administrador, techo de perfil, caída/respuesta inválida de
permisos y menú propio sin suplantación del rol. Pruebas del controlador confirman
que usa un ID de rol diferente del nombre canónico y registra la consulta propia.
El navegador verifica ocultación del menú, URL directa sin ADM17 y recuperación
ante caída de permisos. Los resultados se resumen en
[ficha del incremento](../pruebas/jose-concurrencia-permisos.md).

Falta comprobar asignaciones y revocaciones con los servicios/BD reales, incluyendo
la UI administrativa de Fabian/Ramses. Este contrato y su implementación no cierran
por sí solos ADM5/ADM7 ni cambian responsables o puntos del backlog.
