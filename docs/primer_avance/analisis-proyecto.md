# Mapa técnico de Nuevo Avatar

Este diagnostico describe el estado previo a la separacion de bases y la adaptacion de servicios. El funcionamiento posterior se documenta en [integracion-servicios.md](integracion-servicios.md).

Revisión del 14 de octubre de 2026 sobre la rama local `jose`, HEAD `1ad918f`.

## Alcance de esta revisión

Revisión estática del PDF de requisitos, Doc Master, planificación, auditorías, estructura de proyectos, puntos de entrada, controladores, lógica de negocio, validadores, persistencia, clientes HTTP, configuración, scripts SQL, scripts de ejecución y pruebas existentes. También se inspeccionó visualmente el diagrama de la raíz.

No se ejecutaron suites, servicios, migraciones ni solicitudes a la base remota. Los resultados de pruebas de auditorías y archivos de reportes son evidencia histórica; no certifican la ejecución actual. No se modificó código de aplicación ni configuración.

## Propósito y requisitos

El sistema administra una academia: usuarios, autenticación, oferta académica, estudiantes, prematrícula, matrícula, evaluación, historial, facturación, pagos y correo. El alcance documentado del primer entregable es backend/API y base de datos, probado mediante Postman, sin interfaz gráfica.

La fuente funcional principal es [Proyecto Nuevo Avatar V1.pdf](requerimientos/Proyecto%20Nuevo%20Avatar%20V1.pdf). El PDF contiene **22 HU**: USR1–USR5, ACD1–ACD6, MAT1–MAT5, ACA1–ACA2, IPN1–IPN3 y GEN1. Roadmap y Doc Master indican 24, aunque sus listas suman 22.

La página 29 pide documentación de análisis/diseño, diagramas de BD/casos de uso/clases, pruebas por criterio, conclusiones, bibliografía, SQL, servicios REST, HU actualizadas y PR a la rama final.

## Arquitectura que existe

Hay **22 aplicaciones independientes**, una por HU, y tres bibliotecas compartidas de seguridad .NET. Las aplicaciones independientes están en las carpetas `NuevoAvatar.*` de la raíz.

Coexisten con proyectos agrupados en `services/seguridad`, `services/persona2`, `services/persona4` y `services/persona5`. El commit `b7ab928` separó las aplicaciones de personas 1, 2, 4 y 5. Los lanzadores actuales apuntan a las aplicaciones independientes; las pruebas automatizadas permanecen en las agrupadas.

Los servicios se despliegan por separado, pero comparten tablas de SQL Server. Por ejemplo, Matrícula lee directamente Curso, Grupo y Periodo; Factura lee Matrícula; Pago modifica Factura. La separación de procesos no implica aislamiento de datos por servicio.

### Tecnologías declaradas en los proyectos

| Área | Tecnología |
|---|---|
| Seguridad y servicios de persona 3 | C# / ASP.NET Core, .NET 8 |
| Persistencia de seguridad | Entity Framework Core |
| Persistencia de persona 3 | Dapper y SQL parametrizado |
| Personas 2 y 5 | Java 21, Spring Boot 4.1.1, JPA/Hibernate |
| Persona 4 | Java 21, Spring Boot 3.5.16, Spring JDBC |
| Autenticación | BCrypt, JWT HMAC SHA-256, refresh token persistido |
| Integraciones | REST/JSON con RestClient y HttpClient |
| Correo | JavaMailSender / SMTP |
| Pruebas existentes | JUnit, Mockito, Spring Boot Test, H2, pruebas .NET y Postman |
| Infraestructura documentada | SQL Server remoto mediante Tailscale |

Las versiones anteriores son las declaradas localmente; no se investigó compatibilidad ni documentación externa. El roadmap propone Spring Boot 3 como stack uniforme, pero el código actual es mixto.

## Responsables, servicios y puertos

Puertos HTTP tomados de `tools/servicios.json`, `application.properties` y perfiles HTTP .NET. Son valores de configuración, no comprobaciones de procesos activos.

| Persona | HU | Aplicación | Puerto | Rutas principales |
|---|---|---|---:|---|
| Héctor | USR5 | Autenticacion | 5158 | `/login`, `/refresh`, `/validate` |
| Héctor | GEN1 | Bitacora | 5159 | `/bitacora` |
| Héctor | USR2 | Rol | 5160 | `/rol` |
| Héctor | USR3 | Parametro | 5161 | `/parametro` |
| Héctor | USR4 | Modulo | 5162 | `/modulo` |
| Ramsés | USR1 | Usuario | 8082 | `/usuario` |
| Ramsés | ACD1 | Institucion | 8086 | `/institucion` |
| Ramsés | ACD6 | Profesor | 8087 | `/profesor` |
| Ramsés | ACD2 | Carrera | 8088 | `/carrera` |
| Alejandro | ACD3 | Curso | 5255 | `/curso` |
| Alejandro | ACD4 | Grupo | 5256 | `/grupo` |
| Alejandro | ACD5 | Periodo | 5262 | `/periodo` |
| Alejandro | MAT4 | Direccion | 5227 | `/provincias`, `/cantones/{provinciaId}`, `/distritos/{provinciaId}/{cantonId}` |
| Fabián | MAT3 | Expediente | 8084 | `/expediente` |
| Fabián | MAT1 | Prematricula | 8089 | `/prematricula` |
| Fabián | ACA1 | HistorialAcademico | 8090 | `/historialacademico` |
| Fabián | ACA2 | ListadoEstudiantes | 8091 | `/listadoestudiantes` |
| José | MAT2 | Matricula | 8085 | `/matricula` |
| José | MAT5 | Notas | 8092 | `/cargardesglose`, `/asignarnotarubro`, `/obtenerdesglose`, `/obtenernotas` |
| José | IPN1 | Factura | 8093 | `/factura` |
| José | IPN2 | Pago | 8094 | `/pago` |
| José | IPN3 | Notificacion | 8095 | `/notificar` |

Las tres bibliotecas compartidas son `NuevoAvatar.Seguridad.Abstract`, `.DataAccess` y `.Models`. Los servicios .NET de seguridad reutilizan sus interfaces, implementación y modelos.

## Flujos y reglas de negocio

### Autenticación y datos generales

`POST /login` recibe `usuario` y `contrasena` en headers, busca el email (también prueba el sufijo `@cuc.cr`), verifica BCrypt y devuelve 201 con JWT, expiración, refresh token y usuario. `/refresh` revoca el refresh anterior antes de generar otro. `/validate` acepta token por query o header y devuelve `true`/200 o 401.

El JWT identifica al usuario mediante `sub`. Se valida firma y vigencia; no se valida issuer/audience. `EXPJWT` y `EXPREFRESH` configuran duración, con valores por defecto de 5 y 60 minutos. La autenticación no implementa por sí misma una matriz de permisos por rol.

Usuario aplica correo único, hash BCrypt, existencia de rol y correspondencia entre dominio y rol: `cuc.cr` para ESTUDIANTE; `cuc.ac.cr` para PROFESOR o ADMIN. Los filtros permiten buscar por identificación, nombre y tipo. Rol, Parámetro y Módulo tienen CRUD propios; sus controladores registran cambios con JSON en la bitácora.

### Oferta académica y estudiantes

Institución → Carrera → Curso → Grupo. Carrera requiere institución y profesor director existentes. Profesor exige mayoría de edad, teléfonos sin repetir y dominio definido por `DOMPROF`. Curso valida nivel 1–12 y nombre. Grupo requiere curso, profesor, horario, cupo y periodo.

Expediente contiene identificación, datos personales, teléfonos y dirección. Valida `DOMESTUD` por el servicio Parámetro y la jerarquía territorial mediante Dirección. Usa repositorios JDBC y registra el estado persistido nuevo/anterior/actual/eliminado.

Prematrícula valida estudiante, carrera, cursos y periodo; exige cursos de primer nivel y fecha inicial futura. Tiene su propia cabecera y detalle de cursos. No se identificó una conversión automática de prematrícula a matrícula: son operaciones independientes.

### Matrícula, notas y consultas académicas

Matrícula valida curso y grupo, correspondencia curso/grupo/periodo, fechas del periodo y duplicado activo. Su eliminación cambia el estado a ANULADA. El criterio implementado de periodo activo es `fechaInicio <= hoy <= fechaFin`.

Notas define rubros por grupo cuya suma debe ser exactamente 100. Permite reemplazarlos mientras no tengan notas. La nota se asigna por matrícula y rubro, entre 1 y 100, y exige que ambos correspondan al mismo grupo. Una asignación repetida actualiza la nota.

Historial consulta intentos del estudiante y obtiene calificaciones mediante HTTP desde Notas. Calcula el acumulado ponderado `sum(nota * porcentaje / 100)`; los rubros pendientes aportan cero. Rechaza respuestas inconsistentes y ambigüedad de múltiples intentos con el mismo curso/grupo.

Listado recorre grupos del periodo y consulta Matrícula por curso/grupo; filtra matrículas activas del periodo solicitado y combina los resultados con estudiante, carrera, curso y grupo almacenados en SQL.

### Facturación, pagos y notificaciones

Factura se crea desde `matriculaId`, requiere matrícula activa y evita otra factura no anulada para ella. Usa cabecera/detalle, subtotal 30000, impuesto 600 y total 30600; inicia PENDIENTE. Reversarla la deja ANULADA, pero bloquea facturas PAGADA.

Pago exige una factura PENDIENTE, toma su total, crea un pago APLICADO y deja la factura PAGADA en una transacción local. Reversar el pago lo deja ANULADO y devuelve la factura a PENDIENTE. No representa pagos parciales.

Notificación envía email con asunto y HTML por SMTP. El envío se solicita explícitamente mediante `/notificar`; no se observó un disparo automático desde factura o pago.

## Persistencia y scripts SQL

El modelo usa cinco esquemas: `seguridad`, `general`, `academico`, `matricula` y `finanzas`. El diagrama [PrograV Diagrama completo.jpeg](../PrograV%20Diagrama%20completo.jpeg) declara 26 tablas y 31 relaciones; su organización es coherente con los dominios revisados. La imagen no verifica el estado actual de la BD remota.

Seguridad contiene rol, usuario, refresh_token, parametro y modulo; General contiene bitacora. Académico contiene institución, profesor, teléfonos de profesor, carrera, curso, periodo y grupo. Matrícula contiene catálogos territoriales, estudiante/teléfonos, prematrícula/detalle, matrícula, rubros y notas. Finanzas contiene factura, detalle y pago.

`db/persona2/sql/README.md` ofrece dos rutas alternativas para la base inicial. Su denominada instalación completa cubre la base de persona 2 y catálogos, no todas las tablas finales de personas 4 y 5. Persona 3 aporta scripts de oferta y direcciones; personas 4 y 5 agregan migraciones. Hay definiciones solapadas que impiden asumir que todos los scripts pueden ejecutarse consecutivamente.

En particular, `db/persona4/migraciones/002_relacionar_expedientes.sql` relaciona tablas de Matrícula/Factura con Estudiante, por lo que el orden final debe considerar que esas tablas ya existan. No se ha certificado aquí una instalación desde cero.

## Configuración y ejecución

`tools/ejecutar-servicio.ps1` lee `.env` como datos, transforma variables de BD para Java y .NET, establece destinos separados y verifica el puerto antes de ejecutar Maven o dotnet. Los `.bat` agrupan servicios por persona. `-Verificar` valida configuración sin arrancar procesos.

El manifiesto tiene 18 entradas: deja fuera los cuatro servicios de persona 3, cuyos puertos están en sus perfiles .NET. `tools/detener-servicios.ps1` contempla también esos puertos y comprueba pertenencia al repositorio antes de detener procesos.

Las variables de conexión usan diferentes convenciones: .NET requiere `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`; persona 4 usa `DB_URL` y `DB_USER`; personas 2/5 usan propiedades `SPRING_DATASOURCE_*`. El lanzador adapta estas convenciones. Persona 5 también busca `.env` en carpetas ascendentes; persona 4 no usa ese cargador.

Los principales destinos independientes son Autenticación 5158, Bitácora 5159, Parámetro 5161, Matrícula 8085, Notas 8092 y Dirección 5227. Arrancar directamente desde un IDE requiere mantener la misma configuración efectiva.

## Pruebas y evidencia existentes

Las fuentes de pruebas están en los proyectos agrupados: seguridad .NET y `services/persona{2,4,5}/src/test`. No se encontraron fuentes de pruebas propias dentro de las aplicaciones independientes.

Los reportes XML existentes reflejan 12 pruebas para persona 2, 251 para persona 4 y 16 para persona 5, sin fallos registrados. La auditoría 4 registra adicionalmente 5 pruebas de seguridad y cuatro compilaciones .NET. Estas cifras son históricas y corresponden a los proyectos/ejecuciones originales.

Comparando las fuentes Java de ambas estructuras e ignorando espacio al principio/final, gran parte de la lógica se conserva. Los clientes de Seguridad cambian al separar Bitácora/Parámetro; los clientes académicos separan Matrícula/Notas. Precisamente esos cambios de integración necesitan cobertura actualizada.

Las colecciones están en `docs/pruebas/postman`; capturas y resultados están repartidos entre `docs/evidencia` y `docs/pruebas/evidencias`. El manifiesto de Fabián declara alcance parcial. Una colección preparada o una captura histórica no demuestra todos los criterios contra la arquitectura actual.

## Hallazgos confirmados por inspección

| Prioridad | Hallazgo | Fuente / efecto |
|---|---|---|
| Alta | Periodo y Dirección registran middleware sin `Invoke`/`InvokeAsync` | `NuevoAvatar.Periodo/GrupoAuthorizationMiddleware.cs` y equivalente en Dirección contienen clases vacías. Ambos Program registran ese middleware. La auditoría 4 documentó el fallo de arranque; el defecto sigue visible. |
| Alta | Persona 3 conserva el destino anterior de bitácora | Los cuatro `Services/SeguridadService.cs` envían `/validate` y `/bitacora` con el mismo HttpClient. `Seguridad:BaseUrl` apunta a 5158 y Autenticación ya no expone `/bitacora`. Con los valores actuales, la llamada de auditoría apunta a una ruta inexistente. |
| Alta | Bitácoras incompletas en personas 2/5 y 3 | Los filtros Java registran request, pero no el estado anterior en PUT ni el eliminado en DELETE; Curso registra método/ruta y Grupo/Periodo describen acciones/IDs. No satisface todo el contenido pedido en PDF pp. 26–27. |
| Alta | Pruebas permanecen fuera de las aplicaciones independientes | Ejecutar la suite agrupada no comprueba el nuevo cableado ni los nuevos puntos de entrada. |
| Media | Política diferente ante fallos de auditoría | Personas 2/5 registran el fallo y continúan. Persona 4 propaga 503 y sus escrituras llaman a GEN1 antes del commit local; esto no constituye una transacción distribuida. Los clientes de persona 3 ni siquiera comprueban el estado de la respuesta de bitácora. |
| Media | Matrícula no valida existencia del estudiante antes de guardar | `MatriculaService.validar` verifica oferta y duplicados, pero no expediente. Su handler general convierte una posible excepción de integridad en 500. |
| Media | Periodo no devuelve su identificador en consultas | `PeriodoRepository` selecciona datos hacia `PeriodoRequest`, sin `PeriodoId`, lo que dificulta usar el resultado en grupos/matrículas. |
| Media | Duraciones de refresh/JWT sin validación conjunta | `AuthService` valida positividad de cada parámetro, pero no que refresh dure más que JWT, como pide USR5. |
| Media | Contrato de Factura distinto del PDF | El request recibe `matriculaId`; el requisito describe identificación y monto. El cálculo implementado sí coincide con 30000 + 2%. |
| Media | Ambigüedad del periodo activo en el propio PDF | La página 18 dice activo y fecha de inicio posterior a la actual; el código implementa un periodo en curso. Requiere una decisión funcional explícita. |
| Media | Documentación no refleja completamente la implementación | Total incorrecto de HU, stack propuesto diferente, dos generaciones de proyectos y evidencia dispersa. Doc Master tiene cero dibujos embebidos; el diagrama de BD sí existe externamente. |
| Media | Restos de integración en el cliente académico | `MatriculaNotasClient.obtenerDesglose` todavía usa el cliente de Matrícula para `/obtenerdesglose`, ruta ahora de Notas. No se observó su invocación en el flujo de historial revisado. |

Los servicios .NET de seguridad validan JWT localmente mediante `IAuthService` y escriben bitácora mediante la biblioteca compartida. Conviene contrastar esa decisión con la exigencia literal de consumir `/validate` y GEN1; separar procesos no cambió esos accesos por sí solo.

## Aspectos que requieren ejecución o decisión funcional

No se comprobó concurrencia en pago/factura ni al reemplazar rubros. Las transacciones locales y verificaciones previas no demuestran por sí mismas que dos peticiones simultáneas mantengan las reglas.

Historial y consultas de notas trabajan con matrículas activas. Debe definirse cómo se representan anulaciones, repeticiones e intentos anteriores. Cambiar esa política afecta tanto MAT5 como ACA1.

La imagen de BD encontrada actualiza el hallazgo histórico de diagrama ausente. Falta comprobar su incorporación al entregable, los diagramas de clases/casos de uso y la matriz final por criterio. El tablero y PR finales no se inspeccionaron en servicios externos.

El repositorio ya tenía cambios locales en binarios de `services/persona4/target` y el diagrama sin seguimiento en Git. Se preservaron esos archivos. `.gitignore` ignora outputs, pero eso no elimina el seguimiento previo de binarios ya versionados.

## Guía para los próximos cambios

1. Aplicar cambios funcionales en las aplicaciones independientes que usan los lanzadores. Revisar expresamente si la versión agrupada debe mantenerse y cómo se trasladarán sus pruebas.
2. Estabilizar Periodo/Dirección y separar correctamente los destinos HTTP de persona 3.
3. Probar el cableado real de Autenticación, Bitácora y Parámetro antes de un recorrido académico completo.
4. Definir las decisiones pendientes de periodo activo, contrato de factura e historial antes de modificar sus reglas.
5. Verificar con datos dedicados: oferta → expediente → prematrícula; expediente/oferta → matrícula → notas → historial/listado; matrícula → factura → pago/reverso. La prematrícula no es un paso técnico obligatorio de MAT2 en el código actual.
6. Unificar el contrato de bitácora y trasladar cobertura a los servicios separados, incluyendo errores de dependencias.
7. Cerrar guía de instalación SQL, matriz criterio/evidencia y documentación final con el modelo realmente implementado.

Este mapa sirve como referencia de arquitectura y estado del código; no es una certificación de que todos los servicios actuales compilan, arrancan o cumplen cada criterio de aceptación.
