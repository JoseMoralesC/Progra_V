# Evidencia local de la auditoría 4

> Organización de la entrega: trasladé los resultados esenciales a [resultados conservados](resultados-conservados.md). Los nombres de logs, JSON, TRX y guiones que aparecen más abajo identifican archivos históricos retirados; sus comandos son antecedentes de ejecución. Conservamos únicamente Markdown y capturas PNG.

Fecha: 10 de noviembre de 2026, America/Mexico_City. Versión: `4eeb3362dc1071fb4fb9bb046a9ea0e195724460` más el árbol local.

Mantengo separado el corte actual de los antecedentes. El balance vigente es **550 casos: 548 aprobados y dos fallidos**. Ninguna ejecución de esta carpeta acredita por sí sola SQL Server, SMTP o aceptación integral del segundo avance.

## Archivos del corte actual

| Archivo | Contenido |
| --- | --- |
| [resultados_actuales](resultados-conservados.md#resultados-actuales) | Cada caso final, suite, responsable de HU, estado y detalle aplicable. Incluye análisis estático y artefactos verificados. |
| [notas_actual.png](notas_actual.png) | Notas dentro del layout, con grupo y datos simulados. |
| [instituciones-edicion.png](instituciones-edicion.png) | Resultado del caso de edición/búsqueda que no encontró el registro esperado. |
| [instituciones-permisos.png](instituciones-permisos.png) | Resultado del caso que esperaba bloqueo de Instituciones sin permiso. |

Los fallos pertenecen a ADM8 de Fabian. El primero requiere revisar la coordinación entre guardado, limpieza de filtros y búsqueda; no demuestra por sí mismo una pérdida de persistencia en SQL. El segundo conserva controles donde la prueba espera el estado de acceso restringido. No se modificó ese formulario ni se eliminaron sus aserciones.

## Balance por integrante

| Responsable de HU | Aprobadas | Fallidas | Total |
| --- | ---: | ---: | ---: |
| Jose | 155 | 0 | 155 |
| Ramses | 78 | 0 | 78 |
| Alejandro | 217 | 0 | 217 |
| Fabian | 95 | 2 | 97 |
| Compartida | 3 | 0 | 3 |
| Total | 548 | 2 | 550 |

La asignación sigue la historia que respalda cada prueba, no el antiguo número de persona del paquete. Las tres de Integración no se duplican por integrante. El resultado aprobado/ejecutado es una medida de estas ejecuciones, no cobertura del PDF, aporte ni calificación.

## Ejecuciones del corte

Desde la raíz:

```powershell
.\NuevoAvatar.Usuario\mvnw.cmd -B -pl NuevoAvatar.Usuario,NuevoAvatar.Institucion,NuevoAvatar.Carrera,NuevoAvatar.Profesor,NuevoAvatar.Expediente,NuevoAvatar.Prematricula,NuevoAvatar.Notas,NuevoAvatar.Notificacion -am package
dotnet test PrograV.sln --filter 'FullyQualifiedName!~GrupoServiceTest&FullyQualifiedName!~GrupoExigeIdentificadores'
npm --prefix web/jose test
npm --prefix web/ramses test
npm --prefix web/jose run test:ui
npm --prefix web/ramses run test:ui
npm --prefix web/jose run build
npm --prefix web/ramses run build
```

El balance conserva 287 casos Java, 180 .NET, 24 Node, doce de navegador aislado y 47 de la entrada principal. Las diecinueve de Curso/Periodo se confirmaron también con el filtro académico; esa repetición sustituye el mismo conjunto y no agrega casos.

En `NuevoAvatar.Estudiante.React`:

```powershell
npm run build
npm run lint
node node_modules/@playwright/test/cli.js test --timeout=20000 --reporter=json
```

La ejecución final completa del portal contiene **45 aprobadas y dos fallidas de 47**. Las comprobaciones parciales y los intentos anteriores no se suman. Sesión/Notas aportan 27 casos, los mantenimientos de Ramses diez y el portal administrativo diez.

El lint conserva cinco advertencias previas en InicioEstudiante, Prematrícula y NotificationContext; no presenta errores. Los tres builds React aprobaron. Las pruebas .NET aprobaron 29 de Autenticación, 132 de Gateway y diecinueve académicas del corte.

Verifiqué los ocho artefactos Java seleccionados con:

```powershell
python tools/validar-artefactos.py --servicio Usuario
```

Repetí el mismo validador para Institución, Carrera, Profesor, Expediente, Prematrícula, Notas y Notificación. Cada uno aprobó la comprobación de JAR y biblioteca/contrato compartidos. No equivale a verificar su arranque con la infraestructura real.

El analizador `tools/auditar-limites.py` recorrió 22 servicios y 297 fuentes sin infracciones detectadas. Ejecuté el mismo código con salida separada, preservando el informe histórico; no comprueba permisos SQL efectivos.

## Límites y antecedentes

El navegador intercepta las APIs y utiliza datos ficticios. Las pruebas HTTP del gateway tienen Kestrel/YARP reales con destinos controlados. Java usa H2 o dobles según la suite. Notas comprueba su coordinación con bloqueos simulados; Periodo prueba concurrencia de una instancia en memoria. No se certifican dos conexiones SQL Server ni SMTP reales, no se aplicaron migraciones y no se enviaron correos.

Los archivos `resultados.json`, `diagnostico.json`, `diagnostico.cjs`, `notas.png` y las capturas `movil-*` pertenecen al primer levantamiento de esta misma auditoría. Se conservan como antecedentes; **no se suman al balance actual**. El guion de diagnóstico describe aquel contrato de interfaz y no debe usarse como prueba vigente de la sesión actual. Los anchos móviles documentados allí son observaciones del layout que sigue pendiente de revisión.
