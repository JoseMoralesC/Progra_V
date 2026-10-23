# Backlog unico de los avances 2 y 3

Propuesta del 23 de octubre de 2026. Todas las unidades estan **pendientes**.
Esta es la fuente unica del reparto: **30 puntos por persona en cada avance**.
Se mantienen las 33 HU del PDF y las 8 HU WEB del equipo, con 240 puntos.

El usuario confirmo que el profesor permite subhistorias evaluables con criterios
y responsables propios. Se descomponen ADM3, ADM17, MOB1 y MOB5 de 8 puntos en
bloques funcionales de 5 + 3. Los IDs con sufijo identifican esas subhistorias;
no son nuevos IDs del PDF. Ver [criterios y trazabilidad](subhistorias.md).

El backlog tiene **45 unidades evaluables para 41 historias de origen**. Los
cuatro padres sirven para agrupar y verificar cobertura; no suman puntos ni
calificacion otra vez. Cada fila tiene un responsable evaluable unico. No se
cambian estimaciones ni se eliminan criterios para cuadrar el reparto.

Los puntos incluyen interfaz o gateway, adaptaciones de API, permisos, reglas,
bitacora, integracion, pruebas y documentacion. Los resumenes de la tabla no
sustituyen los criterios del PDF ni los de [WEB](../requerimientos/web-estudiantes.md).

## Asignacion de unidades evaluables

| Unidad | Origen | Historia y limite del alcance | Responsable | Avance | Puntos |
|---|---|---|---|---:|---:|
| ADM1 | PDF pp. 4-5 | Plantilla web responsive y navegacion por rol para las dos webs; componentes comunes, sin duplicar el trabajo en WEB. | Jose | 2 | 8 |
| ADM2 | PDF p. 5 | Login, refresh y salida compartidos por ambas webs; redireccion al inicio de cada perfil. | Jose | 2 | 8 |
| ADM3-A | PDF pp. 5-6 | Dashboard administrativo y componentes comunes: metricas, notificaciones, bitacoras y accesos reales. | Fabian | 2 | 5 |
| ADM3-P | PDF pp. 5-6 | Dashboard del profesor: grupos/cursos propios del periodo y acceso autorizado a notas, reutilizando componentes comunes. | Jose | 2 | 3 |
| ADM4 | PDF pp. 6-7 | Usuarios, filtros, roles/dominios, contrasena opcional al editar y auditoria. | Alejandro | 2 | 8 |
| ADM5 | PDF p. 7 | Roles y permisos persistidos; autorizacion efectiva, sin depender solo del menu. | Alejandro | 2 | 8 |
| ADM6 | PDF pp. 7-8 | Parametros, validaciones, paginacion y auditoria de cambios. | Alejandro | 2 | 3 |
| ADM7 | PDF p. 8 | Modulos, estado, orden y asociacion con roles; contrato de menu acordado con ADM5. | Ramses | 2 | 5 |
| ADM8 | PDF p. 8 | Instituciones, busqueda y bloqueo de eliminacion con carreras. | Alejandro | 2 | 3 |
| ADM9 | PDF p. 8 | Carreras, institucion/director y restricciones por cursos. | Ramses | 2 | 5 |
| ADM10 | PDF p. 8 | Cursos, filtros por carrera/nivel y restricciones por grupos. | Ramses | 2 | 5 |
| ADM11 | PDF p. 9 | Profesores, edad/dominio/telefonos, filtros y restricciones por grupos activos. | Ramses | 2 | 5 |
| ADM12 | PDF p. 9 | Periodos, fechas, estados derivados y controles de solapamiento requeridos. | Ramses | 2 | 5 |
| ADM13 | PDF p. 9 | Mantenimiento de grupos, relaciones, unicidad y horario; acceso de profesor segun politica acordada. | Jose | 3 | 5 |
| ADM14 | PDF p. 10 | Gestion administrativa de prematriculas con filtros y CRUD. WEB4 es la operacion del propio estudiante. | Fabian | 3 | 5 |
| ADM15 | PDF p. 10 | Matricula administrativa guiada, consultas y CSV. | Alejandro | 3 | 8 |
| ADM16 | PDF pp. 10-11 | Expedientes administrativos completos. WEB2/MOB3 solo permiten los cambios propios autorizados. | Alejandro | 3 | 8 |
| ADM17-R | PDF p. 11 | Carga y consulta de rubros, suma 100, reemplazo sin notas y bloqueo con notas. | Fabian | 2 | 5 |
| ADM17-N | PDF p. 11 | Asignacion/modificacion de notas por rubro y vistas por grupo/estudiante; validaciones y auditoria. | Jose | 2 | 3 |
| ADM18 | PDF p. 11 | Historial para los perfiles del PDF, filtros, orden, paginacion y CSV/PDF. | Ramses | 3 | 5 |
| ADM19 | PDF pp. 11-12 | Listados por periodo/carrera/curso/grupo y CSV/PDF. | Ramses | 3 | 5 |
| ADM20 | PDF p. 12 | Facturas administrativas, calculo, detalle, filtros y anulacion con motivo. | Fabian | 3 | 5 |
| ADM21 | PDF p. 12 | Pagos administrativos, validaciones, consulta y reverso con motivo. | Fabian | 3 | 5 |
| ADM22 | PDF p. 12 | Envio de correo, HTML sanitizado, historial y configuracion desde parametros; alimenta ADM3. | Ramses | 2 | 5 |
| ADM23 | PDF pp. 12-13 | Consulta administrativa de bitacoras, filtros y JSON de antes/despues. El registro se implementa en cada HU desde el avance 2. | Jose | 3 | 5 |
| ADM24 | PDF p. 13 | Perfil y cambio de contrasena compartidos por las webs, politica parametrizada y salida de sesiones en cliente. | Fabian | 2 | 5 |
| GTW1 | PDF pp. 15-16 | Routing completo de las APIs V1 publicadas por el gateway; HTTP y contratos de error. | Alejandro | 2 | 8 |
| GTW2 | PDF p. 16 | Validacion mediante /validate y politica de rol/identidad; excepciones explicitas. Coordina un unico gateway con GTW1. | Jose | 2 | 8 |
| WEB1 | Equipo | Inicio y accesos del estudiante al expediente y prematricula; sin metricas inventadas. | Fabian | 2 | 5 |
| WEB2 | Equipo | Consulta del expediente propio y actualizacion exclusiva de telefonos/direccion. | Fabian | 2 | 5 |
| WEB3 | Equipo | Cursos y grupos del periodo del estudiante, detalle y navegacion a notas/historial. | Ramses | 3 | 5 |
| WEB4 | Equipo | Prematricula propia en cursos de primer nivel y periodos futuros; consulta, cambios y cancelacion conforme al servicio. | Fabian | 2 | 5 |
| WEB5 | Equipo | Matricula propia en curso/grupo de periodo activo, confirmacion y consulta. Sin agregar anulacion estudiantil no acordada. | Alejandro | 3 | 8 |
| WEB6 | Equipo | Consulta propia de rubros, notas y acumulado; sin permisos de edicion de notas. | Ramses | 3 | 5 |
| WEB7 | Equipo | Historial propio, filtros y presentacion de promedios; reutiliza calculo del servicio. | Ramses | 3 | 5 |
| WEB8 | Equipo | Consulta propia de facturas, detalles y pagos; sin cobro en linea, creaciones ni reversos del estudiante. | Fabian | 3 | 5 |
| MOB0 | PDF p. 13 | Plantilla movil, navegacion, mensajes y confirmaciones; destinos acordados sin enlaces vacios. | Jose | 3 | 5 |
| MOB1-A | PDF pp. 13-14 | Formulario y autenticacion movil: dominio, contrasena, respuesta de login, errores y bitacora de intentos. | Jose | 3 | 5 |
| MOB1-S | PDF pp. 13-14 | Persistencia segura de credenciales, recordar sesion, restauracion y cierre de sesion con limpieza y bitacora. | Alejandro | 3 | 3 |
| MOB2 | PDF p. 14 | Resumen movil propio y accesos a prematricula/matricula/cursos/pagos. | Jose | 3 | 5 |
| MOB3 | PDF p. 14 | Expediente movil propio, telefonos/direccion editables y confirmacion. | Jose | 3 | 5 |
| MOB4 | PDF pp. 14-15 | Prematricula movil, futuro/duplicados y opcion de notificacion. | Fabian | 3 | 5 |
| MOB5-S | PDF p. 15 | Seleccion movil de curso/grupo disponible y preparacion validada del resumen de matricula propia. | Fabian | 3 | 5 |
| MOB5-C | PDF p. 15 | Confirmacion, envio y resultado de matricula movil: identidad, periodo activo, duplicados, navegacion y bitacora. | Alejandro | 3 | 3 |
| MOB6 | PDF p. 15 | Mis cursos movil, busqueda, actualizacion y detalle; destinos de notas/historial definidos. | Ramses | 3 | 5 |

## Balance por persona y entrega

| Persona | Unidades A2 | Puntos A2 | Unidades nuevas A3 | Puntos A3 | Unidades totales | Puntos totales |
|---|---:|---:|---:|---:|---:|---:|
| Jose | 5 | 30 | 6 | 30 | 11 | 60 |
| Ramses | 6 | 30 | 6 | 30 | 12 | 60 |
| Alejandro | 5 | 30 | 5 | 30 | 10 | 60 |
| Fabian | 6 | 30 | 6 | 30 | 12 | 60 |
| Total | 22 | 120 | 23 | 120 | 45 | 240 |

## Reglas para no contar dos veces

- ADM1/ADM2/ADM24 se entregan una vez para ambas webs. WEB paga la experiencia
  estudiantil del dominio, no otro login, layout o backend V1.
- ADM5 (Alejandro) gobierna permisos; ADM7 (Ramses) gobierna catalogo/orden/estado
  del menu. Acuerdan un mismo contrato.
- GTW1 (Alejandro) y GTW2 (Jose) se integran en un unico gateway. El cambio de
  responsable de routing no implica crear otra aplicacion ni otro contrato.
- Las dos subhistorias de cada padre se entregan en el mismo avance y se prueban
  juntas. No se deja media historia oficial para el siguiente entregable.
- Cada subhistoria acredita sus criterios y errores propios. Las dependencias
  se documentan, sin duplicar el desarrollo de componentes o servicios compartidos.
- WEB y MOB comparten contratos/reglas cuando corresponde, pero requieren
  interfaz y pruebas de plataforma propias.
- Las correcciones y APIs de apoyo se vinculan a la unidad que primero las necesita.
  No crean puntos adicionales ni trabajo sin un responsable visible.
- A3 demuestra tambien las 22 unidades de A2 sin sumar otros 120 puntos.
