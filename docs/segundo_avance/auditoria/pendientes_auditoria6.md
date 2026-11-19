# Pendientes de la auditoría 6 - Segundo avance

> Estado al 13 de noviembre de 2026: las pruebas automatizadas y sus ejecutores fueron retirados. Las referencias a archivos de pruebas y comandos de ejecucion de este documento son historicas; los resultados y capturas se conservan como evidencia. El detalle de los retiros esta en `docs/mapeo-pruebas/limpieza-realizada.csv`.


> Nota de mantenimiento (13 de noviembre de 2026): las pruebas automatizadas de `NuevoAvatar.Estudiante.React`, su configuración de Playwright y los ejecutores Python asociados se retiraron del proyecto. Las referencias a esos archivos y comandos en este informe corresponden a ejecuciones históricas y ya no son instrucciones vigentes. Se conservan los resultados y evidencias.


Fecha: 12 de noviembre de 2026. 

Informe: [auditoría 6](auditoria_avance6.md). Evidencias: [registro de esta revisión](evidencias6/README.md).

Dejo por separado lo que falta desarrollar, los errores encontrados y las comprobaciones pendientes. Mantengo un responsable por historia y señalo las coordinaciones necesarias. No corregí estos puntos durante la auditoría.

Actualizo este mismo documento después del [recorrido real con las credenciales suministradas](evidencias6/recorrido-real/README.md). Incorporo sus fallos, retiro condiciones que ya pude comprobar y conservo pendientes los problemas que no corregí. Los datos temporales se retiraron; las cuentas suministradas permanecieron sin cambios.

## Alejandro

### A1. Recuperar el resumen del inicio estudiantil — WEB1, H12

**Alta; regresión funcional confirmada.** El inicio consulta expediente y prematrículas y almacena disponibilidad, errores y cantidad. Sin embargo, el contenido visible solo conserva las tarjetas con enlaces. Por eso no comunica el expediente vinculado, las dieciséis solicitudes preparadas, el estado vacío, la falta de vínculo ni la caída de una dependencia.

Ubicación: `NuevoAvatar.Estudiante.React/src/pages/Estudiante/InicioEstudiante.jsx`, especialmente el contenido de las tarjetas después de «Mis gestiones».

Lo comprobé con cuatro escenarios independientes: respuesta normal, expediente 404, cero solicitudes y expediente 503. Los cuatro fallaron en [recorrido-estudiante](evidencias6/resultados-conservados.md#recorrido-estudiante). La causa está en el contenido retirado de la vista, no en que la consulta no se ejecute.

Dejo pendiente restituir los mensajes de carga, disponibilidad, vacío, error y falta de vínculo con el canal de gestión, además del conteo real. Como equipo mantengo que un enlace por sí solo no cumple el resumen de WEB1. Cierro este punto cuando los cuatro estados se distingan sin datos inventados y se conserve el acceso a las gestiones propias.

### A2. Impedir duplicados entre solicitudes — WEB4, H01

**Alta; faltante identificado por inspección.** `crearPropia` y `modificarPropia` validan la solicitud y pasan a guardar sin consultar si otra solicitud ya contiene ese curso, estudiante y período. El rechazo de cursos repetidos dentro del mismo cuerpo no cubre dos solicitudes diferentes.

Ubicación: `NuevoAvatar.Prematricula/src/main/java/cr/ac/cuc/nuevoavatar/persona4/prematricula/`, en `PrematriculaService`, `PrematriculaValidador` y `PrematriculaRepository`.

La lectura de índices encontró únicamente las claves de identificador de cabecera y detalle. En el seguimiento real envié dos solicitudes iguales de una cuenta temporal, con curso y período temporales: ambas devolvieron 201. Confirmé el fallo por HTTP y retiré las solicitudes. Evidencia: «Rechazo de duplicado entre solicitudes» en [servicios-y-datos](evidencias6/recorrido-real/resultados-conservados.md#observaciones).

Dejo pendiente desarrollar el control en creación y edición, excluyendo la solicitud que se modifica, y acordar cómo protegerlo ante envíos simultáneos. Cierro cuando el segundo intento se rechace sin cambios parciales y quede registrado. Si requiere cambio de datos, mantengo el proceso de aprobación del curso.

### A3. Consumir el tamaño de página acordado — WEB4, H02

**Alta; fallo reproducido.** `SOLICITUDES_POR_PAGINA = 10` fija el listado. Preparé TAMPAG=2 y dieciséis solicitudes; se mostraron diez. El componente no consulta el parámetro.

Ubicación: `NuevoAvatar.Estudiante.React/src/pages/Estudiante/Prematricula.jsx`. Evidencia: `A-WEB4-TAMPAG`.

Dejo pendiente consumir el tamaño configurable, respetar el máximo de quince y manejar valores inválidos o servicio no disponible según el contrato común. En SQL compartido no encontré TAMPAG entre los parámetros consultados: Fabian debe preparar el valor acordado y Alejandro integrar su consumo. Configurarlo por sí solo no cambia la constante actual.

### A4. Mantener una sola ruta activa — ADM1, H03

**Media; fallo reproducido.** En expediente aparecen activos Inicio y Mi expediente. `Sidebar.jsx` aplica coincidencia exacta solo a `/inicio`; la raíz estudiantil también coincide por prefijo con sus rutas hijas.

Ubicación: `NuevoAvatar.Estudiante.React/src/components/Sidebar/Sidebar.jsx`. Evidencia: `A-ADM1-ruta-activa`.

Dejo pendiente una selección única en inicio, expediente, prematrícula y perfil, preservando título y breadcrumbs.

### A5. Conservar nombre y rol en móvil — ADM1, H04

**Media; fallo reproducido.** A 390 px el nombre y el rol están en el documento, pero la regla `.user-info { display: none; }` los oculta. Las seis mediciones de los inicios no mostraron desbordamiento; eso no resuelve la identidad oculta ni certifica todos los formularios.

Ubicación: `NuevoAvatar.Estudiante.React/src/styles/portal.css`. Evidencia: `A-ADM1-identidad-movil` y las seis capturas de plantilla de esta auditoría.

Dejo pendiente una presentación compacta o un acceso claro a esa identidad desde la barra, además de comprobar tablas, formularios y diálogos en los tamaños acordados.

### A6. Conservar el pie común — ADM1, H14

**Media; regresión confirmada.** `StudentLayout` envía `mostrarFooter=false` en expediente y perfil estudiantil. La comprobación de expediente encontró cero pies de página. El PDF incluye el pie en la plantilla y el acuerdo estudiantil mantiene la base común.

Ubicación: `NuevoAvatar.Estudiante.React/src/layouts/StudentLayout.jsx` y `src/layouts/MainLayout.jsx`. Evidencia: `A-ADM1-pie`. En perfil identifiqué la misma condición por inspección; no lo cuento como una segunda reproducción.

Dejo pendiente conservar el pie sin perjudicar el espacio útil de esas vistas y revisar la plantilla completa, no solo su inicio.

### A7. Actualizar las pruebas de contraseña — ADM24, H05

**Alta de verificación; tres fallos repetidos.** El doble de `DatosRemotos` no devuelve PASSMIN/PASSMAX/PASSMAY/PASSNUM, por lo que el servicio responde 503 antes de llegar a lo que varios casos pretenden comprobar. Además, una prueba exige minúscula y carácter especial, distintos de la política acordada.

Ubicación: `NuevoAvatar.Usuario/src/test/java/cr/ac/cuc/nuevoavatar/persona2/usuario/UsuarioPerfilPropioTest.java`. Evidencia: los cuatro casos de perfil en [resultados](evidencias6/resultados-conservados.md#resultados), uno aprobado y tres fallidos.

Confirmé que los cuatro parámetros sí están en la base compartida con los valores documentados. No atribuyo estos tres fallos a parámetros ausentes en producción. Dejo pendiente preparar los dobles con el contrato actual y comprobar cambio válido, contraseña actual incorrecta, límites y caída del proveedor, sin reducir las comprobaciones.

En el seguimiento real ya cambié la contraseña de una cuenta temporal desde la web, ingresé con la nueva, confirmé el rechazo de la anterior y comprobé BCrypt y bitácora. Retiro ese pendiente general de demostración del cambio. Los tres casos Java siguen sin actualizarse; WEB2 conserva sus criterios restantes y la inconsistencia de la cuenta suministrada indicada a continuación.

### A8. Delimitar el menú de esta entrega — H13

**Alta para presentación; coordinación con Fabian.** `AppRoutes.jsx` incorpora una pantalla administrativa de expedientes que no está en las veinte historias comprometidas. La lectura compartida encontró una asignación de su módulo; si el menú la devuelve activa, el cliente la ofrece.

Ubicación: `NuevoAvatar.Estudiante.React/src/routes/AppRoutes.jsx`. Evidencia: `permisos_fuera_del_compromiso` en [inspeccion](evidencias6/resultados-conservados.md#observaciones).

Dejo pendiente preparar el menú/permisos de la presentación para mantener exclusivamente el compromiso acordado. No propongo eliminar código durante la auditoría, no evalué esa pantalla y no le asigno puntos.

### A9. Preparar una identidad coherente para la cuenta estudiantil — WEB1/WEB2/WEB4, H15

**Alta para presentación; inconsistencia real de datos, coordinación con Fabian.** La cuenta suministrada ingresa y obtiene el perfil ESTUDIANTE. Encontré un expediente con su correo, pero la identificación y el tipo de documento difieren entre Usuario y Expediente. La vinculación comprueba los tres campos: expediente, solicitudes y opciones devolvieron 404.

Lo confirmé mediante consultas de lectura sin publicar identificaciones ni datos personales: [vínculo de la cuenta suministrada](evidencias6/recorrido-real/resultados-conservados.md#observaciones). La restricción protege la identidad; no corresponde quitarla para permitir el acceso.

Dejo pendiente acordar cuáles son los datos correctos y preparar la cuenta de presentación por el proceso del equipo. No corregí ni reemplacé sus valores. Con otra cuenta temporal coherente pude guardar contacto y crear, modificar y cancelar una prematrícula desde navegador; esos resultados no convierten la cuenta suministrada en una cuenta ya preparada. Fabian atiende la cuenta, Alejandro el expediente y Jose apoya la integración de identidad.

### A10. Ofrecer en el perfil solo cambios que puedan guardarse — ADM24, H16

**Media; incoherencia de interfaz reproducida.** En el perfil estudiantil, «Editar perfil» abre un correo editable. Al intentar guardar un correo válido, la propia pantalla informa que el backend no permite cambiarlo. El método `guardarCorreo` no envía una actualización; el servidor de perfil admite nombre, no ese correo.

Ubicación: `NuevoAvatar.Estudiante.React/src/pages/Perfil/Perfil.jsx`. Evidencia: «Edición ofrecida en perfil estudiantil permite guardar un cambio válido» en [navegador](evidencias6/recorrido-real/resultados-conservados.md#observaciones).

Dejo pendiente alinear los campos y acciones con el contrato acordado. El criterio no obliga a habilitar un cambio de correo nuevo: como equipo podemos mantenerlo de lectura y ofrecer únicamente las modificaciones permitidas. Evito que el usuario complete un formulario que necesariamente terminará rechazado. No cambié el correo de ninguna cuenta.

## Fabian

### F1. Actualizar los escenarios administrativos — ADM3/ADM4/ADM5/ADM6/ADM8, H06

**Alta de verificación; ocho fallos actuales.** La ejecución completa dio dos aprobados y ocho fallidos de diez:

| Historia | Resultado de interfaz | Causa observada                                                                                                                           |
| -------- | --------------------- | ----------------------------------------------------------------------------------------------------------------------------------------- |
| ADM3     | 2/4                   | Dos comprobaciones esperan el texto unido «Consulta · Usuarios» o elementos de lista; la vista ahora muestra una tabla con columnas.   |
| ADM4     | 0/1                   | El recorrido no contempla cerrar la bienvenida antes de accionar el mantenimiento.                                                        |
| ADM5     | 0/1                   | El recorrido llega con la bienvenida y no completa la interacción prevista.                                                              |
| ADM6     | 0/1                   | La bienvenida intercepta el clic en Siguiente.                                                                                            |
| ADM8     | 0/3                   | La bienvenida intercepta acciones; una comprobación también busca Eliminar como botón directo de la fila, previo al menú de acciones. |

Ubicación: `NuevoAvatar.Estudiante.React/tests/ui/`, archivos `dashboard`, `usuarios`, `roles`, `parametros` e `instituciones`. Evidencia: [ejecución confirmada](evidencias6/resultados-conservados.md#observaciones).

Dejo pendiente incorporar las interacciones actuales y mantener las expectativas de permisos, validación, persistencia y cancelación. Coordino el contrato visual con Alejandro. No recomiendo quitar la bienvenida solo para que pasen pruebas antiguas ni declaro rotas las operaciones de negocio a partir de estos fallos.

### F2. Indicador de facturas y política de contraseña

**Faltantes anteriores retirados.** La consulta limitada del indicador exige ADM3 y devuelve solo pendientes; sus siete nuevos casos pasan. Confirmé los cuatro parámetros de contraseña y el índice de unicidad de roles en SQL compartido.

Dejo únicamente la consolidación de evidencia por criterio y la revisión de la configuración de presentación. Mantengo separada la preparación de TAMPAG, que sigue pendiente, de la política de contraseña que sí está presente.

### F3. Completar aceptación documental y coordinar parámetros — ADM4/ADM5/ADM6/ADM8

**Alta de aceptación.** Hay recorridos reales anteriores de usuarios, permisos, parámetros e instituciones; no falta construirlos desde cero. Dejo pendiente completar filtros, orden, paginación, restricciones, dependencia caída y JSON exigidos en la matriz actual.

Como equipo mantengo una preparación identificada de parámetros y permisos, incluida la paginación y el menú del corte. Coordino con Ramses el procedimiento para SMTP: su evidencia preparó los valores sin pasarlos por una operación que registra el contenido completo del parámetro. No publiqué ni consulté valores de credenciales en las evidencias.

### F4. Evitar la exposición de la contraseña SMTP — ADM6, H17

**Alta de confidencialidad; comprobado en la aplicación activa.** La consulta de parámetros devuelve MAILPASS y la tabla representa su valor como texto común. Comprobé en memoria que la celda coincide con el valor devuelto; guardé únicamente los indicadores `parametro_presente` y `valor_visible_en_claro`, ambos verdaderos. No copié la contraseña ni dejé una captura que la muestre.

Ubicación: `NuevoAvatar.Estudiante.React/src/pages/Parametros/Parametros.jsx`, `NuevoAvatar.Parametro/Controllers/ParametroController.cs` y el contrato de parámetros con Notificación. Evidencia: [indicadores sin valores](evidencias6/recorrido-real/resultados-conservados.md#observaciones) y [comprobación de navegador](evidencias6/recorrido-real/resultados-conservados.md#observaciones).

Dejo pendiente acordar con Ramses un tratamiento específico para credenciales: limitar su devolución y representación, además de revisar los registros de altas/cambios que serializan el valor completo. No cambié MAILPASS ni provoqué una operación que lo copie a bitácora; no afirmo que haya una filtración comprobada en el historial. Cierro este punto cuando el valor deje de exponerse y la administración siga funcionando con el contrato acordado.

### F5. Coordinar la cuenta de presentación — H15

La credencial estudiantil es válida, pero sus datos de identificación no coinciden con el expediente del mismo correo. Dejo pendiente con Alejandro la revisión indicada en A9. El acceso no debe resolverse cambiando permisos ni eludiendo la comprobación de pertenencia. Las credenciales originales siguieron permitiendo ingresar al cierre.

## Jose

### J1. Responder claramente ante rubros repetidos — ADM17, H11
+
**Corregido y verificado a solicitud de Jose.** Agregué validación de nombres antes de bloquear o reemplazar el desglose. Rechazo duplicados con diferencias de espacios, mayúsculas y tildes equivalentes mediante 422 y un mensaje claro. Si la representación de SQL detecta otra equivalencia en su restricción de nombre/grupo, respondo 409 y mantengo el rollback. No oculto otros fallos de integridad bajo ese mensaje.
+
La comprobación real confirmó que el desglose previo queda intacto en ambos rechazos y que nombres distintos siguen guardándose. Pasaron 55/55 pruebas de Notas, 3/3 de contratos y 9/9 comprobaciones con servicios/SQL reales. Retiré los registros temporales y detuve los procesos propios.
+
Ubicación: `NuevoAvatar.Notas/src/main/java/cr/ac/cuc/nuevoavatar/persona5/notas/NotasService.java`. Evidencia y pruebas añadidas: [cierre de H11](evidencias6/cierre-jose-h11/README.md). Conservo la respuesta 500 anterior como antecedente del diagnóstico; ya no es un pendiente vigente de esta corrección.
+
+### J2. Completar condiciones reales de aceptación — ADM17 y GTW2

**Pendiente de evidencia, no un fallo repetido.** Las 253 pruebas actuales pasan; la de SQL con dos conexiones también se ejecutó y pasó. Mantengo el cierre del error transaccional y de los ejecutores de Jose.

**Orden HTTP inverso comprobado.** En el nuevo recorrido, el reemplazo ganó con 200 y la nota sobre el rubro anterior fue rechazada con 422, sin dejar datos parciales. Retiro ese pendiente. Mantengo el rechazo por módulo inactivo en un escenario aislado; no cambié módulos compartidos para comprobarlo. La ambigüedad de SRV7 sigue requiriendo aclaración en contratos; no vuelvo pública una operación por interpretar un identificador incierto.

Dejo pendiente vincular las evidencias actuales, sus precondiciones y la guía de gateway a la versión final de presentación.

## Ramses

### R1. Adaptar el recorrido de la plantilla a la bienvenida — H06

**Alta de verificación; nueve fallos actuales.** En la plantilla pasó uno de diez casos. Los recorridos de los seis mantenimientos y tres comprobaciones adicionales de módulos no completan su interacción con la bienvenida abierta. Los registros muestran que el diálogo intercepta clics; algunos casos agotan su tiempo antes de continuar.

Ubicación: `NuevoAvatar.Estudiante.React/tests/ui/ramses.spec.js`. Evidencia: [ejecución confirmada](evidencias6/resultados-conservados.md#observaciones).

El cliente aislado terminó con seis aprobados y sin errores generales. Retiro el pendiente anterior de cierre del ejecutor. Dejo pendiente adaptar la preparación del recorrido integrado, coordinando con Alejandro, y repetir las mismas validaciones sin debilitarlas.

En el recorrido real de esta actualización sí abrí y cancelé los seis formularios después de cerrar la bienvenida. Esto confirma el diagnóstico de preparación de las pruebas; no modifica el resultado anterior de esas suites ni sustituye todos sus criterios.

### R2. Unificar el destino del historial — ADM22, H08

**Alta para presentación; diferencia comprobada.** La evidencia del 12 de noviembre conserva historial local. `tools/ejecutar-presentacion-servicio.ps1` sigue enviando Notificación a una base local, mientras la lectura SQL de esta auditoría confirma que la tabla `general.Notificacion` ya existe en la base compartida.

No considero inválida la evidencia local ni afirmo que falte hoy la tabla remota. La diferencia significa que dos arranques pueden mostrar historiales distintos. Dejo pendiente acordar un destino, documentar conexión/migración y repetir un envío e historial sobre ese mismo entorno cuando corresponda. No moví registros ni cambié conexiones.

En esta actualización inicié Notificación con el destino compartido del manifiesto y comprobé HTTP 200 en historial y la apertura de su formulario. No fue necesario crear otra tabla ni aplicar una migración. Mantengo pendiente alinear el ejecutor de presentación que sigue apuntando local; no envié otro correo.

### R3. Completar aceptación de los mantenimientos — ADM7/ADM9/ADM10/ADM11/ADM12

**Pendiente de evidencia por criterio.** Reconozco el CRUD 26/26 y las respuestas 201/409 ante altas simultáneas de período documentadas por el equipo. No los sustituyo por un «sin pruebas reales» ni los interpreto como cobertura de todas las restricciones.

Dejo pendiente organizar casos de institución/director, curso con grupos, profesor con grupos activos, edad/dominio/teléfonos, estados y solapamientos, filtros/orden/paginación y JSON por operación. El escenario HTTP simultáneo acredita exclusión en el recorrido realizado; no prueba por sí solo el despliegue con dos instancias.

### R4. Correo: conservar el cierre de envío y completar fallos — ADM22

**Envío real acreditado por el equipo; aceptación restante pendiente.** Reconozco HTTP 200, estado ENVIADA, historial y captura de recepción del 12 de noviembre. La sección posterior de esa evidencia supera su párrafo previo de configuración ausente. Confirmé presencia de los seis parámetros SMTP sin leer sus valores en el informe.

Dejo pendiente completar fallos de transporte y de guardado de historial sobre el entorno acordado. Tener una captura de recepción no acredita esos rechazos ni la uniformidad de los destinos. No envié otro correo durante esta revisión.

## Pendientes compartidos de entrega

### E1. Consolidar documentación y reparar sus referencias — H09

**Alta.** No localicé un paquete actualizado único que reúna portada, introducción, datos completos, casos de uso, clases, pruebas por criterio, conclusiones, recomendaciones y bibliografía de este corte. Tener documentos anteriores y contratos separados no permite darlo por completo.

Encontré seis destinos ausentes: `docs/desarrollo-java.md`, `docs/limpieza-proyecto.md`, `docs/segundo_avance/contexto-desarrollo.md`, `docs/segundo_avance/desarrollo-jose.md`, `docs/segundo_avance/pruebas/criterio-evidencia.md` y `docs/segundo_avance/pruebas/jose-concurrencia-permisos.md`. Los índices, el roadmap y los contratos conservan referencias a esos archivos. La inspección de enlaces queda en [inspeccion](evidencias6/resultados-conservados.md#observaciones).

Como equipo mantengo que cada integrante completa sus historias y evidencia; luego reunimos el paquete y revisamos enlaces. No restauré archivos eliminados ni alteré informes anteriores.

### E2. Preparar la misma versión y entorno para demostrar el corte

**Arranque real comprobado; guía común pendiente.** Después del corte inicial inicié los servicios y recorrí el sistema. Una primera baja de período devolvió 503 porque faltaba el servicio heredado que comprueba una de sus referencias; al iniciarlo, la baja devolvió 204. Quedaron 21 procesos propios activos y todos se detuvieron al terminar. La guía debe incluir esas dependencias de apoyo sin asignarles historias adicionales.

Dejo pendiente una comprobación final de los seis recorridos del roadmap con servicios reales, sin mezclar capturas de distintas bases. Jose verifica sesión/gateway/notas; Ramses oferta/correo; Fabian administración/dashboard/parámetros; Alejandro plantilla/perfil/portal estudiantil. No hace falta rehacer lo acreditado si su versión y precondiciones siguen siendo válidas, pero sí cubrir lo que la evidencia no demuestra.

El nuevo recorrido ya respalda ingreso, administración, contacto/trámites de una cuenta temporal coherente, Notas y cambio de contraseña. No presento el envío de correo como repetido ni el acceso de la cuenta suministrada como resuelto. Retiro la ausencia general de recorrido real y conservo las condiciones específicas documentadas.

### E3. Vincular aprobación de cambios, tablero y entrega final

**Pendiente de verificación externa.** Los scripts y merges locales existen. No comprobé la aprobación del curso a cada cambio de datos, el estado remoto del tablero ni el PR hacia la versión final. Dejo esos comprobantes a cargo del equipo; no afirmo que no existan.

## Condición para considerar lista la presentación

Como criterio de cierre mantengo resolver los faltantes funcionales, repetir las comprobaciones afectadas, delimitar el menú y utilizar el mismo entorno para la evidencia final. Después corresponde reunir el paquete documental y revisar la aceptación por criterio de las veinte historias. **Con los pendientes actuales no doy por terminado el segundo avance al 100%.**
