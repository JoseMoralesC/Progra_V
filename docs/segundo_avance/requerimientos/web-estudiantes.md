# Historias propuestas para la web de estudiantes

Origen: aclaracion del profesor comunicada por Jose el 23 de octubre de 2026;
el PDF omite esta web y permite que el equipo redacte sus historias a partir de
los servicios construidos. Jose confirmo que este portal es solo para estudiantes.
Los IDs WEB1-WEB8 son del equipo: no se presentan como historias literales del PDF
ni como aprobadas individualmente por el profesor.

La [asignacion y los puntos](../planificacion/backlog.md) se mantienen en el
backlog unico. Esta especificacion propone criterios revisables con el equipo.
Cada criterio numerado requiere evidencia propia o una evidencia identificada
que demuestre expresamente varios criterios.

## Condiciones comunes obligatorias

- Solo el estudiante autenticado puede acceder a sus datos. La identidad se
  deriva de la sesion y se vincula a Usuario/Expediente; un ID del navegador no
  acredita pertenencia. Cambiar IDs, query o body debe ser rechazado por servidor.
- El portal usa el gateway y operaciones publicas autorizadas. No recibe
  `SERVICES_INTERNAL_KEY`, credenciales SQL ni acceso a `/interno/**`.
- Login, renovacion, salida, layout y perfil de cuenta se reutilizan de
  ADM1/ADM2/ADM24, incluyendo politica de contrasena; no son otras tres HU WEB.
- Se prueban carga, vacio, validacion, dependencia caida y sesion vencida.
  Las acciones de negocio se auditan desde el servicio con el detalle requerido.
- Listados de mantenimiento respetan la paginacion parametrizable del PDF
  (maximo 15 registros). No se inventan datos cuando una consulta falla.
- La web estudiantil no habilita mantenimiento global, notas editables,
  creacion de facturas, registro de pagos ni reversos administrativos.

## WEB1 - Inicio del estudiante (5 puntos; avance 2)

Como estudiante quiero identificar mi sesion y acceder a mi expediente y mis
prematriculas para iniciar mis tramites sin navegar por opciones administrativas.

Base: Usuario, Expediente y Prematricula; analogia de navegacion con ADM3 y MOB2,
sin duplicar el dashboard administrativo ni prometer todas las metricas de MOB2.

1. Al ingresar muestra nombre y rol de la cuenta autenticada, con opcion de salir.
2. Presenta el estado de disponibilidad del expediente propio y el numero real
   de prematriculas propias, o un estado vacio explicito.
3. Los accesos a expediente y prematricula llevan a WEB2/WEB4 y funcionan con
   datos propios. No aparecen accesos habilitados a pantallas aun no entregadas.
4. Si no existe vinculo con un expediente, informa el problema y el canal de
   gestion administrativa; no permite escoger arbitrariamente otro estudiante.
5. Un usuario ajeno al perfil estudiante no obtiene estos datos mediante URL o API.
6. Se comprueban sesion vencida, dependencia caida y registro de la consulta.

## WEB2 - Mi expediente (5 puntos; avance 2)

Como estudiante quiero consultar mi expediente y actualizar mis telefonos y
direccion para mantener mis datos de contacto correctos.

Base: MAT3/MAT4, ADM16 y restricciones de edicion de MOB3.

1. Consulta exclusivamente el expediente vinculado a la cuenta autenticada.
2. Identificacion, tipo, nombre, email y fecha de nacimiento son de lectura;
   solo telefonos, provincia/canton/distrito y otras senas son editables.
3. Los selectores territoriales limpian los descendientes cuando cambia su
   padre y validan la correspondencia antes de guardar.
4. Guardar requiere confirmacion, muestra el resultado persistido y permite
   regresar sin cambios. Se aplican las validaciones del servicio.
5. Manipular el body para cambiar campos protegidos u otro expediente se
   rechaza en servidor, sin cambios parciales.
6. Consulta y actualizacion se auditan; esta ultima conserva antes/despues.

## WEB3 - Mis cursos (5 puntos; avance 3)

Como estudiante quiero consultar mis cursos y grupos del periodo seleccionado
para conocer mi oferta matriculada y sus datos de clase.

Base: MAT2, ACD3-ACD6 y MOB6; no es el mantenimiento de cursos ADM10.

1. Lista solo matriculas propias activas del periodo, con curso, grupo,
   profesor y horario obtenidos de los propietarios de los datos.
2. Permite seleccionar periodo y buscar en el listado; distingue ausencia
   de matriculas de un fallo de consulta.
3. El detalle corresponde a una matricula propia y conserva los identificadores
   de curso/grupo/periodo que requieren las consultas de notas.
4. Los accesos a notas e historial conducen a WEB6/WEB7, sin enlaces vacios.
5. No permite listar datos de otro estudiante alterando filtros o identificadores.
6. Las consultas se registran en bitacora; errores no muestran datos de ejemplo.

## WEB4 - Mi prematricula (5 puntos; avance 2)

Como estudiante quiero prematricular cursos de primer nivel de periodos futuros
y consultar mis solicitudes para organizar mi ingreso.

Base: MAT1, ADM14 y MOB4. La notificacion opcional de MOB4 no se agrega como
obligacion adicional de esta historia web.

1. Selecciona carrera, periodo futuro y cursos pertenecientes a esa carrera;
   el formulario usa la identidad autenticada y permite observaciones opcionales.
2. Solo acepta cursos de primer nivel y periodos cuya fecha inicial sea futura.
3. Impide duplicados de estudiante/curso/periodo en servidor, incluso al editar.
4. Permite consultar, modificar y cancelar solicitudes propias conforme a las
   reglas de MAT1; las operaciones destructivas requieren confirmacion.
5. No puede consultar, modificar ni eliminar una solicitud ajena, ni sustituir
   la identificacion del estudiante en el request.
6. Demuestra alta, consulta, cambio, cancelacion, rechazos funcionales y bitacora
   con JSON nuevo/anterior/actual/eliminado segun la operacion.

## WEB5 - Mi matricula (8 puntos; avance 3)

Como estudiante quiero matricularme en un grupo de un curso de periodo activo
y consultar el resultado para confirmar mi inscripcion.

Base: MAT2, ADM15 y MOB5. No se agrega una politica nueva de anulacion estudiantil.

1. El flujo selecciona curso y grupo, muestra profesor/horario/periodo y solicita
   confirmacion antes de crear la matricula propia.
2. Curso, grupo y periodo deben corresponder y el periodo debe estar activo;
   se aplica la misma regla temporal que en el servicio, documentada en el contrato.
3. Impide duplicados y aplica la politica de cupo que se acuerde con el servicio;
   no garantiza disponibilidad si el backend no puede acreditarla.
4. Tras confirmar, muestra el estado persistido y el acceso a la consulta propia.
5. Prueba cambio malicioso de identificacion/grupo, sesion expirada y repeticion
   de la solicitud; no deja una matricula ajena ni un exito ficticio.
6. Registra la accion y sus errores tecnicos; no supone conversion automatica
   de prematricula ni creacion automatica de factura si no existe tal contrato.

## WEB6 - Mis notas (5 puntos; avance 3)

Como estudiante quiero consultar el desglose y mis notas para conocer el avance
de mi evaluacion sin modificar las calificaciones.

Base: MAT5, consulta de ADM17 y destinos de MOB6.

1. Selecciona una matricula propia y consulta los rubros de su curso/grupo.
2. Muestra porcentaje y nota por rubro, diferenciando sin desglose y sin notas.
3. El acumulado usa la misma regla ponderada del servicio; distingue una nota
   final de un acumulado incompleto y no inventa una politica de aprobacion.
4. No muestra controles de escritura ni permite asignar notas por API al estudiante.
5. Cambiar identificacion, matricula, curso o grupo no revela notas de terceros.
6. Comprueba los valores con un escenario calculable y registra la consulta.

## WEB7 - Mi historial (5 puntos; avance 3)

Como estudiante quiero consultar los promedios de los cursos que he llevado
para revisar mi historial academico.

Base: ACA1, ADM18 y destino de historial de MOB6.

1. La identidad de consulta procede de la sesion, no de una busqueda libre
   de estudiantes.
2. Presenta codigo y nombre del curso, periodo y promedio cuando el contrato
   los proporcione; antes de desarrollar se acuerda el dato necesario para filtrar.
3. Permite filtrar por periodo/anio y ordenar sin mezclar intentos distintos.
4. La politica de matriculas anuladas, repeticiones y notas incompletas es
   explicita y coherente con ACA1; no se redefine solo en el navegador.
5. El servidor impide acceder a otro historial; vacio y error son estados distintos.
6. Se comprueba un promedio contra el servicio y se audita la consulta. CSV/PDF
   de ADM18 no se duplica como exigencia WEB7 sin acuerdo adicional del equipo.

## WEB8 - Mis facturas y pagos (5 puntos; avance 3)

Como estudiante quiero consultar mis facturas y los pagos registrados para
conocer mi situacion financiera.

Base: IPN1/IPN2, ADM20/ADM21 y resumen financiero de MOB2.

1. Lista exclusivamente facturas propias y permite filtrar por periodo/estado.
2. El detalle muestra conceptos, subtotal, impuesto, total y estado persistidos.
3. Muestra los pagos vinculados y sus estados sin confundir un reverso con un
   pago aplicado; contempla la recuperacion pendiente de Pago/Factura.
4. No permite crear/reversar pagos o facturas ni introduce una pasarela de pago.
5. Cambiar facturaId o pagoId no revela documentos financieros ajenos.
6. Se prueban una factura pendiente, una pagada y un pago reversado con datos
   verificables; consulta auditada y errores sin datos simulados.

## Validacion de estas historias

La asignacion permite planificar, pero los criterios y 43 puntos nuevos deben
revisarse con el equipo y documentarse en el tablero. La autorizacion general
para redactar historias no equivale a una nota o ponderacion concedida por el
profesor. Si cambia el alcance, se reestima y se redistribuye antes de cerrar
el compromiso; no se agregan puntos solo para conservar un total redondo.
