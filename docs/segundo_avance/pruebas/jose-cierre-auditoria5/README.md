# Seguimiento de Jose después de la auditoría 5

Fecha: 12 de noviembre de 2026. Alcance: **ADM2, ADM17, GTW1 y GTW2 del segundo avance**.
Antecedente: [auditoría 5](../../auditoria/auditoria_avance5.md) y [pendientes de Jose](../../auditoria/pendientes_auditoria5.md).

Como equipo mantengo la auditoría anterior como registro del estado que encontré. En este seguimiento documento las correcciones y las comprobaciones posteriores de mi parte, sin cambiar el reparto de historias ni convertir las pruebas en puntos de desarrollo.

## Lo que corregí

**Preparación de sesión y Notas.** Actualicé las respuestas de las pruebas para reconocer la llamada de auditoría de interfaz. Ahora compruebo su método, autorización y evento, y verifico que la navegación a Notas quede registrada. La prueba de expiración rechaza específicamente las consultas de Notas y comprueba una sola renovación, regreso al login, limpieza de credenciales y ausencia de escrituras. Las doce pruebas que fallaban en este grupo ya pasan.

**Cierre del ejecutor de navegador.** Reemplacé el arranque mediante `cmd → npm → Vite` por un servidor Vite administrado desde la preparación de Playwright. El mismo ejecutor lo cierra al terminar. Así las suites de sesión/Notas y del cliente aislado de Jose finalizan sin el tiempo agotado de apagado observado anteriormente. El puerto sigue siendo exclusivo; no reutilizo silenciosamente otro servidor.

**Bloqueo de Notas con SQL Server real.** Durante el recorrido encontré un defecto adicional: crear un desglose devolvía 409 aunque no hubiera otra operación modificando el grupo. Reproduje la causa con el controlador JDBC: tener `autoCommit=false` todavía no garantizaba una transacción iniciada en SQL Server, y `sp_getapplock` con dueño transaccional devolvía `-999`.

Corregí `BloqueoGrupoNotas` para materializar esa transacción mediante una lectura vacía antes de pedir el bloqueo. No leo los datos de evaluación antes de protegerlos. Spring conserva la confirmación o reversión de la misma conexión. La nueva prueba real verifica dos adquisiciones del mismo bloqueo, una sola transacción, rechazo desde otra conexión mientras se mantiene el bloqueo y liberación después del rollback.

## Pruebas ejecutadas

| Grupo | Resultado | Evidencia |
| --- | --- | --- |
| Sesión y Notas en la plantilla | **27/27 aprobadas**, sin omisiones ni errores generales del ejecutor | [Informe del portal](ui-sesion-notas-final.json) |
| Cliente aislado de Jose | **6/6 aprobadas**, cierre normal | [Informe del cliente](ui-jose-final.json) |
| Servicio Notas | **46/46 aprobadas**, incluida la nueva prueba con dos conexiones SQL reales | [Resumen de suites](pruebas-locales.json) |
| Contratos compartidos ejecutados junto con Notas | **3/3 aprobadas** | [Resumen de suites](pruebas-locales.json) |
| Servicios, SQL y confirmación del recorrido real | **41 comprobaciones aprobadas** | [Servicios reales](servicios-reales.json) |
| JWT vencido y dependencias inaccesibles | **4 comprobaciones aprobadas**, incluido el ingreso de preparación | [Fallos reales de gateway](gateway-fallos-reales.json) |
| Navegación real de los tres perfiles | **14 comprobaciones aprobadas**, sin respuestas interceptadas | [Navegador real](navegador-real.json) |
| Artefacto Notas | JAR reconstruido, iniciado y aceptado por el validador de artefactos | Servicio utilizado en el recorrido real |

El grupo de 41 comprobaciones incluye la confirmación global del navegador y la lectura SQL posterior de la nota cambiada desde la web. Por eso no sumo otra vez las catorce comprobaciones de navegador como si fueran un recorrido independiente adicional. Los resultados de esta tabla tampoco sustituyen los criterios pendientes de las otras personas.

### Barras del resultado de estas pruebas

| Comprobación | Barra | Resultado |
| --- | --- | ---: |
| Pruebas locales de servidor/contratos y navegador | `[####################]` | 82/82 aprobadas |
| Servicios reales y escenarios de gateway | `[####################]` | 45/45 comprobaciones aprobadas |
| Detalle del navegador real, incluido en el recorrido anterior | `[####################]` | 14/14 aprobadas |

Estas barras representan las ejecuciones seleccionadas. No equivalen a cobertura total, a una calificación ni a un 100% de aporte de desarrollo. Mantengo esas dimensiones separadas de las barras de la auditoría.

## Qué comprobé con el sistema funcionando

Usé las cuentas de administrador y estudiante proporcionadas en el archivo externo. Con autorización preparé un profesor temporal, su rol, permiso de Notas, cuatro grupos y sus matrículas de prueba. Leí el curso, período activo y estudiante existentes para preparar las relaciones; no cambié sus datos.

Comprobé el ingreso, identidad, menú, renovación y salida de los tres perfiles. En navegador comprobé recarga de sesión, bloqueo de Notas para administrador/estudiante y salida sin recuperar acceso desde el historial. El profesor ingresó desde su dashboard, abrió Notas, modificó una calificación de prueba y volvió al inicio. Después confirmé en SQL que la nota guardada desde la web era 91.

También comprobé:

- Rechazo de consultas sin token, con token inválido y con un JWT de firma válida pero fecha de expiración pasada.
- Rechazo de un grupo ajeno y de la siguiente operación después de retirar el permiso del rol temporal.
- Creación del desglose, primera nota, modificación y bloqueo del reemplazo cuando ya existen notas.
- Primera nota y reemplazo enviados en paralelo: observé 200 para la nota y 409 para el reemplazo, con el estado conservado.
- Dos altas simultáneas del mismo par matrícula/rubro: observé 200 y 409, con una sola nota persistida.
- Espera agotada del bloqueo: 409 sin cambios parciales.
- Reversión ante un error SQL provocado por rubros repetidos: el servicio devolvió 500 y el desglose anterior quedó intacto. Este caso demuestra rollback ante un error técnico; no lo presento como una validación funcional de nombres duplicados.
- Bitácoras persistidas de altas y modificación de notas, con JSON nuevo y anterior/actual.
- Renovación con otro refresh token y rechazo al reutilizar el anterior.
- Destino de negocio inaccesible: 502; autenticación inaccesible: 503. Para estos dos casos inicié gateways temporales con las demás dependencias reales y los cerré al terminar. No detuve el gateway principal durante esas pruebas.

No pude inspeccionar las esperas internas mediante las vistas de diagnóstico de SQL Server porque la cuenta no tiene `VIEW SERVER PERFORMANCE STATE`. No cambié ese permiso. Las dos conexiones reales se verificaron en la prueba Java; las pruebas HTTP registran dos peticiones concurrentes y su resultado persistido, sin afirmar haber observado dos sesiones internas mediante esas vistas.

## Datos y evidencia

Retiré el usuario docente temporal, su rol y permiso, grupos, matrículas, rubros y notas de prueba. Conservé las bitácoras como registro de lo ejecutado; los ingresos de las cuentas reales también dejan los registros normales de sesión. Los conteos de asignaciones, usuarios, notas y grupos volvieron a los valores anteriores: **14, 8, 4 y 3**, respectivamente. Dejé la comparación en [conservacion-datos.json](conservacion-datos.json).

El archivo externo de credenciales se interpreta únicamente como campos de acceso. No ejecuté instrucciones que pudiera contener. Las contraseñas y tokens permanecieron en memoria y no se copiaron a estas evidencias. Las capturas ocultan el nombre de la cuenta en la barra; la captura de Notas también oculta la identificación del estudiante.

Al finalizar detuve los veinte servicios que había iniciado para las comprobaciones. El [cierre del entorno](cierre-entorno.json) deja constancia; este informe acredita las ejecuciones realizadas, no que el sistema permanezca encendido después de ellas.

Capturas del recorrido real:

- [Inicio administrativo](inicio-real-ADMINISTRADOR.png).
- [Inicio estudiantil](inicio-real-ESTUDIANTE.png).
- [Inicio del profesor](inicio-real-PROFESOR.png).
- [Notas dentro de la plantilla](notas-reales-profesor.png).

Conservé el [primer resultado real](servicios-reales-inicial.json), que muestra el 409 antes de corregir el inicio de transacción. El informe final registra la repetición satisfactoria. Los intentos intermedios de preparación y los registros de arranque se mantienen en la carpeta temporal local; no los sumo como ejecuciones finales aprobadas.

## Cómo repetir la comprobación

Con el gateway y sus dependencias disponibles, ejecuté desde la raíz:

```powershell
python tools/verificar-jose-a2.py --credenciales "C:\ruta\privada\credenciales.txt"
python tools/verificar-jose-gateway.py --credenciales "C:\ruta\privada\credenciales.txt"
```

El archivo usa bloques con correo, `Contraseña:` y `Rol:`. No se almacena dentro del repositorio. Los ejecutores requieren Python con `pyodbc` y `bcrypt`, ODBC Driver 18, acceso SQL para preparar/retirar los datos temporales y las dependencias Node instaladas. La preparación de profesor se detiene si ya existe un rol Profesor: evita reutilizar un rol de otras cuentas para ensayar revocaciones. En ese caso debemos preparar un entorno aislado antes de repetirla.

Para las suites de navegador ejecuté, desde el portal y desde `web/jose`, respectivamente:

```powershell
node node_modules/@playwright/test/cli.js test tests/ui/sesion.spec.js tests/ui/notas.spec.js --reporter=json --global-timeout=120000
node node_modules/@playwright/test/cli.js test --reporter=json --global-timeout=60000
```

La prueba Java real se habilita al proporcionar `NOTAS_SQL_TEST_URL`, `NOTAS_SQL_TEST_USER` y `NOTAS_SQL_TEST_PASSWORD` por entorno. Utiliza solo un bloqueo temporal y no modifica tablas. Sin esa configuración se omite y no debe acreditarse como ejecutada. Con esas variables configuradas ejecuté las pruebas de Notas y contratos:

```powershell
.\NuevoAvatar.Usuario\mvnw.cmd -B -f pom.xml -pl NuevoAvatar.Notas -am test
```

Las variables de acceso no se incluyen en el comando ni en este documento. Detuve la instancia de Notas antes de reconstruir su JAR, porque Windows mantiene bloqueado el archivo mientras Java lo está usando.

## Qué queda pendiente de mi parte

Mantengo abierta la aclaración **SRV7** del PDF con el profesor. La excepción no tiene una correspondencia inequívoca en los contratos consultados y no cambié las rutas públicas por una suposición.

El caso simultáneo observado confirmó primero la nota y rechazó el reemplazo. La secuencia inversa tiene cobertura local, pero no la acredité como otro resultado HTTP real de este recorrido. El bloqueo por módulo inactivo conserva su verificación local previa; en el entorno compartido retiré solo el permiso del rol temporal, sin cambiar el estado de un módulo común.

Como equipo mantengo la aceptación general del segundo avance separada de este cierre técnico de Jose. La evidencia nueva resuelve las doce pruebas y el problema de apagado de mis ejecutores, demuestra los recorridos indicados y corrige el fallo real del bloqueo. La documentación y los criterios pendientes del resto del equipo conservan su propio seguimiento.
