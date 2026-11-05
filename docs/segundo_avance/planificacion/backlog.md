# Backlog unico de los avances 2 y 3

Reajuste del 28 de octubre de 2026, limitado a los avances 2 y 3.
Esta es la fuente unica del reparto: **una historia completa, un responsable y
un avance**. El profesor no permite historias divididas ni una historia entre
dos personas. Los puntos pueden ser desiguales.

Se mantienen las **41 historias y 240 puntos**: 33 HU del PDF y 8 HU WEB del
equipo. ADM3, ADM17, MOB1 y MOB5 recuperan su ID original y sus 8 puntos cada
una. Los criterios completos se conservan en la
[guia de historias indivisibles](subhistorias.md).

**Autoria de UI/UX: Alejandro creo la plantilla de UI/UX.** ADM1 queda a su
cargo, incluida su integracion responsive, navegacion por rol y componentes
comunes. Este reconocimiento documenta el trabajo comunicado por el equipo;
el cierre de ADM1 requiere verificar todos sus criterios.

Los puntos incluyen interfaz o gateway, adaptaciones de API, permisos, reglas,
bitacora, integracion, pruebas y documentacion. Los resumenes de la tabla no
sustituyen los criterios del PDF ni los de [WEB](../requerimientos/web-estudiantes.md).

## Asignacion de historias completas

| HU | Origen | Historia y limite del alcance | Responsable | Avance | Puntos |
|---|---|---|---|---:|---:|
| ADM1 | PDF pp. 4-5 | Plantilla web responsive y navegacion por rol para las dos webs; componentes comunes. Alejandro creo la plantilla de UI/UX; sin duplicar el trabajo en WEB. | Alejandro | 2 | 8 |
| ADM2 | PDF p. 5 | Login, refresh y salida compartidos por ambas webs; redireccion al inicio de cada perfil. | Jose | 2 | 8 |
| ADM3 | PDF pp. 5-6 | Dashboard administrativo y del profesor: metricas, notificaciones, bitacoras, grupos/cursos propios y accesos autorizados a notas. | Fabian | 2 | 8 |
| ADM4 | PDF pp. 6-7 | Usuarios, filtros, roles/dominios, contrasena opcional al editar y auditoria. | Fabian | 2 | 8 |
| ADM5 | PDF p. 7 | Roles y permisos persistidos; autorizacion efectiva, sin depender solo del menu. | Fabian | 2 | 8 |
| ADM6 | PDF pp. 7-8 | Parametros, validaciones, paginacion y auditoria de cambios. | Fabian | 2 | 3 |
| ADM7 | PDF p. 8 | Modulos, estado, orden y asociacion con roles; contrato de menu acordado con ADM5. | Ramses | 2 | 5 |
| ADM8 | PDF p. 8 | Instituciones, busqueda y bloqueo de eliminacion con carreras. | Fabian | 2 | 3 |
| ADM9 | PDF p. 8 | Carreras, institucion/director y restricciones por cursos. | Ramses | 2 | 5 |
| ADM10 | PDF p. 8 | Cursos, filtros por carrera/nivel y restricciones por grupos. | Ramses | 2 | 5 |
| ADM11 | PDF p. 9 | Profesores, edad/dominio/telefonos, filtros y restricciones por grupos activos. | Ramses | 2 | 5 |
| ADM12 | PDF p. 9 | Periodos, fechas, estados derivados y controles de solapamiento requeridos. | Ramses | 2 | 5 |
| ADM13 | PDF p. 9 | Mantenimiento de grupos, relaciones, unicidad y horario; acceso de profesor segun politica acordada. | Jose | 3 | 5 |
| ADM14 | PDF p. 10 | Gestion administrativa de prematriculas con filtros y CRUD. WEB4 es la operacion del propio estudiante. | Fabian | 3 | 5 |
| ADM15 | PDF p. 10 | Matricula administrativa guiada, consultas y CSV. | Alejandro | 3 | 8 |
| ADM16 | PDF pp. 10-11 | Expedientes administrativos completos. WEB2/MOB3 solo permiten los cambios propios autorizados. | Alejandro | 3 | 8 |
| ADM17 | PDF p. 11 | Rubros y notas completos: carga/consulta, suma 100, reemplazo sin notas, bloqueo con notas, asignacion/modificacion, permisos y auditoria. | Jose | 2 | 8 |
| ADM18 | PDF p. 11 | Historial para los perfiles del PDF, filtros, orden, paginacion y CSV/PDF. | Ramses | 3 | 5 |
| ADM19 | PDF pp. 11-12 | Listados por periodo/carrera/curso/grupo y CSV/PDF. | Ramses | 3 | 5 |
| ADM20 | PDF p. 12 | Facturas administrativas, calculo, detalle, filtros y anulacion con motivo. | Fabian | 3 | 5 |
| ADM21 | PDF p. 12 | Pagos administrativos, validaciones, consulta y reverso con motivo. | Fabian | 3 | 5 |
| ADM22 | PDF p. 12 | Envio de correo, HTML sanitizado, historial y configuracion desde parametros; alimenta ADM3. | Ramses | 2 | 5 |
| ADM23 | PDF pp. 12-13 | Consulta administrativa de bitacoras, filtros y JSON de antes/despues. El registro se implementa en cada HU desde el avance 2. | Jose | 3 | 5 |
| ADM24 | PDF p. 13 | Perfil y cambio de contrasena compartidos por las webs, politica parametrizada y salida de sesiones en cliente. | Alejandro | 2 | 5 |
| GTW1 | PDF pp. 15-16 | Routing completo de las APIs V1 publicadas por el gateway; HTTP y contratos de error. | Jose | 2 | 8 |
| GTW2 | PDF p. 16 | Validacion mediante /validate y politica de rol/identidad; excepciones explicitas. Coordina un unico gateway con GTW1. | Jose | 2 | 8 |
| WEB1 | Equipo | Inicio y accesos del estudiante al expediente y prematricula; sin metricas inventadas. | Alejandro | 2 | 5 |
| WEB2 | Equipo | Consulta del expediente propio y actualizacion exclusiva de telefonos/direccion. | Alejandro | 2 | 5 |
| WEB3 | Equipo | Cursos y grupos del periodo del estudiante, detalle y navegacion a notas/historial. | Ramses | 3 | 5 |
| WEB4 | Equipo | Prematricula propia en cursos de primer nivel y periodos futuros; consulta, cambios y cancelacion conforme al servicio. | Alejandro | 2 | 5 |
| WEB5 | Equipo | Matricula propia en curso/grupo de periodo activo, confirmacion y consulta. Sin agregar anulacion estudiantil no acordada. | Alejandro | 3 | 8 |
| WEB6 | Equipo | Consulta propia de rubros, notas y acumulado; sin permisos de edicion de notas. | Ramses | 3 | 5 |
| WEB7 | Equipo | Historial propio, filtros y presentacion de promedios; reutiliza calculo del servicio. | Ramses | 3 | 5 |
| WEB8 | Equipo | Consulta propia de facturas, detalles y pagos; sin cobro en linea, creaciones ni reversos del estudiante. | Fabian | 3 | 5 |
| MOB0 | PDF p. 13 | Plantilla movil, navegacion, mensajes y confirmaciones; destinos acordados sin enlaces vacios. | Jose | 3 | 5 |
| MOB1 | PDF pp. 13-14 | Login movil completo: validaciones, autenticacion, almacenamiento seguro, recordar/restaurar sesion, salida y bitacora. | Jose | 3 | 8 |
| MOB2 | PDF p. 14 | Resumen movil propio y accesos a prematricula/matricula/cursos/pagos. | Jose | 3 | 5 |
| MOB3 | PDF p. 14 | Expediente movil propio, telefonos/direccion editables y confirmacion. | Jose | 3 | 5 |
| MOB4 | PDF pp. 14-15 | Prematricula movil, futuro/duplicados y opcion de notificacion. | Fabian | 3 | 5 |
| MOB5 | PDF p. 15 | Matricula movil completa: seleccion de curso/grupo, resumen, confirmacion, registro propio, validaciones, resultado y bitacora. | Alejandro | 3 | 8 |
| MOB6 | PDF p. 15 | Mis cursos movil, busqueda, actualizacion y detalle; destinos de notas/historial definidos. | Ramses | 3 | 5 |

## Balance por persona y entrega

| Persona | HU A2 | Puntos A2 | HU nuevas A3 | Puntos A3 | HU totales | Puntos totales |
|---|---:|---:|---:|---:|---:|---:|
| Jose | 4 | 32 | 6 | 33 | 10 | 65 |
| Ramses | 6 | 30 | 6 | 30 | 12 | 60 |
| Alejandro | 5 | 28 | 4 | 32 | 9 | 60 |
| Fabian | 5 | 30 | 5 | 25 | 10 | 55 |
| Total | 20 | 120 | 21 | 120 | 41 | 240 |

La carga desigual conserva las estimaciones y la continuidad por dominio.
GTW1 pasa completa de Alejandro a Jose, quien ya tiene GTW2. Asi el avance 2
queda en 32/30/28/30 puntos para Jose/Ramses/Alejandro/Fabian, respectivamente.
Alejandro conserva ADM1 y la autoria de la plantilla de UI/UX, y recibe
ADM24/WEB1/WEB2/WEB4 completas (20 puntos). Fabian recibe ADM4/ADM5/ADM6/ADM8
completas (22 puntos) y conserva ADM3. Jose reune sesion, gateway y notas. El avance 3 conserva sus
asignaciones. No se divide ninguna HU ni se modifican estimaciones.

## Reglas para no contar dos veces

- ADM1/ADM2/ADM24 se entregan una vez para ambas webs. WEB paga la experiencia
  estudiantil del dominio, no otro login, layout o backend V1.
- ADM5 (Fabian) gobierna permisos; ADM7 (Ramses) gobierna catalogo/orden/estado
  del menu. Acuerdan un mismo contrato.
- GTW1 y GTW2 (ambas de Jose) se integran en un unico gateway. El cambio de
  responsable de routing no implica crear otra aplicacion ni otro contrato.
- Cada historia se entrega completa en el avance asignado, con evidencia y un
  unico responsable. El apoyo o la revision no crean coautoria evaluable.
- Las dependencias se documentan sin duplicar componentes o servicios compartidos.
- WEB y MOB comparten contratos/reglas cuando corresponde, pero requieren
  interfaz y pruebas de plataforma propias.
- Las correcciones y APIs de apoyo se vinculan a la historia que primero las necesita.
  No crean puntos adicionales ni trabajo sin un responsable visible.
- A3 demuestra tambien las 20 historias de A2 sin sumar otros 120 puntos.
