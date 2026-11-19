# Renovación de datos universitarios

> Estado al 13 de noviembre de 2026: las pruebas automatizadas y sus ejecutores fueron retirados. Las referencias a archivos de pruebas y comandos de ejecucion de este documento son historicas; los resultados y capturas se conservan como evidencia. El detalle de los retiros esta en `docs/mapeo-pruebas/limpieza-realizada.csv`.


La carga sustituye los registros identificados como ensayos en las bases de los servicios y en la base histórica `PrograV`. Conserva el esquema, los logins SQL, los parámetros técnicos y los permisos ya asignados. Los registros generados son sintéticos; sus nombres, cédulas y contactos no constituyen identidades verificadas. Los datos personales plausibles de dos integrantes se conservan al reemplazar sus cuentas anteriores por las solicitadas.

## Resultado y evidencias

- [Resumen por tabla](resumen-tablas.md): registros eliminados, creados y actualizados, separados por base.
- [Resultado de la transacción](resultado.json): respaldos, cambios y comprobaciones de integridad.
- [Verificación independiente y funcional](verificacion.json): relaciones, ocho accesos, permisos y filtrado geográfico.
- [Inconsistencias anteriores](inconsistencias-previas.json): dos carreras referían profesores inexistentes.
- [Contenidos académicos](contenidos-academicos.md): las ocho carreras con 24 materias y sus contenidos sintéticos.

Las bases de los servicios contienen 240 estudiantes, 32 profesores, cuatro cuentas administrativas, ocho carreras, 192 cursos, 576 grupos, tres períodos, 1.920 matrículas, 82 prematrículas, 1.728 rubros, 3.800 calificaciones, 1.920 facturas y 1.600 pagos. `PrograV` recibe la misma población académica y financiera; no se deben sumar ambas copias para contar personas.

Los períodos son enero–abril de 2026, septiembre–diciembre de 2026 y enero–abril de 2027. Cada estudiante tiene cuatro matrículas históricas y cuatro actuales; 40 matrículas actuales están anuladas. Los grupos tienen cupo para 35 personas y atienden cohortes de 30. Los horarios de las materias de cada cohorte y profesor no se traslapan. Los rubros suman 100%; las notas corresponden al grupo de la matrícula. Las facturas pagadas tienen pagos por su total, las pendientes no tienen pagos y las matrículas anuladas tienen su factura anulada.

Las cuatro cuentas estudiantiles del equipo tienen expediente, teléfonos, matrículas, notas y prematrícula. Las cuatro cuentas administrativas son independientes. Las contraseñas se generan con aleatoriedad criptográfica y se almacenan en SQL únicamente como BCrypt, compatible con el mecanismo existente. El usuario corto se resuelve mediante el dominio `@cuc.cr`, según la autenticación existente.

## Conservación y correcciones

Se identificaron las raíces artificiales por marcas de ensayo, nombres genéricos y sus dependencias. El plan conserva un inventario completo cifrado. Una fila no clasificada detiene la herramienta; no se presupone que cualquier registro existente puede borrarse.

Se reemplazaron las carreras que referían profesores inexistentes, los períodos incoherentes y la geografía ficticia. Se preservaron los nombres e identificaciones anteriores de Ramsés y Alejandro. No se inventaron apellidos para completar el nombre parcial de Alejandro. Los identificadores nuevos son ficticios, tienen nueve dígitos y comienzan entre 1 y 7.

Se conservan las definiciones de roles y sus asignaciones anteriores. Se agrega `PROFESOR` con acceso a inicio, notas y perfil. `DOMPROF` se normaliza a `cuc.cr`; las credenciales y direcciones de infraestructura SMTP se conservan. Las cuentas SQL no son las cuentas de aplicación y no se alteran.

Se eliminan las sesiones anteriores de las cuentas sustituidas y las bitácoras/notificaciones con marcas explícitas de ensayo. Las demás bitácoras se conservan. Los ensayos HTTP de verificación generan nuevas sesiones y bitácoras legítimas, por lo que sus recuentos posteriores pueden aumentar. No se envían correos durante esta tarea.

El modelo de curso no tiene una columna de contenidos. Por ello, la guía académica contiene esa información sin introducir cambios de esquema. Los archivos históricos de evidencia y los fixtures de las pruebas del repositorio no se eliminan: la limpieza corresponde a los registros de las bases.

## Geografía

El catálogo incluye siete provincias, 84 cantones y 494 distritos, con sus códigos oficiales. La fuente es la [tabla territorial 2026 del Registro Nacional](https://www.snitcr.go.cr/pdfs/ign_repositorio/DTA-TABLA%20POR%20PROVINCIA-CANT%C3%93N-DISTRITO%202026.pdf). El PDF declara 494 distritos pero enumera 493; Duacarí, código 70605, se completa con la [división territorial oficial de 2025](https://www.pgrweb.go.cr/DOCS/NORMAS/1/VIGENTE/D/2020-2029/2025-2029/2025/1953C/17275C.HTML). Se normaliza el acento de Puerto Jiménez. Los cantones por provincia son 20, 16, 8, 10, 11, 13 y 6.

La consulta existente de Dirección ya filtra `ProvinciaId`; se comprueba su respuesta HTTP para cada provincia. No fue necesario cambiar la lógica del servicio.

## Respaldos y recuperación

Antes de modificar filas se crearon 23 respaldos nativos `COPY_ONLY` con `CHECKSUM`, uno por base. Todos pasaron `RESTORE VERIFYONLY WITH CHECKSUM`. Las rutas del servidor están en `resultado.json`; no son rutas del equipo cliente. `VERIFYONLY` comprueba la legibilidad del respaldo; no equivale a haber ensayado una restauración completa.

El plan, su inventario y las ocho claves iniciales se guardan cifrados en `tmp/limpieza-universitaria/plan-cifrado.bin`. La clave de cifrado se guarda separada en `clave-local.key`. La carpeta tiene herencia de permisos deshabilitada y acceso para el usuario local y SYSTEM. Todo queda excluido de Git; los informes versionables no contienen hashes de usuarios, contraseñas, tokens ni configuración SMTP.

Para recuperar el estado anterior, detener escrituras de las aplicaciones y restaurar **el conjunto de las 23 bases** usando las rutas registradas, con `RESTORE FILELISTONLY` para determinar archivos y `MOVE` cuando corresponda. Restaurar solo una base puede romper referencias entre servicios. No se ejecuta una restauración automática ni se sobrescriben bases durante la verificación.

## Herramientas

```powershell
# Antes de una transición compatible con el inventario auditado:
python tools/renovar-datos-universitarios.py --respaldar

# Revisa el plan público y luego aplica exactamente el plan cifrado:
python tools/renovar-datos-universitarios.py --aplicar

# Comprobación SQL independiente:
python tools/verificar-datos-universitarios.py

# Con los servicios y Gateway iniciados contra las bases remotas:
python tools/verificar-datos-universitarios.py --funcional
```

La renovación es una transición controlada del inventario original, no un reinicio automático ni una sincronización periódica. Después de esta carga, un plan nuevo rechaza las raíces académicas ya renovadas. `--aplicar` también rechaza cualquier cambio del inventario respecto del plan cifrado. No debe reutilizarse para borrar datos nuevos del equipo.

La escritura usa una única transacción ODBC entre bases, bloqueos exclusivos y `XACT_ABORT`. Se comprueba `@@TRANCOUNT` para evitar transacciones anidadas. La carga solo se declara confirmada después del commit y de una segunda validación desde otra conexión. No se deshabilitan restricciones ni se reinician identidades.

Las herramientas usan las conexiones del `.env` a la instancia remota. Los lanzadores opcionales de presentación incluyen alternativas locales; las instancias locales consultadas no fueron accesibles y no se modificaron. La validación funcional usa las bases remotas del manifiesto de servicios.
