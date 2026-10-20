# Desarrollo Java

## Biblioteca e IDE

Abro la carpeta raiz `Progra_V`, no `services/`. El `pom.xml` de la raiz agrupa
los 13 servicios Java y la biblioteca `integracion`. Cada servicio conserva su
version de Spring Boot; la biblioteca no incorpora otro servidor ni impone sus
dependencias Spring en tiempo de ejecucion.

Mantengo las clases compartidas en `integracion/src/main/java`. Maven incluye
`integracion/contratos.json` en el JAR de la biblioteca; conservo ese contrato
en su ubicacion para no cambiar sus consumidores .NET.

La configuracion compartida `.vscode/settings.json` excluye `services/` de la
importacion Java. Si VS Code conserva errores anteriores, abro la paleta de
comandos y ejecuto `Java: Clean Java Language Server Workspace`, confirmo el
reinicio y espero la importacion Maven. No borro carpetas de datos de VS Code
manualmente. Esta recarga requiere una comprobacion visual en el editor.

## Compilacion y pruebas locales

Desde la raiz ejecuto:

```powershell
.\NuevoAvatar.Usuario\mvnw.cmd -f pom.xml clean verify
python tools/validar-artefactos.py
dotnet test NuevoAvatar.Autenticacion.Tests/NuevoAvatar.Autenticacion.Tests.csproj
dotnet test NuevoAvatar.Academico.Tests/NuevoAvatar.Academico.Tests.csproj
```

Estas pruebas usan dobles de prueba y H2 en memoria; no arrancan los servicios
contra SQL Server ni envian correos. Las pruebas de integracion con el servidor
son independientes. Los recursos SQL bajo `src/test/resources/sql` son copias
de los escenarios H2 anteriores, no migraciones ni scripts del servidor.

Para compilar un servicio y su biblioteca uso:

```powershell
powershell -NoProfile -File tools/compilar-servicios.ps1 -Servicio Usuario
```

Si ejecuto Maven dentro de un servicio fuera del reactor, primero instalo la
biblioteca en la cache local:

```powershell
.\NuevoAvatar.Usuario\mvnw.cmd -f integracion/pom.xml install
```

## Recuperacion de las pruebas anteriores

Los 26 archivos de pruebas originales tienen su destino en proyectos activos.
Conservo las pruebas originales en `services/` como referencia de la migracion.
Los primeros 17 archivos quedaron distribuidos asi:

| Origen | Destino | Archivos |
| --- | --- | --- |
| Autenticacion .NET | `NuevoAvatar.Autenticacion.Tests` | `AuthControllerTests` |
| Seguridad de persona 2 | `NuevoAvatar.Usuario/src/test` | `AutorizacionInterceptorTest`, `BitacoraFilterTest` |
| Expediente y seguridad de persona 4 | `NuevoAvatar.Expediente/src/test` | `DireccionClientTest`, `ExpedienteValidadorTest`, `AutorizacionInterceptorTest`, `BitacoraServiceTest`, `SeguridadClientTest` |
| Prematricula | `NuevoAvatar.Prematricula/src/test` | `PrematriculaValidadorTest` |
| Cliente de notas | `NuevoAvatar.HistorialAcademico/src/test` | `MatriculaNotasClientTest` |
| Persona 5 | `src/test` de Matricula, Notas, Factura, Pago y Notificacion | Los cinco `ServiceTest` y `NuevoAvatarPersona5ApplicationTests` en Matricula |
| Configuracion local de persona 5 | `integracion/src/test` | `ConfiguracionLocalTest`, adaptado al cargador compartido actual |

Adapto los nueve restantes de esta manera:

| Prueba original | Destino actual |
| --- | --- |
| `NuevoAvatarPersona2ApplicationTests` | Usuario: arranque del contexto Spring con H2, sin credenciales reales |
| `ValidacionesRequestTest` | `NuevoAvatar.Academico.Tests`: validadores actuales de Curso, Grupo y Periodo en C# |
| `GrupoServiceTest` | `NuevoAvatar.Academico.Tests`: rechazo de Curso inexistente mediante cliente HTTP simulado |
| `PeriodoServiceTest` | `NuevoAvatar.Academico.Tests`: fechas verificadas con el servicio y validador actuales |
| `SeguridadRutasTest` | Una prueba por servicio de Persona 4; cada una comprueba sus rutas propias |
| `ExpedienteApiTest` | Expediente: CRUD, validaciones, referencias remotas, bitacora y rollback local |
| `PrematriculaApiTest` | Prematricula: CRUD, oferta academica simulada, autorizacion y rollback local |
| `HistorialAcademicoApiTest` | HistorialAcademico: expedientes, matriculas y cursos simulados; calculo de promedios |
| `ListadoEstudiantesApiTest` | ListadoEstudiantes: grupos, cursos, carreras y expedientes simulados |

En Persona 4 uso `PruebaRemotaBase` para reemplazar `DatosRemotos` con un doble
que consulta los escenarios H2. El codigo de produccion sigue usando sus clientes
remotos; los escenarios locales no demuestran conectividad real entre servicios
ni reproducen la distribucion fisica de bases del servidor. Conservo las pruebas
de fallos y rollback sin desactivarlas. La verificacion `DB_NAME()` se sustituye
solo en las pruebas, porque H2 no es SQL Server.

Registro dos diferencias respecto a los casos del monolito:

- Expediente rechaza con 409 cambiar una identificacion referenciada por otros
  servicios; ya no presupongo actualizaciones SQL en cascada entre bases.
- El validador .NET actual de Periodo permite inicio y fin iguales y rechaza un
  fin anterior. La prueba documenta ese contrato; no cambio esa regla de negocio.

La prueba que antes invocaba `/expediente` dentro de Prematricula ahora comprueba
que ese endpoint no existe alli. La restriccion por referencias se comprueba en
Expediente, que es el servicio propietario.

No elimino `services/` en esta migracion de pruebas. El lanzador local no versionado
`tools/ejecutar-persona-java.ps1` aun apunta a esa estructura antigua; los `.bat`
actuales utilizan `tools/ejecutar-servicio.ps1` y `NuevoAvatar.*`.
