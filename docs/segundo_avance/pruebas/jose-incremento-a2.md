# Jose - verificacion del incremento de A2

Fecha: 28 de octubre de 2026. Version: arbol de trabajo de este incremento,
sin commit nuevo. Responsabilidad: ADM2, ADM17, GTW1, GTW2.
La [ficha anterior](persona1.md) conserva la etapa inicial de backend;
los resultados actuales la amplian, no se suman otra vez sus pruebas.

## Ejecuciones actuales

| Comando | Resultado | Alcance |
|---|---|---|
| `dotnet test NuevoAvatar.Gateway.Tests/NuevoAvatar.Gateway.Tests.csproj --no-restore` | 16 aprobadas | Gateway YARP y Kestrel reales; propietarios/auth simulados por HTTP en puertos efimeros. |
| `dotnet test NuevoAvatar.Autenticacion.Tests/NuevoAvatar.Autenticacion.Tests.csproj --no-restore` | 29 aprobadas | Incluye las 25 previas y 4 de logout/auditoria; dependencias con dobles. |
| `.\NuevoAvatar.Usuario\mvnw.cmd -B -q -o -f pom.xml -pl NuevoAvatar.Notas -am test` | 34 Notas + 3 integracion aprobadas | Snapshots/commit/rollback, reglas y HTTP simulado; sin SQL remoto. |
| `npm test`, desde `web/jose` | 8 aprobadas | Cliente de sesion con fetch/almacenamiento controlados. |
| `npm run test:ui`, desde `web/jose` | 6 aprobadas | Chromium real con API interceptada por Playwright; no base de negocio. |
| `npm run build`, desde `web/jose` | Aprobado | Bundle React/Vite compilado. |
| `mvnw ... package -DskipTests` y `python tools/validar-artefactos.py --servicio Notas` | Aprobado | JAR actualizado/verificado; no cuenta pruebas nuevas. |
| `powershell -NoProfile -File tools/ejecutar-gateway.ps1 -Verificar` | Aprobado | Comprobacion del lanzador, sin iniciar servicios V1. |
| `dotnet run --project NuevoAvatar.Gateway/NuevoAvatar.Gateway.csproj --no-build --launch-profile http` y consultas locales | /health 200, /gateway/admin/usuario sin token 401, ruta con interno codificado 404 | Arranque real desde el proyecto y configuracion de puerto 5080; proceso detenido al terminar, sin llamadas al negocio. |

Total: **96 pruebas distintas aprobadas**, 0 fallos en las ejecuciones finales.
La repeticion de dos pruebas de navegador para inspeccionar capturas y la
reejecucion del gateway tras ampliar el verbo de Bitacora no aumentan el total.

Para una instalacion nueva, restaurar primero los proyectos .NET y ejecutar
`npm ci` en `web/jose`; las pruebas UI requieren
`npx playwright install chromium`. Los comandos Maven offline requieren la
cache local ya restaurada, igual que el proyecto existente.

## Casos verificados

| HU | Caso / observado | Fuente automatizada |
|---|---|---|
| GTW1 | GET/POST/PUT/DELETE atraviesan el proxy; preserva cuerpo/query/status, DELETE 204 y Location publico. | [ProxyHttpTests](../../../NuevoAvatar.Gateway.Tests/ProxyHttpTests.cs) |
| GTW1 | Todas las familias del catalogo tienen destino y politica; cubren los 22 propietarios. | ProxyHttpTests.RoutingDeTodasLasFamiliasTieneDestinoYPolitica |
| GTW1/GTW2 | Auth caida -> 503; negocio caido -> 502. Se distingue el origen de la falla. | ProxyHttpTests |
| GTW2 | Sin token/invalido -> 401; rol incorrecto -> 403; no llega al negocio. Internos, incluso codificados, -> 404. | ProxyHttpTests |
| GTW2 | El destino no recibe X-Rol, X-Servicio-Key ni X-Forwarded-Host inyectados. POST login/refresh funcionan sin token. | ProxyHttpTests |
| ADM2 | Login usa headers del contrato real, obtiene identidad del servidor y no persiste password. | [sesion.test.js](../../../web/jose/tests/sesion.test.js) |
| ADM2 | Peticiones simultaneas comparten refresh; fallo limpia la sesion; segundo 401 no causa bucle; logout gana a refresh pendiente. | sesion.test.js |
| ADM2 | Caida de negocio no devuelve datos ficticios ni elimina automaticamente una sesion valida. No envia tokens a URL ajena. | sesion.test.js |
| ADM2 | Credenciales incorrectas muestran mensaje requerido; estudiante no recibe controles de notas; logout limpia sessionStorage. | [jose.spec.js](../../../web/jose/tests/ui/jose.spec.js) |
| ADM2 | /logout exige token valido, conserva usuario para auditoria y el middleware no registra credenciales. | [SesionYGatewayTests](../../../NuevoAvatar.Autenticacion.Tests/SesionYGatewayTests.cs), [TokenYAuditoriaTests](../../../NuevoAvatar.Autenticacion.Tests/TokenYAuditoriaTests.cs) |
| ADM17 | UI asigna con POST, modifica con PUT y bloquea rubros al consultar notas existentes; sin desglose permite alta confirmada; dependencia caida no permite guardar. | jose.spec.js |
| ADM17 | Backend captura rubros anteriores y resultado persistido; evento publica JSON nuevo o anterior/actual solo tras commit; rollback no publica exito. | [NotasServiceTest](../../../NuevoAvatar.Notas/src/test/java/cr/ac/cuc/nuevoavatar/persona5/notas/NotasServiceTest.java), [AuditoriaNotasTest](../../../NuevoAvatar.Notas/src/test/java/cr/ac/cuc/nuevoavatar/persona5/notas/AuditoriaNotasTest.java) |
| ADM2/ADM17 | Login movil sin desbordamiento horizontal; inspeccion visual local de login y notas sobre CSS de Alejandro. | jose.spec.js; capturas temporales `tmp/jose-login-movil.png` y `tmp/jose-notas.png`. |

Las capturas temporales contienen fixtures de Playwright; no se publican como
evidencia final de integracion. Los datos simulados solo estan en pruebas, no
en los componentes de producto. No se mostraron credenciales SQL ni tokens
reales en las capturas.

## Limites y cierre pendiente por historia

| HU | Falta para acreditar cierre completo |
|---|---|
| GTW1 | Recorrido contra cada propietario real y configuracion de entrega. |
| GTW2 | Integracion con permisos/menu y operaciones propias de los demas responsables; resolver SRV7; evidencia real de rechazos. |
| ADM2 | Integracion del componente con layout ADM1 y redireccion al dashboard ADM3/WEB1 de cada perfil; comprobar login/refresh/logout y bitacora persistida con servicios reales. |
| ADM17 | Integracion en layout final, fixtures reales de profesor/grupo/matricula, bitacora SQL y pruebas de concurrencia/caidas con propietarios. |

No se aplicaron scripts SQL, no se cambiaron usuarios ni grupos compartidos y no
se enviaron correos. La revision de apoyo sigue pendiente. La matriz final debe
tener una fila por cada criterio del PDF y evidencia real de la version entregada.
