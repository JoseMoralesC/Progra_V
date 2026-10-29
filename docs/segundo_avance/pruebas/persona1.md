# Jose: verificacion tecnica local de A2

Fecha: 28 de octubre de 2026. Version: arbol de trabajo sin commit de esta sesion.
Alcance autorizado: backend de Jose, sin UI/UX. Revisor de apoyo: Fabian; revision
de apoyo pendiente. Estas pruebas no cierran historias ni constituyen capturas
de las interfaces finales. Fuentes vigentes: [backlog](../planificacion/backlog.md) y
[contratos](../planificacion/contratos.md).

Tras el reajuste del 28 de octubre, ADM17 corresponde completa a Jose y ADM3
completa a Fabian. Las pruebas de dashboard conservan su autoria tecnica de
Jose como aporte previo; no le asignan parte de ADM3 ni cierran esa historia.
Alejandro creo la plantilla de UI/UX y es responsable de ADM1.

## Ejecuciones finales

| Comando | Observado |
|---|---|
| `dotnet test NuevoAvatar.Autenticacion.Tests/NuevoAvatar.Autenticacion.Tests.csproj --no-restore` | 25 aprobadas, 0 fallos, 0 omitidas. Incluye 5 pruebas existentes. |
| `mvnw -B -q -o -f pom.xml -pl NuevoAvatar.Notas -am package` | 30 pruebas Notas y 3 integracion aprobadas; 0 fallos, errores u omisiones. JAR empaquetado. |
| `python tools/validar-artefactos.py --servicio Notas` | Artefacto Notas verificado. |

La primera prueba adicional de parametro negativo detecto una respuesta 403 en
lugar de 400 en MockMvc. Se ajusto la validacion de metodos de los controladores
para utilizar la validacion MVC; el caso aprobo en la ejecucion final.

## Cobertura comprobada

Todos los estados siguientes significan **verificado localmente con dobles**.
La fuente automatizada enlazada contiene pasos, datos y aserciones reproducibles;
las filas agrupan casos tecnicos y no reemplazan la matriz final de cada criterio
del PDF ni su evidencia de integracion real.

| HU relacionada | Caso / esperado | Observado y registro |
|---|---|---|
| ADM2 / GTW2 | Vincular identidad mediante email, tipo e identificacion; sin vinculo no elegir otra persona. | Aprobado: [SesionYGatewayTests](../../../NuevoAvatar.Autenticacion.Tests/SesionYGatewayTests.cs). |
| ADM2 / GTW2 | Ignorar headers de identidad del navegador; sin Bearer no consultar la cuenta. | Aprobado: SesionYGatewayTests. |
| GTW2 | JWT con firma ajena, vencido o malformado no valida. | Aprobado: [TokenYAuditoriaTests](../../../NuevoAvatar.Autenticacion.Tests/TokenYAuditoriaTests.cs). |
| ADM2 | Auditar login exitoso/fallido, refresh y consulta de sesion sin guardar credenciales. | Aprobado: TokenYAuditoriaTests. Persistencia remota pendiente. |
| GTW2 | Solo login/refresh por POST son publicos; rutas internas, incluso codificadas, quedan bloqueadas. | Aprobado: SesionYGatewayTests. |
| GTW2 | Rol equivocado o ruta sin politica no llega al destino. Auth caida devuelve 503. | Aprobado: SesionYGatewayTests. Middleware probado con destino HTTP simulado. |
| ADM3 | Consultar grupos del profesor de sesion y seleccionar periodos activos; fallo no se presenta como vacio. | Aprobado: [ResumenProfesorTest](../../../NuevoAvatar.Notas/src/test/java/cr/ac/cuc/nuevoavatar/persona5/notas/ResumenProfesorTest.java). |
| ADM17 | Estudiante y profesor ajeno no operan notas; verificar curso/periodo/grupo. | Aprobado: [SeguridadNotasTest](../../../NuevoAvatar.Notas/src/test/java/cr/ac/cuc/nuevoavatar/persona5/notas/SeguridadNotasTest.java). |
| ADM17 | Rechazos por HTTP antes de llamar al servicio; profesor propio usa PUT; nota/grupo invalidos devuelven 400. | Aprobado: [NotasHttpTest](../../../NuevoAvatar.Notas/src/test/java/cr/ac/cuc/nuevoavatar/persona5/notas/NotasHttpTest.java). MockMvc sin procesos remotos. |
| ADM17 | Rango 1-100, POST no sobrescribe, PUT no crea, nota invalida no escribe y fallo SQL no publica exito. | Aprobado: [EscrituraNotasTest](../../../NuevoAvatar.Notas/src/test/java/cr/ac/cuc/nuevoavatar/persona5/notas/EscrituraNotasTest.java). |
| ADM17 | Snapshot anterior/actual; enviar bitacora solo tras commit y no ante rollback. | Aprobado: EscrituraNotasTest y [AuditoriaNotasTest](../../../NuevoAvatar.Notas/src/test/java/cr/ac/cuc/nuevoavatar/persona5/notas/AuditoriaNotasTest.java). Ciclo transaccional en memoria, no SQL real. |
| ADM17 | Conservar rechazo de suma distinta de 100 y bloqueo de rubros con notas existentes. | Las 2 pruebas existentes de NotasServiceTest siguen aprobadas. Prueba conjunta real pendiente. |

## Pendiente antes de acreditar cierre

1. Integrar middleware y politicas de ruta al gateway unico de GTW1.
2. Preparar profesor propietario, profesor ajeno y dos estudiantes identificables
   en un entorno aislado; comprobar el vinculo real de cuenta y expediente.
3. Ejecutar login/refresh, grupos propios, consultas por estudiante/grupo y
   alta/modificacion de notas usando servicios reales por el gateway.
4. Verificar en Bitacora los registros persistidos, el bloqueo tras la primera
   nota y la ausencia de cambios tras rechazos. Probar fallas y concurrencia acordada.
5. Integrar notas completas de ADM17 con ADM3 de Fabian y la plantilla de UI/UX
   creada por Alejandro en ADM1,
   sin atribuir a estas pruebas la implementacion visual.
6. Registrar version/commit, una fila por criterio final, capturas y revision de apoyo.

No se ha consultado ni modificado SQL Server durante estas verificaciones.
No se han enviado correos ni aplicado migraciones.
