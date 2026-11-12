# Evidencias de la auditoría 5 - Segundo avance

> Organización de la entrega: trasladé los resultados esenciales a [resultados conservados](resultados-conservados.md). Los nombres de logs, JSON, TRX y guiones que aparecen más abajo identifican archivos históricos retirados; sus comandos son antecedentes de ejecución. Conservamos únicamente Markdown y capturas PNG.

Fecha de revisión: 12 de noviembre de 2026. Base: `ba576558f5d61c34cf1d0561b2a325b375605496`.

Ejecuté estas comprobaciones para la [auditoría 5](../auditoria_avance5.md). Conservé los resultados, incluidos los fallos, del diagnóstico inicial; en ese primer corte no cambié el producto ni sus pruebas existentes. Las respuestas controladas del navegador son datos ficticios de prueba y no demuestran persistencia en servicios reales.

## Actualización posterior de Jose

El contenido original de esta carpeta describe el diagnóstico inicial. Después corregí las pruebas y el bloqueo de Notas y ejecuté servicios/SQL reales. El [registro actualizado](resultados-conservados.md#resultados-actualizados) sustituye únicamente las ejecuciones repetidas de Jose, conserva .NET/Node y los resultados anteriores de los demás integrantes y reúne 553 casos aprobados y 12 fallidos de 565 incluidos. El [seguimiento de Jose](../../pruebas/jose-cierre-auditoria5/README.md) contiene las 82 pruebas locales repetidas y las 45 comprobaciones reales, con sus límites y capturas. Las catorce comprobaciones detalladas del navegador ya están incluidas en la confirmación del recorrido real y no se suman de nuevo.

`resultados.json` conserva el primer corte y sus doce fallos de Jose; no es el resultado vigente de ese integrante. `resumir.py` corresponde al levantamiento inicial y no reconstruye por sí solo la consolidación posterior. Las afirmaciones de diagnóstico que siguen se leen con ese alcance temporal; las correcciones y pruebas nuevas se consultan en los enlaces anteriores.

## Resultado y atribución

El [resumen por caso](resultados-conservados.md#resultados) conserva un responsable único, grupo, nombre y estado. El ejecutor temporal `resumir.py` lo reconstruyó desde los informes de esta ejecución y los resultados Java generados en `target/surefire-reports`.

- Atribuí autenticación, gateway, contratos compartidos y notas a Jose.
- Atribuí cursos/períodos, carreras, profesores, notificaciones y mantenimientos de Ramses a Ramses.
- Atribuí expediente, prematrícula, perfil propio y el recorrido de plantilla/estudiante a Alejandro.
- Atribuí las restantes pruebas de Usuario y las páginas de dashboard, usuarios, roles, parámetros e instituciones a Fabian.

La numeración antigua de paquetes no altera la responsabilidad de las historias. Los dos casos heredados de grupos ejecutados por la solución .NET quedan como apoyo y no suman a las barras. Las pruebas del gateway apoyan a varios consumidores, pero se cuentan una sola vez en Jose.

## Ejecuciones conservadas

| Archivo | Comprobación y límite |
| --- | --- |
| `dotnet.log`, `dotnet/*.trx` | 182 casos .NET aprobados; 180 corresponden a las barras de este corte y dos son apoyo heredado. |
| `java-red.log` | Contratos compartidos y Usuario. Usuario: 11 aprobados, dos fallos de aserción y un error; los tres problemas pertenecen a perfil propio. El reactor se detuvo al fallar Usuario. |
| `java-restantes-red.log` | Continuación independiente: Institución, Profesor, Carrera, Expediente, Prematrícula, Notas y Notificación. Compilación y casos disponibles aprobados. Institución no tiene suite propia; no se registra como una prueba funcional aprobada. |
| `java.log`, `java-restantes.log` | Intentos sin red bloqueados por dependencias ausentes en el repositorio accesible desde ese contexto. Se reintentó con acceso autorizado. No son casos fallidos del producto. |
| `jose-node.log`, `ramses-node.log` | 10 y 14 casos Node aprobados, respectivamente. |
| `principal-build.log`, `jose-build.log`, `ramses-build.log` | Las tres aplicaciones React construyeron. |
| `principal-lint.log` | Advertencias de React y de ejecutores reales; no acredita defectos funcionales por sí solo. |
| `principal-ui-final.json` | 47 casos: 29 aprobados y 18 fallidos. Diecisiete fallos incluyen auditoría de interfaz no prevista; otro espera un selector que no aparece en el caso de expiración. |
| `jose-ui-final.json`, `ramses-ui-final.json` | Seis casos aprobados por cliente aislado. Los tres ejecutores finales, incluido el principal, registran además tiempo agotado al cerrar su servidor auxiliar. |
| `recorrido-estudiante.cjs`, `recorrido-estudiante.json`, `recorrido-estudiante.log` | 13 comprobaciones propias de auditoría: 10 aprobadas y tres fallidas. Inicio, seis medidas de ancho, confirmaciones y salida entre pestañas pasan. Fallan paginación parametrizada, ruta activa única e identidad visible en la barra móvil. |
| `plantilla-*.png` | Seis capturas de los inicios de administración/estudiante en 390, 768 y 1366 px. No certifican todos los formularios. |
| `limites-servicios.json` | 22 servicios, 298 fuentes, cero infracciones encontradas por la comprobación de accesos SQL/JPA. Es inspección estática; no prueba cuentas SQL ni todos los accesos posibles. |
| `artefactos.log` | Trece JAR aceptados por el validador de clases y contratos disponibles. No acredita que estén desplegados ni empaquetados con el código más reciente. |

Los archivos `ui-principal.json`, `jose-ui.json` y `ramses-ui.json` corresponden al primer intento sin límite global, detenido al no finalizar el cierre del servidor. No contienen un informe completo ni se suman al resultado final. Los primeros comandos `npm` encontraron la política de ejecución de PowerShell; continué usando `npm.cmd`, sin modificar esa política.

En `recorrido-estudiante-inicial.json` conservé el primer diagnóstico del recorrido: el caso de ancho esperaba un nombre visible que el CSS oculta en móvil. Ajusté solamente el ejecutor nuevo de auditoría para medir el ancho cuando el elemento ya existe, y agregué una comprobación distinta para la visibilidad del nombre/rol. El informe final conserva el defecto de identidad; no presenta la corrección del criterio de medición como una corrección del producto.

## Reproducción

Desde la raíz ejecuté .NET con:

```powershell
dotnet test PrograV.sln --no-restore --logger trx --results-directory docs/segundo_avance/auditoria/evidencias5/dotnet
```

Ejecuté Java en dos grupos para que el fallo de Usuario no impidiera revisar las otras áreas:

```powershell
.\NuevoAvatar.Usuario\mvnw.cmd -B -f pom.xml -pl NuevoAvatar.Usuario,NuevoAvatar.Institucion,NuevoAvatar.Profesor,NuevoAvatar.Carrera,NuevoAvatar.Expediente,NuevoAvatar.Prematricula,NuevoAvatar.Notas,NuevoAvatar.Notificacion -am test
.\NuevoAvatar.Usuario\mvnw.cmd -B -fae -f pom.xml -pl NuevoAvatar.Institucion,NuevoAvatar.Profesor,NuevoAvatar.Carrera,NuevoAvatar.Expediente,NuevoAvatar.Prematricula,NuevoAvatar.Notas,NuevoAvatar.Notificacion -am test
```

En cada carpeta React ejecuté `npm.cmd run build`. En `web/jose` y `web/ramses` ejecuté además `npm.cmd test`; en el portal principal, `npm.cmd run lint`. El comando final de navegador, desde cada carpeta, fue:

```powershell
node node_modules/@playwright/test/cli.js test --reporter=json --timeout=8000 --global-timeout=180000 --workers=1
```

Conservé el límite de ocho segundos por caso y tres minutos de ejecución total. El tiempo agotado del selector de notas requiere revisión con su escenario preparado; no se atribuye automáticamente al producto. Los informes finales incluyen los errores generales de apagado, aunque los casos aislados hayan pasado.

El recorrido propio utilizó Vite del portal principal en `127.0.0.1:4175`. Sus guiones temporales fueron retirados al organizar la entrega. Para los artefactos utilicé:

```powershell
python tools/validar-artefactos.py
```

Para la comprobación de límites utilicé la lógica de `tools/auditar-limites.py`, cambiando únicamente en memoria la ruta de salida a esta carpeta. Así no sobrescribí su evidencia histórica ni modifiqué el ejecutor original.

Algunas salidas redirigidas por PowerShell tienen codificación UTF-16; el ejecutor temporal detectaba esa codificación. `resultados.json`, los documentos nuevos y el recorrido propio se guardan en UTF-8. Los resultados Java por caso del resumen se copian como datos simples; no se incorporan los XML completos de Surefire con propiedades del entorno.

## Límites de aceptación

No envié correo, no apliqué scripts SQL, no alteré permisos ni ejecuté los scripts reales que crean o eliminan cuentas/datos. No había gateway de negocio escuchando en 5080 cuando comprobé los puertos. La evidencia real anterior de Fabian y Ramses está referenciada en la auditoría, separada de estas ejecuciones.

Las barras de pruebas expresan únicamente la proporción de casos aprobados en este conjunto. No son cobertura, cierre de historias ni barras de aportes. Los fallos generales de apagado se informan aparte; no se ocultan detrás de un 100% de casos aprobados.
