# Verificacion del reparto contra los requerimientos V2

Fecha: 28 de octubre de 2026.  
Fuentes: [PDF V2](../requerimientos/Proyecto%20Nuevo%20Avatar%20V2%20%281%29.pdf), [backlog](../planificacion/backlog.md), [historias de estudiantes](../requerimientos/web-estudiantes.md) y [criterios indivisibles](../planificacion/subhistorias.md).  
Tipo de revision: correspondencia documental, no prueba de implementacion.

## Resultado de la verificacion

**Las 33 historias identificadas en el PDF estan incluidas una vez en el reparto, con sus IDs originales.** Compruebo los IDs del PDF mediante extraccion local de texto y los comparo con las filas del backlog: ADM1-ADM24, MOB0-MOB6 y GTW1-GTW2. No hay historias oficiales omitidas ni IDs oficiales duplicados.

El backlog tiene 41 filas porque agrega WEB1-WEB8 para la web estudiantil omitida en el PDF. Esos ocho IDs, sus criterios y sus 43 puntos son una propuesta del equipo. Los nombres de responsables, puntos y corte A2/A3 proceden de la planificacion del equipo; no son un reparto literal del PDF.

La aclaracion del usuario establece que la web estudiantil debe guardar correspondencia con las funciones administrativas, adaptadas al estudiante. El reparto actual representa esa correspondencia parcialmente, pero **no demuestra todavia equivalencia completa de criterios entre ambas experiencias**. Hay restricciones justificadas por el perfil y decisiones de alcance que no se desprenden automaticamente de esa restriccion.

## Historias oficiales por responsable

| Responsable | Historias del PDF asignadas | Historias WEB adicionales | Total |
|---|---|---|---:|
| Jose | ADM2, ADM13, ADM17, ADM23, GTW1, GTW2, MOB0, MOB1, MOB2, MOB3 | Ninguna | 10 |
| Ramses | ADM7, ADM9, ADM10, ADM11, ADM12, ADM18, ADM19, ADM22, MOB6 | WEB3, WEB6, WEB7 | 12 |
| Alejandro | ADM1, ADM15, ADM16, ADM24, MOB5 | WEB1, WEB2, WEB4, WEB5 | 9 |
| Fabian | ADM3, ADM4, ADM5, ADM6, ADM8, ADM14, ADM20, ADM21, MOB4 | WEB8 | 10 |
| Total | 33 | 8 | 41 |

Los IDs corresponden. Los enunciados del backlog son resumenes: por ejemplo, ADM4 no enumera en su fila todos los criterios de columnas, orden, detalle y ultima modificacion; ADM15 no detalla todo el listado y exportacion CSV; ADM1 no enumera cada elemento del layout ni cada evento de bitacora. Esos criterios siguen siendo exigibles por el PDF. La tabla de asignacion no debe utilizarse sola como especificacion de desarrollo o cierre.

## Correspondencia de la web estudiantil

La equivalencia se aplica a reglas de negocio, datos pertinentes, validaciones, estados, confirmaciones, mensajes y calidad de interfaz. El rol y la propiedad de los datos determinan las operaciones permitidas. No se concede al estudiante mantenimiento de usuarios, roles, catalogos, notas o finanzas por copiar una pantalla administrativa.

| Funcion | Base en el PDF | Cobertura actual del estudiante | Resultado |
|---|---|---|---|
| Plantilla y navegacion | ADM1, pp.4-5 | Compartida mediante ADM1 | Correspondencia razonable. Debe conservar layout, responsive, mensajes y auditoria, con menu autorizado del estudiante. |
| Login, refresh y salida | ADM2, p.5; MOB1, pp.13-14 | Compartida mediante ADM2 | No falta una WEB de login: se cuenta una vez. Debe funcionar para el perfil estudiante y conservar todos sus criterios web. |
| Inicio/resumen | ADM3, pp.5-6; MOB2, p.14 | WEB1 | Cobertura limitada: estado de expediente y numero de prematriculas; no incluye el resumen completo de matricula, cursos y finanzas del estudiante. |
| Perfil y contrasena | ADM24, p.13 | Compartida mediante ADM24 | No falta una WEB de perfil. Conservar campos protegidos, politica parametrizada, salida de sesiones del cliente y auditoria. |
| Expediente | ADM16, pp.10-11; MOB3, p.14 | WEB2 | Adaptacion por perfil coherente: consulta propia y edicion de telefonos/direccion. Mantener validaciones y selectores territoriales. No habilitar CRUD global. |
| Cursos y grupos propios | ADM10/ADM13, pp.8-9; MOB6, p.15 | WEB3 | Adaptacion a consulta, no mantenimiento de oferta. Detalle de profesor/horario/grupo y accesos a notas/historial cubiertos. Pull-to-refresh es propio de movil y no se exige literalmente en web. |
| Prematricula | ADM14, p.10; MOB4, pp.14-15 | WEB4 | Conserva operacion propia, periodo futuro y duplicados. Omite expresamente la opcion de notificacion por correo que si aparece en MOB4. |
| Matricula | ADM15, p.10; MOB5, p.15 | WEB5 | Conserva flujo propio y validaciones. Falta detallar en la HU web la navegacion al detalle, los datos visibles de la consulta y la disponibilidad de exportacion propia equivalente al CSV administrativo. |
| Notas | ADM17, p.11; enlaces MOB6, p.15 | WEB6 | Diferencia funcional justificada: el estudiante consulta sus rubros/notas; el profesor carga o modifica. El calculo debe ser comun. |
| Historial | ADM18, p.11; enlace MOB6, p.15 | WEB7 | Cubre consulta propia/filtros, pero excluye expresamente CSV/PDF y no explicita paginacion/orden con la misma precision. Esa reduccion no se justifica solo por ser estudiante. |
| Facturas y pagos | ADM20/ADM21, p.12; resumen/accesos MOB2, p.14 | WEB8 | Consulta propia coherente; creacion, cobro y reversos quedan administrativos. Revisar paridad de filtros y consulta de comprobante si aplica. |
| Catalogos, usuarios, permisos, correo administrativo y bitacora global | ADM4-ADM12, ADM19, ADM22/ADM23, pp.6-13 | Sin mantenimiento estudiantil equivalente | Diferencia funcional justificada. Puede consumir catalogos necesarios o recibir notificaciones sin obtener permisos globales. |

La informacion de MOB aporta una referencia explicita del mismo actor estudiante. No la convierto automaticamente en requisito web literal: permite detectar decisiones omitidas que deben resolverse al completar la especificacion web.

## Diferencias que deben quedar resueltas

1. **WEB1: alcance del resumen.** La limitacion a expediente/prematricula sirve como corte de A2, pero no define por si sola el inicio final equivalente para el estudiante. Si se mantiene una HU indivisible cerrada en A2, debe especificarse como se incorporaran despues las funciones estudiantiles A3, sin dejar esa ampliacion sin responsable ni declararla ya cumplida. Las tarjetas deben mostrar datos propios, nunca los conteos administrativos globales.
2. **WEB7: exportacion y presentacion.** ADM18 exige CSV/PDF, paginacion y orden. WEB7 omite expresamente la exportacion salvo acuerdo posterior. Con la premisa de equivalencia funcional indicada por el usuario, corresponde revisar esa exclusión y conservar exportacion de datos propios, salvo una distincion funcional documentada. No basta indicar que el estudiante tiene otro rol para quitarla.
3. **WEB4: correo opcional.** MOB4 incluye una opcion para notificar por correo; WEB4 decide no incorporarla. No contradice literalmente ADM14, que no la pide, pero deja una diferencia entre clientes del mismo estudiante. Debe registrarse si se busca tambien paridad funcional web/movil. No implica conceder acceso al envio administrativo libre ADM22.
4. **WEB5: consulta, detalle y CSV.** El flujo de alta esta alineado. ADM15 pide listado/CSV para el administrativo, y MOB5 acceso al detalle. La especificacion estudiantil debe precisar que conserva para sus datos, en lugar de dar por completo un listado administrativo de terceros o de omitir toda presentacion equivalente.
5. **WEB8: filtros y comprobante.** ADM20 filtra por periodo/estudiante/estado y ADM21 contempla listado por periodo y comprobante si aplica. El filtro por estudiante se sustituye por identidad propia. WEB8 debe aclarar si conserva filtro por periodo en pagos y consulta del comprobante disponible; estas capacidades no implican permiso de escritura financiera.
6. **Criterios completos por HU oficial.** El reparto conserva los IDs, pero solo cuatro historias tienen desarrollo complementario en `subhistorias.md`. Todas las historias oficiales deben disponer de ficha vinculada a cada criterio del PDF, incluidos los transversales. No puede inferirse cumplimiento o recorte desde una descripcion abreviada.

La equivalencia no obliga a crear 24 historias WEB en espejo. ADM1/ADM2/ADM24 son comunes; WEB8 agrupa dos consultas financieras; varios mantenimientos no pertenecen al estudiante. Agrupar es aceptable si mantiene toda la trazabilidad y un responsable, y no oculta criterios o esfuerzo.

## Efecto sobre el segundo avance y la auditoria

El PDF no enumera las 20 HU del corte A2: ese corte lo eligio el equipo. La auditoria existente evalua el compromiso documentado de 15 ADM, 2 GTW y 3 WEB. Ese resultado no certifica que las ocho historias estudiantiles propuestas representen toda la web omitida.

En A2 estan comprometidas WEB1, WEB2 y WEB4, ademas de las comunes ADM1/ADM2/ADM24. La equivalencia final con matricula, cursos, historial y finanzas debe contemplar sus entregas A3. Verificar esas dependencias no adelanta sus interfaces al segundo avance.

Esta revision **no modifica responsables, puntos, criterios vigentes ni fechas de entrega**. Documenta que el origen de los IDs oficiales es correcto y que hace falta cerrar la paridad de la especificacion estudiantil. Si se incorporan criterios o cambia el alcance de una HU, se deben actualizar primero sus criterios y el backlog, revisar la estimacion y recalcular los resumenes afectados; no conservar 41 HU/240 puntos solo para mantener los totales anteriores.

## Dictamen

La discrepancia no consiste en historias oficiales faltantes: las 33 estan representadas. Consiste en comparar una tabla resumida de 41 historias del equipo con un PDF de 33, y en haber definido algunas funciones WEB con un alcance menor que sus analogas sin dejar toda la justificacion funcional explicita.

El criterio de correccion es conservar el requisito original de ADM/MOB/GTW y documentar, para cada funcion estudiantil, que criterio se reutiliza, cual cambia por propiedad/permisos, cual es especifico de plataforma y cual queda pendiente de decision. WEB1 y WEB7 son los puntos mas claros donde la propuesta actual necesita revision antes de afirmar equivalencia completa.
