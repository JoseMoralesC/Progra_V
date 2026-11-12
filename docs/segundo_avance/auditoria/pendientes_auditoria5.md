# Pendientes de la auditoría 5 - Segundo avance

Fecha: 12 de noviembre de 2026. Versión revisada: `ba576558f5d61c34cf1d0561b2a325b375605496`.
Informe: [auditoría 5](auditoria_avance5.md). Ejecuciones: [evidencias5](evidencias5/README.md).

Dejo aquí lo que falta desarrollar, lo que requiere revisión y lo que todavía debe demostrarse. Como equipo mantengo un responsable por historia y distingo un error confirmado de una comprobación pendiente. Actualizo los puntos de Jose tras sus correcciones y pruebas reales del 12 de noviembre; los demás mantienen su último estado documentado. Detalle: [seguimiento de Jose](../pruebas/jose-cierre-auditoria5/README.md).

## Jose

### J1. Pruebas de sesión y Notas — ADM2 y ADM17

**Resuelto.** Las doce pruebas anteriores ya pasan. Actualicé la preparación de auditoría de interfaz y acoté la expiración a las consultas de Notas. La repetición da 27/27 aprobadas en la plantilla, con una renovación, limpieza de credenciales y ninguna escritura en el caso rechazado. Conservé las comprobaciones funcionales.

Evidencia: [suite de sesión/Notas](../pruebas/jose-cierre-auditoria5/ui-sesion-notas-final.json).

### J2. Recorrido real de sesión y autorización — ADM2, GTW1 y GTW2

**Recorrido realizado; condiciones de aceptación específicas pendientes.** Comprobé los tres perfiles, identidad, menú, restauración desde navegador, refresh, salida, JWT vencido, rechazo de perfiles indebidos y de un grupo ajeno. Retirar el permiso del rol temporal bloqueó la siguiente operación. Gateways temporales con destinos inaccesibles devolvieron 502 para negocio y 503 para autenticación.

Mantengo pendiente la prueba de módulo inactivo sobre un escenario aislado y la consolidación de evidencia por criterio y guía de presentación. No cambié el estado de un módulo compartido para ensayar ese caso. Evidencia: [servicios reales](../pruebas/jose-cierre-auditoria5/servicios-reales.json), [fallos del gateway](../pruebas/jose-cierre-auditoria5/gateway-fallos-reales.json) y [navegador real](../pruebas/jose-cierre-auditoria5/navegador-real.json).

### J3. Bloqueo, concurrencia y recuperación de Notas — ADM17

**Defecto corregido y escenarios reales comprobados.** El recorrido descubrió que el bloqueo devolvía `-999` porque el modo manual JDBC aún no había iniciado una transacción SQL. Corregí ese inicio mediante una lectura vacía, sin anidar transacciones. Las 46 pruebas de Notas pasan, incluida una con dos conexiones SQL que comprueba retención y liberación del bloqueo.

También comprobé primera nota frente a reemplazo, dos altas simultáneas, espera agotada sin cambios parciales, rollback ante un error SQL provocado, persistencia de la nota cambiada desde la web y bitácoras reales. El resultado simultáneo observado fue nota 200 y reemplazo 409. Mantengo pendiente acreditar también el orden inverso por HTTP; su cobertura local no se presenta como otra ejecución real.

Evidencia: [resumen de suites](../pruebas/jose-cierre-auditoria5/pruebas-locales.json) y [servicios reales](../pruebas/jose-cierre-auditoria5/servicios-reales.json).

### J4. Aclaración de SRV7 — GTW2

**Pendiente documental.** El PDF menciona una excepción SRV7 cuya equivalencia sigue sin resolverse en los contratos consultados. Mantengo la aclaración con el profesor; no convierto una operación de negocio en pública por interpretar un identificador ambiguo.

### J5. Cierre del ejecutor de navegador

**Resuelto para los ejecutores de Jose.** Vite se inicia y cierra desde la preparación de Playwright. Las suites de plantilla de Jose y su cliente aislado terminan sin errores generales: 27/27 y 6/6 aprobadas. Esto no acredita una nueva ejecución del cliente aislado de Ramses.

### J6. Respuesta ante rubros repetidos — ADM17, H11

**Prioridad media; pendiente.** Enviar dos rubros con el mismo nombre incumple la restricción de la base y devuelve 500 porque la excepción se trata como error técnico general. El rollback conservó el desglose previo, pero falta ofrecer una validación o conflicto con mensaje claro. No confundo la comprobación satisfactoria del rollback con una validación funcional resuelta.

Al terminar retiré el profesor y todos los datos temporales de evaluación. Los conteos iniciales de asignaciones, usuarios, notas y grupos se conservaron; las bitácoras quedan como evidencia de las operaciones.

## Ramses

### R1. Unificar el entorno de módulos y permisos — ADM7

**Prioridad alta para presentación.** La evidencia del 11 de noviembre explica que se utilizó una base local de Módulo porque la compartida no tenía la relación rol–módulo. Otros registros posteriores muestran flujos de permisos operativos, pero no acreditan que todos usaran esa misma base.

Dejo pendiente identificar servidor, base, migración y asignaciones vigentes del escenario que se presentará, coordinándolo con Fabian y Jose. El seguimiento de Jose sí comprobó permisos sobre las bases compartidas. Mantengo pendiente vincular esa configuración con la presentación de los mantenimientos de Ramses; no afirmo que la migración siga faltando hoy. La diferencia entre escenarios impide reutilizar capturas como si fueran de un único entorno.

Ubicación: `docs/segundo_avance/pruebas/ramses/evidencias-reales/2026-11-11/README.md` y `db/por_servicio/migraciones/002_modulo_v2.sql`.

### R2. Completar la evidencia funcional de los mantenimientos — ADM7, ADM9, ADM10, ADM11 y ADM12

**Prioridad alta de aceptación.** Las pantallas y sus controles están disponibles; las pruebas locales de estas áreas pasan. Falta la evidencia completa por criterio con servicios reales: tamaño de página parametrizado, filtros/orden, institución y director, restricciones de borrado, dominio y teléfonos del profesor y estados de período.

Dejo pendiente contrastar altas, cambios, bajas, consultas y errores con la bitácora persistida, incluyendo antes/después cuando corresponde. Mostrar un modal o comprobar una llamada simulada no prueba esa persistencia.

### R3. Verificar períodos entre conexiones reales — ADM12

**Prioridad alta de aceptación.** El repositorio ya usa transacción serializable y bloqueos al comprobar solapamientos del mismo año/número. La suite disponible prueba reglas y coordinación dentro de una instancia.

Falta ejecutar el caso simultáneo entre conexiones SQL reales y conservar el resultado de conflicto o espera, fechas límite y rollback. No propongo una prohibición general de períodos con números distintos: conservo la regla del PDF y su interpretación documentada.

### R4. Demostrar envío e historial de correo — ADM22

**Prioridad alta de aceptación.** El servicio valida destinatarios, limpia HTML y distingue el envío realizado del error al guardar su historial. Las cinco pruebas de servidor pasaron; utilizan transporte controlado.

La evidencia real disponible muestra un historial vacío y el formulario, no una entrega SMTP. Falta comprobar un envío autorizado, su recepción, la fila de historial y el consumo desde el dashboard; también configuración ausente, rechazo de transporte y fallo de historial después del envío. No envié correos durante esta auditoría.

### R5. Cierre del ejecutor de navegador

**Prioridad de verificación.** Los seis casos aislados de interfaz pasaron, pero el ejecutor agotó el límite al cerrar su servidor auxiliar. Falta separar ese problema de ejecución de los resultados por caso y dejar un comando que finalice limpiamente. No lo registro como defecto demostrado de los mantenimientos.

## Alejandro

### A1. Evitar prematrículas duplicadas entre solicitudes — WEB4, H01

**Prioridad alta de desarrollo.** Encuentro validación de identidad, carrera del curso, primer nivel y período futuro. Sin embargo, `crearPropia` y `modificarPropia` pasan al repositorio después de validar la solicitud actual, sin consultar si ya existe otra solicitud del mismo estudiante que contenga ese curso en ese período.

La creación guarda una cabecera nueva y sus cursos. Rechazar cursos repetidos dentro del mismo cuerpo o una llave repetida dentro de esa cabecera no evita repetirlos en otra cabecera. Tampoco localicé en los scripts revisados una restricción equivalente entre solicitudes.

Ubicación: `NuevoAvatar.Prematricula/src/main/java/cr/ac/cuc/nuevoavatar/persona4/prematricula/PrematriculaService.java`, `PrematriculaRepository.java` y `PrematriculaValidador.java`.

Dejo pendiente desarrollar la comprobación en creación y edición, excluyendo el propio registro cuando corresponda, y decidir con el equipo cómo sostenerla ante dos envíos simultáneos. Si requiere cambio de datos, se conserva el proceso acordado del curso. Cierro este punto cuando el segundo intento sea rechazado sin cambios parciales y el resultado quede auditado. La ausencia descrita procede de inspección; no intenté duplicar datos de la base compartida.

### A2. Usar la paginación configurable en prematrícula — WEB4, H02

**Prioridad alta de desarrollo.** Ya existe paginación, pero `SOLICITUDES_POR_PAGINA = 10` fija su tamaño. Preparé dieciséis solicitudes y TAMPAG=2; se mostraron diez. El componente no consulta ese parámetro.

Ubicación: `NuevoAvatar.Estudiante.React/src/pages/Estudiante/Prematricula.jsx`. Evidencia: caso `A-WEB4-TAMPAG` de [recorrido-estudiante.json](evidencias5/recorrido-estudiante.json).

Dejo pendiente consumir el tamaño acordado y mantener el máximo de quince, contemplando valor inválido o servicio no disponible según el contrato común. La página debe conservar una posición válida después de crear, cambiar o cancelar solicitudes.

### A3. Mantener una sola opción activa del menú — ADM1, H03

**Prioridad media de desarrollo.** Al entrar a Mi expediente encuentro activos Inicio y Mi expediente. `Sidebar.jsx` activa la coincidencia exacta únicamente para `/inicio`, mientras la raíz estudiantil `/estudiante` coincide también por prefijo.

Ubicación: `NuevoAvatar.Estudiante.React/src/components/Sidebar/Sidebar.jsx`. Evidencia: `A-ADM1-ruta-activa`. Dejo pendiente aplicar el comportamiento exacto a la opción de inicio estudiantil y comprobar expediente, prematrícula y perfil sin alterar los breadcrumbs.

### A4. Conservar el acceso al nombre y rol en móvil — ADM1, H04

**Prioridad media de desarrollo.** Las seis mediciones de inicio no muestran desbordamiento en 390, 768 y 1366 px. Sin embargo, la regla para hasta 600 px oculta `.user-info`; en la barra queda el avatar sin nombre ni rol visibles. Lo reproduje en expediente estudiantil a 390 px. La misma regla pertenece a la plantilla compartida.

Ubicación: `NuevoAvatar.Estudiante.React/src/styles/portal.css`. Evidencia: `A-ADM1-identidad-movil` y capturas `plantilla-*-390.png`.

Dejo pendiente una presentación compacta o un acceso claro a esa identidad desde la barra móvil, conservando el criterio del PDF. También queda revisar los formularios, tablas y diálogos en los tres tamaños: medir los inicios no certifica todas las pantallas.

### A5. Actualizar las pruebas del perfil y acreditar los parámetros — ADM24, H05

**Prioridad alta de verificación.** La política parametrizable ya está desarrollada en React y Usuario. Tres casos de `UsuarioPerfilPropioTest` fallan porque el doble de `DatosRemotos` no devuelve PASSMIN y los demás valores; el servicio rechaza con 503 antes de comprobar lo que la prueba espera. Además, el caso de complejidad conserva requisitos anteriores de minúscula y carácter especial, distintos del acuerdo vigente.

Ubicación: `NuevoAvatar.Usuario/src/test/java/cr/ac/cuc/nuevoavatar/persona2/usuario/UsuarioPerfilPropioTest.java`. Evidencia: `java-red.log` y [resultados por caso](evidencias5/resultados.json).

Dejo pendiente actualizar esa preparación, agregar el contraste de parámetros ausentes/inválidos y confirmar con Fabian los valores del entorno. No propongo relajar el servicio para satisfacer una prueba anterior. El rechazo cuando falta configuración es coherente con la política documentada.

Ya comprobé salida entre pestañas, confirmación del expediente y cancelación confirmada con respuestas controladas. No mantengo esos puntos como desarrollo ausente. Falta demostrar con servicios reales las operaciones propias, dos estudiantes, campos protegidos, correspondencia territorial y bitácoras de WEB1/WEB2/WEB4.

## Fabian

### F1. Hacer reproducible el indicador de facturas — ADM3 y ADM5, H07

**Prioridad alta para presentación.** El indicador ya está desarrollado y consulta todos los períodos. Sin embargo, `Dashboard.jsx` solo lo solicita si existen ambos permisos requeridos, mientras `permisos_a2_inicial.sql` no entrega al administrador el permiso de consulta de facturas que esa implementación necesita.

La evidencia real de Fabian menciona la asignación adicional. Además, `tests/reales/adm3_indicadores.mjs` restaura esa asignación únicamente si la prueba no termina aprobada, por lo que un recorrido exitoso puede dejar preparado un entorno que el script inicial por sí solo no reproduce.

Dejo pendiente acordar con Jose la consulta autorizada que alimentará el indicador y documentar su preparación permanente. No propongo habilitar pantallas adicionales. Cierro el punto cuando una instalación preparada con la guía acordada muestre el indicador sin depender de un permiso dejado por un ejecutor de pruebas.

### F2. Actualizar las pruebas de las pantallas administrativas — ADM3, ADM4, ADM6 y ADM8, H06

**Prioridad alta de verificación.** En el portal principal fallan seis de sus diez casos: uno de dashboard, tres de Instituciones, uno de Parámetros y uno de Usuarios. En los seis aparece la ruta de auditoría de interfaz como no prevista por la preparación de la prueba.

Ubicación: `NuevoAvatar.Estudiante.React/tests/ui/`. Evidencia: [principal-ui-final.json](evidencias5/principal-ui-final.json).

Dejo pendiente incorporar el contrato de la llamada y repetir los casos, revisando después cualquier fallo de guardado, búsqueda, permisos o sincronización que permanezca. No vuelvo a afirmar que los controles por página estén ausentes: ya existen. Tampoco utilizo estas ejecuciones para afirmar que una edición falló en SQL, porque el navegador usa respuestas controladas.

### F3. Confirmar la preparación de datos y política — ADM4, ADM5, ADM6 y ADM8

**Prioridad alta de aceptación.** Reconozco el registro del equipo del 11 de noviembre sobre CRUD, restricciones, permisos y migraciones. Falta vincular esa evidencia con la configuración exacta que se utilizará al presentar y la versión de esta entrega.

Dejo pendiente confirmar con Alejandro los valores PASSMIN/PASSMAX/PASSMAY/PASSNUM, comprobar el indicador y permisos desde una cuenta nueva, y cerrar evidencia de filtros, orden, paginación y bitácoras. No localicé una suite propia de servidor en Institución; el éxito de su compilación no demuestra sus reglas. La falta de esa suite no crea un requisito nuevo de tecnología de pruebas: lo obligatorio sigue siendo demostrar los criterios.

### F4. Revisar los ejecutores de evidencia real

**Prioridad media de verificación.** La revisión estática señaló bloques `finally` que lanzan errores al restaurar datos o permisos. Esos errores pueden ocultar el fallo original de un recorrido. También hay una variable sin uso y advertencias de React en el cliente.

Dejo pendiente asegurar que el resultado principal y el estado de limpieza sean visibles por separado, y revisar el comportamiento de preparación permanente descrito en F1. Las advertencias no son, por sí mismas, prueba de que el producto falle; no las utilicé para rebajar el aporte de desarrollo automáticamente.

## Pendientes del equipo

| Orden | Trabajo pendiente | Responsable de continuidad | Condición de salida |
| ---: | --- | --- | --- |
| 1 | Cerrar duplicados, paginación, ruta activa e identidad móvil | Alejandro por historia | Rechazos y navegación correctos, con evidencia. |
| 2 | Alinear las suites pendientes de política y administración | Alejandro y Fabian según historia | Casos preparados con el contrato actual; las doce pruebas de Jose ya están resueltas. |
| 3 | Comprobar el cierre del ejecutor aislado de Ramses | Ramses | Finaliza sin agotar la espera; los dos ejecutores de Jose ya cierran correctamente. |
| 4 | Consolidar bases, migraciones, parámetros, permisos y arranque | Los cuatro, por servicio e historia | Un entorno identificado que reproduce la presentación siguiendo la guía. |
| 5 | Completar los cuatro recorridos restantes y las condiciones específicas de Jose | Cada responsable de historia | Mantener los dos recorridos reales acreditados; completar evidencia de los restantes. |
| 6 | Completar evidencia por criterio | Los cuatro | Fuente, precondición, esperado, observado, captura, versión y revisión. |
| 7 | Preparar paquete documental y comprobantes de entrega | Los cuatro | Análisis/diseño, diagramas, pruebas, conclusiones, recomendaciones, bibliografía, scripts, tablero y PR verificables. |

Los tres informes iniciales de Playwright conservan su tiempo agotado de cierre como antecedente. Después corregí los dos ejecutores utilizados por Jose y comprobé que terminan sin errores generales. El ejecutor aislado de Ramses mantiene su estado pendiente de repetición.

No localicé en la documentación del segundo avance un paquete que reúna todos los apartados de pp. 21-22 del PDF. Encontré el documento maestro previo y el diagrama general, pero no acredito que estén actualizados para este corte. Los índices siguen enlazando cuatro archivos ausentes: `docs/desarrollo-java.md`, `docs/limpieza-proyecto.md`, `docs/segundo_avance/contexto-desarrollo.md` y `docs/segundo_avance/desarrollo-jose.md`. Como equipo debemos preparar una guía e índice dedicados a esta entrega, conservando los antecedentes históricos.

Dejo la presentación pendiente de estos cierres. Puedo reconocer lo disponible para una demostración parcial, pero no sostener que se cumple el 100% mientras existan los faltantes descritos.
