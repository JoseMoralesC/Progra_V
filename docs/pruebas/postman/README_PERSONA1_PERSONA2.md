# Postman: Persona 1 y Persona 2 en PrograV

Las dos APIs deben conectarse a la base `PrograV` por Tailscale. Postman consume las APIs HTTP; no se conecta directamente a SQL Server.

## Preparación

1. Activar Tailscale y confirmar acceso al servidor SQL del equipo.
2. Configurar `SeguridadDb` en el archivo local `services/seguridad/NuevoAvatarSeguridad/appsettings.Development.json` para usar `PrograV`. Las credenciales quedan fuera de Git.
3. Levantar Seguridad desde su carpeta con `dotnet run --launch-profile http` (puerto 5158).
4. Desde `services/persona2`, ejecutar `./run-prograv.ps1` (puerto 8082). Este script usa la misma conexión local de Seguridad.
5. Confirmar que existan el rol `ESTUDIANTE` y el parámetro `DOMPROF`, cuyo valor debe coincidir con `dominioProfesor` del entorno. Si falta el parámetro, acordar con el equipo la carga de los datos iniciales de `db/persona2/sql/separado/04_datos_iniciales.sql` antes de probar profesores.

## Importar y ejecutar

Importar estos tres archivos en Postman:

- `Persona1_Seguridad.postman_collection.json`: 43 peticiones para USR2, USR3, USR4, USR5 y GEN1.
- `Persona2_Ramses.postman_collection.json`: 56 peticiones para USR1, ACD1, ACD6 y ACD2, incluyendo seguridad y bitácora.
- `Persona1_Persona2.postman_environment.json`: entorno con URLs locales y credenciales vacías.

Seleccionar el entorno `Persona1_Persona2.local`. Completar `usuario` y `password` con las credenciales de una cuenta de la aplicación en PrograV. La clave del login de la aplicación es distinta de la clave de conexión SQL.

Ejecutar cada colección en el Runner, en orden, con una iteración. La primera petición hace login y guarda automáticamente `token` y `refresh_token`. Si vence el JWT durante una ejecución manual, repetir el login y luego la petición pendiente; reiniciar toda la colección genera nuevos identificadores de prueba.

Las pruebas CRUD crean registros ficticios identificados por el número de ejecución. La limpieza utiliza solo los identificadores devueltos por las altas exitosas. Mantener los valores automáticos de esos identificadores. Persona 2 elimina primero carrera, luego profesor, institución y usuario ficticio. Si se interrumpe la ejecución, conservar las variables y ejecutar la carpeta de limpieza al terminar. Las bitácoras y sesiones de autenticación permanecen como evidencia de las operaciones.

## Guardar evidencia en docs

Crear una carpeta `docs/pruebas/evidencias/AAAA-MM-DD/persona2-postman/` y guardar:

- Capturas con nombre de petición, método, URL, código HTTP y respuesta.
- Resumen del Runner con peticiones aprobadas y fallidas.
- Un resumen por HU indicando resultado, archivo de evidencia y observaciones.

Para USR1 cubrir alta, consulta, filtros, actualización, duplicado, campos vacíos, dominio/rol y eliminación. Para ACD1 cubrir CRUD y validaciones del nombre. Para ACD6 cubrir CRUD, teléfonos, mayoría de edad, dominio y duplicados. Para ACD2 cubrir CRUD, filtro por institución, referencias inexistentes y conflictos al eliminar registros relacionados. Para GEN1 revisar también el texto de las bitácoras: alta con registro nuevo, actualización con anterior/actual y eliminación con registro eliminado. Estos controles manuales complementan las aserciones del Runner; la colección por sí sola no certifica todos los criterios del PDF.

Ocultar encabezados de contraseña y Authorization, tokens, claves SQL y datos personales antes de guardar capturas o exportaciones en Git. Conservar el entorno versionado con los secretos vacíos.

## Estado de verificación

El 5 de octubre de 2026 se comprobó el arranque de Persona 2 con `ddl-auto=validate` contra `PrograV`, login HTTP en Seguridad y consultas autenticadas de usuario, institución, profesor y carrera. Ver `../evidencias/2026-10-05/persona2-prograv/verificacion-http.json`.

La consulta inicial de `DOMPROF` respondió 404. Durante las pruebas de CRUD se creó mediante la API con valor `cuc.ac.cr`, conforme al script de datos iniciales; ahora está configurado en PrograV.

Las dos colecciones se ejecutaron con `ejecutar-http.cjs` y después en la aplicación Postman 12.30.5 mediante Runner local: 99 peticiones y 128 aserciones aprobadas (56 de Persona 1 y 72 de Persona 2), cero fallos, errores y omisiones. Ver [resumen y capturas del Runner](../RESUMEN_PERSONA2.md). La prueba de token inválido genera un UUID aleatorio; no contiene un token fijo ni una credencial real. Los valores de usuario y contraseña se mantienen locales: no pulsar Share ni exportarlos al repositorio.

Para repetir con el ejecutor HTTP local, configurar `POSTMAN_USER`, `POSTMAN_PASSWORD` y `HTTP_REPORT_PATH` en el ambiente local y ejecutar `node docs/pruebas/postman/ejecutar-http.cjs` desde la raíz. El reporte omite credenciales, tokens y respuestas completas. El ejecutor conserva DOMPROF si existe y lo crea con el dominio del entorno únicamente si falta. Solo admite los métodos de aserción utilizados por estas dos colecciones; no reemplaza el runtime general de Postman.
